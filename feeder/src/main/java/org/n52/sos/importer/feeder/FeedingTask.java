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
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.util.FileHelper;
import org.n52.sos.importer.feeder.util.HTTPClient;
import org.n52.sos.importer.feeder.util.FtpClient;
import org.n52.sos.importer.feeder.util.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>FeedingTask class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
/*
 * TODO if failed observations -&gt; store in file
 */
public class FeedingTask {

    private static final String EXCEPTION_STACK_TRACE = "Exception Stack Trace:";

    private static final Logger LOG = LoggerFactory.getLogger(FeedingTask.class);

    private static final Map<String, Lock> FEEDER_LOCKS = new ConcurrentSkipListMap<>();

    private final Configuration config;

    private WebClient webClient;

    private DataFile dataFile;

    public FeedingTask(final Configuration config) {
        this.config = config;
    }

    public FeedingTask(final Configuration config, final File datafile) {
        this(config);
        dataFile = new DataFile(config, datafile);
    }

    private DataFile downloadRemoteFile() {
        // get remoteUrl from configFile
        URL fileUrl = null;
        try {
            fileUrl = new URL(config.getRemoteFileURL());
        } catch (MalformedURLException e) {
            LOG.error("Remote File URL is not valid '{}'", config.getRemoteFileURL());
            e.printStackTrace();
        }
        if (fileUrl != null) {
            switch (fileUrl.getProtocol()) {
                case"ftp":
                    webClient = new FtpClient(config);
                    break;
                case "http":
                case "https":
                    webClient = new HTTPClient(config);
                    break;
                default:
                    throw new IllegalArgumentException("Protocol not supported: " + fileUrl.getProtocol());
            }
            return webClient.download();
        } else {
            return null;
        }
    }

    public void feedData() {
        Lock lock = getLock();
        try {
            lock.lock();

            LOG.trace("startFeeding()");
            LOG.info("Starting feeding data via configuration '{}' to SOS instance.", config.getFileName());
            // local or remote
            if (config.isRemoteFile()) {
                dataFile = downloadRemoteFile();
            } else if (dataFile == null) {
                dataFile = new DataFile(config, config.getDataFile());
            }
            if (dataFile == null) {
                LOG.error("No datafile was found!");
                // no data -> nothing to do
                return;
            }
            LOG.info("Datafile: '{}'.", dataFile.getAbsolutePath());
            if (!dataFile.isAvailable()) {
                LOG.error("Datafile is not available. Cancel feeding!");
                return;
            }
            try {
                // check SOS
                Feeder feeder = null;
                String sosURL = config.getSosUrl().toString();
                feeder = new Feeder(config);
                if (feeder == null || !feeder.isSosAvailable()) {
                    LOG.error(String.format("SOS '%s' is not available. Please check the configuration!", sosURL));
                } else if (!feeder.isSosTransactional()) {
                    LOG.error(String.format("SOS '%s' does not support required transactional operations: "
                            + "InsertSensor, InsertObservation. Please enable.",
                            sosURL));
                } else {
                    File timeStampFile = null;
                    File counterFile = null;
                    if (config.isUseLastTimestamp()) {
                        timeStampFile = FileHelper.createFileInImporterHomeWithUniqueFileName(
                                generateFileNameWithPostfix(Configuration.TIMESTAMP_FILE_POSTFIX));
                        LOG.debug("Check last timestamp file '{}'.", timeStampFile.getCanonicalPath());
                        if (timeStampFile.exists()) {
                            // read already inserted UsedLastTimeStamp
                            LOG.debug("Read already inserted LastUsedTimeStamp from file.");
                            try (Scanner sc = new Scanner(timeStampFile, Configuration.DEFAULT_CHARSET)) {
                                String storedTimeStamp = sc.next();
                                Timestamp tmp = new Timestamp(storedTimeStamp);
                                feeder.setLastUsedTimestamp(tmp);
                            }
                        } else {
                            LOG.debug("Timestamp file does not exist.");
                        }
                    } else {
                        counterFile = FileHelper.createFileInImporterHomeWithUniqueFileName(
                                generateFileNameWithPostfix(Configuration.COUNTER_FILE_POSTFIX));
                        LOG.debug("Check counter file '{}'.", counterFile.getCanonicalPath());
                        // read already inserted line count
                        if (counterFile.exists()) {
                            LOG.debug("Read already read lines from file");
                            try (Scanner sc = new Scanner(counterFile, Configuration.DEFAULT_CHARSET)) {
                                feeder.setLastReadLine(sc.nextInt());
                            }
                        } else {
                            LOG.debug("Counter file does not exist.");
                        }
                    }

                    // SOS is available and transactional
                    feeder.importData(dataFile);

                    // read and log lastUsedTimestamp
                    if (config.isUseLastTimestamp() && timeStampFile != null) {
                        Timestamp timestamp = feeder.getLastUsedTimestamp();
                        LOG.info("OneTimeFeeder: save read lastUsedTimestamp: '{}' to '{}'",
                                timestamp,
                                timeStampFile.getCanonicalPath());
                        // override lastUsedTimestamp file
                        try (
                                FileWriterWithEncoding timeStampFileWriter = new FileWriterWithEncoding(
                                        timeStampFile.getAbsolutePath(),
                                        Configuration.DEFAULT_CHARSET);
                                PrintWriter out = new PrintWriter(timeStampFileWriter);) {
                            out.println(timestamp.toISO8601String());
                        }
                    } else {
                        int lastLine = feeder.getLastReadLine();
                        LOG.info("OneTimeFeeder: save read lines count: '{}' to '{}'",
                                lastLine,
                                counterFile.getCanonicalPath());
                        /*
                         * Hack for UoL EPC instrument files
                         * The EPC instrument produces data files with empty lines at the end.
                         * When a new sample is appended, this empty line is removed, hence
                         * the line counter needs to be decremented.
                         */
                        if (config.getFileName().contains("EPC_import-config.xml") && isLinuxOrSimilar()) {
                            lastLine = lastLine - 1;
                            LOG.info("Decrement lastLine counter: {}", lastLine);
                        }
                        // override counter file
                        try (
                                FileWriterWithEncoding counterFileWriter = new FileWriterWithEncoding(
                                        counterFile.getAbsoluteFile(),
                                        Configuration.DEFAULT_CHARSET);
                                PrintWriter out = new PrintWriter(counterFileWriter);) {
                            out.println(lastLine);
                        }
                    }

                    if (config.isRemoteFile()) {
                        webClient.deleteDownloadedFile();
                    }
                    LOG.info("Feeding data from file {} to SOS instance finished.", dataFile.getFileName());

                }
            } catch (final MalformedURLException mue) {
                LOG.error("SOS URL syntax not correct in configuration file '{}'. Exception thrown: {}",
                        config.getFileName(),
                        mue.getMessage());
                LOG.debug(EXCEPTION_STACK_TRACE, mue);
            } catch (final IOException |  XmlException | ParseException | IllegalArgumentException e) {
                log(e);
            }
        } finally {
            lock.unlock();
        }
    }

    private synchronized Lock getLock() {
        String key = FileHelper.shortenStringViaMD5Hash(config.getAbsolutePath());
        if (!FEEDER_LOCKS.containsKey(key)) {
            FEEDER_LOCKS.put(key, new ReentrantLock(true));
        }
        return  FEEDER_LOCKS.get(key);
    }

    private String generateFileNameWithPostfix(String postfix) throws IOException {
        String fileName = dataFile.getFileName() + postfix;
        if (!config.isRemoteFile()) {
            fileName = getLocalFilename(postfix);
        }
        return fileName;
    }

    private String getLocalFilename(String postfix) throws IOException {
        return config.getConfigFile().getCanonicalPath() +
                "_" +
                dataFile.getCanonicalPath() +
                postfix;
    }

    private boolean isLinuxOrSimilar() {
        final String osName = System.getProperty("os.name").toLowerCase();
        return osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0;
    }

    private void log(final Exception e) {
        LOG.error("Exception thrown: {}", e.getMessage());
        LOG.debug(EXCEPTION_STACK_TRACE, e);
    }

}
