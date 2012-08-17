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
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Column;

/**
 * Class to test the methods of the position controller
 * @author e.h.juerrens@52north.org
 *
 */
public class PositionControllerTest {

	private static final Logger logger = 
			Logger.getLogger(PositionControllerTest.class);

	public static void main(String[] args) {
		// init logger
		Logger root = Logger.getRootLogger();
		root.setLevel(Level.DEBUG);
		root.addAppender(new ConsoleAppender(new PatternLayout("%-6r %-1p (%c{1}.java:%L) - %m %n")));
		if (logger.isDebugEnabled()) {
			logger.debug("Start Test");
		}
		int firstLineWithData = 1;
		String g = "1", 
				pa1 = "LAT",
				pa2 = "LON";
		//
		// first element
		Column c1 = new Column(0, firstLineWithData);
		Position p1 = new Position();
		p1.setLatitude(new Latitude(c1, pa1));
		p1.setGroup(g);
		//
		// second element
		Column c2 = new Column(1, firstLineWithData);
		Position p2 = new Position();
		p2.setLongitude(new Longitude(c2, pa2));
		p2.setGroup(g);
		//
		// add elements to modelstore
		ModelStore ms = ModelStore.getInstance();
		ms.add(p1);
		ms.add(p2);
		//
		if (logger.isDebugEnabled()) {
			logger.debug("Example data created");
		}
		//
		// Position controller
		PositionController pc = new PositionController();
		pc.mergePositions();
	}

}
