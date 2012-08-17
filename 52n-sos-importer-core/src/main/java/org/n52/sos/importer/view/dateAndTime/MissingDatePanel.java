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
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for year, month and day
 * @author Raimund
 */
public class MissingDatePanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dateLabel = new JLabel(Lang.l().date() + ": ");
	
	private SpinnerDateModel dateModel;
	private JSpinner dateSpinner;
	
	public MissingDatePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		GregorianCalendar calendar = new GregorianCalendar();
		Date initDate = calendar.getTime();
		calendar.add(Calendar.YEAR, -100);
		Date earliestDate = calendar.getTime();
		calendar.add(Calendar.YEAR, 200);
		Date latestDate = calendar.getTime();
		dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dateLabel);
		this.add(dateSpinner);
	}

	@Override
	public void assignValues() {
		Calendar c = new GregorianCalendar();
		c.setTime(dateModel.getDate());
		dateAndTime.setDay(new Day(c.get(Calendar.DAY_OF_MONTH)));
		dateAndTime.setMonth(new Month(c.get(Calendar.MONTH)));
		dateAndTime.setYear(new Year(c.get(Calendar.YEAR)));
	}

	@Override
	public void unassignValues() {
		dateAndTime.setDay(null);
		dateAndTime.setMonth(null);
		dateAndTime.setYear(null);	
	}

	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		Calendar c = new GregorianCalendar();
		c.setTime(dateModel.getDate());
		org.n52.sos.importer.model.dateAndTime.Date date = new org.n52.sos.importer.model.dateAndTime.Date();
		date.setDay(new Day(c.get(Calendar.DAY_OF_MONTH)));
		date.setMonth(new Month(c.get(Calendar.MONTH) + 1));
		date.setYear(new Year(c.get(Calendar.YEAR)));
		return date;
	}

	@Override
	public void setMissingComponent(Component c) {
		org.n52.sos.importer.model.dateAndTime.Date date = (org.n52.sos.importer.model.dateAndTime.Date)c;
		int year = date.getYear().getValue();
		int month = date.getMonth().getValue() - 1;
		int day = date.getDay().getValue();
		GregorianCalendar gc = new GregorianCalendar(year, month, day, 0, 0, 0);
		dateModel.setValue(gc.getTime());
	}
}
