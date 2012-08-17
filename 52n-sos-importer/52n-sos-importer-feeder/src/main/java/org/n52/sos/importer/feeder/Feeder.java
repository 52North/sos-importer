/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.feeder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.task.OneTimeFeeder;
import org.n52.sos.importer.feeder.task.RepeatedFeeder;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class Feeder {

	private static final Logger logger = Logger.getLogger(Feeder.class);

	private static final String[] ALLOWED_PARAMETERS = { "-c", "-d", "-p"};

	public static void main(String[] args) {
		if (logger.isTraceEnabled()) {
			logger.trace("main()");
		}
		if(logger.isDebugEnabled()) {
			logApplicationMetadata();
		}
		if (checkArgs(args)) {
			// read configuration
			String configFile = args[1];
			try {
				Configuration c = new Configuration(configFile);
				// start application with valid configuration
				// data file
				if (args.length == 2) { // Case: one time feeding with defined configuration
					new Thread(new OneTimeFeeder(c),OneTimeFeeder.class.getCanonicalName()).start();
					
				} else if (args.length == 4) { // Case: one time feeding with file override or period with file from configuration
					if (args[2].equals(ALLOWED_PARAMETERS[1])) { // Case: file override
						new Thread(new OneTimeFeeder(c,new File(args[3])),OneTimeFeeder.class.getCanonicalName()).start();
						
					} else if (args[2].equals(ALLOWED_PARAMETERS[2])) { // Case: repeated feeding
						repeatedFeeding(c,Integer.parseInt(args[3]));
					}
				} else if (args.length == 6) { // Case: repeated feeding with file override
					repeatedFeeding(c,new File(args[3]),Integer.parseInt(args[5]));
				}
			} catch (XmlException e) {
				String errorMsg = 
						String.format("Configuration file \"%s\" could not be " +
								"parsed. Exception thrown: %s",
								configFile,
								e.getMessage());
				logger.fatal(errorMsg);
				if (logger.isDebugEnabled()) {
					logger.debug("", e);
				}
			} catch (IOException e) {
				logger.fatal(String.format("Exception thrown: %s", e.getMessage()));
				if (logger.isDebugEnabled()) {
					logger.debug("", e);
				}
			} catch (IllegalArgumentException iae) {
				logger.fatal("Given parameters could not be parsed! -p must be a number.");
				if (logger.isDebugEnabled()) {
					logger.debug("Exception Stack Trace:",iae);
				}
			}
		} else {
			showUsage();
		}
	}

	private static void repeatedFeeding(Configuration c, File f, int periodInMinutes) {
		Timer t = new Timer("FeederTimer");
		t.schedule(new RepeatedFeeder(c,f), 1, periodInMinutes*1000*60);
	}

	private static void repeatedFeeding(Configuration c, int periodInMinutes) {
		repeatedFeeding(c,c.getDataFile(),periodInMinutes);
	}

	/**
	 * Prints the usage test to the Standard-Output.
	 * TODO if number of arguments increase --> use JOpt Simple: http://pholser.github.com/jopt-simple/
	 */
	private static void showUsage() {
		if (logger.isTraceEnabled()) {
			logger.trace("showUsage()");
		}
		logger.info("\nusage: java -jar Feeder.jar -c file [-d datafile] [-p period]\n" +
				"options and arguments:\n" + 
				"-c file	 : read the config file and start the import process\n" +
				"-d datafile : OPTIONAL override of the datafile defined in config file\n" +
				"-p period   : OPTIONAL time period in minutes for repeated feeding");
	}

	/**
	 * This method validates the input parameters from the user. If something 
	 * wrong, it will be logged.
	 * @param args the parameters given by the user
	 * @return <b>true</b> if the parameters are valid and the programm has all
	 * 				required information.<br />
	 * 			<b>false</b> if parameters are missing or not usable in the 
	 * 				specified form.
	 */
	private static boolean checkArgs(String[] args) {
		if (logger.isTraceEnabled()) {
			logger.trace("checkArgs()");
		}
		if (args == null) {
			logger.fatal("no parameters defined. null received as args!");
			return false;
		} else if (args.length == 2) {
			if (ALLOWED_PARAMETERS[0].equals(args[0])) {
				return true;
			}
		} else if (args.length == 4) {
			if (ALLOWED_PARAMETERS[0].equals(args[0]) && (
					args[2].equals(ALLOWED_PARAMETERS[1]) || 
					args[2].equals(ALLOWED_PARAMETERS[2]) ) ) {
				return true;
			}
		} else if (args.length == 6) {
			if (args[0].equals(ALLOWED_PARAMETERS[0]) &&
					args[2].equals(ALLOWED_PARAMETERS[1]) &&
					args[4].equals(ALLOWED_PARAMETERS[2])) {
				return true;
			}
		}
		logger.fatal("Given parameters do not match programm specification. ");
		return false;
	}

	/**
	 * Method print all available information from the jar's manifest file. 
	 */
	private static void logApplicationMetadata() {
		if (logger.isTraceEnabled()) {
			logger.trace("logApplicationMetadata()");
		}
		InputStream manifestStream;
		String logMessage;
		//
		logMessage = "Application started";
		manifestStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
		try {
			Manifest manifest = new Manifest(manifestStream);
			Attributes attributes = manifest.getMainAttributes();
			Set<Object> keys = attributes.keySet();
			for (Iterator<Object> iterator = keys.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if (object instanceof Name) {
					Name key = (Name) object;
					logMessage += String.format("\n\t\t%s: %s",key,attributes.getValue(key));
				}
			}
			// add heap information
			logMessage += "\n\t\t" + heapSizeInformation();
		}
		catch(IOException ex) {
			logger.warn("Error while reading manifest file from application jar file: " + ex.getMessage());
		}
		logger.info(logMessage);
	}

	protected static String heapSizeInformation() {
		long mb = 1024 * 1024;
		Runtime rt = Runtime.getRuntime();
		long maxMemoryMB = rt.maxMemory() / mb;
		long totalMemoryMB = rt.totalMemory() / mb;
		long freeMemoryMB = rt.freeMemory() / mb;
		long usedMemoryMB = (rt.totalMemory() - rt.freeMemory()) / mb;
		return String.format("HeapSize Information: max: %sMB; total now: %sMB; free now: %sMB; used now: %sMB",
				maxMemoryMB,
				totalMemoryMB,
				freeMemoryMB,
				usedMemoryMB);
	}

}
