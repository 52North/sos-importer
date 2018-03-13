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
import java.util.concurrent.Phaser;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Manages the observation collection and import workflow using a multithreading approach. Hence,
 * observations are collected and imported in parallel. The details depend on the used {@link Collector}
 * and {@link Importer} implementation. In addition, the {@link SosClient} implementation bean is loaded.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
@Configurable
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

    private ClassPathXmlApplicationContext applicationContext;

    private Phaser collectorPhaser;

    /**
     * <p>Constructor for Feeder.</p>
     *
     * @param config a {@link org.n52.sos.importer.feeder.Configuration} object.
     * @throws java.net.MalformedURLException if any.
     */
    public Feeder(final Configuration config)
            throws MalformedURLException {
        LOG.trace(String.format("Feeder(%s)", config.toString()));

        configuration = config;

        applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");

        sosClient = (SosClient) applicationContext.getBean("sosClient");
        sosClient.setConfiguration(configuration);
        sosClient.setHttpClient(null);

        importer = (Importer) initObjectByClassName(configuration.getImporterClassName());
        importer.setConfiguration(configuration);
        importer.setFeedingContext(this);
        importer.setSosClient(sosClient);

        collector = (Collector) initObjectByClassName(configuration.getCollectorClassName());
        collector.setConfiguration(configuration);
        collector.setFeedingContext(this);

        collectedObservationsCount = 0;

        collectorPhaser = new Phaser();
        collectorPhaser.register();
    }

    public void importData(DataFile dataFile)
            throws IOException, XmlException, IllegalArgumentException, ParseException {
        LOG.trace("importData()");
        CountDownLatch latch = new CountDownLatch(1);
        LocalDateTime startImportingData = LocalDateTime.now();
        exceptions = new ArrayList<>();
        initCollectorThread(dataFile, latch).start();
        importer.startImporting();
        try {
            // FIXME is this double synchronization? e.g. latch not required anymore because of phaser
            latch.await();
            collectorPhaser.arriveAndAwaitAdvance();
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
        if (applicationContext != null) {
            applicationContext.close();
        }
        int failedObservations = importer.getFailedObservations().size();
        final int newObservationsCount = getCollectedObservationsCount() - failedObservations;
        LOG.info("New observations in SOS: {}. Failed observations: {}.",
                newObservationsCount,
                failedObservations);
        LOG.debug("Import Timing:\nStart : {}\nEnd   : {}", startImportingData, LocalDateTime.now());
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
                    collectorPhaser.register();
                    importer.addObservationForImporting(insertObservations);
                } catch (Exception e) {
                    exceptions.add(e);
                    collector.stopCollecting();
                } finally {
                    collectorPhaser.arriveAndDeregister();
                }
            }
        }).start();
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

    @Override
    public boolean shouldUpdateLastUsedTimestamp(Timestamp newLastUsedTimestamp) {
        return configuration.isUseLastTimestamp()
                && newLastUsedTimestamp != null
                && newLastUsedTimestamp.isAfter(lastUsedTimestamp);
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

    private synchronized void increaseCollectedObservationsCount(int collectedObservations) {
        collectedObservationsCount += collectedObservations;
    }

    private synchronized int getCollectedObservationsCount() {
        return collectedObservationsCount;
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

    private Thread initCollectorThread(DataFile dataFile, CountDownLatch latch) {
        return new Thread(new Runnable() {
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
        }, "collector-" + collector.getClass().getSimpleName());
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
    }
}
