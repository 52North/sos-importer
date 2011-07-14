package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.n52.sos.importer.Formatable;
import org.n52.sos.importer.Parseable;

public class DateAndTime implements Formatable, Parseable {
	
	private Year year;
	private Month month;
	private Day day;
	private Hour hour;
	private Minute minute;
	private Second second;
	private TimeZone timeZone;
	
	/** for parsing */
	private String pattern;
	
	/** for merging */
	private String group;
	
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		this.month = month;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public Hour getHour() {
		return hour;
	}
	public void setHour(Hour hour) {
		this.hour = hour;
	}
	public Minute getMinute() {
		return minute;
	}
	public void setMinute(Minute minute) {
		this.minute = minute;
	}
	public Second getSecond() {
		return second;
	}
	public void setSecond(Second second) {
		this.second = second;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
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
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	@Override
	public String format(Object o) {
        Date date = (Date)o;		        
    	SimpleDateFormat formatter =
	           new SimpleDateFormat(getPattern());      	
        String dateString = formatter.format(date);

		return dateString;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getGroup() {
		return group;
	}
}
