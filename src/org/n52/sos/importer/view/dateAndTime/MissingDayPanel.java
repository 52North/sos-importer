package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;

public class MissingDayPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dayLabel = new JLabel("Day: ");
	private DateAndTime dateAndTime;
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	
	public MissingDayPanel(DateAndTime dateAndTime) {
		super();
		this.dateAndTime = dateAndTime;
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
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Day(dayModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		dayModel.setValue(((Day)c).getValue());
	}
}
