package org.n52.sos.importer;

import java.awt.Color;

import javax.swing.JLabel;

public class ExampleFormatLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private Formatable formatter;
	
	public ExampleFormatLabel(Formatable formatter) {
		super();
		this.formatter = formatter;
	}
	
	public void reformat(Object o) {
		try {
			String formattedValue = formatter.format(o);
	        this.setForeground(Color.black);
	        this.setText(formattedValue);
		} catch (Exception e) {
	    	this.setForeground(Color.red);
	    	this.setText("Error: " + e.getMessage());
		}
	}			
}