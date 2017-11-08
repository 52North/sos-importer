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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.n52.oxf.OXFException;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.shetland.util.CollectionHelper;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.svalbard.encode.exception.EncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.sos.x20.CapabilitiesDocument;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.GetResultTemplateResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:application-context.xml")
public class ArcticSeaSosClientTest {

    private Configuration configuration;
    private HttpClient client;
    private HttpResponse response;
    private HttpEntity entity;
    private StatusLine status;
    private SosCapabilities capabilitiesCache;

    @Autowired
    private ArcticSeaSosClient sosClient;

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
        String sensorUri = "test-sensor";
        CapabilitiesDocument capaDoc = createCapabilitiesWithOffering(sensorUri);
        Mockito.when(entity.getContent()).thenReturn(capaDoc.newInputStream());

        Assert.assertThat(sosClient.isSensorRegistered(sensorUri), Is.is(true));
    }

    @Test
    public void isSensorRegisteredShouldReturnFalseWhenNotContainedInContentsSection()
            throws UnsupportedOperationException, IOException, XmlException {
        CapabilitiesDocument capaDoc = createCapabilitiesWithOffering("test-sensor");
        Mockito.when(entity.getContent()).thenReturn(capaDoc.newInputStream());

        Assert.assertThat(sosClient.isSensorRegistered("sensor-not-found"), Is.is(false));
    }

    @Test
    public void insertSensorShouldCreateRequestAndProcessResponse() throws EncodingException, OXFException, XmlException, IOException {
        InsertSensor registerSensor = Mockito.mock(InsertSensor.class);
        Mockito.when(registerSensor.getSensorName()).thenReturn("sensor-name");
        String procedure = "sensor-uri";
        Mockito.when(registerSensor.getSensorURI()).thenReturn(procedure);
        Collection<ObservedProperty> properties = CollectionHelper.list(
                new ObservedProperty("property-1-name", "porperty-1-uri"),
                new ObservedProperty("property-2-name", "porperty-2-uri"));
        Mockito.when(registerSensor.getObservedProperties()).thenReturn(properties);
        String offering = "offering-name";
        Mockito.when(registerSensor.getOfferingUri()).thenReturn(offering);
        Mockito.when(registerSensor.getMeasuredValueType(Mockito.any(ObservedProperty.class))).thenReturn("NUMERIC");
        Mockito.when(registerSensor.getUnitOfMeasurementCode(Mockito.any(ObservedProperty.class))).thenReturn("uom");

        Mockito.when(entity.getContent()).thenReturn(createInsertSensorResponse(procedure, offering).newInputStream());
        SimpleEntry<String, String> insertSensorResponse = sosClient.insertSensor(registerSensor);

        Assert.assertThat(insertSensorResponse.getKey(), Is.is(procedure));
        Assert.assertThat(insertSensorResponse.getValue(), Is.is(offering));
    }

    @Test(expected = NoSuchElementException.class)
    public void isResultTemplateRegisteredShouldThrowNoSuchElementExceptionWhenSensorIsNotRegistered()
            throws EncodingException {
        sosClient.isResultTemplateRegistered("sensor-uri", "property");
    }

    @Test
    public void isResultTemplateRegisteredShouldReturnTrueIfTemplateIsRegistered()
            throws EncodingException, UnsupportedOperationException, IOException, XmlException {
        GetResultTemplateResponseDocument getResultTemplateDoc = createResultTemplateResponse();
        Mockito.when(entity.getContent()).thenReturn(getResultTemplateDoc.newInputStream());
        SosObservationOffering offering = new SosObservationOffering();
        String sensorUri = "test-sensor";
        offering.setProcedures(CollectionHelper.list(sensorUri));
        offering.setIdentifier("test-offering");
        TreeSet<SosObservationOffering> treeset = new TreeSet<>();
        treeset.add(offering);
        Optional<SortedSet<SosObservationOffering>> contents = Optional.of(treeset);
        Mockito.when(capabilitiesCache.getContents()).thenReturn(contents);
        sosClient.setCache(capabilitiesCache);

        Assert.assertThat(sosClient.isResultTemplateRegistered(sensorUri, "test-property"), Is.is(true));
    }

    private CapabilitiesDocument createCapabilitiesWithOffering(String sensorUri) throws XmlException {
        return CapabilitiesDocument.Factory.parse("<Capabilities version=\"2.0.0\" " +
                "xmlns=\"http://www.opengis.net/sos/2.0\"  " +
                "xmlns:swes=\"http://www.opengis.net/swes/2.0\">" +
                "<contents><Contents><swes:offering><ObservationOffering>" +
                "<swes:identifier>http://www.52north.org/test/offering/9</swes:identifier>" +
                "<swes:procedure>" +
                sensorUri +
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

    private InsertSensorResponseDocument createInsertSensorResponse(String procedure, String offering) throws XmlException {
        return InsertSensorResponseDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<swes:InsertSensorResponse xmlns:swes=\"http://www.opengis.net/swes/2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/swes/2.0 http://schemas.opengis.net/swes/2.0/swesInsertSensor.xsd\">\n" +
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
