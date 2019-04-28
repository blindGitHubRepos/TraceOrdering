package com.controller; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.microprofile.metrics.Timer;
import org.eclipse.uml2.uml.State;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.StringUtils;

import ca.queensu.cs.antler4AC.ACLexer;
import ca.queensu.cs.antler4AC.ACParser;
import ca.queensu.cs.antler4AC.EvalVisitor;
import ca.queensu.cs.antler4AC.SendMessage;
import ca.queensu.cs.antler4AC.Value;
import ca.queensu.cs.graph.ViewEngine;
import ca.queensu.cs.server.Event;
import ca.queensu.cs.server.ServerVTOrdering;
import ca.queensu.cs.umlrtParser.CapsuleConn;
import ca.queensu.cs.umlrtParser.MyConnector;
import ca.queensu.cs.umlrtParser.PES;
import ca.queensu.cs.umlrtParser.ParserEngine;
import ca.queensu.cs.umlrtParser.StateData;
import ca.queensu.cs.umlrtParser.TableDataMember;
import ca.queensu.cs.umlrtParser.TransitionData;
import ca.queensu.cs.umlrtParser.UmlrtParser;
import ca.queensu.cs.umlrtParser.UmlrtUtils;


public class CapsuleTracker implements Runnable{

	private Semaphore semCapsuleTracker;
	private PriorityBlockingQueue <Event> eventQueueTmp;
	private String currentStatus; //{StartUp, INIT, TRANSITIONEND}
	public String previousState;
	public String transitionName;
	public DataContainer dataContainer;
	private StateData sourceStateData;
	private StateData targetStateData;
	private boolean targetChoiceState;
	private boolean sourceChoiceState;
	private OutputStream outputFileStream;
	private double timeMilli;
	private String outputStrTofile;
	private long offset;
	private Timer timer;
	private int[] logicalVectorTime;
	private int TrackerMakerNumber;
	private Event stateExitEvent;
	private double stateExitTimer =0;
	private static String senderCapInstanceName;
	private String currentStateToChiceState;
	private String currentTransitionToFromChiceState;
	private List<String> listPaths;
	private String currentCapsuleRegion;
	private String prvTookPath;
	private boolean startUpDone;
	private boolean initDone;
	private Map<String,List<String>> mapPathTrigger;
	private Map<String,List<String>> mapPathGuards;
	private Map<String,List<String>> mapPathActionCode;
	private Map<String, Value> maplocalHeap;
    private List<SendMessage> listPortMsg;
    private List<String> listConsumedPaths;
    private List<String> listAllPathTaken;
    private String lastTookPath;
    private String msgTobeConsumed;
    private String msgSender;
    private boolean msgConsumedTmpQueue;
    private boolean msgConsumedQueue;



	public CapsuleTracker(Semaphore semCapsuleTracker, OutputStream outputFileStream, int[] logicalVectorTime, DataContainer dataContainer) {
		this.msgConsumedQueue = false;
		this.msgConsumedTmpQueue = false;
		this.msgTobeConsumed = "";
		this.maplocalHeap = new HashMap<String, Value>();
		this.listConsumedPaths = new ArrayList<String>();
		this.listPortMsg = new ArrayList<SendMessage>();
		this.mapPathGuards =  new HashMap<String,List<String>>();
		this.mapPathTrigger =  new HashMap<String,List<String>>();
		this.mapPathActionCode =  new HashMap<String,List<String>>();
		this.listAllPathTaken =  new ArrayList<String>();
		this.listPaths =  new ArrayList<String>();
		this.currentStateToChiceState ="";
		this.currentTransitionToFromChiceState = "";
		this.dataContainer = dataContainer;
		this.senderCapInstanceName = "";
		this.stateExitEvent = null;
		this.TrackerMakerNumber = 0;
		this.semCapsuleTracker = semCapsuleTracker;
		//this.capsuleInstance = capsuleInstance;
		this.outputFileStream = outputFileStream;
		this.logicalVectorTime = logicalVectorTime;
		this.eventQueueTmp = new PriorityBlockingQueue<Event>(); // read but not consume!
		this.currentStatus = "StartUp";
		//this.currentStateName = "INIT";
		//this.currentStateId = "";
		this.targetStateData = new StateData();
		this.sourceStateData = new StateData();
		this.targetChoiceState = false;
		this.sourceChoiceState = false;
		this.timeMilli = 0;
		this.outputStrTofile = "";
		this.timer = new TimerImpl();
		this.currentCapsuleRegion = "INIT";
		this.prvTookPath = "";
		this.initDone = false;
		this.startUpDone = false;
		this.lastTookPath = "";
		this.msgSender = "";
	}


	public void run() {
		try {
			semCapsuleTracker.acquire();
			System.out.println("============> Running : "+ dataContainer.getCapsuleName() );

			List<String> listAttributes = ParserEngine.mapCapAttributes.get(dataContainer.getCapsuleName());

			if (listAttributes != null) {
				for (String att : listAttributes) {
					String [] attSplit = att.split(":");
					if (attSplit[1].contentEquals("String"))
						maplocalHeap.put(attSplit[0], new Value("", "String"));
					else if (attSplit[1].contentEquals("Integer"))
						maplocalHeap.put(attSplit[0], new Value(Double.valueOf(0), "Integer"));
					else if (attSplit[1].contentEquals("Real"))
						maplocalHeap.put(attSplit[0], new Value(Double.valueOf(0), "Double"));
					else if (attSplit[1].contentEquals("Boolean"))
						maplocalHeap.put(attSplit[0], new Value(false, "Boolean"));
					else {
						System.err.println("__________ The Tyep of Attribute Not supported! __________");
						System.exit(1);
					}
				}
			}
			semCapsuleTracker.release();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(true) {
			try {
				semCapsuleTracker.acquire();

				if (!eventQueueTmp.isEmpty() || !dataContainer.getEventQueue().isEmpty()) {
					Event currentEventTmp = new Event();
					Event currentEvent = new Event();
					
					if(!dataContainer.getEventQueue().isEmpty()) {
						currentEvent =  dataContainer.eventQueue.poll(); 
					


							if (isConsumable(currentEvent) || (listPaths.size()>1)) {
								listConsumedPaths.add(PES.mapQnameId.get(currentEvent.getSourceName()));

								if (currentStatus.contentEquals("TRANISTIONEND")) {
									if ((listPaths.size() == 1) && isRequirementMet(listPaths.get(0)) && pathCompeleted(listPaths.get(0))) {
										
										listConsumedPaths.clear();
										listAllPathTaken.add(listPaths.get(0));
										addToListConsumedPaths(listPaths.get(0));
										updateLocalHeap(listPaths.get(0),msgSender);
										updateCurrentState();
										if (!msgTobeConsumed.isEmpty())
											dataContainer.mapSendMessages.remove(msgTobeConsumed);
										msgTobeConsumed = "";
										msgSender = "";
										listPaths.clear();
										msgConsumedQueue = true;
									}


								}

							}else {eventQueueTmp.add(currentEvent);}
						
					}
					
					if (!eventQueueTmp.isEmpty()  && !msgConsumedQueue) {
						int eventQueueTmpSize = eventQueueTmp.size();

							currentEventTmp = eventQueueTmp.poll();


							if (currentEventTmp.getSourceName().contains("Server1Failure") || currentEventTmp.getSourceName().contains("Server2Failure")) {
								

							}


								if (isConsumable(currentEventTmp) || (listPaths.size()>1)) {
									listConsumedPaths.add(PES.mapQnameId.get(currentEventTmp.getSourceName()));

									if (currentStatus.contentEquals("TRANISTIONEND")) {
										if ((listPaths.size() == 1) && isRequirementMet(listPaths.get(0)) && pathCompeleted(listPaths.get(0))) {
											listConsumedPaths.clear();
											listAllPathTaken.add(listPaths.get(0));
											addToListConsumedPaths(listPaths.get(0));
											updateLocalHeap(listPaths.get(0),msgSender);
											updateCurrentState();
											if (!msgTobeConsumed.isEmpty())
												dataContainer.mapSendMessages.remove(msgTobeConsumed);
											msgTobeConsumed = "";
											msgSender = "";
											listPaths.clear();
											msgConsumedTmpQueue = true;

										}
										
									}
 queue 
								}else {eventQueueTmp.add(currentEventTmp);}
							
					}

					if (msgConsumedTmpQueue && currentEventTmp != null && currentEventTmp.getSourceName() != null) {

						showInfo(currentEventTmp);
						currentEventTmp = null;
					}else if (msgConsumedQueue && currentEvent != null && currentEvent.getSourceName() !=null) {

						showInfo(currentEvent);
						currentEvent = null;
					}
					
					msgConsumedTmpQueue = false;
					msgConsumedQueue = false;

					
				}
				semCapsuleTracker.release();
				Thread.yield();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public boolean pathCompeleted(String path) {
		if (path.contains(",")) {
			String [] pathsSplit = path.split("\\,");
			for (String tr : pathsSplit) {
				if (!listConsumedPaths.contains(tr))
					return false;
			}
		}
		return true;
	}

	//==================================================================	
	//====================================================[addToListConsumedPaths]
	//==================================================================	
	public String separateTopFromInstanceName(String top__instanceName) {
		if (top__instanceName.contains("__")) {
			int idx = top__instanceName.indexOf("__");
			return top__instanceName.substring(idx+2);
		}else {
			System.err.println("__________ bad instanceName __________");
			System.exit(1);
		}
		return null;
	}

	//==================================================================	
	//====================================================[addToListConsumedPaths]
	//==================================================================	
	public void addToListConsumedPaths(String path) {
		if (path.contains(","))
			listConsumedPaths.add(path);
	}
	
	//==================================================================	
	//====================================================[isPassedEvent]
	//==================================================================	
	public boolean isPassedEvent(Event event , String queueName) {
		String id = PES.mapQnameId.get(event.getSourceName());
		//if(!prvTookPath.isEmpty() && prvTookPath.contains(",") && prvTookPath.contains(id)) {
		//	return true;
		//}
		
		for (String path : listConsumedPaths) {
			String lastTr = "";
			if(path.contains(",")) {
				String [] pathsSplit = path.split("\\,");
				lastTr = pathsSplit[pathsSplit.length-1];
			}
			if (lastTr.contentEquals(id)) {
				System.err.println(dataContainer.getCapsuleInstance()+ " from: "+queueName + " , lastTr.contentEquals(id):"+event.allDataToString());
				if (queueName.contentEquals("eventQueue"))
					 listConsumedPaths.remove(path);
				return true;
			}
		}
		
		return false;
	}
	
	//==================================================================	
	//====================================================[vTimeHandler]
	//==================================================================
	public void vTimeHandler(Event event) {
		if (currentStatus.contains("TRANISTIONEND")) { // current event is stateExit => save event and do not put timestamp before transition done
			stateExitEvent = event;
			stateExitTimer = ((TimerImpl) timer).nowNano();
		}else if ((currentStatus.contains("STATEENTRYEND")) && (stateExitEvent != null)) { // current event is transition => put timestamp for stateExit and transition
			logicalVectorTime[TrackerMakerNumber]--;
			outputStrTofile = "Vector Time: "+Arrays.toString(logicalVectorTime)+/*"-["+String.format("%f", stateExitTimer)+"]: "+*/stateExitEvent.allDataToString()+"\n\n";
			outputStreamToFile(this.outputFileStream,outputStrTofile);
			logicalVectorTime[TrackerMakerNumber]++;
			outputStrTofile = "Vector Time: "+Arrays.toString(logicalVectorTime)+/*"-["+String.format("%f", ((TimerImpl) timer).nowNano())+"]: "+*/event.allDataToString()+"\n\n";
			outputStreamToFile(this.outputFileStream,outputStrTofile);
			stateExitEvent = null;
			stateExitTimer = 0;
		}else {
			outputStrTofile = "Vector Time: "+Arrays.toString(logicalVectorTime)+/*"-["+String.format("%f", ((TimerImpl) timer).nowNano())+"]: "+*/event.allDataToString()+"\n\n";
			outputStreamToFile(this.outputFileStream,outputStrTofile);
		}
	}
	
	//==================================================================	
	//==============================================[callSendJsonToServer]
	//==================================================================
	public static boolean callSendJsonToServer(int priorityEventCounter, String capsuleInstance, String itemName, String allVariables) throws Exception {
		if (isPortInUse("localhost",8090)) { //8090 used to send command to the local draw.io server
			try {
	
				JSONObject jsonObj = makeJSONobj(priorityEventCounter,capsuleInstance, itemName, allVariables);
				ViewEngine.sendJsonToServer(jsonObj.toJSONString());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TrackerMaker.priorityEventCounter++;
			return true;
		}
		return false;
	}

	//==================================================================	
	//==============================================[isConsumable]
	//==================================================================
	public boolean isConsumable(Event event) throws InterruptedException {
		try {
			String capsuleInstance = dataContainer.getCapsuleInstance();
		switch (currentStatus) {
			case "StartUp":          	if (registerChecking(event))       {
				System.out.println(">>>>>>>>>>>>>>>["+ Thread.currentThread().getName() +"]--> ["+capsuleInstance+"]: StartUp received!");
				startUpDone = true;
				return true;};
				break;
			
			case "INIT":  	      		if (initChecking(event))           {
				System.out.println(">>>>>>>>>>>>>>>["+ Thread.currentThread().getName() +"]--> ["+capsuleInstance+"]: Init received!");
				initDone = true;
				return true;};
				break;
				
			case "TRANISTIONEND":     	if (transitionChecking(event))     {
				return true;};
				break;
		}

		return false;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return false;
			
		}
	}
	
	
	//==================================================================	
	//==============================================[showInfo]
	//==================================================================
	public void showInfo(Event event) {
		if (event !=null)
			System.out.println("=====================[event]: " + event.allDataToString());
		else
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<[No Event Selected]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("=====================[listAllPathTaken]");
		int i = 0;
		
		for (String path : listAllPathTaken) {
			String pathCurr = "";
			if(path.contains(",")) {
				String [] pathsSplit = path.split("\\,");
				pathCurr = "";
				for (String str : pathsSplit) {
					if (pathCurr.isEmpty()) {
						pathCurr = PES.mapIdQname.get(str);
						//pathCurr = str;
					}else {	
						pathCurr = pathCurr+ "-->" + PES.mapIdQname.get(str);
						//pathCurr = pathCurr+ "-->" + str;
					}
				}
			}else {
				pathCurr = PES.mapIdQname.get(path);
				//pathCurr = listRegionalPaths.get(i);
			}
			System.out.println("["+i+++"]:" +pathCurr);
		}

		System.out.println("=====================[listPaths]");
		i = 0;
		for (String path : listPaths) {
			String pathCurr = "";
			if(path.contains(",")) {
				String [] pathsSplit = path.split("\\,");
				pathCurr = "";
				for (String str : pathsSplit) {
					if (pathCurr.isEmpty()) {
						pathCurr = PES.mapIdQname.get(str);
						//pathCurr = str;
					}else {	
						pathCurr = pathCurr+ "-->" + PES.mapIdQname.get(str);
						//pathCurr = pathCurr+ "-->" + str;
					}
				}
			}else {
				pathCurr = PES.mapIdQname.get(path);
				//pathCurr = listRegionalPaths.get(i);
			}
			System.out.println("["+i+++"]:" +pathCurr);
		}
		System.out.println("=====================[currentState]");
		for (Entry<String, String> entry : dataContainer.mapRegionCurrentState.entrySet()) {
			System.out.println("key: " + entry.getKey() + ", value: "+ ParserEngine.mapStateData.get(entry.getValue()).getStateName());
		}
		System.out.println("=====================[mapLocalHeap]");
		for (Entry<String, Value> entry : maplocalHeap.entrySet()) {
			System.out.println("key: " + entry.getKey() + ", value: "+ entry.getValue());
		}
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<[DONE]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
	}
	
	//==================================================================	
	//==============================================[updateCurrentState]
	//==================================================================		
	public boolean updateCurrentState () {
		String [] pathSplit;
		String lastId = "";
		String region = "";
		String region_begin = "";
		String region_begin_currentState = "";
		boolean firstTrTook = false;
		boolean trTookIntoCS = false;
		String trPrv = "";

		
		if (listPaths.size()==1) {
			String path = listPaths.get(0);
			prvTookPath = path;

			if(path.contains(",")) {
				pathSplit = path.split("\\,");
				lastId = pathSplit[pathSplit.length -1];
				for(String tr : pathSplit) {
					if (!firstTrTook) {
						region_begin = ParserEngine.mapTransitionData.get(tr).getReginName();
						region_begin_currentState = dataContainer.mapRegionCurrentState.get(region_begin);
						firstTrTook = true;
					}
					region = ParserEngine.mapTransitionData.get(tr).getReginName();
					String [] trSplit = ParserEngine.mapTransitionData.get(tr).getPath().split("\\-");
					dataContainer.mapRegionCurrentState.put(region, trSplit[2]);

					if (region.contains(region_begin+"_")) { 
						String [] trPrvSplit = ParserEngine.mapTransitionData.get(trPrv).getPath().split("\\-");
						region_begin_currentState = trPrvSplit[2];
						trTookIntoCS = true;
					}
					trPrv = tr;
				}
			}else {
				lastId = path;
				region = ParserEngine.mapTransitionData.get(lastId).getReginName();
				String [] trSplit = ParserEngine.mapTransitionData.get(lastId).getPath().split("\\-");
				dataContainer.mapRegionCurrentState.put(region, trSplit[2]);	
				}
			
			if ( (trTookIntoCS || firstTrTook) && !region.contentEquals(region_begin)) {
				dataContainer.mapRegionCurrentState.put(region_begin, region_begin_currentState);
			}
			
			return true;
		}else {
			showInfo(null);
		}
		return false;
	}

	//==================================================================	
	//======================================================[checkPathConsistency]
	//==================================================================		
	public boolean checkPathConsistency (String region, String path) {
		String firstId = "";
		if (path.contains(",")) {
			String [] pathSplit = path.split("\\,");
			firstId = pathSplit[0];
		}else {
			firstId = path;}
		String [] stateIds = ParserEngine.mapTransitionData.get(firstId).getPath().split("\\-");		
		
		if (((dataContainer.mapRegionCurrentState.get(region) != null) && dataContainer.mapRegionCurrentState.get(region).contentEquals(stateIds[0])) || currentStatus.contentEquals("INIT"))
			return true;
		else 
			return false;
	}

	//==================================================================	
	//======================================================[pathFinder]
	//==================================================================		
	public List<String> pathFinder (String qName) {

		String id = PES.mapQnameId.get(qName);
		String region = ParserEngine.mapTransitionData.get(id).getReginName();
		String capInstance = ParserEngine.mapTransitionData.get(id).getCapsuleInstanceName();
		//System.err.println(qName + ", capInstance is : "+capInstance);

		
		if (listPaths.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : PES.mapRegionPaths.entrySet()) {
				if (entry.getKey().contains(capInstance+"::"+region)) {
					for (String path : entry.getValue()) {
						if (path.contains(id) && checkPathConsistency(region, path)) {
							listPaths.add(path);
						}
					}
					break;
				}
			}
		}else {
			List <String> listPathsTmp =  new ArrayList<String>();
			listPathsTmp.clear();
			
			for (String path : listPaths) {
				if (path.contains(id)){
					listPathsTmp.add(path);
				}
			}
			
			listPaths.clear();
			listPaths.addAll(listPathsTmp);
			
			if (listPaths.size() == 2) {//add all path from history into listPath
				listPathsTmp.clear();
				for (String path : listPaths) {
					String lastId = "";
					if (path.contains(",")) {
						String [] pathSplit = path.split("\\,");
						lastId = pathSplit[pathSplit.length -1];
					}else {
						lastId = path;
					}
					region = ParserEngine.mapTransitionData.get(lastId).getReginName();
					capInstance = ParserEngine.mapTransitionData.get(lastId).getCapsuleInstanceName();
					
					String currentState = dataContainer.mapRegionCurrentState.get(region);
					if(ParserEngine.mapTransitionData.get(lastId).getIsInit()) {
						if((currentState != null) && !currentState.contains("INIT")) {
							listPathsTmp.addAll(addAllPathFrom(currentState));
						}else {
							listPathsTmp.add(path);
						}
					}
				}

				listPaths.clear();
				listPaths.addAll(listPathsTmp);
			}

		}
		return listPaths;
	}


	//==================================================================	
	//==============================================[setLogicalVectorTime]
	//==================================================================		
	public List<String> addAllPathFrom (String currentState) {
		
		List <String> listPathsTmp =  new ArrayList<String>();
		String innerRegionName = ParserEngine.mapStateData.get(currentState).getRegion();
		String capInstance = ParserEngine.mapStateData.get(currentState).getCapsuleInstanceName();
		List<String> innerRegionPaths = PES.mapRegionPaths.get(capInstance+"::"+innerRegionName);
		
		for (TransitionData tr :  ParserEngine.listTransitionData){
			if((tr.getReginName().contentEquals(innerRegionName)) && 
					(tr.getCapsuleInstanceName().contentEquals(capInstance))) {
				String [] pathSplit = tr.getPath().split("\\-");
				String trId = tr.getId();
				if (pathSplit[0].contentEquals(currentState)) {
					for (String innerRegionPath : innerRegionPaths) {
						String innerPathId = "";
						if (innerRegionPath.contains(",")) {
						
						String [] innerRegionPathSplit = innerRegionPath.split("\\,");
						innerPathId = innerRegionPathSplit[0];
						}else
							innerPathId = innerRegionPath;
						
						if (trId.contentEquals(innerPathId)) {
							listPathsTmp.add(innerRegionPath);
						}
					}
				}
			}
		}
		return listPathsTmp;
	}

	//==================================================================	
	//==============================================[setLogicalVectorTime]
	//==================================================================		
	public void setLogicalVectorTime (int [] logicalVT) {
		for (int i = 0; i<logicalVectorTime.length -1 ; i++)
		if (logicalVectorTime[i]< logicalVT[i]) {
			logicalVectorTime[i] = logicalVT[i];
		}
	}

	//==================================================================	
	//==============================================[toHistory]
	//==================================================================	
	public boolean toHistory(List<String> list) {
		
		for(String path : list) {
			if(path.contains(",")) {
				String [] regionPathSplit = path.split("\\,");
				if(ParserEngine.mapTransitionData.get(regionPathSplit[regionPathSplit.length-1]).getIsInit())
					return true;
			}
		}
		return false;
	}

	//==================================================================	
	//==============================================[registerChecking]
	//==================================================================
	public boolean registerChecking(Event event) {

		//Check transitionKind = 10 and eventType = 36 [For REGISTER] 
		if (event.getSourceKind().contentEquals("10") && event.getType().contentEquals("36")) {
			currentStatus = "INIT";
			return true;
		}
		return false;
	}

	//==================================================================	
	//==============================================[initChecking]
	//==================================================================	
	public boolean initChecking(Event event) throws InterruptedException {
		System.out.println(dataContainer.getCapsuleInstance() +", in initChecking "+ listPaths.size());

		boolean result = false;
		String id = PES.mapQnameId.get(event.getSourceName());
		
		//Samples: PingPong::Pinger::PingerStateMachine::Region::PLAYING , PingPong::Pinger::PingerStateMachine::Region::onPong

		if (event.getSourceKind().contentEquals("3") && event.getType().contentEquals("14") && ParserEngine.mapTransitionData.get(id).getIsInit()) {

			
			String [] pathSplit = ParserEngine.mapTransitionData.get(id).getPath().split("\\-");
			dataContainer.mapRegionCurrentState.put(ParserEngine.mapTransitionData.get(id).getReginName(),pathSplit[2]);
			
			pathFinder(event.getSourceName());
			if ((listPaths.size() == 1)) {
				result =  true;
				currentStatus = "TRANISTIONEND";
			}else if ((listPaths.size() > 1)){
				result = false;
			
			}else {
				System.err.println("__________ No Path Found! __________");
				System.exit(1);
			}
		}
		return result;
	}
	
	
	//==================================================================	
	//==============================================[sendMessages]
	//==================================================================	
	public void sendMessages() throws InterruptedException {
		
		for (SendMessage sendMessage : listPortMsg) {
			sendMessage.capsuleInstance = dataContainer.getCapsuleInstance();
			List<MyConnector> listMyConnectors = new ArrayList <MyConnector>();
			String trgCapsule = "";
			if (sendMessage.dest != null) {
				System.out.println("sendMessage.dest != null");
				if (sendMessage.dest.asString().contentEquals("msg->sapIndex0_")) {
	            	 for (MyConnector myConnector : ParserEngine.listMyConnectors) {
	            		 if (msgSender.contains(myConnector.capInstanceName1) && dataContainer.getCapsuleInstance().contains(myConnector.capInstanceName2)) {
	            			 sendMessage.dest = new Value(myConnector.port2Idx,"Integer");
	            			 break;
	            		 }else if (msgSender.contains(myConnector.capInstanceName2) && dataContainer.getCapsuleInstance().contains(myConnector.capInstanceName1)){
	            			 sendMessage.dest = new Value(myConnector.port1Idx,"Integer");
	            			 break;
	            		 }
	            	 }            	 
	        	 }	
				
				for ( CapsuleConn capConn : ParserEngine.listCapsuleConn) {
					if (capConn.getCapsuleInstanceName().contains(dataContainer.getCapsuleInstance())) { 
						listMyConnectors = capConn.getListMyConnector();
						for (MyConnector connector : listMyConnectors) { 
							if (connector.portCap1.contentEquals(sendMessage.port) && (connector.port1Idx == sendMessage.dest.asInteger())) {
								trgCapsule = connector.capInstanceName2;
								break;
							}else if (connector.portCap2.contentEquals(sendMessage.port)&& (connector.port2Idx == sendMessage.dest.asInteger())) {
								trgCapsule = connector.capInstanceName1;
								break;
							}
						}
						break;
					}
				}
				
			}else {
				for ( CapsuleConn capConn : ParserEngine.listCapsuleConn) {
					if (capConn.getCapsuleInstanceName().contains(dataContainer.getCapsuleInstance())) {

						listMyConnectors = capConn.getListMyConnector();
						for (MyConnector connector : listMyConnectors) { 
							if (connector.portCap1.contentEquals(sendMessage.port) && sendMessage.capsuleInstance.contains(connector.capInstanceName1) && (!sendMessage.capsuleInstance.contains(connector.capInstanceName2))) {
								trgCapsule = connector.capInstanceName2;
								break;
							}else if (connector.portCap2.contentEquals(sendMessage.port) && sendMessage.capsuleInstance.contains(connector.capInstanceName2) && (!sendMessage.capsuleInstance.contains(connector.capInstanceName1))) {
								trgCapsule = connector.capInstanceName1;
								break;
							}
						}
						break;
					}
				}
			}

			

			if (trgCapsule.isEmpty()){
				System.err.println("In " + dataContainer.getCapsuleInstance() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>> msg: " + sendMessage.msg + ", is sending via " + sendMessage.port +" to "+ trgCapsule);
				System.err.println(sendMessage.capsuleInstance + "  In " + dataContainer.getCapsuleInstance() + ">>>>>>>>>>>>> msgSender: " +msgSender +", dest: " + sendMessage.dest.asString());
				System.err.println("__________ Target Capsule not Found! __________");
				System.exit(1);
			}
				
			
			Iterator<Entry<String, CapsuleTracker>> itr = TrackerMaker.mapCapsuleTracker.entrySet().iterator();  
			while(itr.hasNext()) 
	        { 
				Entry<String, CapsuleTracker> entry = itr.next();
				if (entry.getKey().contains(trgCapsule)) {
					if ((sendMessage.data != null) && (sendMessage.dataName.contentEquals("__getName__") || sendMessage.data.toString().contains("__CapInstanceName__"))) {
						sendMessage.data = new Value (separateTopFromInstanceName(dataContainer.getCapsuleInstance()),"String");
						entry.getValue().dataContainer.mapSendMessages.put(sendMessage.msg, sendMessage);
					}else
						entry.getValue().dataContainer.mapSendMessages.put(sendMessage.msg, sendMessage);
					break;
				}
	        }
			
			listMyConnectors = null;
		}
		
	}
	
	//==================================================================	
	//==============================================[interpretActionCode]
	//==================================================================	
	public void interpretActionCode(String actionCode, String msgSender , boolean guardEval) throws InterruptedException {

		ACLexer lexer = new ACLexer(new ANTLRInputStream(actionCode));
		ACParser parser = new ACParser(new CommonTokenStream(lexer));
		EvalVisitor visitor = new EvalVisitor(maplocalHeap,separateTopFromInstanceName(dataContainer.getCapsuleInstance()));
		visitor.visit(parser.parse());

		Iterator<Entry<String, Value>> itr = visitor.getHeapMem().entrySet().iterator(); 
        
        while(itr.hasNext()) 
        { 
             Map.Entry<String, Value> entry = itr.next();
             if (entry.getValue().toString().contentEquals("__CapInstanceName__")) {
            	 maplocalHeap.put(entry.getKey(), new Value (separateTopFromInstanceName(dataContainer.getCapsuleInstance()),"String"));
             }else if (entry.getValue().toString().contentEquals("msg->sapIndex0")) { 
            	 for (MyConnector myConnector : ParserEngine.listMyConnectors) {
            		 if (msgSender.contains(myConnector.capInstanceName1) && dataContainer.getCapsuleInstance().contains(myConnector.capInstanceName2)) {
            			 maplocalHeap.put(entry.getKey(), new Value(myConnector.port2Idx,"Integer"));
            			 break;
            		 }else if (msgSender.contains(myConnector.capInstanceName2) && dataContainer.getCapsuleInstance().contains(myConnector.capInstanceName1)){
            			 maplocalHeap.put(entry.getKey(), new Value(myConnector.port1Idx,"Integer"));
            			 break;
            		 }
            		 
            	 }            	 
        	 }else {
            	 maplocalHeap.put(entry.getKey(), entry.getValue());
        	 }
        }
        
        if (!guardEval) {
        	listPortMsg = visitor.getListPortMsg();
        	sendMessages();
        }
		
		lexer = null;
		parser = null;
		visitor = null;
		
	}
	//==================================================================	
	//==============================================[processTriggers]
	//==================================================================	
	public void processTriggers(String path) {
		List<String> listTrigger = new ArrayList<String>();

		listTrigger = mapPathTrigger.get(path);

		if (!listTrigger.isEmpty()) {

			for(String msg : listTrigger) {
				String [] msgSplit = msg.split("\\.");

				SendMessage sendMessage = dataContainer.mapSendMessages.get(msgSplit[1]);
				if ((sendMessage != null) && (sendMessage.dataName != null) && (sendMessage.data != null)) 
					maplocalHeap.put(sendMessage.dataName, sendMessage.data);

			}
		}
	}
	//==================================================================	
	//==============================================[updateLocalHeap]
	//==================================================================	
	public void updateLocalHeap(String path, String msgSender) throws InterruptedException {
		List<String> listActionCode = new ArrayList<String>();
		String firstTrInPath = "";
		if (path.contains(",")) {
			String [] pathSplit = path.split("\\,");
			firstTrInPath = pathSplit[0];
		}else
			firstTrInPath = path;
			
		processTriggers(path);
		
		listActionCode = mapPathActionCode.get(path);
		
		for(String ac : listActionCode) {
			if (!ac.isEmpty())
				interpretActionCode(ac,msgSender, false);
		}
	}
	//==================================================================	
	//==============================================[makeListTrigger]
	//==================================================================	
		public List<String> makeListTrigger(String path) throws InterruptedException {
			List<String> listTrigger = new ArrayList<String>();
			List<String> listGuards = new ArrayList<String>();
			List<String> listActionCode = new ArrayList<String>();
			
			if (path.contains(",")) {
				String [] pathSplit = path.split("\\,");
				for (String pathID : pathSplit) {
					listTrigger.addAll(ParserEngine.mapTransitionData.get(pathID).getTriggers());
					listGuards.addAll(ParserEngine.mapTransitionData.get(pathID).getGuard());
					listActionCode.add(ParserEngine.mapTransitionData.get(pathID).getActionCode());
					
					String [] trPartsID = ParserEngine.mapTransitionData.get(pathID).getPath().split("\\-");
					if (ParserEngine.mapStateData.get(trPartsID[0]).getExitAC() != null)
						listActionCode.add(ParserEngine.mapStateData.get(trPartsID[0]).getExitAC());
					if (ParserEngine.mapStateData.get(trPartsID[2]).getEntrAC() != null)
						listActionCode.add(ParserEngine.mapStateData.get(trPartsID[2]).getEntrAC());
					
				}
			}else {
				listTrigger.addAll(ParserEngine.mapTransitionData.get(path).getTriggers());
				listGuards.addAll(ParserEngine.mapTransitionData.get(path).getGuard());
				listActionCode.add(ParserEngine.mapTransitionData.get(path).getActionCode());
				
				
				String [] trPartsID = ParserEngine.mapTransitionData.get(path).getPath().split("\\-");
				if (ParserEngine.mapStateData.get(trPartsID[0]).getExitAC() != null)
					listActionCode.add(ParserEngine.mapStateData.get(trPartsID[0]).getExitAC());
				if (ParserEngine.mapStateData.get(trPartsID[2]).getEntrAC() != null)
					listActionCode.add(ParserEngine.mapStateData.get(trPartsID[2]).getEntrAC());
			}
			if (listTrigger != null) {
				mapPathGuards.put(path, listGuards);
				mapPathTrigger.put(path, listTrigger);
				mapPathActionCode.put(path, listActionCode);
				
			}else {
				System.err.println("__________ No Trigger Found! __________");
				System.exit(1);
			}
				
			return listTrigger;
		}
	
	//==================================================================	
	//==============================================[isRequirementMet]
	//==================================================================	
	public boolean isRequirementMet(String path) throws InterruptedException {

		boolean result = false;
		List<String> listTrigger = new ArrayList<String>();
		List<String> listGuards = new ArrayList<String>();
		
		listTrigger = mapPathTrigger.get(path);
		listGuards = mapPathGuards.get(path);

		
		if (listTrigger == null) {
			listTrigger = makeListTrigger(path);
		}
		
		if (listTrigger.isEmpty())
			result = true;
		else {
			result = true;
			for(String msg : listTrigger) {
				if(!msg.contains("__TIMER__")){
					result = false;
				}
			}

			for(String msg : listTrigger) {
				String [] msgSplit = msg.split("\\.");
				if ((dataContainer.mapSendMessages.get(msgSplit[1]) != null)) {
					msgSender = dataContainer.mapSendMessages.get(msgSplit[1]).capsuleInstance;
					result = true;
					msgTobeConsumed = msgSplit[1];
				}
			}
		}
		
		//GUARD EVALUATION
		if (result && listGuards != null) {

			Map<String, Value> maplocalHeapCopy = new HashMap<String, Value>();
			maplocalHeapCopy.putAll(maplocalHeap);
			updateLocalHeap(path,"");
			
			for(String ac : listGuards) {
				if (!ac.isEmpty()) {
					interpretActionCode(ac,msgSender, true);
					boolean guard = maplocalHeap.get("__GUARD__").asBoolean();
					if (!guard) {
						result = false;
						break;
					}
					maplocalHeap.remove("__GUARD__");
				}
			}
			maplocalHeap.clear();
			maplocalHeap.putAll(maplocalHeapCopy);
		}

		return result;
			
	}
	
	//==================================================================	
	//==============================================[transitionChecking]
	//==================================================================	
	public boolean transitionChecking(Event event) throws InterruptedException {
		
		boolean result = false;
		String id = PES.mapQnameId.get(event.getSourceName());
		
		String currentSateId = dataContainer.mapRegionCurrentState.get(ParserEngine.mapTransitionData.get(id).getReginName());
		
		//Samples: PingPong::Pinger::PingerStateMachine::Region::PLAYING , PingPong::Pinger::PingerStateMachine::Region::onPong

		if (event.getSourceKind().contentEquals("3") && event.getType().contentEquals("14")) {
			
			pathFinder(event.getSourceName());
			if ((listPaths.size() == 1) && isRequirementMet(listPaths.get(0))) {
				result =  true;
				currentStatus = "TRANISTIONEND";
			}else {
				result = false;
			}
		}
		return result;
	}
	//==================================================================	
	//==============================================[outputStreamToFile]
	//==================================================================	
	private static void outputStreamToFile(OutputStream os, String data) {
		try {
			os.write(data.getBytes(), 0, data.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//==================================================================	
	//==============================================[isPortInUse]
	//==================================================================
	public static boolean isPortInUse(String host, int port) {
		  // Assume no connection is possible.
		  boolean result = false;

		  try {
		    (new Socket(host, port)).close();
		    result = true;
		  }
		  catch(SocketException e) {
		    // Could not connect.
		  } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  return result;
		}
	//==================================================================	
	//==============================================[makeJSONobj]
	//==================================================================
	private static JSONObject makeJSONobj(int priorityEventCounter, String capInstName, String transitionName, String allVariables) {
			JSONObject jsonObj = new JSONObject();
			JSONArray traceID = new JSONArray();
			traceID.add(priorityEventCounter); traceID.add(capInstName); traceID.add(transitionName);
			jsonObj.put("traceID", traceID);
			JSONArray traceVar = new JSONArray();

			if(allVariables!=null) {
				String[] variables = allVariables.split(System.getProperty("line.separator"));
				for (int i=0; i<variables.length;i++) {
					String[] varData = variables[i].split("\\,"); 
					if (varData.length == 3) {
						traceVar.add(varData[0]); traceVar.add(varData[1]); traceVar.add(varData[2]);
					}
				}
			}
			
			jsonObj.put("traceVar", traceVar);
			return jsonObj;
			
		}
	@SuppressWarnings("deprecation")
	public void shutdown() {
		Thread.currentThread().stop();
	}


}
