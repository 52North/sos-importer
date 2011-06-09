package org.n52.sos.importer.controller.dateAndTime;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;
import org.n52.sos.importer.model.dateAndTime.DayModel;
import org.n52.sos.importer.model.dateAndTime.HourModel;
import org.n52.sos.importer.model.dateAndTime.MinuteModel;
import org.n52.sos.importer.model.dateAndTime.MonthModel;
import org.n52.sos.importer.model.dateAndTime.SecondModel;
import org.n52.sos.importer.model.dateAndTime.TimeZoneModel;
import org.n52.sos.importer.model.dateAndTime.YearModel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;
import org.n52.sos.importer.view.dateAndTime.MissingDayPanel;
import org.n52.sos.importer.view.dateAndTime.MissingHourPanel;
import org.n52.sos.importer.view.dateAndTime.MissingMinutePanel;
import org.n52.sos.importer.view.dateAndTime.MissingMonthPanel;
import org.n52.sos.importer.view.dateAndTime.MissingSecondPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimePanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;
import org.n52.sos.importer.view.dateAndTime.MissingYearPanel;


public class DateAndTimeController {
	
	private DateAndTimeModel dateAndTimeModel;
	
	private List<MissingComponentPanel> missingComponentPanels;
	
	public DateAndTimeController() {
		dateAndTimeModel = new DateAndTimeModel();
	}
	
	public DateAndTimeController(DateAndTimeModel dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}
	
	public List<MissingComponentPanel> getMissingComponentPanels() {		
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
		
		if (dateAndTimeModel.getDayModel() == null && 
			dateAndTimeModel.getMonthModel() == null && 
			dateAndTimeModel.getYearModel() == null)
			missingComponentPanels.add(new MissingDatePanel(this));
		else {
			if (dateAndTimeModel.getDayModel() == null)
				missingComponentPanels.add(new MissingDayPanel(this));
			if (dateAndTimeModel.getMonthModel() == null) 
				missingComponentPanels.add(new MissingMonthPanel(this));
			if (dateAndTimeModel.getYearModel() == null)
				missingComponentPanels.add(new MissingYearPanel(this));
		}
		if (dateAndTimeModel.getHourModel() == null && 
			dateAndTimeModel.getMinuteModel() == null && 
			dateAndTimeModel.getSecondModel() == null)
				missingComponentPanels.add(new MissingTimePanel(this));
			else {
				if (dateAndTimeModel.getHourModel() == null)
					missingComponentPanels.add(new MissingHourPanel(this));
				if (dateAndTimeModel.getMinuteModel() == null) 
					missingComponentPanels.add(new MissingMinutePanel(this));
				if (dateAndTimeModel.getSecondModel() == null)
					missingComponentPanels.add(new MissingSecondPanel(this));
			}
		
		if (dateAndTimeModel.getTimeZoneModel() == null)
			missingComponentPanels.add(new MissingTimeZonePanel(this));
		
		return missingComponentPanels;
	}	
	
	public DateAndTimeModel getModel() {
		return dateAndTimeModel;
	}
	
	public void setModel(DateAndTimeModel dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}
	
	public void assignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.assignValues();
	}
	
	public void assignPattern(String pattern, TableElement tableElement) {
    	if (pattern.indexOf("y") != -1) dateAndTimeModel.setYearModel(new YearModel(tableElement));
    	if (pattern.indexOf("M") != -1 || pattern.indexOf("w") != -1 || pattern.indexOf("D") != -1) 
    		dateAndTimeModel.setMonthModel(new MonthModel(tableElement));
    	if (pattern.indexOf("d") != -1 || (pattern.indexOf("W") != -1 && pattern.indexOf("d") != -1)) 
    		dateAndTimeModel.setDayModel(new DayModel(tableElement));
    	if (pattern.indexOf("H") != -1 || pattern.indexOf("k") != -1 || ((pattern.indexOf("K") != -1 || (pattern.indexOf("h") != -1) && pattern.indexOf("a") != -1))) 
    		dateAndTimeModel.setHourModel(new HourModel(tableElement));
    	if (pattern.indexOf("m") != -1)
    		dateAndTimeModel.setMinuteModel(new MinuteModel(tableElement));
    	if (pattern.indexOf("s") != -1)
    		dateAndTimeModel.setSecondModel(new SecondModel(tableElement));
    	if (pattern.indexOf("Z") != -1 || pattern.indexOf("z") != -1)
    		dateAndTimeModel.setTimeZoneModel(new TimeZoneModel(tableElement));
	}
	
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setDateAndTimeModel(dateAndTimeModel);	
	}

	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getDateAndTimeModel() != null;
	}

	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getDateAndTimeModel() == dateAndTimeModel)
				mv.setDateAndTimeModel(null);
		}		
	}
	
	public void mark(Color color) {
		if (dateAndTimeModel.getSecondModel() != null)
			dateAndTimeModel.getSecondModel().mark(color);
		if (dateAndTimeModel.getMinuteModel() != null) 
			dateAndTimeModel.getMinuteModel().mark(color);
		if (dateAndTimeModel.getHourModel() != null)
			dateAndTimeModel.getHourModel().mark(color);
		if (dateAndTimeModel.getDayModel() != null)
			dateAndTimeModel.getDayModel().mark(color);
		if (dateAndTimeModel.getMonthModel() != null) 
			dateAndTimeModel.getMonthModel().mark(color);
		if (dateAndTimeModel.getYearModel() != null)
			dateAndTimeModel.getYearModel().mark(color);
		if (dateAndTimeModel.getTimeZoneModel() != null)
			dateAndTimeModel.getTimeZoneModel().mark(color);
	}
	
}
