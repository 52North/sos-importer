package org.n52.sos.importer.view.step3;

import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Combination;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.TableElement;

public class DateAndTimeCombinationPanel extends CombinationPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;
	
	private DateAndTime dateAndTime;
	
	public DateAndTimeCombinationPanel(JPanel containerPanel) {	
		super(containerPanel);
	}

	@Override
	public Combination getCombination() {
		if (dateAndTime == null) dateAndTime = new DateAndTime();
		return dateAndTime;
	}

	@Override
	public String[] getGroupItems() {
		return Settings.getInstance().getDateAndTimeGroups();
	}

	@Override
	public DefaultComboBoxModel getPatterns() {
		return EditableComboBoxItems.getInstance().getDateAndTimePatterns();
	}

	@Override
	public Object getTestValue() {
		return new Date();
	}
	
	@Override
	public void assign(TableElement tableElement) {
    	String[] part = getSelection().split("SEP");
		String pattern = part[0];
		String group = part[1];

		DateAndTime dtm = new DateAndTime();
		dtm.setGroup(group);
		DateAndTimeController dtc = new DateAndTimeController(dtm);
		dtc.assignPattern(pattern, tableElement);			
		ModelStore.getInstance().add(dtm);
	}

	@Override
	public void unassign(TableElement tableElement) {
		DateAndTime dateAndTimeToRemove = null;
		for (DateAndTime dtm: ModelStore.getInstance().getDateAndTimes()) {
			Second second = dtm.getSecond();
			Minute minute = dtm.getMinute();
			Hour hour = dtm.getHour();
			Day day = dtm.getDay();
			Month month = dtm.getMonth();
			Year year = dtm.getYear();
			TimeZone timeZone = dtm.getTimeZone();
			
			if ((second != null && tableElement.equals(second.getTableElement())) ||
				(minute != null && tableElement.equals(minute.getTableElement())) ||
				(hour != null && tableElement.equals(hour.getTableElement())) ||
				(day != null && tableElement.equals(day.getTableElement())) ||
				(month != null && tableElement.equals(month.getTableElement())) ||
				(year != null && tableElement.equals(year.getTableElement())) ||
				(timeZone != null && tableElement.equals(timeZone.getTableElement()))) {
				dateAndTimeToRemove = dtm;
				break;
			}
		}
		
		ModelStore.getInstance().remove(dateAndTimeToRemove);
	}
}
