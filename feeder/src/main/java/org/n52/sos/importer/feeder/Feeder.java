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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class Feeder implements FeedingContext {

    private static final Logger LOG = LoggerFactory.getLogger(Feeder.class);

    private int lastLine;

    private Configuration configuration;

    private Timestamp lastUsedTimestamp;

    private Importer importer;

    private Collector collector;

    private int collectedObservationsCount;

    private List<Exception> exceptions;

    private SosClient sosClient;

    private ClassPathXmlApplicationContext applicationContext;

    private Phaser collectorPhaser;

    // TODO Replace with thread pool naming the threaads
    private ExecutorService adderThreads = Executors.newFixedThreadPool(5);

    public Feeder() {}

    public Feeder init(Configuration config) throws MalformedURLException {
        LOG.trace(String.format("init(%s)", config.toString()));

        configuration = config;

        setApplicationContext(new ClassPathXmlApplicationContext("/application-context.xml"));

        sosClient = (SosClient) getApplicationContext().getBean("sosClient");
        sosClient.setConfiguration(configuration);
        sosClient.setHttpClient(null);

        importer = (Importer) Application.initObjectByClassName(configuration.getImporterClassName());
        getImporter().setConfiguration(configuration);
        getImporter().setFeedingContext(this);
        getImporter().setSosClient(sosClient);

        collector = (Collector) Application.initObjectByClassName(configuration.getCollectorClassName());
        getCollector().setConfiguration(configuration);
        getCollector().setFeedingContext(this);

        collectedObservationsCount = 0;

        setCollectorPhaser(new Phaser());
        getCollectorPhaser().register();
        LOG.info("Instance of Feeder '{}' created.", this.getClass().getName());
        return this;
    }

    public void importData(DataFile dataFile)
            throws IOException, XmlException, IllegalArgumentException, ParseException {
        LOG.info("Start importing data via '{}'", this.getClass().getName());
        CountDownLatch latch = new CountDownLatch(1);
        LocalDateTime startImportingData = LocalDateTime.now();
        setExceptions(new ArrayList<>());
        initCollectorThread(dataFile, latch).start();
        getImporter().startImporting();
        try {
            // FIXME is this double synchronization? e.g. latch not required anymore because of phaser
            latch.await();
            getCollectorPhaser().arriveAndAwaitAdvance();
        } catch (InterruptedException e) {
            log(e);
        }
        try {
            getAdderThreads().shutdown();
        } catch (Exception e) {
            getExceptions().add(e);
        }
        try {
            getImporter().stopImporting();
        } catch (Exception e) {
            getExceptions().add(e);
        }
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

        getAdderThreads().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getCollectorPhaser().register();
                    getImporter().addObservations(insertObservations);
                    increaseCollectedObservationsCount(insertObservations.length);
                } catch (Exception e) {
                    getExceptions().add(e);
                    getCollector().stopCollecting();
                } finally {
                    getCollectorPhaser().arriveAndDeregister();
                }
            }
        });
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

    protected synchronized void increaseCollectedObservationsCount(int collectedObservations) {
        collectedObservationsCount += collectedObservations;
    }

    protected synchronized int getCollectedObservationsCount() {
        return collectedObservationsCount;
    }

    private Thread initCollectorThread(DataFile dataFile, CountDownLatch latch) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getCollector().collectObservations(dataFile, latch);
                } catch (Exception e) {
                    try {
                        getImporter().stopImporting();
                    } catch (Exception e2) {
                        getExceptions().add(e2);
                    } finally {
                        getExceptions().add(e);
                    }
                }
            }
        }, "collector-" + getCollector().getClass().getSimpleName());
    }

    private void log(Exception exception) {
        LOG.error("Exception thrown: {}", exception.getMessage());
        LOG.debug("Stacktrace:", exception);
    }

    protected void handleExceptions() {
        if (getExceptions().isEmpty()) {
            // Nothing to handle
            return;
        }
        // first level of handling: logging
        for (Exception exception : getExceptions()) {
            log(exception);
        }
        // FIXME implement better handling than logging
        throw new RuntimeException("Excpetion thrown during feeding -> feeder stopped.");
    }

    protected void handleFailedObservations(List<InsertObservation> failedObservations) {
        // TODO the failed insert observations should be handled here!
        // FIXME implement
    }

    protected Phaser getCollectorPhaser() {
        return collectorPhaser;
    }

    protected void setCollectorPhaser(Phaser collectorPhaser) {
        this.collectorPhaser = collectorPhaser;
    }

    protected ExecutorService getAdderThreads() {
        return adderThreads;
    }

    protected void setAdderThreads(ExecutorService adderThreads) {
        this.adderThreads = adderThreads;
    }

    protected List<Exception> getExceptions() {
        return exceptions;
    }

    protected void setExceptions(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    protected ClassPathXmlApplicationContext getApplicationContext() {
        return applicationContext;
    }

    protected void setApplicationContext(ClassPathXmlApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected Collector getCollector() {
        return collector;
    }

    protected Importer getImporter() {
        return importer;
    }
}
