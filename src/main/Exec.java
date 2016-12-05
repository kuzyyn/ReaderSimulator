package main;

import java.awt.EventQueue;

import main.controller.ViewController;



public class Exec {

	public static void main(String[] args) {
		
			EventQueue.invokeLater(() -> {
			ViewController.getInstance().initializeMainFrame();
			
        });
	}

}
