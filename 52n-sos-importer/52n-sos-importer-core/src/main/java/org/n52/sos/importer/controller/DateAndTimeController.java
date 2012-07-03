/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;

/**
 * handles operations on DateAndTime objects
 * @author Raimund
 * 
 */
public class DateAndTimeController {
	
	private static final Logger logger = Logger.getLogger(DateAndTimeController.class);
	
	private DateAndTime dateAndTime;
	
	private List<MissingComponentPanel> missingComponentPanels;
	
	public DateAndTimeController() {
		dateAndTime = new DateAndTime();
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
	}
	
	public DateAndTimeController(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
	}
	
	public List<MissingComponentPanel> getMissingComponentPanels() {	
		if (!missingComponentPanels.isEmpty()) {
			return missingComponentPanels;
		}
		/*
		 * 	DATE SECTION
		 *
		 * whole date component is missing
		 */
		/* Disabled because the user might enter observations on year or month level without giving a date
		if (dateAndTime.getDay() == null && 
				dateAndTime.getMonth() == null && 
				dateAndTime.getYear() == null) {
			missingComponentPanels.add(new MissingDatePanel(dateAndTime));
		} else {
			// not all elements of date are missing
			if (dateAndTime.getDay() == null) {
				missingComponentPanels.add(new MissingDayPanel(dateAndTime));
			}
			if (dateAndTime.getMonth() == null) {
				missingComponentPanels.add(new MissingMonthPanel(dateAndTime));
			}
			if (dateAndTime.getYear() == null) {
				missingComponentPanels.add(new MissingYearPanel(dateAndTime));
			}
		}
		*/
		/*
		 * 	TIME SECTION
		 * 
		 * whole time is missing
		 */
		/* Disabled because the user might enter observations on year, month, or day level without giving a specific time
		if (dateAndTime.getHour() == null && 
				dateAndTime.getMinute() == null && 
				dateAndTime.getSeconds() == null) {
			missingComponentPanels.add(new MissingTimePanel(dateAndTime));
		}else {
			// not all elements of time are missing
			if (dateAndTime.getHour() == null) {
				missingComponentPanels.add(new MissingHourPanel(dateAndTime));
			}
			if (dateAndTime.getMinute() == null) {
				missingComponentPanels.add(new MissingMinutePanel(dateAndTime));
			}
			if (dateAndTime.getSeconds() == null) {
				missingComponentPanels.add(new MissingSecondPanel(dateAndTime));
			}
		}
		*/
		/*
		 * 	TIME_ZONE SECTION
		 *  Only add if at least one time element is already set
		 */
		if (dateAndTime.getTimeZone() == null && ( 
				(dateAndTime.getHour() != null && dateAndTime.getHour().getTableElement() != null) || 
				(dateAndTime.getMinute() != null && dateAndTime.getMinute().getTableElement() != null) ||
				(dateAndTime.getSeconds() != null && dateAndTime.getSeconds().getTableElement() != null)) ) {
			missingComponentPanels.add(new MissingTimeZonePanel(dateAndTime));
		}
		//
		return missingComponentPanels;
	}	
	
	public void setMissingComponents(List<Component> components) {
		for (Component c: components) {
			MissingComponentPanel mcp = c.getMissingComponentPanel(dateAndTime);
			mcp.setMissingComponent(c);
			missingComponentPanels.add(mcp);
		}
	}
	
	public List<Component> getMissingComponents() {
		List<Component> components = new ArrayList<Component>();
		for (MissingComponentPanel mcp: missingComponentPanels) 
			components.add(mcp.getMissingComponent());
		return components;
	}
	
	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	public void setDateAndTime(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	/**
	 * Assigns for each MissingComponentPanel the values
	 */
	public void assignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) {
			mcp.assignValues();
		}
	}
	
	public void unassignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.unassignValues();
	}
	
	
	/**
	 * Method checks the <code>pattern</code> and sets the members of the 
	 * controlled <code>DateAndTime</code> instance.
	 * @param pattern
	 * @param tableElement
	 */
	public void assignPattern(String pattern, TableElement tableElement) {		
		logger.info("Assign pattern " + pattern + " to " + dateAndTime + " in " + tableElement);
		
    	if (pattern.indexOf("y") != -1) 
    		dateAndTime.setYear(new Year(tableElement, pattern));
    	if (pattern.indexOf("M") != -1 || pattern.indexOf("w") != -1 || pattern.indexOf("D") != -1) 
    		dateAndTime.setMonth(new Month(tableElement, pattern));
    	if (pattern.indexOf("d") != -1 || (pattern.indexOf("W") != -1 && pattern.indexOf("d") != -1)) 
    		dateAndTime.setDay(new Day(tableElement, pattern));
    	if (pattern.indexOf("H") != -1 || pattern.indexOf("k") != -1 || ((pattern.indexOf("K") != -1 || (pattern.indexOf("h") != -1) && pattern.indexOf("a") != -1))) 
    		dateAndTime.setHour(new Hour(tableElement, pattern));
    	if (pattern.indexOf("m") != -1)
    		dateAndTime.setMinute(new Minute(tableElement, pattern));
    	if (pattern.indexOf("s") != -1)
    		dateAndTime.setSecond(new Second(tableElement, pattern));
    	if (pattern.indexOf("Z") != -1 || pattern.indexOf("z") != -1)
    		dateAndTime.setTimeZone(new TimeZone(tableElement, pattern));
	}
	
	public DateAndTime getNextDateAndTimeWithMissingValues() {
		List<MissingComponentPanel> missingComponentPanels;
		
		for (DateAndTime dateAndTime: ModelStore.getInstance().getDateAndTimes()) {
			setDateAndTime(dateAndTime);
			missingComponentPanels = getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return dateAndTime;
		}
		return null;
	}
	
	public void markComponents() {
		if (dateAndTime.getSeconds() != null)
			dateAndTime.getSeconds().mark();
		if (dateAndTime.getMinute() != null) 
			dateAndTime.getMinute().mark();
		if (dateAndTime.getHour() != null)
			dateAndTime.getHour().mark();
		if (dateAndTime.getDay() != null)
			dateAndTime.getDay().mark();
		if (dateAndTime.getMonth() != null) 
			dateAndTime.getMonth().mark();
		if (dateAndTime.getYear() != null)
			dateAndTime.getYear().mark();
		if (dateAndTime.getTimeZone() != null)
			dateAndTime.getTimeZone().mark();
	}
	
	public String forThis(Cell measuredValuePosition) throws ParseException {
		int second = dateAndTime.getSeconds()!=null?dateAndTime.getSeconds().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		int minute = dateAndTime.getMinute()!=null?dateAndTime.getMinute().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		int hour = dateAndTime.getHour()!=null?dateAndTime.getHour().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		int day = dateAndTime.getDay()!=null?dateAndTime.getDay().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		int month = dateAndTime.getMonth()!=null?dateAndTime.getMonth().getParsedValue(measuredValuePosition) + 1:Integer.MIN_VALUE;
		int year = dateAndTime.getYear()!=null?dateAndTime.getYear().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		int timezone = dateAndTime.getTimeZone()!=null?dateAndTime.getTimeZone().getParsedValue(measuredValuePosition):Integer.MIN_VALUE;
		
		StringBuffer ts = new StringBuffer(25); // <- yyyy-mm-ddThh:mm:ss+hh:mm
		if (year != Integer.MIN_VALUE) {
			ts.append(year);
			if (month != Integer.MIN_VALUE) {
				ts.append("-");
			}
		}
		if (month != Integer.MIN_VALUE) {
			ts.append(month<10?"0"+month:month);
			if (day != Integer.MIN_VALUE) {
				ts.append("-");
			}
		}
		if (day != Integer.MIN_VALUE) {
			ts.append(day<10?"0"+day:day);
		}
		if ( (year != Integer.MIN_VALUE || month != Integer.MIN_VALUE || day != Integer.MIN_VALUE )
				&& (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || second != Integer.MIN_VALUE) ) {
			ts.append("T");
		}
		if (hour != Integer.MIN_VALUE) {
			ts.append(hour<10?"0"+hour:hour);
			if (minute != Integer.MIN_VALUE) {
				ts.append(":");
			}
		}
		if (minute != Integer.MIN_VALUE) {
			ts.append(minute<10?"0"+minute:minute);
			if (second != Integer.MIN_VALUE) {
				ts.append("");
			}
		}
		if (second != Integer.MIN_VALUE) {
			ts.append(second<10?"0"+second:second);
		}
		if (timezone != Integer.MIN_VALUE && 
				(hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || second != Integer.MIN_VALUE) ) {
			ts.append(convertTimeZone(timezone));
		}
		
		return ts.toString();
	}
	
	private String convertTimeZone(int timeZone) {
		if (timeZone >= 0) {
			if (timeZone >= 10) return "+" + timeZone + ":00";
			else return "+0" + timeZone + ":00";
		} else {
			if (timeZone <= -10) return timeZone + ":00";
			else return "-0" + Math.abs(timeZone) + ":00";
		}
	}
	
	
	public void mergeDateAndTimes() {
		if (logger.isTraceEnabled()) {
			logger.trace("mergeDateAndTimes()");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Merge Date & Times");
		}
		List<DateAndTime> dateAndTimes = ModelStore.getInstance().getDateAndTimes();
		ArrayList<DateAndTime> mergedDateAndTimes = new ArrayList<DateAndTime>(dateAndTimes.size());
		while (!dateAndTimes.isEmpty()) {
			DateAndTime dt1 = dateAndTimes.get(0);
			dateAndTimes.remove(dt1);
			// create tmp list from left over dts
			List<DateAndTime> list2 = new ArrayList<DateAndTime>(dateAndTimes);
			Iterator<DateAndTime> dATIter = list2.iterator();
			while (dATIter.hasNext()) {
				DateAndTime dt2 = dATIter.next();
				if (dt1.getGroup().equals(dt2.getGroup())) {
					this.merge(dt1, dt2);
					dateAndTimes.remove(dt2);
				}
			}
			mergedDateAndTimes.add(dt1);
		}
		mergedDateAndTimes.trimToSize();
		ModelStore.getInstance().setDateAndTimes(mergedDateAndTimes);
	}
	
	private void merge(DateAndTime dateAndTime1, DateAndTime dateAndTime2) {
		if (dateAndTime1.getSeconds() == null && dateAndTime2.getSeconds() != null)
			dateAndTime1.setSecond(dateAndTime2.getSeconds());
		if (dateAndTime1.getMinute() == null && dateAndTime2.getMinute() != null) 
			dateAndTime1.setMinute(dateAndTime2.getMinute());
		if (dateAndTime1.getHour() == null && dateAndTime2.getHour() != null)
			dateAndTime1.setHour(dateAndTime2.getHour());
		if (dateAndTime1.getDay() == null && dateAndTime2.getDay() != null)
			dateAndTime1.setDay(dateAndTime2.getDay());
		if (dateAndTime1.getMonth() == null && dateAndTime2.getMonth() != null) 
			dateAndTime1.setMonth(dateAndTime2.getMonth());
		if (dateAndTime1.getYear() == null && dateAndTime2.getYear() != null)
			dateAndTime1.setYear(dateAndTime2.getYear());
		if (dateAndTime1.getTimeZone() == null && dateAndTime2.getTimeZone() != null)
			dateAndTime1.setTimeZone(dateAndTime2.getTimeZone());
	}
}
