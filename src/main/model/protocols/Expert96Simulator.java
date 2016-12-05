package main.model.protocols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import main.model.logger.ConsoleLogger;
import model.serial.Protocol;
import model.serial.Sender;


public class Expert96Simulator implements Protocol {
		
		private static final int baudRate = 19200;
		private static final int parity = SerialPort.PARITY_EVEN;
		private static final int bits =SerialPort.DATABITS_7;
		private static final int stopBit = SerialPort.STOPBITS_2;
		
		private final static char endLine=(char)13; //CR
		private final static char NUL = (char)0; // null message
		private final static char STX = (char)2; // Start of text
		private final static char ETX = (char)3; // end of text
		private final static char EOT = (char)4; // end of transmission
		private final static char ENQ = (char)5; // select reader
		private final static char ACK = (char)6; // positive acknowledge to the sender
		private final static char NAK = (char)21; // negative acknowledge to the sender
		
		boolean connectionEstablished = false;
		
		public enum messageType {endOfCommunication,communicationEstablishment,acknowledge,negAcknowledge,broken,statusCommand,controlCommand};
		
		public void decodeMessage(String lastMessage) {
			
			Expert96Simulator.messageType message = getMessageType(lastMessage);
			if (connectionEstablished){
				switch (message){
				case endOfCommunication: 
					endCommunication(); break;
					
				case broken:
					sendNegativeAcknowledgment(); break;
				
					
				default: sendNegativeAcknowledgment(); break;
				
				}
				
			} else if (!connectionEstablished) {
				switch (message){
				case communicationEstablishment: 
					establishConnection(); break;		
				case endOfCommunication: 
					endCommunication(); break;	
				default: sendNegativeAcknowledgment();	break;
				}
			}
			ConsoleLogger.inRegularLog("Odczytano: "+lastMessage + " Ascii: "+getASCII(lastMessage)+ " Typ: " +message );
			
		}
		

		private Expert96Simulator.messageType getMessageType(String lastMessage) {
			Expert96Simulator.messageType message=messageType.broken;
			if (lastMessage.equals(String.valueOf(EOT))){
				message = messageType.endOfCommunication;
				System.out.println("checked if EOT");
			} else if (lastMessage.equals("20"+String.valueOf(ENQ))){
				message = messageType.communicationEstablishment;
			
				
			}else if (lastMessage.equals(Expert96Simulator.ACK)){
				message = messageType.acknowledge;
				System.out.println("checked if ACK");
			} else if (lastMessage.equals(Expert96Simulator.NAK)){
				message = messageType.negAcknowledge;
				System.out.println("checked if NAK");
			} else if (lastMessage.equals(Expert96Simulator.NUL)){
				//to do 
				System.out.println("checked if NUL");
			} else if (isControlMessage(lastMessage)){
					message = messageType.controlCommand;
					System.out.println("checked if control");
			} else if (isStatusMessage(lastMessage)){
					message = messageType.statusCommand;
					System.out.println("checked if status");

			} else {
				message = messageType.broken;
				System.out.println("broken message: " +getASCII(lastMessage));
			}
			System.out.println("Message received: "+message);	
			return message;
		}

		
			private boolean isStatusMessage(String lastMessage) {
				boolean isStatus = false;
				if (hasGoodFormat(lastMessage) && hasGoodBBC(lastMessage)) {
					String message = lastMessage.substring(1, lastMessage.length()-2);
					System.out.println("IC ascii message  "+getASCII(message));
					
					String pattern = "[QEF]{0,3}";
					
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(message);
					isStatus = m.matches();
				}
				if (!isStatus) {
					System.out.println("not status message");
				}
				return isStatus;
		}


			private boolean isControlMessage(String lastMessage) {
				boolean isControl = false;
				if (hasGoodFormat(lastMessage) && hasGoodBBC(lastMessage)) {
					String message = lastMessage.substring(1, lastMessage.length()-2);
					System.out.println("IC ascii message  "+getASCII(message));
					
					String pattern = "([IASXJRa-z0-9])+G{1}$";
					
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(message);
					isControl = m.matches();
				}
				if (!isControl) {
					System.out.println("not control message");
				}
				return isControl;
			}
			private boolean hasGoodFormat(String fullMessage){
				boolean formatGood = false;
				if (fullMessage.length() >=3){
					System.out.println("HGF ascii fullMessage "+getASCII(fullMessage));
					String message = fullMessage.substring(0, fullMessage.length()-1);
					System.out.println("HGF ascii message " + getASCII(message));
					String pattern = "^\u0002{1}.{1,255}\u0003$";
					
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(message);
					formatGood = m.matches();
				}
				System.out.println("HGF formatGood: " + formatGood);
				return formatGood;
			}
	
			private  boolean hasGoodBBC(String fullMessage) {
				
				int BBCcount=-1;
				int BBCreceived=-2;
				if (fullMessage.length() >=3){				
					System.out.println("HGBBC ascii fullMessage "+getASCII(fullMessage));
					BBCreceived = fullMessage.charAt(fullMessage.length()-1);
					String message = fullMessage.substring(0, fullMessage.length()-1);
					
					BBCcount = message.charAt(1);
					for (int i = 2; i<message.length();i++){
							BBCcount = (BBCcount ^ message.charAt(i));	
							
					}
					
				}
				System.out.println("bbc received: " + BBCreceived + " and BBC counted: "+BBCcount);
				if (BBCcount == BBCreceived){
					return true;
				} else {
					return false;
				}

			}

					private String getASCII(String fullMessage) {
							String asciiMessage="";
							int ascii;
							for (int i = 0; i<fullMessage.length();i++){
								ascii = (int)fullMessage.charAt(i);
								asciiMessage+= ascii + " "; 
							}
						return asciiMessage;
						}


		private void establishConnection() {	
				ConsoleLogger.outRegularLog("Connection established");
				connectionEstablished = true;
				Sender.send("20"+String.valueOf(ACK));
		}
			
		private void endCommunication(){
			connectionEstablished = false;
			ConsoleLogger.outRegularLog("Received EOT, ending communication");
		}
		private void sendPositiveAcknowledgment(){
			Sender.send(String.valueOf(ACK));
		}
		private void sendNegativeAcknowledgment(){
			Sender.send(String.valueOf(NAK));
		}
		private void setSyntaxError(int errorCode){
			
		}
		// communication management
		
		boolean waitingForBBC=false;
		byte[] buffer = new byte[1024];
		int tail = 0;

		@Override
		public void onMessage(byte b){
			if (waitingForBBC){
				waitingForBBC=false;
				buffer[tail]=b;
				tail++;
				decodeMessage(getMessage(buffer,tail));		
				tail=0;
			} else { 
				buffer[tail]=b;
				tail++;
				
				if (b == ETX){
					waitingForBBC=true;
				} else if (b==EOT || b==ENQ || b == ACK || b == NAK ){		
					decodeMessage(getMessage(buffer,tail));	
					tail = 0;
				}			
			} 	
		}

		public String getMessage(byte[] buffer, int length){
			return new String(buffer,0,tail);
		}
		
		//reader settings section
		public SerialPort setSerialPortParams(SerialPort serialPort) {
			
			try {
				serialPort.setSerialPortParams(baudRate,bits,stopBit,parity);
			} catch (UnsupportedCommOperationException e) {

				e.printStackTrace();
			}
			return serialPort;
		}

		@Override
		public char getEndLineSign() {
			return endLine;
		}
	}
