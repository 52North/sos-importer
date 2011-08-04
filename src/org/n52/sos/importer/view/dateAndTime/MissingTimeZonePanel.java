package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.tooltips.ToolTips;

public class MissingTimeZonePanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeZoneLabel = new JLabel("UTC offset: ");
	
	private SpinnerNumberModel timeZoneModel = new SpinnerNumberModel(0, -12, 12, 1);
	private JSpinner timeZoneSpinner = new JSpinner(timeZoneModel);
	
	public MissingTimeZonePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		timeZoneSpinner.setToolTipText(ToolTips.get("UTCOffset"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timeZoneLabel);
		this.add(timeZoneSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setTimeZone(new TimeZone(timeZoneModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setTimeZone(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}

	@Override
	public Component getMissingComponent() {
		return new TimeZone(timeZoneModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		timeZoneModel.setValue(((TimeZone)c).getValue());
	}
}
