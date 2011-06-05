package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MissingTimeZonePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeZoneLabel = new JLabel("Timezone: ");
	
	private SpinnerNumberModel timeZoneModel = new SpinnerNumberModel(0, -12, 12, 1);
	private JSpinner timeZoneSpinner = new JSpinner(timeZoneModel);
	
	public MissingTimeZonePanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timeZoneLabel);
		this.add(timeZoneSpinner);
	}
}
