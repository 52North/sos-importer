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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;

/**
 * @author e.h.juerrens@52north.org
 * This class holds shared test data for the other test classes.
 */
public class TestData {
	
	private static final Logger logger = Logger.getLogger(TestData.class);

	/**
	 * Example Dataset with 8 columns
	 */
	public static final Object[][] EXAMPLE_TABLE = {
				//         0				1			2				3		4		5		6			7		8
				{"Timestamp",			"Sensor",	   "Phenomenon",  "Feature", "UOM",  "Value","Feature","UOM",  "Value"},
				{"2012-04-23 11:52:23", "Thermometer", "Temperature", "Münster", "degC", "14.5", "Berlin", "degC", "14.5"},
				{"2012-04-23 11:52:33", "Thermometer", "Temperature", "Münster", "degC", "15.5", "Berlin", "degC", "15.5"},
				{"2012-04-23 11:52:43", "Thermometer", "Temperature", "Münster", "degC", "13.5", "Berlin", "degC", "13.5"},
				{"2012-04-23 11:52:53", "Thermometer", "Temperature", "Münster", "degC", "12.5", "Berlin", "degC", "12.5"},
				{"2012-04-23 11:53:03", "Thermometer", "Temperature", "Münster", "degC", "12.7", "Berlin", "degC", "12.7"},
				{"2012-04-23 11:53:13", "Thermometer", "Temperature", "Münster", "degC", "12.8", "Berlin", "degC", "12.8"},
				{"2012-04-23 11:53:23", "Thermometer", "Temperature", "Münster", "degC", "12.1", "Berlin", "degC", "12.1"},
				{"2012-04-23 11:53:33", "Thermometer", "Temperature", "Münster", "degC", "12.2", "Berlin", "degC", "12.4"},
				{"2012-04-23 11:53:43", "Thermometer", "Temperature", "Münster", "degC", "12.3", "Berlin", "degC", "12.5"},
				{"2012-04-23 11:53:53", "Thermometer", "Temperature", "Münster", "degC", "12.4", "Berlin", "degC", "12.6"},
				{"2012-04-23 11:54:13", "Thermometer", "Temperature", "Münster", "degC", "12.5", "Berlin", "degC", "12.7"},
				};
	/**
	 * Example feature of interest with position: lat: 52, long: 7, alt: 42, 
	 * epsg: 4979, table element: col 3</br>
	 * To change the related column call foi.setTableElement(new Column(newColumnId, firstlinewithData);
	 */
	public static final FeatureOfInterest EXAMPLE_FOI = TestData.exampleFoi();
	
	private static FeatureOfInterest exampleFoi() {
		if (logger.isTraceEnabled()) {
			logger.trace("exampleFoi()");
		}
		FeatureOfInterest foi = new FeatureOfInterest();
		
		foi.setName("FOI_TEST_1");
		foi.setPosition(new Position(
				new Latitude(52.0, "deg"),
				new Longitude(7.0, "deg"),
				new Height(42.0, "m"),
				new EPSGCode(4979)));
		try {
			foi.setURI(new URI("http://example.com/spatial/fois/test/1"));
		} catch (URISyntaxException e) {
			logger.error("Exception thrown: " + e.getMessage(), e);
		}
		foi.setTableElement(new Column(3, 0));
		return foi;
	}
			

}
