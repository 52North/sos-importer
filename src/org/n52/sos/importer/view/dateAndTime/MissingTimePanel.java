package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Second;

public class MissingTimePanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeLabel = new JLabel("Time: ");
	
	private SpinnerDateModel timeModel;
	private JSpinner timeSpinner;
	
	public MissingTimePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		timeModel = new SpinnerDateModel();
		timeModel.setCalendarField(Calendar.HOUR_OF_DAY);
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timeLabel);
		this.add(timeSpinner);
	}	
	
	@Override
	public void assignValues() {
		Calendar c = new GregorianCalendar();
		c.setTime(timeModel.getDate());
		dateAndTime.setHour(new Hour(c.get(Calendar.HOUR_OF_DAY)));
		dateAndTime.setMinute(new Minute(c.get(Calendar.MINUTE)));
		dateAndTime.setSecond(new Second(c.get(Calendar.SECOND)));
	}

	@Override
	public void unassignValues() {
		dateAndTime.setHour(null);
		dateAndTime.setMinute(null);
		dateAndTime.setSecond(null);	
	}
}
