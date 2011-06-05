package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingYearPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel yearLabel = new JLabel("Year: ");
	
	private SpinnerNumberModel yearModel = new SpinnerNumberModel(2011, 1900, 2100, 1);
	private JSpinner yearSpinner = new JSpinner(yearModel);
	
	public MissingYearPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(yearLabel);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		this.add(yearSpinner);
	}
}
