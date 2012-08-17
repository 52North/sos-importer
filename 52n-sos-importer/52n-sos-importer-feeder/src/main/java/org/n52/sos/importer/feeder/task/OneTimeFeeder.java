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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
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
		this(config,config.getDataFile());
	}
	
	public OneTimeFeeder(Configuration config, File datafile) {
		this.config = config;
		this.dataFile = new DataFile(config, datafile);
	}

	@Override
	public void run() {
		if (logger.isTraceEnabled()) {
			logger.trace("run()");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Starting feeding data from file to SOS instance");
		}
		if (dataFile.isAvailable()) {
			try {
			// check SOS
			URL sosURL = config.getSosUrl();
			SensorObservationService sos = new SensorObservationService(sosURL);
			if (!sos.isAvailable()) {
				logger.fatal(String.format("SOS \"%s\" is not available. Please check the configuration!", sosURL));
			} else if (!sos.isTransactional()){
				logger.fatal(String.format("SOS \"%s\" does not support required operations \"InsertObservation\" & \"RegisterSensor\"!", sosURL));
			} else {
				// SOS is available and transactional
				// start reading data file line by line starting from flwd
				ArrayList<InsertObservation> failedInserts = sos.importData(dataFile);
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
