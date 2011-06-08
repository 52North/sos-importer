package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.SecondModel;

public class MissingSecondPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel secondLabel = new JLabel("Seconds: ");

	private SpinnerNumberModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner secondSpinner = new JSpinner(secondModel);
	
	public MissingSecondPanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(secondLabel);
		this.add(secondSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setSecondModel(new SecondModel(secondModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setSecondModel(null);	
	}
}
