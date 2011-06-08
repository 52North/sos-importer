package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.DayModel;

public class MissingDayPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dayLabel = new JLabel("Day: ");
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	
	public MissingDayPanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dayLabel);
		this.add(daySpinner);
	}

	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setDayModel(new DayModel(dayModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setDayModel(null);		
	}
}
