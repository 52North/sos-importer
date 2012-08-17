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
package org.n52.sos.importer.model.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.view.i18n.Lang;
import org.x52North.sensorweb.sos.importer.x02.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.TypeDocument.Type;

/**
 * Get updates from Step3Model Provided information for each column:
 * <ul>
 * <li>column index</li>
 * <li>column type</li>
 * <li>the type depending metadata</li>
 * </ul>
 * Allowed column types are:
 * <ul>
 * <li>DO_NOT_EXPORT</li>
 * <li>MEASURED_VALUE</li>
 * <li>DATE_TIME</li>
 * <li>POSITION</li>
 * <li>FOI</li>
 * <li>OBSERVED_PROPERTY</li>
 * <li>UOM</li>
 * <li>SENSOR</li>
 * </ul>
 * The metadata consists of key value pairs. The allowed keys are:
 * <ul>
 * <li>TYPE</li>
 * <li>GROUP</li>
 * <li>PARSE_PATTERN</li>
 * <li>DECIMAL_SEPARATOR</li>
 * <li>THOUSANDS_SEPARATOR</li>
 * <li>SOS_FOI</li>
 * <li>SOS_OBSERVED_PROPERTY</li>
 * <li>SOS_SENSOR</li>
 * <li>OTHER</li>
 * </ul>
 * For the latest configuration set-up and schema check: {@link
 * 52n-sos-importer-bindings/src/main/xsd/}
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step3ModelHandler implements ModelHandler<Step3Model> {
	
	private static final Logger logger = Logger.getLogger(Step3ModelHandler.class);

	@Override
	public void handleModel(Step3Model stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		HashMap<Integer, List<String>> colAssignments = stepModel.getAllSelections();
		Set<Integer> keySet = colAssignments.keySet();
		Integer[] keys = keySet.toArray(new Integer[keySet.size()]);
		CsvMetadata csvMeta = sosImportConf.getCsvMetadata();
		//
		if (csvMeta == null) {
			csvMeta = sosImportConf.addNewCsvMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new CsvMetadata element");
			}
		}
		//
		ColumnAssignments colAssignmentsXB = csvMeta.getColumnAssignments();
		if (colAssignmentsXB == null) {
			colAssignmentsXB = csvMeta.addNewColumnAssignments();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new ColumnAssignments element");
			}
		}
		Column[] cols = colAssignmentsXB.getColumnArray();
		//
		for (int i = 0; i < keys.length; i++) {
			/*
			 * key = columnIndex List<String> contains: list.get(0) = type
			 * list.get(n) = endcoded meta data Type: Date & Time Measured Value
			 * Position * Feature of Interest * Observed Property * Unit of
			 * Measurement * Sensor * Do not export Encoded Metadata: Date &
			 * Time: Combination, Pattern <- parse pattern SEP Group UNIX TIME
			 * Measured Value: Numeric, .:, (: is separator between decimal
			 * Count, 0 and thousands separator) Boolean, 0 Text, 0 Position:
			 * Combination, Pattern <- parse pattern SEP Group
			 */
			// value should have one or two elements
			List<String> value = colAssignments.get(keys[i]);
			int key = keys[i].intValue();
			Column col = getColumnForKey(key, cols);
			String type = null;
			String encodedMetadata = null;
			if (col == null) {
				col = colAssignmentsXB.addNewColumn();
				col.setNumber(key);
			}
			/*
			 * SIMPLE TYPES (incl. UnixTime, Do-Not-Export require no metadata)
			 */
			if (value.size() < 3) {
				type = value.get(0);
				setSimpleColumnType(col, type);
				/*
				 * COMPLEX TYPES
				 */
			} else if (value.size() == 3) {
				type = value.get(0);
				encodedMetadata = value.get(2);
				// delete old metadata
				{
					Metadata[] metadata = col.getMetadataArray();
					if (metadata != null && metadata.length > 0) {
						col.removeMetadata(0);
					}
					metadata = null;
				}
				//
				/*
				 * DATE & TIME
				 */
				if (type.equalsIgnoreCase(Lang.l().step3ColTypeDateTime())) {
					setComplexColumnTypeDateAndTime(col, value.get(1),
							encodedMetadata);
					/*
					 * MEASURED VALUE
					 */
				} else if (type.equalsIgnoreCase(Lang.l()
						.step3ColTypeMeasuredValue())) {
					setComplexColumnTypeMeasuredValue(col, value.get(1),
							encodedMetadata);
					/*
					 * POSITION
					 */
				} else if (type.equalsIgnoreCase(Lang.l().position())) {
					setComplexColumnTypePosition(col, value.get(1),
							encodedMetadata);
				}
			} else {
				logger.error("Implementation error: value should have one to three elements: "
						+ value);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("handling of Step3Model finished");
		}
	}
	/**
	 * @param number
	 *            the number of the column in the data file
	 * @param cols
	 *            all columns in the configuration
	 * @return the
	 *         <code>org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column</code>
	 *         with the given number
	 */
	private Column getColumnForKey(int number, Column[] cols) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tgetColumnForKey()");
		}
		//
		for (int i = 0; i < cols.length; i++) {
			if (cols[i].getNumber() == number) {
				return cols[i];
			}
		}
		return null;
	}
	/**
	 * Date & Time:<br />
	 * Combination, Pattern <- parse pattern SEP Group<br />
	 * UNIX TIME
	 * 
	 * @param col
	 * @param type
	 * @param encodedMetadata
	 */
	private void setComplexColumnTypeDateAndTime(Column col, String type,
			String encodedMetadata) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetComplexTypeDateAndTime()");
		}
		//
		col.setType(Type.DATE_TIME);
		//
		if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeCombination())) {
			String[] splittedMetadata = encodedMetadata
					.split(Constants.SEPARATOR_STRING);
			String parsePattern = splittedMetadata[0], group = splittedMetadata[1];
			//
			Helper.addOrUpdateColumnMetadata(Key.GROUP, group, col);
			//
			Helper.addOrUpdateColumnMetadata(Key.PARSE_PATTERN,parsePattern, col);
			//
			Helper.addOrUpdateColumnMetadata(Key.TYPE,Constants.COMBINATION,col);
			//
			splittedMetadata = null;
			parsePattern = null;
			group = null;
		} else if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeUnixTime())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unix time selected");
			}
			Helper.addOrUpdateColumnMetadata(Key.TYPE, Constants.UNIX_TIME,col);
		}
	}
	
	/**
	 * Measured Value:<br />
	 * Numeric, .SEP, (decimal and thousands separator)<br />
	 * Count, 0<br />
	 * Boolean, 0<br />
	 * Text, 0<br />
	 * 
	 * @param col
	 * @param type
	 *            Numeric, Count, Boolean, or Text
	 * @param encodedMetadata
	 */
	private void setComplexColumnTypeMeasuredValue(Column col, String type,
			String encodedMetadata) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetComplexColumnTypeMeasuredValue()");
		}
		col.setType(Type.MEASURED_VALUE);
		
		String value = null;
		//
		if (type.equalsIgnoreCase(Lang.l().step3MeasuredValNumericValue())) {
			value = Constants.NUMERIC;
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValBoolean())) {
			value = Constants.BOOLEAN;
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValCount())) {
			value = Constants.COUNT;
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValText())) {
			value = Constants.TEXT;
		}
		Helper.addOrUpdateColumnMetadata(Key.TYPE, value, col);
	}
	
	/**
	 * Position: Combination, Pattern <- parse pattern SEP Group
	 * 
	 * @param col
	 * @param type
	 *            Combination
	 * @param encodedMetadata
	 *            e.g.: LAT LON ALT EPSG<code>SEP</code>A
	 */
	private void setComplexColumnTypePosition(Column col, String type,
			String encodedMetadata) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetComplexColumnTypePosition()");
		}
		//
		col.setType(Type.POSITION);
		Metadata meta = col.addNewMetadata();
		meta.setKey(Key.TYPE);
		//
		if (type.equalsIgnoreCase(Lang.l().step3PositionCombination())) {
			String[] splittedEncodedMetadat = encodedMetadata
					.split(Constants.SEPARATOR_STRING);
			String parsePattern = splittedEncodedMetadat[0], group = splittedEncodedMetadat[1];
			//
			meta.setValue(Constants.COMBINATION);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.PARSE_PATTERN);
			meta.setValue(parsePattern);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.GROUP);
			meta.setValue(group);
		}
		meta = null;
	}
	/**
	 * @param col
	 * @param type
	 */
	private void setSimpleColumnType(Column col, String type) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetSimpleColumnType()");
		}
		if (type.equalsIgnoreCase(Lang.l().step3ColTypeDoNotExport())) {
			col.setType(Type.DO_NOT_EXPORT);
		} else if (type.equalsIgnoreCase(Lang.l().sensor())) {
			col.setType(Type.SENSOR);
		} else if (type.equalsIgnoreCase(Lang.l().unitOfMeasurement())) {
			col.setType(Type.UOM);
		} else if (type.equalsIgnoreCase(Lang.l().observedProperty())) {
			col.setType(Type.OBSERVED_PROPERTY);
		} else if (type.equalsIgnoreCase(Lang.l().featureOfInterest())) {
			col.setType(Type.FOI);
		} else if (type.equalsIgnoreCase(Lang.l().step3ColTypeDateTime())) {
			setComplexColumnTypeDateAndTime(col, 
					Lang.l().step3DateAndTimeUnixTime(), null);
		} else {
			logger.error("Type not known to schema : " + type);
		}
	}
}
