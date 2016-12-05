package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.controller.ViewController;
import main.model.logger.ConsoleLogger;
import main.model.protocols.Expert96Simulator;
import net.miginfocom.swing.MigLayout;
import model.serial.AvailablePorts;
import model.serial.Protocol;
import model.serial.ReaderConnect;
import model.serial.Sender;

public class SettingsPanel {
	JPanel settingsPanel;
	String reader="";
	JList<String> comJList;
	String comChosen;
	ReaderConnect rc;

	
	public JPanel createSettingsPanel(){
		settingsPanel = new JPanel(new MigLayout());
		settingsPanel.add(createComScrollList(),"h 150::,w 45%::,growy");

		JButton expert96Button = new JButton("Expert96");		
		JButton goButton = new JButton("RUN simulator");
		JButton stopButton = new JButton("Stop simulator");
		JButton sendACK = new JButton("ACK");
		settingsPanel.add(expert96Button,"wrap");
		settingsPanel.add(goButton);
		settingsPanel.add(stopButton,"wrap");
		settingsPanel.add(sendACK);
		
		expert96Button = setupExpert96Button(expert96Button);
		goButton = setupGoButton(goButton);
		stopButton = setupStopButton(stopButton);
		settingsPanel.add(goButton);
		
		sendACK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Sender.send(String.valueOf((char)6));
				
			}
			
		});
		
		
		return settingsPanel;
	}

	private JScrollPane createComScrollList() {
			
		Vector<String> vector = AvailablePorts.listPorts();
		comJList = new JList<String>(vector);
		comJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane= getScrollPane(comJList);			
					
		Border paddingBorder = BorderFactory.createEmptyBorder(2,2,2,5);
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		scrollPane.setBorder(border);
		comJList.setBorder(paddingBorder);	
		return scrollPane;
	}
	private JScrollPane getScrollPane(JList<String> list) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50, 30, 300, 50);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		return scrollPane;
	}
	private JButton setupGoButton(JButton goButton) {
		goButton.setFocusable(false);
		goButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();
				if (source.equals(goButton)){
					comChosen = comJList.getSelectedValue();
					if (reader!="" && comChosen!=null){
						ViewController.getInstance().updateStatusLabels(reader,comChosen);
						connectReader();			
					} else {
						JOptionPane.showMessageDialog(ViewController.getInstance().getMainFrame().getMainFrame(),
							    "Wybierz prawid³owy czytnik i port COM");
					}
				}	
			}		
		});
		return goButton;
	}
	private JButton setupStopButton(JButton stopButton) {
		stopButton.setFocusable(false);
		stopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();
				if (source.equals(stopButton)){
					if (rc != null){
						rc.disconnect();
					}
				}
			}
		});	
		return stopButton;
	}
	private JButton setupExpert96Button(JButton expert96Button) {
		expert96Button.setFocusable(false);
		expert96Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JButton source = (JButton) e.getSource();
				if (source.equals(expert96Button)){
					System.out.println("expertButton");
					reader="EXPERT96";
				}
			}
			
		});
		
		return expert96Button;
	}
	
	private void connectReader() {
		if (rc!=null){
			rc.disconnect();
		}
		Protocol protocol = null;
		if (reader.equals("EXPERT96")){
			protocol = new Expert96Simulator();
		}
		if (protocol != null){
			try {
			rc = new ReaderConnect(protocol);
			rc.connect(comChosen);


			}catch(NullPointerException e){
				ConsoleLogger.outErrorLog("B³¹d podczas tworzenia po³¹czenia");
				rc = null;
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(ViewController.getInstance().getMainFrame().getMainFrame(),
				    "Nie ma takiego sterownika else");
		}	
		
	}




}
