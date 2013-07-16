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

import static java.lang.String.format;

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
import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.sos.request.observation.BooleanObservationParameters;
import org.n52.oxf.sos.request.observation.CountObservationParameters;
import org.n52.oxf.sos.request.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.request.observation.ObservationParameters;
import org.n52.oxf.sos.request.observation.TextObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
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
	
	private static final Logger LOG = Logger.getLogger(SensorObservationService.class);
	
	private final URL sosUrl;
	private final String sosVersion;
	private final SOSWrapper sosWrapper;
	private final ServiceDescriptor serviceDescriptor;

	private final ArrayList<String> registeredSensors;

	private final ArrayList<InsertObservation> failedInsertObservations;
	
	private int lastLine = 0;
	
	public SensorObservationService(final URL sosUrl) throws ExceptionReport, OXFException {
		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("SensorObservationService(%s)", sosUrl));
		}
		this.sosUrl = sosUrl;
		sosVersion = "1.0.0"; // TODO get from configuration
		sosWrapper = SOSWrapper.createFromCapabilities(sosUrl.toString(), sosVersion);
		serviceDescriptor = sosWrapper.getServiceDescriptor();
		failedInsertObservations = new ArrayList<InsertObservation>();
		registeredSensors = new ArrayList<String>();
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
		LOG.trace("isTransactional()");
		if (serviceDescriptor == null) {
			LOG.error(String.format("Service descriptor not available for SOS '%s'", sosUrl));
			return false;
		}
		final OperationsMetadata opMeta = serviceDescriptor.getOperationsMetadata();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
		}
		// check for RegisterSensor and InsertObservationOperation
		// TODO implement version specific
		if (opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null &&
				opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
			LOG.debug(String.format("Found all required operations: %s, %s",
					SOSAdapter.REGISTER_SENSOR,
					SOSAdapter.INSERT_OBSERVATION));
			return true;
		}
		return false;
	}

	public ArrayList<InsertObservation> importData(final DataFile dataFile) throws IOException, OXFException, XmlException {
		LOG.trace("importData()");
		// 0 Get line
		final CSVReader cr = dataFile.getCSVReader();
		String[] values;
		int lineCounter = dataFile.getFirstLineWithData();
		// 1 Get all measured value columns =: mvCols
		final int[] mVCols = dataFile.getMeasuredValueColumnIds();
		if (mVCols == null || mVCols.length == 0) {
			LOG.fatal("No measured value columns found in configuration");
			return null;
		}
		// get the number of lines to skip (coming from already read lines)
		int skipCount = lastLine;
		// for each line
		while ((values = cr.readNext()) != null) {
			/*
			 * check if the decreasing skip counter indicates, that all lines
			 * marked for skipping are already skipped
			 */
			if (skipCount < 1 && isNotEmpty(values)) {
				LOG.debug(String.format("\n\n\t\tHandling CSV line #%d: %s\n\n",lineCounter,Arrays.toString(values)));
				// A: collect all information
				final InsertObservation[] ios = getInsertObservations(values,mVCols,dataFile,lineCounter);
				insertObservationsForOneLine(ios,values);
				lineCounter++; // line counter overreads failed insert observations
				LOG.debug(Feeder.heapSizeInformation());
				/*
				 * The lastline counter initially was set to the last known
				 * number of read lines. Reaching this part of code means, that
				 * new lines will be read and thus the counter has to be
				 * increased.
				 */
				lastLine++;
			} else {
				LOG.debug(String.format("\n\n\t\tSkip CSV line #%d: %s\n\n",lineCounter,Arrays.toString(values)));
			}
			skipCount--;
		}
		return failedInsertObservations;
	}

	private boolean isNotEmpty(final String[] values)
	{
		if (values != null && values.length > 0) {
			for (final String value : values) {
				if (value != null && !value.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	private InsertObservation[] getInsertObservations(final String[] values,
			final int[] mVColumns,
			final DataFile df,
			final int currentLine){
		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("getInsertObservations(%s, %s)",
					Arrays.toString(values),
					Arrays.toString(mVColumns)));
		}
		if (values == null || values.length == 0 || mVColumns == null || mVColumns.length == 0) {
			LOG.error(String.format("Method called with bad arguments: values: %s, mVColumns: %s",
					Arrays.toString(values),
					Arrays.toString(mVColumns)));
			return null;
		}
		final ArrayList<InsertObservation> result = new ArrayList<InsertObservation>(mVColumns.length);
		for (final int mVColumn : mVColumns) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Parsing for measure value column %s",
						mVColumn));
			}
			final InsertObservation io = getInsertObservationForColumnIdFromValues(mVColumn,values,df);
			if (io != null) {
				result.add(io);
			}
		}
		result.trimToSize();
		return result.toArray(new InsertObservation[result.size()]);
	}

	private InsertObservation getInsertObservationForColumnIdFromValues(final int mVColumnId,
			final String[] values,
			final DataFile df){
		LOG.trace("getInsertObservationForColumnIdFromValues()");
		try {
			// SENSOR
			final Sensor sensor = df.getSensorForColumn(mVColumnId,values);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Sensor: %s", 
						sensor));
			}
			// FEATURE OF INTEREST incl. Position
			final FeatureOfInterest foi = df.getFoiForColumn(mVColumnId,values);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Feature of Interest: %s",
						foi));
			}
			// VALUE
			final Object value = df.getValue(mVColumnId,values);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Value: %s", value.toString()));
			}
			// TODO implement using different templates in later version depending on the class of value
			// TIMESTAMP
			final String timeStamp = df.getTimeStamp(mVColumnId,values).toString();
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Timestamp: %s", timeStamp));
			}
			// UOM CODE
			final UnitOfMeasurement uom = df.getUnitOfMeasurement(mVColumnId,values);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("UomCode: '%s'", uom));
			}
			// OBSERVED_PROPERTY
			final ObservedProperty observedProperty = df.getObservedProperty(mVColumnId,values);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("ObservedProperty: %s", observedProperty));
			}
			final Offering offer = df.getOffering(sensor);
			return new InsertObservation(sensor,
					foi,
					value,
					timeStamp,
					uom,
					observedProperty,
					offer,
					df.getType(mVColumnId));
		} catch (final ParseException pe) {
			LOG.error(String.format("Could not retrieve all information required for insert observation because of parsing error: %s: %s. Skipped this one.",
					pe.getClass().getName(),
					pe.getMessage()));
			if (LOG.isDebugEnabled()) {
				LOG.debug("Exception stack trace:",pe);
			}
			/*
			 catch (ParseException e) {
				LOG.error(String.format("Could not parse values from data file '%s' with configuration '%s'.\n" +
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

	private void insertObservationsForOneLine(final InsertObservation[] ios, final String[] values) throws OXFException, XmlException, IOException {
		insertObservationForALine:
			for (final InsertObservation io : ios) {
				if (io != null) {
					if (!isSensorRegistered(io.getSensorURI())) {
						final String assignedSensorId = registerSensor(new RegisterSensor(io),values);
						if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
							LOG.error(String.format("Sensor '%s'[%s] could not be registered at SOS '%s'. Skipping insert obsevation for this and store it.",
									io.getSensorName(),
									io.getSensorURI(),
									sosUrl.toExternalForm()));
							failedInsertObservations.add(io);
							continue insertObservationForALine;
						} else {
							LOG.debug(String.format("Sensor registered at SOS  '%s' with assigned id '%s'", 
									sosUrl.toExternalForm(),
									assignedSensorId));
							registeredSensors.add(assignedSensorId);
						}
					}
					// sensor is registered -> insert the data
					final String observationId = insertObservation(io);
					if (observationId == null || observationId.equalsIgnoreCase("")) {
						LOG.error(String.format("Insert observation failed for sensor '%s'[%s]. Store: %s",
								io.getSensorName(),
								io.getSensorURI(),
								io));
						failedInsertObservations.add(io);
					} else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
						LOG.debug(String.format("Observation was already contained in SOS: %s",
								io));
					}
				}
			}
	}

	private String insertObservation(final InsertObservation io) throws IOException {
		LOG.trace("insertObservation()");
		OperationResult opResult = null;
		InsertObservationResponseDocument response = null;
		InsertObservationParameterBuilder_v100 builder = null;
		
		try {
			builder = createParameterBuilderFromIO(io);
			try {
				LOG.debug("\n\nBEFORE OXF - doOperation \"InsertObservation\"\n\n");
				opResult = sosWrapper.doInsertObservation(builder);
				LOG.debug("\n\nAFTER OXF - doOperation \"InsertObservation\"\n\n");
				if (sosVersion.equals("1.0.0")) {
					try {
						response = InsertObservationResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
						LOG.debug(String.format("Observation inserted succesfully. Returned id: %s",
								response.getInsertObservationResponse().getAssignedObservationId()));
						return response.getInsertObservationResponse().getAssignedObservationId();
					} catch (final XmlException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						LOG.error(String.format("Exception thrown: %s",e.getMessage()),e);
					} catch (final IOException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						LOG.error(String.format("Exception thrown: %s",e.getMessage()),e);
					}
				}
			} catch (final ExceptionReport e) {
				// TODO Auto-generated catch block generated on 20.06.2012 around 10:40:38
				final Iterator<OWSException> iter = e.getExceptionsIterator();
				StringBuffer buf = new StringBuffer();
				while (iter.hasNext()) {
					final OWSException owsEx = iter.next();
					// check for observation already contained exception
					if (owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
							owsEx.getExceptionTexts().length > 0 &&
							(owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) > -1
									||
									owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED) > -1
									)) {
						return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
					}
					buf = buf.append(String.format("ExceptionCode: '%s' because of '%s'\n",
							owsEx.getExceptionCode(),
							Arrays.toString(owsEx.getExceptionTexts())));
				}
				LOG.error(String.format("Exception thrown: %s\n%s",e.getMessage(),buf.toString()));
				LOG.debug(e.getMessage(),e);
			}
			
		} catch (final OXFException e) {
			LOG.error(String.format("Problem with OXF. Exception thrown: %s",e.getMessage()),e);
		}
        return null;
	}

	private InsertObservationParameterBuilder_v100 createParameterBuilderFromIO(
			final InsertObservation io) throws OXFException {
		
		LOG.trace("createParameterBuilderFromIO()");
		
		ObservationParameters obsParameter = null;
		
		if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			// set text
			obsParameter = new TextObservationParameters();
			((TextObservationParameters) obsParameter).addObservationValue(io.getValue().toString());
		} else if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			// set count
			obsParameter = new CountObservationParameters();
			((CountObservationParameters) obsParameter).addObservationValue((Integer) io.getValue());
		} else if (io.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			// set boolean
			obsParameter = new BooleanObservationParameters();
			((BooleanObservationParameters) obsParameter).addObservationValue((Boolean) io.getValue());
		} else {
			// set default value type
			obsParameter = new MeasurementObservationParameters();
			((MeasurementObservationParameters) obsParameter).addUom(io.getUnitOfMeasurementCode());
			((MeasurementObservationParameters) obsParameter).addObservationValue(io.getValue().toString());
		}
		obsParameter.addObservedProperty(io.getObservedPropertyURI());
		obsParameter.addFoiId(io.getFeatureOfInterestName());
		obsParameter.addNewFoiName(io.getFeatureOfInterestURI());
		obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
		obsParameter.addSrsPosition(Configuration.EPSG_CODE_PREFIX + io.getEpsgCode());
		// position
		boolean eastingFirst = false;
		if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) == null) {
			Configuration.EPSG_EASTING_FIRST_MAP.get("default");
		} else {
			eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode());
		}
		final String pos = eastingFirst?
				String.format("%s %s",
				io.getLongitudeValue(),
				io.getLatitudeValue()) :
					String.format("%s %s",
							io.getLatitudeValue(),
							io.getLongitudeValue());
		obsParameter.addFoiPosition(pos);
		obsParameter.addObservedProperty(io.getObservedPropertyURI());
		obsParameter.addSamplingTime(io.getTimeStamp());
		
		return new InsertObservationParameterBuilder_v100(io.getSensorURI(), obsParameter);
	}

	private String registerSensor(final RegisterSensor rs, final String[] values) throws OXFException, XmlException, IOException {
		LOG.trace("registerSensor()");
		final RegisterSensorParameters regSensorParameter = createParameterBuilderFromRS(rs);
		try {
			final OperationResult opResult = sosWrapper.doRegisterSensor(regSensorParameter);
			if(sosVersion.equals("1.0.0")){
				final RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
				LOG.debug("RegisterSensorResponse parsed");
				return response.getRegisterSensorResponse().getAssignedSensorId();
			}
		} catch (final ExceptionReport e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:40
			// Handle already registered sensor case here (happens when the sensor is registered but not listed in the capabilities):
			final Iterator<OWSException> iter = e.getExceptionsIterator();
			while(iter.hasNext()) {
				final OWSException owsEx = iter.next();
				if (owsEx.getExceptionCode().equals(OwsExceptionCode.NoApplicableCode.name()) && 
						owsEx.getExceptionTexts() != null &&
						owsEx.getExceptionTexts().length > 0) {
					for (final String string : owsEx.getExceptionTexts()) {
						if (string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1 &&
								string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
							return rs.getSensorURI();
						}
					}
				}
						
			}
			LOG.error(String.format("Exception thrown: %s",
					e.getMessage()),
					e);
		} catch (final OXFException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:40
			LOG.error(String.format("Exception thrown: %s",
					e.getMessage()),
					e);
		} catch (final XmlException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:54
			LOG.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		} catch (final IOException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around 14:53:54
			LOG.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		}
		return null;
	}

	private RegisterSensorParameters createParameterBuilderFromRS(
			final RegisterSensor registerSensor) throws OXFException, XmlException, IOException {
		LOG.trace("createParameterContainterFromRS()");
		
		// create SensorML
		final SystemDocument sml = createSML(registerSensor);
		final SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
		sensorMLDocument.addNewSensorML().addNewMember().set(sml);
		sensorMLDocument.getSensorML().setVersion("1.0.1"); // TODO version variable
		
        // create template
		ObservationTemplateBuilder observationTemplate;
		if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeText();
		} else if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCount();
		} else if (registerSensor.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeTruth();
		} else {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeMeasurement(registerSensor.getUnitOfMeasurementCode());
		}
		observationTemplate.setDefaultValue(registerSensor.getDefaultValue());
		
		return new RegisterSensorParameters(sensorMLDocument.toString(), observationTemplate.generateObservationTemplate());
	}

	private SystemDocument createSML(final RegisterSensor rs) throws XmlException, IOException {
		if (LOG.isTraceEnabled()) {
			LOG.trace("createSML()");
		}
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		
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
				"SYSTEM_LOCATION",
				rs.getLongitudeUnit(), rs.getLongitudeValue(),
				rs.getLatitudeUnit(), rs.getLatitudeValue(),
				rs.getAltitudeUnit(), rs.getAltitudeValue());
		
		// add inputs
		builder.addInput(rs.getObservedPropertyName(), rs.getObservedPropertyURI());
		
		// add outputs
		if (rs.getMvType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			builder.addOutputText(rs.getObservedPropertyName(),
					rs.getObservedPropertyURI(), rs.getOfferingUri(),
					rs.getOfferingName());
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

	private boolean isSensorRegistered(final String sensorURI) {
		LOG.trace(format("isSensorRegistered(%s)",sensorURI));
		if (serviceDescriptor == null) {
			LOG.error(String.format("Service descriptor not available for SOS '%s'", 
					sosUrl));
			return false;
		}
		final SOSContents sosContent = (SOSContents) serviceDescriptor.getContents();
		final String[] offeringIds = sosContent.getDataIdentificationIDArray();
		if (offeringIds != null) {
			for (final String offeringId : offeringIds) {
				final ObservationOffering offering = sosContent.getDataIdentification(offeringId);
				final String[] sensorIds = offering.getProcedures();
				for (final String sensorId : sensorIds) {
					if (sensorId.equals(sensorURI)) {
						return true;
					}
				}
			}
		} // check the list of newly registered sensors because the capabilities update might take to long to wait for
		if (registeredSensors != null && registeredSensors.size() > 0) {
			for (final String sensorId : registeredSensors) {
				if (sensorId.equals(sensorURI)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getLastLine() {
		return lastLine;
	}

	public void setLastLine(final int lastLine) {
		this.lastLine = lastLine;
	}

}
