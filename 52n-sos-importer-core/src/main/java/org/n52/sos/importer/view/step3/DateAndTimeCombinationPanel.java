/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
