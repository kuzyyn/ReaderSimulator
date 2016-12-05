package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;

public class TextPanel {
JPanel textPanel;
JTextPane inputArea,outputArea;
	public JPanel createTextPanel(){
		textPanel = new JPanel(new MigLayout());
		JLabel inputLabel = new JLabel("stream IN");
		JLabel outputLabel = new JLabel("stream OUT");
		inputArea = new JTextPane();
		inputArea = setupArea(inputArea);
		outputArea = new JTextPane();
		outputArea = setupArea(outputArea);
		
		textPanel.add(inputLabel,"align center");
		textPanel.add(outputLabel,"align center, wrap");
		textPanel.add(getScrollPaneForArea(inputArea),"grow,push");
		textPanel.add(getScrollPaneForArea(outputArea),"grow,push");
		return textPanel;
		
	}
	private JScrollPane getScrollPaneForArea(JTextPane area) {
		JScrollPane jp = new JScrollPane(area);
		jp.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		jp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return jp;
	}
	private JTextPane setupArea(JTextPane area) {
		area.setEditable(false);
		area= alignBorder(area);
		return area;
	}
	public JPanel getTextJPanel() {
		return textPanel;
		
	}
	
	JTextPane alignBorder(JTextPane area){
		area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return area;
	}
	public JTextPane getInputArea() {
		return inputArea;
	}
	public JTextPane getOutputArea() {
		return outputArea;
	}

}



