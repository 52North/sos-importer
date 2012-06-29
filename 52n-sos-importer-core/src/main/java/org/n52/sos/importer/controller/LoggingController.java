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
package org.n52.sos.importer.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Configures a log4j console logger and a log4j file logger.
 * @author Raimund
 */
public class LoggingController {

	private static final Logger logger = Logger.getLogger(LoggingController.class);
	
	// TODO read value from pom or properties file
	private static final Level LOGGING_LEVEL = Level.INFO;
	
	private static final PatternLayout CONSOLE_LOGGING_PATTERN = 
			new PatternLayout("%-1p (%c{1}.java:%L) - %m %n");
	
	private static final PatternLayout FILE_LOGGING_PATTERN = 
			new PatternLayout("%d %-1p (%c{1}.java:%L) - %m %n");
	
	private static final String DATE_LOGGING_PATTERN = "'.'yyyy-MM-dd";
	
	private static final String LOG_FILE_FOLDER = System.getProperty("user.home") 
			+ File.separator + ".SOSImporter" + File.separator + "logs" + File.separator;
	
	private static final String LOG_FILE_NAME = "52n-sensorweb-sos-importer.log";
	
	private DailyRollingFileAppender fileAppender;
	
	private static LoggingController instance = null;
	
	private LoggingController() {
		// try to load config file from jar else this version
		URL log4jURL = LoggingController.class.getResource("/org/n52/sos/importer/log4j.xml");
		DOMConfigurator.configure(log4jURL);
		if(log4jURL == null) {
			//System.setProperty("log4j.defaultInitOverride", "true");
			Logger root = Logger.getRootLogger();
			root.setLevel(LOGGING_LEVEL);
			root.addAppender(new ConsoleAppender(CONSOLE_LOGGING_PATTERN));

			String filename = LOG_FILE_FOLDER + LOG_FILE_NAME;
			try {
				fileAppender = new DailyRollingFileAppender(
						FILE_LOGGING_PATTERN, filename, DATE_LOGGING_PATTERN);
				root.addAppender(fileAppender);
			} catch (IOException e) {
				logger.warn("Could not initialize file logging.", e);
			}
			logger.error("Could not read log4j config file. Using default logging settings.");
		}
	}

	public static LoggingController getInstance() {
		if (instance == null) {
			instance = new LoggingController();
			//
			// we want to log additional meta data when the application is started
			if(logger.isInfoEnabled()) {
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
		return instance;
	}
	
	
	public FileAppender getFileAppender() {
		return fileAppender;
	}
}
