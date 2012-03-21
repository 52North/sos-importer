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

import java.util.ArrayList;
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
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;
import org.n52.sos.importer.view.dateAndTime.MissingDayPanel;
import org.n52.sos.importer.view.dateAndTime.MissingHourPanel;
import org.n52.sos.importer.view.dateAndTime.MissingMinutePanel;
import org.n52.sos.importer.view.dateAndTime.MissingMonthPanel;
import org.n52.sos.importer.view.dateAndTime.MissingSecondPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimePanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;
import org.n52.sos.importer.view.dateAndTime.MissingYearPanel;

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
		if (!missingComponentPanels.isEmpty()) return missingComponentPanels;
	
		if (dateAndTime.getDay() == null && 
			dateAndTime.getMonth() == null && 
			dateAndTime.getYear() == null)
			missingComponentPanels.add(new MissingDatePanel(dateAndTime));
		else {
			if (dateAndTime.getDay() == null)
				missingComponentPanels.add(new MissingDayPanel(dateAndTime));
			if (dateAndTime.getMonth() == null) 
				missingComponentPanels.add(new MissingMonthPanel(dateAndTime));
			if (dateAndTime.getYear() == null)
				missingComponentPanels.add(new MissingYearPanel(dateAndTime));
		}
		if (dateAndTime.getHour() == null && 
			dateAndTime.getMinute() == null && 
			dateAndTime.getSecond() == null)
				missingComponentPanels.add(new MissingTimePanel(dateAndTime));
			else {
				if (dateAndTime.getHour() == null)
					missingComponentPanels.add(new MissingHourPanel(dateAndTime));
				if (dateAndTime.getMinute() == null) 
					missingComponentPanels.add(new MissingMinutePanel(dateAndTime));
				if (dateAndTime.getSecond() == null)
					missingComponentPanels.add(new MissingSecondPanel(dateAndTime));
			}
		
		if (dateAndTime.getTimeZone() == null)
			missingComponentPanels.add(new MissingTimeZonePanel(dateAndTime));
		
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
			components.add((Component)mcp.getMissingComponent());
		return components;
	}
	
	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	public void setDateAndTime(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	public void assignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.assignValues();
	}
	
	public void unassignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.unassignValues();
	}
	
	
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
		if (dateAndTime.getSecond() != null)
			dateAndTime.getSecond().mark();
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
	
	public String forThis(Cell measuredValuePosition) {
		int second = dateAndTime.getSecond().getParsedValue(measuredValuePosition);
		int minute = dateAndTime.getMinute().getParsedValue(measuredValuePosition);
		int hour = dateAndTime.getHour().getParsedValue(measuredValuePosition);
		int day = dateAndTime.getDay().getParsedValue(measuredValuePosition);
		int month = dateAndTime.getMonth().getParsedValue(measuredValuePosition) + 1;
		int year = dateAndTime.getYear().getParsedValue(measuredValuePosition);
		int timezone = dateAndTime.getTimeZone().getParsedValue(measuredValuePosition);
		
		String timeStamp = year + "-" + month + "-" + day + "T" +
			hour + ":" + minute + ":" + second + convertTimeZone(timezone);	
		
		return timeStamp;
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
		logger.info("Merge Date & Times");
		List<DateAndTime> dateAndTimes = ModelStore.getInstance().getDateAndTimes();
		List<DateAndTime> mergedDateAndTimes = new ArrayList<DateAndTime>();
		for (int i = 0; i < dateAndTimes.size(); i++) {
			DateAndTime dt1 = dateAndTimes.get(i);
			dateAndTimes.remove(dt1);
			for (int j = 0; j < dateAndTimes.size(); j++) {
				DateAndTime dt2 = dateAndTimes.get(j);
				if (dt1.getGroup().equals(dt2.getGroup())) {
					merge(dt1, dt2);
					dateAndTimes.remove(dt2);
				}
			}
			mergedDateAndTimes.add(dt1);
		}
		ModelStore.getInstance().setDateAndTimes(mergedDateAndTimes);
	}
	
	private void merge(DateAndTime dateAndTime1, DateAndTime dateAndTime2) {
		if (dateAndTime1.getSecond() == null && dateAndTime2.getSecond() != null)
			dateAndTime1.setSecond(dateAndTime2.getSecond());
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
