package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingSecondPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel secondLabel = new JLabel("Seconds: ");

	private SpinnerNumberModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner secondSpinner = new JSpinner(secondModel);
	
	public MissingSecondPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(secondLabel);
		this.add(secondSpinner);
	}
}
