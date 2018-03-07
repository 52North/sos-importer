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
package org.n52.sos.importer.feeder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.AbstractMap.SimpleEntry;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.web.HttpClient;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 * @since 0.5.0
 */
public interface SosClient {

    boolean isInstanceAvailable();

    boolean isInstanceTransactional();

    boolean isSensorRegistered(URI sensorURI);

    SimpleEntry<URI, URI> insertSensor(InsertSensor insertSensor)
            throws XmlException, IOException, EncodingException;

    String insertObservation(InsertObservation insertObservation) throws IOException;

    String insertSweArrayObservation(TimeSeries timeSeries) throws IOException;

    void setHttpClient(HttpClient client);

    void setConfiguration(Configuration configuration) throws MalformedURLException;

    boolean isResultTemplateRegistered(URI sensorURI, URI observedPropertyUri) throws EncodingException;

    String insertResultTemplate(TimeSeries timeseries);

    boolean insertResult(TimeSeries ts);

}
