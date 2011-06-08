package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.HourModel;

public class MissingHourPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel hourLabel = new JLabel("Hours: ");
	
	private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner hourSpinner = new JSpinner(hourModel);
	
	public MissingHourPanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(hourLabel);
		this.add(hourSpinner);
	}	
	
	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setHourModel(new HourModel(hourModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setHourModel(null);	
	}
}
