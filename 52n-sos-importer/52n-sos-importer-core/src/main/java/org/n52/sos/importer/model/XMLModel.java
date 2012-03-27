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
package org.n52.sos.importer.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.Constants;
import org.x52North.sensorweb.sos.importer.x02.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.TypeDocument.Type;

/**
 * In this class the XML model for an CSV file is stored for later re-use by 
 * another application.
 * @author e.h.juerrens@52north.org
 * @since 0.2
 * @version 
 *
 */
public class XMLModel {

	private SosImportConfiguration sosImpConf;
	
	private StepModel[] stepModells = new Step3Model[1];
	
	private static final Logger logger = Logger.getLogger(XMLModel.class);

	/**
	 * Create a new and empty model
	 */
	public XMLModel() {
		this.sosImpConf = SosImportConfiguration.Factory.newInstance();
	}
	
	/**
	 * Create model based on xml file
	 * @param xmlFileWithModel the file containing the <code>XMLModel</code>
	 * @throws XmlException thrown while parsing the file &rarr; <code>XMLModel</code> is file is <b>not</b> valid.
	 * @throws IOException having any problems while reading file
	 */
	public XMLModel(File xmlFileWithModel) throws XmlException, IOException {
		SosImportConfigurationDocument sosImpConfDoc = SosImportConfigurationDocument.Factory.parse(xmlFileWithModel);
		this.sosImpConf = sosImpConfDoc.getSosImportConfiguration();
	}
	
	/**
	 * Create model based on an existing one
	 * @param sosImpConf
	 */
	public XMLModel(SosImportConfiguration sosImpConf) {
		this.sosImpConf = sosImpConf;
	}

	public boolean registerProvider(StepModel sm) {
		if (logger.isTraceEnabled()) {
			logger.trace("registerProvider()");
		}
		//
		ArrayList<StepModel> sMs;
		//
		sMs = createArrayListFromArray(stepModells);
		boolean result = sMs.add(sm);
		saveProvidersInArray(sMs);
		//
		return result;
	}
	
	public boolean removeProvider(StepModel sm) {
		if (logger.isTraceEnabled()) {
			logger.trace("removeProvider()");
		}
		//
		ArrayList<StepModel> provider;
		//
		provider = createArrayListFromArray(stepModells);
		boolean result = provider.remove(sm);
		saveProvidersInArray(provider);
		//
		return result;
	}
	
	public boolean save(File file) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("save()");
		}
		//
		// we save only valid configurations
//		if(this.validate()) {
			//
			// check write access to file
			if(file != null) {
				if(!file.exists()) {
					if (logger.isDebugEnabled()) {
						logger.debug("file " + file + " does not exist. Try to create it.");
					}
					if (!file.createNewFile()) {
						logger.error("Could not create file " + file);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("File " + file + " created");
						}
					}
				}
				if(file.isFile()) {
					if(file.canWrite()) {
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory.newInstance();
						doc.setSosImportConfiguration(sosImpConf);
						bw.write(doc.xmlText(new XmlOptions().
								setSavePrettyPrint().
								setSavePrettyPrintIndent(4)));
						bw.flush();
						bw.close();
						fw.close();
					} else {
						logger.error("model not saved: could not write to file: " + file);
					}
				} else {
					logger.error("model not saved: file is not a file: " + file);
				}
			} else {
				logger.error("model not saved: file is null");
			}
//		}
		return false;
	}
	
	/**
	 * Updates the model. Should be called when one of the providers has changed.
	 */
	public void updateModel() {
		if (logger.isTraceEnabled()) {
			logger.trace("updateModel()");
		}
		// check each provider and update the internal model
		if(stepModells != null && stepModells.length > 0) {
			//
			for (StepModel sm : this.stepModells) {
				//
				if (sm instanceof Step1Model) {
					//
					Step1Model s1M = (Step1Model) sm;
					handleStep1Model(s1M);
					//
				} else if (sm instanceof Step2Model) {
					//
					Step2Model s2M = (Step2Model) sm;
					handleStep2Model(s2M);
					//
				} else if (sm instanceof Step3Model) {
					//
					Step3Model s3M = (Step3Model) sm;
					handleStep3Model(s3M);
					//
				}
			}
		}
	}
	/**
	 * Should be called after final step to validate the final model.
	 * @return
	 */
	public boolean validate() {
		if (logger.isTraceEnabled()) {
			logger.trace("validate()");
		}
		//
		SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory.newInstance();
		doc.setSosImportConfiguration(sosImpConf);
		boolean modelValid = doc.validate();
		if(!modelValid) {
			logger.error("The model is not valid. Please update your values.");
		}
		return modelValid;
	}

	/**
	 * Get updates from Step1Model
	 * Provided information: <ul>
	 * 		<li>DataFile.LocalFile.Path</li></ul>
	 * @param s1M instance of {@linkplain org.n52.sos.importer.model.Step1Model}
	 */
	private void handleStep1Model(Step1Model s1M) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep1Model()");
		}
		//
		String path = s1M.getCSVFilePath();
		DataFile dF = this.sosImpConf.getDataFile();
		LocalFile lF = null;
		//
		if(dF == null) {
			dF = this.sosImpConf.addNewDataFile();
			lF = dF.addNewLocalFile();
		} else if(dF.isSetLocalFile()){
			lF = dF.getLocalFile();
		}
		if(path != null && !path.equals("")) {
			lF.setPath(path);
		} else {
			logger.error("empty path to CSV file in Step1Model");
		}
	}

	/**
	 * Get updates from Step2Model
	 * Provided information: <ul>
	 * 		<li>CsvMeta.Parameter.*</li>
	 * 		<li>CsvMeta.FirstLineWithData</li>
	 * 		<li>CsvMeta.UseHeader</li></ul>
	 * @param s2M instance of {@linkplain org.n52.sos.importer.model.Step2Model}
	 */
	private void handleStep2Model(Step2Model s2M) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep2Model()");
		}
		//
		CsvMetadata cM = sosImpConf.getCsvMetadata();
		Parameter p = null;
		//
		if(cM == null) {
			cM = this.sosImpConf.addNewCsvMetadata();
			p = cM.addNewParameter();
		} else {
			p = cM.getParameter();
		}
		cM.setFirstLineWithData(s2M.getFirstLineWithData());
		cM.setUseHeader(s2M.getUseHeader());
		p.setCommentIndicator(s2M.getCommentIndicator());
		p.setElementSeparator(s2M.getColumnSeparator());
		p.setTextIndicator(s2M.getTextQualifier());
	}

	/**
	 * Get updates from Step3Model
	 * Provided information for each column:<ul>
	 * 		<li>column index</li>
	 * 		<li>column type</li>
	 * 		<li>the type depending metadata</li></ul>
	 * Allowed column types are:<ul>
	 * 		<li>DO_NOT_EXPORT</li>
	 * 		<li>MEASURED_VALUE</li>
	 * 		<li>DATE_TIME</li>
	 * 		<li>POSITION</li>
	 * 		<li>FOI</li>
	 * 		<li>OBSERVED_PROPERTY</li>
	 * 		<li>UOM</li>
	 * 		<li>SENSOR</li></ul>
	 * The metadata consists of key value pairs. The allowed keys are:<ul>
	 * 		<li>TYPE</li>
	 * 		<li>GROUP</li>
	 * 		<li>PARSE_PATTERN</li>
	 * 		<li>DECIMAL_SEPARATOR</li>
	 * 		<li>THOUSANDS_SEPARATOR</li>
	 * 		<li>SOS_FOI</li>
	 * 		<li>SOS_OBSERVED_PROPERTY</li>
	 * 		<li>SOS_SENSOR</li>
	 * 		<li>OTHER</li></ul>
	 * For the latest configuration set-up and schema check: 
	 * 	{@link 52n-sos-importer-bindings/src/main/xsd/}
	 * @param s3M instance of {@linkplain org.n52.sos.importer.model.Step3Model}
	 */
	private void handleStep3Model(Step3Model s3M) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep3Model()");
		}
		HashMap<Integer, List<String>> colAssignments = s3M.getAllSelections();
		Set<Integer> keySet = colAssignments.keySet();
		Integer[] keys = keySet.toArray(new Integer[keySet.size()]);
		CsvMetadata csvMeta = this.sosImpConf.getCsvMetadata();
		//
		if (csvMeta == null) {
			csvMeta = this.sosImpConf.addNewCsvMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new CsvMetadata element");
			}
		}
		//
		ColumnAssignments colAssignmentsXB = csvMeta.getColumnAssignments();
		if(colAssignmentsXB == null) {
			colAssignmentsXB = csvMeta.addNewColumnAssignments();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new ColumnAssignments element");
			}
		}
		Column[] cols = colAssignmentsXB.getColumnArray();
		//
		for (int i = 0; i < keys.length; i++) {
			/*
			 * key = columnIndex
			 * List<String> contains:
			 * 			list.get(0) = type
			 * 			list.get(n) = endcoded meta data
			 * Type:
			 * 	Date & Time 
			 *  Measured Value
			 *  Position
			 *  * Feature of Interest
			 *  * Observed Property
			 *  * Unit of Measurement
			 *  * Sensor
			 *  * Do not export
			 * Encoded Metadata:
			 * 	Date & Time: 	Combination, Pattern <- parse pattern SEP Group
			 * 					UNIX TIME
			 *  Measured Value:	Numeric, .:, (: is separator between decimal 
			 *  				Count, 0				and thousands separator)
			 *  				Boolean, 0
			 *  				Text, 0
			 *  Position:		Combination, Pattern <- parse pattern SEP Group
			 */
			// value should have one or two elements
			List<String> value = colAssignments.get(keys[i]);
			int key = keys[i].intValue();
			Column col = this.getColumnForKey(key,cols);
			String type = null;
			String encodedMetadata = null;
			if(col == null) {
				col = colAssignmentsXB.addNewColumn();
				col.setNumber(key);
			}
			/*
			 * 	SIMPLE TYPES (incl. UnixTime <- requires no metadata)
			 */
			if(value.size() == 2) {
				type = value.get(0);
				this.setSimpleColumnType(col,type);
			/*
			 * 	COMPLEX TYPES
			 */
			} else if (value.size() == 3){
				type = value.get(0);
				encodedMetadata = value.get(2);
				// delete old metadata
				{
					Metadata[] metadata = col.getMetadataArray();
					if(metadata != null && metadata.length > 0) {
						col.removeMetadata(0);
					}
					metadata = null;
				}
				//
				/*
				 * 	DATE & TIME
				 */
				if(type.equalsIgnoreCase(Lang.l().step3ColTypeDateTime())) {
					this.setComplexColumnTypeDateAndTime(col,value.get(1),encodedMetadata);
				/*
				 * MEASURED VALUE
				 */
				} else if (type.equalsIgnoreCase(Lang.l().step3ColTypeMeasuredValue())) {
					this.setComplexColumnTypeMeasuredValue(col,value.get(1),encodedMetadata);
				/*
				 * POSITION
				 */
				} else if (type.equalsIgnoreCase(Lang.l().position())) {
					this.setComplexColumnTypePosition(col,value.get(1),encodedMetadata);
				}
			} else {
				logger.error("Implementation error: value should have one to three elements: " + value);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("handling of Step3Model finished");
		}
	}

	/**
	 * Position:		Combination, Pattern <- parse pattern SEP Group
	 * @param col
	 * @param type Combination
	 * @param encodedMetadata e.g.: LAT LON ALT EPSG<code>SEP</code>A
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
			String[] splittedEncodedMetadat = encodedMetadata.split(Constants.SEPARATOR_STRING);
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
	 * Measured Value:<br />
	 * 					Numeric, .SEP, (decimal and thousands separator)<br /> 
	 *  				Count, 0<br />
	 *  				Boolean, 0<br />
	 *  				Text, 0<br />
	 * @param col
	 * @param type Numeric, Count, Boolean, or Text
	 * @param encodedMetadata
	 */
	private void setComplexColumnTypeMeasuredValue(Column col, String type, String encodedMetadata) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetComplexColumnTypeMeasuredValue()");
		}
		col.setType(Type.MEASURED_VALUE);
		Metadata meta = col.addNewMetadata();
		meta.setKey(Key.TYPE);
		//
		if (type.equalsIgnoreCase(Lang.l().step3MeasuredValNumericValue())) {
			String[] splittedEncodedMetadata = encodedMetadata.split(Constants.SEPARATOR_STRING);
			// order of the next two params is defined in org.n52.sos.importer.view.step3.NumericValuePanel
			String decimalSepartor = splittedEncodedMetadata[0], thousandsSeparator = splittedEncodedMetadata[1];
			//
			meta.setValue(Constants.NUMERIC);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.DECIMAL_SEPARATOR);
			meta.setValue(decimalSepartor);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.THOUSANDS_SEPARATOR);
			meta.setValue(thousandsSeparator);
			//
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValBoolean())) {
			meta.setValue(Constants.BOOLEAN);
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValCount())) {
			meta.setValue(Constants.COUNT);
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValText())) {
			meta.setValue(Constants.TEXT);
		}
		meta = null;
	}

	/**
	 * Date & Time:<br />
	 * 				Combination, Pattern <- parse pattern SEP Group<br />
	 * 				UNIX TIME
	 * @param col
	 * @param type
	 * @param encodedMetadata
	 */
	private void setComplexColumnTypeDateAndTime(Column col, String type, String encodedMetadata) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tsetComplexTypeDateAndTime()");
		}
		//
		Metadata meta = col.addNewMetadata();
		col.setType(Type.DATE_TIME);
		//
		if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeCombination())) {
			String[] splittedMetadata = encodedMetadata.split(Constants.SEPARATOR_STRING);
			String parsePattern = splittedMetadata[0], group = splittedMetadata[1];
			//
			meta.setKey(Key.GROUP);
			meta.setValue(group);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.PARSE_PATTERN);
			meta.setValue(parsePattern);
			//
			meta = col.addNewMetadata();
			meta.setKey(Key.TYPE);
			meta.setValue(Constants.COMBINATION);
			//
			splittedMetadata = null;
			parsePattern = null;
			group = null;
		} else if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeUnixTime())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unix time selected");
			}
			meta.setKey(Key.TYPE);
			meta.setValue(Constants.UNIX_TIME);
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
			this.setComplexColumnTypeDateAndTime(col, Lang.l().step3DateAndTimeUnixTime(), null);
		} else {
			logger.error("Type not known to schema : " + type);
		}
	}

	/**
	 * @param number the number of the column in the data file
	 * @param cols all columns in the configuration
	 * @return the <code>org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column</code> with the given number
	 */
	private Column getColumnForKey(int number, Column[] cols) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tgetColumnForKey()");
		}
		//
		for (int i = 0; i < cols.length; i++) {
			if(cols[i].getNumber() == number) {
				return cols[i];
			}
		}
		return null;
	}

	/*
	 * 
	 * 	Private Helper methods
	 * 
	 * 
	 */
	private ArrayList<StepModel> createArrayListFromArray(StepModel[] models) {
		if (logger.isTraceEnabled()) {
			logger.trace("\tcreateArrayListFromArray()");
		}
		//
		ArrayList<StepModel> result;
		//
		result = new ArrayList<StepModel>(stepModells.length+1);
		for (StepModel stepModel : this.stepModells) {
			if(stepModel != null) {
				result.add(stepModel);
			}
		}
		result.trimToSize();
		//
		return result;
	}

	private void saveProvidersInArray(ArrayList<StepModel> aL) {
		if (logger.isTraceEnabled()) {
			logger.trace("\tsaveProvidersInArray()");
		}
		//
		aL.trimToSize();
		this.stepModells = aL.toArray(new StepModel[aL.size()]);
	}
	
}
