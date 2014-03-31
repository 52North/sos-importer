/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.feeder.util;

import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import net.opengis.gml.TimePeriodType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.xmlbeans.XmlException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;

public class DescriptionBuilderTest {

	private final String meter = "m";
	private final String degree = "deg";
	private final double altitude = 42.0;
	private final double latitude = 52.0;
	private final double longitude = 7.5;
	private final String offeringUri = "offering-uri";
	private final String offeringName = "offering-name";
	private final Offering off = new Offering(offeringName, offeringUri);
	private final String obsPropUri = "obs-prop-uri";
	private final String obsPropName = "obs-prop-name";
	private final ObservedProperty obsProp = new ObservedProperty(obsPropName, obsPropUri);
	private final String uomUri = "uom-uri";
	private final String uomCode = "uom-code";
	private final UnitOfMeasurement uom = new UnitOfMeasurement(uomCode, uomUri);
	private final Timestamp timeStamp = new Timestamp().set(System.currentTimeMillis()); //"2013-09-25T15:25:33+02:00";
	private final int value = 52;
	private final String featureName = "feature-name";
	private final String featureUri = "feature-uri";
	private final String[] units = {degree,degree,meter};
	private final double[] values = {longitude,latitude,altitude};
	private final int epsgCode = 4979;
	private final Position featurePosition = new Position(values, units, epsgCode);
	private final FeatureOfInterest foi = new FeatureOfInterest(featureName, featureUri, featurePosition);
	private final String mvType = "NUMERIC";
	private final String sensorUri = "sensor-uri";
	private final String sensorName = "sensor-name";
	private final Sensor sensor = new Sensor(sensorName, sensorUri);
	private final Map<ObservedProperty, String> unitOfMeasurements = singletonMap(obsProp, uom.getCode());
	private final Map<ObservedProperty, String> measuredValueTypes = singletonMap(obsProp, mvType);
	private final Collection<ObservedProperty> observedProperties = singletonList(obsProp);
	private final InsertObservation io = new InsertObservation(sensor, foi, value, timeStamp, uom, obsProp, off, mvType);
	private final RegisterSensor rs = new RegisterSensor(io, observedProperties, measuredValueTypes, unitOfMeasurements);
	private SystemType system;

	@Before
	public void createSensorML() throws XmlException, IOException {
		final String createdSensorML = new DescriptionBuilder().createSML(rs);
//		System.out.println(SensorMLDocument.Factory.parse(createdSensorML).xmlText(XmlUtil.PRETTYPRINT));
		system = SystemType.Factory.parse(SensorMLDocument.Factory.parse(createdSensorML).getSensorML().getMemberArray(0).getProcess().newInputStream());
	}

	@Test public void
	shouldSetKeywords()
	{
		assertThat(system.getKeywordsArray().length, is(1));
		final String[] keywordArray = system.getKeywordsArray(0).getKeywordList().getKeywordArray();
		assertThat(keywordArray.length,is(3));
		assertThat(keywordArray,hasItemInArray(featureName));
		assertThat(keywordArray,hasItemInArray(sensorName));
		assertThat(keywordArray,hasItemInArray(obsPropName));
	}

	@Test public void
	shouldSetIdentification()
	{
		assertThat(system.getIdentificationArray().length, is(1));
		final Identifier[] identifierArray = system.getIdentificationArray(0).getIdentifierList().getIdentifierArray();
		assertThat(identifierArray.length,is(3));

		assertThat(identifierArray[0].getName(), is("uniqueID"));
		assertThat(identifierArray[0].getTerm().getDefinition(), is("urn:ogc:def:identifier:OGC:1.0:uniqueID"));
		assertThat(identifierArray[0].getTerm().getValue(),is(sensorUri));

		assertThat(identifierArray[1].getName(), is("longName"));
		assertThat(identifierArray[1].getTerm().getDefinition(), is("urn:ogc:def:identifier:OGC:1.0:longName"));
		assertThat(identifierArray[1].getTerm().getValue(),is(sensorName));

		assertThat(identifierArray[2].getName(), is("shortName"));
		assertThat(identifierArray[2].getTerm().getDefinition(), is("urn:ogc:def:identifier:OGC:1.0:shortName"));
		assertThat(identifierArray[2].getTerm().getValue(),is(sensorName));
	}

	@Test public void
	shouldSetSensorPosition()
	{
		assertThat(system.isSetPosition(),is(true));
		assertThat(system.getPosition().getName(),is("sensorPosition"));
		final VectorType vector = system.getPosition().getPosition().getLocation().getVector();
		assertThat(vector.getId(),is("SYSTEM_LOCATION"));
		assertThat(vector.getCoordinateArray().length,is(3));

		assertThat(vector.getCoordinateArray(0).getName(),is("easting"));
		assertThat(vector.getCoordinateArray(0).getQuantity().getAxisID(),is(equalToIgnoringCase("x")));
		assertThat(vector.getCoordinateArray(0).getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(vector.getCoordinateArray(0).getQuantity().getValue(),is(longitude));

		assertThat(vector.getCoordinateArray(1).getName(),is("northing"));
		assertThat(vector.getCoordinateArray(1).getQuantity().getAxisID(),is(equalToIgnoringCase("y")));
		assertThat(vector.getCoordinateArray(1).getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(vector.getCoordinateArray(1).getQuantity().getValue(),is(latitude));

		assertThat(vector.getCoordinateArray(2).getName(),is("altitude"));
		assertThat(vector.getCoordinateArray(2).getQuantity().getAxisID(),is(equalToIgnoringCase("z")));
		assertThat(vector.getCoordinateArray(2).getQuantity().getUom().getCode(),is(equalToIgnoringCase(meter)));
		assertThat(vector.getCoordinateArray(2).getQuantity().getValue(),is(altitude));
	}

	@Test public void
	shouldSetInputs()
	{
		assertThat(system.isSetInputs(),is(true));
		assertThat(system.getInputs().getInputList().getInputArray().length,is(1));
		assertThat(system.getInputs().getInputList().getInputArray(0).getName(),is(obsPropName));
		assertThat(system.getInputs().getInputList().getInputArray(0).getObservableProperty().getDefinition(),is(obsPropUri));
	}

	@Test public void
	shouldSetOutputs()
	{
		assertThat(system.isSetOutputs(),is(true));
		assertThat(system.getOutputs().getOutputList().getOutputArray().length,is(1));
		assertThat(system.getOutputs().getOutputList().getOutputArray(0).getName(),is(obsPropName));
		assertThat(system.getOutputs().getOutputList().getOutputArray(0).getQuantity().getDefinition(),is(obsPropUri));
		assertThat(system.getOutputs().getOutputList().getOutputArray(0).getQuantity().getUom().getCode(),is(uomCode));
	}

	@Test public void
	shouldSetOfferings()
	{
		final Capabilities offering = getCapabilitiesByName("offerings");
		final AnyScalarPropertyType field = ((SimpleDataRecordType)offering.getAbstractDataRecord()).getFieldArray(0);
		assertThat(field.getName(),is(offeringName));
		assertThat(field.isSetText(),is(true));
		assertThat(field.getText().getDefinition(), is("urn:ogc:def:identifier:OGC:1.0:offeringID"));
		assertThat(field.getText().getValue(), is(offeringUri));
	}

	@Test public void
	shouldSetFeatureOfInterest()
	{
		final Capabilities features = getCapabilitiesByName("featuresOfInterest");
		final DataComponentPropertyType field = ((DataRecordType)features.getAbstractDataRecord()).getFieldArray(0);
		assertThat(field.getName(),is("featureOfInterestID"));
		assertThat(field.isSetText(),is(true));
		assertThat(field.getText().getDefinition(), is("http://www.opengis.net/def/featureOfInterest/identifier"));
		assertThat(field.getText().getValue(),is(featureUri));
	}

	@Test public void
	shouldSetObservedBBOX()
			 throws XmlException, IOException {
		final Capabilities observedBBOX = getCapabilitiesByName("observedBBOX");
		final DataComponentPropertyType field = ((DataRecordType)observedBBOX.getAbstractDataRecord()).getFieldArray(0);
		assertThat(field.getName(),is("observedBBOX"));
		final EnvelopeType envelope = EnvelopeType.Factory.parse(field.getAbstractDataRecord().newInputStream());
		assertThat(envelope.getDefinition(),is("urn:ogc:def:property:OGC:1.0:observedBBOX"));

		assertThat(envelope.isSetReferenceFrame(),is(true));
		assertThat(envelope.getReferenceFrame(),is(SensorDescriptionBuilder.EPSG_CODE_PREFIX+4326));
		final Coordinate[] lcCoords = envelope.getLowerCorner().getVector().getCoordinateArray();

		assertThat(lcCoords.length,is(2));

		assertThat(lcCoords[0].getName(),is("easting"));
		assertThat(lcCoords[0].getQuantity().getAxisID(),is(equalToIgnoringCase("x")));
		assertThat(lcCoords[0].getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(lcCoords[0].getQuantity().getValue(),is(longitude));

		assertThat(lcCoords[1].getName(),is("northing"));
		assertThat(lcCoords[1].getQuantity().getAxisID(),is(equalToIgnoringCase("y")));
		assertThat(lcCoords[1].getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(lcCoords[1].getQuantity().getValue(),is(latitude));

		final Coordinate[] ucCoords = envelope.getUpperCorner().getVector().getCoordinateArray();

		assertThat(ucCoords.length,is(2));

		assertThat(ucCoords[0].getName(),is("easting"));
		assertThat(ucCoords[0].getQuantity().getAxisID(),is(equalToIgnoringCase("x")));
		assertThat(ucCoords[0].getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(ucCoords[0].getQuantity().getValue(),is(longitude));

		assertThat(ucCoords[1].getName(),is("northing"));
		assertThat(ucCoords[1].getQuantity().getAxisID(),is(equalToIgnoringCase("y")));
		assertThat(ucCoords[1].getQuantity().getUom().getCode(),is(equalToIgnoringCase(degree)));
		assertThat(ucCoords[1].getQuantity().getValue(),is(latitude));
	}

	@Test public void
	shouldSetValidTime()
			throws XmlException, IOException {
		final TimePeriodType validTime = system.getValidTime().getTimePeriod();
		assertThat(validTime.getBeginPosition().getObjectValue(),is(notNullValue()));
		final long durationMillis = new Interval(new DateTime(validTime.getBeginPosition().getStringValue()).getMillis(), System.currentTimeMillis()).toDurationMillis();
		assertThat(durationMillis,is(lessThanOrEqualTo(2000l)));
		assertThat(validTime.getEndPosition().isSetIndeterminatePosition(),is(true));
		assertThat(validTime.getEndPosition().getIndeterminatePosition().toString(),is("unknown"));
		// test for valid time -> set by server
	}

	// test for contact -> set by server

	private Capabilities getCapabilitiesByName(final String name) {
		for (final Capabilities capabilities : system.getCapabilitiesArray()) {
			if (capabilities.isSetName() && capabilities.getName().equalsIgnoreCase(name)) {
				return capabilities;
			}
		}
		fail("sml:capabilities element with name '" + name +"' not found!");
		return null;
	}
}
