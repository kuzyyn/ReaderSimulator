package model.serial;
import java.util.Enumeration;
import java.util.Vector;

import gnu.io.*;

public class AvailablePorts {
		public static Vector<String> listPorts() {
			Vector<String> portList = new Vector<String>();
			@SuppressWarnings("unchecked")
			Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
			
			while(ports.hasMoreElements()){
				CommPortIdentifier portIdentifier = ports.nextElement();
				if (portIdentifier.getPortType()== CommPortIdentifier.PORT_SERIAL){
					portList.add(portIdentifier.getName());
				}
			}
			return portList;
		}


}