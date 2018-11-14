/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
 *
 * @author Raimund
 */
public class MissingDatePanel extends MissingDateAndTimePanel {

    private static final long serialVersionUID = 1L;

    private final JLabel dateLabel = new JLabel(Lang.l().date() + ": ");

    private SpinnerDateModel dateModel;
    private JSpinner dateSpinner;

    /**
     * <p>Constructor for MissingDatePanel.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
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
        org.n52.sos.importer.model.dateAndTime.Date date = (org.n52.sos.importer.model.dateAndTime.Date) c;
        int year = date.getYear().getValue();
        int month = date.getMonth().getValue() - 1;
        int day = date.getDay().getValue();
        GregorianCalendar gc = new GregorianCalendar(year, month, day, 0, 0, 0);
        dateModel.setValue(gc.getTime());
    }
}
