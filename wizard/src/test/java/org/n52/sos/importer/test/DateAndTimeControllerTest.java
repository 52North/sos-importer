/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to test the methods of the date and time controller
 *
 * @author e.h.juerrens@52north.org
 * @version $Id: $Id
 * @since 0.5.0
 */
public class DateAndTimeControllerTest {


    private static final Logger logger =
            LoggerFactory.getLogger(DateAndTimeControllerTest.class);

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    //CHECKSTYLE:OFF
    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        // CHECKSTYLE_ON
        if (logger.isDebugEnabled()) {
            logger.debug("Start Test");
        }
        final int firstLineWithData = 1;
        final String g = "1";
        //
        // first element
        final String p1 = "d.M.yyyy";
        final Column c1 = new Column(0, firstLineWithData);
        final DateAndTime dt1 = new DateAndTime();
        dt1.setDay(new Day(c1, p1));
        dt1.setMonth(new Month(c1, p1));
        dt1.setYear(new Year(c1, p1));
        dt1.setGroup(g);
        //
        // second element
        final String p2 = "HH,00";
        final Column c2 = new Column(1, firstLineWithData);
        final DateAndTime dt2 = new DateAndTime();
        dt2.setHour(new Hour(c2, p2));
        dt2.setGroup(g);
        //
        // third element
        final String p3 = "mm,00";
        final Column c3 = new Column(2, firstLineWithData);
        final DateAndTime dt3 = new DateAndTime();
        dt3.setHour(new Hour(c3, p3));
        dt3.setGroup(g);
        //
        // add elements to modelstore
        final ModelStore ms = ModelStore.getInstance();
        ms.add(dt1);
        ms.add(dt2);
        ms.add(dt3);
        //
        if (logger.isDebugEnabled()) {
            logger.debug("Example data created");
        }
        //
        // DateAndTimeController
        final DateAndTimeController dtc = new DateAndTimeController();
        dtc.mergeDateAndTimes();
    }

}
