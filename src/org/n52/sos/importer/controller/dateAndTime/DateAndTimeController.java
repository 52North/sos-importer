package org.n52.sos.importer.controller.dateAndTime;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;
import org.n52.sos.importer.model.dateAndTime.DayModel;
import org.n52.sos.importer.model.dateAndTime.HourModel;
import org.n52.sos.importer.model.dateAndTime.MinuteModel;
import org.n52.sos.importer.model.dateAndTime.MonthModel;
import org.n52.sos.importer.model.dateAndTime.SecondModel;
import org.n52.sos.importer.model.dateAndTime.TimeZoneModel;
import org.n52.sos.importer.model.dateAndTime.YearModel;
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
	
	public DateAndTimeController() {
		dateAndTimeModel = new DateAndTimeModel();
	}
	
	public List<JPanel> getMissingComponents() {
		List<JPanel> missingComponents = new ArrayList<JPanel>();
		
		if (dateAndTimeModel.getDayModel() == null && 
			dateAndTimeModel.getMonthModel() == null && 
			dateAndTimeModel.getYearModel() == null)
			missingComponents.add(new MissingDatePanel());
		else {
			if (dateAndTimeModel.getDayModel() == null)
				missingComponents.add(new MissingDayPanel());
			if (dateAndTimeModel.getMonthModel() == null) 
				missingComponents.add(new MissingMonthPanel());
			if (dateAndTimeModel.getYearModel() == null)
				missingComponents.add(new MissingYearPanel());
		}
		if (dateAndTimeModel.getHourModel() == null && 
			dateAndTimeModel.getMinuteModel() == null && 
			dateAndTimeModel.getSecondModel() == null)
				missingComponents.add(new MissingTimePanel());
			else {
				if (dateAndTimeModel.getHourModel() == null)
					missingComponents.add(new MissingHourPanel());
				if (dateAndTimeModel.getMinuteModel() == null) 
					missingComponents.add(new MissingMinutePanel());
				if (dateAndTimeModel.getSecondModel() == null)
					missingComponents.add(new MissingSecondPanel());
			}
		
		if (dateAndTimeModel.getTimeZoneModel() == null)
			missingComponents.add(new MissingTimeZonePanel());
		
		return missingComponents;
	}	
	
	public void assignPattern(String pattern) {
    	if (pattern.indexOf("y") != -1) dateAndTimeModel.setYearModel(new YearModel());
    	if (pattern.indexOf("M") != -1 || pattern.indexOf("w") != -1 || pattern.indexOf("D") != -1) 
    		dateAndTimeModel.setMonthModel(new MonthModel());
    	if (pattern.indexOf("d") != -1 || (pattern.indexOf("W") != -1 && pattern.indexOf("d") != -1)) 
    		dateAndTimeModel.setDayModel(new DayModel());
    	if (pattern.indexOf("H") != -1 || pattern.indexOf("k") != -1 || ((pattern.indexOf("K") != -1 || (pattern.indexOf("h") != -1) && pattern.indexOf("a") != -1))) 
    		dateAndTimeModel.setHourModel(new HourModel());
    	if (pattern.indexOf("m") != -1)
    		dateAndTimeModel.setMinuteModel(new MinuteModel());
    	if (pattern.indexOf("s") != -1)
    		dateAndTimeModel.setSecondModel(new SecondModel());
    	if (pattern.indexOf("Z") != -1 || pattern.indexOf("z") != -1)
    		dateAndTimeModel.setTimeZoneModel(new TimeZoneModel());
	}
}
