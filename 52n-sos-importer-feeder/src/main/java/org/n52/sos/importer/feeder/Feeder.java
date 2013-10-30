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

import static java.lang.Integer.parseInt;

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
import org.n52.sos.importer.feeder.task.OneTimeFeeder;
import org.n52.sos.importer.feeder.task.RepeatedFeeder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class Feeder {

	private static final Logger LOG = LoggerFactory.getLogger(Feeder.class);

	private static final String[] ALLOWED_PARAMETERS = { "-c", "-d", "-p"};

	public static void main(final String[] args) {
		LOG.trace("main()");
		logApplicationMetadata();
		if (checkArgs(args)) {
			// read configuration
			final String configFile = args[1];
			try {
				final Configuration c = new Configuration(configFile);
				// start application with valid configuration
				// data file
				if (args.length == 2) { 
					// Case: one time feeding with defined configuration
					new Thread(new OneTimeFeeder(c),OneTimeFeeder.class.getSimpleName()).start();
				} 
				else if (args.length == 4) { 
					// Case: one time feeding with file override or period with file from configuration
					if (isFileOverride(args[2])) { 
						// Case: file override
						new Thread(new OneTimeFeeder(c,new File(args[3])),OneTimeFeeder.class.getCanonicalName()).start();
						
					} 
					else if (isTimePeriodSet(args[2]))	{
						// Case: repeated feeding
						repeatedFeeding(c,parseInt(args[3]));
					}
				} 
				else if (args.length == 6) { 
					// Case: repeated feeding with file override
					repeatedFeeding(c,new File(args[3]),parseInt(args[5]));
				}
			} 
			catch (final XmlException e)
			{
				final String errorMsg = 
						String.format("Configuration file '%s' could not be " +
								"parsed. Exception thrown: %s",
								configFile,
								e.getMessage());
				LOG.error(errorMsg);
				LOG.debug("", e);
			} 
			catch (final IOException e) 
			{
				LOG.error("Exception thrown: {}", e.getMessage());
				LOG.debug("", e);
			}
			catch (final IllegalArgumentException iae)
			{
				LOG.error("Given parameters could not be parsed! -p must be a number.");
				LOG.debug("Exception Stack Trace:",iae);
			}
		} 
		else {
			showUsage();
		}
	}

	private static void repeatedFeeding(final Configuration c, final File f, final int periodInMinutes) {
		final Timer t = new Timer("FeederTimer");
		t.schedule(new RepeatedFeeder(c,f,periodInMinutes), 1, periodInMinutes*1000*60);
	}

	private static void repeatedFeeding(final Configuration c, final int periodInMinutes) {
		repeatedFeeding(c,c.getDataFile(),periodInMinutes);
	}

	/**
	 * Prints the usage test to the Standard-Output.
	 * TODO if number of arguments increase --> use JOpt Simple: http://pholser.github.com/jopt-simple/
	 */
	private static void showUsage() {
		LOG.trace("showUsage()");
		LOG.info("\nusage: java -jar Feeder.jar -c file [-d datafile] [-p period]\n" +
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
	private static boolean checkArgs(final String[] args) {
		LOG.trace("checkArgs({})",Arrays.toString(args));
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
					isTimePeriodSet(args[2]) ) ) {
				return true;
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

	private static boolean isConfigFileSet(final String parameter)
	{
		return ALLOWED_PARAMETERS[0].equals(parameter);
	}

	private static boolean isFileOverride(final String parameter)
	{
		return parameter.equals(ALLOWED_PARAMETERS[1]);
	}

	private static boolean isTimePeriodSet(final String parameter)
	{
		return parameter.equals(ALLOWED_PARAMETERS[2]);
	}

	/**
	 * Method print all available information from the jar's manifest file. 
	 */
	private static void logApplicationMetadata() {
		LOG.trace("logApplicationMetadata()");
		InputStream manifestStream;
		String logMessage;
		//
		logMessage = "Application started";
		manifestStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
		try {
			final Manifest manifest = new Manifest(manifestStream);
			final Attributes attributes = manifest.getMainAttributes();
			final Set<Object> keys = attributes.keySet();
			for (final Object object : keys) {
				if (object instanceof Name) {
					final Name key = (Name) object;
					logMessage += String.format("\n\t\t%s: %s",key,attributes.getValue(key));
				}
			}
			// add heap information
			logMessage += "\n\t\t" + heapSizeInformation();
		}
		catch(final IOException ex) {
			LOG.warn("Error while reading manifest file from application jar file: " + ex.getMessage());
		}
		LOG.info(logMessage);
	}

	protected static String heapSizeInformation() {
		final long mb = 1024 * 1024;
		final Runtime rt = Runtime.getRuntime();
		final long maxMemoryMB = rt.maxMemory() / mb;
		final long totalMemoryMB = rt.totalMemory() / mb;
		final long freeMemoryMB = rt.freeMemory() / mb;
		final long usedMemoryMB = (rt.totalMemory() - rt.freeMemory()) / mb;
		return String.format("HeapSize Information: max: %sMB; total now: %sMB; free now: %sMB; used now: %sMB",
				maxMemoryMB,
				totalMemoryMB,
				freeMemoryMB,
				usedMemoryMB);
	}

}
