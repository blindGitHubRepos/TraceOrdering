package com.controller; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

import ca.queensu.cs.antler4AC.SendMessage;
import ca.queensu.cs.server.Event;
import ca.queensu.cs.server.Server;
import ca.queensu.cs.umlrtParser.TableDataMember;



public class DataContainer {
	
	private String capsuleInstance;
	private String capsuleName;
	public PriorityBlockingQueue <Event> eventQueue;
	public Map<String,SendMessage> mapSendMessages;
	public Map<String, String> mapRegionCurrentState;
	
	//private String threadName;
	//--
	public DataContainer(String capsuleName, String capsuleInstance, PriorityBlockingQueue <Event> eventQueue) {
		this.capsuleName = capsuleName;
		this.mapRegionCurrentState =  new HashMap<String, String>();
		this.capsuleInstance = capsuleInstance;
		this.eventQueue = eventQueue;
		this.mapSendMessages = new HashMap<String, SendMessage>();;
	}
	//--
	public DataContainer() {
		this(null, null, null);
	}
	
	  
	//--GETTERS
	public String getCapsuleName() {
		return capsuleName;
	}
	public String getCapsuleInstance() {
		return this.capsuleInstance;
	}
	public Queue getEventQueue() {
		return this.eventQueue;
	}

	//--SETTERS
	public void setCapsuleInstance(String capsuleInstance ) {
		this.capsuleInstance = capsuleInstance;
	}
	public void setEventToEventQueue(Event event) throws InterruptedException {
		this.eventQueue.put(event);
	}
	public void setEventQueue(PriorityBlockingQueue <Event> eventQueue) {
		this.eventQueue = eventQueue;
	}
	
	public String allDataToString() {
		return "TableDataMember [capsuleInstance=" + capsuleInstance + ", eventQueue=" + eventQueue +"]";
	}
	

}
