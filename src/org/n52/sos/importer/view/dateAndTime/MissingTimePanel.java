package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class MissingTimePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeLabel = new JLabel("Time: ");
	
	private SpinnerDateModel timeModel;
	private JSpinner timeSpinner;
	
	public MissingTimePanel() {
		timeModel = new SpinnerDateModel();
		timeModel.setCalendarField(Calendar.HOUR_OF_DAY);
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timeLabel);
		this.add(timeSpinner);
	}	
}
