package main.model.logger;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import view.TextPanel;

public class ConsoleLogger {
TextPanel textPanel;
private static JTextPane input;
private static JTextPane output;
	public ConsoleLogger(TextPanel textPanel){
		this.textPanel=textPanel;
		getAreas();
	}
	
	private void getAreas() {
		input=textPanel.getInputArea();
		output=textPanel.getOutputArea();
		
	}

	public static void inRegularLog(String message){
		appendToPane(input,getCurrentTime() + " "+message+"\n",Color.black);
	}
	public static void outRegularLog(String message){
		appendToPane(output,getCurrentTime() + " "+message+"\n",Color.black);
	}
	public static void inErrorLog(String message){
		appendToPane(input,getCurrentTime() + " "+message+"\n",Color.red);
	}
	public static void outErrorLog(String message){
		appendToPane(output,getCurrentTime() + " "+message+"\n",Color.red);
	}
	
	private static String getCurrentTime(){
		String time;
		int temp;
		Calendar calendar = Calendar.getInstance();
		temp = calendar.get(Calendar.HOUR_OF_DAY);
		if (temp < 10) {
			time = "0"+Integer.toString(temp)+":";
		} else {
			time = Integer.toString(temp)+":";
		}
		temp = calendar.get(Calendar.MINUTE);
		if (temp < 10) {
			time += "0"+Integer.toString(temp)+":";
		} else {
			time += Integer.toString(temp)+":";
		}
		temp = calendar.get(Calendar.SECOND);
		if (temp < 10) {
			time += "0"+Integer.toString(temp);
		} else {
			time += Integer.toString(temp);
		}		
		return time;
		
	}
	private static void appendToPane(JTextPane tp, String msg, Color c){
		tp.setEditable(true);
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_RIGHT);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
        tp.setEditable(false);
	}
	
	
	
}

