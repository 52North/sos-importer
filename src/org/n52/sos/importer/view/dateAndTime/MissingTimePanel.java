package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.HourModel;
import org.n52.sos.importer.model.dateAndTime.MinuteModel;
import org.n52.sos.importer.model.dateAndTime.SecondModel;

public class MissingTimePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel timeLabel = new JLabel("Time: ");
	
	private SpinnerDateModel timeModel;
	private JSpinner timeSpinner;
	
	public MissingTimePanel(DateAndTimeController dateAndTimeController) {
		super(dateAndTimeController);
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
		dateAndTimeController.getModel().setHourModel(new HourModel(c.get(Calendar.HOUR_OF_DAY)));
		dateAndTimeController.getModel().setMinuteModel(new MinuteModel(c.get(Calendar.MINUTE)));
		dateAndTimeController.getModel().setSecondModel(new SecondModel(c.get(Calendar.SECOND)));
	}

	@Override
	public void unassignValues() {
		dateAndTimeController.getModel().setHourModel(null);
		dateAndTimeController.getModel().setMinuteModel(null);
		dateAndTimeController.getModel().setSecondModel(null);	
	}
}
