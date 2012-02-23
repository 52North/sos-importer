package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Second;

/**
 * consists of a label and a JSpinner for a single second
 * @author Raimund
 */
public class MissingSecondPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel secondLabel = new JLabel("Seconds: ");

	private SpinnerNumberModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner secondSpinner = new JSpinner(secondModel);
	
	public MissingSecondPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(secondLabel);
		this.add(secondSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setSecond(new Second(secondModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setSecond(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Second(secondModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		secondModel.setValue(((Second)c).getValue());
	}
}
