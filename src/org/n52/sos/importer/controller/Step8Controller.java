package org.n52.sos.importer.controller;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step8Model;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step8Panel;

/**
 * assembles all information from previous steps,
 * fills XML template files with it and uploads
 * them to the Sensor Observation Service
 * @author Raimund
 *
 */
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
		logger.info("Log file is stored at: " + f.toString());

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
			
			logger.info("Assemble information from table and previous steps");
			assembleInformation();
			
			for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister())
				logger.debug(rs);
			for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert())
				logger.debug(io);	
			
			return null;
		}
    	
        @Override
        public void done() {
        	step8Panel.setIndeterminate(false);
            assembleInformationDone();
        }
    }
    
	public void assembleInformation() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			Column column = (Column) mv.getTableElement();
			DateAndTimeController dtc = new DateAndTimeController();
			
			// TODO insert firstLineWithData here?
			
			for (int i = 0; i < TableController.getInstance().getRowCount(); i++) {
				RegisterSensor rs = new RegisterSensor();
				InsertObservation io = new InsertObservation();
				
				//the cell of the current Measured Value
				Cell c = new Cell(i, column.getNumber());
				String value = TableController.getInstance().getValueAt(c);
				try {
					String parsedValue = mv.parse(value).toString();
					io.setValue(parsedValue);
				} catch (Exception e) {
					continue;
				}
				
				//when was the current Measured Value measured
				dtc.setDateAndTime(mv.getDateAndTime());
				String timeStamp = dtc.forThis(c);	
				io.setTimeStamp(timeStamp);
				
				FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(c);
				io.setFeatureOfInterestName(foi.getNameString());
				io.setFeatureOfInterestURI(foi.getURIString());
				
				//where was the current Measured Value measured
				Position p = foi.getPosition();
				io.setLatitudeValue(p.getLatitude().getValue() + "");
				io.setLongitudeValue(p.getLongitude().getValue() + "");
				io.setEpsgCode(p.getEPSGCode().getValue() + "");
				rs.setLatitudeValue(p.getLatitude().getValue() + "");
				rs.setLatitudeUnit(p.getLatitude().getUnit());
				rs.setLongitudeValue(p.getLongitude().getValue() + "");
				rs.setLongitudeUnit(p.getLongitude().getUnit());
				rs.setHeightValue(p.getHeight().getValue() + "");
				rs.setHeightUnit(p.getHeight().getUnit());
				rs.setEpsgCode(p.getEPSGCode().getValue() + "");
				
				ObservedProperty op = mv.getObservedProperty().forThis(c);
				io.setObservedPropertyURI(op.getURIString());
				rs.setObservedPropertyName(op.getNameString());
				rs.setObservedPropertyURI(op.getURIString());
				
				UnitOfMeasurement uom = mv.getUnitOfMeasurement().forThis(c);
				io.setUnitOfMeasurementCode(uom.getNameString());
				rs.setUnitOfMeasurementCode(uom.getNameString());
				
				Sensor sensor = mv.getSensor();
				if (sensor != null) {
					 sensor = mv.getSensor().forThis(c);
				} else { //Step6bSpecialController
					sensor = mv.getSensorFor(foi.getNameString(), op.getNameString());
				}
				
				io.setSensorName(sensor.getNameString());
				io.setSensorURI(sensor.getURIString());
				rs.setSensorName(sensor.getNameString());
				rs.setSensorURI(sensor.getURIString());
					
				ModelStore.getInstance().addObservationToInsert(io);
				ModelStore.getInstance().addSensorToRegister(rs);
			}
		}
	}
	
    private class RegisterSensors extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
        	logger.info("Register Sensors at Sensor Observation Service");
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
        	logger.info("Insert Observations at Sensor Observation Service");
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
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = this.getClass().getResourceAsStream("/org/n52/sos/importer/templates/" + templateName + ".xml");
			InputStreamReader fr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException ioe) {
			logger.error("Error while reading template.", ioe);
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
			logger.error("Error while sending POST request to SOS", e);
		} catch (ClientProtocolException e) {
			logger.error("Error while sending POST request to SOS", e);
		} catch (IOException e) {
			logger.error("Error while sending POST request to SOS", e);
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
