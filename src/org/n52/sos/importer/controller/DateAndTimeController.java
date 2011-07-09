package org.n52.sos.importer.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;
import org.n52.sos.importer.view.dateAndTime.MissingDayPanel;
import org.n52.sos.importer.view.dateAndTime.MissingHourPanel;
import org.n52.sos.importer.view.dateAndTime.MissingMinutePanel;
import org.n52.sos.importer.view.dateAndTime.MissingMonthPanel;
import org.n52.sos.importer.view.dateAndTime.MissingSecondPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimePanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;
import org.n52.sos.importer.view.dateAndTime.MissingYearPanel;
import org.n52.sos.importer.view.position.MissingComponentPanel;

public class DateAndTimeController {
	
	private DateAndTime dateAndTime;
	
	private List<MissingComponentPanel> missingComponentPanels;
	
	public DateAndTimeController() {
		dateAndTime = new DateAndTime();
	}
	
	public DateAndTimeController(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	public List<MissingComponentPanel> getMissingComponentPanels() {		
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
		
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
	
	public void assignPattern(String pattern, TableElement tableElement) {
    	if (pattern.indexOf("y") != -1) dateAndTime.setYear(new Year(tableElement, pattern));
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
	
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setDateAndTime(dateAndTime);	
	}

	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getDateAndTime() != null;
	}

	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getDateAndTime() == dateAndTime)
				mv.setDateAndTime(null);
		}		
	}
	
	public DateAndTime getNextUnassignedDateAndTime() {
		for (DateAndTime dateAndTime: ModelStore.getInstance().getDateAndTimes())
			if (isAssignedToMeasuredValue(dateAndTime))
				return dateAndTime;	
		return null;
	}
	
	public DateAndTime getNextDateAndTimeWithMissingValues() {
		List<MissingComponentPanel> missingComponentPanels;
		
		for (DateAndTime dateAndTime: ModelStore.getInstance().getDateAndTimes()) {
			missingComponentPanels = getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return dateAndTime;
		}
		return null;
	}
	
	public boolean isAssignedToMeasuredValue(DateAndTime dateAndTime) {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) 
			if (mv.getDateAndTime().equals(dateAndTime))
				return true;
		return false;
	}
	
	public void mark(Color color) {
		if (dateAndTime.getSecond() != null)
			dateAndTime.getSecond().mark(color);
		if (dateAndTime.getMinute() != null) 
			dateAndTime.getMinute().mark(color);
		if (dateAndTime.getHour() != null)
			dateAndTime.getHour().mark(color);
		if (dateAndTime.getDay() != null)
			dateAndTime.getDay().mark(color);
		if (dateAndTime.getMonth() != null) 
			dateAndTime.getMonth().mark(color);
		if (dateAndTime.getYear() != null)
			dateAndTime.getYear().mark(color);
		if (dateAndTime.getTimeZone() != null)
			dateAndTime.getTimeZone().mark(color);
	}
	
	public GregorianCalendar forThis(Cell measuredValuePosition) {
		int second = dateAndTime.getSecond().getParsedValue(measuredValuePosition);
		int minute = dateAndTime.getMinute().getParsedValue(measuredValuePosition);
		int hour = dateAndTime.getHour().getParsedValue(measuredValuePosition);
		int day = dateAndTime.getDay().getParsedValue(measuredValuePosition);
		int month = dateAndTime.getMonth().getParsedValue(measuredValuePosition) + 1;
		int year = dateAndTime.getYear().getParsedValue(measuredValuePosition);
		int timezone = dateAndTime.getTimeZone().getParsedValue(measuredValuePosition);
		
		GregorianCalendar gc = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		gc.set(GregorianCalendar.ZONE_OFFSET, timezone);
		return gc;
	}
}
