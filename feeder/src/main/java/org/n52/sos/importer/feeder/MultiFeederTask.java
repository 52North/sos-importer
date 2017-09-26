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
 * @author <a href="mailto:m.radtke@52north.org">Maurin Radtke</a>
 */
public class MultiFeederTask {

    private static final Logger LOG = LoggerFactory.getLogger(MultiFeederTask.class);
    
    private static final Lock ONE_PERIODIC_FEEDING_LOCK = new ReentrantLock(true);

    private File directory;

    private int period;

    private ExecutorService threads;

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
                                pathname.getName().toLowerCase().endsWith(".xml");
                    }

                });
                if (files != null && files.length > 0) {
                    for (final File fileEntry : files) {
                        // create FeedingTask
                        try {
                            Configuration config = new Configuration(fileEntry.getAbsolutePath());
                            FeedingTask currentFeedingTask = new FeedingTask(config);
                            // submit FeedingTask into threadsPool
                            threads.submit(currentFeedingTask);
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

    public MultiFeederTask(String directoryPath, int periodInMins, int threads) {
        directory = new File(directoryPath);
        period = periodInMins;
        // TODO: name Threads
        this.threads = Executors.newFixedThreadPool(threads);
    }

    public void startFeeding() {
        PeriodicMultiFeederTask pmft = new PeriodicMultiFeederTask();
        Timer timer = new Timer();
        final int periodInMillis = period*60*1000;
        timer.scheduleAtFixedRate(pmft, 0, periodInMillis);
    }

}
