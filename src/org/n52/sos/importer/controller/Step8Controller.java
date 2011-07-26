package org.n52.sos.importer.controller;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step8Model;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.view.Step8Panel;

public class Step8Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step8Controller.class);
	
	private Step8Panel step8Panel;
	
	private Step8Model step8Model;
	
	private HttpClient httpClient;
	
	private HttpPost httpPost;
	
	private boolean cancelled;
	
	private AssembleInformation assembleInformation;
	
	private RegisterSensors registerSensors;
	
	private InsertObservations insertObservations;
	
	public Step8Controller(Step8Model step8Model) {
		this.step8Model = step8Model;
	}
	
	@Override
	public void loadSettings() {		
		step8Panel = new Step8Panel();
		BackNextController.getInstance().setFinishButtonEnabled(false);
		BackNextController.getInstance().changeNextToFinish();
		
		FileAppender a = (FileAppender) LogManager.getRootLogger().getAppender("RoFi");
		File f = new File(a.getFile());
		step8Panel.setLogFileURI(f.toURI());		

		assembleInformation = new AssembleInformation();
		registerSensors = new RegisterSensors();
		insertObservations = new InsertObservations();
		cancelled = false;
		assembleInformation.execute();
	}
	
	public void assembleInformationDone() {
		String sosURL = step8Model.getSosURL();
		connectToSOS(sosURL);

		registerSensors.execute();
	}
	
	public void registerSensorsDone() {
		insertObservations.execute();
	}

    private class AssembleInformation extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			step8Panel.setIndeterminate(true);
			
			for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
				mv.print();
			}
			
			/*
			for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister())
				logger.info(rs);
			for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert())
				logger.info(io);	
			*/
			
			return null;
		}
    	
        @Override
        public void done() {
        	step8Panel.setIndeterminate(false);
            assembleInformationDone();
        }
		
    }
	
	
    private class RegisterSensors extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
    		String registerSensorTemplate = readTemplate("RegisterSensor_measurement_template");
    		String completedTemplate = "";
    		
    		int counter = 0;
    		int successful = 0;
    		int errors = 0;
    		int total = ModelStore.getInstance().getSensorsToRegister().size();
    		step8Panel.setTotalNumberOfSensors(total);
    		Iterator<RegisterSensor> i = ModelStore.getInstance().getSensorsToRegister().iterator();

    		while(i.hasNext()) {
    			RegisterSensor rs = i.next();
    			completedTemplate = rs.fillTemplate(registerSensorTemplate);
    			
    			String answer = sendPostMessage(completedTemplate);
    			if (answer.contains("AssignedSensorId")) {
    				step8Panel.setNumberOfSuccessfulSensors(++successful);
    			} else if (answer.contains("Exception")) {
    				logger.error(rs.toString());
    				logger.error(answer);
    				step8Panel.setNumberOfErroneousSensors(++errors);
    			}
    			counter++;
    			double process = (double) counter / (double) total * 100;
    			step8Panel.setRegisterSensorProgress((int) process);
    		}

            return null;
        }

        @Override
        public void done() {
            registerSensorsDone();
        }
    }
    
    class InsertObservations extends SwingWorker<Void, Void> {
    	
        @Override
        public Void doInBackground() {
        	String insertObservationTemplate = readTemplate("InsertObservation_samplingPoint_template");
        	String completedTemplate = "";
        	
    		int counter = 0;
    		int successful = 0;
    		int errors = 0;
    		int total = ModelStore.getInstance().getObservationsToInsert().size();
    		step8Panel.setTotalNumberOfObservations(total);
    		Iterator<InsertObservation> i = ModelStore.getInstance().getObservationsToInsert().iterator();
    		
    		while (i.hasNext()) {
    			InsertObservation io = i.next();
    			completedTemplate = io.fillTemplate(insertObservationTemplate);	
    			
    			String answer = sendPostMessage(completedTemplate);
    			if (answer.contains("AssignedObservationId"))
    				step8Panel.setNumberOfSuccessfulObservations(++successful);	
    			if (answer.contains("Exception")) {
    				logger.error(io.toString());
    				logger.error(answer);
    				step8Panel.setNumberOfErroneousObservations(++errors);
    			}
    			counter++;
    			double process = (double) counter / (double) total * 100;
    			step8Panel.setInsertObservationProgress((int) process);
    		}

            return null;
        }

        @Override
        public void done() {
        	if (!cancelled) {
    		disconnectFromSOS();
    		Toolkit.getDefaultToolkit().beep();
    		BackNextController.getInstance().setFinishButtonEnabled(true);
        	}
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
	public void saveSettings() {
		
	}

	@Override
	public String getDescription() {
		return "Step 8: Register Sensors and Insert Observations into SOS";
	}

	@Override
	public JPanel getStepPanel() {
		return step8Panel;
	}

	@Override
	public StepController getNextStepController() {
		return null;
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public StepController getNext() {
		return null;
	}
 
	@Override
	public void back() {
		BackNextController.getInstance().changeFinishToNext();
		cancelled = true;
		assembleInformation.cancel(true);
		registerSensors.cancel(true);
		insertObservations.cancel(true);
		ModelStore.getInstance().clearObservationsToInsert();
		ModelStore.getInstance().clearSensorsToRegister();
	}
}
