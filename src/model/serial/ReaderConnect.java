package model.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import main.controller.ViewController;
import main.model.logger.ConsoleLogger;

public class ReaderConnect {
	Protocol protocol;
	InputStream in ;
    OutputStream out ;
    SerialPort serialPort ;
    CommPort commPort;
    Receiver receiver;
    Sender sender;
    public ReaderConnect(Protocol protocol){
    	this.protocol =  protocol;
    }
    
public void connect(String portName) throws Exception{

	boolean used = false;
	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	if(portIdentifier.isCurrentlyOwned())
		{
			JOptionPane.showMessageDialog(ViewController.getInstance().getMainFrame().getMainFrame(), "Port jest zajêty, wybierz inny");
		} 
		else{
			try{
				 commPort = portIdentifier.open(this.getClass().getName(),2000);
				} catch(PortInUseException e){
					JOptionPane.showMessageDialog(ViewController.getInstance().getMainFrame().getMainFrame(), "Port jest zajêty, wybierz inny port exc");
					used=true;
				}
				if (used == false){
					ConsoleLogger.outRegularLog("Próba po³¹czenia do portu "+portName);
	               serialPort = (SerialPort) commPort;
	               serialPort = protocol.setSerialPortParams(serialPort); 

	               in = serialPort.getInputStream();
	               out = serialPort.getOutputStream();    
	               receiver = new Receiver(in, protocol);
	               serialPort.addEventListener(receiver);
	               serialPort.notifyOnDataAvailable(true);
	               sender = new Sender(out,protocol);
	               ConsoleLogger.outRegularLog("Pod³¹czenie do portu zakoñczone sukcesem.");
				}	
			}
		}
		
		public void disconnect(){
			if (serialPort != null) {
		        try {
		        	in.close();
		        	out.close();       
		        } catch (IOException ex) {
		            ConsoleLogger.outErrorLog("B³¹d przy zamykaniu portów");
		        } finally{
		        	serialPort.removeEventListener();
		        	serialPort.close();
		        	ConsoleLogger.outRegularLog("Serial port disconnected");
		        }        
		    }
		}
	}