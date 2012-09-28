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
package org.n52.sos.importer.feeder;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.RegisterSensorResponseDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.builder.BooleanObservationBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.CategoryObservationBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.CountObservationBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.MeasurementBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.RegisterSensorParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.Offering;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Handles connection to SOS and provides an easy to use interface.<br />
 * Now this class supports only OGC SOS <b>1.0.0</b>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class SensorObservationService {
	
	private static final Logger logger = Logger.getLogger(SensorObservationService.class);
	
	private final URL sosUrl;
	private final String sosVersion;
	private final SOSWrapper sosWrapper;
	private ServiceDescriptor serviceDescriptor;

	private ArrayList<String> registeredSensors;

	private ArrayList<InsertObservation> failedInsertObservations;
	
	public SensorObservationService(URL sosUrl) throws ExceptionReport, OXFException {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("SensorObservationService(%s)", sosUrl));
		}
		this.sosUrl = sosUrl;
		this.sosVersion = "1.0.0";
		this.sosWrapper = SOSWrapper.createFromCapabilities(sosUrl.toString(), sosVersion);
		this.serviceDescriptor = sosWrapper.getServiceDescriptor();
		this.failedInsertObservations = new ArrayList<InsertObservation>();
		this.registeredSensors = new ArrayList<String>();
	}

	public boolean isAvailable() {
		return sosWrapper.getServiceDescriptor() != null;
	}
	
	/**
	 * Checks for <b>RegisterSensor</b> and <b>InsertObservation</b> operations.
	 * 
	 * @return <code>true</code> if RegisterSensor and InsertObservation
	 *         operations are listed in the capabilities of this SOS, <br />
	 *         else <code>false</code>.
	 */
	public boolean isTransactional() {
		if (logger.isTraceEnabled()) {
			logger.trace("isTransactional()");
		}
		if (serviceDescriptor == null) {
			logger.error(String.format("Service descriptor not available for SOS \"%s\"", sosUrl));
			return false;
		}
		OperationsMetadata opMeta = serviceDescriptor.getOperationsMetadata();
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("OperationsMetadata found: %s", opMeta));
		}
		// check for RegisterSensor and InsertObservationOperation
		if (opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null &&
				opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Found all required operations: %s, %s",
						SOSAdapter.REGISTER_SENSOR,
						SOSAdapter.INSERT_OBSERVATION));
			}
			return true;
		}
		return false;
	}

	public ArrayList<InsertObservation> importData(DataFile dataFile) throws IOException, OXFException, XmlException {
		if (logger.isTraceEnabled()) {
			logger.trace("importData()");
		}
		// 0 Get line
		CSVReader cr = dataFile.getCSVReader();
		String[] values;
		int lineCounter = dataFile.getFirstLineWithData();
		// 1 Get all measured value columns =: mvCols
		int[] mVCols = dataFile.getMeasuredValueColumnIds();
		if (mVCols == null || mVCols.length == 0) {
			logger.fatal("No measured value columns found in configuration");
			return null;
		}
		// for each line
		while ((values = cr.readNext()) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("\n\n\t\tHandling CSV line #%d: %s\n\n",lineCounter,Arrays.toString(values)));
			}
			// A: collect all information
			InsertObservation[] ios = getInsertObservations(values,mVCols,dataFile,lineCounter);
			insertObservationsForOneLine(ios,values);
			lineCounter++;
			if (logger.isDebugEnabled()) {
				logger.debug(Feeder.heapSizeInformation());
			}
		}
		return failedInsertObservations;
	}

	private InsertObservation[] getInsertObservations(String[] values,
			int[] mVColumns,
			DataFile df,
			int currentLine){
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getInsertObservations(%s, %s)",
					Arrays.toString(values),
					Arrays.toString(mVColumns)));
		}
		if (values == null || values.length == 0 || mVColumns == null || mVColumns.length == 0) {
			logger.error(String.format("Method called with bad arguments: values: %s, mVColumns: %s",
					Arrays.toString(values),
					Arrays.toString(mVColumns)));
			return null;
		}
		ArrayList<InsertObservation> result = new ArrayList<InsertObservation>(mVColumns.length);
		for (int i = 0; i < mVColumns.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Parsing for measure value column %s",
						mVColumns[i]));
			}
			InsertObservation io = getInsertObservationForColumnIdFromValues(mVColumns[i],values,df);
			if (io != null) {
				result.add(io);
			}
		}
		result.trimToSize();
		return result.toArray(new InsertObservation[result.size()]);
	}

	private InsertObservation getInsertObservationForColumnIdFromValues(int mVColumnId,
			String[] values,
			DataFile df){
		if (logger.isTraceEnabled()) {
			logger.trace("getInsertObservationForColumnIdFromValues()");
		}
		try {
			// SENSOR
			Sensor sensor = df.getSensorForColumn(mVColumnId,values);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Sensor: %s", 
						sensor));
			}
			// FEATURE OF INTEREST incl. Position
			FeatureOfInterest foi = df.getFoiForColumn(mVColumnId,values);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Feature of Interest: %s",
						foi));
			}
			// VALUE
			Object value = df.getValue(mVColumnId,values);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Value: %s", value.toString()));
			}
			// TODO implement using different templates in later version depending on the class of value
			// TIMESTAMP
			String timeStamp = df.getTimeStamp(mVColumnId,values).toString();
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Timestamp: %s", timeStamp));
			}
			// UOM CODE
			UnitOfMeasurement uom = df.getUnitOfMeasurement(mVColumnId,values);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("UomCode: \"%s\"", uom));
			}
			// OBSERVED_PROPERTY
			ObservedProperty observedProperty = df.getObservedProperty(mVColumnId,values);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("ObservedProperty: %s", observedProperty));
			}
			Offering offer = df.getOffering(sensor);
			return new InsertObservation(sensor,
					foi,
					value,
					timeStamp,
					uom,
					observedProperty,
					offer,
					df.getType(mVColumnId));
		} catch (ParseException pe) {
			logger.error(String.format("Could not retrieve all information required for insert observation because of parsing error: %s: %s. Skipped this one.",
					pe.getClass().getName(),
					pe.getMessage()));
			if (logger.isDebugEnabled()) {
				logger.debug("Exception stack trace:",pe);
			}
			/*
			 catch (ParseException e) {
				logger.error(String.format("Could not parse values from data file \"%s\" with configuration \"%s\".\n" +
						"Raw data: %s.\n" +
						"Exception thrown: %s",
						dataFile.getFileName(),
						dataFile.getConfigurationFileName(),
						Arrays.toString(values),
						e.getMessage()),
						e);
			} 
			 */
		}
		return null;
	}

	private void insertObservationsForOneLine(InsertObservation[] ios, String[] values) throws OXFException, XmlException, IOException {
		insertObservationForALine:
			for (InsertObservation io : ios) {
				if (io != null) {
					if (!isSensorRegistered(io.getSensorURI())) {
						String assignedSensorId = registerSensor(new RegisterSensor(io),values);
						if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
							logger.error(String.format("Sensor \"%s\"[%s] could not be registered at SOS \"%s\". Skipping insert obsevation for this and store it.",
									io.getSensorName(),
									io.getSensorURI(),
									sosUrl.toExternalForm()));
							failedInsertObservations.add(io);
							continue insertObservationForALine;
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug(String.format("Sensor registered at SOS  \"%s\" with assigned id \"%s\"", 
										sosUrl.toExternalForm(),
										assignedSensorId));
							}
							registeredSensors.add(assignedSensorId);
						}
					}
					// sensor is registered -> insert the data
					String observationId = insertObservation(io);
					if (observationId == null || observationId.equalsIgnoreCase("")) {
						logger.error(String.format("Insert observation failed for sensor \"%s\"[%s]. Store: %s",
								io.getSensorName(),
								io.getSensorURI(),
								io));
						failedInsertObservations.add(io);
					} else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Observation was already contained in SOS: %s",
									io));
						}
					}
				}
			}
	}

	private String insertObservation(InsertObservation io) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("insertObservation()");
		}
		OperationResult opResult = null;
		InsertObservationResponseDocument response = null;
		InsertObservationParameterBuilder_v100 builder = null;
		
		try {
			builder = createParameterBuilderFromIO(io);
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("\n\nBEFORE OXF - doOperation \"InsertObservation\"\n\n");
				}
				opResult = sosWrapper.doInsertObservation(builder);
				if (logger.isDebugEnabled()) {
					logger.debug("\n\nAFTER OXF - doOperation \"InsertObservation\"\n\n");
				}
				if (sosVersion.equals("1.0.0")) {
					try {
						response = InsertObservationResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Observation inserted succesfully. Returned id: %s",
									response.getInsertObservationResponse().getAssignedObservationId()));
						}
						return response.getInsertObservationResponse().getAssignedObservationId();
					} catch (XmlException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						logger.error(String.format("Exception thrown: %s",e.getMessage()),e);
					} catch (IOException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						logger.error(String.format("Exception thrown: %s",e.getMessage()),e);
					}
				}
			} catch (ExceptionReport e) {
				// TODO Auto-generated catch block generated on 20.06.2012 around 10:40:38
				Iterator<OWSException> iter = e.getExceptionsIterator();
				StringBuffer buf = new StringBuffer();
				while (iter.hasNext()) {
					OWSException owsEx = iter.next();
					// check for observation already contained exception
					if (owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
							owsEx.getExceptionTexts().length > 0 &&
							owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) > -1) {
						return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
					}
					buf = buf.append(String.format("ExceptionCode: \"%s\" because of \"%s\"\n",
							owsEx.getExceptionCode(),
							Arrays.toString(owsEx.getExceptionTexts())));
				}
				logger.error(String.format("Exception thrown: %s\n%s",e.getMessage(),buf.toString()));
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(),e);
				}
			}
			
		} catch (OXFException e) {
			logger.error(String.format("Problem with OXF. Exception thrown: %s",e.getMessage()),e);
		}
        return null;
	}

	private InsertObservationParameterBuilder_v100 createParameterBuilderFromIO(
			InsertObservation io) throws OXFException {
		
		ObservationBuilder obsBuilder = null;
		
		// set text/category
		if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			obsBuilder = ObservationBuilder.createObservationForTypeCategory();
			((CategoryObservationBuilder) obsBuilder).addResultCodespace(io.getUnitOfMeasurementCode());
			((CategoryObservationBuilder) obsBuilder).addObservationValue(io.getValue().toString());
		} else if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			// set count (like measurement for now) TODO
			obsBuilder = ObservationBuilder.createObservationForTypeCount();
			((CountObservationBuilder) obsBuilder).addObservationValue((Integer) io.getValue());
		} else if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			// set boolean (like text/category) TODO
			obsBuilder = ObservationBuilder.createObservationForTypeBoolean();
			((BooleanObservationBuilder) obsBuilder).addObservationValue((Boolean) io.getValue());
		} else {
			// set default value type
			obsBuilder = ObservationBuilder.createObservationForTypeMeasurement();
			((MeasurementBuilder) obsBuilder).addUom(io.getUnitOfMeasurementCode());
			((MeasurementBuilder) obsBuilder).addObservationValue(io.getValue().toString());
		}
		obsBuilder.addOservedProperty(io.getObservedPropertyURI());
		obsBuilder.addFoiId(io.getFeatureOfInterestName());
		obsBuilder.addNewFoiName(io.getFeatureOfInterestURI());
		obsBuilder.addFoiDescription(io.getFeatureOfInterestURI());
		obsBuilder.addSrsPosition(Configuration.EPSG_CODE_PREFIX + io.getEpsgCode());
		// position
		boolean eastingFirst = false;
		if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) == null) {
			Configuration.EPSG_EASTING_FIRST_MAP.get("default");
		} else {
			eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode());
		}
		String pos = eastingFirst?
				String.format("%s %s",
				io.getLongitudeValue(),
				io.getLatitudeValue()) :
					String.format("%s %s",
							io.getLatitudeValue(),
							io.getLongitudeValue());
		obsBuilder.addFoiPosition(pos);
		obsBuilder.addOservedProperty(io.getObservedPropertyURI());
		obsBuilder.addSamplingTime(io.getTimeStamp());
		
		return new InsertObservationParameterBuilder_v100(io.getSensorURI(), obsBuilder);
	}

	private String registerSensor(RegisterSensor rs, String[] values) throws OXFException, XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("registerSensor()");
		}
		RegisterSensorParameterBuilder_v100 builder = createParameterBuilderFromRS(rs);
		try {
			OperationResult opResult = sosWrapper.doRegisterSensor(builder);
			if(sosVersion.equals("1.0.0")){
				RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
				if (logger.isDebugEnabled()) {
					logger.debug("RegisterSensorResponse parsed");
				}
				return response.getRegisterSensorResponse().getAssignedSensorId();
			}
		} catch (ExceptionReport e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:40
			// Handle already registered sensor case here (happens when the sensor is registered but not listed in the capabilities):
			Iterator<OWSException> iter = e.getExceptionsIterator();
			while(iter.hasNext()) {
				OWSException owsEx = iter.next();
				if (owsEx.getExceptionCode().equals(OwsExceptionCode.NoApplicableCode) && 
						owsEx.getExceptionTexts() != null &&
						owsEx.getExceptionTexts().length > 0) {
					for (String string : owsEx.getExceptionTexts()) {
						if (string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1 &&
								string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
							return rs.getSensorURI();
						}
					}
				}
						
			}
			logger.error(String.format("Exception thrown: %s",
					e.getMessage()),
					e);
		} catch (OXFException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:40
			logger.error(String.format("Exception thrown: %s",
					e.getMessage()),
					e);
		} catch (XmlException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:54
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		} catch (IOException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:54
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		}
		return null;
	}

	private RegisterSensorParameterBuilder_v100 createParameterBuilderFromRS(
			RegisterSensor registerSensor) throws OXFException, XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("createParameterContainterFromRS()");
		}
		
		// create SensorML
		SystemDocument sml = createSML(registerSensor);
		SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
		sensorMLDocument.addNewSensorML().addNewMember().set(sml);
		sensorMLDocument.getSensorML().setVersion("1.0.1"); // TODO version variable
		
        // create template
		ObservationTemplateBuilder observationTemplate;
		if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCategory(registerSensor.getUnitOfMeasurementCode());
		} else if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCount();
		} else if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeTruth();
		} else {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeMeasurement(registerSensor.getUnitOfMeasurementCode());
		}
		observationTemplate.setDefaultValue(registerSensor.getDefaultValue());
        
		return new RegisterSensorParameterBuilder_v100(sensorMLDocument.toString(), observationTemplate.generateObservationTemplate());
	}

	private SystemDocument createSML(RegisterSensor rs) throws XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("createSML()");
		}
		SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		
		// add all keywords
		builder.addKeyword(rs.getFeatureOfInterestName());
		builder.addKeyword(rs.getSensorName());
		builder.addKeyword(rs.getObservedPropertyName());
		
		// add all classifier
		builder.setClassifierIntendedApplication(rs.getObservedPropertyName());
		
		// add all identifier
		builder.setIdentifierUniqeId(rs.getSensorURI());
		builder.setIdentifierLongName(rs.getSensorName());
		builder.setIdentifierShortName(rs.getSensorName());
		
		// set capabilities - status
		builder.setCapabilityCollectingStatus("status", true);
		builder.addFeatureOfInterest(rs.getFeatureOfInterestName(), rs.getFeatureOfInterestURI());
		builder.setCapabilityBbox(rs.getLongitudeUnit(),
				rs.getLongitudeValue(), rs.getLatitudeUnit(),
				rs.getLatitudeValue(), rs.getLongitudeUnit(),
				rs.getLongitudeValue(), rs.getLatitudeUnit(),
				rs.getLatitudeValue());
		
		// set position data
		builder.setPosition("sensorPosition",
				SensorDescriptionBuilder.EPSG_CODE_PREFIX + rs.getEpsgCode(),
				"SYSTEM_LOCATION", rs.getLongitudeUnit(),
				rs.getLongitudeValue(), rs.getLatitudeUnit(),
				rs.getLatitudeValue(), rs.getAltitudeUnit(),
				rs.getAltitudeValue());
		
		// add inputs
		builder.addInput(rs.getObservedPropertyName(), rs.getObservedPropertyURI());
		
		// add outputs
		if (rs.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			builder.addOutputCategory(rs.getObservedPropertyName(),
					rs.getObservedPropertyURI(), rs.getOfferingUri(),
					rs.getOfferingName(), "");
		} else if (rs.getMvType().equals(
				Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			builder.addOutputBoolean(rs.getObservedPropertyName(),
					rs.getObservedPropertyURI(), rs.getOfferingUri(),
					rs.getOfferingName());
		} else if (rs.getMvType().equals(
				Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			builder.addOutputCount(rs.getObservedPropertyName(),
					rs.getObservedPropertyURI(), rs.getOfferingUri(),
					rs.getOfferingName());
		} else {
			builder.addOutputMeasurement(rs.getObservedPropertyName(),
					rs.getObservedPropertyURI(), rs.getOfferingUri(),
					rs.getOfferingName(), rs.getUnitOfMeasurementCode());
		}
		
		return builder.buildSensorDescription();
	}

	private boolean isSensorRegistered(String sensorURI) {
		if (logger.isTraceEnabled()) {
			logger.trace("isSensorRegistered()");
		}
		if (serviceDescriptor == null) {
			logger.error(String.format("Service descriptor not available for SOS \"%s\"", 
					sosUrl));
			return false;
		}
		SOSContents sosContent = (SOSContents) serviceDescriptor.getContents();
		String[] offeringIds = sosContent.getDataIdentificationIDArray();
		if (offeringIds != null) {
			for (String offeringId : offeringIds) {
				ObservationOffering offering = sosContent.getDataIdentification(offeringId);
				String[] sensorIds = offering.getProcedures();
				for (String sensorId : sensorIds) {
					if (sensorId.equals(sensorURI)) {
						return true;
					}
				}
			}
		} // check the list of newly registered sensors because the capabilities update might take to long to wait for
		if (registeredSensors != null && registeredSensors.size() > 0) {
			for (String sensorId : registeredSensors) {
				if (sensorId.equals(sensorURI)) {
					return true;
				}
			}
		}
		return false;
	}

}
