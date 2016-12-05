package main.controller;


import javax.swing.JLabel;

import main.model.logger.ConsoleLogger;
import view.MainFrame;

public class ViewController {
MainFrame mainFrame;	

private static ViewController viewObject;
private ViewController(){};

public static ViewController getInstance(){
	if (viewObject==null){
		synchronized(ViewController.class){
			if(viewObject ==null){
				viewObject = new ViewController();
			}
		}
	}
	return viewObject;
}
	public void initializeMainFrame() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
			new ConsoleLogger(mainFrame.getTextPanel());
		}
		
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void updateStatusLabels(String readerL, String com) {
		JLabel reader = mainFrame.getStatusPanel().getReaderLabel();
		JLabel comm = mainFrame.getStatusPanel().getCommLabel();
		reader.setText(readerL);
		comm.setText(com);
	
		
	}
	
}
