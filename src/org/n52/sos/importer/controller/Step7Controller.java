package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.view.Step7Panel;

public class Step7Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step7Controller.class);
	
	private Step7Panel step7Panel;
	
	private HttpClient httpClient;
	
	private HttpPost httpPost;
	
	public Step7Controller() {
	}
	
	@Override
	public void loadSettings() {
		step7Panel = new Step7Panel();	
	}

	@Override
	public void saveSettings() {
		String sosURL = step7Panel.getSOSURL();

		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			mv.print();
		}
		
		String registerSensorTemplate = readTemplate("RegisterSensor_measurement_template");
		String completedTemplate = "";
		
		int successfullyRegisteredSensors = 0;
		int sensorsToRegister = ModelStore.getInstance().getSensorsToRegister().size();
		ArrayList<String> sensorErrors = new ArrayList<String>();
		ArrayList<String> wrongSensors = new ArrayList<String>();
		
		connectToSOS(sosURL);
		for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister()) {
			completedTemplate = rs.fillTemplate(registerSensorTemplate);
			//logger.info(rs);
			
			String answer = sendPostMessage(completedTemplate);
			if (answer.contains("AssignedSensorId"))
				successfullyRegisteredSensors++;
			if (answer.contains("Exception")) {
				sensorErrors.add(answer);
				wrongSensors.add(rs.toString());
			}
		}
		
		logger.info("Sensors to register: " + sensorsToRegister);
		logger.info("Successful: " + successfullyRegisteredSensors);
		for (int i = 0; i < sensorErrors.size(); i++) {
			logger.error(wrongSensors.get(i));			
			logger.error(sensorErrors.get(i));
		}
		
		ArrayList<String> observationErrors = new ArrayList<String>();
		ArrayList<String> wrongObservations = new ArrayList<String>();
		
		int observationsToInsert = ModelStore.getInstance().getObservationsToInsert().size();
		int successfullyInsertedObservations = 0;
		String insertObservationTemplate = readTemplate("InsertObservation_samplingPoint_template");
		
		for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert()) {
			completedTemplate = io.fillTemplate(insertObservationTemplate);
			//logger.info(io);		
			
			String answer = sendPostMessage(completedTemplate);
			if (answer.contains("AssignedObservationId"))
				successfullyInsertedObservations++;	
			if (answer.contains("Exception")) {
				observationErrors.add(answer);
				wrongObservations.add(io.toString());
			}
			
			if (observationErrors.size() > 50) break;
		}
		disconnectFromSOS();
		
		logger.info("Observations to insert: " + observationsToInsert);
		logger.info("Successful: " + successfullyInsertedObservations);
		
		for (int i = 0; i < observationErrors.size(); i++) {
			logger.error(observationErrors.get(i));			
			logger.error(wrongObservations.get(i));
		}
		
	}
	
	private String readTemplate(String templateName) {
		URL url = Step7Controller.class.getResource("/org/n52/sos/importer/templates/");
		File file;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			file = new File(url.getPath());
		}
		File f = new File(file.getAbsolutePath() + "/" + templateName + ".xml");
		
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return sb.toString();
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
		return null;
	}
	
    /**
     * sends a POST-request
     * 
     * @param serviceURL
     * @param request
     * @return
     */
    public String sendPostMessage(String request) { 
    	String answer = "";

        try {
			httpPost.setEntity(new StringEntity(request, "text/xml", "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) 
	        	answer = EntityUtils.toString(resEntity);

	        return answer;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
    }
    
    public void connectToSOS(String serviceURL) {
    	httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(serviceURL);
    }
    
    public void disconnectFromSOS() {
        httpClient.getConnectionManager().shutdown();
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
