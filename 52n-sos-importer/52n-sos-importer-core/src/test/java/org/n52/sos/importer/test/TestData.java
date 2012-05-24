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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.table.Column;

/**
 * @author e.h.juerrens@52north.org
 * This class holds shared test data for the other test classes.
 */
public class TestData {
	
	private static final Logger logger = Logger.getLogger(TestData.class);
	
	static {
		Logger root = Logger.getRootLogger();
		root.setLevel(Level.DEBUG);
		root.addAppender(new ConsoleAppender(new PatternLayout("%-6r %-1p (%c{1}.java:%L) - %m %n")));
	}

	/**
	 * Example data set with 8 columns
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
	
	public static final Object[][] EXAMPLE_TABLE_NO_UOM =  {
		//         0				1			2				3			4
		{"Timestamp",			"Sensor",	   "Phenomenon",  "Feature", "Value"},
		{"2012-04-23 11:52:23", "Thermometer", "Temperature", "Münster", "14.5"},
		{"2012-04-23 11:52:33", "Thermometer", "Temperature", "Münster", "15.5"},
		{"2012-04-23 11:52:43", "Thermometer", "Temperature", "Münster", "13.5"},
		{"2012-04-23 11:52:53", "Thermometer", "Temperature", "Münster", "12.5"},
		{"2012-04-23 11:53:03", "Thermometer", "Temperature", "Münster", "12.7"},
		{"2012-04-23 11:53:13", "Thermometer", "Temperature", "Münster", "12.8"},
		{"2012-04-23 11:53:23", "Thermometer", "Temperature", "Münster", "12.1"},
		{"2012-04-23 11:53:33", "Thermometer", "Temperature", "Münster", "12.2"},
		{"2012-04-23 11:53:43", "Thermometer", "Temperature", "Münster", "12.3"},
		{"2012-04-23 11:53:53", "Thermometer", "Temperature", "Münster", "12.4"},
		{"2012-04-23 11:54:13", "Thermometer", "Temperature", "Münster", "12.5"},
		};
	
	/**
	 * Example data set without feature of interest
	 */
	public static final Object[][] EXAMPLE_TABLE_NO_FOI = {
				//         0				1			2				3		4	
				{"Timestamp",			"Sensor",	   "Phenomenon",  "UOM",  "Value"},
				{"2012-04-23 11:52:23", "Thermometer", "Temperature", "degC", "14.5"},
				{"2012-04-23 11:52:33", "Thermometer", "Temperature", "degC", "15.5"},
				{"2012-04-23 11:52:43", "Thermometer", "Temperature", "degC", "13.5"},
				{"2012-04-23 11:52:53", "Thermometer", "Temperature", "degC", "12.5"},
				{"2012-04-23 11:53:03", "Thermometer", "Temperature", "degC", "12.7"},
				{"2012-04-23 11:53:13", "Thermometer", "Temperature", "degC", "12.8"},
				{"2012-04-23 11:53:23", "Thermometer", "Temperature", "degC", "12.1"},
				{"2012-04-23 11:53:33", "Thermometer", "Temperature", "degC", "12.2"},
				{"2012-04-23 11:53:43", "Thermometer", "Temperature", "degC", "12.3"},
				{"2012-04-23 11:53:53", "Thermometer", "Temperature", "degC", "12.4"},
				{"2012-04-23 11:54:13", "Thermometer", "Temperature", "degC", "12.5"},
				};
	/**
	 * Example feature of interest with position: lat: 52, long: 7, alt: 42, 
	 * epsg: 4979, table element: col 3, flwd 1</br>
	 * To change the related column call foi.setTableElement(new Column(newColumnId, firstlinewithData);
	 */
	public static final FeatureOfInterest EXAMPLE_FOI = TestData.exampleFoi();
	
	private static FeatureOfInterest exampleFoi() {
		if (logger.isTraceEnabled()) {
			logger.trace("exampleFoi()");
		}
		FeatureOfInterest foi = new FeatureOfInterest();
		
		foi.setName("FOI_TEST_1");
		foi.setGenerated(false);
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
		foi.setTableElement(new Column(3, 1));
		return foi;
	}
	
	/**
	 * Example observed property created from manual input. Related table element: col 3, flwd 1</br>
	 * To change the related column call <code>obsProp.setTableElement(new Column(newColumnId, firstlinewithData);</code>
	 */
	public static final ObservedProperty EXAMPLE_OBS_PROP = TestData.exampleObsProp();

	private static ObservedProperty exampleObsProp() {
		if (logger.isTraceEnabled()) {
			logger.trace("exampleObsProp()");
		}
		ObservedProperty obsProp = new ObservedProperty();
		obsProp.setName("OBS_PROP_TEST_1");
		obsProp.setGenerated(false);
		try {
			obsProp.setURI(new URI("http://example.com/spatial/observedproperties/test/1"));
		} catch (URISyntaxException e) {
			logger.error("Exception thrown: " + e.getMessage(), e);
		}
		obsProp.setTableElement(new Column(3, 1));
		return obsProp;
	}
	
	/**
	 * Example measure value of type numeric. Related table element is: col 1, flwd 1.<br />
	 * To change the related column call <code>mv.setTableElement(new Column(newColumnId, firstlinewithData);</code>
	 */
	public static final MeasuredValue EXAMPLE_MEASURED_VALUE_NUMERIC = TestData.exampleMVnumeric();

	private static MeasuredValue exampleMVnumeric() {
		if (logger.isTraceEnabled()) {
			logger.trace("exampleMVnumeric()");
		}
		NumericValue nv = new NumericValue();
		nv.setTableElement(new Column(1, 1));
		return nv;
	}
}
