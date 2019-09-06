/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.feeder.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.util.FileHelper;
import org.n52.sos.importer.feeder.util.InvalidColumnCountException;
import org.n52.sos.importer.feeder.util.JavaApiBugJDL6203387Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>ScheduledFeedingTask class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class ScheduledFeedingTask extends TimerTask {

    private static final String EXCEPTION_THROWN = "Exception thrown: {}";
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private static final String LAST_FEED_FILE = "lastFeedFile";
    private static File LAST_USED_DATA_FILE;

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledFeedingTask.class);
    private static final Lock ONE_FEEDER_LOCK = new ReentrantLock(true);

    private final Configuration configuration;
    private final File file;
    private final int periodInMinutes;
    private TaskHelper taskHelper;

    public ScheduledFeedingTask(Configuration configuration, File file, int periodInMinutes) {
        this.configuration = configuration;
        this.file = file;
        this.periodInMinutes = periodInMinutes;
        taskHelper = new TaskHelper(configuration);
    }

    @Override
    public void run() {
        LOG.trace("run()");
        // used to sync access to LAST_USED_DATA_FILE and to not have more than one feeder at a time.
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
                ArrayList<File> filesToFeed = new ArrayList<>();
                getLastFeedFile();
                if (getLastUsedDataFile() != null) {
                    filesToFeed.add(getLastUsedDataFile());
                }
                // TODO if last feed file is null: add all (OR only the newest?) files in directory to "filesToFeed"
                // TODO else: get all files newer than last feed file and add to list "filesToFeed"
                List<File> files = taskHelper.getFiles(file);
                for (File fileToCheck : files) {
                    if (getLastUsedDataFile() == null ||
                            fileToCheck.lastModified() >= getLastUsedDataFile().lastModified()) {
                        filesToFeed.add(fileToCheck);
                    }
                }
                if (filesToFeed.size() < 1) {
                    LOG.error("No new file found in directory '{}'. Last used file was '{}'.",
                            file.getAbsolutePath(),
                            getLastUsedDataFile() != null
                            ? getLastUsedDataFile().getName()
                                    : "none");
                }
                for (File fileToFeed : filesToFeed) {
                    LOG.info("Start feeding file {}", fileToFeed.getName());
                    try {
                        new FeedingTask(configuration, fileToFeed).feedData();
                        setLastUsedDataFile(fileToFeed);
                        saveLastFeedFile();
                        LOG.info("Finished feeding file {}.", fileToFeed.getName());
                    } catch (InvalidColumnCountException iae) {
                        // Exception is already logged -> nothing to do
                    } catch (JavaApiBugJDL6203387Exception e) {
                        // Exception is already logged -> nothing to do
                    }
                }
            } else {
                // OneTimeFeeder with file override used not as thread
                new FeedingTask(configuration, file).feedData();
                LOG.info("Finished feeding file {}. Next run in {} minute{}.",
                        file.getName(),
                        periodInMinutes,
                        periodInMinutes > 1 ? "s" : "");
            }
        } catch (InvalidColumnCountException | JavaApiBugJDL6203387Exception e) {
            // Exception is already logged -> nothing to do
        } catch (Exception e) {
            LOG.error("Exception catched. Switch logging to debug for more details: {}", e.getMessage());
            LOG.debug("StackTrace:", e);
        } finally {
            ONE_FEEDER_LOCK.unlock();
        }
    }

    private void saveLastFeedFile() {
        Properties prop = new Properties();
        prop.put(LAST_FEED_FILE, getLastUsedDataFile().getAbsolutePath());
        try (FileWriterWithEncoding fw = new FileWriterWithEncoding(
                    FileHelper.getHome().getAbsolutePath() + File.separator +
                    FileHelper.cleanPathToCreateFileName(configuration.getConfigFile().getAbsolutePath()) +
                    PROPERTIES_FILE_EXTENSION,
                Configuration.DEFAULT_CHARSET)) {
            prop.store(fw, null);
            LOG.info("Saved last used data file: {}", getLastUsedDataFile().getName());
        } catch (IOException e) {
            LOG.error(EXCEPTION_THROWN, e.getMessage(), e);
        }
    }

    private void getLastFeedFile() {
        Properties prop = new Properties();
        String lastFeedFilePropertiesPath = FileHelper.getHome().getAbsolutePath() + File.separator +
                FileHelper.cleanPathToCreateFileName(configuration.getConfigFile().getAbsolutePath()) +
                PROPERTIES_FILE_EXTENSION;
        File lastFeedPropertiesFile = new File(lastFeedFilePropertiesPath);
        if (lastFeedPropertiesFile.exists()) {
            try (Reader fr = new InputStreamReader(
                    new FileInputStream(lastFeedPropertiesFile), Configuration.DEFAULT_CHARSET)) {
                prop.load(fr);
            } catch (IOException e) {
                // only on DEBUG because it is not a problem if this file does not exist
                LOG.debug(EXCEPTION_THROWN, e.getMessage(), e);
            }
            String lastFeedFileName = prop.getProperty(LAST_FEED_FILE);
            if (lastFeedFileName == null) {
                return;
            }
            File lastFeedFile = new File(lastFeedFileName);
            if (lastFeedFile.canRead()) {
                setLastUsedDataFile(lastFeedFile);
            } else {
                setLastUsedDataFile(null);
            }
        } else {
            LOG.debug(String.format("Last feed file properties not found: %s", lastFeedFilePropertiesPath));
        }
    }

    private static synchronized File getLastUsedDataFile() {
        return LAST_USED_DATA_FILE;
    }

    private static synchronized void setLastUsedDataFile(File lastUsedDataFile) {
        ScheduledFeedingTask.LAST_USED_DATA_FILE = lastUsedDataFile;
    }


}
