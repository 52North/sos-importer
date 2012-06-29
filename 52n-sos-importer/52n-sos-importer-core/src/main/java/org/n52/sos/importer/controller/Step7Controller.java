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
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.utils.XMLTools;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step7Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * Lets the user choose a URL of a Sensor Observation Service (and test the 
 * connection), define the offering, and save the configuration.
 * @author Raimund
 * TODO implement offering input field
 */
public class Step7Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step7Controller.class);

	private Step7Panel s7P;

	private Step7Model s7M;

	private int firstLineWithData;

	public Step7Controller(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		this.s7M = new Step7Model();
	}

	@Override
	public void loadSettings() {
		s7P = new Step7Panel(this);
		if (this.s7M != null) {
			s7P.setSaveConfig(s7M.isSaveConfig());
			s7P.setDirectImport(s7M.isDirectImport());
			if (s7M.getSosURL() != null) {
				s7P.setSOSURL(s7M.getSosURL());
			}
			if (s7M.getConfigFile() != null) {
				s7P.setConfigFile(s7M.getConfigFile().getAbsolutePath());
			}
		}
		BackNextController.getInstance().changeFinishToNext();
	}
	
	public void back() {
		BackNextController bnc = BackNextController.getInstance();
		bnc.setNextButtonEnabled(true);
		bnc.changeFinishToNext();
	}

	@Override
	public void saveSettings() {
		String sosURL = s7P.getSOSURL(),
				offering = s7P.getOfferingName();
		boolean directImport = s7P.isDirectImport(),
				saveConfig = s7P.isSaveConfig(),
				generateOfferingFromSensorName = s7P.isGenerateOfferingFromSensorName();
		File configFile = new File(s7P.getConfigFile());
		if (this.s7M == null) {
			this.s7M = new Step7Model(sosURL,
					directImport,
					saveConfig,
					configFile,
					generateOfferingFromSensorName,
					offering);
		} else {
			s7M.setSosURL(sosURL);
			s7M.setConfigFile(configFile);
			s7M.setDirectImport(directImport);
			s7M.setSaveConfig(saveConfig);
			s7M.setGenerateOfferingFromSensorName(generateOfferingFromSensorName);
			if (!generateOfferingFromSensorName) {
				s7M.setOffering(offering);
			}
		}
	}

	@Override
	public String getDescription() {
		return Lang.l().step7Description();
	}

	@Override
	public JPanel getStepPanel() {
		return s7P;
	}

	@Override
	public StepController getNextStepController() {
		return new Step8Controller(s7M,this.firstLineWithData);
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		//get and check URI
		boolean result = true;
		// check inputs 
		if (!s7P.isGenerateOfferingFromSensorName() && 
				(s7P.getOfferingName() == null || 
				s7P.getOfferingName().equalsIgnoreCase(""))
				) {
			// user decided to give input but (s)he did NOT, so tell him
			String msg = Lang.l().step7OfferingNameNotGiven();
			JOptionPane.showMessageDialog(null,
					msg,
					Lang.l().errorDialogTitle(),
					JOptionPane.ERROR_MESSAGE);
			logger.error(msg);	
			result = result & false; // shortcut return false; ???
		} else if (!s7P.isGenerateOfferingFromSensorName() &&
				!XMLTools.isNCName(s7P.getOfferingName()) ){
			// user gave input but it is not valid
			String msg = Lang.l().step7OfferingNameNotValid(s7P.getOfferingName());
			JOptionPane.showMessageDialog(null,
					msg,
					Lang.l().errorDialogTitle(),
					JOptionPane.ERROR_MESSAGE);
			logger.error(msg);
			result = result & false;
		}
		if (s7P.isDirectImport()) {
			String url = s7P.getSOSURL();
			result = result && testConnection(url);
		}
		return result;
	}

	/*
	 * TODO Expand -> Send get capabilities request
	 *
	 * TODO add proxy handling: http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
	 */
	public boolean testConnection(String strURL) {
		if (logger.isTraceEnabled()) {
			logger.trace("testConnection()");
		}
		// show dialog to start sos connection testing
		BackNextController.getInstance().setNextButtonEnabled(false);
		int userChoice = JOptionPane.showConfirmDialog(s7P,
				Lang.l().step7SOSConncetionStart(strURL),
				Lang.l().infoDialogTitle(),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (userChoice == JOptionPane.YES_OPTION) {
			try {
				// send GetCapabilities via GET-Method
				strURL = strURL + Constants.SOS_GET_GET_CAPABILITIES_REQUEST;
				URL url = new URL(strURL);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(Constants.URL_CONNECT_TIMEOUT_SECONDS*1000);
				urlConn.setReadTimeout(Constants.URL_CONNECT_READ_TIMEOUT_SECONDS*1000);
				urlConn.connect();

				if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
					logger.info("Successfully tested connection to Sensor Observation Service: " + strURL);
					BackNextController.getInstance().setNextButtonEnabled(true);
					return true;
				} else {
					String msg = Lang.l().step7SOSconnectionFailed(strURL,urlConn.getResponseCode());
					JOptionPane.showMessageDialog(null,
							msg,
							Lang.l().warningDialogTitle(),
							JOptionPane.WARNING_MESSAGE);
					logger.warn(msg);
				}        	
			} catch (IOException e) {
				String msg = Lang.l().step7SOSConnectionFailedException(strURL,
						e.getMessage(),
						Constants.URL_CONNECT_READ_TIMEOUT_SECONDS,
						Constants.URL_CONNECT_TIMEOUT_SECONDS);
				JOptionPane.showMessageDialog(null,
						msg,
						Lang.l().errorDialogTitle(),
						JOptionPane.ERROR_MESSAGE);
				logger.error(msg,e);
			}
		}
		BackNextController.getInstance().setNextButtonEnabled(true);
		return false;
	}

	@Override
	public StepController getNext() {
		return null;
	}

	@Override
	public StepModel getModel() {
		return this.s7M;
	}

}
