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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;

import org.n52.oxf.xml.NcNameResolver;
import org.n52.sos.importer.feeder.exceptions.JavaApiBugJDL6203387Exception;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Resource;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x02.GeneratedResourceType;
import org.x52North.sensorweb.sos.importer.x02.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.ManualResourceType;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.ObservedPropertyType;
import org.x52North.sensorweb.sos.importer.x02.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x02.SensorType;
import org.x52North.sensorweb.sos.importer.x02.SpatialResourceType;
import org.x52North.sensorweb.sos.importer.x02.TypeDocument.Type;
import org.x52North.sensorweb.sos.importer.x02.UnitOfMeasurementType;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Class holds the datafile and provides easy to use interfaces to get certain
 * required resources.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class DataFile {

	private static final Logger LOG = LoggerFactory.getLogger(DataFile.class);

	private final Configuration configuration;

	private final File file;

	public DataFile(final Configuration configuration, final File file) {
		this.configuration = configuration;
		this.file = file;
	}

	/**
	 * Checks if the file is available and can be read. All errors like not
	 * available, not a file, and not readable are logged to
	 * <code>LOG.error()</code>.
	 * @return <code>true</code>, if the Datafile is a file and can be read,<br />
	 * 			else <code>false</code>.
	 */
	public boolean isAvailable() {
		LOG.trace("isAvailable()");
		if (!file.exists()) {
			LOG.error(String.format("Data file '%s' specified in '%s' does not exist.",
					file.getAbsolutePath(),
					configuration.getConfigFile().getAbsolutePath()));
		} else if (!file.isFile()){
			LOG.error(String.format("Data file '%s' is not a file!",
					file.getAbsolutePath()));
		} else if (!file.canRead()) {
			LOG.error(String.format("Data file '%s' can not be accessed, please check file permissions!",
					file.getAbsolutePath()));
		} else if (checkWindowsJavaApiBugJDK6203387(file)){
			LOG.error(String.format("Data file '%s' can not be accessed, because another process blocked read access!",
					file.getAbsolutePath()));
			throw new JavaApiBugJDL6203387Exception(file.getName());
		} else {
			LOG.debug(String.format("Data file '%s' is a file and read permission is available.",
						file.getAbsolutePath()));
			return true;
		}
		return false;
	}

	private boolean checkWindowsJavaApiBugJDK6203387(final File file)
	{
		if (isWindows()) {
			try {
				new FileReader(file);
			}
			catch (final FileNotFoundException fnfe) {
				// TODO add more language specific versions of this error message
				if (
						(
        					fnfe.getMessage().indexOf("Der Prozess kann nicht auf die Datei zugreifen, da sie von einem anderen Prozess verwendet wird")>=0
        					||
        					fnfe.getMessage().indexOf("The process cannot access the file because it is being used by another process")>=0
						)
					&&
						fnfe.getMessage().indexOf(file.getName()) >=0)
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isWindows() {
		return System.getProperty("os.name").indexOf("Windows")>=0||System.getProperty("os.name").indexOf("windows")>=0;
	}

	/**
	 * Returns a CSVReader instance for the current DataFile using the configuration
	 * including the defined values for: first line with data, separator, escape, and text qualifier.
	 * @return a <code>CSVReader</code> instance
	 * @throws FileNotFoundException
	 */
	public CSVReader getCSVReader() throws FileNotFoundException {
		LOG.trace("getCSVReader()");
		final FileReader fr = new FileReader(file);
		final BufferedReader br = new BufferedReader(fr);
		final int flwd = configuration.getFirstLineWithData();
		final char separator = configuration.getCsvSeparator(),
				quotechar = configuration.getCsvQuoteChar(),
				escape = configuration.getCsvEscape();
		final CSVReader cr = new CSVReader(br, separator, quotechar, escape, flwd);
		return cr;
	}

	/**
	 * @see {@link Configuration#getMeasureValueColumnIds()}
	 */
	public int[] getMeasuredValueColumnIds() {
		return configuration.getMeasureValueColumnIds();
	}

	/**
	 * @see {@link Configuration#getFirstLineWithData()}
	 */
	public int getFirstLineWithData() {
		return configuration.getFirstLineWithData();
	}

	/**
	 *
	 * @param mvColumnId
	 * @param values
	 * @return
	 */
	public Sensor getSensorForColumn(final int mvColumnId, final String[] values) {
		LOG.trace("getSensorForColumn({},{})", mvColumnId, Arrays.toString(values));
		// check for sensor column and return new sensor
		Sensor sensor = getSensorFromColumn(mvColumnId,values);
		if (sensor == null) {
			LOG.debug("Could not find sensor column for column id {}", mvColumnId);
		} else {
			return sensor;
		}
		// else build sensor from manual or generated resource
		SensorType sensorType = configuration.getRelatedSensor(mvColumnId);
		// Case: one mv column => no related sensor element => check for one single sensor in additional metadata
		if (sensorType == null && configuration.isOneMvColumn()) {
			sensorType = configuration.getSensorFromAdditionalMetadata();
		}
		if (sensorType != null && sensorType.getResource() != null) {
			// generated sensor
			if (sensorType.getResource() instanceof GeneratedResourceType) {
				final GeneratedResourceType gRT = (GeneratedResourceType) sensorType.getResource();
				final String[] a = getUriAndNameFromGeneratedResourceType(
						gRT.isSetConcatString()?gRT.getConcatString():null, // concatstring
						gRT.isSetURI()?gRT.getURI().getStringValue():null, // uri
						gRT.isSetURI()&&gRT.getURI().isSetUseAsPrefix()?gRT.getURI().getUseAsPrefix():false, // useUriAsPrefix
						gRT.getNumberArray(),
						values
						);
				sensor = new Sensor(a[0],a[1]);
			} else if (sensorType.getResource() instanceof ManualResourceType) {
				// manual sensor
				final ManualResourceType mRT = (ManualResourceType) sensorType.getResource();
				sensor = new Sensor(mRT.getName(),
						mRT.getURI().getStringValue());
			}
		}
		return sensor;
	}

	/**
	 *
	 * @param mvColumnId
	 * @param values
	 * @return
	 * @throws ParseException
	 */
	public FeatureOfInterest getFoiForColumn(final int mvColumnId, final String[] values) throws ParseException {
		LOG.trace(String.format("getFoiForColumn(%d,%s)",
					mvColumnId,
					Arrays.toString(values)));
		// check for foi column and return new sensor
		FeatureOfInterest foi = getFoiColumn(mvColumnId,values);
		if (foi == null) {
			LOG.debug(String.format("Could not find foi column for column id %d",
					mvColumnId));
		} else {
			return foi;
		}
		// else build foi from manual or generated resource
		final FeatureOfInterestType foiT = configuration.getRelatedFoi(mvColumnId);
		if (foiT != null && foiT.getResource() != null) {
			// generated sensor
			if (foiT.getResource() instanceof GeneratedSpatialResourceType) {
				final GeneratedSpatialResourceType gSRT =
						(GeneratedSpatialResourceType) foiT.getResource();
				final String[] a = getUriAndNameFromGeneratedResourceType(
						gSRT.isSetConcatString()?gSRT.getConcatString():null, // concatstring
								gSRT.isSetURI()?gSRT.getURI().getStringValue():null, // uri
										gSRT.isSetURI()&&gSRT.getURI().isSetUseAsPrefix()?gSRT.getURI().getUseAsPrefix():false, // useUriAsPrefix
												gSRT.getNumberArray(),
												values
						);
				final Position p = getPosition(gSRT.getPosition(),values);
				foi = new FeatureOfInterest(a[0],a[1],p);
			} else if (foiT.getResource() instanceof SpatialResourceType) {
				// manual sensor
				final SpatialResourceType mSRT = (SpatialResourceType) foiT.getResource();
				final Position p = getPosition(mSRT.getPosition(),values);
				foi = new FeatureOfInterest(mSRT.getName(),
						mSRT.getURI().getStringValue(),
						p);
			}
		}
		if (!NcNameResolver.isNCName(foi.getName())){
			final String[] a = createCleanNCName(foi);
			foi.setName(a[0]);
			if (!a[0].equals(a[1])) {
				LOG.debug(String.format("Feature Of Interest name changed to match NCName production: '%s' to '%s'",
						a[1],
						a[0]));
			}
		}
		return foi;
	}

	/**
	 * @return result[0] := newName<br /> result[1] := originaleName
	 */
	private String[] createCleanNCName(final Resource res) {
		// implement check for NCName compliance and remove bad values
		String name = res.getName();
		final String origName = new String(name);
		// clean rest of string using Constants.UNICODE_REPLACER
		final char[] foiNameChars = name.toCharArray();
		for (int i = 0; i < foiNameChars.length; i++) {
			final char c = foiNameChars[i];
			if (!NcNameResolver.isNCNameChar(c)) {
				foiNameChars[i] = Configuration.UNICODE_REPLACER;
			}
		}
		name = String.valueOf(foiNameChars);
		// check if name is only containing "_"
		final Matcher matcher = Configuration.UNICODE_ONLY_REPLACER_LEFT_PATTERN.matcher(name);
		if (matcher.matches()) {
			// if yes -> change to "className" + res.getUri().hashCode()
			name = res.getClass().getSimpleName().toLowerCase() + res.getUri().hashCode();
		}
		final String[] result = { name, origName };
		return result;
	}

	/**
	 *
	 * @param mVColumn
	 * @param values
	 * @return
	 */
	public Object getValue(final int mVColumn, final String[] values) throws ParseException {
		LOG.trace(String.format("getValue(%s,%s)",
				mVColumn,
				Arrays.toString(values)));
		final Column column = configuration.getColumnById(mVColumn);
		String value = values[mVColumn];
		for (final Metadata m : column.getMetadataArray()) {
			if (m.getKey().equals(Key.TYPE)) {
				// check various types of observation
				// TEXT
				if (m.getValue().equals("TEXT")) {
					return new String(value);
				}
				// text is done -> clean string before parsing to other types
				value = value.trim();
				// BOOLEAN
				if (m.getValue().equals("BOOLEAN")) {
					if (value.equalsIgnoreCase("0")) {
						value = "false";
					}
					else if (value.equalsIgnoreCase("1")) {
						value = "true";
					}
					return Boolean.parseBoolean(value);
				}
				// COUNT
				else if (m.getValue().equals("COUNT")) {
					return Integer.parseInt(value);
				}
				// NUMERIC
				else if (m.getValue().equals("NUMERIC")) {
					return configuration.parseToDouble(value);
				}
			}
		}
		return null;
	}

	/*
	 * {@link org.n52.sos.importer.controller.DateAndTimeController#assignPattern()}
	 * {@link org.n52.sos.importer.controller.DateAndTimeController#forThis()}
	 * {@link {@link org.n52.sos.importer.model.dateAndTime.DateAndTimeComponent#parse()}
	 */
	/**
	 * @param mVColumn
	 * @param values
	 * @return
	 * @throws ParseException
	 */
	public Timestamp getTimeStamp(final int mVColumn, final String[] values) throws ParseException {
		LOG.trace("getTimeStamp()");
		// if RelatedDateTimeGroup is set for mvColumn -> get group id
		final Column col = configuration.getColumnById(mVColumn);
		String group = null;
		if (col.isSetRelatedDateTimeGroup()) {
			group = col.getRelatedDateTimeGroup();
		}
		// else check all columns for Type::DATE_TIME -> get Metadata.Key::GROUP->Value
		group = configuration.getFirstDateTimeGroup();
		final Column[] cols = configuration.getAllColumnsForGroup(group, Type.DATE_TIME);
		if (cols != null) {
			// get value from each column
			final Timestamp ts = new Timestamp();
			for (final Column column : cols) {
				// get pattern and fields
				final String pattern = getParsePattern(column);
				final int[] fields = getGregorianCalendarFields(pattern);
				for (final int field : fields) {
					// parse values
					final short value =
							parseTimestampComponent(values[column.getNumber()],
									pattern,
									field);
					// add to timestamp object
					switch (field) {
					case GregorianCalendar.YEAR:
						ts.setYear(value);
						break;
					case GregorianCalendar.MONTH:
						// java starts month counting at 0 -> +1 for each month
						ts.setMonth((byte) (value+1));
						break;
					case GregorianCalendar.DAY_OF_MONTH:
						ts.setDay((byte) value);
						break;
					case GregorianCalendar.HOUR_OF_DAY:
						ts.setHour((byte) value);
						break;
					case GregorianCalendar.MINUTE:
						ts.setMinute((byte) value);
						break;
					case GregorianCalendar.SECOND:
						ts.setSeconds((byte) value);
						break;
					case GregorianCalendar.ZONE_OFFSET:
						ts.setTimezone((byte) value);
						break;
					default:
						break;
					}
				}
				enrichTimestampWithColumnMetadata(ts,column);
			}
			if (configuration.isDateInfoExtractionSetupValid()) {
				ts.enrichByFilename(
					file.getName(),
					configuration.getRegExDateInfoInFileName(),
					configuration.getDateInfoPattern());
			}
			}
			return ts;
		}
		return null;
	}

	/**
	 * Checks for <code>Column.Metadata[]</code> and updates and returns the given {@link Timestamp}. Allowed keys are:
	 * <ul><li>TIME_DAY</li>
	 * <li>TIME_HOUR</li>
	 * <li>TIME_MINUTE</li>
	 * <li>TIME_MONTH</li>
	 * <li>TIME_SECOND</li>
	 * <li>TIME_YEAR</li>
	 * <li>TIME_ZONE</li></ul>
	 */
	private void enrichTimestampWithColumnMetadata(final Timestamp ts,
			final Column col) {
		if (col.getMetadataArray() != null) {
			for (final Metadata m : col.getMetadataArray()) {
				if (m.getKey().equals(Key.TIME_ZONE)) {
					ts.setTimezone( Byte.parseByte( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_YEAR)) {
					ts.setYear( Short.parseShort( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_MONTH)) {
					ts.setMonth( Byte.parseByte( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_DAY)) {
					ts.setDay( Byte.parseByte( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_HOUR)) {
					ts.setHour( Byte.parseByte( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_MINUTE)) {
					ts.setMinute( Byte.parseByte( m.getValue() ) );
					continue;
				}
				if (m.getKey().equals(Key.TIME_SECOND)) {
					ts.setSeconds( Byte.parseByte( m.getValue() ) );
					continue;
				}
			}
		}
	}

	/**
	 * Case A.1: RelatedUnitOfMeasurement is a IDref<br />
	 * 			-> Case A.1.1 or A.1.2<br />
	 * <br />
	 * Case A.1.1: Related UOM resource is manual<br />
	 * 			-> get code from name element <br />
	 * 				UnitOfMeasurement.ManualResource.Name<br />
	 * <br />
	 * Case A.1.2: Related UOM resource is generated<br />
	 * 			-> generate name and return its value<br />
	 * <br />
	 *
	 * Case A.2: RelatedUnitOfMeasurement is a number<br />
	 * 			-> get information from the column<br />
	 * 			-> return values[number]<br />
	 * <br />
	 * Case B: RelatedUnitOfMeasurement is not set<br />
	 * 			-> Check for column with Type == "UOM"<br />
	 * 			-> get number of this column<br />
	 * 			-> return values[number]<br />
	 *
	 * @param mVColumnId
	 * @param values
	 * @return
	 *
	 */
	public UnitOfMeasurement getUnitOfMeasurement(final int mVColumnId, final String[] values) {
		LOG.trace("getUnitOfMeasurement()");
		final Column mvColumn = configuration.getColumnById(mVColumnId);

		// Case A*
		if (mvColumn.getRelatedUnitOfMeasurementArray() != null &&
				mvColumn.getRelatedUnitOfMeasurementArray().length > 0) {
			final RelatedUnitOfMeasurement relUom =
					mvColumn.getRelatedUnitOfMeasurementArray(0);

			// Case A.1.*: idRef
			if (relUom.isSetIdRef() && !relUom.isSetNumber()) {
				final UnitOfMeasurementType uom = configuration.getUomById(relUom.getIdRef());
				if (uom != null) {

					// Case A.1.1
					if (uom.getResource() instanceof ManualResourceType) {
						final ManualResourceType uomMRT =
								(ManualResourceType) uom.getResource();
						return new UnitOfMeasurement(uomMRT.getName(), uomMRT.getURI().getStringValue());
					}

					// Case A.1.2
					if (uom.getResource() instanceof GeneratedResourceType) {
						final GeneratedResourceType uomGRT =
								(GeneratedResourceType) uom.getResource();
						final String[] a = getUriAndNameFromGeneratedResourceType(
								uomGRT.isSetConcatString()?
										uomGRT.getConcatString():"",
								"",
								false,
								uomGRT.getNumberArray(),
								values);
						return new UnitOfMeasurement(a[0], a[1]);
					}
				}
			}

			// Case A.2: number
			if (relUom.isSetNumber() && !relUom.isSetIdRef()) {
				return new UnitOfMeasurement(values[relUom.getNumber()],values[relUom.getNumber()]);
			}
		}

		// Case B: Information stored in another column
		final int uomColumnId = configuration.getColumnIdForUom(mVColumnId);
		if (uomColumnId > -1) {
			return new UnitOfMeasurement(values[uomColumnId],values[uomColumnId]);
		}

		// no UOM found
		return null;
	}

	/**
	 * Case A.1: RelatedObserverdProperty is a IDref<br />
	 * 			-> Case A.1.1 or A.1.2<br />
	 * <br />
	 * Case A.1.1: RelatedObserverdProperty resource is manual<br />
	 * 			-> get code from name element <br />
	 * 				ObserverdProperty.ManualResource.Name<br />
	 * <br />
	 * Case A.1.2: RelatedObserverdProperty resource is generated<br />
	 * 			-> generate name and return its value<br />
	 * <br />
	 *
	 * Case A.2: RelatedObserverdProperty is a number<br />
	 * 			-> get information from the column<br />
	 * 			-> return values[number]<br />
	 * <br />
	 * Case B: RelatedObserverdProperty is not set<br />
	 * 			-> Check for column with Type == "UOM"<br />
	 * 			-> get number of this column<br />
	 * 			-> return values[number]<br />
	 *
	 * @param mVColumnId
	 * @param values
	 * @return
	 *
	 */
	public ObservedProperty getObservedProperty(final int mVColumnId, final String[] values) {
		LOG.trace("getObservedProperty()");
		final Column mvColumn = configuration.getColumnById(mVColumnId);

		// Case A*
		if (mvColumn.getRelatedObservedPropertyArray() != null &&
				mvColumn.getRelatedObservedPropertyArray().length > 0) {
			final RelatedObservedProperty relOp =
					mvColumn.getRelatedObservedPropertyArray(0);

			// Case A.1.*: idRef
			if (relOp.isSetIdRef() && !relOp.isSetNumber()) {
				final ObservedPropertyType op = configuration.getObsPropById(relOp.getIdRef());
				if (op != null) {

					// Case A.1.1
					if (op.getResource() instanceof ManualResourceType) {
						final ManualResourceType opMRT =
								(ManualResourceType) op.getResource();
						return new ObservedProperty(opMRT.getName(),opMRT.getURI().getStringValue());

					}

					// Case A.1.2
					if (op.getResource() instanceof GeneratedResourceType) {
						final GeneratedResourceType opGRT =
								(GeneratedResourceType) op.getResource();
						final String[] a = getUriAndNameFromGeneratedResourceType(
								opGRT.isSetConcatString()?
										opGRT.getConcatString():"",
								opGRT.getURI().getStringValue(),
								opGRT.getURI().isSetUseAsPrefix()?opGRT.getURI().getUseAsPrefix():false,
								opGRT.getNumberArray(),
								values);
						return new ObservedProperty(a[0], a[1]);
					}
				}
			}

			// Case A.2: number
			if (relOp.isSetNumber() && !relOp.isSetIdRef()) {
				return new ObservedProperty(values[relOp.getNumber()],values[relOp.getNumber()]);
			}
		}

		// Case B: Information stored in another column
		final int opColumnId = configuration.getColumnIdForOpsProp(mVColumnId);
		if (opColumnId > -1) {
			return new ObservedProperty(values[opColumnId],values[opColumnId]);
		}

		// no OP found
		return null;
	}

	public Offering getOffering(final Sensor s) {
		final Offering off = configuration.getOffering(s);
		if (!NcNameResolver.isNCName(off.getName())) {
			final String[] a = createCleanNCName(off);
			off.setName(a[0]);
			if (!a[0].equals(a[1])) {
				LOG.debug(String.format("Offering name changed to match NCName production: '%s' to '%s'",
						a[1],
						a[0]));
			}
		}
		return off;
	}

	/**
	 * @return the name of the data file. Not the whole path.
	 */
	public String getFileName() {
		return file.getName();
	}

	public String getCanonicalPath() throws IOException {
		return file.getCanonicalPath();
	}

	/**
	 * @see {@link Configuration#getFileName()}
	 */
	public String getConfigurationFileName() {
		return configuration.getFileName();
	}

	/**
	 * <code>String[] result = {name,uri};</code>
	 * @return <code>String[] result = {name,uri};</code>
	 */
	private String[] getUriAndNameFromGeneratedResourceType(String concatString,
			String uri,
			final boolean useUriAsPrefixAfterNameAsUri,
			final int[] columnIds,
			final String[] values) {
		LOG.trace(String.format("getValuesFromResourceType(%s,%s,%b,%s,%s)",
				concatString,
				uri,
				useUriAsPrefixAfterNameAsUri,
				Arrays.toString(columnIds),
				Arrays.toString(values)));
		String name = "";
		// first the name
		if (concatString == null) {
			concatString = "";
		}
		for (int i = 0; i < columnIds.length; i++) {
			if (i > 0) {
				name = name + concatString + values[columnIds[i]];
			} else {
				name = values[columnIds[i]];
			}
			LOG.trace(String.format("name: %s", name));
		}
		LOG.debug(String.format("name: %s", name));
		// than the uri
		if (uri != null && useUriAsPrefixAfterNameAsUri) {
			uri = uri + name;
		} else {
			uri = name;
		}
		LOG.debug(String.format("uri: %s", uri));
		final String[] result = {name,uri};
		return result;
	}

	private Sensor getSensorFromColumn(final int mvColumnId, final String[] values) {
		LOG.trace(String.format("getSensorColumn(%d,%s)",
			mvColumnId,
			Arrays.toString(values)));
		final int i = configuration.getColumnIdForSensor(mvColumnId);
		if (i < 0) {
			// sensor is not in the data file -> return null
			return null;
		} else {
			final Sensor s = new Sensor(values[i],values[i]);
			LOG.debug(String.format("Sensor found in datafile: %s", s));
			return s;
		}
	}

	private FeatureOfInterest getFoiColumn(final int mvColumnId, final String[] values) {
		LOG.trace(String.format("getFoiColumn(%d,%s)",
					mvColumnId,
					Arrays.toString(values)));
		final int i = configuration.getColumnIdForFoi(mvColumnId);
		if (i < 0) {
			// foi is not in the data file -> return null
			return null;
		} else {
			final Position p = configuration.getFoiPosition(values[i]);
			final FeatureOfInterest s = new FeatureOfInterest(values[i],
					values[i],
					p);
			LOG.debug(String.format("Feature of Interst found in datafile: %s", s));
			return s;
		}
	}

	private Position getPosition(
			final org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position p,
			final String[] values) throws ParseException {
		LOG.trace(String.format("getPosition(%s,%s)",
					p.xmlText(),
					Arrays.toString(values)));
		// Case A: Position is in configuration
		if (!p.isSetGroup() &&
				//p.isSetAlt() &&
				p.isSetEPSGCode() &&
				p.isSetLat() &&
				p.isSetLong()) {
			return configuration.getModelPositionXBPosition(p);
		}
		// Case B: Position is in data file (and configuration [missing values])
		else if (p.isSetGroup() &&
				//!p.isSetAlt() &&
				!p.isSetEPSGCode() &&
				!p.isSetLat() &&
				!p.isSetLong()) {
			return configuration.getPosition(p.getGroup(),values);
		}
		return null;
	}

	private short parseTimestampComponent(final String timestampPart,
			final String pattern,
			final int field) throws ParseException {
		LOG.trace(String.format("parseTimestampComponent(%s,%s,%d)",
					timestampPart,
					pattern,
					field));
		Date date = null;
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		date = sdf.parse(timestampPart);

		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		return new Integer(gc.get(field)).shortValue();
	}

	private int[] getGregorianCalendarFields(final String pattern) {
		LOG.trace(String.format("getGregorianCalendarFields(%s)",
					pattern));
		final ArrayList<Integer> fields = new ArrayList<Integer>();
		if (pattern.indexOf("y") != -1) {
    		fields.add(GregorianCalendar.YEAR);
		}
    	if (pattern.indexOf("M") != -1 ||
    			pattern.indexOf("w") != -1 ||
    			pattern.indexOf("D") != -1) {
    		fields.add(GregorianCalendar.MONTH);
    	}
    	if (pattern.indexOf("d") != -1 ||
    			(pattern.indexOf("W") != -1 && pattern.indexOf("d") != -1)) {
    		fields.add(GregorianCalendar.DAY_OF_MONTH);
    	}
    	if (pattern.indexOf("H") != -1 ||
    			pattern.indexOf("k") != -1 ||
    			((pattern.indexOf("K") != -1 ||
    			(pattern.indexOf("h") != -1) && pattern.indexOf("a") != -1))) {
    		fields.add(GregorianCalendar.HOUR_OF_DAY);
    	}
    	if (pattern.indexOf("m") != -1) {
    		fields.add(GregorianCalendar.MINUTE);
    	}
    	if (pattern.indexOf("s") != -1) {
    		fields.add(GregorianCalendar.SECOND);
    	}
    	if (pattern.indexOf("Z") != -1 || pattern.indexOf("z") != -1) {
    		fields.add(GregorianCalendar.ZONE_OFFSET);
    	}
    	fields.trimToSize();
    	final int[] result = new int[fields.size()];
    	int j = 0;
    	for (final Integer i : fields) {
			result[j++] = i.intValue();
		}
    	return result;
	}

	private String getParsePattern(final Column column) {
		LOG.trace("getParsePattern()");
		if (column.getMetadataArray() != null && column.getMetadataArray().length > 1) {
			for (final Metadata m : column.getMetadataArray()) {
				if (m.getKey().equals(Key.PARSE_PATTERN)) {
					LOG.debug(String.format("Parsepattern found: %s",
								m.getValue()));
					return m.getValue();
				}
			}
		}
		LOG.debug(String.format("No Metadata element found with key %s in column %s",
					Key.PARSE_PATTERN.toString(),
					column.xmlText()));
		return null;
	}

	@Override
	public String toString() {
		return String.format("DataFile [file=%s, configuration=%s]",file,configuration);
	}

	public String getType(final int mVColumnId) {
		return configuration.getType(mVColumnId);
	}

	public int getExpectedColumnCount()
	{
		return configuration.getExpectedColumnCount();
	}

	public int getHeaderLine() {
		return configuration.getHeaderLine();
	}

	public String getEncoding() {
		return configuration.getDataFileEncoding();
	}

	public char getSeparatorChar() {
		return configuration.getCsvSeparator();
	}
}
