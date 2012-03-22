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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step8Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step7Panel;

/**
 * lets the user choose a URL of a Sensor Observation Service
 * and test the connection
 * @author Raimund
 *
 */
public class Step7Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step7Controller.class);
	
	private Step7Panel step7Panel;
	
	public Step7Controller() {
	}
	
	@Override
	public void loadSettings() {
		step7Panel = new Step7Panel();	
	}

	@Override
	public void saveSettings() {		
	}

	@Override
	public String getDescription() {
		return "Step 7: Choose Sensor Observation Service";
	}

	@Override
	public JPanel getStepPanel() {
		return step7Panel;
	}

	@Override
	public StepController getNextStepController() {
		String url = step7Panel.getSOSURL();
		return new Step8Controller(new Step8Model(url));
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		//get and check URI
		String url = step7Panel.getSOSURL();
		return testConnection(url);
	}
	
	public boolean testConnection(String strURL) {
	    try {
	        URL url = new URL(strURL);
	        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	        urlConn.connect();

	        if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
	    		logger.info("Successfully tested connection to Sensor Observation Service: " + strURL);
	        	return true;
	        } else {
	        	String msg = "Could not connect to Sensor Observation Service: "
	        		+ strURL + ". HTTP Response Code: " 
	        		+ urlConn.getResponseCode();
				JOptionPane.showMessageDialog(null,
						msg,
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
	    		logger.warn(msg);
	        	return false;
	        }        	
	    } catch (IOException e) {
			JOptionPane.showMessageDialog(null,
				    "Connection to Sensor Observation Service " + strURL + " failed. " + e.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
	    	return false;
	    }
	}

	@Override
	public StepController getNext() {
		return null;
	}

	@Override
	public StepModel getModel() {
		return null;
	}	
}
