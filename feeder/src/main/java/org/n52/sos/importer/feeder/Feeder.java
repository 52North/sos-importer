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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles connection to SOS and provides an easy to use interface.<br>
 * Now this class supports only OGC SOS <b>1.0.0</b>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public final class Feeder implements FeedingContext {

    private static final Logger LOG = LoggerFactory.getLogger(Feeder.class);

    private int lastLine;

    private Configuration configuration;

    private Timestamp lastUsedTimestamp;

    private final Importer importer;

    private final Collector collector;

    private int collectedObservationsCount;

    private List<Exception> exceptions;

    private SosClient sosClient;

    /**
     * <p>Constructor for Feeder.</p>
     *
     * @param config a {@link org.n52.sos.importer.feeder.Configuration} object.
     * @throws org.n52.oxf.ows.ExceptionReport if any.
     * @throws org.n52.oxf.OXFException if any.
     * @throws java.net.MalformedURLException if any.
     */
    public Feeder(final Configuration config)
            throws ExceptionReport, OXFException, MalformedURLException {
        LOG.trace(String.format("Feeder(%s)", config.toString()));
        configuration = config;
        sosClient = new SosClient(configuration);
        importer = (Importer) initObjectByClassName(configuration.getImporterClassName());
        importer.setConfiguration(configuration);
        importer.setSosClient(sosClient);
        importer.setFeedingContext(this);
        collector = (Collector) initObjectByClassName(configuration.getCollectorClassName());
        collector.setConfiguration(configuration);
        collector.setFeedingContext(this);
        collectedObservationsCount = 0;
    }

    private Object initObjectByClassName(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor((Class<?>[]) null);
            return constructor.newInstance();
        } catch (ClassNotFoundException |
                NoSuchMethodException |
                SecurityException |
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException e) {
            String errorMsg = String.format("Could not load defined type implementation class '%s'. Cancel import",
                    className);
            LOG.error(errorMsg);
            log(e);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    public boolean isSosAvailable() {
        return sosClient.isInstanceAvailable();
    }

    /**
     * Checks for <b>Register/InsertSensor</b> and <b>InsertObservation</b> operations.
     *
     * @return <code>true</code> if Register/InsertSensor and InsertObservation
     *         operations are listed in the capabilities of this SOS, <br>
     *         else <code>false</code>.
     */
    public boolean isSosTransactional() {
        return sosClient.isInstanceTransactional();
    }

    @Override
    public void addObservationForImporting(InsertObservation... insertObservations) {
        if (insertObservations == null || insertObservations.length == 0) {
            return;
        }
        increaseCollectedObservationsCount(insertObservations.length);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    importer.addObservationForImporting(insertObservations);
                } catch (Exception e) {
                    exceptions.add(e);
                    collector.stopCollecting();
                }
            }
        }).start();
    }

    @Override
    public boolean shouldUpdateLastUsedTimestamp(Timestamp newLastUsedTimestamp) {
        return configuration.isUseLastTimestamp()
                && newLastUsedTimestamp != null
                && newLastUsedTimestamp.isAfter(lastUsedTimestamp);
    }

    private synchronized void increaseCollectedObservationsCount(int collectedObservations) {
        collectedObservationsCount += collectedObservations;
    }

    public void importData(DataFile dataFile)
            throws IOException, OXFException, XmlException, IllegalArgumentException, ParseException {
        LOG.trace("importData()");
        CountDownLatch latch = new CountDownLatch(1);
        LocalDateTime startImportingData = LocalDateTime.now();
        exceptions = new ArrayList<>();
        startCollector(dataFile, latch);
        importer.startImporting();
        try {
            latch.await();
        } catch (InterruptedException e) {
            log(e);
        }
        try {
            importer.stopImporting();
        } catch (Exception e) {
            exceptions.add(e);
        }
        if (!exceptions.isEmpty()) {
            handleExceptions();
        }
        if (importer.hasFailedObservations()) {
            handleFailedObservations(importer.getFailedObservations());
        }
        LOG.debug("Import Timing:\nStart : {}\nEnd   : {}", startImportingData, LocalDateTime.now());
    }

    private void startCollector(DataFile dataFile, CountDownLatch latch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    collector.collectObservations(dataFile, latch);
                } catch (Exception e) {
                    try {
                        importer.stopImporting();
                    } catch (Exception e2) {
                        exceptions.add(e2);
                    } finally {
                        exceptions.add(e);
                    }
                }
            }
        }, "collector-" + collector.getClass().getSimpleName()).start();
    }

    private void log(Exception exception) {
        LOG.error("Exception thrown: {}", exception.getMessage());
        LOG.debug("Stacktrace:", exception);
    }

    private void handleExceptions() {
        // FIXME implement better handling than logging
        // first level of handling: logging
        for (Exception exception : exceptions) {
            log(exception);
        }
    }

    private void handleFailedObservations(List<InsertObservation> failedObservations) {
        // TODO the failed insert observations should be handled here!
        // FIXME implement
        final int newObservationsCount = collectedObservationsCount - failedObservations.size();
        LOG.info("New observations in SOS: {}. Failed observations: {}.",
                newObservationsCount,
                failedObservations.size());
    }

    @Override
    public int getLastReadLine() {
        return lastLine;
    }

    @Override
    public void setLastReadLine(final int lastLine) {
        LOG.debug("Lastline updated: old: {}; new: {}", this.lastLine, lastLine);
        this.lastLine = lastLine;
    }

    @Override
    public Timestamp getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    @Override
    public void setLastUsedTimestamp(final Timestamp timeStamp) {
        LOG.debug("LastUsedTimestamp updated: old: {}; new: {}", lastUsedTimestamp, timeStamp);
        lastUsedTimestamp = timeStamp;
    }
}
