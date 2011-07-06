package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.n52.sos.importer.view.Step8Panel;

public class Step8Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step8Controller.class);
	
	private Step8Panel step8Panel;
	
	public Step8Controller() {
		step8Panel = new Step8Panel();
	}
	
	private String readTemplate(String templateName) {
		URL url = Step8Controller.class.getResource("/org/n52/sos/importer/templates/");
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
		return "Step 8: Choose Sensor Observation Service";
	}

	@Override
	public JPanel getStepPanel() {
		return step8Panel;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNextStepController() {
		String sosURL = step8Panel.getSOSURL();

		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			mv.print();
		}
		
		String registerSensorTemplate = readTemplate("RegisterSensor_measurement_template");
		for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister()) {
			logger.info(rs);
			String completedTemplate = rs.fillTemplate(registerSensorTemplate);
			try {
				sendPostMessage(sosURL, completedTemplate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String insertObservationTemplate = readTemplate("InsertObservation_samplingPoint_template");
		for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert()) {
			String completedTemplate = io.fillTemplate(insertObservationTemplate);
			logger.info(io);
			
			try {
				sendPostMessage(sosURL, completedTemplate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
    /**
     * sends a POST-request
     * 
     * @param serviceURL
     * @param request
     * @return
     */
    public void sendPostMessage(String serviceURL, String request) throws IOException {

        logger.info("POST-request sent to: " + serviceURL);
    	
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(serviceURL);

        method.setEntity(new StringEntity(request, "text/xml", "UTF-8"));

        HttpResponse response = httpClient.execute(method);
        HttpEntity resEntity = response.getEntity();

        logger.info("Response Status: " + response.getStatusLine());
        if (resEntity != null) {
        	logger.info("Answer: " + EntityUtils.toString(resEntity));
        }
        httpClient.getConnectionManager().shutdown();
    }	
}
