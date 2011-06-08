package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.DayModel;
import org.n52.sos.importer.model.dateAndTime.MonthModel;
import org.n52.sos.importer.model.dateAndTime.YearModel;

public class MissingDatePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dateLabel = new JLabel("Date: ");
	
	private SpinnerDateModel dateModel;
	private JSpinner dateSpinner;
	
	public MissingDatePanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
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
		dateAndTimeController.getModel().setDayModel(new DayModel(c.get(Calendar.DAY_OF_MONTH)));
		dateAndTimeController.getModel().setMonthModel(new MonthModel(c.get(Calendar.MONTH) + 1));
		dateAndTimeController.getModel().setYearModel(new YearModel(c.get(Calendar.YEAR)));
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setDayModel(null);
		dateAndTimeController.getModel().setMonthModel(null);
		dateAndTimeController.getModel().setYearModel(null);	
	}
}
