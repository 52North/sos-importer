package org.n52.sos.importer.model.dateAndTime;

public class DateAndTimeModel {

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
		this.yearModel = yearModel;
	}
	public MonthModel getMonthModel() {
		return monthModel;
	}
	public void setMonthModel(MonthModel monthModel) {
		this.monthModel = monthModel;
	}
	public DayModel getDayModel() {
		return dayModel;
	}
	public void setDayModel(DayModel dayModel) {
		this.dayModel = dayModel;
	}
	public HourModel getHourModel() {
		return hourModel;
	}
	public void setHourModel(HourModel hourModel) {
		this.hourModel = hourModel;
	}
	public MinuteModel getMinuteModel() {
		return minuteModel;
	}
	public void setMinuteModel(MinuteModel minuteModel) {
		this.minuteModel = minuteModel;
	}
	public SecondModel getSecondModel() {
		return secondModel;
	}
	public void setSecondModel(SecondModel secondModel) {
		this.secondModel = secondModel;
	}
	public TimeZoneModel getTimeZoneModel() {
		return timeZoneModel;
	}
	public void setTimeZoneModel(TimeZoneModel timeZoneModel) {
		this.timeZoneModel = timeZoneModel;
	}
	
}
