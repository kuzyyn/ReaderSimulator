package model.serial;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import model.serial.Protocol;

public class Receiver implements SerialPortEventListener {
	InputStream in;
	Protocol protocol;
	char endLine;

	int tail = 0;
	boolean finishFlag = false;

		public Receiver(InputStream in,Protocol protocol) {
			this.in = in;
			this.protocol=protocol;
			this.endLine = protocol.getEndLineSign();
		}

		public void onReceive(byte b) {
			protocol.onMessage(b);
	}

		@Override
		public void serialEvent(SerialPortEvent se) {
			int data;
			try{
				data=in.read();
				//while ((data=in.read()) != -1){
					onReceive((byte)data);
				//	if ( data == endLine ) { // timer, po 1 sekundzie niech przerywa pêtlê 
               //         break;
            //       }
			//	}
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}

	}
