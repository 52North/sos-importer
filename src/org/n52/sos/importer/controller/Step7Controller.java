package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JPanel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
		
		logger.info(ModelStore.getInstance().getSensorsToRegister().size() +
		" Sensors to register:");
		
		String registerSensorTemplate = readTemplate("RegisterSensor_measurement_template");
		String completedTemplate = "";
		
		for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister()) {
			completedTemplate = rs.fillTemplate(registerSensorTemplate);
			logger.debug(rs);
			logger.info(completedTemplate);
			try {
				sendPostMessage(sosURL, completedTemplate);
			} catch (IOException e) {
				logger.error("Could not send RegisterSensor to SOS", e);
			}
		}
		
		String insertObservationTemplate = readTemplate("InsertObservation_samplingPoint_template");
		for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert()) {
			completedTemplate = io.fillTemplate(insertObservationTemplate);
			logger.debug(io);		
			
			try {
				sendPostMessage(sosURL, completedTemplate);
			} catch (IOException e) {
				logger.error("Could not send InsertObservation to SOS", e);
			}
			
		}
		
		logger.info(ModelStore.getInstance().getObservationsToInsert().size() +
		" Observations to insert. For example: ");	
		logger.info(completedTemplate);
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
    public void sendPostMessage(String serviceURL, String request) throws IOException {   	
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(serviceURL);

        method.setEntity(new StringEntity(request, "text/xml", "UTF-8"));

        HttpResponse response = httpClient.execute(method);
        HttpEntity resEntity = response.getEntity();

        logger.info("POST-request sent to: " + serviceURL);
        logger.info("Response Status: " + response.getStatusLine());
        if (resEntity != null) {
        	logger.info("Answer: " + EntityUtils.toString(resEntity));
        }
        httpClient.getConnectionManager().shutdown();
    }

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		//get and check URI
		String uri = step7Panel.getSOSURL();
		URI URI = null;
		try {
			URI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Wrong URI", e);
			return false;
		}
		return true;
	}

	@Override
	public StepController getNext() {
		return null;
	}	
}
