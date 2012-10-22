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
package org.n52.sos.importer.feeder.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.n52.sos.importer.feeder.SensorObservationService;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;

/**
 * TODO if failed observations-> store in file
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class OneTimeFeeder implements Runnable {

	private Configuration config;

	private DataFile dataFile;
	
	private static final Logger logger = Logger.getLogger(OneTimeFeeder.class);

	public OneTimeFeeder(Configuration config) {
		this.config = config;
	}
	
	public OneTimeFeeder(Configuration config, File datafile) {
		this.config = config;
		this.dataFile = new DataFile(config, datafile);
	}

	private DataFile getRemoteFile(Configuration config) {
		File dataFile = null;

		// ftp client
		FTPClient client;

		// proxy
		String pHost = System.getProperty("proxyHost", "proxy");
		int pPort = -1;
		if (System.getProperty("proxyPort") != null) {
			pPort = Integer.parseInt(System.getProperty("proxyPort"));
		}
		String pUser = System.getProperty("http.proxyUser");
		String pPassword = System.getProperty("http.proxyPassword");
		if (pHost != null && pPort != -1) {
			if (pUser != null && pPassword != null) {
				client = new FTPHTTPClient(pHost, pPort, pUser, pPassword);
			}
			client = new FTPHTTPClient(pHost, pPort);
		} else {
			client = new FTPClient();
		}

		// get first file
		String directory = config.getConfigFile().getAbsolutePath();
		dataFile = createTempFile(directory + ".csv");

		// if back button was used: delete old file
		if (dataFile.exists()) {
			dataFile.delete();
		}

		try {
			client.connect(config.getFtpHost());
			boolean login = client.login(config.getUser(), config.getPassword());
			if (login) {
				// download file
				int result = client.cwd(config.getFtpSubdirectory());
				if (result == 250) { // successfully connected
					FileOutputStream fos = new FileOutputStream(dataFile);
					client.retrieveFile(config.getFtpFile(), fos);
					fos.flush();
					fos.close();
				}
				boolean logout = client.logout();
				if (!logout) {
					logger.info("Step1Controller: ftp - cannot logout!");
				}
			} else {
				logger.info("Step1Controller: ftp - cannot login!");
			}

		} catch (SocketException e) {
			logger.fatal("The file you specified cannot be obtained.");
			return null;
		} catch (IOException e) {
			logger.fatal("The file you specified cannot be obtained.");
			return null;
		}
		
		return new DataFile(config, dataFile);
	}
	
	@Override
	public void run() {
		if (logger.isTraceEnabled()) {
			logger.trace("run()");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Starting feeding data from file to SOS instance");
		}
		// csv / ftp
		if (config.isRemoteFile()) {
			dataFile = getRemoteFile(config);
		} else {
			dataFile = new DataFile(config, config.getDataFile());
		}
		if (dataFile == null) {
			logger.fatal("No datafile was found!");
		}
		if (dataFile.isAvailable()) {
			try {
			// check SOS
			URL sosURL = config.getSosUrl();
			SensorObservationService sos = null;
				try {
					sos = new SensorObservationService(sosURL);
				} catch (ExceptionReport er) {
					logger.fatal("SOS " + sosURL + " is not available. Please check the configuration!", er);
				} catch (OXFException oxfe) {
					logger.fatal("SOS " + sosURL + " is not available. Please check the configuration!", oxfe);
				}
			if (sos == null || !sos.isAvailable()) {
				logger.fatal(String.format("SOS \"%s\" is not available. Please check the configuration!", sosURL));
			} else if (!sos.isTransactional()){
				logger.fatal(String.format("SOS \"%s\" does not support required operations \"InsertObservation\" & \"RegisterSensor\"!", sosURL));
			} else {
				String directory = dataFile.getFileName();
				File counterFile = null;
				if (config.isRemoteFile()) {
					counterFile = createTempFile(directory + "_counter");
				} else {
					counterFile = createTempFile(config.getConfigFile().getAbsolutePath()
							 + "_counter");
				}

				// read already inserted line count
				if (counterFile.exists()) {
					Scanner sc = new Scanner(counterFile);
					int count = sc.nextInt();
					sos.setLastLine(count);
				}
				
				// SOS is available and transactional
				// start reading data file line by line starting from flwd
				ArrayList<InsertObservation> failedInserts = sos.importData(dataFile);
				
				// override counter file
				FileWriter counterFileWriter = new FileWriter(counterFile.getAbsoluteFile());
				PrintWriter out = new PrintWriter(counterFileWriter);
				out.println(sos.getLastLine());
				out.close();
				
				saveFailedInsertObservations(failedInserts);
			}
			} catch (MalformedURLException mue) {
				String errorMsg = 
						String.format("SOS URL syntax not correct in configuration file %s. Exception thrown: %s", 
								config.getFileName(),
								mue.getMessage());
				logger.error(errorMsg);
				if (logger.isDebugEnabled()) {
					logger.debug("Exception Stack Trace:", mue);
				}
			} catch (IOException e) {
				logger.error(String.format("Exception thrown: %s",
						e.getMessage()));
				if (logger.isDebugEnabled()) {
					logger.debug("Exception Stack Trace:",e);
				}
			} catch (OXFException e) {
				logger.error(String.format("Exception thrown: %s",
						e.getMessage()));
				if (logger.isDebugEnabled()) {
					logger.debug("Exception Stack Trace:",e);
				}
			} catch (XmlException e) {
				logger.error(String.format("Exception thrown: %s",
						e.getMessage()));
				if (logger.isDebugEnabled()) {
					logger.debug("Exception Stack Trace:",e);
				}
			} 
		}
		if (logger.isInfoEnabled()) {
			logger.info("Feeding data to SOS instance finished.");
		}
	}
	
	/**
	 * Creates a unique filename for the given path.
	 * 
	 * @return temporary file
	 */
	private File createTempFile(String fileName) {
		String baseDir = System.getProperty("user.home") + File.separator
				+ ".SOSImporter" + File.separator;
		if (!new File(baseDir).exists()) {
			new File(baseDir).mkdir();
		}
		String tempFile = fileName.replace(":", "").replace(File.separatorChar, '_');
		return new File(baseDir + tempFile);
	}
	
	/*
	 * Method should store failed insertObservations in a defined directory and
	 * created configuration for this.
	 */
	private static void saveFailedInsertObservations(
			ArrayList<InsertObservation> failedInserts) {
		// TODO Auto-generated method stub generated on 25.06.2012 around 11:39:44 by eike
		if (logger.isTraceEnabled()) {
			logger.trace("saveFailedInsertObservations()");
		}
	}

}
