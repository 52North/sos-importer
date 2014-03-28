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
import static org.n52.sos.importer.feeder.Configuration.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.InsertObservationResponseDocument.InsertObservationResponse;
import net.opengis.sos.x10.RegisterSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.sos.observation.BooleanObservationParameters;
import org.n52.oxf.sos.observation.CountObservationParameters;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.n52.sos.importer.feeder.exceptions.InvalidColumnCountException;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;
import org.n52.sos.importer.feeder.util.DescriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Handles connection to SOS and provides an easy to use interface.<br />
 * Now this class supports only OGC SOS <b>1.0.0</b>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class SensorObservationService {

	private static final Logger LOG = LoggerFactory.getLogger(SensorObservationService.class);

	private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";

	private static final String OM_200_SAMPLING_FEATURE = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";

	private final URL sosUrl;
	private final String sosVersion;
	private final SOSWrapper sosWrapper;
	private final ServiceDescriptor serviceDescriptor;
	private final List<String> registeredSensors;
	private final List<InsertObservation> failedInsertObservations;
	private int lastLine = 0;
	private final Binding sosBinding;
	private Map<String, String> offerings;
	private final DescriptionBuilder sensorDescBuilder;

	private String[] headerLine;

	public SensorObservationService(final URL sosUrl, final String version, final String binding) throws ExceptionReport, OXFException {
		LOG.trace(String.format("SensorObservationService(%s)", sosUrl));
		this.sosUrl = sosUrl;
		sosVersion = version;
		sosBinding = getBinding(binding);
		sosWrapper = SosWrapperFactory.newInstance(sosUrl.toString(),sosVersion,sosBinding);
		serviceDescriptor = sosWrapper.getServiceDescriptor();
		if (sosVersion.equals("2.0.0")) {
			sensorDescBuilder = new DescriptionBuilder(false);
		} else {
			sensorDescBuilder = new DescriptionBuilder();
		}
		failedInsertObservations = new LinkedList<InsertObservation>();
		registeredSensors = new LinkedList<String>();
		if (sosVersion.equals("2.0.0")) {
			offerings = new HashMap<String, String>();
		}
	}

	private Binding getBinding(final String binding) throws OXFException {
		if (binding == null  || binding.isEmpty()) {
			return null;
		}
		if (binding.equals(Binding.POX.name())) {
			return Binding.POX;
		}
		if (binding.equals(Binding.SOAP.name())) {
			return Binding.SOAP;
		}
		throw new OXFException(String.format("Binding not supported by this implementation: %s",binding));
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
		LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
		// check for RegisterSensor and InsertObservationOperation
		// TODO implement version specific
		if ((opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null ||
				opMeta.getOperationByName(SOSAdapter.INSERT_SENSOR) != null)
				&&
				opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
			LOG.debug(String.format("Found all required operations: (%s|%s), %s",
					SOSAdapter.REGISTER_SENSOR,
					SOSAdapter.INSERT_SENSOR,
					SOSAdapter.INSERT_OBSERVATION));
			return true;
		}
		return false;
	}

	public List<InsertObservation> importData(final DataFile dataFile) throws IOException, OXFException, XmlException, IllegalArgumentException {
		LOG.trace("importData()");
		// 0 Get line
		final CSVReader cr = dataFile.getCSVReader();
		String[] values;
		int lineCounter = dataFile.getFirstLineWithData();
		if (dataFile.getHeaderLine() > -1 && headerLine == null) {
			headerLine = readHeaderLine(dataFile);
		}
		final int failedObservationsBefore = failedInsertObservations.size();
		int numOfObsTriedToInsert = 0;
		// 1 Get all measured value columns =: mvCols
		final int[] mVCols = dataFile.getMeasuredValueColumnIds();
		if (mVCols == null || mVCols.length == 0) {
			LOG.error("No measured value columns found in configuration");
			return null;
		}
		skipAlreadyReadLines(cr, lineCounter);
		// for each line
		while ((values = cr.readNext()) != null) {
			if (isNotEmpty(values) && isSizeValid(dataFile, values) && !isHeaderLine(values)) {
				LOG.debug(String.format("Handling CSV line #%d: %s",lineCounter+1,Arrays.toString(values)));
				// A: collect all information
				final InsertObservation[] ios = getInsertObservations(values,mVCols,dataFile,lineCounter);
				numOfObsTriedToInsert += ios.length;
				insertObservationsForOneLine(ios,values,dataFile);
				LOG.debug(Feeder.heapSizeInformation());
			} else {
				LOG.trace(String.format("\t\tSkip CSV line #%d: %s",(lineCounter+1),Arrays.toString(values)));
			}
			lastLine++;
			lineCounter++;
		}
		final int newFailedObservationsCount = failedInsertObservations.size()-failedObservationsBefore;
		final int newObservationsCount = numOfObsTriedToInsert-newFailedObservationsCount;
		LOG.info("New observations in SOS: {}. Failed observations: {}.", newObservationsCount,newFailedObservationsCount);
		return failedInsertObservations;
	}

	private void skipAlreadyReadLines(final CSVReader cr,
			int lineCounter) throws IOException {
		// get the number of lines to skip (coming from already read lines)
		String[] values;
		int skipCount = lastLine;
		while (skipCount > 0) {
			values = cr.readNext();
			LOG.trace(String.format("\t\tSkip CSV line #%d: %s",(lineCounter+1),Arrays.toString(values)));
			skipCount--;
			lineCounter++;
		}
	}

	private String[] readHeaderLine(final DataFile dataFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(
								dataFile.getCanonicalPath()),
								dataFile.getEncoding()))) {
			int counter = 1;
		    for(String line; (line = br.readLine()) != null; ) {
		    	if (counter++ == dataFile.getHeaderLine()) {
		    		return line.split(Character.toString(dataFile.getSeparatorChar()));
		    	}
		    }
		}
		return null;
	}

	private boolean isHeaderLine(final String[] values) {
		return Arrays.equals(headerLine, values);
	}

	private boolean isSizeValid(final DataFile dataFile,
			final String[] values)
	{
		if (values.length != dataFile.getExpectedColumnCount()) {
			final String errorMsg = String.format("Number of Expected columns '%s' does not match number of found columns '%s' -> Cancel import!",
					dataFile.getExpectedColumnCount(),
					values.length);
			LOG.error(errorMsg);
			throw new InvalidColumnCountException(errorMsg);
		}
		return true;
	}

	private boolean isNotEmpty(final String[] values)
	{
		if (values != null && values.length > 0) {
			for (final String value : values) {
				if (value == null || value.isEmpty()) {
					LOG.error("Current line '{}' contains empty values . Skipping this line!", Arrays.toString(values));
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private InsertObservation[] getInsertObservations(final String[] values,
			final int[] mVColumns,
			final DataFile df,
			final int currentLine){
		LOG.trace("getInsertObservations()");
		if (mVColumns == null || mVColumns.length == 0) {
			LOG.error("Method called with bad arguments: values: {}, mVColumns: {}", Arrays.toString(values), Arrays.toString(mVColumns));
			return null;
		}
		final ArrayList<InsertObservation> result = new ArrayList<InsertObservation>(mVColumns.length);
		for (final int mVColumn : mVColumns) {
			LOG.debug("Parsing measured value column {}",mVColumn);
			try {
				final InsertObservation io = getInsertObservationForColumnIdFromValues(mVColumn,values,df);
				if (io != null) {
					result.add(io);
				}
			} catch (final ParseException pe) {
				logExceptionThrownDuringParsing(pe);
			} catch (final NumberFormatException nfe) {
				logExceptionThrownDuringParsing(nfe);
			}
		}
		result.trimToSize();
		return result.toArray(new InsertObservation[result.size()]);
	}

	private InsertObservation getInsertObservationForColumnIdFromValues(final int mVColumnId,
			final String[] values,
			final DataFile dataFile) throws ParseException{
		LOG.trace("getInsertObservationForColumnIdFromValues()");
		// SENSOR
		final Sensor sensor = dataFile.getSensorForColumn(mVColumnId,values);
		LOG.debug("Sensor: {}",sensor);
		// FEATURE OF INTEREST incl. Position
		final FeatureOfInterest foi = dataFile.getFoiForColumn(mVColumnId,values);
		LOG.debug("Feature of Interest: {}",foi);
		// VALUE
		final Object value = dataFile.getValue(mVColumnId,values);
		LOG.debug("Value: {}", value.toString());
		// TODO implement using different templates in later version depending on the class of value
		// TIMESTAMP
		final String timeStamp = dataFile.getTimeStamp(mVColumnId,values).toString();
		LOG.debug("Timestamp: {}", timeStamp);
		// UOM CODE
		final UnitOfMeasurement uom = dataFile.getUnitOfMeasurement(mVColumnId,values);
		LOG.debug("UomCode: '{}'", uom);
		// OBSERVED_PROPERTY
		final ObservedProperty observedProperty = dataFile.getObservedProperty(mVColumnId,values);
		LOG.debug("ObservedProperty: {}", observedProperty);
		final Offering offer = dataFile.getOffering(sensor);
		LOG.debug("Offering: {}", offer);
		return new InsertObservation(sensor,
				foi,
				value,
				timeStamp,
				uom,
				observedProperty,
				offer,
				dataFile.getType(mVColumnId));
	}

	private void logExceptionThrownDuringParsing(final Exception exception) {
		LOG.error("Could not retrieve all information required for insert observation because of parsing error: {}: {}. Skipped this one.",
				exception.getClass().getName(),
				exception.getMessage());
		LOG.debug("Exception stack trace:",exception);
	}

	private void insertObservationsForOneLine(final InsertObservation[] ios, final String[] values, final DataFile dataFile) throws OXFException, XmlException, IOException {
		insertObservationForALine:
			for (final InsertObservation io : ios) {
				if (io != null) {
					if (!isSensorRegistered(io.getSensorURI())) {
						final RegisterSensor rs = new RegisterSensor(io,
								getObservedProperties(io.getSensorURI(),ios),
								getMeasuredValueTypes(io.getSensorURI(),ios),
								getUnitsOfMeasurement(io.getSensorURI(),ios));
						final String assignedSensorId = registerSensor(rs,values);
						if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
							LOG.error(String.format("Sensor '%s'[%s] could not be registered at SOS '%s'. Skipping insert observation for this and store it.",
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

	private Map<ObservedProperty, String> getUnitsOfMeasurement(final String sensorURI,
			final InsertObservation[] ios)
	{
		LOG.trace("getUnitsOfMeasurement(...)");
		final Map<ObservedProperty,String> unitsOfMeasurement = new HashMap<ObservedProperty, String>(ios.length);
		for (final InsertObservation insertObservation : ios) {
			if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI))
			{
				unitsOfMeasurement.put(insertObservation.getObservedProperty(), insertObservation.getUnitOfMeasurementCode());
			}
		}
		LOG.debug(String.format("Found '%d' units of measurement for observed properties of sensor '%s': '%s'.",
				unitsOfMeasurement.size(),sensorURI, unitsOfMeasurement));
		return unitsOfMeasurement;
	}

	private Map<ObservedProperty, String> getMeasuredValueTypes(final String sensorURI, final InsertObservation[] ios)
	{
		LOG.trace("getMeasuredValueTypes(...)");
		final Map<ObservedProperty,String> measuredValueTypes = new HashMap<ObservedProperty, String>(ios.length);
		for (final InsertObservation insertObservation : ios) {
			if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI))
			{
				measuredValueTypes.put(insertObservation.getObservedProperty(), insertObservation.getMeasuredValueType());
			}
		}
		LOG.debug(String.format("Found '%d' Measured value types for observed properties of sensor '%s': '%s'.",
				measuredValueTypes.size(),sensorURI, measuredValueTypes));
		return measuredValueTypes;
	}

	private Collection<ObservedProperty> getObservedProperties(final String sensorURI, final InsertObservation[] ios)
	{
		LOG.trace("getObservedProperties(...)");
		final Set<ObservedProperty> observedProperties = new HashSet<ObservedProperty>(ios.length);
		for (final InsertObservation insertObservation : ios) {
			if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI))
			{
				observedProperties.add(insertObservation.getObservedProperty());
			}
		}
		LOG.debug(String.format("Found '%d' Observed Properties for Sensor '%s': '%s'",observedProperties.size(),sensorURI,observedProperties));
		return observedProperties;
	}

	private String insertObservation(final InsertObservation io) throws IOException {
		LOG.trace("insertObservation()");
		OperationResult opResult = null;
		org.n52.oxf.sos.request.InsertObservationParameters parameters = null;

		try {
			parameters = createParameterAssemblyFromIO(io);
			try {
				LOG.debug("\tBEFORE OXF - doOperation 'InsertObservation'");
				opResult = sosWrapper.doInsertObservation(parameters);
				LOG.debug("\tAFTER OXF - doOperation 'InsertObservation'");
				if (sosVersion.equals("1.0.0")) {
					try {
						final InsertObservationResponse response = InsertObservationResponseDocument.Factory.parse(opResult.getIncomingResultAsStream()).getInsertObservationResponse();
						LOG.debug(String.format("Observation inserted succesfully. Returned id: %s",
								response.getAssignedObservationId()));
						return response.getAssignedObservationId();
					} catch (final XmlException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						LOG.error(String.format("Exception thrown: %s",e.getMessage()),e);
					} catch (final IOException e) {
						// TODO Auto-generated catch block generated on 20.06.2012 around 10:43:01
						LOG.error(String.format("Exception thrown: %s",e.getMessage()),e);
					}
				}
				else if (sosVersion.equals("2.0.0")) {
					try {
						net.opengis.sos.x20.InsertObservationResponseDocument.Factory.parse(opResult.getIncomingResultAsStream()).getInsertObservationResponse();
						LOG.debug("Observation inserted successfully.");
						return "SOS 2.0 InsertObservation doesn't return the assigned id";
					} catch (final XmlException e) {
						LOG.error("Exception thrown: {}", e.getMessage(), e);
					}
				}
			} catch (final ExceptionReport e) {
				final Iterator<OWSException> iter = e.getExceptionsIterator();
				StringBuffer buf = new StringBuffer();
				while (iter.hasNext()) {
					final OWSException owsEx = iter.next();
					// check for observation already contained exception
					// TODO update to latest 52nSOS 4.0x message
					if (owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
							owsEx.getExceptionTexts().length > 0 &&
							(owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) > -1
									||
									owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED) > -1
									||
									owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT) > -1)) {
						return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
					}
					buf = buf.append(String.format("ExceptionCode: '%s' because of '%s'\n",
							owsEx.getExceptionCode(),
							Arrays.toString(owsEx.getExceptionTexts())));
				}
				// TODO improve logging here:
				// add logOwsEceptionReport static util method to OxF or
				// some OER report logger which has unit tests
				LOG.error(String.format("Exception thrown: %s\n%s",e.getMessage(),buf.toString()));
				LOG.debug(e.getMessage(),e);
			}

		} catch (final OXFException e) {
			LOG.error(String.format("Problem with OXF. Exception thrown: %s",e.getMessage()),e);
		}
        return null;
	}

	private org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
			final InsertObservation io) throws OXFException {

		LOG.trace("createParameterBuilderFromIO()");
		ObservationParameters obsParameter = null;

		if (io.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
			// set text
			obsParameter = new TextObservationParameters();
			((TextObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
		} else if (io.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
			// set count
			obsParameter = new CountObservationParameters();
			((CountObservationParameters) obsParameter).addObservationValue((Integer) io.getResultValue());
		} else if (io.getMeasuredValueType().equals(Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
			// set boolean
			obsParameter = new BooleanObservationParameters();
			((BooleanObservationParameters) obsParameter).addObservationValue((Boolean) io.getResultValue());
		} else {
			// set default value type
			obsParameter = new MeasurementObservationParameters();
			((MeasurementObservationParameters) obsParameter).addUom(io.getUnitOfMeasurementCode());
			((MeasurementObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
		}
		obsParameter.addObservedProperty(io.getObservedPropertyURI());
		obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
		obsParameter.addNewFoiName(io.getFeatureOfInterestName());
		obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
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
		if (io.isSetAltitudeValue()) {
			pos = String.format("%s %s", pos, io.getAltitudeValue());
		}
		obsParameter.addFoiPosition(pos);
		obsParameter.addObservedProperty(io.getObservedPropertyURI());
		obsParameter.addProcedure(io.getSensorURI());

		if (sosVersion.equalsIgnoreCase("2.0.0")) {
			obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + io.getEpsgCode());
			obsParameter.addPhenomenonTime(io.getTimeStamp());
			obsParameter.addResultTime(io.getTimeStamp());
			return new org.n52.oxf.sos.request.v200.InsertObservationParameters(obsParameter, Collections.singletonList(io.getOffering().getUri()));
		}

		obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + io.getEpsgCode());
		obsParameter.addSamplingTime(io.getTimeStamp());
		return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
	}

	private String getOfferingForSensor(final InsertObservation io)	{
		if (offerings != null && offerings.containsKey(io.getSensorURI())){
			return offerings.get(io.getSensorURI());
		}
		final SOSContents sosContent = (SOSContents) serviceDescriptor.getContents();
		final String[] offeringIds = sosContent.getDataIdentificationIDArray();
		if (offeringIds != null) {
			for (final String offeringId : offeringIds) {
				final ObservationOffering offering = sosContent.getDataIdentification(offeringId);
				final String[] sensorIds = offering.getProcedures();
				for (final String sensorId : sensorIds) {
					if (sensorId.equals(io.getSensorURI())) {
						return offering.getIdentifier();
					}
				}
			}
		}
		return null;
	}

	private String registerSensor(final RegisterSensor rs, final String[] values) throws OXFException, XmlException, IOException {
		try {
			if(sosVersion.equals("1.0.0")) {
				LOG.trace("registerSensor()");
				final RegisterSensorParameters regSensorParameter = createRegisterSensorParametersFromRS(rs);
				final OperationResult opResult = sosWrapper.doRegisterSensor(regSensorParameter);
				final RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
				LOG.debug("RegisterSensorResponse parsed");
				return response.getRegisterSensorResponse().getAssignedSensorId();
			}
			else if (sosVersion.equals("2.0.0")) {
				LOG.trace("insertSensor()");
				final InsertSensorParameters insSensorParams = createInsertSensorParametersFromRS(rs);
				if (sosBinding != null) {
					insSensorParams.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
				}
				final OperationResult opResult = sosWrapper.doInsertSensor(insSensorParams);
				final InsertSensorResponseDocument response = InsertSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsStream());
				LOG.debug("InsertSensorResponse parsed");
				offerings.put(response.getInsertSensorResponse().getAssignedProcedure(),response.getInsertSensorResponse().getAssignedOffering());
				return response.getInsertSensorResponse().getAssignedProcedure();
			}
		} catch (final ExceptionReport e) {
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
				// handle offering already contained case here
				else if (owsEx.getExceptionCode().equals(OwsExceptionCode.InvalidParameterValue.name()) &&
						owsEx.getLocator().equals("offeringIdentifier") &&
						owsEx.getExceptionTexts() != null &&
						owsEx.getExceptionTexts().length > 0) {
					for (final String string : owsEx.getExceptionTexts()) {
						if (string.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) > -1 &&
								string.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END) > -1) {
							offerings.put(rs.getSensorURI(), rs.getOfferingUri());
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

	private InsertSensorParameters createInsertSensorParametersFromRS(final RegisterSensor rs) throws XmlException, IOException
	{
		return new InsertSensorParameters(sensorDescBuilder.createSML(rs),
				SML_101_FORMAT_URI,
				getObservedPropertyURIs(rs.getObservedProperties()),
				Collections.singleton(OM_200_SAMPLING_FEATURE),
				getObservationTypeURIs(rs));
	}

	private Collection<String> getObservationTypeURIs(final RegisterSensor rs)
	{
		if (rs == null || rs.getObservedProperties() == null || rs.getObservedProperties().size() < 1) {
			return Collections.emptyList();
		}
		final Set<String> tmp = new HashSet<String>(rs.getObservedProperties().size());
		for (final ObservedProperty obsProp : rs.getObservedProperties()) {
			final String measuredValueType = rs.getMeasuredValueType(obsProp);
			if (measuredValueType != null) {
				tmp.add(getURIForObservationType(measuredValueType));
			}
		}
		return tmp;
	}

	private String getURIForObservationType(final String measuredValueType)
	{
		if (measuredValueType.equals("NUMERIC")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
		}
		if (measuredValueType.equals("COUNT")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
		}
		if (measuredValueType.equals("BOOLEAN")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
		}
		if (measuredValueType.equals("TEXT")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
		}
		final String errorMsg = String.format("Observation type '%s' not supported!", measuredValueType);
		LOG.error(errorMsg);
		throw new IllegalArgumentException(errorMsg);
	}

	private Collection<String> getObservedPropertyURIs(final Collection<ObservedProperty> observedProperties)
	{
		if (observedProperties == null || observedProperties.size() < 1) {
			return Collections.emptyList();
		}
		final Collection<String> result = new ArrayList<String>(observedProperties.size());
		for (final ObservedProperty observedProperty : observedProperties) {
			result.add(observedProperty.getUri());
		}
		return result;
	}

	private RegisterSensorParameters createRegisterSensorParametersFromRS(
			final RegisterSensor registerSensor) throws OXFException, XmlException, IOException {
		LOG.trace("createParameterContainterFromRS()");

		// create SensorML
		// create template --> within the 52N 1.0.0 SOS implementation this template is somehow ignored --> take first observed property to get values for template
		ObservationTemplateBuilder observationTemplate;
		final ObservedProperty firstObservedProperty = registerSensor.getObservedProperties().iterator().next();
		if (registerSensor.getMeasuredValueType(firstObservedProperty).equals(SOS_OBSERVATION_TYPE_TEXT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeText();
		} else if (registerSensor.getMeasuredValueType(firstObservedProperty).equals(SOS_OBSERVATION_TYPE_COUNT)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCount();
		} else if (registerSensor.getMeasuredValueType(firstObservedProperty).equals(SOS_OBSERVATION_TYPE_BOOLEAN)) {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeTruth();
		} else {
			observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeMeasurement(registerSensor.getUnitOfMeasurementCode(firstObservedProperty));
		}
		observationTemplate.setDefaultValue(registerSensor.getDefaultValue());

		return new RegisterSensorParameters(sensorDescBuilder.createSML(registerSensor), observationTemplate.generateObservationTemplate());
	}

	private boolean isSensorRegistered(final String sensorURI) {
		LOG.trace(format("isSensorRegistered(%s)",sensorURI));
		if (serviceDescriptor == null) {
			LOG.error(String.format("Service descriptor not available for SOS '%s'",
					sosUrl));
			return false;
		}
		// 0 check operation metadata of DescribeSensor
//		if (serviceDescriptor.getOperationsMetadata() != null &&
//				serviceDescriptor.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR) != null &&
//				serviceDescriptor.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameters() != null &&
//				serviceDescriptor.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameters().size() > 0)
//		{
//			for (final Parameter parameter : serviceDescriptor.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameters()) {
//				if (((parameter.getCommonName() != null && parameter.getCommonName().equals("procedure")) || (parameter.getServiceSidedName() != null && parameter.getServiceSidedName().equals("procedure"))) &&
//						parameter.getValueDomain().containsValue(sensorURI)) {
//					return true;
//				}
//			}
//		}

		// 1 check if offering is available
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
		}
		// 2 check the list of newly registered sensors because the capabilities update might take to long to wait for
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
