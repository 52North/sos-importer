package org.n52.sos.importer.controller;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Configures a log4j console logger and a log4j file logger.
 * @author Raimund
 */
public class LoggingController {

	private static final Logger logger = Logger.getLogger(LoggingController.class);
	
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
	}

	public static LoggingController getInstance() {
		if (instance == null)
			instance = new LoggingController();
		return instance;
	}
	
	
	public void initialize() {
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
	}
	
	public FileAppender getFileAppender() {
		return fileAppender;
	}
}
