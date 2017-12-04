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
package org.n52.sos.importer.feeder.model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlString;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.SweArrayObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.xml.NcNameResolver;
import org.n52.oxf.xml.XMLConstants;
import org.n52.sos.importer.feeder.Configuration;

import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CountType;
import net.opengis.swe.x20.DataArrayDocument;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataArrayType.ElementType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TextType;
import net.opengis.swe.x20.TimeType;

/**
 * Data holding class for all observations of a time series.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class TimeSeries {

    public static final String OBSERVED_PROPERTY_NOT_SET_CONST = "OBSERVED_PROPERTY_NOT_SET";

    /** Constant <code>SENSOR_ID_NOT_SET="SENSOR_ID_NOT_SET"</code> */
    public static final String SENSOR_ID_NOT_SET = "SENSOR_ID_NOT_SET";

    /** Constant <code>OBSERVED_PROPERTY_NOT_SET</code> */
    public static final ObservedProperty OBSERVED_PROPERTY_NOT_SET =
            new ObservedProperty(OBSERVED_PROPERTY_NOT_SET_CONST, OBSERVED_PROPERTY_NOT_SET_CONST);

    /** Constant <code>UOM_CODE_NOT_SET="UOM_CODE_NOT_SET"</code> */
    public static final String UOM_CODE_NOT_SET = "UOM_CODE_NOT_SET";

    /** Constant <code>MV_TYPE_NOT_SET="MV_TYPE_NOT_SET"</code> */
    public static final String MV_TYPE_NOT_SET = "MV_TYPE_NOT_SET";

    /** Constant <code>SENSOR_NAME_NOT_SET="SENSOR_NAME_NOT_SET"</code> */
    public static final String SENSOR_NAME_NOT_SET = "SENSOR_NAME_NOT_SET";

    private static final String TOKEN_SEPARATOR = ";";

    private static final String BLOCK_SEPARATOR = "@";

    private static final String N_M_STRING = "%s %s";

    private final LinkedList<InsertObservation> timeseries = new LinkedList<>();

    /**
     * <p>addObservation.</p>
     *
     * @param insertObservation a {@link org.n52.sos.importer.feeder.model.InsertObservation} object.
     * @return a boolean.
     */
    public boolean addObservation(final InsertObservation insertObservation) {
        return timeseries.add(insertObservation);
    }

    /**
     * <p>getSensorURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorURI() {
        if (timeseries.isEmpty()) {
            return SENSOR_ID_NOT_SET;
        }
        final String sensorURI = timeseries.getFirst().getSensorURI();
        if (sensorURI == null || sensorURI.isEmpty()) {
            return SENSOR_ID_NOT_SET;
        }
        return sensorURI;
    }

    /**
     * <p>getSensorName.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getSensorName() {
        if (timeseries.isEmpty()) {
            return SENSOR_NAME_NOT_SET;
        }
        final String sensorName = timeseries.getFirst().getSensorName();
        if (sensorName == null || sensorName.isEmpty()) {
            return SENSOR_NAME_NOT_SET;
        }
        return sensorName;
    }

    /**
     * <p>getFirst.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.InsertObservation} object.
     */
    public InsertObservation getFirst() {
        if (timeseries.isEmpty()) {
            return null;
        }
        return timeseries.getFirst();
    }

    /**
     * <p>getObservedProperty.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.ObservedProperty} object.
     */
    public ObservedProperty getObservedProperty() {
        if (timeseries.isEmpty()) {
            return OBSERVED_PROPERTY_NOT_SET;
        }
        final ObservedProperty obsProp = timeseries.getFirst().getObservedProperty();
        if (obsProp == null) {
            return OBSERVED_PROPERTY_NOT_SET;
        }
        return obsProp;
    }

    /**
     * <p>getUnitOfMeasurementCode.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUnitOfMeasurementCode() {
        if (timeseries.isEmpty()) {
            return UOM_CODE_NOT_SET;
        }
        final String uomCode = timeseries.getFirst().getUnitOfMeasurementCode();
        if (uomCode == null || uomCode.isEmpty()) {
            return UOM_CODE_NOT_SET;
        }
        return uomCode;
    }

    /**
     * <p>getMeasuredValueType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMeasuredValueType() {
        if (timeseries.isEmpty()) {
            return MV_TYPE_NOT_SET;
        }
        final String mVType = timeseries.getFirst().getMeasuredValueType();
        if (mVType == null || mVType.isEmpty()) {
            return MV_TYPE_NOT_SET;
        }
        return mVType;
    }

    /**
     * <p>getSweArrayObservation.</p>
     *
     * @param sosVersion a {@link java.lang.String} object.
     * @return a {@link org.n52.oxf.sos.request.InsertObservationParameters} object.
     */
    public InsertObservationParameters getSweArrayObservation(final String sosVersion) {
        return getSweArrayObservation(sosVersion, getFirst().getOffering().getUri());
    }

    /**
     * <p>getSweArrayObservation.</p>
     *
     * @param sosVersion a {@link java.lang.String} object.
     * @param offering Optional paramater allowing to override the offering in the timeseries.
     * if {@code null} the setted offering will be used.
     *
     * @return a {@link org.n52.oxf.sos.request.InsertObservationParameters} object.
     */
    public InsertObservationParameters getSweArrayObservation(final String sosVersion, String offering) {
        final SweArrayObservationParameters obsParameter = new SweArrayObservationParameters();
        // add extension
        obsParameter.addExtension(
                "<swe:Boolean xmlns:swe=\"http://www.opengis.net/swe/2.0\" "
                + "definition=\"SplitDataArrayIntoObservations\"><swe:value>true</swe:value></swe:Boolean>");

        // OM_Observation
        // procedure
        obsParameter.addProcedure(getSensorURI());
        // obsProp
        obsParameter.addObservedProperty(getObservedProperty().getUri());
        // feature
        addFeature(obsParameter);
        // result
        addResult(obsParameter);
        if (sosVersion.equalsIgnoreCase("2.0.0")) {
            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + getFirst().getEpsgCode());
            // phentime
            obsParameter.addPhenomenonTime(getPhenomenonTime());
            // temporal bbox for result time
            obsParameter.addResultTime(getResultTime());
            // offering
            if (offering == null) {
                offering = getFirst().getOffering().getUri();
            }
            return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                    obsParameter,
                    Collections.singletonList(offering));
        }

        obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + getFirst().getEpsgCode());
        obsParameter.addSamplingTime(getPhenomenonTime());
        return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
    }

    private void addResult(final SweArrayObservationParameters obsParameter) {
        final DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance();
        final DataArrayType xbDataArray = xbDataArrayDoc.addNewDataArray1();
        // count
        xbDataArray.addNewElementCount().addNewCount().setValue(BigInteger.valueOf(timeseries.size()));
        final DataRecordType xbDataRecord = DataRecordType.Factory.newInstance();
        // phentime
        final Field xbPhenTime = xbDataRecord.addNewField();
        xbPhenTime.setName("phenomenonTime");
        final TimeType xbTimeWithUom = TimeType.Factory.newInstance();
        xbTimeWithUom.setDefinition("http://www.opengis.net/def/property/OGC/0/PhenomenonTime");
        xbTimeWithUom.addNewUom().setHref("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
        xbPhenTime.addNewAbstractDataComponent().set(xbTimeWithUom);
        xbPhenTime
            .getAbstractDataComponent()
            .substitute(XMLConstants.QN_SWE_2_0_TIME, TimeType.type);
        // obsProp
        final Field xbObsProperty = xbDataRecord.addNewField();
        xbObsProperty.setName(NcNameResolver.fixNcName(getObservedProperty().getName()));
        if (getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
            final TextType xbTextType = TextType.Factory.newInstance();
            xbTextType.setDefinition(getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbTextType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_TEXT, TextType.type);
        } else if (getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
            final CountType xbCountType = CountType.Factory.newInstance();
            xbCountType.setDefinition(getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbCountType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_COUNT, CountType.type);
        } else if (getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
            final BooleanType xbBooleanType = BooleanType.Factory.newInstance();
            xbBooleanType.setDefinition(getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbBooleanType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_BOOLEAN, BooleanType.type);
            throw new RuntimeException("NO YET IMPLEMENTED");
        } else {
            final QuantityType xbQuantityWithUom = QuantityType.Factory.newInstance();
            xbQuantityWithUom.setDefinition(getObservedProperty().getUri());
            xbQuantityWithUom.addNewUom().setCode(getUnitOfMeasurementCode());
            xbObsProperty.addNewAbstractDataComponent().set(xbQuantityWithUom);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_QUANTITY, QuantityType.type);
        }
        // element type
        final ElementType xbElementType = xbDataArray.addNewElementType();
        xbElementType.setName("definition");
        xbElementType.addNewAbstractDataComponent().set(xbDataRecord);
        xbElementType
            .getAbstractDataComponent()
            .substitute(XMLConstants.QN_SWE_2_0_DATA_RECORD, DataRecordType.type);

        // encoding
        final TextEncodingType textEncoding = TextEncodingType.Factory.newInstance();
        // token
        textEncoding.setTokenSeparator(TOKEN_SEPARATOR);
        // block seperator
        textEncoding.setBlockSeparator(BLOCK_SEPARATOR);
        xbDataArray.addNewEncoding().addNewAbstractEncoding().set(textEncoding);
        xbDataArray
            .getEncoding().getAbstractEncoding()
            .substitute(XMLConstants.QN_SWE_2_0_TEXT_ENCODING, TextEncodingType.type);

        // values
        xbDataArray.addNewValues().set(createValuesString());
        obsParameter.addObservationValue(xbDataArrayDoc.xmlText());
    }

    private XmlString createValuesString() {
        // values <-- linebreak every 100 lines?
        final StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (final InsertObservation io : timeseries) {
            sb.append(io.getTimeStamp().toString());
            sb.append(TOKEN_SEPARATOR);
            sb.append(io.getResultValue());
            sb.append(BLOCK_SEPARATOR);
            if (counter > 0 && counter++ % 100 == 0) {
                sb.append("\n");
            }
        }
        sb.trimToSize();
        String valueString = sb.toString();
        valueString = valueString.substring(0, valueString.lastIndexOf(BLOCK_SEPARATOR));
        final XmlString xbValueString = XmlString.Factory.newInstance();
        xbValueString.setStringValue(valueString);
        return xbValueString;
    }

    private void addFeature(final ObservationParameters obsParameter) {
        final InsertObservation io = getFirst();
        obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
        obsParameter.addNewFoiName(io.getFeatureOfInterestName());
        obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
        if (io.hasFeatureParentFeature()) {
            obsParameter.addFoiSampleFeature(io.getParentFeatureIdentifier());
        }
        // position
        boolean eastingFirst = false;
        if (Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode()) == null) {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get("default");
        } else {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode());
        }
        String pos = eastingFirst ?
                String.format(N_M_STRING,
                io.getLongitudeValue(),
                io.getLatitudeValue()) :
                    String.format(N_M_STRING,
                            io.getLatitudeValue(),
                            io.getLongitudeValue());
        if (io.isSetAltitudeValue()) {
            pos = String.format(N_M_STRING, pos, io.getAltitudeValue());
        }
        obsParameter.addFoiPosition(pos);
    }

    private String getResultTime() {
        Timestamp resultTime = null;
        for (final InsertObservation io : timeseries) {
            if (resultTime == null || resultTime.isBefore(io.getTimeStamp())) {
                resultTime = io.getTimeStamp();
            }
        }
        if (resultTime == null || resultTime.toString().isEmpty()) {
            return "Could not get result time date of timeseries";
        }
        return resultTime.toString();
    }

    private String getPhenomenonTime() {
        Timestamp start = null;
        Timestamp end = null;
        for (final InsertObservation io : timeseries) {
            if (start == null || start.isAfter(io.getTimeStamp())) {
                start = io.getTimeStamp();
            }
            if (end == null || end.isBefore(io.getTimeStamp())) {
                end = io.getTimeStamp();
            }
        }
        if (start == null || start.toString().isEmpty() || end == null || end.toString().isEmpty()) {
            return "Could not get start and/or end date of timeseries";
        }
        return new StringBuffer(start.toString()).append("/").append(end.toString()).toString();
    }

    @Override
    public String toString() {
        return String.format("TimeSeries [sensor=%s, observedProperty=%s, feature=%s]",
                getSensorURI(),
                getObservedProperty(),
                timeseries.getFirst().getFeatureOfInterestURI());
    }

    /**
     * <p>isEmpty.</p>
     *
     * @return <code>true</code>, if this time series contains no {@link InsertObservation} objects.
     */
    public boolean isEmpty() {
        return timeseries.isEmpty();
    }

    /**
     * <p>getInsertObservations.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends InsertObservation> getInsertObservations() {
        return Collections.unmodifiableList(timeseries);
    }

}
