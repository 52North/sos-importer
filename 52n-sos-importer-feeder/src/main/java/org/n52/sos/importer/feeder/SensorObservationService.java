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

import net.opengis.gml.MetaDataPropertyType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList.Classifier;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.KeywordsDocument.Keywords.KeywordList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.RegisterSensorResponseDocument;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.BooleanDocument.Boolean;
import net.opengis.swe.x101.CategoryDocument.Category;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.TextDocument.Text;
import net.opengis.swe.x101.UomPropertyType;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.SOSRequestBuilderFactory;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.sos.util.SosUtil;
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
	private final SOSAdapter sosAdapter;
	private ServiceDescriptor serviceDescriptor;

	private ArrayList<String> registeredSensors;

	private ArrayList<InsertObservation> failedInsertObservations;
	
	public SensorObservationService(URL sosUrl) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("SensorObservationService(%s)", sosUrl));
		}
		this.sosUrl = sosUrl;
		this.sosVersion = "1.0.0";
		this.sosAdapter = new SOSAdapter(sosVersion);
		this.failedInsertObservations = new ArrayList<InsertObservation>();
		this.registeredSensors = new ArrayList<String>();
	}

	/**
	 * Tries to reach the SOS and returns false if failed.
	 * @return <code>true</code> if GetCapabilities operation is successful,<br />
	 * 				<code>false</code> if not
	 */
	public boolean isAvailable() {
		if (logger.isTraceEnabled()) {
			logger.trace("isAvailable()");
		}
        try {
            Operation getCapOperation = new Operation(SOSAdapter.GET_CAPABILITIES,
            		sosUrl + "?",
            		sosUrl.toString());
            OperationResult opResult = sosAdapter.doOperation(getCapOperation, createGetCapsParameterContainer());
            serviceDescriptor = sosAdapter.initService(opResult);
        } catch (ExceptionReport e) {
        	logger.error(String.format("Error while init SOS service: %s",e.getMessage()),e);
            return false;
        } catch (OXFException e) {
        	logger.error(String.format("Error with OXF while init SOS service: %s",e.getMessage()),e);
            return false;
        }
        return true;
	}

	private ParameterContainer createGetCapsParameterContainer() throws OXFException {
	    ParameterContainer paramCon = new ParameterContainer();
	    paramCon.addParameterShell(
	    		ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER,
	            sosVersion);
	    paramCon.addParameterShell(
	    		ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER,
	    		SosUtil.SERVICE_TYPE);
	    if (logger.isDebugEnabled()) {
	    	logger.debug(
	        			String.format("GetCapabilitiesRequest to %s:\n%s",
	        			sosUrl,
	        			sosAdapter.getRequestBuilder().
	        			buildGetCapabilitiesRequest(paramCon)
	        			)
	    			);
	    }
	    return paramCon;
	}

	/**
	 * Checks for <b>RegisterSensor</b> and <b>InsertObservation</b> operations.
	 * @return <code>true</code> if RegisterSensor and InsertObservation operations are listed in the capabilities of this SOS,
	 * 		<br />else <code>false</code>.
	 */
	public boolean isTransactional() {
		if (logger.isTraceEnabled()) {
			logger.trace("isTransactional()");
		}
		if (serviceDescriptor == null) {
			logger.error(String.format("Service descriptor not available for SOS \"%s\"", 
					sosUrl));
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
		ParameterContainer paramCon = null;
		OperationResult opResult = null;
		InsertObservationResponseDocument response = null;
		
		try {
			paramCon = createParameterContainterFromIO(io);
			if (logger.isDebugEnabled()) {
				logger.debug(
						String.format("InsertObservation request:\n%s\n",
									SOSRequestBuilderFactory.generateRequestBuilder(sosVersion).buildInsertObservation(paramCon)));
			}
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("\n\nBEFORE OXF - doOperation \"InsertObservation\"\n\n");
				}
				opResult = sosAdapter.doOperation(
						new Operation(SOSAdapter.INSERT_OBSERVATION,
								sosUrl.toExternalForm()+"?",
								sosUrl.toExternalForm()),
								paramCon);
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

	private ParameterContainer createParameterContainterFromIO(
			InsertObservation io) throws OXFException {
		ParameterContainer paramCon = new ParameterContainer();
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER,
		        sosVersion);
		paramCon.addParameterShell(
        		ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER,
        		SosUtil.SERVICE_TYPE);
		/*
		 * Feature Of Interest
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER,
				io.getFeatureOfInterestName());
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME,
				io.getFeatureOfInterestURI());
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC,
				io.getFeatureOfInterestURI());
		/*
		 * Position
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS,
				Configuration.EPSG_CODE_PREFIX + io.getEpsgCode());
		boolean eastingFirst = false;
		if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) == null) {
			Configuration.EPSG_EASTING_FIRST_MAP.get("default");
		} else {
			eastingFirst = 
					Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode());
		}
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION,
				( eastingFirst?
						String.format("%s %s",
						io.getLongitudeValue(),
						io.getLatitudeValue()) :
							String.format("%s %s",
									io.getLatitudeValue(),
									io.getLongitudeValue())
						)
				);
		/*
		 * Sensor
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER,
				io.getSensorURI());
		/*
		 * Observed Property
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
				io.getObservedPropertyURI());
		/*
		 * Unit Of Measurement Code
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE,
				io.getUnitOfMeasurementCode());
		/*
		 * Timestamp
		 */
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
				io.getTimeStamp());
		/*
		 * Value
		 */
		// if the parameter INSERT_OBSERVATION_TYPE is not set, the OXF uses measurement as default
		if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE,
					ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_CATEGORY);
			paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE,
					io.getUnitOfMeasurementCode());
		}
		paramCon.addParameterShell(
				ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER,
				io.getValue().toString());
		return paramCon;
	}

	private String registerSensor(RegisterSensor rs, String[] values) throws OXFException, XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("registerSensor()");
		}
		ParameterContainer paramCon = createParameterContainterFromRS(rs);
		try {

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("This RegisterSensor Request will be send to the SOS running at \"%s\":\n%s",
						sosUrl.toExternalForm(),
						sosAdapter.getRequestBuilder().buildRegisterSensor(paramCon)));
			}
			OperationResult opResult = sosAdapter.doOperation(
					new Operation(SOSAdapter.REGISTER_SENSOR,
							sosUrl.toExternalForm() + "?",
							sosUrl.toExternalForm()), paramCon);
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

	private ParameterContainer createParameterContainterFromRS(
			RegisterSensor registerSensor) throws OXFException, XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("createParameterContainterFromRS()");
		}
		ParameterContainer paramCon = new ParameterContainer();
		paramCon.addParameterShell(
				ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER,
				sosVersion);
        paramCon.addParameterShell(
        		ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER,
        		"SOS");
        paramCon.addParameterShell(
        		ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER,
        		createSML(registerSensor));
        // TODO implement handling of different types
        if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
        	paramCon.addParameterShell(
        			ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE,
        			ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY);
        	paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_CODESPACE_PARAMETER,
            		registerSensor.getUnitOfMeasurementCode());
        } else { // default: MEASUREMENT
        	paramCon.addParameterShell(
        			ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE,
        			ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
        	paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER,
            		registerSensor.getUnitOfMeasurementCode());
        }
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_DEFAULT_RESULT_VALUE,
        		registerSensor.getDefaultValue());
		return paramCon;
	}

	private String createSML(RegisterSensor rs) throws XmlException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("createSML()");
		}
		SystemDocument sysDoc = SystemDocument.Factory.newInstance();
		SystemType system = sysDoc.addNewSystem();
		addKeywords(system,rs);
		addIntendedApplicationClassifier(system,rs);
		addIdentification(system,rs);
		addCapabilities(system,rs);
		addPosition(system,rs);
		addInputs(system,rs);
		addOutputs(system,rs);
		return sysDoc.toString();
	}

	private void addIntendedApplicationClassifier(SystemType system, RegisterSensor rs) {
		Classifier intendedApplicationClassifier = system.addNewClassification().addNewClassifierList().addNewClassifier();
		Term intendedApplication = intendedApplicationClassifier.addNewTerm();
		intendedApplication.setDefinition(Configuration.OGC_DISCOVERY_INTENDED_APPLICATION_DEFINITION);
		intendedApplication.setValue(rs.getObservedPropertyName());
	}

	private void addKeywords(SystemType system, RegisterSensor rs) {
		KeywordList keywords = system.addNewKeywords().addNewKeywordList();
		keywords.addKeyword(rs.getFeatureOfInterestName());
		keywords.addKeyword(rs.getSensorName());
		keywords.addKeyword(rs.getObservedPropertyName());
	}

	private void addIdentification(SystemType system, RegisterSensor rs) {
		IdentifierList identifierList = system.addNewIdentification().addNewIdentifierList();
		
		Term uniqueId = identifierList.addNewIdentifier().addNewTerm();
		uniqueId.setDefinition(Configuration.OGC_DISCOVERY_ID_TERM_DEFINITION);
		uniqueId.setValue(rs.getSensorURI());
		
		Term longName = identifierList.addNewIdentifier().addNewTerm();
		longName.setDefinition(Configuration.OGC_DISCOVERY_LONG_NAME_DEFINITION);
		longName.setValue(rs.getSensorName());
		
		Term shortName = identifierList.addNewIdentifier().addNewTerm();
		shortName.setDefinition(Configuration.OGC_DISCOVERY_SHORT_NAME_DEFINITION);
		shortName.setValue(rs.getSensorName());
		
	}

	private void addCapabilities(SystemType system, RegisterSensor rs) {
		Capabilities caps = system.addNewCapabilities();
		DataRecordType dataRecordType;
	    DataComponentPropertyType f;
	    Boolean b;
	    Text t;
	    AbstractDataRecordType adrt = caps.addNewAbstractDataRecord();
	    dataRecordType = (DataRecordType) adrt.
	    		substitute(Configuration.QN_SWE_1_0_1_DATA_RECORD, 
	    				DataRecordType.type);
	    
	    // Status of the Sensor (collecting data?)
	    f =  dataRecordType.addNewField();
	    f.setName("status");
	    b = f.addNewBoolean();
	    b.setValue(true);
	    
	    // add foi id
	    f = dataRecordType.addNewField();
	    f.setName("FeatureOfInterestID");
	    t = f.addNewText();
	    t.setDefinition("FeatureOfInterest identifier");
	    t.setValue(rs.getFeatureOfInterestName());
	    
	    // add foi name
	    f = dataRecordType.addNewField();
	    f.setName("FeatureOfInterestName");
	    t = f.addNewText();
	    t.setValue(rs.getFeatureOfInterestURI());
	    
	    // add observed boundingbox
	    // TODO implement solution for moving sensors
	    addObservedBoundingBox(dataRecordType,rs);
	    
	    caps.setAbstractDataRecord(dataRecordType);
	    
	}

	private void addObservedBoundingBox(DataRecordType dRT,
			RegisterSensor rs) {
		DataComponentPropertyType observedBBoxField = dRT.addNewField();
		observedBBoxField.setName("observedBBOX");
		AbstractDataRecordType aDRT = observedBBoxField.addNewAbstractDataRecord();
		EnvelopeType envelope = (EnvelopeType) aDRT.substitute(Configuration.QN_SWE_1_0_1_ENVELOPE, EnvelopeType.type);
		envelope.setDefinition(Configuration.OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION);
		envelope.addNewLowerCorner().setVector(getLowerCornerOfObservedBBox(rs));
		envelope.addNewUpperCorner().setVector(getUpperCornerOfObservedBBox(rs));
	}

	// TODO implement for moving sensors
	private VectorType getUpperCornerOfObservedBBox(RegisterSensor rs) {
		return getLowerCornerOfObservedBBox(rs);
	}

	private VectorType getLowerCornerOfObservedBBox(RegisterSensor rs) {
		VectorType positionVector = VectorType.Factory.newInstance();
		Coordinate easting = positionVector.addNewCoordinate();
		easting.setName("easting");
		Quantity eastingValue = easting.addNewQuantity();
		eastingValue.setAxisID("x");
		eastingValue.addNewUom().setCode(rs.getLongitudeUnit());
		eastingValue.setValue(rs.getLongitudeValue());
		
		Coordinate northing = positionVector.addNewCoordinate();
		northing.setName("northing");
		Quantity northingValue = northing.addNewQuantity();
		northingValue.setAxisID("y");
		northingValue.addNewUom().setCode(rs.getLatitudeUnit());
		northingValue.setValue(rs.getLatitudeValue());
		
		return positionVector;
	}

	private void addPosition(SystemType system, RegisterSensor rs) {
		Position p;
	    PositionType pT;
	    VectorPropertyType loc;
	    VectorType vT;
	    Coordinate c;
	    Quantity q;
	    UomPropertyType uom;
	    // Position
	    p = system.addNewPosition();
	    p.setName("sensorPosition");
	    pT = p.addNewPosition();
	    pT.setReferenceFrame(Configuration.EPSG_CODE_PREFIX + rs.getEpsgCode());
	    loc = pT.addNewLocation();
	    
	    // Northing <> Y <> Latitude
	    vT = loc.addNewVector();
	    c = vT.addNewCoordinate();
	    c.setName("northing");
	    q = c.addNewQuantity();
	    q.setAxisID("y");
	    uom = q.addNewUom();
	    uom.setCode(rs.getLatitudeUnit());
	    q.setValue(rs.getLatitudeValue());
	    
	    // Easting
	    c = vT.addNewCoordinate();
	    c.setName("easting");
	    q = c.addNewQuantity();
	    q.setAxisID("x");
	    uom = q.addNewUom();
	    uom.setCode(rs.getLongitudeUnit());
	    q.setValue(rs.getLongitudeValue());
	    
	    // Altitude
	    c = vT.addNewCoordinate();
	    c.setName("altitude");
	    q = c.addNewQuantity();
	    q.setAxisID("z");
	    uom = q.addNewUom();
	    uom.setCode(rs.getAltitudeUnit());
	    q.setValue(rs.getAltitudeValue());
	}

	private void addInputs(SystemType system, RegisterSensor rs) {
		IoComponentPropertyType input = system.addNewInputs().addNewInputList().addNewInput();
		input.setName(rs.getObservedPropertyName());
		input.addNewObservableProperty().setDefinition(rs.getObservedPropertyURI());
	}

	private void addOutputs(SystemType system, RegisterSensor rs) {
		IoComponentPropertyType output = system.addNewOutputs().addNewOutputList().addNewOutput();
		output.setName(rs.getObservedPropertyName());
		if (rs.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			Category category = output.addNewCategory();
			category.setDefinition(rs.getObservedPropertyURI());
			addOfferingMetadata(category.addNewMetaDataProperty(),rs);
			category.addNewCodeSpace();
		} else {
			Quantity quantity = output.addNewQuantity();
			quantity.setDefinition(rs.getObservedPropertyURI());
			addOfferingMetadata(quantity.addNewMetaDataProperty(), rs);
			quantity.addNewUom().setCode(rs.getUnitOfMeasurementCode());
		}
	}

	private void addOfferingMetadata(MetaDataPropertyType addNewMetaDataProperty, RegisterSensor rs) {
		XmlCursor c = addNewMetaDataProperty.newCursor();
		c.toNextToken();
		c.beginElement(Configuration.QN_SOS_1_0_OFFERING);
		c.insertElementWithText(Configuration.QN_SOS_1_0_ID,rs.getOfferingUri());
		c.insertElementWithText(Configuration.QN_SOS_1_0_NAME,rs.getOfferingName());
		c.dispose();
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
