package org.n52.sos.importer;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;


public class Step5bPanel extends StepPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel descriptionLabel = new JLabel("Enter missing time information " +
														"when the measurements were taken.");
	private final JLabel timeLabel = new JLabel("Time: ");
	private final JLabel secondLabel = new JLabel("Seconds: ");
	private final JLabel minuteLabel = new JLabel("Minutes: ");
	private final JLabel hourLabel = new JLabel("Hours: ");
	private final JLabel dateLabel = new JLabel("Date: ");
	private final JLabel dayLabel = new JLabel("Day: ");
	private final JLabel monthLabel = new JLabel("Month: ");
	private final JLabel yearLabel = new JLabel("Year: ");
	private final JLabel timeZoneLabel = new JLabel("Timezone: ");

	private SpinnerNumberModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner secondSpinner = new JSpinner(secondModel);
	private SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner minuteSpinner = new JSpinner(minuteModel);
	private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner hourSpinner = new JSpinner(hourModel);	
	
	private SpinnerDateModel timeModel;
	private JSpinner timeSpinner;
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	private SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
	private JSpinner monthSpinner = new JSpinner(monthModel);
	private SpinnerNumberModel yearModel = new SpinnerNumberModel(2011, 1900, 2100, 1);
	private JSpinner yearSpinner = new JSpinner(yearModel);
	
	private SpinnerDateModel dateModel;
	private JSpinner dateSpinner;
	
	private SpinnerNumberModel timeZoneModel = new SpinnerNumberModel(0, -12, 12, 1);
	private JSpinner timeZoneSpinner = new JSpinner(timeZoneModel);
	
	public Step5bPanel(MainFrame mainFrame) {
		super(mainFrame);

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);
		this.add(descriptionPanel);
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		timePanel.add(hourLabel);
		timePanel.add(hourSpinner);
		timePanel.add(minuteLabel);
		timePanel.add(minuteSpinner);
		timePanel.add(secondLabel);
		timePanel.add(secondSpinner);
		this.add(timePanel);
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		datePanel.add(dayLabel);
		datePanel.add(daySpinner);
		datePanel.add(monthLabel);
		datePanel.add(monthSpinner);
		datePanel.add(yearLabel);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		datePanel.add(yearSpinner);
		this.add(datePanel);
		JPanel timeZonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		timeZonePanel.add(timeZoneLabel);
		timeZonePanel.add(timeZoneSpinner);
		this.add(timeZonePanel);
		initializeTimeSpinner();
		initializeDateSpinner();
	}
	
	private void initializeTimeSpinner() {
		timeModel = new SpinnerDateModel();
		timeModel.setCalendarField(Calendar.HOUR_OF_DAY);
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
		
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		timePanel.add(timeLabel);
		timePanel.add(timeSpinner);
		this.add(timePanel);
	}
	
	private void initializeDateSpinner() {
		GregorianCalendar calendar = new GregorianCalendar();
		Date initDate = calendar.getTime();
		calendar.add(Calendar.YEAR, -100);
		Date earliestDate = calendar.getTime();
		calendar.add(Calendar.YEAR, 200);
		Date latestDate = calendar.getTime();
		dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
		
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		datePanel.add(dateLabel);
		datePanel.add(dateSpinner);
		this.add(datePanel);
	}

	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getDescription() {
		return "Step 5: Add missing Metadata";
	}

	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void next() {
		// TODO Auto-generated method stub
		
	}

}
