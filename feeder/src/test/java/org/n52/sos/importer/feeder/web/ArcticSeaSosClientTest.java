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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.xmlbeans.XmlException;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.shetland.util.CollectionHelper;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.svalbard.encode.exception.EncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.sos.x20.CapabilitiesDocument;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.GetResultTemplateResponseDocument;
import net.opengis.sos.x20.InsertObservationResponseDocument;
import net.opengis.sos.x20.InsertResultResponseDocument;
import net.opengis.sos.x20.InsertResultTemplateResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class ArcticSeaSosClientTest {

    @Autowired
    private ArcticSeaSosClient sosClient;

    private Configuration configuration;
    private HttpClient client;
    private HttpEntity entity;
    private HttpResponse response;
    private Optional<SortedSet<SosObservationOffering>> contents;
    private SosCapabilities capabilitiesCache;
    private SosObservationOffering offering;
    private StatusLine status;
    private URI sensorUri;
    private URI propertyUri;
    private TimeSeries timeseries;
    private URI featureUri;

    @Before
    public void setUp() throws IOException {
        configuration = Mockito.mock(Configuration.class);
        client = Mockito.mock(HttpClient.class);
        response = Mockito.mock(CloseableHttpResponse.class);
        status = Mockito.mock(StatusLine.class);
        entity = Mockito.mock(HttpEntity.class);
        capabilitiesCache = Mockito.mock(SosCapabilities.class);
        Mockito.when(configuration.getSosUrl()).thenReturn(new URL("http://localhost/no-service"));
        Mockito.when(configuration.getSosVersion()).thenReturn(Sos2Constants.SERVICEVERSION);
        Mockito.when(status.getStatusCode()).thenReturn(404);
        Mockito.when(response.getStatusLine()).thenReturn(status);
        Mockito.when(response.getEntity()).thenReturn(entity);
        Mockito.when(client.executeGet(Mockito.anyString())).thenReturn(response);
        Mockito.when(client.executePost(Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        sosClient.setConfiguration(configuration);
        sosClient.setHttpClient(client);
        sosClient.cleanCache();

        offering = new SosObservationOffering();
        sensorUri = URI.create("test-sensor");
        featureUri = URI.create("feature-uri");
        propertyUri = URI.create("prop-uri");
        offering.setProcedures(CollectionHelper.list(sensorUri.toString()));
        offering.setIdentifier("test-offering");
        TreeSet<SosObservationOffering> treeset = new TreeSet<>();
        treeset.add(offering);
        contents = Optional.of(treeset);

        timeseries = new TimeSeries();
        timeseries.addObservation(createInsertObservation(52.0, 0));
    }

    @Test
    public void isInstanceAvailableShouldReturnFalseWhenSosIsNotAvailable() throws MalformedURLException {
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(false));
        Mockito.when(status.getStatusCode()).thenReturn(400);
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(false));
        Mockito.when(status.getStatusCode()).thenReturn(199);
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(false));
    }

    @Test
    public void isInstanceAvailableShouldReturnTrueWhenSosIsAvailable() throws MalformedURLException {
        Mockito.when(status.getStatusCode()).thenReturn(200);
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(true));
        Mockito.when(status.getStatusCode()).thenReturn(399);
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(true));
        Mockito.when(status.getStatusCode()).thenReturn(301);
        Assert.assertThat(sosClient.isInstanceAvailable(), Is.is(true));
    }

    @Test
    public void isInstanceTransactionalShouldReturnTrueWhenAllRequiredOperationsAreAvailable()
            throws UnsupportedOperationException, IOException {
        // ONLINE - running SOS instance available at http://localhost:8080/52n-sos-webapp/service
        // sosClient.setHttpClient(new SimpleHttpClient());
        // Mockito.when(configuration.getSosUrl()).thenReturn(new URL("http://localhost:8080/52n-sos-webapp/service"));
        // sosClient.setConfiguration(configuration);
        // OFFLINE
        CapabilitiesDocument capaDoc = CapabilitiesDocument.Factory.newInstance();
        CapabilitiesType capabilities = capaDoc.addNewCapabilities();
        capabilities.setVersion(Sos2Constants.SERVICEVERSION);
        OperationsMetadata operationsMetadata = capabilities.addNewOperationsMetadata();
        operationsMetadata.addNewOperation().setName("InsertSensor");
        operationsMetadata.addNewOperation().setName("InsertObservation");
        operationsMetadata.addNewOperation().setName("InsertResult");
        operationsMetadata.addNewOperation().setName("InsertResultTemplate");
        Mockito.when(entity.getContent()).thenReturn(capaDoc.newInputStream());
        //

        Assert.assertThat(sosClient.isInstanceTransactional(), Is.is(true));
    }

    @Test
    public void isSensorRegisteredShouldReturnTrueWhenContainedInContentsSection()
            throws UnsupportedOperationException, IOException, XmlException {
        URI sensorUri = URI.create("test-sensor");
        CapabilitiesDocument capaDoc = createCapabilitiesWithOffering(sensorUri.toString());
        Mockito.when(entity.getContent()).thenReturn(capaDoc.newInputStream());

        Assert.assertThat(sosClient.isSensorRegistered(sensorUri), Is.is(true));
    }

    @Test
    public void isSensorRegisteredShouldReturnFalseWhenNotContainedInContentsSection()
            throws UnsupportedOperationException, IOException, XmlException {
        CapabilitiesDocument capaDoc = createCapabilitiesWithOffering("test-sensor");
        Mockito.when(entity.getContent()).thenReturn(capaDoc.newInputStream());

        Assert.assertThat(sosClient.isSensorRegistered(URI.create("sensor-not-found")), Is.is(false));
    }

    @Test
    public void insertSensorShouldCreateRequestAndProcessResponse()
            throws EncodingException, XmlException, IOException {
        InsertSensor registerSensor = Mockito.mock(InsertSensor.class);
        Mockito.when(registerSensor.getSensorName()).thenReturn("sensor-name");
        Mockito.when(registerSensor.getSensorURI()).thenReturn(sensorUri);
        Collection<ObservedProperty> properties = CollectionHelper.list(
                new ObservedProperty("property-1-name", "porperty-1-uri"),
                new ObservedProperty("property-2-name", "porperty-2-uri"));
        Mockito.when(registerSensor.getObservedProperties()).thenReturn(properties);
        URI offering = URI.create("offering-name");
        Mockito.when(registerSensor.getOfferingUri()).thenReturn(offering);
        Mockito.when(registerSensor.getMeasuredValueType(Mockito.any(ObservedProperty.class))).thenReturn("NUMERIC");
        Mockito.when(registerSensor.getUnitOfMeasurementCode(Mockito.any(ObservedProperty.class))).thenReturn("uom");

        Mockito.when(entity.getContent()).thenReturn(createInsertSensorResponse(sensorUri.toString(), offering.toString()).newInputStream());
        SimpleEntry<URI, URI> insertSensorResponse = sosClient.insertSensor(registerSensor);

        Assert.assertThat(insertSensorResponse.getKey(), Is.is(sensorUri));
        Assert.assertThat(insertSensorResponse.getValue(), Is.is(offering));
    }

    @Test(expected = NoSuchElementException.class)
    public void isResultTemplateRegisteredShouldThrowNoSuchElementExceptionWhenSensorIsNotRegistered()
            throws EncodingException {
        sosClient.isResultTemplateRegistered(sensorUri, propertyUri);
    }

    @Test
    public void isResultTemplateRegisteredShouldReturnTrueIfTemplateIsRegistered()
            throws EncodingException, UnsupportedOperationException, IOException, XmlException {
        GetResultTemplateResponseDocument getResultTemplateDoc = createResultTemplateResponse();
        Mockito.when(entity.getContent()).thenReturn(getResultTemplateDoc.newInputStream());
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        sosClient.setCache(capabilitiesCache);

        Assert.assertThat(sosClient.isResultTemplateRegistered(sensorUri, propertyUri), Is.is(true));
    }

    @Test
    public void shouldInsertResult() throws UnsupportedOperationException, IOException, XmlException {
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        Mockito.when(entity.getContent()).thenReturn(createInsertResultResponse().newInputStream());
        sosClient.setCache(capabilitiesCache);

        timeseries.addObservation(createInsertObservation(42.0, 5000));
        timeseries.addObservation(createInsertObservation(32.0, 15000));
        timeseries.addObservation(createInsertObservation(22.0, 25000));
        timeseries.addObservation(createInsertObservation(12.0, 35000));

        Assert.assertThat(sosClient.insertResult(timeseries), Is.is(true));
    }

    @Test
    public void shouldHandleInsertResultExceptionReponse()
            throws UnsupportedOperationException, IOException, XmlException {
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        Mockito.when(entity.getContent()).thenReturn(createInsertResultExceptionResponse().newInputStream());
        sosClient.setCache(capabilitiesCache);

        Assert.assertThat(sosClient.insertResult(timeseries), Is.is(false));
    }

    @Test
    public void shouldInsertResultTemplate() throws UnsupportedOperationException, IOException, XmlException {
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        Mockito.when(entity.getContent()).thenReturn(createInsertResultTemplateResponse().newInputStream());
        sosClient.setCache(capabilitiesCache);

        Assert.assertThat(sosClient.insertResultTemplate(timeseries),
                Is.is("template-" + sensorUri + "-" + propertyUri));
    }

    @Test
    public void shouldReturnTemplateIdentifierIfTemplateWithIdentifierIsAlreadyRegistered()
            throws UnsupportedOperationException, IOException, XmlException, EncodingException {
        ExceptionReportDocument exceptionReportDoc = createResultTemplateResponseForDuplicateIdentifier();
        Mockito.when(entity.getContent()).thenReturn(exceptionReportDoc.newInputStream());
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        sosClient.setCache(capabilitiesCache);

        Assert.assertThat(sosClient.insertResultTemplate(timeseries),
                Is.is("template-" + sensorUri + "-" + propertyUri));
    }

    @Test
    public void shouldInsertObservation() throws UnsupportedOperationException, IOException, XmlException {
        InsertObservationResponseDocument requestDoc = createInsertObservationResponse();
        Mockito.when(entity.getContent()).thenReturn(requestDoc.newInputStream());
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);

        InsertObservation io = createInsertObservation(55.0, 0);
        Assert.assertThat(sosClient.insertObservation(io), Is.is("SOS 2.0 Instances do not return the observation id"));
    }

    @Test
    public void shouldNotFailOnDuplicateInsertObservation()
            throws UnsupportedOperationException, IOException, XmlException {
        ExceptionReportDocument exceptionReportDoc = createInsertObservationDuplicationResponse();
        Mockito.when(entity.getContent()).thenReturn(exceptionReportDoc.newInputStream());
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);

        InsertObservation io = createInsertObservation(55.0, 0);
        Assert.assertThat(sosClient.insertObservation(io), Is.is(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED));
    }

    @Test
    public void shouldInsertSweArrayObservation() throws XmlException, UnsupportedOperationException, IOException {
        InsertObservationResponseDocument requestDoc = createInsertObservationResponse();
        Mockito.when(entity.getContent()).thenReturn(requestDoc.newInputStream());
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);

        timeseries.addObservation(createInsertObservation(42.0, 5000));
        timeseries.addObservation(createInsertObservation(32.0, 15000));
        timeseries.addObservation(createInsertObservation(22.0, 25000));
        timeseries.addObservation(createInsertObservation(12.0, 35000));

        Assert.assertThat(sosClient.insertSweArrayObservation(timeseries),
                Is.is("SOS 2.0 Instances do not return the observation id"));
    }

    private ExceptionReportDocument createInsertObservationDuplicationResponse() throws XmlException {
        return ExceptionReportDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ows:ExceptionReport xmlns:ows=\"http://www.opengis.net/ows/1.1\" version=\"2.0.0\">\n" +
                "  <ows:Exception exceptionCode=\"NoApplicableCode\">\n" +
                "    <ows:ExceptionText>The observation for procedure=" + sensorUri +
                "observedProperty=" + propertyUri +
                "featureOfInter=" + featureUri +
                "phenomenonTime=Time instant: " + new DateTime("1970-01-01T00:00:00.000Z") + ",null" +
                "resultTime=Time instant: " + new DateTime("1970-01-01T00:00:00.000Z") + ",null already exists in the database!" +
                "</ows:ExceptionText>\n" +
                "  </ows:Exception>\n" +
                "</ows:ExceptionReport>");
    }

    private InsertObservationResponseDocument createInsertObservationResponse() throws XmlException {
        return InsertObservationResponseDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<sos:InsertObservationResponse xmlns:sos=\"http://www.opengis.net/sos/2.0\"/>");
    }

    private ExceptionReportDocument createInsertResultExceptionResponse() throws XmlException {
        return ExceptionReportDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ows:ExceptionReport xmlns:ows=\"http://www.opengis.net/ows/1.1\" version=\"2.0.0\">\n" +
                "  <ows:Exception exceptionCode=\"NoApplicableCode\">\n" +
                "    <ows:ExceptionText>The observation for procedure=http://www.52north.org/test/procedure/9"
                + "observedProperty=http://www.52north.org/test/observableProperty/9_3featureOfInter=http://www."
                + "52north.org/test/featureOfInterest/9phenomenonTime=Time instant: 2012-11-19T13:30:00.000+02:00,"
                + "nullresultTime=Time instant: 2012-11-19T13:30:00.000+02:00,null already exists in the database!"
                + "</ows:ExceptionText>\n" +
                "  </ows:Exception>\n" +
                "</ows:ExceptionReport>");
    }

    private InsertResultResponseDocument createInsertResultResponse() throws XmlException {
        return InsertResultResponseDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<sos:InsertResultResponse xmlns:sos=\"http://www.opengis.net/sos/2.0\" />");
    }

    private InsertObservation createInsertObservation(Object value, int offset) {
        return new InsertObservation(
                new Sensor("sensor-name", sensorUri.toString()),
                new FeatureOfInterest("feature-name", featureUri.toString(),
                        new Position(new double[] {1.0,  2.0,  3.0}, new String[] {"deg", "deg", "deg"}, 4326)),
                value,
                new Timestamp().ofUnixTimeMillis(0 + offset),
                new UnitOfMeasurement("uom-code", "uom-uri"),
                new ObservedProperty("prop-name", propertyUri.toString()),
                new Offering("offering-name", "offering-uri"),
                Optional.empty(),
                "NUMERIC");
    }

    private ExceptionReportDocument createResultTemplateResponseForDuplicateIdentifier() throws XmlException {
        return ExceptionReportDocument.Factory.parse("<ows:ExceptionReport " +
                "xmlns:ows=\"http://www.opengis.net/ows/1.1\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"2.0.0\" " +
                "xsi:schemaLocation=\"http://www.opengis.net/ows/1.1 " +
                "http://schemas.opengis.net/ows/1.1.0/owsAll.xsd\">\n" +
                "<ows:Exception exceptionCode=\"InvalidParameterValue\" locator=\"identifier\">\n" +
                "<ows:ExceptionText>The requested resultTemplate identifier ("
                + "template-" + sensorUri + "-" + propertyUri + ") " +
                "is already registered at this service</ows:ExceptionText>\n" +
                "</ows:Exception>\n" +
                "</ows:ExceptionReport>");
    }

    private InsertResultTemplateResponseDocument createInsertResultTemplateResponse() throws XmlException {
        return InsertResultTemplateResponseDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<sos:InsertResultTemplateResponse xmlns:sos=\"http://www.opengis.net/sos/2.0\">\n" +
                "  <sos:acceptedTemplate>template-" + sensorUri + "-" + propertyUri + "</sos:acceptedTemplate>\n" +
                "</sos:InsertResultTemplateResponse>");
    }

    private CapabilitiesDocument createCapabilitiesWithOffering(String sensoorUri) throws XmlException {
        return CapabilitiesDocument.Factory.parse("<Capabilities version=\"2.0.0\" " +
                "xmlns=\"http://www.opengis.net/sos/2.0\"  " +
                "xmlns:swes=\"http://www.opengis.net/swes/2.0\">" +
                "<contents><Contents><swes:offering><ObservationOffering>" +
                "<swes:identifier>http://www.52north.org/test/offering/9</swes:identifier>" +
                "<swes:procedure>" +
                sensoorUri +
                "</swes:procedure>" +
                "<swes:procedureDescriptionFormat>http://www.opengis.net/sensorml/2.0" +
                "</swes:procedureDescriptionFormat>" +
                "<swes:observableProperty>http://www.52north.org/test/observableProperty/9_1" +
                "</swes:observableProperty>" +
                "<responseFormat>http://www.opengis.net/om/2.0</responseFormat><observationType>" +
                "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement</observationType>" +
                "<featureOfInterestType>" +
                "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint</featureOfInterestType>" +
                "</ObservationOffering></swes:offering></Contents></contents></Capabilities>");
    }

    private InsertSensorResponseDocument createInsertSensorResponse(String procedure, String offering)
            throws XmlException {
        return InsertSensorResponseDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<swes:InsertSensorResponse xmlns:swes=\"http://www.opengis.net/swes/2.0\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"http://www.opengis.net/swes/2.0 "
                + "http://schemas.opengis.net/swes/2.0/swesInsertSensor.xsd\">\n" +
                "  <swes:assignedProcedure>" + procedure + "</swes:assignedProcedure>\n" +
                "  <swes:assignedOffering>" + offering + "</swes:assignedOffering>\n" +
                "</swes:InsertSensorResponse>");
    }

    private GetResultTemplateResponseDocument createResultTemplateResponse() throws XmlException {
        return GetResultTemplateResponseDocument.Factory.parse("<sos:GetResultTemplateResponse\n" +
                "    xmlns:sos=\"http://www.opengis.net/sos/2.0\"\n" +
                "    xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n" +
                "    xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
                "  <sos:resultStructure>\n" +
                "    <swe:DataRecord>\n" +
                "      <swe:field name=\"phenomenonTime\">\n" +
                "        <swe:Time definition=\"http://www.opengis.net/def/property/OGC/0/PhenomenonTime\">\n" +
                "          <swe:uom xlink:href=\"http://www.opengis.net/def/uom/ISO-8601/0/Gregorian\"/>\n" +
                "        </swe:Time>\n" +
                "      </swe:field>\n" +
                "      <swe:field name=\"test_observable_property_9\">\n" +
                "        <swe:Quantity definition=\"http://www.52north.org/test/observableProperty/9_3\">\n" +
                "          <swe:uom code=\"test_unit_9\"/>\n" +
                "        </swe:Quantity>\n" +
                "      </swe:field>\n" +
                "    </swe:DataRecord>\n" +
                "  </sos:resultStructure>\n" +
                "  <sos:resultEncoding>\n" +
                "    <swe:TextEncoding tokenSeparator=\"#\" blockSeparator=\"@\"/>\n" +
                "  </sos:resultEncoding>\n" +
                "</sos:GetResultTemplateResponse>");
    }

}
