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
