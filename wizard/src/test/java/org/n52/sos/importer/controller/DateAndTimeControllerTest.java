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
package org.n52.sos.importer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.Column;

public class DateAndTimeControllerTest {

    @Test
    public void testMergeDateAndTimes() {
        int firstLineWithData = 1;
        String groupId = "1";
        //
        // first element
        String p1 = "d.M.yyyy";
        Column c1 = new Column(0, firstLineWithData);
        DateAndTime dt1 = new DateAndTime();
        Day day = new Day(c1, p1);
        dt1.setDay(day);
        Month month = new Month(c1, p1);
        dt1.setMonth(month);
        Year year = new Year(c1, p1);
        dt1.setYear(year);
        dt1.setGroup(groupId);
        //
        // second element
        String p2 = "HH,00";
        Column c2 = new Column(1, firstLineWithData);
        DateAndTime dt2 = new DateAndTime();
        Hour hour = new Hour(c2, p2);
        dt2.setHour(hour);
        dt2.setGroup(groupId);
        //
        // third element
        String p3 = "mm,00";
        Column c3 = new Column(2, firstLineWithData);
        DateAndTime dt3 = new DateAndTime();
        Minute minute = new Minute(c3, p3);
        dt3.setMinute(minute);
        dt3.setGroup(groupId);
        //
        // add elements to modelstore
        ModelStore ms = ModelStore.getInstance();
        ms.add(dt1);
        ms.add(dt2);
        ms.add(dt3);
        //
        // DateAndTimeController
        DateAndTimeController dtc = new DateAndTimeController();
        dtc.mergeDateAndTimes();

        assertThat(ms.getDateAndTimes(), is(notNullValue()));
        assertThat(ms.getDateAndTimes().size(), is(1));
        DateAndTime dat = ms.getDateAndTimes().get(0);
        assertThat(dat.getGroup(), is(groupId));
        assertThat(dat.getYear(), is(year));
        assertThat(dat.getMonth(), is(month));
        assertThat(dat.getDay(), is(day));
        assertThat(dat.getHour(), is(hour));
        assertThat(dat.getMinute(), is(minute));

    }

}
