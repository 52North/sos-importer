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
package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.Time;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for hour, minute and second
 * @author Raimund
 */
public class MissingTimePanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private JLabel timeLabel;

	private SpinnerDateModel timeModel;
	private JSpinner timeSpinner;
	
	public MissingTimePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		timeModel = new SpinnerDateModel();
		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, 0, 0, 0);
		timeModel.setValue(gc.getTime());
		timeModel.setCalendarField(Calendar.HOUR_OF_DAY);
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.timeLabel = new JLabel(Lang.l().time() + ": ");
		
		this.add(timeLabel);
		this.add(timeSpinner);
	}	
	
	@Override
	public void assignValues() {
		Calendar c = new GregorianCalendar();
		c.setTime(timeModel.getDate());
		dateAndTime.setHour(new Hour(c.get(Calendar.HOUR_OF_DAY)));
		dateAndTime.setMinute(new Minute(c.get(Calendar.MINUTE)));
		dateAndTime.setSecond(new Second(c.get(Calendar.SECOND)));
	}

	@Override
	public void unassignValues() {
		dateAndTime.setHour(null);
		dateAndTime.setMinute(null);
		dateAndTime.setSecond(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}

	@Override
	public Component getMissingComponent() {
		Calendar c = new GregorianCalendar();
		c.setTime(timeModel.getDate());
		Time time = new Time();
		time.setHour(new Hour(c.get(Calendar.HOUR_OF_DAY)));
		time.setMinute(new Minute(c.get(Calendar.MINUTE)));
		time.setSecond(new Second(c.get(Calendar.SECOND)));
		return time;
	}

	@Override
	public void setMissingComponent(Component c) {
		Time time = (Time)c;
		int hour = time.getHour().getValue();
		int minute = time.getMinute().getValue();
		int second = time.getSecond().getValue();
		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, hour, minute, second);
		timeModel.setValue(gc.getTime());
	}
}
