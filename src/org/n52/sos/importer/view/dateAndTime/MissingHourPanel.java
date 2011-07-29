package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;

public class MissingHourPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel hourLabel = new JLabel("Hours: ");
	
	private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner hourSpinner = new JSpinner(hourModel);
	
	public MissingHourPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(hourLabel);
		this.add(hourSpinner);
	}	
	
	@Override
	public void assignValues() {
		dateAndTime.setHour(new Hour(hourModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setHour(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Hour(hourModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		hourModel.setValue(((Hour)c).getValue());
	}
}
