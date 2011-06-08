package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.MonthModel;

public class MissingMonthPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel monthLabel = new JLabel("Month: ");
	
	private SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
	private JSpinner monthSpinner = new JSpinner(monthModel);
	
	public MissingMonthPanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(monthLabel);
		this.add(monthSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTimeController.getModel().setMonthModel(new MonthModel(monthModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setMonthModel(null);	
	}
}
