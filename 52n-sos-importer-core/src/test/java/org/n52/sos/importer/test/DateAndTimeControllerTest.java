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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.Column;

/**
 * Class to test the methods of the date and time controller
 * @author e.h.juerrens@52north.org
 *
 */
public class DateAndTimeControllerTest {


	private static final Logger logger = 
			Logger.getLogger(DateAndTimeControllerTest.class);

	public static void main(String[] args) {
		// init logger
		Logger root = Logger.getRootLogger();
		root.setLevel(Level.DEBUG);
		root.addAppender(new ConsoleAppender(new PatternLayout("%-6r %-1p (%c{1}.java:%L) - %m %n")));
		if (logger.isDebugEnabled()) {
			logger.debug("Start Test");
		}
		int firstLineWithData = 1;
		String g = "1";
		//
		// first element
		String p1 = "d.M.yyyy";
		Column c1 = new Column(0, firstLineWithData);
		DateAndTime dt1 = new DateAndTime();
		dt1.setDay(new Day(c1,p1));
		dt1.setMonth(new Month(c1,p1));
		dt1.setYear(new Year(c1,p1));
		dt1.setGroup(g);
		//
		// second element
		String p2 = "HH,00";
		Column c2 = new Column(1, firstLineWithData);
		DateAndTime dt2 = new DateAndTime();
		dt2.setHour(new Hour(c2, p2));
		dt2.setGroup(g);
		//
		// third element
		String p3 = "mm,00";
		Column c3 = new Column(2, firstLineWithData);
		DateAndTime dt3 = new DateAndTime();
		dt3.setHour(new Hour(c3, p3));
		dt3.setGroup(g);
		//
		// add elements to modelstore
		ModelStore ms = ModelStore.getInstance();
		ms.add(dt1);
		ms.add(dt2);
		ms.add(dt3);
		//
		if (logger.isDebugEnabled()) {
			logger.debug("Example data created");
		}
		//
		// DateAndTimeController
		DateAndTimeController dtc = new DateAndTimeController();
		dtc.mergeDateAndTimes();
	}

}
