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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.task.OneTimeFeeder;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class Feeder {

	private static final Logger logger = Logger.getLogger(Feeder.class);

	private static final String[] ALLOWED_PARAMETERS = { "-f", "--config-file"};

	/**
	 * 
	 * @param args must contain at least the following parameter: (-f|--config-file) /path/to/config/file.xml
	 */
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
					new Thread(new OneTimeFeeder(c),"OneTimeFeeder").start();
				}
			} catch (XmlException e) {
				String errorMsg = 
						String.format("Configuration file \"%s\" could not be " +
								"parsed. Exception thrown: %s",
								configFile,
								e.getMessage());
				logger.error(errorMsg);
				if (logger.isDebugEnabled()) {
					logger.debug("", e);
				}
			} catch (IOException e) {
				logger.error(String.format("Exception thrown: %s", e.getMessage()));
				if (logger.isDebugEnabled()) {
					logger.debug("", e);
				}
			}
		} else {
			showUsage();
		}
	}

	/**
	 * Prints the usage test to the Standard-Output.
	 */
	private static void showUsage() {
		if (logger.isTraceEnabled()) {
			logger.trace("showUsage()");
		}
		logger.info("\nusage: java -jar Feeder.jar (-f | --config-file) file\n" +
				"options and arguments:\n" + 
				"-f file	: read the config file and start the import process");
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
			logger.error("no parameters defined. null received as args!");
			return false;
		} else if (args.length != 2) {
			logger.error("given parameters do not match programm specification.");
			return false;
		} else {
			for (int i = 0; i < ALLOWED_PARAMETERS.length; i++) {
				if (ALLOWED_PARAMETERS[i].equalsIgnoreCase(args[0])) {
					return true;
				}
			}
			logger.error("given parameters is not supported: " + args[0]);
			return false;
		}
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
