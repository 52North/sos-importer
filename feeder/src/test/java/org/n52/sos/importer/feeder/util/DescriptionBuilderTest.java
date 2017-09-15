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
package org.n52.sos.importer.feeder.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.RegisterSensor;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;

public class DescriptionBuilderTest {

    private final String northing = "northing";
    private final String easting = "easting";
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
    //"2013-09-25T15:25:33+02:00"
    private final Timestamp timeStamp = new Timestamp().ofUnixTimeMillis(System.currentTimeMillis());
    private final int value = 52;
    private final String featureName = "feature-name";
    private final String featureUri = "feature-uri";
    private final String[] units = {degree, degree, meter};
    private final double[] values = {longitude, latitude, altitude};
    private final int epsgCode = 4979;
    private final Position featurePosition = new Position(values, units, epsgCode);
    private final FeatureOfInterest foi = new FeatureOfInterest(featureName, featureUri, featurePosition);
    private final String mvType = "NUMERIC";
    private final String sensorUri = "sensor-uri";
    private final String sensorName = "sensor-name";
    private final Sensor sensor = new Sensor(sensorName, sensorUri);
    private final Map<ObservedProperty, String> unitOfMeasurements =
            java.util.Collections.singletonMap(obsProp, uom.getCode());
    private final Map<ObservedProperty, String> measuredValueTypes =
            java.util.Collections.singletonMap(obsProp, mvType);
    private final Collection<ObservedProperty> observedProperties =
            java.util.Collections.singletonList(obsProp);
    private final InsertObservation io =
            new InsertObservation(sensor,
                foi,
                value,
                timeStamp,
                uom,
                obsProp,
                off,
                Optional.empty(),
                mvType);
    private final RegisterSensor rs =
            new RegisterSensor(io, observedProperties, measuredValueTypes, unitOfMeasurements);
    private SystemType system;

    @Before
    public void createSensorML() throws XmlException, IOException {
        final String createdSensorML = new DescriptionBuilder().createSML(rs);
        system = SystemType.Factory.parse(
                SensorMLDocument.Factory.parse(createdSensorML)
                .getSensorML().getMemberArray(0).getProcess().newInputStream());
    }

    @Test public void
    shouldSetKeywords() {
        Assert.assertThat(system.getKeywordsArray().length, Matchers.is(1));
        final String[] keywordArray = system.getKeywordsArray(0).getKeywordList().getKeywordArray();
        Assert.assertThat(keywordArray.length, Matchers.is(3));
        Assert.assertThat(keywordArray, Matchers.hasItemInArray(featureName));
        Assert.assertThat(keywordArray, Matchers.hasItemInArray(sensorName));
        Assert.assertThat(keywordArray, Matchers.hasItemInArray(obsPropName));
    }

    @Test public void
    shouldSetIdentification() {
        Assert.assertThat(system.getIdentificationArray().length, Matchers.is(1));
        final Identifier[] identifierArray = system.getIdentificationArray(0).getIdentifierList().getIdentifierArray();
        Assert.assertThat(identifierArray.length, Matchers.is(3));

        Assert.assertThat(identifierArray[0].getName(), Matchers.is("uniqueID"));
        Assert.assertThat(identifierArray[0].getTerm().getDefinition(),
                Matchers.is("urn:ogc:def:identifier:OGC:1.0:uniqueID"));
        Assert.assertThat(identifierArray[0].getTerm().getValue(), Matchers.is(sensorUri));

        Assert.assertThat(identifierArray[1].getName(), Matchers.is("longName"));
        Assert.assertThat(identifierArray[1].getTerm().getDefinition(),
                Matchers.is("urn:ogc:def:identifier:OGC:1.0:longName"));
        Assert.assertThat(identifierArray[1].getTerm().getValue(), Matchers.is(sensorName));

        Assert.assertThat(identifierArray[2].getName(), Matchers.is("shortName"));
        Assert.assertThat(identifierArray[2].getTerm().getDefinition(),
                Matchers.is("urn:ogc:def:identifier:OGC:1.0:shortName"));
        Assert.assertThat(identifierArray[2].getTerm().getValue(), Matchers.is(sensorName));
    }

    @Test public void
    shouldSetSensorPosition() {
        Assert.assertThat(system.isSetPosition(), Matchers.is(true));
        Assert.assertThat(system.getPosition().getName(), Matchers.is("sensorPosition"));
        final VectorType vector = system.getPosition().getPosition().getLocation().getVector();
        Assert.assertThat(vector.getId(), Matchers.is("SYSTEM_LOCATION"));
        Assert.assertThat(vector.getCoordinateArray().length, Matchers.is(3));

        Assert.assertThat(vector.getCoordinateArray(0).getName(), Matchers.is(easting));
        Assert.assertThat(vector.getCoordinateArray(0).getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("x")));
        Assert.assertThat(vector.getCoordinateArray(0).getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(vector.getCoordinateArray(0).getQuantity().getValue(),
                Matchers.is(longitude));

        Assert.assertThat(vector.getCoordinateArray(1).getName(), Matchers.is(northing));
        Assert.assertThat(vector.getCoordinateArray(1).getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("y")));
        Assert.assertThat(vector.getCoordinateArray(1).getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(vector.getCoordinateArray(1).getQuantity().getValue(),
                Matchers.is(latitude));

        Assert.assertThat(vector.getCoordinateArray(2).getName(), Matchers.is("altitude"));
        Assert.assertThat(vector.getCoordinateArray(2).getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("z")));
        Assert.assertThat(vector.getCoordinateArray(2).getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(meter)));
        Assert.assertThat(vector.getCoordinateArray(2).getQuantity().getValue(),
                Matchers.is(altitude));
    }

    @Test public void
    shouldSetInputs() {
        Assert.assertThat(system.isSetInputs(), Matchers.is(true));
        Assert.assertThat(
                system.getInputs().getInputList().getInputArray().length, Matchers.is(1));
        Assert.assertThat(system.getInputs().getInputList().getInputArray(0).getName(),
                Matchers.is(obsPropName));
        Assert.assertThat(
                system.getInputs().getInputList().getInputArray(0).getObservableProperty().getDefinition(),
                Matchers.is(obsPropUri));
    }

    @Test public void
    shouldSetOutputs() {
        Assert.assertThat(system.isSetOutputs(), Matchers.is(true));
        Assert.assertThat(
                system.getOutputs().getOutputList().getOutputArray().length,
                Matchers.is(1));
        Assert.assertThat(system.getOutputs().getOutputList().getOutputArray(0).getName(),
                Matchers.is(obsPropName));
        Assert.assertThat(system.getOutputs().getOutputList().getOutputArray(0).getQuantity().getDefinition(),
                Matchers.is(obsPropUri));
        Assert.assertThat(
                system.getOutputs().getOutputList().getOutputArray(0).getQuantity().getUom().getCode(),
                Matchers.is(uomCode));
    }

    @Test public void
    shouldSetOfferings() {
        final Capabilities offering = getCapabilitiesByName("offerings");
        final AnyScalarPropertyType field = ((SimpleDataRecordType) offering.getAbstractDataRecord()).getFieldArray(0);
        Assert.assertThat(field.getName(), Matchers.is(offeringName));
        Assert.assertThat(field.isSetText(), Matchers.is(true));
        Assert.assertThat(field.getText().getDefinition(),
                Matchers.is("urn:ogc:def:identifier:OGC:1.0:offeringID"));
        Assert.assertThat(field.getText().getValue(), Matchers.is(offeringUri));
    }

    @Test public void
    shouldSetFeatureOfInterest() {
        final Capabilities features = getCapabilitiesByName("featuresOfInterest");
        final DataComponentPropertyType field = ((DataRecordType) features.getAbstractDataRecord()).getFieldArray(0);
        Assert.assertThat(field.getName(), Matchers.is("featureOfInterestID"));
        Assert.assertThat(field.isSetText(), Matchers.is(true));
        Assert.assertThat(field.getText().getDefinition(),
                Matchers.is("http://www.opengis.net/def/featureOfInterest/identifier"));
        Assert.assertThat(field.getText().getValue(), Matchers.is(featureUri));
    }

    @Test public void
    shouldSetObservedBBOX()
             throws XmlException, IOException {
        final String observedBBox = "observedBBOX";
        final Capabilities observedBBOX = getCapabilitiesByName(observedBBox);
        final DataComponentPropertyType field =
                ((DataRecordType) observedBBOX.getAbstractDataRecord()).getFieldArray(0);
        Assert.assertThat(field.getName(), Matchers.is(observedBBox));
        final EnvelopeType envelope = EnvelopeType.Factory.parse(field.getAbstractDataRecord().newInputStream());
        Assert.assertThat(envelope.getDefinition(),
                Matchers.is("urn:ogc:def:property:OGC:1.0:observedBBOX"));

        Assert.assertThat(envelope.isSetReferenceFrame(), Matchers.is(true));
        Assert.assertThat(envelope.getReferenceFrame(),
                Matchers.is(SensorDescriptionBuilder.EPSG_CODE_PREFIX + 4326));
        final Coordinate[] lcCoords = envelope.getLowerCorner().getVector().getCoordinateArray();

        Assert.assertThat(lcCoords.length, Matchers.is(2));

        Assert.assertThat(lcCoords[0].getName(), Matchers.is(easting));
        Assert.assertThat(lcCoords[0].getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("x")));
        Assert.assertThat(lcCoords[0].getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(lcCoords[0].getQuantity().getValue(), Matchers.is(longitude));

        Assert.assertThat(lcCoords[1].getName(), Matchers.is(northing));
        Assert.assertThat(lcCoords[1].getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("y")));
        Assert.assertThat(lcCoords[1].getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(lcCoords[1].getQuantity().getValue(), Matchers.is(latitude));

        final Coordinate[] ucCoords = envelope.getUpperCorner().getVector().getCoordinateArray();

        Assert.assertThat(ucCoords.length, Matchers.is(2));

        Assert.assertThat(ucCoords[0].getName(), Matchers.is(easting));
        Assert.assertThat(ucCoords[0].getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("x")));
        Assert.assertThat(ucCoords[0].getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(ucCoords[0].getQuantity().getValue(), Matchers.is(longitude));

        Assert.assertThat(ucCoords[1].getName(), Matchers.is(northing));
        Assert.assertThat(ucCoords[1].getQuantity().getAxisID(),
                Matchers.is(Matchers.equalToIgnoringCase("y")));
        Assert.assertThat(ucCoords[1].getQuantity().getUom().getCode(),
                Matchers.is(Matchers.equalToIgnoringCase(degree)));
        Assert.assertThat(ucCoords[1].getQuantity().getValue(), Matchers.is(latitude));
    }

    @Test public void
    shouldSetValidTime()
            throws XmlException, IOException {
        synchronized (this){
            final TimePeriodType validTime = system.getValidTime().getTimePeriod();
            Assert.assertThat(validTime.getBeginPosition().getObjectValue(),
                    Matchers.is(Matchers.notNullValue()));
            final long durationMillis =
                    new Interval(new DateTime(validTime.getBeginPosition().getStringValue()).getMillis(),
                            System.currentTimeMillis()).toDurationMillis();
            Assert.assertThat(durationMillis,
                    Matchers.is(Matchers.lessThanOrEqualTo(2000L)));
            Assert.assertThat(validTime.getEndPosition().isSetIndeterminatePosition(),
                    Matchers.is(true));
            Assert.assertThat(validTime.getEndPosition().getIndeterminatePosition().toString(),
                    Matchers.is("unknown"));
        }
        // test for valid time -> set by server
    }

    // test for contact -> set by server

    private Capabilities getCapabilitiesByName(final String name) {
        for (final Capabilities capabilities : system.getCapabilitiesArray()) {
            if (capabilities.isSetName() && capabilities.getName().equalsIgnoreCase(name)) {
                return capabilities;
            }
        }
        Assert.fail("sml:capabilities element with name '" + name + "' not found!");
        return null;
    }
}
