package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.MinuteModel;

public class MissingMinutePanel extends MissingComponentPanel {
	
	private static final long serialVersionUID = 1L;

	private final JLabel minuteLabel = new JLabel("Minutes: ");
	
	private SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner minuteSpinner = new JSpinner(minuteModel);
	
	public MissingMinutePanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(minuteLabel);
		this.add(minuteSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setMinuteModel(new MinuteModel(minuteModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setMinuteModel(null);	
	}
}
