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
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class SingleThreadFeeder extends Feeder {

    private static final Logger LOG = LoggerFactory.getLogger(SingleThreadFeeder.class);


    @Override
    public Feeder init(Configuration config) throws MalformedURLException {
        super.init(config);
        setCollectorPhaser(null);
        setAdderThreads(null);
        return this;
    }

    @Override
    public void importData(DataFile dataFile)
            throws IOException, XmlException, IllegalArgumentException, ParseException {
        LOG.info("Start importing data via '{}'", this.getClass().getName());
        CountDownLatch latch = new CountDownLatch(1);
        LocalDateTime startImportingData = LocalDateTime.now();
        setExceptions(new ArrayList<>());
        getImporter().startImporting();
        getCollector().collectObservations(dataFile, latch);
        if (getImporter().hasFailedObservations()) {
            handleFailedObservations(getImporter().getFailedObservations());
        }
        if (getApplicationContext() != null) {
            getApplicationContext().close();
        }
        int failedObservations = getImporter().getFailedObservations().size();
        final int newObservationsCount = getCollectedObservationsCount() - failedObservations;
        LOG.info("New observations in SOS: {}. Failed observations: {}.",
                newObservationsCount,
                failedObservations);
        LOG.debug("Import Timing:\nStart : {}\nEnd   : {}", startImportingData, LocalDateTime.now());
        if (!getExceptions().isEmpty()) {
            handleExceptions();
        }
    }

    @Override
    public void addObservationForImporting(InsertObservation... insertObservations) {
        if (insertObservations == null || insertObservations.length == 0) {
            return;
        }
        try {
            getImporter().addObservations(insertObservations);
            increaseCollectedObservationsCount(insertObservations.length);
        } catch (Exception e) {
            getExceptions().add(e);
            getCollector().stopCollecting();
        }
    }

}
