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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>MultiFeederTask class.</p>
 *
 * TODO add more log statements
 *
 * @author <a href="mailto:m.radtke@52north.org">Maurin Radtke</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">J6uuml;rrens, Eike Hinderk</a>
 */
public class MultiFeederTask {

    private static final Logger LOG = LoggerFactory.getLogger(MultiFeederTask.class);

    private static final Lock ONE_PERIODIC_FEEDING_LOCK = new ReentrantLock(true);

    private File directory;

    private int period;

    private ExecutorService threads;

    public MultiFeederTask(String directoryPath, int periodInMins, int threadCount) {
        directory = new File(directoryPath);
        period = periodInMins;
        // TODO: name Threads
        threads = Executors.newFixedThreadPool(threadCount);
    }

    public void startFeeding() {
        int periodInMillis = period * 60 * 1000;
        new Timer("multi-feeder-task").scheduleAtFixedRate(new PeriodicMultiFeederTask(), 0, periodInMillis);
    }

    private class PeriodicMultiFeederTask extends TimerTask {

        @Override
        public void run() {
            try {
                ONE_PERIODIC_FEEDING_LOCK.lock();
                File[] files = directory.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isFile() &&
                                pathname.canRead() &&
                                pathname.getName().toLowerCase().endsWith("-config.xml");
                    }

                });
                if (files != null && files.length > 0) {
                    for (final File fileEntry : files) {
                        try {
                            Configuration config = new Configuration(fileEntry.getAbsolutePath());
                            threads.submit(new Runnable() {

                                @Override
                                public void run() {
                                    new FeedingTask(config).feedData();
                                }
                            });
                        } catch (XmlException | IOException e) {
                            LOG.error("Only valid XML format is supported.");
                            continue;
                        }
                    }
                } else {
                    LOG.error("No config file found in directory '{}'", directory.getAbsolutePath());
                }
            } finally {
                ONE_PERIODIC_FEEDING_LOCK.unlock();
            }
        }
    }

}
