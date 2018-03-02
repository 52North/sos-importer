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
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
import org.n52.shetland.ogc.om.NamedValue;
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
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapability;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sensorML.v20.SmlFeatureOfInterest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
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
import org.n52.shetland.ogc.swe.SweCoordinate;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.CollectionHelper;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.SosClient;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.util.CoordinateHelper;
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
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.io.ParseException;

/**
 * SosClient using the <a href="https://github.com/52North/arctic-sea">52&deg;North Arctic-Sea project</a>
 * for communicating with a SOS instance.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class ArcticSeaSosClient implements SosClient {

    public static final String SOS_2_0_OBSERVATION_INSERTED = "SOS 2.0 Instances do not return the observation id";

    private static final String GET_REQUEST_CAPABILITIES =
            "?service=SOS&request=GetCapabilities";

    private static final String GET_REQUEST_SERVICE_PROVIDER =
            "?service=SOS&request=GetCapabilities&Sections=ServiceProvider";

    private static final Logger LOG = LoggerFactory.getLogger(ArcticSeaSosClient.class);

    private static final Lock INSERT_SENSOR_LOCK = new ReentrantLock(true);

    private static Map<String, String> INSERTED_SENSORS = new ConcurrentSkipListMap<>();

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

    private String serviceVersion;

    private String blockSeparator = "@@";

    private String tokenSeparator = ";";

    @Override
    public void setHttpClient(HttpClient client) {
        if (client == null) {
            SimpleHttpClient simpleClient = new SimpleHttpClient();
            if (configuration.getTimeoutBuffer() > 0) {
                simpleClient.setConnectionTimout(configuration.getTimeoutBuffer());
                simpleClient.setSocketTimout(configuration.getTimeoutBuffer());
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

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    public EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    @Inject
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
                .isEmpty() || INSERTED_SENSORS.containsKey(sensorURI);
    }

    @Override
    public SimpleEntry<String, String> insertSensor(InsertSensor insertSensor)
            throws XmlException, IOException, EncodingException {
        INSERT_SENSOR_LOCK.lock();
        try {
            if (!INSERTED_SENSORS.isEmpty() && INSERTED_SENSORS.containsKey(insertSensor.getSensorURI())) {
                return new SimpleEntry<>(insertSensor.getSensorURI(), insertSensor.getOfferingUri());
            }
            if (serviceVersion.equals(Sos2Constants.SERVICEVERSION)) {
                try {
                    InsertSensorRequest request = createInsertSensorRequest(insertSensor);
                    HttpResponse response = client.executePost(uri, encodeRequest(request));
                    Object decodeResponse = decodeResponse(response);
                    if (decodeResponse.getClass().isAssignableFrom(InsertSensorResponse.class)) {
                        SimpleEntry<String, String> insertedSensor = new SimpleEntry<>(
                                ((InsertSensorResponse) decodeResponse).getAssignedProcedure(),
                                ((InsertSensorResponse) decodeResponse).getAssignedOffering());
                        INSERTED_SENSORS.put(insertedSensor.getKey(), insertedSensor.getValue());
                        return insertedSensor;
                    }
                } catch (IOException | DecodingException | OwsExceptionReport | XmlException |
                        InvalidSridException | NumberFormatException | ParseException | FactoryException e) {
                    logException(e);
                }
            } else {
                LOG.error("insertSensor for SOS 1.0 is NOT supported!");
            }
            return new SimpleEntry<>(null, null);
        } finally {
            INSERT_SENSOR_LOCK.unlock();
        }
    }

    @Override
    public String insertObservation(InsertObservation io) {
        InsertObservationRequest request = new InsertObservationRequest();
        request.setOfferings(Arrays.asList(io.getOffering().getUri()));
        request.setAssignedSensorId(io.getSensorURI());
        try {
            request.setObservation(createObservation(
                    io, createObservationValue(io), getObservationType(io.getMeasuredValueType())));
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertObservationResponse) {
                return SOS_2_0_OBSERVATION_INSERTED;
            }
        } catch (OwsExceptionReport oer) {
            if (isDuplicateObservationError(oer, io)) {
                return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
            }
            logException(oer);
        } catch (IOException | DecodingException | XmlException | EncodingException |
                InvalidSridException | NumberFormatException | ParseException | FactoryException e) {
            logException(e);
        }
        return "";
    }

    @Override
    public String insertSweArrayObservation(TimeSeries timeSeries) throws IOException {
        SweBoolean sweBoolean = new SweBoolean();
        sweBoolean.setValue(true);
        sweBoolean.setDefinition(Sos2Constants.Extensions.SplitDataArrayIntoObservations.name());

        SwesExtension<SweBoolean> swesExtension = new SwesExtension<>();
        swesExtension.setValue(sweBoolean);

        InsertObservationRequest request = new InsertObservationRequest();
        request.setOfferings(Arrays.asList(timeSeries.getFirst().getOffering().getUri()));
        request.setAssignedSensorId(timeSeries.getFirst().getSensorURI());
        request.addExtension(swesExtension);
        try {
            request.setObservation(createObservation(
                    timeSeries.getFirst(),
                    createSweArrayObservationValue(timeSeries),
                    OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION));
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertObservationResponse) {
                return SOS_2_0_OBSERVATION_INSERTED;
            }
        } catch (IOException | DecodingException | XmlException | EncodingException |
                InvalidSridException | NumberFormatException | OwsExceptionReport | ParseException |
                FactoryException e) {
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
                NumberFormatException | ParseException | /*NoSuchAuthorityCodeException |*/ FactoryException e) {
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

    private InsertSensorRequest createInsertSensorRequest(InsertSensor insertSensor) throws IOException,
    InvalidSridException, NumberFormatException, NoSuchAuthorityCodeException, ParseException, FactoryException {

        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(CollectionHelper.list(
                "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint"));
        List<String> observationTypes = insertSensor.getObservedProperties().stream()
                .map(o -> getObservationType(insertSensor.getMeasuredValueType(o)))
                .collect(Collectors.toList());
        if (!observationTypes.isEmpty()
                && !observationTypes.contains(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION)) {
            observationTypes.add(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION);
        }
        metadata.setObservationTypes(observationTypes);

        List<SmlIo> outputs = new ArrayList<>(insertSensor.getObservedProperties().size());
        for (ObservedProperty observedProperty : insertSensor.getObservedProperties()) {
            SmlIo output = new SmlIo();
            output.setIoName(observedProperty.getName());
            output.setIoValue(createSweType(
                    insertSensor.getMeasuredValueType(observedProperty),
                    insertSensor.getUnitOfMeasurementCode(observedProperty),
                    observedProperty.getUri()));
            outputs.add(output);
        }

        SmlCapabilities offeringCapabilities = new SmlCapabilities("offerings");
        SweText offeringSweText = new SweText();
        offeringSweText.setDefinition("urn:ogc:def:identifier:OGC:offeringID");
        offeringSweText.setLabel(String.format("Offering of Sensor '%s'.", insertSensor.getSensorName()));
        offeringSweText.setValue(insertSensor.getOfferingUri());
        offeringCapabilities.addCapability(new SmlCapability("offeringID", offeringSweText));

        PhysicalSystem system = new PhysicalSystem();
        system.setIdentifier(insertSensor.getSensorURI());
        system.setIdentifications(CollectionHelper.list(
                new SmlIdentifier("longName", "urn:ogc:def:identifier:OGC:1.0:longName",
                        insertSensor.getSensorName()),
                new SmlIdentifier("shortName", "urn:ogc:def:identifier:OGC:1.0:shortName",
                        insertSensor.getSensorURI())));
        system.setOutputs(outputs);
        system.addCapabilities(offeringCapabilities);

        if (insertSensor.isSetReferenceValues()) {
            system.addCapabilities(createReferenceValueCapabilities(insertSensor));
        }

        if (insertSensor.isPositionValid()) {
            addPosition(insertSensor, system);
        }
        if (insertSensor.isFeatureAvailable()) {
            addFeature(insertSensor, system);
        }

        InsertSensorRequest request = new InsertSensorRequest(SosConstants.SOS, serviceVersion);
        request.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/2.0");
        request.setProcedureDescription(new SosProcedureDescription<>(system));
        request.setObservableProperty(insertSensor.getObservedProperties().stream()
                .map(p -> p.getUri()).collect(Collectors.toList()));
        request.setMetadata(metadata);
        return request;
    }

    private SmlCapabilities createReferenceValueCapabilities(InsertSensor insertSensor) throws IOException {
        // FIXME is this identifier available as SOS constant? yes, but dependency wanted?
        // @see SensorMLConstants.ELEMENT_NAME_REFERENCE_VALUES
        SmlCapabilities refValueCapabilities = new SmlCapabilities("referenceValues");
        for (Entry<ObservedProperty, List<SimpleEntry<String, String>>> referenceValueMapping :
            insertSensor.getReferenceValues().entrySet()) {
            for (SimpleEntry<String, String> refValueLabelAndValue : referenceValueMapping.getValue()) {
                try {
                    SweQuantity sweQuantity = new SweQuantity(Double.valueOf(refValueLabelAndValue.getValue()),
                            insertSensor.getUnitOfMeasurementCode(referenceValueMapping.getKey()));
                    sweQuantity.setDefinition(referenceValueMapping.getKey().getUri());
                    refValueCapabilities.addCapability(new SmlCapability(refValueLabelAndValue.getKey(), sweQuantity));
                } catch (NumberFormatException nfe) {
                    throw new IOException(String.format(
                            "Could not parse value '%s' of reference value with label '%s' to java.lang.Double.",
                            refValueLabelAndValue.getValue(),
                            refValueLabelAndValue.getKey()), nfe);
                }
            }
        }
        return refValueCapabilities;
    }

    private void addPosition(InsertSensor insertSensor, PhysicalSystem system) {
        //    <sml:position>
        //        <swe:Vector referenceFrame="urn:ogc:def:crs:EPSG::4326">
        //            <swe:coordinate name="easting">
        //                <swe:Quantity axisID="x">
        //                    <swe:uom code="degree"/>
        //                    <swe:value>7.651968812254194</swe:value>
        //                </swe:Quantity>
        //            </swe:coordinate>
        //            <swe:coordinate name="northing">
        //                <swe:Quantity axisID="y">
        //                    <swe:uom code="degree"/>
        //                    <swe:value>51.935101100104916</swe:value>
        //                </swe:Quantity>
        //            </swe:coordinate>
        //            <swe:coordinate name="altitude">
        //                <swe:Quantity axisID="z">
        //                    <swe:uom code="m"/>
        //                    <swe:value>52.0</swe:value>
        //                </swe:Quantity>
        //            </swe:coordinate>
        //        </swe:Vector>
        //    </sml:position>
        SweQuantity longitudeValue = new QuantityValue(insertSensor.getLongitudeValue(),
                insertSensor.getLongitudeUnit());
        SweCoordinate<BigDecimal> longitude = new SweCoordinate<>("longitude", longitudeValue);

        SweQuantity latitudeValue = new QuantityValue(insertSensor.getLatitudeValue(),
                insertSensor.getLatitudeUnit());
        SweCoordinate<BigDecimal> latitude = new SweCoordinate<>("latitude", latitudeValue);

        List<SweCoordinate<BigDecimal>> coordinates;

        if (isAltitudeAvailable(insertSensor)) {
            SweQuantity altitudeValue = new QuantityValue(insertSensor.getAltitudeValue(),
                    insertSensor.getAltitudeUnit());
            SweCoordinate<BigDecimal> altitude = new SweCoordinate<>("altitude", altitudeValue);
            coordinates = CollectionHelper.list(longitude, latitude, altitude);
        } else {
            coordinates = CollectionHelper.list(longitude, latitude);
        }

        SweVector vector = new SweVector();
        vector.setReferenceFrame("urn:ogc:def:crs:EPSG::" + insertSensor.getEpsgCode());
        vector.setCoordinates(coordinates);

        SmlPosition position = new SmlPosition();
        position.setVector(vector);
        system.setPosition(position);
    }

    private void addFeature(InsertSensor insertSensor, PhysicalSystem system) throws InvalidSridException,
            NumberFormatException, NoSuchAuthorityCodeException, ParseException, FactoryException {
        SamplingFeature feature = new SamplingFeature(new CodeWithAuthority(insertSensor.getFeatureOfInterestURI()));
        if (insertSensor.isPositionValid()) {
            feature.setGeometry(CoordinateHelper.createPoint(
                    insertSensor.getLongitudeValue(),
                    insertSensor.getLatitudeValue(),
                    insertSensor.getAltitudeValue(),
                    Integer.parseInt(insertSensor.getEpsgCode())));
        }

        SmlFeatureOfInterest featureOfInterest = new SmlFeatureOfInterest();
        featureOfInterest.addFeatureOfInterest(feature);

        system.setSmlFeatureOfInterest(featureOfInterest);
    }

    private boolean isAltitudeAvailable(InsertSensor insertSensor) {
        return insertSensor.isSetAltitude() && insertSensor.getAltitudeUnit() != null &&
                !insertSensor.getAltitudeUnit().isEmpty();
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
        sweType.setDefinition(definition);
        return sweType;
    }

    private TimeInstant createTimeInstant(Timestamp timestamp) {
        return new TimeInstant(new DateTime(timestamp.toISO8601String()));
    }

    private ObservationValue<?> createObservationValue(InsertObservation io) {
        Time phenomenonTime = createTimeInstant(io.getTimeStamp());
        switch (io.getMeasuredValueType()) {
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                BooleanValue booleanValue = new BooleanValue((Boolean) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, booleanValue);
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                CountValue count = new CountValue((Integer) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, count);
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                TextValue text = new TextValue((String) io.getResultValue());
                return new SingleObservationValue<>(phenomenonTime, text);
            case Configuration.SOS_OBSERVATION_TYPE_NUMERIC:
            default:
                QuantityValue quantity = new QuantityValue((Double) io.getResultValue(), io.getUnitOfMeasurementCode());
                return new SingleObservationValue<>(phenomenonTime, quantity);
        }
    }

    private OmObservationConstellation createObservationTemplate(TimeSeries timeseries)
            throws InvalidSridException, NumberFormatException, ParseException, NoSuchAuthorityCodeException,
            FactoryException {
        OmObservationConstellation observationTemplate = new OmObservationConstellation();
        observationTemplate.setObservationType(getObservationType(timeseries.getMeasuredValueType()));
        observationTemplate.setProcedure(new SosProcedureDescriptionUnknownType(timeseries.getSensorURI()));
        observationTemplate.setObservableProperty(new OmObservableProperty(timeseries.getObservedProperty().getUri()));
        observationTemplate.addOffering(createOffering(timeseries));
        observationTemplate.setFeatureOfInterest(createFeature(timeseries.getFirst()));
        return observationTemplate;
    }

    private List<OmObservation> createObservation(InsertObservation insertObservation,
            ObservationValue<?> value,
            String observationType) throws InvalidSridException, NumberFormatException, NoSuchAuthorityCodeException,
                ParseException, FactoryException {
        OmObservation omObservation = createOmObservationSkeleton(insertObservation);
        omObservation.setValue(value);
        omObservation.getObservationConstellation().setObservationType(observationType);

        if (insertObservation.isSetOmParameters()) {
            for (NamedValue<?> omParameter : insertObservation.getOmParameters()) {
                omObservation.addParameter(omParameter);
            }
        }

        omObservation.setIdentifier(insertObservation.getTimeStamp() +  insertObservation.getObservedPropertyURI() +
                insertObservation.getFeatureOfInterestURI());

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
        sweTime.setDefinition(OmConstants.PHENOMENON_TIME);
        sweTime.setUom(new UoM(OmConstants.PHEN_UOM_ISO8601));

        SweField timestampField = new SweField(OmConstants.EN_PHENOMENON_TIME, sweTime);
        return timestampField;
    }

    private OmObservation createOmObservationSkeleton(InsertObservation io)
            throws InvalidSridException, NumberFormatException, ParseException, NoSuchAuthorityCodeException,
            FactoryException {
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setGmlId("o1");
        observationConstellation.setObservableProperty(new OmObservableProperty(io.getObservedPropertyURI()));
        observationConstellation.setFeatureOfInterest(createFeature(io));
        observationConstellation.setProcedure(new SosProcedureDescriptionUnknownType(io.getSensorURI()));

        TimeInstant resultTime = createTimeInstant(io.getTimeStamp());

        OmObservation omObservation = new OmObservation();
        omObservation.setObservationConstellation(observationConstellation);
        omObservation.setResultTime(resultTime);

        return omObservation;
    }

    private SosResultStructure createResultStructure(TimeSeries timeseries) {
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
            throws InvalidSridException, NumberFormatException, ParseException, NoSuchAuthorityCodeException,
            FactoryException {
        SamplingFeature samplingFeature =
                new SamplingFeature(new CodeWithAuthority(insertObservation.getFeatureOfInterestURI()));
        samplingFeature.setName(new CodeType(insertObservation.getFeatureOfInterestName()));
        if (insertObservation.hasFeatureParentFeature()) {
            samplingFeature.setSampledFeatures(Arrays.asList(new SamplingFeature(
                    new CodeWithAuthority(insertObservation.getParentFeatureIdentifier()))));
        }

        if (insertObservation.isFeaturePositionValid()) {
            samplingFeature.setGeometry(insertObservation.isSetAltitudeValue() ?
                    CoordinateHelper.createPoint(insertObservation.getLongitudeValue(),
                            insertObservation.getLatitudeValue(),
                            insertObservation.getAltitudeValue(),
                            Integer.parseInt(insertObservation.getEpsgCode())) :
                                CoordinateHelper.createPoint(
                                        insertObservation.getLongitudeValue(),
                                        insertObservation.getLatitudeValue(),
                                        Integer.parseInt(insertObservation.getEpsgCode())));
        }

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
                oer.getExceptions().get(0).getMessage().contains(io.getSensorURI()) &&
                oer.getExceptions().get(0).getMessage().contains(io.getObservedPropertyURI()) &&
                oer.getExceptions().get(0).getMessage().contains(io.getFeatureOfInterestURI()) &&
                oer.getExceptions().get(0).getMessage().contains(new DateTime(io.getTimeStamp().toISO8601String())
                        .toString()) &&
                oer.getExceptions().get(0).getMessage().endsWith("already exists in the database!");
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
