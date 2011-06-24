package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;

public class MissingDayPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dayLabel = new JLabel("Day: ");
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	
	public MissingDayPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dayLabel);
		this.add(daySpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setDay(new Day(dayModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setDay(null);		
	}
}
