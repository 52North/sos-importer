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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SortedSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.ows.OwsOperation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapability;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.request.GetResultTemplateRequest;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.shetland.ogc.sos.request.InsertResultRequest;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.shetland.ogc.sos.response.GetResultTemplateResponse;
import org.n52.shetland.ogc.sos.response.InsertObservationResponse;
import org.n52.shetland.ogc.sos.response.InsertResultResponse;
import org.n52.shetland.ogc.sos.response.InsertResultTemplateResponse;
import org.n52.shetland.ogc.sos.response.InsertSensorResponse;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JTSHelper;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.SosClient;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.OperationRequestEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.vividsolutions.jts.io.ParseException;

/**
 * SosClient using the <a href="https://github.com/52North/arctic-sea">52&deg;North Arctic-Sea project</a>
 * for communicating with a SOS instance.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
@Configurable
public class ArcticSeaSosClient implements SosClient {

    public static final String OBSERVATION_ALREADY_IN_DATABASE = "Observation already in database";

    public static final String SOS_2_0_OBSERVATION_INSERTED = "SOS 2.0 Instances do not return the observation id";

    private static final String UTC_PLUS_PATTERN = "\\+00:00";

    private static final String GET_REQUEST_CAPABILITIES =
            "?service=SOS&request=GetCapabilities";

    private static final String GET_REQUEST_SERVICE_PROVIDER =
            "?service=SOS&request=GetCapabilities&Sections=ServiceProvider";

    private static final String SOS_20_DUPLICATE_OBSERVATION_FORMAT =
            "The observation for procedure=%sobservedProperty=%sfeatureOfInter=%sphenomenonTime=Time instant: %s,null"
            + "resultTime=Time instant: %s,null already exists in the database!";

    private static final Logger LOG = LoggerFactory.getLogger(ArcticSeaSosClient.class);

    private HttpClient client;

    private String uri;

    private DecoderRepository decoderRepository;

    private EncoderRepository encoderRepository;

    private Configuration configuration;

    private Optional<SosCapabilities> capabilitiesCache = Optional.empty();

    private List<String> sos20RequiredTransactionalOperations = Arrays.asList(
            Sos2Constants.Operations.InsertSensor.name(),
            SosConstants.Operations.InsertObservation.name(),
            Sos2Constants.Operations.InsertResultTemplate.name(),
            Sos2Constants.Operations.InsertResult.name());

    private List<String> sos10RequiredTransactionalOperations = Arrays.asList(
            "RegisterSensor",
            SosConstants.Operations.InsertObservation.name(),
            Sos2Constants.Operations.InsertResultTemplate.name(),
            Sos2Constants.Operations.InsertResult.name());

    private List<String> registeredSensors = new LinkedList<>();

    private String serviceVersion;

    private String blockSeparator = "|";

    private String tokenSeparator = ";";

    @Override
    public void setHttpClient(HttpClient client) {
        if (client == null) {
            SimpleHttpClient simpleClient = new SimpleHttpClient();
            if (configuration.getInsertSweArrayObservationTimeoutBuffer() > 0) {
                simpleClient.setConnectionTimout(configuration.getInsertSweArrayObservationTimeoutBuffer());
                simpleClient.setSocketTimout(configuration.getInsertSweArrayObservationTimeoutBuffer());
            }
            this.client = simpleClient;
        } else {
            this.client = client;
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) throws MalformedURLException {
        this.configuration = configuration;
        uri = this.configuration.getSosUrl().toString();
        serviceVersion = this.configuration.getSosVersion();
    }

    public DecoderRepository getDecoderRepository() {
        return decoderRepository;
    }

    @Autowired
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    public EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    @Autowired
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Override
    public boolean isInstanceAvailable() {
        try {
            HttpResponse response = client.executeGet(uri + GET_REQUEST_SERVICE_PROVIDER);
            if (response.getStatusLine().getStatusCode() > 199 && response.getStatusLine().getStatusCode() < 400) {
                return true;
            }
        } catch (IOException e) {
            logException(e);
        }
        return false;
    }

    @Override
    public boolean isInstanceTransactional() {
        if (checkCache() && capabilitiesCache.get().getOperationsMetadata().isPresent()) {
            List<String> requiredTransactionalOperations;
            if (capabilitiesCache.get().getVersion().equalsIgnoreCase(Sos1Constants.SERVICEVERSION)) {
                requiredTransactionalOperations = sos10RequiredTransactionalOperations;
            } else {
                requiredTransactionalOperations = sos20RequiredTransactionalOperations;
            }
            SortedSet<OwsOperation> operationsMetadata =
                    capabilitiesCache.get().getOperationsMetadata().get().getOperations();
            int numberOfFoundOperations = operationsMetadata.stream()
                    .map(o -> o.getName())
                    .collect(Collectors.toList())
                    .stream()
                    .filter(o -> requiredTransactionalOperations.contains(o))
                    .collect(Collectors.toList())
                    .size();
            if (numberOfFoundOperations == requiredTransactionalOperations.size()) {
                LOG.debug("All required operations '{}' found.", requiredTransactionalOperations);
                return true;
            } else {
                LOG.error("NOT all required operations '{}' in set of offered ones '{}'.",
                        requiredTransactionalOperations,
                        operationsMetadata.stream().map(o -> o.getName()).collect(Collectors.toList()));
            }
        }
        return false;
    }

    @Override
    public boolean isSensorRegistered(String sensorURI) {
        return checkCache() &&
                capabilitiesCache.get().getContents().isPresent() &&
                !capabilitiesCache.get().getContents().get().stream()
                .map(o -> o.getProcedures().first())
                .filter(s -> s.equals(sensorURI))
                .collect(Collectors.toList())
                .isEmpty() || registeredSensors.contains(sensorURI);
    }

    @Override
    public SimpleEntry<String, String> insertSensor(InsertSensor rs)
            throws XmlException, IOException, EncodingException {
        if (serviceVersion.equals(Sos2Constants.SERVICEVERSION)) {
            try {
                InsertSensorRequest request = createInsertSensorRequest(rs);
                HttpResponse response = client.executePost(uri, encodeRequest(request));
                Object decodeResponse = decodeResponse(response);
                if (decodeResponse.getClass().isAssignableFrom(InsertSensorResponse.class)) {
                    return new SimpleEntry<>(
                            ((InsertSensorResponse) decodeResponse).getAssignedProcedure(),
                            ((InsertSensorResponse) decodeResponse).getAssignedOffering());
                }
            } catch (IOException | DecodingException | OwsExceptionReport | XmlException e) {
                logException(e);
            }
        } else {
            LOG.error("insertSensor for SOS 1.0 is NOT supported!");
        }
        return new SimpleEntry<>(null, null);
    }


    @Override
    public String insertObservation(InsertObservation io) {
        InsertObservationRequest request = new InsertObservationRequest();
        request.setOfferings(Arrays.asList(io.getOffering().getUri()));
        request.setAssignedSensorId(io.getSensorURI());
        try {
            request.setObservation(createOmObservation(io));
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertObservationResponse) {
                return SOS_2_0_OBSERVATION_INSERTED;
            }
        } catch (OwsExceptionReport oer) {
            if (isDuplicateObservationError(oer, io)) {
                return OBSERVATION_ALREADY_IN_DATABASE;
            }
            logException(oer);
        } catch (IOException | DecodingException | XmlException | EncodingException |
                InvalidSridException | NumberFormatException | ParseException e) {
            logException(e);
        }
        return "";
    }

    @Override
    public String insertSweArrayObservation(TimeSeries timeSeries) throws IOException {
        InsertObservationRequest request = new InsertObservationRequest();
        request.setOfferings(Arrays.asList(timeSeries.getFirst().getOffering().getUri()));
        request.setAssignedSensorId(timeSeries.getFirst().getSensorURI());
        try {
            request.setObservation(createSweArrayObservation(timeSeries));
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertObservationResponse) {
                return SOS_2_0_OBSERVATION_INSERTED;
            }
        } catch (IOException | DecodingException | XmlException | EncodingException |
                InvalidSridException | NumberFormatException | OwsExceptionReport | ParseException e) {
            logException(e);
        }
        return "";
    }

    @Override
    public boolean isResultTemplateRegistered(String sensorURI, String observedPropertyUri) throws EncodingException {
        GetResultTemplateRequest request = new GetResultTemplateRequest();
        request.setOffering(getOfferingByProcedure(sensorURI));
        request.setObservedProperty(observedPropertyUri);
        try {
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof GetResultTemplateResponse) {
                return true;
            }
        } catch (IOException | DecodingException | OwsExceptionReport | XmlException e) {
            logException(e);
        }
        return false;
    }

    @Override
    public String insertResultTemplate(TimeSeries timeseries) {
        InsertResultTemplateRequest request = new InsertResultTemplateRequest(SosConstants.SOS, serviceVersion);
        request.setIdentifier(createTemplateIdentifier(timeseries));
        try {
            OmObservationConstellation sosObservationConstellation = createObservationTemplate(timeseries);
            request.setObservationTemplate(sosObservationConstellation);
            request.setResultStructure(createResultStructure(timeseries));
            request.setResultEncoding(createResultEncoding());
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertResultTemplateResponse) {
                return ((InsertResultTemplateResponse) decodedResponse).getAcceptedTemplate();
            }
        } catch (OwsExceptionReport oer) {
            if (isTemplateIdentifierAlreadyContainedError(timeseries, oer)) {
                return createTemplateIdentifier(timeseries);
            }
           logException(oer);
        } catch (EncodingException | IOException | DecodingException | XmlException | InvalidSridException |
                NumberFormatException | ParseException e) {
           logException(e);
        }
        return null;
    }

    @Override
    public boolean insertResult(TimeSeries ts) {
        InsertResultRequest request = new InsertResultRequest(SosConstants.SOS, serviceVersion);
        request.setTemplateIdentifier(createTemplateIdentifier(ts));
        request.setResultValues(encodeResultValues(ts));
        try {
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertResultResponse) {
                return true;
            }
        } catch (OwsExceptionReport | EncodingException | IOException | DecodingException | XmlException e) {
            logException(e);
        }
        return false;
    }

    protected void cleanCache() {
        capabilitiesCache = Optional.empty();
    }

    protected void setCache(SosCapabilities capabilitiesCache) {
        this.capabilitiesCache = Optional.of(capabilitiesCache);
    }

    private boolean checkCache() {
        if (!capabilitiesCache.isPresent()) {
            requestCapabilities();
        }
        return capabilitiesCache.isPresent();
    }

    private InsertSensorRequest createInsertSensorRequest(InsertSensor rs) {

        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(CollectionHelper.list(
                "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint"));
        metadata.setObservationTypes(rs.getObservedProperties().stream()
                .map(o -> rs.getMeasuredValueType(o)).collect(Collectors.toList()));

        List<SmlIo> outputs = new ArrayList<>(rs.getObservedProperties().size());
        for (ObservedProperty observedProperty : rs.getObservedProperties()) {
            SmlIo output = new SmlIo();
            output.setIoName(observedProperty.getName());
            output.setIoValue(createSweType(
                    rs.getMeasuredValueType(observedProperty),
                    rs.getUnitOfMeasurementCode(observedProperty),
                    observedProperty.getUri()));
            outputs.add(output);
        }

        SmlCapabilities offeringCapabilities = new SmlCapabilities("offerings");
        SweText offeringSweText = new SweText();
        offeringSweText.setDefinition("urn:ogc:def:identifier:OGC:offeringID");
        offeringSweText.setLabel(String.format("Offering of Sensor '%s'.", rs.getSensorName()));
        offeringSweText.setValue(rs.getOfferingUri());
        offeringCapabilities.addCapability(new SmlCapability("offeringID", offeringSweText));

        PhysicalSystem system = new PhysicalSystem();
        system.setIdentifier(rs.getSensorURI());
        system.setIdentifications(CollectionHelper.list(
                new SmlIdentifier("longName", "urn:ogc:def:identifier:OGC:1.0:longName", rs.getSensorName()),
                new SmlIdentifier("shortName", "urn:ogc:def:identifier:OGC:1.0:shortName", rs.getSensorURI())));
        system.setOutputs(outputs);
        system.addCapabilities(offeringCapabilities);

        InsertSensorRequest request = new InsertSensorRequest(SosConstants.SOS, serviceVersion);
        request.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/2.0");
        request.setProcedureDescription(new SosProcedureDescription<>(system));
        request.setObservableProperty(rs.getObservedProperties().stream()
                .map(p -> p.getUri()).collect(Collectors.toList()));
        request.setMetadata(metadata);
        return request;
    }

    private SweAbstractDataComponent createSweType(String measuredValueType, String unitOfMeasurementCode,
            String definition) {
        SweAbstractDataComponent sweType;
        switch (measuredValueType) {
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                sweType = new SweText();
                break;
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                sweType = new SweCount();
                break;
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                sweType = new SweBoolean();
                break;
            case Configuration.SOS_OBSERVATION_TYPE_NUMERIC:
            default:
                sweType  = new SweQuantity();
                ((SweQuantity) sweType).setUom(unitOfMeasurementCode);
        }
        return sweType;
    }

    private TimeInstant createTimeInstant(Timestamp timestamp) {
        return new TimeInstant(new DateTime(timestamp.toISO8601String()));
    }

    private ObservationValue<?> createObservationValue(InsertObservation io) {
        Time phenomenonTime = createTimeInstant(io.getTimeStamp());
        switch (io.getMeasuredValueType()) {
            case Configuration.SOS_OBSERVATION_TYPE_NUMERIC:
                QuantityValue quantity = new QuantityValue((Double) io.getResultValue(), io.getUnitOfMeasurementCode());
                return new SingleObservationValue<>(phenomenonTime, quantity);
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                BooleanValue booleanValue = new BooleanValue((Boolean) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, booleanValue);
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                CountValue count = new CountValue((Integer) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, count);
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                TextValue text = new TextValue((String) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, text);
            default:
                // TODO or throw an exception
                return null;
        }
    }

    private OmObservationConstellation createObservationTemplate(TimeSeries timeseries)
            throws InvalidSridException, NumberFormatException, ParseException {
        // TODO Review
        SensorML procedure = new SensorML();
        procedure.setIdentifier(timeseries.getSensorURI());

        OmObservationConstellation observationTemplate = new OmObservationConstellation();
        observationTemplate.setObservationType(getObservationType(timeseries.getMeasuredValueType()));
        observationTemplate.setProcedure(procedure);
        observationTemplate.setObservableProperty(new OmObservableProperty(timeseries.getObservedProperty().getUri()));
        observationTemplate.addOffering(createOffering(timeseries));
        observationTemplate.setFeatureOfInterest(createFeature(timeseries.getFirst()));
        return observationTemplate;
    }

    private List<OmObservation> createOmObservation(InsertObservation io)
            throws InvalidSridException, NumberFormatException, ParseException {
        OmObservation omObservation = createOmObservationSkeleton(io);
        omObservation.setValue(createObservationValue(io));
        omObservation.getObservationConstellation().setObservationType(getObservationType(io.getMeasuredValueType()));
        return Arrays.asList(omObservation);
    }

    private List<OmObservation> createSweArrayObservation(TimeSeries timeSeries)
            throws InvalidSridException, NumberFormatException, ParseException {
        OmObservation omObservation = createOmObservationSkeleton(timeSeries.getFirst());
        omObservation.setValue(createSweArrayObservationValue(timeSeries));
        omObservation.getObservationConstellation().setObservationType(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION);
        return Arrays.asList(omObservation);
    }

    private MultiObservationValues<SweDataArray> createSweArrayObservationValue(TimeSeries timeSeries) {
        SweTextEncoding encoding = new SweTextEncoding();
        encoding.setTokenSeparator(tokenSeparator);
        encoding.setBlockSeparator(blockSeparator);

        SweDataRecord elementType = new SweDataRecord();
        elementType.addField(createSwePhenomenonTimeField());
        elementType.addField(createObservedPropertyField(timeSeries.getFirst()));

        SweDataArray dataArray = new SweDataArray();
        dataArray.setElementType(elementType);
        dataArray.setEncoding(encoding);

        timeSeries.getInsertObservations().forEach(io -> dataArray.add(Arrays.asList(
                        io.getTimeStamp().toISO8601String(),
                        io.getResultValue().toString())));

        MultiValue<SweDataArray> dataArrayValue = new SweDataArrayValue();
        dataArrayValue.setValue(dataArray);

        MultiObservationValues<SweDataArray> value = new MultiObservationValues<>();
        value.setValue(dataArrayValue);
        return value;
    }

    private SweField createObservedPropertyField(InsertObservation first) {
        return new SweField(first.getObservedProperty().getName(),
                createSweType(
                        first.getMeasuredValueType(),
                        first.getUnitOfMeasurementCode(),
                        first.getObservedPropertyURI()));
    }

    private SweField createSwePhenomenonTimeField() {
        SweTime sweTime = new SweTime();
        sweTime.setDefinition("http://www.opengis.net/def/property/OGC/0/PhenomenonTime");
        sweTime.setUom(new UoM("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian"));

        SweField timestampField = new SweField("phenomenonTime", sweTime);
        return timestampField;
    }

    private OmObservation createOmObservationSkeleton(InsertObservation io)
            throws InvalidSridException, NumberFormatException, ParseException {
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setGmlId("o1");
        observationConstellation.setObservableProperty(new OmObservableProperty(io.getObservedPropertyURI()));
        observationConstellation.setFeatureOfInterest(createFeature(io));
        PhysicalSystem procedure = new PhysicalSystem();
        procedure.setIdentifier(io.getSensorURI());
        observationConstellation.setProcedure(procedure);

        TimeInstant resultTime = createTimeInstant(io.getTimeStamp());

        OmObservation omObservation = new OmObservation();
        omObservation.setObservationConstellation(observationConstellation);
        omObservation.setResultTime(resultTime);
        return omObservation;
    }

    private SosResultStructure createResultStructure(TimeSeries timeseries) {
        //FIXME are there any constants for these strings in arctic sea?
        SweField timestampField = createSwePhenomenonTimeField();

        SweField measuredValueField = new SweField(
                timeseries.getObservedProperty().getUri(),
                createSweType(timeseries.getMeasuredValueType(),
                        timeseries.getUnitOfMeasurementCode(),
                        timeseries.getObservedProperty().getUri()));

        SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.addField(timestampField);
        dataRecord.addField(measuredValueField);

        return new SosResultStructure(dataRecord);
    }

    private AbstractFeature createFeature(InsertObservation insertObservation)
            throws InvalidSridException, NumberFormatException, ParseException {
        SamplingFeature samplingFeature =
                new SamplingFeature(new CodeWithAuthority(insertObservation.getFeatureOfInterestURI()));
        samplingFeature.setName(new CodeType(insertObservation.getFeatureOfInterestName()));
        if (insertObservation.hasFeatureParentFeature()) {
            samplingFeature.setSampledFeatures(Arrays.asList(new SamplingFeature(
                    new CodeWithAuthority(insertObservation.getParentFeatureIdentifier()))));
        }
        samplingFeature.setGeometry(JTSHelper.createGeometryFromWKT(
                String.format("POINT(%s %s)",
                        insertObservation.getLongitudeValue(),
                        insertObservation.getLatitudeValue()),
                Integer.parseInt(insertObservation.getEpsgCode())));

        return samplingFeature;
    }

    private SosResultEncoding createResultEncoding() {
        SweTextEncoding encoding = new SweTextEncoding();
        encoding.setBlockSeparator(blockSeparator);
        encoding.setTokenSeparator(tokenSeparator);
        return new SosResultEncoding(encoding);
    }

    private String createOffering(TimeSeries timeseries) {
        return getOfferingByProcedure(timeseries.getSensorURI());
    }

    private String createTemplateIdentifier(TimeSeries timeseries) {
        return String.format("template-%s-%s", timeseries.getSensorURI(), timeseries.getObservedProperty().getUri());
    }

    private Object decodeResponse(HttpResponse response)
            throws OwsExceptionReport, DecodingException, XmlException, IOException {
        try (InputStream content = response.getEntity().getContent()) {
            XmlObject xmlResponse = XmlObject.Factory.parse(content);
            DecoderKey decoderKey = CodingHelper.getDecoderKey(xmlResponse);
            Decoder<Object, Object> decoder = getDecoderRepository().getDecoder(decoderKey);
            if (decoder == null) {
                throw new NoDecoderForKeyException(decoderKey);
            }
            Object decode = decoder.decode(xmlResponse);
            if (decode instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) decode;
            }
            return decode;
        }
    }

    private String encodeRequest(OwsServiceRequest request) throws EncodingException, DecodingException {
        request.setService(SosConstants.SOS);
        request.setVersion(serviceVersion);
        EncoderKey encoderKey = new OperationRequestEncoderKey(SosConstants.SOS,
                serviceVersion,
                getOperationNameEnum(request),
                MediaType.application("xml"));
        Encoder<XmlObject, OwsServiceRequest> encoder = encoderRepository.getEncoder(encoderKey);
        if (encoder == null) {
            throw new NoEncoderForKeyException(encoderKey);
        }
        XmlObject xmlRequest = encoder.encode(request);
        XmlHelper.validateDocument(xmlRequest);
        return xmlRequest.xmlText();
    }

    private String encodeResultValues(TimeSeries ts) {
        return ts.getInsertObservations().stream()
                .map(io -> io.getTimeStamp() + tokenSeparator + io.getResultValue().toString())
                .collect(Collectors.joining(blockSeparator));
    }

    private Enum<?> getOperationNameEnum(OwsServiceRequest request) {
        try {
            return SosConstants.Operations.valueOf(request.getOperationName());
        } catch (IllegalArgumentException e) {
            return serviceVersion.equalsIgnoreCase(Sos2Constants.SERVICEVERSION) ?
                    Sos2Constants.Operations.valueOf(request.getOperationName()) :
                        Sos1Constants.Operations.valueOf(request.getOperationName());
        }
    }

    private String getOfferingByProcedure(String sensorURI) {
        NoSuchElementException noSuchElementException =
                new NoSuchElementException("No offering for sensor '" + sensorURI + "'.");
        if (!checkCache() || !capabilitiesCache.get().getContents().isPresent()) {
            throw noSuchElementException;
        }
        List<String> offerings = capabilitiesCache.get().getContents().get().stream()
                .filter(o -> o.getProcedures().first().equals(sensorURI))
                .map(o -> o.getIdentifier())
                .collect(Collectors.toList());
        if (offerings.isEmpty()) {
            throw noSuchElementException;
        }
        return offerings.get(0);
    }

    private String getObservationType(String measuredValueType) {
        switch (measuredValueType) {
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                return OmConstants.OBS_TYPE_TRUTH_OBSERVATION;
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                return OmConstants.OBS_TYPE_COUNT_OBSERVATION;
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                return OmConstants.OBS_TYPE_TEXT_OBSERVATION;
            case Configuration.SOS_OBSERVATION_TYPE_NUMERIC:
            default:
                return OmConstants.OBS_TYPE_MEASUREMENT;
                // throw new IllegalArgumentException("Observation Type '" + measuredValueType + "' not supported.");
        }
    }

    private boolean isTemplateIdentifierAlreadyContainedError(TimeSeries timeseries, OwsExceptionReport oer) {
        return oer.getCause() != null &&
                !oer.getExceptions().isEmpty() &&
                oer.getExceptions().get(0).hasMessage() &&
                oer.getExceptions().get(0).getMessage().equals(
                        String.format("The requested resultTemplate identifier (%s) " +
                                "is already registered at this service",
                                createTemplateIdentifier(timeseries)));
    }

    private boolean isDuplicateObservationError(OwsExceptionReport oer, InsertObservation io) {
        return oer.getCause() != null &&
                !oer.getExceptions().isEmpty() &&
                oer.getExceptions().get(0).hasMessage() &&
                oer.getExceptions().get(0).getMessage().equals(
                        String.format(SOS_20_DUPLICATE_OBSERVATION_FORMAT,
                                io.getSensorURI(),
                                io.getObservedPropertyURI(),
                                io.getFeatureOfInterestURI(),
                                io.getTimeStamp().toISO8601String().replaceAll(UTC_PLUS_PATTERN, "Z"),
                                io.getTimeStamp().toISO8601String().replaceAll(UTC_PLUS_PATTERN, "Z")));
    }

    private void logException(Exception e) {
        if (e instanceof IOException) {
            LOG.error("Problem occured in communication with server: '{}'", e.getMessage());
        } else if (e instanceof DecodingException) {
            LOG.error("Response from server could not be decoded: '{}'", e.getMessage());
        } else if (e instanceof XmlException) {
            LOG.error("Server returned bad XML: '{}'", e.getMessage());
        } else {
            LOG.error("Error Occured: '{}'", e.getMessage());
        }
        LOG.debug("Exception thrown:", e);
    }

    private void requestCapabilities() {
        String request = uri + GET_REQUEST_CAPABILITIES;
        try {
            HttpResponse response = client.executeGet(request);
            GetCapabilitiesResponse decodedResponse = (GetCapabilitiesResponse) decodeResponse(response);
            capabilitiesCache = Optional.of((SosCapabilities) decodedResponse.getCapabilities());
        } catch (IOException | DecodingException | XmlException e) {
            logException(e);
        } catch (OwsExceptionReport e) {
            LOG.error("Request to server failed! Request: '{}' ; Error: '{}'", request, e.getMessage());
            logException(e);
        }
    }

}
