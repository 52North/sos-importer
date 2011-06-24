package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Year;

public class MissingDatePanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dateLabel = new JLabel("Date: ");
	
	private SpinnerDateModel dateModel;
	private JSpinner dateSpinner;
	
	public MissingDatePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		GregorianCalendar calendar = new GregorianCalendar();
		Date initDate = calendar.getTime();
		calendar.add(Calendar.YEAR, -100);
		Date earliestDate = calendar.getTime();
		calendar.add(Calendar.YEAR, 200);
		Date latestDate = calendar.getTime();
		dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dateLabel);
		this.add(dateSpinner);
	}

	@Override
	public void assignValues() {
		Calendar c = new GregorianCalendar();
		c.setTime(dateModel.getDate());
		dateAndTime.setDay(new Day(c.get(Calendar.DAY_OF_MONTH)));
		dateAndTime.setMonth(new Month(c.get(Calendar.MONTH) + 1));
		dateAndTime.setYear(new Year(c.get(Calendar.YEAR)));
	}

	@Override
	public void unassignValues() {
		dateAndTime.setDay(null);
		dateAndTime.setMonth(null);
		dateAndTime.setYear(null);	
	}
}
