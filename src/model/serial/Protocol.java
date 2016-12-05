package model.serial;


import gnu.io.SerialPort;
public interface Protocol {

	SerialPort setSerialPortParams(SerialPort serialPort);
	public char getEndLineSign();
	void decodeMessage(String lastMessage);
	void onMessage(byte b);
	

}