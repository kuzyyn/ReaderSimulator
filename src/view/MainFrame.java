package view;


import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class MainFrame {
JFrame mainFrame;

JPanel mainPanel;
SettingsPanel settingsPanel;
StatusPanel statusPanel;



TextPanel textPanel;
	public MainFrame(){
		initialize();
	}

	private void initialize() {
		mainFrame = new JFrame();
		mainPanel = new JPanel(new MigLayout());
		settingsPanel = new SettingsPanel();
		mainPanel.add(settingsPanel.createSettingsPanel(),"growx,pushx,wrap");
		textPanel = new TextPanel();
		mainPanel.add(textPanel.createTextPanel(),"grow,push,align center");
		setupMainFrame();
		Container pane = mainFrame.getContentPane();
		pane.setLayout(new MigLayout());
		pane.add(mainPanel,"grow,push");
		statusPanel = new StatusPanel();
		mainPanel.add(statusPanel.createStatusPanel(),"pushx,growx,south");

		
	}
	private void setupMainFrame() {
		mainFrame.setTitle("Reader Simulator 0.0.1");
		mainFrame.setSize(800,800);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(3);
		mainFrame.setMinimumSize(new Dimension(500,200));
		mainFrame.setVisible(true);
	//	ImageIcon appIcon = new ImageIcon("res/settings.png");
		//mainFrame.setIconImage(appIcon.getImage());
		
	}

	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	public SettingsPanel getSettingsPanel() {
		return settingsPanel;
	}

	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	public TextPanel getTextPanel() {
		return textPanel;
	}

	
}
