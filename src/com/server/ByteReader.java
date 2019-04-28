package com.server; 


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.queensu.cs.server.ByteReader.Command;
import ca.queensu.cs.server.SocketIO;

public final class ByteReader implements Runnable {

	public static enum Command {
		WAITING_FOR_BREAKPOINT_REACHED,
		WAITING_FOR_BREAKPOINT_LIST,
		WAITING_FOR_EVENT_LIST,
		WAITING_FOR_VALUE_CHANGE_ACK,
		WAITING_FOR_ACK,
		WAITING_FOR_RUN_ACK,
		WAITING_FOR_LAST_EVENT,
		STEPPING,
		RESUME
	};

	private Map<Integer, Command> commandStack;
	private static int commandId = 0;
	private String[] capsuleNames;
	private Semaphore sem;
	public static int eventCounter;
	private int totalSize = 0;
	


	public ByteReader(String[] caps, Semaphore sem ) {
		this.eventCounter = 0;
		capsuleNames = caps;
		this.sem = sem; 
	}

	public final void run() {
		try {
			try {
				Server.io.input = new DataInputStream(Server.io.socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			while (!Thread.currentThread().isInterrupted()) {
				//this.sem.acquire(); 
				readFromSocketWithSize();
				//this.sem.release(); 
			}
			System.out.println("Deleting client");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//==================================================================	
	//==============================================[eventMaker]
	//==================================================================
	public Event eventMaker(String message) {
		String eventId = ""; 
		String eventSourceKind = ""; 
		String eventType = ""; 
		String eventCapsuleName = ""; 
		String eventCapsuleInstance = ""; 
		String eventCapsuleIndex = ""; 
		String eventSourceName ="";
		String eventCpuTik = ""; 
		String eventTimePointSecond = ""; 
		String eventTimePointNano = ""; 
		String eventVariableData = "";
		String eventSignal = "";
		String eventSource = "";
		String eventStatus = "";
		String eventTarget = "";
		String vectorTime  = "";

		int index = 0;
		if (message.contains("[getEventId]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventId = message.substring(message.indexOf("]") + 1, message.indexOf(";"));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getEventSourceKind]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventSourceKind = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getEventType]") && message.contains(";") && (message.indexOf("], index+1") < message.indexOf(";", index+1))) {
			eventType = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getCapsuleName]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventCapsuleName = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getCapsuleInstance]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventCapsuleInstance = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			//Extract vectorTime if exists

			if (eventCapsuleInstance.contains(" ")) {//vt's come with the message
				int indexOfSpace = eventCapsuleInstance.indexOf(" ");				
				vectorTime = eventCapsuleInstance.substring(indexOfSpace+1,eventCapsuleInstance.length());
				eventCapsuleInstance = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(" ", index+1));
			}
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getCapsuleIndex]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventCapsuleIndex = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getSourceName]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventSourceName = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getCpuTik]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventCpuTik = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getTimePointSecond]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventTimePointSecond = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getTimePointNano]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventTimePointNano = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("[getVariableData]") && message.contains(";") && (message.indexOf("]", index+1) < message.indexOf(";", index+1))) {
			eventVariableData = message.substring(message.indexOf("]", index+1) + 1, message.indexOf(";", index+1));
			index = message.indexOf(";", index+1);
		}
		if (message.contains("Signal:")){
			if (message.contains("Signal:") && message.contains(",") && (message.indexOf("]", index+1) < message.indexOf(",", index+1))) {
				eventSignal = message.substring(message.indexOf(":", index+1) + 1, message.indexOf(",", index+1));
				index = message.indexOf(",", index+1);
			}
			if (message.contains("Source:") && message.contains(",") && (message.indexOf("]", index+1) < message.indexOf(",", index+1))) {
				eventSource = message.substring(message.indexOf(":", index+1) + 1, message.indexOf(",", index+1));
				index = message.indexOf(",", index+1);
			}
			if (message.contains("Status:") && message.contains(",") && (message.indexOf("]", index+1) < message.indexOf(",", index+1))) {
				eventStatus = message.substring(message.indexOf(":", index+1) + 1, message.indexOf(",", index+1));
				index = message.indexOf(",", index+1);
			}
			if (message.contains("Target:") && message.contains(",")) {
				eventTarget = message.substring(message.indexOf(":", index+1) + 1, message.length());
			}
		} else if (message.contains("Status:")) {
			eventStatus = message.substring(message.indexOf(":", index+1) + 1, message.length());
		}

		Event event = new Event( eventId,  eventSourceKind, eventType, eventCapsuleName, eventCapsuleInstance, eventCapsuleIndex, 
				eventSourceName, eventCpuTik, eventTimePointSecond, eventTimePointNano, eventVariableData, eventSignal, eventSource, eventStatus, eventTarget,vectorTime);

		return event;

	}

	//==================================================================	
	//==============================================[retrieveLastEvent]
	//==================================================================
	private void retrieveLastEvent(String message) {
		System.out.println("========================[In retrieveLastEvent]========================");	
		String[] traces = message.split("[|]");
		String capsule = traces[0];
		String currentState = traces[1];

		for (int i = 0; i < traces.length -2; i++) {
			String event = traces[i+2];
			System.out.println("event: " +  event);
			System.out.println("-------------------------------------");
		}	

	}

	//==================================================================	
	//==============================================[getCommandStack]
	//==================================================================
	public  Map<Integer, Command> getCommandStack() {
		return this.commandStack;
	}

	//==================================================================	
	//==============================================[retrieveCapsuleList]
	//==================================================================
	public void retrieveCapsuleList(String message) {
		System.out.println("========================[In retrieveCapsuleList]========================");	
		String[] capsuleTraces;

		capsuleTraces = message.split("&");

		int n = capsuleTraces.length;
		String[] capsules = new String[n];
		String[] currentStates = new String[n];

		for (int i = 0 ; i < n; i++) {
			String[] traces = capsuleTraces[i].split("[|]");
			capsules[i] = traces[0];
			currentStates[i] = traces[1];
			System.out.println("capsule: " +  capsules[i]);

			System.out.println("capsule: " +  currentStates[i]);
			System.out.println("-------------------------------------");

		}
	}

	//==================================================================	
	//==============================================[readFromSocketWithSize]
	//==================================================================	
	public void readFromSocketWithSize() throws InterruptedException {
		try {
			if (!Server.io.isConnected()) {
				Server.io.close();
				return;
			}
			char[] sizeBuffer = new char[4];
			char[] messageBuffer;
			String message;
			int size;

			//----------[Reading size and message from client]

			byte[] byteBuffer1 = new byte[sizeBuffer.length];
			int result = Server.io.read(byteBuffer1, 0, 4);
			for (int i = 0; i < byteBuffer1.length; i++) {
				sizeBuffer[i] = (char) byteBuffer1[i];
			}
			if (result == -1) {
				Server.io.close();
				return;
			}
			
			byteBuffer1 = null;
			
			size = Integer.parseInt(new String(sizeBuffer));
			
			sizeBuffer =  null;

			messageBuffer = new char[size];

			byte[] byteBuffer2 = new byte[messageBuffer.length+100];

			message = new String(messageBuffer);
			Server.io.read(byteBuffer2, 0, size);
			messageBuffer = null;
			message = new String(byteBuffer2, StandardCharsets.UTF_8);
			byteBuffer2 = null;

			Event event = eventMaker(message);
			
			if((event.getSourceKind().contentEquals("3") || event.getSourceKind().contentEquals("10")) && 
					(event.getType().contentEquals("36") || event.getType().contentEquals("14")))
				Server.eventQueue.add(event);
			event = null;
			int id = 0;

			if (message.indexOf('|') >= 0) {

				String[] table = message.split("[|]", 2);
				String messageId = table[0];
				message = table[1];

				try{
					id = Integer.parseInt(messageId);
				} catch (NumberFormatException e) {
				}


				if ((id == 0)) {
					this.retrieveCapsuleList(message);
				}
				else {
					this.retrieveLastEvent(message);			
				}
			}

			sizeBuffer = new char[4];
			messageBuffer = null;
		}catch (final IOException e) {
			e.printStackTrace();
		}

	}
}
