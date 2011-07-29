package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Minute;

public class MissingMinutePanel extends MissingDateAndTimePanel {
	
	private static final long serialVersionUID = 1L;

	private final JLabel minuteLabel = new JLabel("Minutes: ");
	
	private SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner minuteSpinner = new JSpinner(minuteModel);
	
	public MissingMinutePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(minuteLabel);
		this.add(minuteSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setMinute(new Minute(minuteModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setMinute(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Minute(minuteModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		minuteModel.setValue(((Minute)c).getValue());
	}
}
