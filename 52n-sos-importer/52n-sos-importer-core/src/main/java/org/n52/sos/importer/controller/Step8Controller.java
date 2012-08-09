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

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;
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
import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.utils.XMLTools;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.Boolean;
import org.n52.sos.importer.model.measuredValue.Count;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.measuredValue.Text;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step8Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * Assembles all information from previous steps, 
 * fills XML template files with it and uploads
 * them to the Sensor Observation Service,
 * displays the configuration,
 * and the log file 
 * depending on the options from step 7.
 * @author Raimund
 * @author e.h.juerrens@52north.org
 *
 */
public class Step8Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step8Controller.class);

	private Step8Panel step8Panel;
	
	private Step7Model step7Model;
	
	private HttpClient httpClient;
	
	private HttpPost httpPost;
	
	private boolean cancelled;
	
	private AssembleInformation assembleInformation;
	
	private RegisterSensors registerSensors;
	
	private InsertObservations insertObservations;

	private TableController tableController;
	
	/**
	 * List of changed foi names in <code>String[]</code>.<br />
	 * <code>[0]</code> is the <b>old</b> and<br />
	 * <code>[1]</code> is the <b>new</b> name
	 */
	private ArrayList<String[]> changedResourceNames = new ArrayList<String[]>();
	
	public Step8Controller(Step7Model step7Model, int firstLineWithData) {
		this.step7Model = step7Model;
		this.tableController = TableController.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see org.n52.sos.importer.interfaces.StepController#loadSettings()
	 * In this case, the sensors are registered and the observations are 
	 * inserted by this method
	 */
	@Override
	public void loadSettings() {		
		step8Panel = new Step8Panel(step7Model);
		BackNextController.getInstance().changeNextToFinish();
		File f = null;
		
		Logger rL = Logger.getRootLogger();
		@SuppressWarnings("rawtypes")
		Enumeration appender = rL.getAllAppenders();
		while(appender.hasMoreElements()) {
			Object o = appender.nextElement();
			if(o instanceof FileAppender) {
				FileAppender fA = (FileAppender) o;
				f = new File(fA.getFile());
				step8Panel.setLogFileURI(f.toURI());
				logger.info("Log saved to file: " + f.getAbsolutePath());
			}
		}
		// save model always
		try {
			if (MainController.getInstance().saveModel(step7Model.getConfigFile())) {
				if (logger.isInfoEnabled()) {
					logger.info("Configuration saved to file: " + step7Model.getConfigFile().getAbsolutePath());
				}
			}
		} catch (IOException e) {
			logger.error("Exception thrown: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(step8Panel, 
					Lang.l().step8SaveModelFailed(f,e.getLocalizedMessage()), 
					Lang.l().errorDialogTitle(), 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * When information assembling is done, the sensors are registered
	 */
	public void assembleInformationDone() {
		String sosURL = step7Model.getSosURL();
		connectToSOS(sosURL);

		registerSensors.execute();
	}
	
	/**
	 * When sensor registering is done, the data is submitted
	 * @param notRegisteredSensors
	 */
	public void registerSensorsDone(String[] notRegisteredSensors) {
		insertObservations.setNotRegisteredSensors(notRegisteredSensors);
		insertObservations.execute();
	}

    private class AssembleInformation extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			step8Panel.setIndeterminate(true);
			
			logger.info("Assemble information from table and previous steps");
			assembleInformation();
			
			if(logger.isDebugEnabled()) {
				for (RegisterSensor rs: ModelStore.getInstance().getSensorsToRegister()) {
					if (logger.isDebugEnabled()) {
						logger.debug(rs);
					}
				}
				for (InsertObservation io: ModelStore.getInstance().getObservationsToInsert()) {
					if (logger.isDebugEnabled()) {
						logger.debug(io);	
					}
				}
			}
			
			logger.info("Assembling information finished");
			
			return null;
		}
		
		/**
		 * Here, all information collected before is combined and the modelstore is
		 * filled.
		 */
		public void assembleInformation() {
			for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
				Column column = (Column) mv.getTableElement();
				DateAndTimeController dtc = new DateAndTimeController();

				for (int row = tableController.getFirstLineWithData(); row < tableController.getRowCount(); row++) {
					RegisterSensor rs = new RegisterSensor();
					InsertObservation io = new InsertObservation();
					NumericValue nv = new NumericValue();
					
					//the cell of the current Measured Value
					Cell cell = new Cell(row, column.getNumber());
					String value = tableController.getValueAt(cell);
					try {
						String parsedValue = mv.parse(value).toString();
						io.setValue(parsedValue);
					} catch (Exception e) {
						if (logger.isTraceEnabled()) {
							logger.trace("Value could not be parsed: " + value, e);
						}
						continue;
					}
					
					// set default value for register sensor observation template
					String defaultValue = "";
					if (mv instanceof NumericValue) {
						defaultValue = "0.0";
					} else if (mv instanceof Text) {
						defaultValue = "default";
					} else if (mv instanceof Boolean) {
						defaultValue = "false";
					} else if (mv instanceof Count) {
						defaultValue = "0";
					}
					rs.setDefaultValue(defaultValue);
					
					// when was the current Measured Value measured
					dtc.setDateAndTime(mv.getDateAndTime());
					String timeStamp;
					try {
						timeStamp = dtc.forThis(cell);
					} catch (ParseException e) {
						logger.error("Timestamp of " + cell + 
								" could not be parsed. Skipping it. " +
								"Error Message: " + e.getMessage(), e);
						continue;
					}	
					io.setTimeStamp(timeStamp);
					
					// which is the observed feature of interest
					FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(cell);
					String foiName = "", foiURI = "", origFoiName = "";
					boolean foiNameAdjusted = false;
					if (foi.isGenerated()) {
						String[] resValues = getValuesForGeneratedResource(foi,row);
						foiName = resValues[0];
						foiURI = resValues[1];
					} else {
						foiName = foi.getNameString();
						foiURI = foi.getURIString();
					}
					// implement check for NCName compliance and remove bad values
					origFoiName = new String(foiName);
					// check first character
					if (foiName.charAt(0) != '_' || !XMLTools.isLetter(foiName.charAt(0))) {
						// if failed -> add "_"
						foiName = Constants.UNICODE_FOI_PREFIX + foiName;
						foiNameAdjusted = true;
					}
					// clean rest of string using Constants.UNICODE_REPLACER
					char[] foiNameChars = foiName.toCharArray();
					for (int i = 1; i < foiNameChars.length; i++) {
						char c = foiNameChars[i];
						if (!XMLTools.isNCNameChar(c)) {
							foiNameChars[i] = Constants.UNICODE_REPLACER;
						}
					}
					foiName = String.valueOf(foiNameChars);
					// check if name is only containing "_"
					Matcher matcher = Constants.
							UNICODE_ONLY_REPLACER_LEFT_PATTERN.matcher(foiName);
					if (matcher.matches()) {
						// if yes -> change to "_foi-" + foiURI.hashCode()
						foiName = Constants.UNICODE_FOI_PREFIX + foiURI.hashCode();
					}
					// check NCName compliance only for debugging
					if (logger.isDebugEnabled()) {
						boolean shouldBeTrue = XMLTools.isNCName(foiName);
						logger.debug("Is generated foi name valid?: " + shouldBeTrue);
					}
					// if failed -> log and skip
					// else continue
					if (foiNameAdjusted) {
						String[] a = {origFoiName,foiName};
						setChangedResourceNames(a);
						logger.info("The name of the feature of interest \"" + 
								origFoiName + "\" is changed to \"" + foiName + 
								"\" due to XML compliance reasons. " +
								"Check documentation for further information.");
					}
					rs.setFeatureOfInterestURI(foiURI);
					rs.setFeatureOfInterstName(foiName);
					io.setFeatureOfInterestName(foiName);
					io.setFeatureOfInterestURI(foiURI);
					
					{ // Position
						// where was the current Measured Value measured
						Position p = foi.getPosition();
						// FIXME implement handling of positions from table
						String latitude = "";
						if (p.getLatitude() != null && p.getLatitude().getTableElement() != null) {
							latitude = tableController.getValueAt(row,
									((Column) p.getLatitude().getTableElement())
									.getNumber());
							latitude = nv.parse(latitude) + "";
						} else {
							latitude = p.getLatitude().getValue() + "";
						}
						String latUnit = "";
						if (p.getLatitude() != null
								&& p.getLatitude().getUnit() != null) {
							latUnit = p.getLatitude().getUnit();
						} else {
							latUnit = Constants.DEFAULT_LATITUDE_UNIT;
						}

						String longitude = "";
						if (p.getLongitude() != null && p.getLongitude().getTableElement() != null) {
							longitude = tableController.getValueAt(row,
									((Column) p.getLongitude().getTableElement())
									.getNumber());
							longitude = nv.parse(longitude) + "";
						} else {
							longitude = p.getLongitude().getValue() + "";
						}
						String longUnit = "";
						if (p.getLongitude() != null
								&& p.getLongitude().getUnit() != null) {
							longUnit = p.getLongitude().getUnit();
						} else {
							longUnit = Constants.DEFAULT_LONGITUDE_UNIT;
						}

						String epsgCode = "";
						if (p.getEPSGCode() != null && p.getEPSGCode().getTableElement() != null) {
							epsgCode = tableController.getValueAt(row,
									((Column) p.getEPSGCode().getTableElement())
									.getNumber());
						} else {
							epsgCode = p.getEPSGCode().getValue() + "";
						}

						String altitude = "";
						if (p.getEPSGCode() != null && p.getHeight().getTableElement() != null) {
							altitude = tableController.getValueAt(row,
									((Column) p.getHeight().getTableElement())
									.getNumber());
							altitude = nv.parse(altitude) + "";
						} else {
							altitude = p.getHeight().getValue() + "";
						}
						String altitudeUnit = "";
						if (p.getLatitude() != null
								&& p.getLatitude().getUnit() != null) {
							altitudeUnit = p.getLatitude().getUnit();
						} else {
							altitudeUnit = Constants.DEFAULT_ALTITUDE_UNIT;
						}

						io.setLatitudeValue(latitude);
						io.setLongitudeValue(longitude);
						io.setEpsgCode(epsgCode);
						rs.setLatitudeValue(latitude);
						rs.setLatitudeUnit(latUnit);
						rs.setLongitudeValue(longitude);
						rs.setLongitudeUnit(longUnit);
						rs.setAltitudeValue(altitude);
						rs.setAltitudeUnit(altitudeUnit);
						rs.setEpsgCode(epsgCode);
					} // EO: position
					
					// what property is observed at this foi
					ObservedProperty op = mv.getObservedProperty().forThis(cell);
					String obsPropName = "";
					String obsPropURI = "";
					if (op.isGenerated()) {
						String[] resValues = getValuesForGeneratedResource(op,row);
						obsPropName = resValues[0];
						obsPropURI = resValues[1];
					} else {
						obsPropName = op.getNameString();
						obsPropURI = op.getURIString();
					}
					io.setObservedPropertyURI(obsPropURI);
					rs.setObservedPropertyName(obsPropName);
					rs.setObservedPropertyURI(obsPropURI);
					
					// what is the UOM for the observed/measured value
					UnitOfMeasurement uom = mv.getUnitOfMeasurement().forThis(cell);
					String uomName = "";
					if (uom.isGenerated()) {
						String[] resValues = getValuesForGeneratedResource(uom,row);
						uomName = resValues[0];
					} else {
						uomName = uom.getNameString();
					}
					io.setUnitOfMeasurementCode(uomName);
					rs.setUnitOfMeasurementCode(uomName);
					
					Sensor sensor = mv.getSensor();
					if (sensor != null) {
						 sensor = mv.getSensor().forThis(cell);
					} else { //Step6bSpecialController
						sensor = mv.getSensorFor(foi.getNameString(), op.getNameString());
					}
					String sensorName = "";
					String sensorURI = "";
					if (sensor.isGenerated()) {
						String[] resValues = getValuesForGeneratedResource(sensor,row);
						sensorName = resValues[0];
						sensorURI = resValues[1];
					} else {
						sensorName = sensor.getNameString();
						sensorURI = sensor.getURIString();
					}
					io.setSensorName(sensorName);
					io.setSensorURI(sensorURI);
					rs.setSensorName(sensorName);
					rs.setSensorURI(sensorURI);
					
					// offering handling
					String offering = "";
					String offeringId = "";
					if (step7Model.isGenerateOfferingFromSensorName()) {
						offering = sensorURI;
						offeringId = sensorName;
					} else {
						offering = step7Model.getOffering();
						offeringId = offering;
					}		
					if (!XMLTools.isNCName(offeringId)) {
						// implement check for NCName compliance and remove bad values
						String origOfferingId = new String(offeringId);
						boolean offeringIdAdjusted = false;
						// check first character
						if (offeringId.charAt(0) != '_' || !XMLTools.isLetter(offeringId.charAt(0))) {
							// if failed -> add "_"
							offeringId = Constants.UNICODE_FOI_PREFIX + offeringId;
							offeringIdAdjusted = true;
						}
						// clean rest of string using Constants.UNICODE_REPLACER
						char[] offeringIdChars = offeringId.toCharArray();
						for (int i = 1; i < offeringIdChars.length; i++) {
							char c = offeringIdChars[i];
							if (!XMLTools.isNCNameChar(c)) {
								offeringIdChars[i] = Constants.UNICODE_REPLACER;
							}
						}
						offeringId = String.valueOf(offeringIdChars);
						// check if name is only containing "_"
						Matcher matcher2 = Constants.
								UNICODE_ONLY_REPLACER_LEFT_PATTERN.matcher(offeringId);
						if (matcher2.matches()) {
							// if yes -> change to "_offering-" + offeringURI.hashCode()
							offeringId = Constants.UNICODE_OFFERING_PREFIX + offeringId.hashCode();
						}
						// check NCName compliance only for debugging
						if (logger.isDebugEnabled()) {
							boolean shouldBeTrue = XMLTools.isNCName(offeringId);
							logger.debug("Is generated offering name valid?: " + shouldBeTrue);
						}
						// if failed -> log and skip
						// else continue
						if (offeringIdAdjusted) {
							String[] a = {origOfferingId,offeringId};
							setChangedResourceNames(a);
							logger.info(
									String.format("The id of the offering \"%s\" is changed from \"%s\" to \"%s\" due to XML compliance reasons. " +
									"Check the documentation for further information.", 
									offering,
									origOfferingId,
									offeringId));
						}
					}
					rs.setOfferingName(offering);
					rs.setOfferingId(offeringId);
					
					ModelStore.getInstance().addObservationToInsert(io);
					// To ban double entries, check for already contained sensors by id
					if (!isAlreadyInList(ModelStore.getInstance().getSensorsToRegister(),rs)) {
						ModelStore.getInstance().addSensorToRegister(rs);
					}
				}
			}
		}
    	
		/**
		 * Iterates over the HashSet and compare the sensor ID with each element
		 * @param sensorsToRegister
		 * @param rs
		 * @return
		 */
		private boolean isAlreadyInList(
				HashSet<RegisterSensor> sensorsToRegister, RegisterSensor rs) {
			if (logger.isTraceEnabled()) {
				logger.trace("isAlreadyInList()");
			}
			for (Iterator<RegisterSensor> iter = sensorsToRegister.iterator();
					iter.hasNext();) {
				RegisterSensor rsFromList = iter.next();
				if (rsFromList.getSensorURI() != null && 
						rs.getSensorURI() != null && 
						rsFromList.getSensorURI().equalsIgnoreCase( rs.getSensorURI() ) ){
					return true;
				}
			}
			return false;
		}

		/**
		 * Build the name and uri from the related columns.
		 * @param res
		 * @return <code>String[]</code> with two values:<ul>
		 * 	<li><code>[0]</code>: resourceName</li>
		 *  <li><code>[1]</code>: resourceURI</li></ul>
		 */
		private String[] getValuesForGeneratedResource(Resource res, int row) {
			if (logger.isTraceEnabled()) {
				logger.trace("getValuesForGeneratedResource()");
			}
			String concatString = res.getConcatString(),
					uriOrUriPrefix = res.getUriPrefix();
			boolean useNameAfterPrefix = res.isUseNameAfterPrefixAsURI();
			Column[] relatedCols = (Column[]) res.getRelatedCols();
			String[] result = {"",""};
			if (concatString == null) {
				concatString = "";
			}
			// get values from related cols and use concatString
			if (relatedCols.length >= 1) {
				result[0] = tableController.getValueAt(row, 
						relatedCols[0].getNumber());
				for (int i = 1; i < relatedCols.length; i++) {
					String tmp = tableController.getValueAt(row, relatedCols[i].getNumber());
					result[0] = result[0] + concatString + tmp;
				}
			}
			if (useNameAfterPrefix) {
				result[1] = uriOrUriPrefix + result[0];
			} else {
				result[1] = uriOrUriPrefix;
			}
			return result;
		}

		@Override
        public void done() {
        	step8Panel.setIndeterminate(false);
            assembleInformationDone();
        }
    }
    
    private class RegisterSensors extends SwingWorker<Void, Void> {
    	
    	private String[] failedSensors;

		@Override
        public Void doInBackground() {
        	logger.info("Register Sensors at Sensor Observation Service");
    		String registerSensorTemplate = readTemplate("RegisterSensor_measurement_template");
    		String completedTemplate = "";
    		
    		int counter = 0;
    		int successful = 0;
    		int errors = 0;
    		int total = ModelStore.getInstance().getSensorsToRegister().size();
    		
    		ArrayList<String> failed = new ArrayList<String>(total);
    		
    		step8Panel.setTotalNumberOfSensors(total);
    		Iterator<RegisterSensor> i = ModelStore.getInstance().getSensorsToRegister().iterator();

    		while(i.hasNext()) {
    			RegisterSensor rs = i.next();
    			completedTemplate = rs.fillTemplate(registerSensorTemplate);
    			counter++;
    			
    			String answer = sendPostMessage(completedTemplate,total,counter);
    			if (answer.contains("AssignedSensorId")) {
    				step8Panel.setNumberOfSuccessfulSensors(++successful);
    				logger.info("Sensor \"" + 
    						rs.getSensorURI() + 
    						"\" registered successfully at \"" + 
    						step7Model.getSosURL() +
    						"\".");
    			//
    			// check if the sensor is already registered is SOS
    			} else if(answer.contains(Constants.SOS_RESPONSE_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
    					answer.contains(Constants.SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_START) &&
    					answer.contains(Constants.SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_END) &&
    					answer.contains(rs.getSensorURI())) {
    				logger.info("Sensor with ID \"" + rs.getSensorURI() + 
    							"\" is already registered is SOS");
					step8Panel.setNumberOfSuccessfulSensors(++successful);
				//
				//
    			}else if (answer.contains("Exception")) {
    				String errorMsg = 
    					"Error while sending request to SOS\nSended request:\n" +
    					completedTemplate +
    					"\nResponse:\n" +
    					answer;
    				logger.error(errorMsg);
    				step8Panel.setNumberOfErroneousSensors(++errors);
    				failed.add(rs.getSensorURI());
    			}
    			double process = (double) counter / (double) total * 100;
    			step8Panel.setRegisterSensorProgress((int) process);
    		}
    		
    		failed.trimToSize();
    		if(failed.size() > 0) {
    			this.failedSensors = failed.toArray(new String[failed.size()]);
    		} else {
    			this.failedSensors = null;
    		}

            return null;
        }

        @Override
        public void done() {
            registerSensorsDone(failedSensors);
        }
    }
    
    private class InsertObservations extends SwingWorker<Void, Void> {
    	
    	private String[] notRegisteredSensors = null;
    	
        @Override
        public Void doInBackground() {
        	logger.info("Insert Observations at Sensor Observation Service");
        	String insertObservationTemplate = readTemplate("InsertObservation_samplingPoint_template");
        	String completedTemplate = "";
        	
    		int counter = 0;
    		int skipped = 0;
    		int successful = 0;
    		int errors = 0;
    		int total = ModelStore.getInstance().getObservationsToInsert().size();
    		
    		boolean anyFailedSensorRegistrations = false;
    		
    		if(notRegisteredSensors != null && 
    				notRegisteredSensors.length > 0) {
    			anyFailedSensorRegistrations = true;
    		}
    		
    		step8Panel.setTotalNumberOfObservations(total);
    		Iterator<InsertObservation> i = ModelStore.getInstance().getObservationsToInsert().iterator();
    		
    		while (i.hasNext()) {
    			InsertObservation io = i.next();
    			if(anyFailedSensorRegistrations && isThisSensorRegistered(io.getSensorURI())) {
    				if(logger.isDebugEnabled()) {
    					logger.debug("skipped insertobservation for sensor " + io.getSensorURI());
    				}
    				skipped++;
    				counter++;
    			} else {
    				completedTemplate = io.fillTemplate(insertObservationTemplate);	
    				counter++;
    				// TODO before sending request to SOS validate it
    				String answer = sendPostMessage(completedTemplate,total,counter);
    				if (answer.contains("AssignedObservationId"))
    					step8Panel.setNumberOfSuccessfulObservations(++successful);	
    				if (answer.contains("Exception")) {
    					String errorMsg = 
    						"Error while sending request to SOS\nSended request:\n" +
    						completedTemplate +
    						"\nResponse:\n" +
    						answer;
    					logger.error(errorMsg);
    					step8Panel.setNumberOfErroneousObservations(++errors);
    				}
    			}
    			double process = (double) counter / (double) total * 100;
    			step8Panel.setInsertObservationProgress((int) process);
    		}
    		logger.info("Sending observations finished, " +
    				"successful: " + successful + "; " +
    				"failed: " + errors);
    		if (skipped > 0) {
    			logger.info("Skipped " + skipped + " insert observation requests because of not registered sensors");
    		}
            return null;
        }

        private boolean isThisSensorRegistered(String sensorURI) {
        	for (int i = 0; i < this.notRegisteredSensors.length; i++) {
				if(this.notRegisteredSensors[i].equalsIgnoreCase(sensorURI))
					return false;
			}
			return true;
		}

		public void setNotRegisteredSensors(String[] notRegisteredSensors) {
			this.notRegisteredSensors = notRegisteredSensors;
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
    public String sendPostMessage(String request, int totalNumOfRequests, int currentNumber) { 
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
		return Lang.l().step8Description();
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
		if (assembleInformation != null) {
			assembleInformation.cancel(true);
		}
		if (registerSensors != null) {
			registerSensors.cancel(true);
		}
		if (insertObservations != null) {
			insertObservations.cancel(true);
		}
		ModelStore.getInstance().clearObservationsToInsert();
		ModelStore.getInstance().clearSensorsToRegister();
	}

	@Override
	public StepModel getModel() {
		return this.step7Model;
	}

	/**
	 * @return the changedResourceNames
	 */
	public String[][] getChangedResourceNames() {
		changedResourceNames.trimToSize();
		String[][] res = new String[changedResourceNames.size()][2];
		res = changedResourceNames.toArray(res);
		return res;
	}

	/**
	 * @param changedResourceName to add to the changedResourceNames<br />
	 * <code>[0]</code> is the <b>old</b> and<br />
	 * <code>[1]</code> is the <b>new</b> name
	 */
	public void setChangedResourceNames(String[] changedResourceName) {
		this.changedResourceNames.add(changedResourceName);
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
		int userChoice = JOptionPane.showConfirmDialog(step8Panel,
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
}
