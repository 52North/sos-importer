package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingMonthPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel monthLabel = new JLabel("Month: ");
	
	private SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
	private JSpinner monthSpinner = new JSpinner(monthModel);
	
	public MissingMonthPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(monthLabel);
		this.add(monthSpinner);
	}
}
