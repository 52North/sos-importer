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
package org.n52.sos.importer.feeder.task;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.exceptions.InvalidColumnCountException;
import org.n52.sos.importer.feeder.exceptions.JavaApiBugJDL6203387Exception;
import org.n52.sos.importer.feeder.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>RepeatedFeeder class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class RepeatedFeeder extends TimerTask {

    /**
     * 
     */
    private static final String EXCEPTION_THROWN = "Exception thrown: {}";
    /**
     * 
     */
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    /**
     * 
     */
    private static final String LAST_FEED_FILE = "lastFeedFile";
    private static File lastUsedDateFile;
    private static final Logger LOG = LoggerFactory.getLogger(RepeatedFeeder.class);
    private static final Lock ONE_FEEDER_LOCK = new ReentrantLock(true);

    private final Configuration configuration;
    private final File file;

    private final int periodInMinutes;

    /**
     * <p>Constructor for RepeatedFeeder.</p>
     *
     * @param c a {@link org.n52.sos.importer.feeder.Configuration} object.
     * @param f a {@link java.io.File} object.
     * @param periodInMinutes a int.
     */
    public RepeatedFeeder(final Configuration c, final File f, final int periodInMinutes) {
        configuration = c;
        file = f;
        this.periodInMinutes = periodInMinutes;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        LOG.trace("run()");
        File datafile;
        // used to sync access to lastUsedDateFile and to not have more than one feeder at a time.
        ONE_FEEDER_LOCK.lock();
        try {
            /*
             * save last feeded file incl. counter
             * check for newer files
             * each on own thread?
             *  feed all obs in last feeded file
             *  feed all newer files
             */
            // if file is a directory, get latest from file list
            if (file.isDirectory()) {
                final ArrayList<File> filesToFeed = new ArrayList<File>();
                getLastFeedFile();
                if (lastUsedDateFile != null) {
                    filesToFeed.add(lastUsedDateFile);
                }
                addNewerFiles(filesToFeed);
                for (final File fileToFeed : filesToFeed) {
                    LOG.info("Start feeding file {}", fileToFeed.getName());
                    try {
                        new OneTimeFeeder(configuration, fileToFeed).run();
                        lastUsedDateFile = fileToFeed;
                        saveLastFeedFile();
                        LOG.info("Finished feeding file {}.", fileToFeed.getName());
                    } catch (final InvalidColumnCountException iae) {
                        // Exception is already logged -> nothing to do
                    } catch (final JavaApiBugJDL6203387Exception e) {
                        // Exception is already logged -> nothing to do
                    }
                }
            } else {
                datafile = file;
                // OneTimeFeeder with file override used not as thread
                new OneTimeFeeder(configuration, datafile).run();
                LOG.info("Finished feeding file {}. Next run in {} minute{}.",
                        datafile.getName(),
                        periodInMinutes,
                        periodInMinutes > 1 ? "s" : "");
            }
        } catch (final InvalidColumnCountException iae) {
            // Exception is already logged -> nothing to do
        } catch (final JavaApiBugJDL6203387Exception e) {
            // Exception is already logged -> nothing to do
        } catch (final Exception e) {
            LOG.error("Exception catched. Switch logging to debug for more details: {}", e.getMessage());
            LOG.debug("StackTrace:", e);
        } finally {
            ONE_FEEDER_LOCK.unlock();
        }
    }

    private void addNewerFiles(final ArrayList<File> filesToFeed) {
        // TODO if last feed file is null: add all (OR only the newest?) files in directory to list "filesToFeed"
        // TODO else: get all files newer than last feed file and add to list "filesToFeed"
        final File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.isFile() &&
                        pathname.canRead() &&
                        (configuration.getLocaleFilePattern() != null
                        ? configuration.getLocaleFilePattern().matcher(pathname.getName()).matches()
                                : true);
            }
        });
        if (files != null) {
            for (final File fileToCheck : files) {
                if (lastUsedDateFile == null || fileToCheck.lastModified() >= lastUsedDateFile.lastModified()) {
                    filesToFeed.add(fileToCheck);
                }
            }
            if (filesToFeed.size() < 1) {
                LOG.error("No new file found in directory '{}'. Last used file was '{}'.",
                        file.getAbsolutePath(),
                        lastUsedDateFile != null
                        ? lastUsedDateFile.getName()
                                : "none");
            }
        } else {
            LOG.error("No file found in directory '{}'", file.getAbsolutePath());
        }
    }

    private void saveLastFeedFile() {
        final Properties prop = new Properties();
        prop.put(LAST_FEED_FILE, lastUsedDateFile.getAbsolutePath());
        try {
            prop.store(new FileWriter(FileHelper.getHome().getAbsolutePath() + File.separator + 
                    FileHelper.cleanPathToCreateFileName(
                            configuration.getConfigFile().getAbsolutePath()) + PROPERTIES_FILE_EXTENSION),
                    null);
            LOG.info("Saved last used data file: {}", lastUsedDateFile.getName());
        } catch (final IOException e) {
            LOG.error(EXCEPTION_THROWN, e.getMessage(), e);
        }
    }

    private void getLastFeedFile() {
        final Properties prop = new Properties();
        String lastFeedFilePropertiesPath = "";
        try {
            lastFeedFilePropertiesPath = new StringBuffer(FileHelper.getHome().getAbsolutePath())
                .append(File.separator)
                .append(FileHelper.cleanPathToCreateFileName(configuration.getConfigFile().getAbsolutePath()))
                .append(PROPERTIES_FILE_EXTENSION)
                .toString();
            prop.load(new FileReader(lastFeedFilePropertiesPath));
        } catch (final FileNotFoundException fnfe) {
            LOG.debug(String.format("Last feed file properties not found: %s", lastFeedFilePropertiesPath));
        } catch (final IOException e) {
            // only on DEBUG because it is not a problem if this file does not exist
            LOG.debug(EXCEPTION_THROWN, e.getMessage(), e); 
        }
        final String lastFeedFileName = prop.getProperty(LAST_FEED_FILE);
        if (lastFeedFileName == null) {
            return;
        }
        final File lastFeedFile = new File(lastFeedFileName);
        if (lastFeedFile.canRead()) {
            lastUsedDateFile = lastFeedFile;
        } else {
            lastUsedDateFile = null;
        }
    }

}
