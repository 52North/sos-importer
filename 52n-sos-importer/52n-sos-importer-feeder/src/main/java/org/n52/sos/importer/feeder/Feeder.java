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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;

/**
 * TODO check System.exit(-1)
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
		if(logger.isInfoEnabled()) {
			logApplicationMetadata();
		}
		if (checkArgs(args)) {
			// read configuration
			String configFile = args[1];
			try {
				Configuration c = new Configuration(configFile);
				// start application with valid configuration
				// data file
				DataFile dataFile = new DataFile(c);
				if (dataFile.isAvailable()) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Data file can be accessed: %s", dataFile.toString()));
					}
					// check SOS
					URL sosURL = c.getSosUrl();
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("SOS URL: \"%s\"", 
								sosURL.toExternalForm()));
					}
					SensorObservationService sos = new SensorObservationService(sosURL);
					if (!sos.isAvailable()) {
						logger.fatal(String.format("SOS \"%s\" is not available. Please check the configuration!", sosURL));
					} else if (!sos.isTransactional()){
						logger.fatal(String.format("SOS \"%s\" does not support required operations \"InsertObservation\" & \"RegisterSensor\"!", sosURL));
					} else {
						// SOS is available and transactional
						// start reading data file line by line starting from flwd
						// TODO if failed observations-> store in file
						sos.importData(dataFile);
					}

					// TODO remove next four lines before release!
					boolean justImplementing = true;
					if(logger.isInfoEnabled()){
						logger.info(String.format("Are you just implementing? %s",(justImplementing?"yes":"no")));
					}
				} 
			} catch (XmlException e) {
				String errorMsg = 
						String.format("Configuration file \"%s\" could not be " +
								"parsed. Exception thrown: %s",
								configFile,
								e.getMessage());
				logger.error(errorMsg, e);
			} catch (MalformedURLException mue) {
				String errorMsg = 
						String.format("SOS URL syntax not correct in configuration file %s. Exception thrown: %s", 
								configFile,
								mue.getMessage());
				logger.error(errorMsg, mue);
			} catch (IOException e) {
				logger.error(String.format("Exception thrown: %s", e.getMessage()), e);
			} catch (OXFException e) {
				// TODO Auto-generated catch block generated on 21.06.2012 around 15:26:34
				logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
						e);
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
					logMessage = logMessage + "\n\t\t" + key + ": " + attributes.getValue(key);
				}
			}
		}
		catch(IOException ex) {
			logger.warn("Error while reading manifest file from application jar file: " + ex.getMessage());
		}
		logger.info(logMessage);
	}

}
