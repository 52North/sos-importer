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
import org.n52.janmayen.http.MediaType;
import org.n52.oxf.OXFException;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
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
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.shetland.ogc.sos.response.GetResultTemplateResponse;
import org.n52.shetland.ogc.sos.response.InsertResultTemplateResponse;
import org.n52.shetland.ogc.sos.response.InsertSensorResponse;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
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

    private static final String GET_REQUEST_CAPABILITIES =
            "?service=SOS&request=GetCapabilities";

    private static final String GET_REQUEST_SERVICE_PROVIDER =
            "?service=SOS&request=GetCapabilities&Sections=ServiceProvider";

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

    private String blockSeparator;

    private String tokenSeparator;

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

    private boolean checkCache() {
        if (!capabilitiesCache.isPresent()) {
            requestCapabilities();
        }
        return capabilitiesCache.isPresent();
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

    protected Object decodeResponse(HttpResponse response)
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
            throws OXFException, XmlException, IOException, EncodingException {
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
            case "TEXT":
                sweType = new SweText();
                break;
            case "COUNT":
                sweType = new SweCount();
                break;
            case "BOOLEAN":
                sweType = new SweBoolean();
                break;
            case "NUMERIC":
            default:
                sweType  = new SweQuantity();
                ((SweQuantity) sweType).setUom(unitOfMeasurementCode);
        }
        return sweType;
    }

    @Override
    public String insertObservation(InsertObservation io) throws IOException {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not Yet Implemented! ");
    }

    @Override
    public String insertSweArrayObservation(TimeSeries timeSeries) throws IOException {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not Yet Implemented!");
    }

    protected void cleanCache() {
        capabilitiesCache = Optional.empty();
    }

    protected void setCache(SosCapabilities capabilitiesCache) {
        this.capabilitiesCache = Optional.of(capabilitiesCache);
    }

    @Override
    public boolean isResultTemplateRegistered(String sensorURI, String observedPropertyUri) throws EncodingException {
        GetResultTemplateRequest request = new GetResultTemplateRequest();
        request.setOffering(getOfferingByProcedure(sensorURI));
        request.setObservedProperty(observedPropertyUri);
        try {
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodeResponse = decodeResponse(response);
            if (decodeResponse instanceof GetResultTemplateResponse) {
                return true;
            }
        } catch (IOException | DecodingException | OwsExceptionReport | XmlException e) {
            logException(e);
        }
        return false;
    }

    private String encodeRequest(OwsServiceRequest request) throws EncodingException {
        request.setService(SosConstants.SOS);
        request.setVersion(serviceVersion);
        EncoderKey encoderKey = new OperationRequestEncoderKey(SosConstants.SOS,
                serviceVersion,
                serviceVersion.equalsIgnoreCase(Sos2Constants.SERVICEVERSION) ?
                        Sos2Constants.Operations.valueOf(request.getOperationName()) :
                            Sos1Constants.Operations.valueOf(request.getOperationName()),
                MediaType.application("xml"));
        Encoder<XmlObject, OwsServiceRequest> encoder = encoderRepository.getEncoder(encoderKey);
        if (encoder == null) {
            throw new NoEncoderForKeyException(encoderKey);
        }
        XmlObject xmlRequest = encoder.encode(request);
        return xmlRequest.xmlText();
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

    @Override
    public String insertResultTemplate(TimeSeries timeseries) {
        InsertResultTemplateRequest request = new InsertResultTemplateRequest(SosConstants.SOS, serviceVersion);
        request.setIdentifier(createIdentifier(timeseries));
        try {
            OmObservationConstellation sosObservationConstellation = createObservationTemplate(timeseries);
            request.setObservationTemplate(sosObservationConstellation);
            request.setResultStructure(createResultStructure(timeseries));
            request.setResultEncoding(createResultEncoding(timeseries));
            HttpResponse response = client.executePost(uri, encodeRequest(request));
            Object decodedResponse = decodeResponse(response);
            if (decodedResponse instanceof InsertResultTemplateResponse) {
                return ((InsertResultTemplateResponse) response).getAcceptedTemplate();
            }
        } catch (EncodingException | IOException | DecodingException | OwsExceptionReport | XmlException |
                InvalidSridException | NumberFormatException | ParseException e) {
           logException(e);
        }
        return null;
    }

    private SosResultStructure createResultStructure(TimeSeries timeseries) {
        //FIXME are there any constants for these strings in arctic sea?
        UoM uom = new UoM("");
        uom.setLink("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");

        SweTime sweTime = new SweTime();
        sweTime.setDefinition("http://www.opengis.net/def/property/OGC/0/PhenomenonTime");
        sweTime.setUom(uom);

        SweField timestampField = new SweField("phenomenonTime", sweTime);

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

    private OmObservationConstellation createObservationTemplate(TimeSeries timeseries) throws InvalidSridException, NumberFormatException, ParseException {
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

    private AbstractFeature createFeature(InsertObservation insertObservation) throws InvalidSridException, NumberFormatException, ParseException {
        SamplingFeature samplingFeature = new SamplingFeature(new CodeWithAuthority(insertObservation.getFeatureOfInterestURI()));
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

    private String getObservationType(String measuredValueType) {
        switch (measuredValueType) {
            case "BOOLEAN":
                return OmConstants.OBS_TYPE_TRUTH_OBSERVATION;
            case "COUNT":
                return OmConstants.OBS_TYPE_COUNT_OBSERVATION;
            case "TEXT":
                return OmConstants.OBS_TYPE_TEXT_OBSERVATION;
            case "NUMERIC":
            default:
                return OmConstants.OBS_TYPE_MEASUREMENT;
                // throw new IllegalArgumentException("Observation Type '" + measuredValueType + "' not supported.");
        }
    }

    private SosResultEncoding createResultEncoding(TimeSeries timeseries) {
        SweTextEncoding encoding = new SweTextEncoding();
        encoding.setBlockSeparator(blockSeparator);
        encoding.setTokenSeparator(tokenSeparator);
        return new SosResultEncoding(encoding);
    }

    private String createOffering(TimeSeries timeseries) {
        return getOfferingByProcedure(timeseries.getSensorURI());
    }

    private String createIdentifier(TimeSeries timeseries) {
        return String.format("template-%s-%s", timeseries.getSensorURI(), timeseries.getObservedProperty().getUri());
    }

}
