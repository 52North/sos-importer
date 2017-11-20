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
package org.n52.sos.importer.feeder.importer;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.model.TimeSeriesRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class ResultHandlingImporter extends SweArrayObservationWithSplitExtensionImporter {

    private static final Logger LOG = LoggerFactory.getLogger(ResultHandlingImporter.class);

    private List<String> storedResultTemplates = new LinkedList<>();

    @Override
    protected void insertAllTimeSeries(TimeSeriesRepository timeSeriesRepository)
            throws OXFException, XmlException, IOException {
        LOG.trace("insertAllTimeSeries()");
        ONE_IMPORTER_LOCK.lock();
        try {
            timeSeriesRepository.getTimeSeries().parallelStream().forEach(new InsertTimeSeries(timeSeriesRepository));
        } finally {
            ONE_IMPORTER_LOCK.unlock();
        }
    }

    private class InsertTimeSeries implements Consumer<TimeSeries> {

        private TimeSeriesRepository timeSeriesRepository;

        InsertTimeSeries(TimeSeriesRepository timeSeriesRepository) {
            this.timeSeriesRepository = timeSeriesRepository;
        }

        @Override
        public void accept(TimeSeries t) {
            // ensure existence of each sensor in sos
            if (!sosClient.isSensorRegistered(t.getSensorURI()) && !failedSensorInsertions.contains(t.getSensorURI())) {
                // OPTIONAL: register/insertSensor
                SimpleEntry<String, String> insertSensorResult = null;
                try {
                    insertSensorResult = sosClient.insertSensor(timeSeriesRepository.getInsertSensor(t.getSensorURI()));
                } catch (OXFException | XmlException | IOException | EncodingException e) {
                    LOG.error("Could not register sensor '{}' at sos instance.", t.getSensorURI());
                }
                if (insertSensorResult == null || insertSensorResult.getKey() == null) {
                    failedSensorInsertions.add(t.getSensorURI());
                    return;
                }
            }
            // ensure existence of resultTemplate for each timeseries
            try {
                if (!sosClient.isResultTemplateRegistered(t.getSensorURI(), t.getObservedProperty().getUri())) {
                    // OPTIONAL: insertResultTemplate: store id somewhere
                    //try {
                    String resultTemplateId = sosClient.insertResultTemplate(t);
                    storedResultTemplates.add(t.hashCode(), resultTemplateId);
                    //} catch (OXFException | XmlException | IOException e) {
                    //    LOG.error("Could not insert result template for '{}', '{}' at sos instance.",
                    //            t.getSensorURI(),
                    //            t.getObservedProperty().getUri());
                    //    failedSensorInsertions.add(t.getSensorURI());
                    //    return;
                    //}
                }
            } catch (EncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // insert result
        }

    }
}
