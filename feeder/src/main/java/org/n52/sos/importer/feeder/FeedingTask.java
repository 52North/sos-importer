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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Scanner;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>FeedingTask class.</p>
 *
 * TODO if failed observations -&gt; store in file
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
// TODO refactor to abstract class: move getRemoteFile to FTPOneTimeFeeder
public class FeedingTask implements Runnable {

    private static final String EXCEPTION_STACK_TRACE = "Exception Stack Trace:";
    private static final String COUNTER_FILE_POSTFIX = "_counter";
    private static final String TIMESTAMP_FILE_POSTFIX = "_timestamp";
    private static final String PROXY_PORT = "proxyPort";
    private static final Logger LOG = LoggerFactory.getLogger(FeedingTask.class);

    private final Configuration config;

    private DataFile dataFile;

    /**
     * <p>Constructor for OneTimeFeeder.</p>
     *
     * @param config a {@link org.n52.sos.importer.feeder.Configuration} object.
     */
    public FeedingTask(final Configuration config) {
        this.config = config;
    }

    /**
     * <p>Constructor for OneTimeFeeder.</p>
     *
     * @param config a {@link org.n52.sos.importer.feeder.Configuration} object.
     * @param datafile a {@link java.io.File} object.
     */
    public FeedingTask(final Configuration config, final File datafile) {
        this.config = config;
        dataFile = new DataFile(config, datafile);
    }

    private DataFile getRemoteFile() {
        File file = null;

        // ftp client
        FTPClient client;

        // proxy
        final String pHost = System.getProperty("proxyHost", "proxy");
        int pPort = -1;
        if (System.getProperty(PROXY_PORT) != null) {
            pPort = Integer.parseInt(System.getProperty(PROXY_PORT));
        }
        final String pUser = System.getProperty("http.proxyUser");
        final String pPassword = System.getProperty("http.proxyPassword");
        if (pHost != null && pPort != -1) {
            LOG.info("Using proxy for FTP connection!");
            if (pUser != null && pPassword != null) {
                client = new FTPHTTPClient(pHost, pPort, pUser, pPassword);
            } else {
                client = new FTPHTTPClient(pHost, pPort);
            }
        } else {
            LOG.info("Using no proxy for FTP connection!");
            client = new FTPClient();
        }

        // get first file
        final String directory = config.getConfigFile().getAbsolutePath();
        file = FileHelper.createFileInImporterHomeWithUniqueFileName(directory + ".csv"); // nicht importerHome Verzeichnis nutzen, 
        // sondern temporäres Verzeichnis der JVM nutzen (Linux berücksichtigen: File.CreateTempFile())

        // if back button was used: delete old file
        if (file.exists()) {
            if (!file.delete()) {
                LOG.error("Could not delete file '{}'", file.getAbsolutePath());
            }
        }

        FileOutputStream fos = null;
        try {
            client.connect(config.getFtpHost());
            final boolean login = client.login(config.getUser(), config.getPassword());
            if (login) {
                LOG.info("FTP: connected...");
                // download file
                final int result = client.cwd(config.getFtpSubdirectory());
                LOG.info("FTP: go into directory...");
                // successfully connected
                if (result == 250) {
                    fos = new FileOutputStream(file);
                    LOG.info("FTP: download file...");
                    client.retrieveFile(config.getFtpFile(), fos);
                    fos.flush();
                    fos.close();
                } else {
                    LOG.info("FTP: cannot go to subdirectory!");
                }
                final boolean logout = client.logout();
                if (!logout) {
                    LOG.info("FTP: cannot logout!");
                }
            } else {
                LOG.info("FTP:  cannot login!");
            }

        } catch (final IOException e) {
            LOG.error("The file you specified cannot be obtained.");
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log(e);
                }
            }
        }

        return new DataFile(config, file);
    }

    @Override
    public void run() {
        LOG.trace("run()");
        LOG.info("Starting feeding data via configuration '{}' to SOS instance.", config.getFileName());
        // csv / ftp
        if (config.isRemoteFile()) {
            dataFile = getRemoteFile();
        } else if (dataFile == null) {
            dataFile = new DataFile(config, config.getDataFile());
        }
        if (dataFile == null) {
            LOG.error("No datafile was found!");
            // no data -> nothing to do
            return;
        }
        LOG.info("Datafile: '{}'.", dataFile.getFileName());
        if (dataFile.isAvailable()) {
            try {
                // check SOS
                Feeder feeder = null;
                final String sosURL = config.getSosUrl().toString();
                try {
                    feeder = new Feeder(config);
                } catch (final ExceptionReport | OXFException e) {
                    LOG.error("SOS " + sosURL + " is not available. Please check the configuration!", e);
                }
                if (feeder == null || !feeder.isSosAvailable()) {
                    LOG.error(String.format("SOS '%s' is not available. Please check the configuration!", sosURL));
                } else if (!feeder.isSosTransactional()) {
                    LOG.error(String.format("SOS '%s' does not support required transactional operations: "
                            + "InsertSensor, InsertObservation. Please enable.",
                            sosURL));
                } else {
                    final String directory = dataFile.getFileName();
                    File counterFile = null;
                    String fileName = null;
                    if (config.isRemoteFile()) {
                        fileName = directory + COUNTER_FILE_POSTFIX; 
                    } else {
                        fileName = getLocalFilename();
                    }
                    counterFile = FileHelper.createFileInImporterHomeWithUniqueFileName(fileName);
                    LOG.debug("Check counter file '{}'.", counterFile.getCanonicalPath());
                    // read already inserted line count
                    if (counterFile.exists()) {
                        LOG.debug("Read already read lines from file");
                        try (Scanner sc = new Scanner(counterFile, Configuration.DEFAULT_CHARSET)) {
                            final int count = sc.nextInt();
                            feeder.setLastLine(count);
                        }
                    } else {
                        LOG.debug("Counter file does not exist.");
                    }
                    File timeStampFile = null;
                    if (config.isUseLastTimestamp()) {
                        String timeStampFileName = null;
                        timeStampFile = FileHelper.createFileInImporterHomeWithUniqueFileName(timeStampFileName);
                        if (config.isRemoteFile()) {
                            timeStampFileName = getLocalTimeStampFilename();
                        } else {
                            timeStampFileName = directory + TIMESTAMP_FILE_POSTFIX; 
                        }
                        if (timeStampFile.exists()) {
                            // read already inserted UsedLastTimeStamp
                            LOG.debug("Read already inserted LastUsedTimeStamp from file '{}'.", timeStampFile.getCanonicalPath());
                            String storedTimeStamp = null;
                            try (Scanner sc = new Scanner(timeStampFile, Configuration.DEFAULT_CHARSET)) {
                                storedTimeStamp = sc.next(); // read TimeStamp as ISO08601 string
                                // get timestamp from SimpleString:
                                int year = Integer.parseInt(storedTimeStamp
                                        .substring(0,storedTimeStamp.indexOf('-'))
                                        );
                                String yearString = storedTimeStamp
                                        .substring(storedTimeStamp.indexOf('-')+1);
                                int month = Integer.parseInt(yearString
                                        .substring(0,yearString.indexOf('-'))
                                        );
                                String monthString = yearString
                                        .substring(yearString.indexOf('-')+1);
                                int day = Integer.parseInt(monthString
                                        .substring(0,monthString.indexOf('-'))
                                        );
                                String dayString = monthString
                                        .substring(monthString.indexOf('-')+1);
                                int hour = Integer.parseInt(dayString
                                        .substring(0,dayString.indexOf('-'))
                                        );
                                String hourString = dayString
                                        .substring(dayString.indexOf('-')+1);
                                int minute = Integer.parseInt(hourString
                                        .substring(0,hourString.indexOf('-'))
                                        );
                                String minuteString = hourString
                                        .substring(hourString.indexOf('-')+1);
                                int second = Integer.parseInt(minuteString
                                        .substring(0,minuteString.indexOf('-'))
                                        );
                                
                                Timestamp tmp = new Timestamp();
                                tmp.setYear(year);
                                tmp.setMonth(month);
                                tmp.setDay(day);
                                tmp.setHour(hour);
                                tmp.setMinute(minute);
                                tmp.setSeconds(second);
                                feeder.setLastUsedTimeStamp(tmp);
                            }
                        }
                    } else {
                        LOG.debug("Timestamp file does not exist.");
                    }

                    // SOS is available and transactional
                    feeder.importData(dataFile);
                    int lastLine = feeder.getLastLine();
                    LOG.info("OneTimeFeeder: save read lines count: '{}' to '{}'",
                            lastLine,
                            counterFile.getCanonicalPath());
                    
                    // read and log lastUsedTimestamp
                    if (config.isUseLastTimestamp()) {
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
                    }
                    
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

                    LOG.info("Feeding data from file {} to SOS instance finished.", dataFile.getFileName());
                }
            } catch (final MalformedURLException mue) {
                LOG.error("SOS URL syntax not correct in configuration file '{}'. Exception thrown: {}",
                        config.getFileName(),
                        mue.getMessage());
                LOG.debug(EXCEPTION_STACK_TRACE, mue);
            } catch (final IOException |  OXFException | XmlException | ParseException | IllegalArgumentException e) {
                log(e);
            }
        }
    }

    /**
     * <p>getLocalFilename.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @since 0.5.0
     */
    protected String getLocalFilename() throws IOException {
        return config.getConfigFile().getCanonicalPath() +
                "_" +
                dataFile.getCanonicalPath() +
                COUNTER_FILE_POSTFIX;
    }
    
    /**
     * <p>getLocalTimeStampFilename.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @since 0.5.0
     */
    protected String getLocalTimeStampFilename() throws IOException {
        return config.getConfigFile().getCanonicalPath() +
                "_" +
                dataFile.getCanonicalPath() +
                TIMESTAMP_FILE_POSTFIX;
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