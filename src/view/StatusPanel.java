package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class StatusPanel {
	private JPanel statusPanel;
	private JLabel commLabel,readerLabel;
	


	JPanel createStatusPanel(){
		statusPanel = new JPanel(new MigLayout());
		JLabel descriptionLabel = new JLabel("Simulating: ");
		commLabel = new JLabel("");
		readerLabel = new JLabel("");
		
		statusPanel.add(descriptionLabel,"w 20mm::,growy");
		statusPanel.add(commLabel);
		statusPanel.add(readerLabel);
		
		return statusPanel;
	}
	public JLabel getCommLabel() {
		return commLabel;
	}

	public void setCommLabel(JLabel commLabel) {
		this.commLabel = commLabel;
	}

	public JLabel getReaderLabel() {
		return readerLabel;
	}

	public void setReaderLabel(JLabel readerLabel) {
		this.readerLabel = readerLabel;
	}
}
