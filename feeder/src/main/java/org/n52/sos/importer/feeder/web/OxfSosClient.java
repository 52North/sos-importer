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
package org.n52.sos.importer.feeder.web;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlString;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.request.MimetypeAwareRequestParameters;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.sos.observation.BooleanObservationParameters;
import org.n52.oxf.sos.observation.CountObservationParameters;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.SweArrayObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.n52.oxf.xml.NcNameResolver;
import org.n52.oxf.xml.XMLConstants;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.SosClient;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.RegisterSensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.util.DescriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.RegisterSensorResponseDocument;
import net.opengis.sos.x10.InsertObservationResponseDocument.InsertObservationResponse;
import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CountType;
import net.opengis.swe.x20.DataArrayDocument;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TextType;
import net.opengis.swe.x20.TimeType;
import net.opengis.swe.x20.DataArrayType.ElementType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swes.x20.InsertSensorResponseDocument;

public class OxfSosClient implements SosClient {

    private static final Logger LOG = LoggerFactory.getLogger(OxfSosClient.class);

    private static final String SOS_VERSION_100 = "1.0.0";

    private static final String SOS_VERSION_200 = "2.0.0";

    private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";

    private static final String OM_200_SAMPLING_FEATURE =
            "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";

    private static final String OBSERVATION_INSERTED_SUCCESSFULLY =
            "Observation inserted successfully.";

    private static final String OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID =
            OBSERVATION_INSERTED_SUCCESSFULLY + " Returned id: %s";

    private static final String SOS_2_0_INSERT_OBSERVATION_RESPONSE =
            "SOS 2.0 InsertObservation doesn't return the assigned id";

    private static final String EXCEPTION_CODE_STRING = "ExceptionCode: '%s' because of '%s'%n";

    private static final String EXCEPTION_THROWN = "Exception thrown: %s%n%s";

    private static final String PROBLEM_WITH_OXF_EXCEPTION_THROWN = "Problem with OXF. Exception thrown: %s";

    private static final String N_M_FORMAT = "%s %s";

    private static final String DEFAULT = "default";

    private static final String TOKEN_SEPARATOR = ";";

    private static final String BLOCK_SEPARATOR = "@";

    private SOSWrapper sosWrapper;

    private DescriptionBuilder sensorDescBuilder;

    private List<String> registeredSensors;

    // FIXME is k,v correct here?
    private Map<String, String> offerings;

    private URL sosUrl;

    private String sosVersion;

    private Binding sosBinding;

    private int sweArrayObservationTimeOutBuffer;

    public OxfSosClient(Configuration configuration) throws OXFException, MalformedURLException, ExceptionReport {
        sosBinding = getBinding(configuration.getSosBinding());
        sosVersion = configuration.getSosVersion();
        sosUrl = configuration.getSosUrl();
        if (sosBinding == null) {
            sosWrapper = SosWrapperFactory.newInstance(sosUrl.toString(), sosVersion);
        } else {
            sosWrapper = SosWrapperFactory.newInstance(sosUrl.toString(), sosVersion, sosBinding);
        }
        if (sosVersion.equals(SOS_VERSION_200)) {
            sensorDescBuilder = new DescriptionBuilder(false);
        } else {
            sensorDescBuilder = new DescriptionBuilder();
        }
        registeredSensors = new LinkedList<>();
        if (sosVersion.equals(SOS_VERSION_200)) {
            offerings = new HashMap<>();
        }
        if (configuration.isInsertSweArrayObservationTimeoutBufferSet()) {
            sweArrayObservationTimeOutBuffer = configuration.getInsertSweArrayObservationTimeoutBuffer();
        }
    }

    @Override
    public boolean isInstanceAvailable() {
        return sosWrapper.getServiceDescriptor() != null;
    }

    @Override
    public boolean isInstanceTransactional() {
        if (!isServiceDescriptorAvailable()) {
            return false;
        }
        final OperationsMetadata opMeta = sosWrapper.getServiceDescriptor().getOperationsMetadata();
        LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
        // check for (Insert|Register)Sensor and InsertObservationOperation
        if ((opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null ||
                opMeta.getOperationByName(SOSAdapter.INSERT_SENSOR) != null)
                &&
                opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
            LOG.debug(String.format("Found all required operations: (%s|%s), %s",
                    SOSAdapter.REGISTER_SENSOR,
                    SOSAdapter.INSERT_SENSOR,
                    SOSAdapter.INSERT_OBSERVATION));
            return true;
        }
        return false;
    }

    private boolean isServiceDescriptorAvailable() {
        if (sosWrapper.getServiceDescriptor() == null) {
            LOG.error(String.format("Service descriptor not available for SOS '%s'", sosUrl));
            return false;
        }
        return true;
    }

    private Binding getBinding(final String binding) throws OXFException {
        if (binding == null  || binding.isEmpty()) {
            return null;
        }
        if (binding.equals(Binding.POX.name())) {
            return Binding.POX;
        }
        if (binding.equals(Binding.SOAP.name())) {
            return Binding.SOAP;
        }
        throw new OXFException(String.format("Binding not supported by this implementation: %s. Use '%s' or '%s'.",
                binding,
                Binding.POX.name(),
                Binding.SOAP.name()));
    }

    @Override
    public boolean isSensorRegistered(String sensorURI) {
        if (!isServiceDescriptorAvailable()) {
            return false;
        }

        // 1 check if offering is available
        final SOSContents sosContent = (SOSContents) sosWrapper.getServiceDescriptor().getContents();
        final String[] offeringIds = sosContent.getDataIdentificationIDArray();
        if (offeringIds != null) {
            for (final String offeringId : offeringIds) {
                final ObservationOffering offering = sosContent.getDataIdentification(offeringId);
                final String[] sensorIds = offering.getProcedures();
                for (final String sensorId : sensorIds) {
                    if (sensorId.equals(sensorURI)) {
                        return true;
                    }
                }
            }
        }
        // 2 check the list of newly registered sensors because the capabilities update might take to long to wait for
        if (registeredSensors != null && registeredSensors.size() > 0) {
            for (final String sensorId : registeredSensors) {
                if (sensorId.equals(sensorURI)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String registerSensor(RegisterSensor rs) throws OXFException, XmlException, IOException {
        String assignedSensorId = null;
        try {
            if (sosVersion.equals(SOS_VERSION_100)) {
                final RegisterSensorParameters regSensorParameter = createRegisterSensorParametersFromRS(rs);
                setMimetype(regSensorParameter);
                final OperationResult opResult = sosWrapper.doRegisterSensor(regSensorParameter);
                final RegisterSensorResponseDocument response =
                        RegisterSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsAutoCloseStream());
                LOG.debug("RegisterSensorResponse parsed");
                assignedSensorId = response.getRegisterSensorResponse().getAssignedSensorId();
            } else if (sosVersion.equals(SOS_VERSION_200)) {
                final InsertSensorParameters insSensorParams = createInsertSensorParametersFromRS(rs);
                if (sosBinding != null) {
                    insSensorParams.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
                }
                setMimetype(insSensorParams);
                final OperationResult opResult = sosWrapper.doInsertSensor(insSensorParams);
                final InsertSensorResponseDocument response =
                        InsertSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsAutoCloseStream());
                LOG.debug("InsertSensorResponse parsed");
                offerings.put(
                        response.getInsertSensorResponse().getAssignedProcedure(),
                        response.getInsertSensorResponse().getAssignedOffering());
                assignedSensorId = response.getInsertSensorResponse().getAssignedProcedure();
            }
        } catch (final ExceptionReport e) {
            /*
             *  Handle already registered sensor case here (happens when the
             *  sensor is registered but not listed in the capabilities):
             */
            final Iterator<OWSException> iter = e.getExceptionsIterator();
            findSensor:
                while (iter.hasNext()) {
                    final OWSException owsEx = iter.next();
                    if (owsEx.getExceptionCode().equals(OwsExceptionCode.NoApplicableCode.name()) &&
                            owsEx.getExceptionTexts() != null &&
                            owsEx.getExceptionTexts().length > 0) {
                        for (final String string : owsEx.getExceptionTexts()) {
                            if (string.contains(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) &&
                                    string.contains(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END)) {
                                assignedSensorId = rs.getSensorURI();
                                break findSensor;
                            }
                        }
                        // handle offering already contained case here
                    } else if (owsEx.getExceptionCode().equals(OwsExceptionCode.InvalidParameterValue.name()) &&
                            owsEx.getLocator().equals("offeringIdentifier") &&
                            owsEx.getExceptionTexts() != null &&
                            owsEx.getExceptionTexts().length > 0) {
                        for (final String string : owsEx.getExceptionTexts()) {
                            if (string.contains(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) &&
                                    string.contains(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END)) {
                                offerings.put(rs.getSensorURI(), rs.getOfferingUri());
                                assignedSensorId = rs.getSensorURI();
                                break findSensor;
                            }
                        }
                    }

                }
            log(e);
        } catch (final OXFException | XmlException | IOException e) {
            log(e);
        }
        if (assignedSensorId != null) {
            LOG.debug(String.format("Sensor registered at SOS  '%s' with assigned id '%s'.",
                    sosUrl.toExternalForm(),
                    assignedSensorId));
            registeredSensors.add(assignedSensorId);
        }
        return assignedSensorId;
    }

    private RegisterSensorParameters createRegisterSensorParametersFromRS(RegisterSensor registerSensor)
            throws OXFException, XmlException, IOException {
        /*
         * create SensorML
         *
         * create template --> within the 52N 1.0.0 SOS implementation this
         * template is somehow ignored
         * --> take first observed property to get values for template
         */
        ObservationTemplateBuilder observationTemplate;
        final ObservedProperty firstObservedProperty = registerSensor.getObservedProperties().iterator().next();
        if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeText();
        } else if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCount();
        } else if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeTruth();
        } else {
            observationTemplate =
                    ObservationTemplateBuilder.createObservationTemplateBuilderForTypeMeasurement(
                            registerSensor.getUnitOfMeasurementCode(firstObservedProperty));
        }
        observationTemplate.setDefaultValue(registerSensor.getDefaultValue());

        return new RegisterSensorParameters(
                sensorDescBuilder.createSML(registerSensor),
                observationTemplate.generateObservationTemplate());
    }

    private boolean isObservationTypeMatching(RegisterSensor registerSensor,
            final ObservedProperty firstObservedProperty,
            final String observationType) {
        return registerSensor.getMeasuredValueType(firstObservedProperty).equals(observationType);
    }

    private void setMimetype(MimetypeAwareRequestParameters parameters) {
        String mimeType = "text/xml";
        if (sosBinding != null) {
            parameters.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
            if (sosBinding.equals(Binding.SOAP)) {
                mimeType = "application/soap+xml";
            }
        }

        parameters.setCharset(Charset.forName("UTF-8"));
        parameters.setType(mimeType);
    }

    private InsertSensorParameters createInsertSensorParametersFromRS(RegisterSensor rs)
            throws XmlException, IOException {
        return new InsertSensorParameters(sensorDescBuilder.createSML(rs),
                SML_101_FORMAT_URI,
                getObservedPropertyURIs(rs.getObservedProperties()),
                Collections.singleton(OM_200_SAMPLING_FEATURE),
                getObservationTypeURIs(rs));
    }


    private void log(Exception exception) {
        if (exception.getClass().isAssignableFrom(ExceptionReport.class) &&
                ((ExceptionReport) exception).getExceptionsIterator().hasNext()) {
            OWSException owsException = ((ExceptionReport) exception).getExceptionsIterator().next();
            LOG.error("Exception thrown: '{}': '{}': '{}'",
                    owsException.getMessage(),
                    owsException.getExceptionCode(),
                    Arrays.toString(owsException.getExceptionTexts()));

        } else {
            LOG.error("Exception thrown: {}", exception.getMessage());
            LOG.debug("Stacktrace:", exception);
        }
    }

    private Collection<String> getObservedPropertyURIs(Collection<ObservedProperty> observedProperties) {
        if (observedProperties == null || observedProperties.isEmpty()) {
            return Collections.emptyList();
        }
        final Collection<String> result = Lists.newArrayListWithCapacity(observedProperties.size());
        for (final ObservedProperty observedProperty : observedProperties) {
            result.add(observedProperty.getUri());
        }
        return result;
    }

    private Collection<String> getObservationTypeURIs(RegisterSensor rs) {
        if (rs == null || rs.getObservedProperties() == null || rs.getObservedProperties().size() < 1) {
            return Collections.emptyList();
        }
        Set<String> tmp = Sets.newHashSetWithExpectedSize(rs.getObservedProperties().size());
        for (ObservedProperty obsProp : rs.getObservedProperties()) {
            String measuredValueType = rs.getMeasuredValueType(obsProp);
            if (measuredValueType != null) {
                tmp.add(getURIForObservationType(measuredValueType));
            }
        }
        return tmp;
    }

    private String getURIForObservationType(String measuredValueType) {
        if (measuredValueType.equals("NUMERIC")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
        }
        if (measuredValueType.equals("COUNT")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
        }
        if (measuredValueType.equals("BOOLEAN")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
        }
        if (measuredValueType.equals("TEXT")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
        }
        final String errorMsg = String.format("Observation type '%s' not supported!", measuredValueType);
        LOG.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

    @Override
    public String insertObservation(InsertObservation io) throws IOException {
        try {
            org.n52.oxf.sos.request.InsertObservationParameters parameters = createParameterAssemblyFromIO(io);
            setMimetype(parameters);
            try {
                OperationResult opResult = sosWrapper.doInsertObservation(parameters);
                if (sosVersion.equals(SOS_VERSION_100)) {
                    try {
                        InsertObservationResponse response =
                                InsertObservationResponseDocument.Factory.parse(
                                        opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(String.format(OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID,
                                response.getAssignedObservationId()));
                        return response.getAssignedObservationId();
                    } catch (XmlException | IOException e) {
                        log(e);
                    }
                } else if (sosVersion.equals(SOS_VERSION_200)) {
                    try {
                        net.opengis.sos.x20.InsertObservationResponseDocument.Factory.parse(
                                opResult.getIncomingResultAsAutoCloseStream())
                                    .getInsertObservationResponse();
                        LOG.debug(OBSERVATION_INSERTED_SUCCESSFULLY);
                        return SOS_2_0_INSERT_OBSERVATION_RESPONSE;
                    } catch (XmlException e) {
                        log(e);
                    }
                }
            } catch (ExceptionReport e) {
                Iterator<OWSException> iter = e.getExceptionsIterator();
                StringBuffer buf = new StringBuffer();
                while (iter.hasNext()) {
                    OWSException owsEx = iter.next();
                    // check for observation already contained exception
                    // TODO update to latest 52nSOS 4.0x message
                    if (isObservationAlreadyContained(owsEx)) {
                        return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
                    }
                    buf = buf.append(String.format(EXCEPTION_CODE_STRING,
                            owsEx.getExceptionCode(),
                            Arrays.toString(owsEx.getExceptionTexts())));
                }
                // TODO improve logging here:
                // add logOwsEceptionReport static util method to OxF or
                // some OER report logger which has unit tests
                LOG.error(String.format(EXCEPTION_THROWN, e.getMessage(), buf.toString()));
                LOG.debug(e.getMessage(), e);
            }

        } catch (OXFException e) {
            LOG.error(String.format(PROBLEM_WITH_OXF_EXCEPTION_THROWN, e.getMessage()), e);
        }
        return null;
    }

    private org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
            InsertObservation io) throws OXFException {
        ObservationParameters obsParameter;

        switch (io.getMeasuredValueType()) {
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                // set text
                obsParameter = new TextObservationParameters();
                ((TextObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
                break;
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                // set count
                obsParameter = new CountObservationParameters();
                ((CountObservationParameters) obsParameter).addObservationValue((Integer) io.getResultValue());
                break;
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                // set boolean
                obsParameter = new BooleanObservationParameters();
                ((BooleanObservationParameters) obsParameter).addObservationValue((Boolean) io.getResultValue());
                break;
            default:
                // set default value type
                obsParameter = new MeasurementObservationParameters();
                ((MeasurementObservationParameters) obsParameter).addUom(io.getUnitOfMeasurementCode());
                ((MeasurementObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
                break;
        }
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
        obsParameter.addNewFoiName(io.getFeatureOfInterestName());
        obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
        if (io.hasFeatureParentFeature()) {
            obsParameter.addFoiSampleFeature(io.getParentFeatureIdentifier());
        }
        // position
        boolean eastingFirst;
        if (Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode()) == null) {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(DEFAULT);
        } else {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode());
        }
        String pos = eastingFirst ?
                String.format(N_M_FORMAT,
                io.getLongitudeValue(),
                io.getLatitudeValue()) :
                    String.format(N_M_FORMAT,
                            io.getLatitudeValue(),
                            io.getLongitudeValue());
        if (io.isSetAltitudeValue()) {
            pos = String.format(N_M_FORMAT, pos, io.getAltitudeValue());
        }
        obsParameter.addFoiPosition(pos);
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addProcedure(io.getSensorURI());

        if (sosVersion.equalsIgnoreCase(SOS_VERSION_200)) {
            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + io.getEpsgCode());
            obsParameter.addPhenomenonTime(io.getTimeStamp().toString());
            obsParameter.addResultTime(io.getTimeStamp().toString());
            if (io.isSetOmParameters()) {
                return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                        obsParameter,
                        Collections.singletonList(io.getOffering().getUri()),
                        io.getOmParameters());
            } else {
                return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                    obsParameter,
                    Collections.singletonList(io.getOffering().getUri()));
            }
        } else {
            obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + io.getEpsgCode());
            obsParameter.addSamplingTime(io.getTimeStamp().toString());
            return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
        }
    }

    private boolean isObservationAlreadyContained(OWSException owsEx) {
        return owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
                owsEx.getExceptionTexts().length > 0 &&
                (owsEx.getExceptionTexts()[0].contains(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_UNIQUE_CONSTRAINT_VIOLATION));
    }

    private boolean isExceptionTextContained(OWSException owsEx, String exceptionText) {
        return owsEx.getExceptionTexts()[0].contains(exceptionText);
    }

    @Override
    public String insertSweArrayObservation(TimeSeries timeSeries) throws IOException {
        if (timeSeries == null || timeSeries.isEmpty()) {
            return null;
        } else {
            return insertSweArrayObservation(getSweArrayObservation(timeSeries));
        }
    }

    private String insertSweArrayObservation(
            final org.n52.oxf.sos.request.InsertObservationParameters sweArrayObservation) {
        try {
            try {
                final int connectionTimeout = sosWrapper.getConnectionTimeout();
                final int readTimeout = sosWrapper.getReadTimeout();
                sosWrapper.setConnectionTimeOut(connectionTimeout + sweArrayObservationTimeOutBuffer);
                sosWrapper.setReadTimeout(readTimeout + sweArrayObservationTimeOutBuffer);
                setMimetype(sweArrayObservation);
                OperationResult opResult = sosWrapper.doInsertObservation(sweArrayObservation);
                sosWrapper.setConnectionTimeOut(connectionTimeout);
                sosWrapper.setReadTimeout(readTimeout);
                if (sosVersion.equals(SOS_VERSION_100)) {
                    try {
                        final InsertObservationResponse response =
                                InsertObservationResponseDocument.Factory.parse(
                                        opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(String.format(OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID,
                                response.getAssignedObservationId()));
                        return response.getAssignedObservationId();
                    } catch (final XmlException | IOException e) {
                        log(e);
                    }
                } else if (sosVersion.equals(SOS_VERSION_200)) {
                    try {
                        net.opengis.sos.x20.InsertObservationResponseDocument.Factory.parse(
                                opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(OBSERVATION_INSERTED_SUCCESSFULLY);
                        return SOS_2_0_INSERT_OBSERVATION_RESPONSE;
                    } catch (final XmlException | IOException e) {
                        log(e);
                    }
                }
            } catch (final ExceptionReport e) {
                final Iterator<OWSException> iter = e.getExceptionsIterator();
                StringBuffer buf = new StringBuffer();
                while (iter.hasNext()) {
                    final OWSException owsEx = iter.next();
                    // check for observation already contained exception
                    // TODO update to latest 52nSOS 4.0x message
                    if (isObservationAlreadyContained(owsEx)) {
                        return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
                    }
                    buf = buf.append(String.format(EXCEPTION_CODE_STRING,
                            owsEx.getExceptionCode(),
                            Arrays.toString(owsEx.getExceptionTexts())));
                }
                // TODO improve logging here:
                // add logOwsEceptionReport static util method to OxF or
                // some OER report logger which has unit tests
                LOG.error(String.format(EXCEPTION_THROWN, e.getMessage(), buf.toString()));
                LOG.debug(e.getMessage(), e);
            }

        } catch (final OXFException e) {
            LOG.error(String.format(PROBLEM_WITH_OXF_EXCEPTION_THROWN, e.getMessage()), e);
        }
        return null;
    }

    private InsertObservationParameters getSweArrayObservation(TimeSeries timeSeries) {
        final SweArrayObservationParameters obsParameter = new SweArrayObservationParameters();
        // add extension
        obsParameter.addExtension(
                "<swe:Boolean xmlns:swe=\"http://www.opengis.net/swe/2.0\" "
                + "definition=\"SplitDataArrayIntoObservations\"><swe:value>true</swe:value></swe:Boolean>");

        // OM_Observation
        // procedure
        obsParameter.addProcedure(timeSeries.getSensorURI());
        // obsProp
        obsParameter.addObservedProperty(timeSeries.getObservedProperty().getUri());
        // feature
        addFeature(obsParameter, timeSeries);
        // result
        addResult(obsParameter, timeSeries);
        if (sosVersion.equalsIgnoreCase(SOS_VERSION_200)) {
            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + timeSeries.getFirst().getEpsgCode());
            // phentime
            obsParameter.addPhenomenonTime(timeSeries.getPhenomenonTime());
            // temporal bbox for result time
            obsParameter.addResultTime(timeSeries.getResultTime());
            return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                    obsParameter,
                    Collections.singletonList(timeSeries.getFirst().getOffering().getUri()));
        }

        obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + timeSeries.getFirst().getEpsgCode());
        obsParameter.addSamplingTime(timeSeries.getPhenomenonTime());
        return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
    }

    private void addFeature(ObservationParameters obsParameter, TimeSeries timeSeries) {
        InsertObservation io = timeSeries.getFirst();
        obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
        obsParameter.addNewFoiName(io.getFeatureOfInterestName());
        obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
        if (io.hasFeatureParentFeature()) {
            obsParameter.addFoiSampleFeature(io.getParentFeatureIdentifier());
        }
        // position
        boolean eastingFirst = false;
        if (Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode()) == null) {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(DEFAULT);
        } else {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode());
        }
        String pos = eastingFirst ?
                String.format(N_M_FORMAT,
                io.getLongitudeValue(),
                io.getLatitudeValue()) :
                    String.format(N_M_FORMAT,
                            io.getLatitudeValue(),
                            io.getLongitudeValue());
        if (io.isSetAltitudeValue()) {
            pos = String.format(N_M_FORMAT, pos, io.getAltitudeValue());
        }
        obsParameter.addFoiPosition(pos);
    }

    private void addResult(SweArrayObservationParameters obsParameter, TimeSeries timeSeries) {
        DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance();
        DataArrayType xbDataArray = xbDataArrayDoc.addNewDataArray1();
        // count
        xbDataArray.addNewElementCount().addNewCount().setValue(BigInteger.valueOf(timeSeries.size()));
        DataRecordType xbDataRecord = DataRecordType.Factory.newInstance();
        // phentime
        Field xbPhenTime = xbDataRecord.addNewField();
        xbPhenTime.setName("phenomenonTime");
        TimeType xbTimeWithUom = TimeType.Factory.newInstance();
        xbTimeWithUom.setDefinition("http://www.opengis.net/def/property/OGC/0/PhenomenonTime");
        xbTimeWithUom.addNewUom().setHref("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
        xbPhenTime.addNewAbstractDataComponent().set(xbTimeWithUom);
        xbPhenTime
            .getAbstractDataComponent()
            .substitute(XMLConstants.QN_SWE_2_0_TIME, TimeType.type);
        // obsProp
        Field xbObsProperty = xbDataRecord.addNewField();
        xbObsProperty.setName(NcNameResolver.fixNcName(timeSeries.getObservedProperty().getName()));
        if (timeSeries.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
            TextType xbTextType = TextType.Factory.newInstance();
            xbTextType.setDefinition(timeSeries.getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbTextType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_TEXT, TextType.type);
        } else if (timeSeries.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
            CountType xbCountType = CountType.Factory.newInstance();
            xbCountType.setDefinition(timeSeries.getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbCountType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_COUNT, CountType.type);
        } else if (timeSeries.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
            BooleanType xbBooleanType = BooleanType.Factory.newInstance();
            xbBooleanType.setDefinition(timeSeries.getObservedProperty().getUri());
            xbObsProperty.addNewAbstractDataComponent().set(xbBooleanType);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_BOOLEAN, BooleanType.type);
            throw new RuntimeException("NO YET IMPLEMENTED");
        } else {
            QuantityType xbQuantityWithUom = QuantityType.Factory.newInstance();
            xbQuantityWithUom.setDefinition(timeSeries.getObservedProperty().getUri());
            xbQuantityWithUom.addNewUom().setCode(timeSeries.getUnitOfMeasurementCode());
            xbObsProperty.addNewAbstractDataComponent().set(xbQuantityWithUom);
            xbObsProperty.getAbstractDataComponent().substitute(XMLConstants.QN_SWE_2_0_QUANTITY, QuantityType.type);
        }
        // element type
        ElementType xbElementType = xbDataArray.addNewElementType();
        xbElementType.setName("definition");
        xbElementType.addNewAbstractDataComponent().set(xbDataRecord);
        xbElementType
            .getAbstractDataComponent()
            .substitute(XMLConstants.QN_SWE_2_0_DATA_RECORD, DataRecordType.type);

        // encoding
        TextEncodingType textEncoding = TextEncodingType.Factory.newInstance();
        // token
        textEncoding.setTokenSeparator(TOKEN_SEPARATOR);
        // block seperator
        textEncoding.setBlockSeparator(BLOCK_SEPARATOR);
        xbDataArray.addNewEncoding().addNewAbstractEncoding().set(textEncoding);
        xbDataArray
            .getEncoding().getAbstractEncoding()
            .substitute(XMLConstants.QN_SWE_2_0_TEXT_ENCODING, TextEncodingType.type);

        // values
        xbDataArray.addNewValues().set(createValuesString(timeSeries));
        obsParameter.addObservationValue(xbDataArrayDoc.xmlText());
    }

    private XmlString createValuesString(TimeSeries timeSeries) {
        // values <-- linebreak every 100 lines?
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (final InsertObservation io : timeSeries.getInsertObservations()) {
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
        XmlString xbValueString = XmlString.Factory.newInstance();
        xbValueString.setStringValue(valueString);
        return xbValueString;
    }

}
