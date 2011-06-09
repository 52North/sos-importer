package org.n52.sos.importer.model.dateAndTime;

import org.apache.log4j.Logger;

public class DateAndTimeModel {

	private static final Logger logger = Logger.getLogger(DateAndTimeModel.class);
	
	private YearModel yearModel;
	private MonthModel monthModel;
	private DayModel dayModel;
	private HourModel hourModel;
	private MinuteModel minuteModel;
	private SecondModel secondModel;
	private TimeZoneModel timeZoneModel;
	
	public YearModel getYearModel() {
		return yearModel;
	}
	public void setYearModel(YearModel yearModel) {
		logger.info("Assign Year to Date & Time");
		this.yearModel = yearModel;
	}
	public MonthModel getMonthModel() {
		return monthModel;
	}
	public void setMonthModel(MonthModel monthModel) {
		logger.info("Assign Month to Date & Time");
		this.monthModel = monthModel;
	}
	public DayModel getDayModel() {
		return dayModel;
	}
	public void setDayModel(DayModel dayModel) {
		logger.info("Assign Day to Date & Time");
		this.dayModel = dayModel;
	}
	public HourModel getHourModel() {
		return hourModel;
	}
	public void setHourModel(HourModel hourModel) {
		logger.info("Assign Hour to Date & Time");
		this.hourModel = hourModel;
	}
	public MinuteModel getMinuteModel() {
		return minuteModel;
	}
	public void setMinuteModel(MinuteModel minuteModel) {
		logger.info("Assign Minute to Date & Time");
		this.minuteModel = minuteModel;
	}
	public SecondModel getSecondModel() {
		return secondModel;
	}
	public void setSecondModel(SecondModel secondModel) {
		logger.info("Assign Second to Date & Time");
		this.secondModel = secondModel;
	}
	public TimeZoneModel getTimeZoneModel() {
		return timeZoneModel;
	}
	public void setTimeZoneModel(TimeZoneModel timeZoneModel) {
		logger.info("Assign Timezone to Date & Time");
		this.timeZoneModel = timeZoneModel;
	}
	
}
