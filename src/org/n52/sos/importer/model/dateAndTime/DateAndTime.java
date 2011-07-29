package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Combination;

public class DateAndTime extends Combination {
	
	private static final Logger logger = Logger.getLogger(DateAndTime.class);
	
	private Year year;
	private Month month;
	private Day day;
	private Hour hour;
	private Minute minute;
	private Second second;
	private TimeZone timeZone;
	
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		logger.info("Add " + year + " to " + this);
		this.year = year;
	}
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		logger.info("Add " + month + " to " + this);
		this.month = month;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		logger.info("Add " + day + " to " + this);
		this.day = day;
	}
	public Hour getHour() {
		return hour;
	}
	public void setHour(Hour hour) {
		logger.info("Add " + hour + " to " + this);
		this.hour = hour;
	}
	public Minute getMinute() {
		return minute;
	}
	public void setMinute(Minute minute) {
		logger.info("Add " + minute + " to " + this);
		this.minute = minute;
	}
	public Second getSecond() {
		return second;
	}
	public void setSecond(Second second) {
		logger.info("Add " + second + " to " + this);
		this.second = second;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		logger.info("Add " + timeZone + " to " + this);
		this.timeZone = timeZone;
	}	
	
	@Override
	public Object parse(String s) {
		Date dateTime = null;
		String currentPattern = getPattern();
		SimpleDateFormat formatter =
	           new SimpleDateFormat(currentPattern);      	
        try {
        	dateTime = formatter.parse(s);
		} catch (ParseException e) {
			throw new NumberFormatException();
		}
		return dateTime;
	}
	
	@Override
	public String format(Object o) {
        Date date = (Date)o;		        
    	SimpleDateFormat formatter =
	           new SimpleDateFormat(getPattern());      	
        String dateString = formatter.format(date);

		return dateString;
	}
	
	@Override
	public String toString() {
		return "Date&Time group " + getGroup();
	}
}
