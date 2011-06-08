package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.TimeZoneModel;

public class MissingTimeZonePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeZoneLabel = new JLabel("Timezone: ");
	
	private SpinnerNumberModel timeZoneModel = new SpinnerNumberModel(0, -12, 12, 1);
	private JSpinner timeZoneSpinner = new JSpinner(timeZoneModel);
	
	public MissingTimeZonePanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timeZoneLabel);
		this.add(timeZoneSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setTimeZoneModel(new TimeZoneModel(timeZoneModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setTimeZoneModel(null);	
	}
}
