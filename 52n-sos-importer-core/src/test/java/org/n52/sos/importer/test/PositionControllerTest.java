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

import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to test the methods of the position controller
 * @author e.h.juerrens@52north.org
 *
 */
public class PositionControllerTest {

	private static final Logger logger = 
			LoggerFactory.getLogger(PositionControllerTest.class);

	public static void main(final String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("Start Test");
		}
		final int firstLineWithData = 1;
		final String g = "1", 
				pa1 = "LAT",
				pa2 = "LON";
		//
		// first element
		final Column c1 = new Column(0, firstLineWithData);
		final Position p1 = new Position();
		p1.setLatitude(new Latitude(c1, pa1));
		p1.setGroup(g);
		//
		// second element
		final Column c2 = new Column(1, firstLineWithData);
		final Position p2 = new Position();
		p2.setLongitude(new Longitude(c2, pa2));
		p2.setGroup(g);
		//
		// add elements to modelstore
		final ModelStore ms = ModelStore.getInstance();
		ms.add(p1);
		ms.add(p2);
		//
		if (logger.isDebugEnabled()) {
			logger.debug("Example data created");
		}
		//
		// Position controller
		final PositionController pc = new PositionController();
		pc.mergePositions();
	}

}
