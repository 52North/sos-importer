package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingMinutesPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private final JLabel minuteLabel = new JLabel("Minutes: ");
	
	private SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner minuteSpinner = new JSpinner(minuteModel);
	
	public MissingMinutesPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(minuteLabel);
		this.add(minuteSpinner);
	}
}
