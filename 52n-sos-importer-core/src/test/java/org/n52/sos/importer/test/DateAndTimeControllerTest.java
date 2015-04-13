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
 * @author e.h.juerrens@52north.org
 *
 */
public class DateAndTimeControllerTest {


	private static final Logger logger = 
			LoggerFactory.getLogger(DateAndTimeControllerTest.class);

	public static void main(final String[] args) {
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
		dt1.setDay(new Day(c1,p1));
		dt1.setMonth(new Month(c1,p1));
		dt1.setYear(new Year(c1,p1));
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
