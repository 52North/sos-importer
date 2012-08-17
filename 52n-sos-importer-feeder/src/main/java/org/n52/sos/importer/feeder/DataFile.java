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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.n52.oxf.xml.XMLTools;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Resource;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.model.requests.Offering;
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
	
	private static final Logger logger = Logger.getLogger(DataFile.class);
	
	private Configuration c;
	
	private File f;
	
	public DataFile(Configuration configuration, File file) {
		c = configuration;
		f = file;
	}

	/**
	 * Checks if the file is available and can be read. All errors like not 
	 * available, not a file, and not readable are logged to 
	 * <code>logger.error()</code>.
	 * @return <code>true</code>, if the Datafile is a file and can be read,<br />
	 * 			else <code>false</code>.
	 */
	public boolean isAvailable() {
		if (logger.isTraceEnabled()) {
			logger.trace("isAvailable()");
		}
		if (!f.exists()) {
			logger.error(String.format("Data file \"%s\" specified in \"%s\" does not exist.",
					f.getAbsolutePath(),
					c.getConfigFile().getAbsolutePath()));
		} else if (!f.isFile()){
			logger.error(String.format("Data file \"%s\" is not a file!",
					f.getAbsolutePath()));
		} else if (!f.canRead()) {
			logger.error(String.format("Data file \"%s\" can not be accessed, please check file permissions!",
					f.getAbsolutePath()));
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Data file \"%s\" is a file and read permission is available.",
						f.getAbsolutePath()));
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns a CSVReader instance for the current DataFile using the configuration
	 * including the defined values for: first line with data, separator, escape, and text qualifier.
	 * @return a <code>CSVReader</code> instance
	 * @throws FileNotFoundException 
	 */
	public CSVReader getCSVReader() throws FileNotFoundException {
		if (logger.isTraceEnabled()) {
			logger.trace("getCSVReader()");
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		int flwd = c.getFirstLineWithData();
		char separator = c.getCsvSeparator(), 
				quotechar = c.getCsvQuoteChar(),
				escape = c.getCsvEscape();
		CSVReader cr = new CSVReader(br, separator, quotechar, escape, flwd);
		return cr;
	}

	/**
	 * @see {@link Configuration#getMeasureValueColumnIds()}
	 */
	public int[] getMeasuredValueColumnIds() {
		return c.getMeasureValueColumnIds();
	}

	/**
	 * @see {@link Configuration#getFirstLineWithData()}
	 */
	public int getFirstLineWithData() {
		return c.getFirstLineWithData();
	}

	/**
	 * 
	 * @param mvColumnId
	 * @param values
	 * @return
	 */
	public Sensor getSensorForColumn(int mvColumnId, String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getSensorForColumn(%d,%s)",
					mvColumnId,
					Arrays.toString(values)));
		}
		// check for sensor column and return new sensor
		Sensor s = getSensorFromColumn(mvColumnId,values);
		if (s == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Could not find sensor column for column id %d",
						mvColumnId));
			}
		} else {
			return s;
		}
		// else build sensor from manual or generated resource 
		SensorType sT = c.getRelatedSensor(mvColumnId);
		// Case: one mv column => no related sensor element => check for one single sensor in additional metadata
		if (sT == null && c.isOneMvColumn()) {
			sT = c.getSensorFromAdditionalMetadata();
		}
		if (sT != null && sT.getResource() != null) {
			// generated sensor
			if (sT.getResource() instanceof GeneratedResourceType) {
				GeneratedResourceType gRT = (GeneratedResourceType) sT.getResource();
				String[] a = getUriAndNameFromGeneratedResourceType(
						gRT.isSetConcatString()?gRT.getConcatString():null, // concatstring
						gRT.isSetURI()?gRT.getURI().getStringValue():null, // uri
						gRT.isSetURI()&&gRT.getURI().isSetUseAsPrefix()?gRT.getURI().getUseAsPrefix():false, // useUriAsPrefix
						gRT.getNumberArray(),
						values
						);
				s = new Sensor(a[0],a[1]);
			} else if (sT.getResource() instanceof ManualResourceType) {
				// manual sensor
				ManualResourceType mRT = (ManualResourceType) sT.getResource();
				s = new Sensor(mRT.getName(),
						mRT.getURI().getStringValue());
			}
		}
		return s; 
	}

	/**
	 * 
	 * @param mvColumnId
	 * @param values
	 * @return
	 * @throws ParseException 
	 */
	public FeatureOfInterest getFoiForColumn(int mvColumnId, String[] values) throws ParseException {
		// TODO Auto-generated method stub generated on 13.06.2012 around 11:03:25 by eike
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getFoiForColumn(%d,%s)",
					mvColumnId,
					Arrays.toString(values)));
		}
		// check for foi column and return new sensor
		FeatureOfInterest foi = getFoiColumn(mvColumnId,values);
		if (foi == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Could not find foi column for column id %d",
						mvColumnId));
			}
		} else {
			return foi;
		}
		// else build foi from manual or generated resource 
		FeatureOfInterestType foiT = c.getRelatedFoi(mvColumnId);
		if (foiT != null && foiT.getResource() != null) {
			// generated sensor
			if (foiT.getResource() instanceof GeneratedSpatialResourceType) {
				GeneratedSpatialResourceType gSRT = 
						(GeneratedSpatialResourceType) foiT.getResource();
				String[] a = getUriAndNameFromGeneratedResourceType(
						gSRT.isSetConcatString()?gSRT.getConcatString():null, // concatstring
								gSRT.isSetURI()?gSRT.getURI().getStringValue():null, // uri
										gSRT.isSetURI()&&gSRT.getURI().isSetUseAsPrefix()?gSRT.getURI().getUseAsPrefix():false, // useUriAsPrefix
												gSRT.getNumberArray(),
												values
						);
				Position p = getPosition(gSRT.getPosition(),values);
				foi = new FeatureOfInterest(a[0],a[1],p);
			} else if (foiT.getResource() instanceof SpatialResourceType) {
				// manual sensor
				SpatialResourceType mSRT = (SpatialResourceType) foiT.getResource();
				Position p = getPosition(mSRT.getPosition(),values);
				foi = new FeatureOfInterest(mSRT.getName(),
						mSRT.getURI().getStringValue(),
						p);
			}
		}
		if (!XMLTools.isNCName(foi.getName())){
			String[] a = createCleanNCName(foi); 
			foi.setName(a[0]);
			if (logger.isInfoEnabled()) {
				logger.info(String.format("Feature Of Interest name changed to match NCName production: \"%s\" to \"%s\"", 
						a[1],
						a[0]));
			}
		}
		return foi; 
	}

	/**
	 * @return result[0] := newName<br /> result[1] := originaleName
	 */
	private String[] createCleanNCName(Resource res) {
		// implement check for NCName compliance and remove bad values
		String name = res.getName();
		String origName = new String(name);
		// clean rest of string using Constants.UNICODE_REPLACER
		char[] foiNameChars = name.toCharArray();
		for (int i = 0; i < foiNameChars.length; i++) {
			char c = foiNameChars[i];
			if (!XMLTools.isNCNameChar(c)) {
				foiNameChars[i] = Configuration.UNICODE_REPLACER;
			}
		}
		name = String.valueOf(foiNameChars);
		// check if name is only containing "_"
		Matcher matcher = Configuration.UNICODE_ONLY_REPLACER_LEFT_PATTERN.matcher(name);
		if (matcher.matches()) {
			// if yes -> change to "className" + res.getUri().hashCode()
			name = res.getClass().getSimpleName().toLowerCase() + res.getUri().hashCode();
		}
		String[] result = { name, origName };
		return result;
	}

	/**
	 * 
	 * @param mVColumn
	 * @param values
	 * @return
	 */
	public Object getValue(int mVColumn, String[] values) throws ParseException {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getValue(%s,%s)",
					mVColumn,
					Arrays.toString(values)));
		}
		Column column = c.getColumnById(mVColumn);
		String value = values[mVColumn];
		for (Metadata m : column.getMetadataArray()) {
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
					return c.parseToDouble(value);
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
	public Timestamp getTimeStamp(int mVColumn, String[] values) throws ParseException {
		if (logger.isTraceEnabled()) {
			logger.trace("getTimeStamp()");
		}
		// if RelatedDateTimeGroup is set for mvColumn -> get group id
		Column col = c.getColumnById(mVColumn);
		String group = null;
		if (col.isSetRelatedDateTimeGroup()) {
			group = col.getRelatedDateTimeGroup();
		}
		// else check all columns for Type::DATE_TIME -> get Metadata.Key::GROUP->Value
		group = c.getFirstDateTimeGroup();
		Column[] cols = c.getAllColumnsForGroup(group, Type.DATE_TIME);
		if (cols != null) {
			// get value from each column
			Timestamp ts = new Timestamp();
			for (Column column : cols) {
				// get pattern and fields
				String pattern = getParsePattern(column);
				int[] fields = getGregorianCalendarFields(pattern);
				for (int field : fields) {
					// parse values
					short value = 
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
				ts = c.getAddtionalTimestampValuesFromColumn(ts,column);
			}
			// create timestamp string via toString()
			return ts;
		}
		return null;
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
	public UnitOfMeasurement getUnitOfMeasurement(int mVColumnId, String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace("getUnitOfMeasurement()");
		}
		Column mvColumn = c.getColumnById(mVColumnId);
		
		// Case A*
		if (mvColumn.getRelatedUnitOfMeasurementArray() != null &&
				mvColumn.getRelatedUnitOfMeasurementArray().length > 0) {
			RelatedUnitOfMeasurement relUom = 
					mvColumn.getRelatedUnitOfMeasurementArray(0);
		
			// Case A.1.*: idRef
			if (relUom.isSetIdRef() && !relUom.isSetNumber()) {
				UnitOfMeasurementType uom = c.getUomById(relUom.getIdRef());
				if (uom != null) {
			
					// Case A.1.1
					if (uom.getResource() instanceof ManualResourceType) {
						ManualResourceType uomMRT =
								(ManualResourceType) uom.getResource();
						return new UnitOfMeasurement(uomMRT.getName(), uomMRT.getURI().getStringValue());
					}
					
					// Case A.1.2
					if (uom.getResource() instanceof GeneratedResourceType) {
						GeneratedResourceType uomGRT =
								(GeneratedResourceType) uom.getResource();
						String[] a = getUriAndNameFromGeneratedResourceType(
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
		int uomColumnId = c.getColumnIdForUom(mVColumnId);
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
	public ObservedProperty getObservedProperty(int mVColumnId, String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace("getObservedProperty()");
		}
		Column mvColumn = c.getColumnById(mVColumnId);
		
		// Case A*
		if (mvColumn.getRelatedObservedPropertyArray() != null &&
				mvColumn.getRelatedObservedPropertyArray().length > 0) {
			RelatedObservedProperty relOp = 
					mvColumn.getRelatedObservedPropertyArray(0);
		
			// Case A.1.*: idRef
			if (relOp.isSetIdRef() && !relOp.isSetNumber()) {
				ObservedPropertyType op = c.getObsPropById(relOp.getIdRef());
				if (op != null) {
			
					// Case A.1.1
					if (op.getResource() instanceof ManualResourceType) {
						ManualResourceType opMRT =
								(ManualResourceType) op.getResource();
						return new ObservedProperty(opMRT.getName(),opMRT.getURI().getStringValue());
								
					}
					
					// Case A.1.2
					if (op.getResource() instanceof GeneratedResourceType) {
						GeneratedResourceType opGRT =
								(GeneratedResourceType) op.getResource();
						String[] a = getUriAndNameFromGeneratedResourceType(
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
		int opColumnId = c.getColumnIdForOpsProp(mVColumnId);
		if (opColumnId > -1) {
			return new ObservedProperty(values[opColumnId],values[opColumnId]);
		}
		
		// no OP found
		return null;
	}

	public Offering getOffering(Sensor s) {
		Offering off = c.getOffering(s);
		if (!XMLTools.isNCName(off.getName())) {
			String[] a = createCleanNCName(off); 
			off.setName(a[0]);
			if (logger.isInfoEnabled()) {
				logger.info(String.format("Offering name changed to match NCName production: \"%s\" to \"%s\"", 
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
		return f.getName();
	}

	/**
	 * @see {@link Configuration#getFileName()}
	 */
	public Object getConfigurationFileName() {
		return c.getFileName();
	}

	/**
	 * <code>String[] result = {name,uri};</code>
	 * @return <code>String[] result = {name,uri};</code>
	 */
	private String[] getUriAndNameFromGeneratedResourceType(String concatString,
			String uri,
			boolean useUriAsPrefixAfterNameAsUri,
			int[] columnIds,
			String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getValuesFromResourceType(%s,%s,%b,%s,%s)",
					concatString,
					uri,
					useUriAsPrefixAfterNameAsUri,
					Arrays.toString(columnIds),
					Arrays.toString(values)));
		}
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
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("name: %s", name));
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("name: %s", name));
		}
		// than the uri
		if (uri != null && useUriAsPrefixAfterNameAsUri) {
			uri = uri + name;
		} else {
			uri = name;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("uri: %s", uri));
		}
		String[] result = {name,uri};
		return result;
	}

	private Sensor getSensorFromColumn(int mvColumnId, String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getSensorColumn(%d,%s)",
			mvColumnId,
			Arrays.toString(values)));
		}
		int i = c.getColumnIdForSensor(mvColumnId);
		if (i < 0) {
			// sensor is not in the data file -> return null
			return null;
		} else {
			Sensor s = new Sensor(values[i],values[i]);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Sensor found in datafile: %s", s));
			}
			return s;
		}
	}
	
	private FeatureOfInterest getFoiColumn(int mvColumnId, String[] values) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getFoiColumn(%d,%s)",
					mvColumnId,
					Arrays.toString(values)));
		}
		int i = c.getColumnIdForFoi(mvColumnId);
		if (i < 0) {
			// foi is not in the data file -> return null
			return null;
		} else {
			Position p = c.getFoiPosition(values[i]);
			FeatureOfInterest s = new FeatureOfInterest(values[i],
					values[i],
					p);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Feature of Interst found in datafile: %s", s));
			}
			return s;
		}
	}

	private Position getPosition(
			org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position p,
			String[] values) throws ParseException {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getPosition(%s,%s)",
					p.xmlText(),
					Arrays.toString(values)));
		}
		// Case A: Position is in configuration
		if (!p.isSetGroup() && 
				p.isSetAlt() && 
				p.isSetEPSGCode() && 
				p.isSetLat() && 
				p.isSetLong()) {
			return c.getModelPositionXBPosition(p);
		}
		// Case B: Position is in data file (and configuration [missing values])
		else if (p.isSetGroup() && 
				!p.isSetAlt() && 
				!p.isSetEPSGCode() && 
				!p.isSetLat() && 
				!p.isSetLong()) {
			return c.getPosition(p.getGroup(),values);
		}
		return null;
	}

	private short parseTimestampComponent(String timestampPart,
			String pattern,
			int field) throws ParseException {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("parseTimestampComponent(%s,%s,%d)",
					timestampPart,
					pattern,
					field));
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		date = sdf.parse(timestampPart);
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		
		return new Integer(gc.get(field)).shortValue();
	}

	private int[] getGregorianCalendarFields(String pattern) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getGregorianCalendarFields(%s)",
					pattern));
		}
		ArrayList<Integer> fields = new ArrayList<Integer>();
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
    	int[] result = new int[fields.size()];
    	int j = 0;
    	for (Integer i : fields) {
			result[j++] = i.intValue();
		}
    	return result;
	}

	private String getParsePattern(Column column) {
		if (logger.isTraceEnabled()) {
			logger.trace("getParsePattern()");
		}
		if (column.getMetadataArray() != null && column.getMetadataArray().length > 1) {
			for (Metadata m : column.getMetadataArray()) {
				if (m.getKey().equals(Key.PARSE_PATTERN)) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Parsepattern found: %s",
								m.getValue()));
					}
					return m.getValue();
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("No Metadata element found with key %s in column %s",
					Key.PARSE_PATTERN.toString(),
					column.xmlText()));
		}
		return null;
	}

	public String toString() {
		return String.format("DataFile [file=%s, c=%s]",f,c);
	}

	public String getType(int mVColumnId) {
		return c.getType(mVColumnId);
	}
}
