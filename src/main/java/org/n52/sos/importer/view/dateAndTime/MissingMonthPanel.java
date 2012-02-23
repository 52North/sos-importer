package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Month;

/**
 * consists of a label and a JSpinner for a single month
 * @author Raimund
 */
public class MissingMonthPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel monthLabel = new JLabel("Month: ");
	
	private SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
	private JSpinner monthSpinner = new JSpinner(monthModel);
	
	public MissingMonthPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(monthLabel);
		this.add(monthSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setMonth(new Month(monthModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setMonth(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Month(monthModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		monthModel.setValue(((Month)c).getValue());
	}
}
