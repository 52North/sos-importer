package org.n52.sos.importer.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step8Model;
import org.n52.sos.importer.view.Step7Panel;

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
				JOptionPane.showMessageDialog(null,
						"Could not connect to Sensor Observation Service: " + strURL +
	    				". HTTP Response Code: " + urlConn.getResponseCode(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
	    		logger.warn("Could not connect to Sensor Observation Service: " + strURL +
	    				". HTTP Response Code: " + urlConn.getResponseCode());
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
}
