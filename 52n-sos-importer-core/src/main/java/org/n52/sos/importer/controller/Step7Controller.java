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
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step7Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * lets the user choose a URL of a Sensor Observation Service
 * and test the connection
 * @author Raimund
 *
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

	@Override
	public void saveSettings() {
		String sosURL = s7P.getSOSURL();
		boolean directImport = s7P.isDirectImport(),
				saveConfig = s7P.isSaveConfig();
		File configFile = new File(s7P.getConfigFile());
		if (this.s7M == null) {
			this.s7M = new Step7Model(sosURL, directImport, saveConfig, configFile);
		} else {
			s7M.setSosURL(sosURL);
			s7M.setConfigFile(configFile);
			s7M.setDirectImport(directImport);
			s7M.setSaveConfig(saveConfig);
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
		if (s7P.isDirectImport()) {
			String url = s7P.getSOSURL();
			result = result && testConnection(url);
		}
		if (s7P.isSaveConfig()) {
			// show dialog about successful save and the location of the file
		}
		return result;
	}
	
	public boolean testConnection(String strURL) {
	    try {
	    	// TODO Expand -> Send get capabilities request
	        URL url = new URL(strURL);
	        // FIXME long timeout if service is not available
	        // TODO add proxy handling: http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
	        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	        urlConn.connect();

	        if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
	    		logger.info("Successfully tested connection to Sensor Observation Service: " + strURL);
	        	return true;
	        } else {
	        	String msg = Lang.l().step7SOSconnectionFailed(strURL,urlConn.getResponseCode());
				JOptionPane.showMessageDialog(null,
						msg,
					    Lang.l().warningDialogTitle(),
					    JOptionPane.WARNING_MESSAGE);
	    		logger.warn(msg);
	        	return false;
	        }        	
	    } catch (IOException e) {
	    	String msg = Lang.l().step7SOSConnectionFailedException(strURL,e.getMessage());
			JOptionPane.showMessageDialog(null,
				    msg,
				    Lang.l().errorDialogTitle(),
				    JOptionPane.ERROR_MESSAGE);
			logger.error(msg,e);
	    	return false;
	    }
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
