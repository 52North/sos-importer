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
package org.n52.sos.importer.view.step3;

import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
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
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * selection panel in step 3 for date&time combinations
 * @author Raimund
 *
 */
public class DateAndTimeCombinationPanel extends CombinationPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;
	
	private DateAndTime dateAndTime;
	
	public DateAndTimeCombinationPanel(JPanel containerPanel, 
			int firstLineWithData) {	
		super(containerPanel,firstLineWithData);
		this.dateAndTime = new DateAndTime();
	}

	@Override
	public DateAndTime getCombination() {
		if (dateAndTime == null) {
			dateAndTime = new DateAndTime();
		}
		return dateAndTime;
	}

	@Override
	public String[] getGroupItems() {
		return ComboBoxItems.getInstance().getDateAndTimeGroups();
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
	public String getPatternToolTip() {
		return ToolTips.get(ToolTips.DATE_AND_TIME_PATTERNS);
	}
	
	@Override
	public String getGroupToolTip() {
		return ToolTips.get(ToolTips.DATE_AND_TIME_GROUPS);
	}
	
	@Override
	public void assign(TableElement tableElement) {
    	String[] part = getSelection().split(Constants.SEPARATOR_STRING);
		String pattern = part[0];
		String group = part[1];

		DateAndTime dtm = new DateAndTime();
		dtm.setGroup(group);
		DateAndTimeController dtc = new DateAndTimeController(dtm);
		dtc.assignPattern(pattern, tableElement);
		// TODO move action to controller (GUI should not manipulate the model)
		ModelStore.getInstance().add(dtm);
	}

	@Override
	public void unAssign(TableElement tableElement) {
		DateAndTime dateAndTimeToRemove = null;
		for (DateAndTime dtm: ModelStore.getInstance().getDateAndTimes()) {
			Second second = dtm.getSeconds();
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
