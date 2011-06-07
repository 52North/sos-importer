package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingHourPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel hourLabel = new JLabel("Hours: ");
	
	private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner hourSpinner = new JSpinner(hourModel);
	
	public MissingHourPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(hourLabel);
		this.add(hourSpinner);
	}	
}
