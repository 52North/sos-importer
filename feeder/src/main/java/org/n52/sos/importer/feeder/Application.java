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
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Application class</p>
 *
 * The starting point of the feeder module. It checks the command line parameters and starts the according Feeder types.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public final class Application {

    private static final String NEW_LINE_WITH_TABS = "\n\t\t";

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private static final String[] ALLOWED_PARAMETERS = { "-c", "-d", "-p", "-m" };

    /**
     * Method to start the application using various commandline parameters.
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    // Fix for uncommented main method error of checkstyle because I need this entry point
    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        LOG.trace("main()");
        logApplicationMetadata();
        if (checkArgs(args)) {
            // read configuration
            String configFile = args[1];
            try {
                if (isConfigFileSet(args[0])) {
                    Configuration configuration = new Configuration(configFile);
                    // start application with valid configuration
                    // data file
                    if (args.length == 2) {
                        // Case: one time feeding with defined configuration
                        new FeedingTask(configuration.enableOneTimeFeeding()).feedData();
                    } else if (args.length == 4) {
                        // Case: one time feeding with file override or period with file from configuration
                        if (isFileOverride(args[2])) {
                            // Case: file override
                            new FeedingTask(configuration, new File(args[3])).feedData();
                        } else if (isTimePeriodSet(args[2]))  {
                            // Case: repeated feeding
                            repeatedFeeding(configuration, Integer.parseInt(args[3]));
                        }
                    } else if (args.length == 6) {
                        // Case: repeated feeding with file override
                        repeatedFeeding(configuration, new File(args[3]), Integer.parseInt(args[5]));
                    }
                } else {
                    if (args.length == 4 && shouldRunMultiFeederTask(args[0], args[1])) {
                        // Case: repeated feeding of files in pool of threads
                        MultiFeederTask mft = new MultiFeederTask(args[1],
                                Integer.parseInt(args[2]),
                                Integer.parseInt(args[3]));
                        mft.startFeeding();
                    }
                }
            } catch (XmlException e) {
                String errorMsg =
                        String.format("Configuration file '%s' could not be " +
                                "parsed. Exception thrown: %s",
                                configFile,
                                e.getMessage());
                LOG.error(errorMsg);
                LOG.debug("", e);
            } catch (IOException e) {
                LOG.error("Exception thrown: {}", e.getMessage());
                LOG.debug("", e);
            } catch (NumberFormatException iae) {
                LOG.error("Given parameters could not be parsed! -p must be a number.");
                LOG.debug("Exception Stack Trace:", iae);
            }
        } else {
            showUsage();
        }
    }

    private static boolean shouldRunMultiFeederTask(String parameter1, String parameter2) {
        return isMultiFeederTask(parameter1) &&
                isValidDirectory(parameter2);
    }

    private static boolean isValidDirectory(String parameter2) {
        File f = new File(parameter2);
        return f.exists() && f.isDirectory() && f.canRead();
    }

    private static void repeatedFeeding(final Configuration c, final File f, final int periodInMinutes) {
        new Timer("FeederTimer").
                schedule(new ScheduledFeedingTask(c, f, periodInMinutes), 1, periodInMinutes * 1000 * 60L);
    }

    private static void repeatedFeeding(final Configuration c, final int periodInMinutes) {
        repeatedFeeding(c, c.getDataFile(), periodInMinutes);
    }

    /*
     * Prints the usage test to the Standard-Output.
     * if number of arguments increase --> use JOpt Simple: http://pholser.github.com/jopt-simple/
     */
    private static void showUsage() {
        LOG.trace("showUsage()");
        System.out.println(new StringBuffer("usage: java -jar Feeder.jar [-c file [-d datafile] [-p period]]|")
                .append("-m directory period threads\n")
                .append("options and arguments:\n")
                .append("-c file     : read the config file and start the import process\n")
                .append("-d datafile : OPTIONAL override of the datafile defined in config file\n")
                .append("-p period   : OPTIONAL time period in minutes for repeated feeding\n")
                .append("-m directory period threads:\n")
                .append("            'directory' path containing configuration XML files that\n")
                .append("            are every 'period' of minutes submitted as FeedingTasks\n")
                .append("            into a ThreadPool of size 'threads'. A period < 1 results\n")
                .append("            in a ontime run of the multi feeder. The XML files MUST\n")
                .append("            end with '-config.xml'.\n")
                .toString());
    }

    /**
     * This method validates the input parameters from the user. If something
     * wrong, it will be logged.
     *
     * @param args the parameters given by the user
     *
     * @return <b>true</b> if the parameters are valid and the programm has all
     *              required information.<br>
     *          <b>false</b> if parameters are missing or not usable in the
     *              specified form.
     */
    private static boolean checkArgs(String[] args) {
        LOG.trace("checkArgs({})", Arrays.toString(args));
        if (args == null) {
            LOG.error("no parameters defined. null received as args!");
            return false;
        } else if (args.length == 2) {
            if (isConfigFileSet(args[0])) {
                return true;
            }
        } else if (args.length == 4) {
            if (isConfigFileSet(args[0]) && (
                    isFileOverride(args[2]) ||
                    isTimePeriodSet(args[2]))) {
                return true;
            } else {
                if (isMultiFeederTask(args[0])) {
                    return true;
                }
            }
        } else if (args.length == 6) {
            if (args[0].equals(ALLOWED_PARAMETERS[0]) &&
                    isFileOverride(args[2]) &&
                    isTimePeriodSet(args[4])) {
                return true;
            }
        }
        LOG.error("Given parameters do not match programm specification. ");
        return false;
    }

    private static boolean isMultiFeederTask(String parameter) {
        return parameter.equals(ALLOWED_PARAMETERS[3]);
    }

    private static boolean isConfigFileSet(String parameter) {
        return ALLOWED_PARAMETERS[0].equals(parameter);
    }

    private static boolean isFileOverride(String parameter) {
        return parameter.equals(ALLOWED_PARAMETERS[1]);
    }

    private static boolean isTimePeriodSet(String parameter) {
        return parameter.equals(ALLOWED_PARAMETERS[2]);
    }

    /**
     * Method print all available information from the jar's manifest file.
     */
    private static void logApplicationMetadata() {
        LOG.trace("logApplicationMetadata()");
        StringBuffer logMessage = new StringBuffer("Application started");
        InputStream manifestStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
        try {
            Manifest manifest = new Manifest(manifestStream);
            Attributes attributes = manifest.getMainAttributes();
            Set<Object> keys = attributes.keySet();
            for (Object object : keys) {
                if (object instanceof Name) {
                    Name key = (Name) object;
                    logMessage.append(NEW_LINE_WITH_TABS)
                        .append(key)
                        .append(": ")
                        .append(attributes.getValue(key));
                }
            }
            // add heap information
            logMessage.append(NEW_LINE_WITH_TABS)
                .append(heapSizeInformation())
                .append(NEW_LINE_WITH_TABS)
                .append(operatingSystemInformation());
        } catch (IOException ex) {
            LOG.warn("Error while reading manifest file from application jar file: " + ex.getMessage());
        }
        LOG.info(logMessage.toString());
    }

    private static String operatingSystemInformation() {
        return String.format("os.name: %s; os.arch: %s; os.version: %s",
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("os.version"));
    }

    public static String heapSizeInformation() {
        long mb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        return String.format("HeapSize Information: max: %sMB; total now: %sMB; free now: %sMB; used now: %sMB",
                rt.maxMemory() / mb,
                rt.totalMemory() / mb,
                rt.freeMemory() / mb,
                (rt.totalMemory() - rt.freeMemory()) / mb);
    }

}
