package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Year;

/**
 * consists of a label and a JSpinner for single years
 * @author Raimund
 */
public class MissingYearPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel yearLabel = new JLabel("Year: ");
	
	private SpinnerNumberModel yearModel = new SpinnerNumberModel(2011, 1900, 2100, 1);
	private JSpinner yearSpinner = new JSpinner(yearModel);
	
	public MissingYearPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(yearLabel);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		this.add(yearSpinner);
	}
	
	@Override
	public void assignValues() {
		dateAndTime.setYear(new Year(yearModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setYear(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Year(yearModel.getNumber().intValue());
	}
	
	public void setMissingComponent(Component c) {
		yearModel.setValue(((Year)c).getValue());
	}
}
