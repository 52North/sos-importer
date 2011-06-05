package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingDayPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dayLabel = new JLabel("Day: ");
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	
	public MissingDayPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dayLabel);
		this.add(daySpinner);
	}
}
