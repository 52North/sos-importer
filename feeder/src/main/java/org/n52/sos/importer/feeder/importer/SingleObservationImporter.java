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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.Importer;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Importer} implementation which imports each received {@link InsertObservation}
 * directly as single observation into the SOS instance. This implementation uses 5 threads
 * for parallel importing.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class SingleObservationImporter extends ImporterSkeleton {

    private static final Logger LOG = LoggerFactory.getLogger(SingleObservationImporter.class);

    private ExecutorService importerThreads;

    @Override
    public void addObservations(InsertObservation... insertObservations)
            throws XmlException, IOException {
        if (insertObservations == null || insertObservations.length == 0) {
            return;
        }
        if (importerThreads == null) {
            // TODO Replace with thread pool naming the threaads
            importerThreads = Executors.newFixedThreadPool(configuration.getImporterThreadsCount());
        }

        importerThreads.submit(new InsertObservationTask(insertObservations, sosClient, failedSensorInsertions,
                configuration, failedObservations, context));
    }

    @Override
    public void stopImporting() {
        try {
            importerThreads.shutdown();
            while (!importerThreads.awaitTermination(60, TimeUnit.SECONDS)) {
                LOG.info("Awaiting completion of threads.");
            }
        } catch (InterruptedException e) {
            LOG.debug("We are interurupted!");
        }
    }

}
