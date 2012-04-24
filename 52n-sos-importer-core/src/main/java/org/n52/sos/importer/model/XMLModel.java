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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.Constants;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x02.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x02.FeatureOfInterestDocument.FeatureOfInterest;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x02.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x02.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x02.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.ObservedPropertyDocument.ObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x02.SensorDocument.Sensor;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.TypeDocument.Type;
import org.x52North.sensorweb.sos.importer.x02.UnitOfMeasurementDocument.UnitOfMeasurement;

/**
 * In this class the XML model for an CSV file is stored for later re-use by
 * another application.
 * 
 * @author e.h.juerrens@52north.org
 * @since 0.2
 * @version
 * 
 */
public class XMLModel {

	private static final Logger logger = Logger.getLogger(XMLModel.class);

	private SosImportConfiguration sosImpConf;

	private StepModel[] stepModells = new Step3Model[1];

	/**
	 * Create a new and empty model
	 */
	public XMLModel() {
		this.sosImpConf = SosImportConfiguration.Factory.newInstance();
	}

	/**
	 * Create model based on xml file
	 * 
	 * @param xmlFileWithModel
	 *            the file containing the <code>XMLModel</code>
	 * @throws XmlException
	 *             thrown while parsing the file &rarr; <code>XMLModel</code>
	 *             file is <b>not</b> valid.
	 * @throws IOException
	 *             having any problems while reading file
	 */
	public XMLModel(File xmlFileWithModel) throws XmlException, IOException {
		SosImportConfigurationDocument sosImpConfDoc = SosImportConfigurationDocument.Factory
				.parse(xmlFileWithModel);
		this.sosImpConf = sosImpConfDoc.getSosImportConfiguration();
	}

	/**
	 * Create model based on an existing one
	 * 
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
		// if(this.validate()) {
		//
		// check write access to file
		if (file != null) {
			if (!file.exists()) {
				if (logger.isDebugEnabled()) {
					logger.debug("file " + file
							+ " does not exist. Try to create it.");
				}
				if (!file.createNewFile()) {
					logger.error("Could not create file " + file);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("File " + file + " created");
					}
				}
			}
			if (file.isFile()) {
				if (file.canWrite()) {
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory
							.newInstance();
					doc.setSosImportConfiguration(sosImpConf);
					bw.write(doc.xmlText(new XmlOptions().setSavePrettyPrint()
							.setSavePrettyPrintIndent(4)));
					bw.flush();
					bw.close();
					fw.close();
				} else {
					logger.error("model not saved: could not write to file: "
							+ file);
				}
			} else {
				logger.error("model not saved: file is not a file: " + file);
			}
		} else {
			logger.error("model not saved: file is null");
		}
		// }
		return false;
	}

	/**
	 * Updates the model. Should be called when one of the providers has
	 * changed.
	 */
	public void updateModel() {
		if (logger.isTraceEnabled()) {
			logger.trace("updateModel()");
		}
		// check each provider and update the internal model
		if (stepModells != null && stepModells.length > 0) {
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
				} else if (sm instanceof Step4bModel) {
					//
					Step4bModel s4bM = (Step4bModel) sm;
					handleStep4bModel(s4bM);
					//
				} else if (sm instanceof Step5aModel) {
					//
					Step5aModel s5aM = (Step5aModel) sm;
					handleStep5aModel(s5aM);
					//
				} else if (sm instanceof Step5cModel) {
					//
					Step5cModel s5cM = (Step5cModel) sm;
					handleStep5cModel(s5cM);
					//
				} else if (sm instanceof Step6aModel) {
					//
					Step6aModel s6aM = (Step6aModel) sm;
					handleStep6aModel(s6aM);
					//
				} else if (sm instanceof Step6bModel) {
					//
					Step6bModel s6bM = (Step6bModel) sm;
					handleStep6bModel(s6bM);
					//
				} else if (sm instanceof Step6bSpecialModel) {
					//
					Step6bSpecialModel s6bSM = (Step6bSpecialModel) sm;
					handleStep6bSpecialModel(s6bSM);
					//
				} else if (sm instanceof Step6cModel) {
					//
					Step6cModel s6cM = (Step6cModel) sm;
					handleStep6cModel(s6cM);
					//
				}
			}
		}
	}

	/**
	 * Should be called after final step to validate the final model.
	 * 
	 * @return
	 */
	public boolean validate() {
		if (logger.isTraceEnabled()) {
			logger.trace("validate()");
		}
		//
		SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory
				.newInstance();
		doc.setSosImportConfiguration(sosImpConf);
		boolean modelValid = doc.validate();
		if (!modelValid) {
			logger.error("The model is not valid. Please update your values.");
		}
		return modelValid;
	}

	/**
	 * Get updates from Step1Model Provided information:
	 * <ul>
	 * <li>DataFile.LocalFile.Path</li>
	 * </ul>
	 * 
	 * @param s1M
	 *            instance of {@linkplain org.n52.sos.importer.model.Step1Model}
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
		if (dF == null) {
			dF = this.sosImpConf.addNewDataFile();
			lF = dF.addNewLocalFile();
		} else if (dF.isSetLocalFile()) {
			lF = dF.getLocalFile();
		}
		if (path != null && !path.equals("")) {
			lF.setPath(path);
		} else {
			logger.error("empty path to CSV file in Step1Model");
		}
	}

	/**
	 * Get updates from Step2Model Provided information:
	 * <ul>
	 * <li>CsvMeta.Parameter.*</li>
	 * <li>CsvMeta.FirstLineWithData</li>
	 * <li>CsvMeta.UseHeader</li>
	 * </ul>
	 * 
	 * @param s2M
	 *            instance of {@linkplain org.n52.sos.importer.model.Step2Model}
	 */
	private void handleStep2Model(Step2Model s2M) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep2Model()");
		}
		//
		CsvMetadata cM = sosImpConf.getCsvMetadata();
		Parameter p = null;
		//
		if (cM == null) {
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
	 * 
	 * @param s3M
	 *            instance of {@linkplain org.n52.sos.importer.model.Step3Model}
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
			Column col = this.getColumnForKey(key, cols);
			String type = null;
			String encodedMetadata = null;
			if (col == null) {
				col = colAssignmentsXB.addNewColumn();
				col.setNumber(key);
			}
			/*
			 * SIMPLE TYPES (incl. UnixTime <- requires no metadata)
			 */
			if (value.size() == 2) {
				type = value.get(0);
				this.setSimpleColumnType(col, type);
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
					this.setComplexColumnTypeDateAndTime(col, value.get(1),
							encodedMetadata);
					/*
					 * MEASURED VALUE
					 */
				} else if (type.equalsIgnoreCase(Lang.l()
						.step3ColTypeMeasuredValue())) {
					this.setComplexColumnTypeMeasuredValue(col, value.get(1),
							encodedMetadata);
					/*
					 * POSITION
					 */
				} else if (type.equalsIgnoreCase(Lang.l().position())) {
					this.setComplexColumnTypePosition(col, value.get(1),
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

	private void handleStep4bModel(Step4bModel s4bM) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\thandleStep4bModel()");
		}
		/*
		 * 	LOCALE FIELDS
		 */
		int[] relatedColumnsIds;
		CsvMetadata csvMeta;
		ColumnAssignments colAssignmts;
		Column[] availableCols, relatedCols;
		ArrayList<Column> relCol;
		Resource res;
		// get related columns
		relatedColumnsIds = s4bM.getSelectedColumns();
		csvMeta = this.sosImpConf.getCsvMetadata();
		if (csvMeta == null) {
			logger.fatal("CsvMetadata element not set in step 4; should not " +
					"happen. Please check the log file!");
			return;
		}
		colAssignmts = csvMeta.getColumnAssignments();
		if (colAssignmts == null) {
			logger.fatal("CsvMetadata.ColumnAssignments element not set in " +
					"step 4; should not happen. Please check the log file!");
			return;
		}
		availableCols = colAssignmts.getColumnArray();
		if (availableCols == null) {
			logger.fatal("CsvMetadata.ColumnAssignments.Column elements not set in " +
					"step 4; should not happen. Please check the log file!");
			return;
		}
		relCol = new ArrayList<Column>(availableCols.length);
		for (Column column : availableCols) {
			// check for correct column id
			if(this.isIntInArray(relatedColumnsIds, column.getNumber()) ) {
				// add column to result set	
				relCol.add(column);
			}
		}
		relCol.trimToSize();
		relatedCols = relCol.toArray(new Column[relCol.size()]);
		
		// identify type of resource that is linked to the given row and or columns
		res = s4bM.getResource();
		
		// add relation to the related column
		this.addRelatedResourceColumn(res, relatedCols);
	}

	/**
	 * For each <code>Column</code> in the array <code>relatedCols</code> add
	 * a related resource 
	 * @param res the resource to add
	 * @param relatedCols the column where to add the resource
	 */
	private boolean addRelatedResourceColumn(Resource res, Column[] relatedCols) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedResourceColumn()");
		}
		/*
		 * 	LOCALE FIELDS
		 */
		TableElement tabE = res.getTableElement();
		int colId;
		boolean result = true;
		// get resource column
		if (tabE instanceof org.n52.sos.importer.model.table.Column) {
			org.n52.sos.importer.model.table.Column col = (org.n52.sos.importer.model.table.Column) tabE;
			colId = col.getNumber();
		} else {
			logger.fatal("Type org.n52.sos.importer.model.table.Column expected. Type is:" + tabE.getClass());
			return false;
		}
		/*
		 * add colId of related resource to Columns in relatedCols
		 */
		for (Column column : relatedCols) {
			boolean addNew;
			/*
			 * 	FEATURE_OF_INTEREST
			 */
			if (res instanceof org.n52.sos.importer.model.resources.FeatureOfInterest) {
				RelatedFOI[] relFois = column.getRelatedFOIArray();
				addNew = !this.isFoiColIdInArray(relFois, colId);
				if (addNew) {
					column.addNewRelatedFOI().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related foi by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related foi was already there");
				}
				result = result && this.isFoiColIdInArray(relFois, colId);
			} else
			/*
			 * 	SENSOR
			 */
			if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
				RelatedSensor[] relSensors = column.getRelatedSensorArray();
				addNew = !this.isSensorInArray(relSensors, colId);
				if (addNew) {
					column.addNewRelatedSensor().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related sensor by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related sensor was already there");
				}
				result = result && this.isSensorInArray(relSensors, colId);
			} else
			/*
			 * 	UNIT_OF_MEASUREMENT
			 */
			if (res instanceof 
					org.n52.sos.importer.model.resources.UnitOfMeasurement) {
				RelatedUnitOfMeasurement[] relUOMs = column.getRelatedUnitOfMeasurementArray();
				addNew = !this.isUOMInArray(relUOMs, colId);
				if (addNew) {
					column.addNewRelatedUnitOfMeasurement().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related UOM by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related UOM was already there");
				}
				result = result && this.isUOMInArray(relUOMs, colId);
			} else
			/*
			 * 	OBSERVED_PROPERTY
			 */
			if (res instanceof 
					org.n52.sos.importer.model.resources.ObservedProperty) {
				RelatedObservedProperty[] relObsProps = column.getRelatedObservedPropertyArray();
				addNew = !this.isObsPropInArray(relObsProps, colId);
				if (addNew) {
					column.addNewRelatedObservedProperty().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related observed property by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related observed property was already there");
				}
				result = result && this.isObsPropInArray(relObsProps, colId);
			}
		}
		return result;
	}

	private boolean isObsPropInArray(RelatedObservedProperty[] relObsProps,
			int colId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isObsPropInArray()");
		}
		for (RelatedObservedProperty relatedObsProp : relObsProps) {
			if (relatedObsProp.isSetNumber() && 
					relatedObsProp.getNumber() == colId) {
				return true;
			}
		}
		return false;
	}

	private boolean isUOMInArray(RelatedUnitOfMeasurement[] relUOMs, int colId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isUOMInArray()");
		}
		for (RelatedUnitOfMeasurement relatedUOM : relUOMs) {
			if (relatedUOM.isSetNumber() && 
					relatedUOM.getNumber() == colId) {
				return true;
			}
		}
		return false;
	}

	private boolean isSensorInArray(RelatedSensor[] relSensors, int colId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isSensorInArray()");
		}
		for (RelatedSensor relatedSensor : relSensors) {
			if (relatedSensor.isSetNumber() && 
					relatedSensor.getNumber() == colId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the column is already referenced.
	 * @param relFois
	 * @param colId
	 * @return
	 */
	private boolean isFoiColIdInArray(RelatedFOI[] relFois, int colId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isFoiInArray()");
		}
		for (RelatedFOI relatedFOI : relFois) {
			if (relatedFOI.isSetNumber() && 
					relatedFOI.getNumber() == colId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the metadata of the according time&date column.
	 * @param s5aM
	 */
	private void handleStep5aModel(Step5aModel s5aM) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleStep5aModel()");
		}
		Enum key = null;
		String value = null;
		DateAndTime dat = null;
		TableElement tabElem = null;
		Column col = null;
		int columnId;
		int val;
		/*
		 * Get Group Id to get the right column from sosImpConf Check each
		 * element of DateAndTime for TableElement if this is null, Check if the
		 * element is not null, than save it to metadata of this column
		 */
		dat = s5aM.getDateAndTime();
		//
		// get right element from configuration by column id
		// 1. get columnId
		tabElem = this.getTableElementFromDateTime(dat);
		columnId = this.getColumnIdFromTableElement(tabElem);
		//
		// 2. get the right element from configuration
		col = this.getColumnById(columnId);
		//
		// 3. check group
		key = Key.GROUP;
		value = dat.getGroup();
		this.addOrUpdateColumnMetadata(key, value, col);
		//
		// 4.1 check Timezone
		if (dat.getTimeZone() != null &&
				dat.getTimeZone().getTableElement() == null) {
			val = dat.getTimeZone().getValue();
			if(val != Constants.NO_INPUT_INT) {
				key = Key.TIME_ZONE;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.2 check day
		if (dat.getDay() != null &&
				dat.getDay().getTableElement() == null) {
			val = dat.getDay().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_DAY;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.3 check hour
		if (dat.getHour() != null &&
				dat.getHour().getTableElement() == null) {
			val = dat.getHour().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_HOUR;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.4 check minute
		if (dat.getMinute() != null &&
				dat.getMinute().getTableElement() == null) {
			val = dat.getMinute().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_MINUTE;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.5 check month
		if (dat.getMonth() != null &&
				dat.getMonth().getTableElement() == null) {
			val = dat.getMonth().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_MONTH;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.6 check seconds
		if (dat.getSeconds() != null &&
				dat.getSeconds().getTableElement() == null) {
			val = dat.getSeconds().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_SECOND;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.7 check year
		if (dat.getYear() != null &&
				dat.getYear().getTableElement() == null) {
			val = dat.getYear().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.TIME_YEAR;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
	}

	/**
	 * Updates the according position column with the given metadata
	 * @param s5cM
	 */
	private void handleStep5cModel(Step5cModel s5cM) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleStep5cModel()");
		}
		Enum key = null;
		String value = null;
		org.n52.sos.importer.model.position.Position pos = null;
		TableElement tabElem = null;
		Column col = null;
		int columnId;
		double val;

		pos = s5cM.getPosition();
		//
		// get right element from configuration by column id
		// 1. get columnId
		tabElem = this.getTableElementFromPosition(pos);
		columnId = this.getColumnIdFromTableElement(tabElem);
		//
		// 2. get the right element from configuration
		col = this.getColumnById(columnId);
		// 3. check group
		key = Key.GROUP;
		value = pos.getGroup();
		this.addOrUpdateColumnMetadata(key, value, col);
		//
		// 4.1 check latitude
		if (pos.getLatitude() != null &&
				pos.getLatitude().getTableElement() == null) {
			val = pos.getLatitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LATITUDE;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.2 check longitude
		if (pos.getLongitude() != null &&
				pos.getLongitude().getTableElement() == null) {
			val = pos.getLongitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LONGITUDE;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.3 check altitude
		if (pos.getHeight() != null &&
				pos.getHeight().getTableElement() == null) {
			val = pos.getHeight().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_ALTITUDE;
				value = val + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.4 check EPSG code
		if (pos.getEPSGCode() != null &&
				pos.getEPSGCode().getTableElement() == null) {
			int valI = pos.getEPSGCode().getValue();
			if (valI != Constants.NO_INPUT_INT) {
				key = Key.POSITION_EPSG_CODE;
				value = valI + "";
				this.addOrUpdateColumnMetadata(key,value,col);
			}
		}
	}

	/**
	 * Called in the case of not having any date information in the file
	 * @param s6aM
	 */
	private void handleStep6aModel(Step6aModel s6aM) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep6aModel()");
		}
		/*
		 *  we have a date time object and need to save it in a Metadata element
		 *  with key Key.TIME
		 *  
		 *  LOCAL FIELDS
		 */
		DateAndTime dAT;
		String timeStamp;
		Enum key = Key.TIME;
		//
		dAT = s6aM.getDateAndTime();
		timeStamp = this.getTimeStampFromDateAndTime(dAT);
		/*
		 * check if metadata with Key.TIME already exists
		 * if yes -> update
		 * else -> create new element
		 */
		AdditionalMetadata addiMeta = this.sosImpConf.getAdditionalMetadata();
		if(addiMeta == null) {
			addiMeta = this.sosImpConf.addNewAdditionalMetadata();
		}
		// get metadata array and check for Key.TIME
		if(this.addOrUpdateMetadata(key, timeStamp, addiMeta)) {
			if (logger.isInfoEnabled()) {
				logger.info("Timestamp in additional metadata updated/added: "
						+ timeStamp);
			}
		}else {
			logger.fatal("Timestamp element could not be updated");
		}
	}

	/**
	 * Called in the case of having missing foi, observed property, unit of 
	 * measurement, or sensor for any measured value column.
	 * @param s6bM
	 */
	private void handleStep6bModel(Step6bModel s6bM) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep6bModel()");
		}
		/*
		 *	LOCAL FIELDS
		 */
		MeasuredValue mV;
		Resource res;
		int mVColumnID = -1;
		AdditionalMetadata addiMeta;
		Column mVColumn;
		/*
		 * Get column reference from measured value object
		 * Get Resource
		 * Check Type
		 * AddOrUpdate Element in additional metadata and in column meta data
		 */
		mV = s6bM.getMeasuredValue();
		res = s6bM.getResource();
		if (logger.isDebugEnabled()) {
			logger.debug("Found measured value \"" + 
					mV + "\" and a resource \"" +
					res + "\"");
		}
		if (mV != null && mV.getTableElement() != null) {
			mVColumnID = this.getColumnIdFromTableElement(mV.getTableElement());
			if (logger.isDebugEnabled()) {
				logger.debug("Column ID of measured value: " + mVColumnID);
			}
		}
		addiMeta = this.sosImpConf.getAdditionalMetadata();
		if(addiMeta == null) {
			addiMeta = this.sosImpConf.addNewAdditionalMetadata();
		}
		mVColumn = this.getColumnById(mVColumnID);
		if(this.addRelatedResource(res,mVColumn,addiMeta)) {
			if (logger.isInfoEnabled()) {
				logger.info("Related resource updated/added: "
						+ res);
			}
		}
	}

	/**
	 * In the case of having no sensor. This sensor must be related to:
	 * <ol><li>a measured value column</li>
	 * <li>a feature of interest</li>
	 * <li>an observed property</li></ol>
	 * @param s6bSM
	 */
	private void handleStep6bSpecialModel(Step6bSpecialModel s6bSM) {
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep6bSpeicalModel()");
		}
		String foiName = s6bSM.getFeatureOfInterestName(), 
				foiURI = foiName,
				obsPropName = s6bSM.getObservedPropertyName(),
				obsPropURI = obsPropName,
				sensorName, 
				sensorURI;
		org.n52.sos.importer.model.resources.Sensor sensor = s6bSM.getSensor();
		/*
		 * TODO get FOI and obsProp URI. Requires update of GUI.
		 */
		/*
		 * add sensor to model
		 */
		sensorName = sensor.getName();
		sensorURI = sensor.getURIString();
		Sensor sensorXB = null;
		Sensor[] sensorsXB;
		AdditionalMetadata addiMeta = this.sosImpConf.getAdditionalMetadata();
		if (addiMeta == null) {
			addiMeta = this.sosImpConf.addNewAdditionalMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new AdditionalMetadata element");
			}
		} else {
			 sensorsXB = addiMeta.getSensorArray();
			 
			 findSensor: 
			 for (Sensor aSensor : sensorsXB) {
				if (aSensor.getURI().equalsIgnoreCase(sensorURI)) {
					sensorXB = aSensor;
					if (logger.isDebugEnabled()) {
						logger.debug("Found Sensor element");
					}
					break findSensor;
				}
			}
		}
		// sensor found or add new one?
		if (sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Sensor element");
			}
		}
		sensorXB.setName(sensorName);
		sensorXB.setURI(sensorURI);
		/*
		 * add relations
		 */
		sensorXB.setRelatedFOI(foiURI);
		sensorXB.setRelatedObservedProperty(obsPropURI);
		// TODO identify related measured value column and update relation
		
	}

	/**
	 * Store the position for each feature of interest
	 * (either stored in a column or manually selected) 
	 * in case there are not any positions given in the CSV file. Add each to
	 * <code>FOIPositions</code> element.
	 * @param s6cM
	 */
	private void handleStep6cModel(Step6cModel s6cM) {
		// TODO Auto-generated method stub generated on 03.04.2012 around 14:02:27 by eike
		if (logger.isTraceEnabled()) {
			logger.trace("\thandleStep6cModel()");
		}
		/*
		 * 	LOCALE FIELDS
		 */
		String name;
		org.n52.sos.importer.model.resources.FeatureOfInterest foi;
		int colId;
		TableElement tabE;
		logger.fatal("CONTINUE DEVELOPMENT HERE");
		/*
		 * check if FOI is already there
		 */
		foi = s6cM.getFeatureOfInterest();
		foi.getTableElement();
		name = s6cM.getFeatureOfInterestName();
		if (name == null) {
			name = foi.getURIString();
		}

			// if not check if the foi has a table element if not add reference by URI, else number
			// create position
			// add position and use ref from previous step
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

	/**
	 * Checks, if a Metadata element with the given <b>key</b> exists,<br />
	 * 		if <b>yes</b>, update this one, <br />
	 * 		<b>else</b> add a new metadata element.
	 * @param key
	 * @param value
	 * @param col
	 * @return
	 */
	private boolean addOrUpdateColumnMetadata(Enum key, String value, Column col) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\taddOrUpdateColumnMetadata()");
		}
		Metadata[] metaElems = col.getMetadataArray();
		Metadata meta = null;
		String addedOrUpdated = "Updated";
		// check if there is already a element with the given key
		for (Metadata metadata : metaElems) {
			if (metadata.getKey().equals(key) ) {
				meta = metadata;
				break;
			}
		}
		if(meta == null) {
			meta = col.addNewMetadata();
			meta.setKey(key);
			addedOrUpdated = "Added";
		}
		meta.setValue(value);
		if (logger.isDebugEnabled()) {
			logger.debug(addedOrUpdated + " column metadata. Key: " + key + "; Value: " + 
					value + " in column " + col.getNumber());
		}
		return (meta.getValue().equalsIgnoreCase(value));
	}

	private boolean addOrUpdateMetadata(Enum key, 
			String value,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("addOrUpdateMetadata()");
		}
		Metadata[] metaElems = addiMeta.getMetadataArray();
		Metadata meta = null;
		String addedOrUpdated = "Updated";
		// check if there is already a element with the given key
		for (Metadata metadata : metaElems) {
			if (metadata.getKey().equals(key) ) {
				meta = metadata;
				break;
			}
		}
		if(meta == null) {
			meta = addiMeta.addNewMetadata();
			meta.setKey(key);
			addedOrUpdated = "Added";
		}
		meta.setValue(value);
		if (logger.isDebugEnabled()) {
			logger.debug(addedOrUpdated + " additional metadata. Key: " + key + "; Value: " + 
					value);
		}
		return (meta.getValue().equalsIgnoreCase(value));
	}

	private boolean addRelatedFOI(
			org.n52.sos.importer.model.resources.FeatureOfInterest foi, 
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedFOI()");
		}
		//
		FeatureOfInterest foiXB = null;
		FeatureOfInterest[] foisXB = addiMeta.getFeatureOfInterestArray();
		RelatedFOI[] relatedFOIs;
		boolean addNew;
		org.n52.sos.importer.model.position.Position pos;
		Position posXB = null;
		//
		if(foisXB != null && foisXB.length > 0) {
						
			findFOI : 
			for (FeatureOfInterest aFOI : foisXB) {
				if ( aFOI.getURI().equalsIgnoreCase(foi.getURIString()) ) {
					foiXB = aFOI;
					break findFOI;
				}
			}
		
		}
		if(foiXB == null) {
			foiXB = addiMeta.addNewFeatureOfInterest();
		}
		foiXB.setName(foi.getName());
		foiXB.setURI(foi.getURIString());
		// add position to FOI
		pos = foi.getPosition();
		posXB = foiXB.getPosition();
		if(posXB == null) {
			foiXB.addNewPosition();
		}
		this.fillXBPosition(posXB,pos);
		/*
		 * the FOI is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedFOIs = mVColumn.getRelatedFOIArray();
		addNew = !this.isFoiInArray(relatedFOIs,foi.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedFOI().setURI(foi.getURIString());
			if (logger.isDebugEnabled()) {
				logger.debug("Added new related FOI element");
			}
		}
		relatedFOIs = mVColumn.getRelatedFOIArray();
		return this.isFoiInArray(relatedFOIs, foi.getURIString());
	}

	private boolean addRelatedObservedProperty(
			org.n52.sos.importer.model.resources.ObservedProperty obsProp,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedObservedProperty()");
		}
		//
		ObservedProperty obsPropXB = null;
		ObservedProperty[] obsPropsXB = addiMeta.getObservedPropertyArray();
		RelatedObservedProperty[] relatedObsProps;
		RelatedObservedProperty relObs;
		boolean addNew;
		//
		if(obsPropsXB != null && obsPropsXB.length > 0) {
						
			findObservedProperty : 
			for (ObservedProperty obsPropy : obsPropsXB) {
				if (obsPropy.getURI().equalsIgnoreCase(obsProp.getURIString())) {
					obsPropXB = obsPropy;
					break findObservedProperty;
				}
			}
		
		}
		if(obsPropXB == null) {
			obsPropXB = addiMeta.addNewObservedProperty();
		}
		obsPropXB.setName(obsProp.getName());
		obsPropXB.setURI(obsProp.getURIString());
		/*
		 * the ObservedProperty is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		addNew = !this.isObsPropInArray(relatedObsProps,obsProp.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedObservedProperty().setURI(obsProp.getURIString());
		}
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		return this.isObsPropInArray(relatedObsProps, obsProp.getURIString());
	}

	/**
	 * Check and add/update additional metadata element and ColumnMetadata 
	 * measure value column having id <code>mVColumnID</code>.
	 * @param res the related <code>Resource</code>
	 * @param mVColumnID
	 * @return
	 */
	private boolean addRelatedResource(Resource res, Column mVColumn, AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\taddRelatedResource()");
		}
		/*
		 * 	ADD FEATURE_OF_INTEREST
		 */
		if (res instanceof org.n52.sos.importer.model.resources.FeatureOfInterest) {
			org.n52.sos.importer.model.resources.FeatureOfInterest foi = 
					(org.n52.sos.importer.model.resources.FeatureOfInterest) res;
			return this.addRelatedFOI(foi,mVColumn,addiMeta);
		}
		/*
		 * 	ADD OBSERVED_PROPERTY
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.ObservedProperty) {
			org.n52.sos.importer.model.resources.ObservedProperty obsProp = 
					(org.n52.sos.importer.model.resources.ObservedProperty) res;
			return this.addRelatedObservedProperty(obsProp,mVColumn,addiMeta);
		}
		/*
		 * 	ADD SENSOR
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
			org.n52.sos.importer.model.resources.Sensor sensor = 
					(org.n52.sos.importer.model.resources.Sensor) res;
			return this.addRelatedSensor(sensor,mVColumn,addiMeta);
		}
		/*
		 * 	ADD UNIT_OF_MEASUREMENT
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.UnitOfMeasurement) {
			org.n52.sos.importer.model.resources.UnitOfMeasurement uOM = 
					(org.n52.sos.importer.model.resources.UnitOfMeasurement) res;
			return this.addRelatedUOM(uOM,mVColumn,addiMeta);
		}
		return false;
	}

	private boolean addRelatedSensor(
			org.n52.sos.importer.model.resources.Sensor sensor,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedSensor()");
		}
		//
		Sensor sensorXB = null;
		Sensor[] sensorsXB = addiMeta.getSensorArray();
		RelatedSensor[] relatedSensors;
		boolean addNew;
		//
		if(sensorsXB != null && sensorsXB.length > 0) {
						
			findSensor : 
			for (Sensor aSensor : sensorsXB) {
				if (aSensor.getURI().equalsIgnoreCase(sensor.getURIString())) {
					sensorXB = aSensor;
					break findSensor;
				}
			}
		
		}
		if(sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
		}
		sensorXB.setName(sensor.getName());
		sensorXB.setURI(sensor.getURIString());
		/*
		 * the Sensor is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedSensors = mVColumn.getRelatedSensorArray();
		addNew = !this.isSensorInArray(relatedSensors,sensor.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedSensor().setURI(sensor.getURIString());
		}
		relatedSensors = mVColumn.getRelatedSensorArray();
		return this.isSensorInArray(relatedSensors, sensor.getURIString());
	}

	private boolean addRelatedUOM(
			org.n52.sos.importer.model.resources.UnitOfMeasurement uOM,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedUOM()");
		}
		//
		UnitOfMeasurement uOMXB = null;
		UnitOfMeasurement[] uOMsXB = addiMeta.getUnitOfMeasurementArray();
		RelatedUnitOfMeasurement[] relatedUOMs;
		boolean addNew;
		//
		if(uOMsXB != null && uOMsXB.length > 0) {
						
			findUOM : 
			for (UnitOfMeasurement uom : uOMsXB) {
				if (uom.getURI().equalsIgnoreCase(uOM.getURIString())) {
					uOMXB = uom;
					break findUOM;
				}
			}
		
		}
		if(uOMXB == null) {
			uOMXB = addiMeta.addNewUnitOfMeasurement();
		}
		uOMXB.setName(uOM.getName());
		uOMXB.setURI(uOM.getURIString());
		uOMXB.setCode(uOM.getName());
		/*
		 * the UOM is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		addNew = !this.isUOMInArray(relatedUOMs,uOM.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedUnitOfMeasurement().setURI(uOM.getURIString());
		}
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		return this.isUOMInArray(relatedUOMs, uOM.getURIString());
	}

	private void fillXBPosition(Position posXB,
			org.n52.sos.importer.model.position.Position pos) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\taddOrUpdatePosition()");
		}
		if (pos == null || posXB == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("One position is null: skip filling: pos? " + pos
						+ "; posXB? " + posXB);
			}
			return;
		}
		/*
		 * 	EPSG_CODE
		 */
		posXB.setEPSGCode(pos.getEPSGCode().getValue());
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.epsg: " + posXB.getEPSGCode() + 
					" - pos.epsg: " + pos.getEPSGCode().getValue());
		}
		/*
		 * 	ALTITUDE
		 */
		Alt alt = posXB.getAlt();
		if (alt == null) {
			alt = posXB.addNewAlt();
		}
		alt.setFloatValue( new Double(pos.getHeight().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.alt: " + posXB.getAlt() + 
					" - pos.alt: " + pos.getHeight().getValue());
		}
		/*
		 * 	LATITUDE
		 */
		Lat lat = posXB.getLat();
		if (lat == null) {
			lat = posXB.addNewLat();
		}
		lat.setFloatValue( new Double(pos.getLatitude().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.lat: " + posXB.getLat() + 
					" - pos.lat: " + pos.getLatitude().getValue());
		}
		/*
		 *	LONGITUDE
		 */
		Long lon = posXB.getLong();
		if (lon == null) {
			lon = posXB.addNewLong();
		}
		lon.setFloatValue( new Double(pos.getLongitude().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.lon: " + posXB.getLong() + 
					" - pos.lon: " + pos.getLongitude().getValue());
		}
	}

	private boolean isFoiInArray(RelatedFOI[] relatedFOIs, String foiURI) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisFoiInArray()");
		}
		for (RelatedFOI relatedFoiFromArray : relatedFOIs) {
			if (relatedFoiFromArray.isSetURI() && 
					relatedFoiFromArray.getURI().equalsIgnoreCase(foiURI) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isObsPropInArray(RelatedObservedProperty[] relatedObsProps,
			String obsPropURI) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisObsPropInArray()");
		}
		for (RelatedObservedProperty relatedObsPropFromArray : relatedObsProps) {
			if (relatedObsPropFromArray.isSetURI() && 
					relatedObsPropFromArray.getURI().equalsIgnoreCase(obsPropURI) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSensorInArray(RelatedSensor[] relatedSensors,
			String sensorURIString) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisSensorInArray()");
		}
		for (RelatedSensor relatedSensorFromArray : relatedSensors) {
			if (relatedSensorFromArray.isSetURI() && 
					relatedSensorFromArray.getURI().equalsIgnoreCase(sensorURIString) ) {
				return true;
			}
		}
		return false;
	}

	private boolean isUOMInArray(RelatedUnitOfMeasurement[] relatedUOMs,
			String uomUriString) {
		if (logger.isTraceEnabled()) {
			logger.trace("isUOMInArray()");
		}
		for (RelatedUnitOfMeasurement relatedUOMFromArray : relatedUOMs) {
			if (relatedUOMFromArray.isSetURI() && 
					relatedUOMFromArray.getURI().equalsIgnoreCase(uomUriString) ) {
				return true;
			}
		}
		return false;
	}

	private boolean isIntInArray(int[] array, int i) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisIntInArray()");
		}
		for (int intFromArray : array) {
			if (intFromArray == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param columnId
	 * @return the Column from the configuration having id columnId
	 */
	private Column getColumnById(int columnId) {
		if (logger.isTraceEnabled()) {
			logger.trace("getColumnById()");
		}
		CsvMetadata csvMeta = this.sosImpConf.getCsvMetadata();
		if (csvMeta != null) {
			ColumnAssignments colAssignMnts = csvMeta.getColumnAssignments();
			if (colAssignMnts != null) {
				Column[] cols = colAssignMnts.getColumnArray();
				if (cols != null && cols.length > 0) {
					// now we have the columns, iterate and check the id
					// return the one with the required one
					for (Column col : cols) {
						if (col.getNumber() == columnId) {
							return col;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param tabElem
	 * @return the id of the column of this TableElement or -1
	 */
	private int getColumnIdFromTableElement(TableElement tabElem) {
		if (logger.isTraceEnabled()) {
			logger.trace("getColumnIdFromTableElement()");
		}
		if (tabElem == null) {
			return -1;
		}
		if (tabElem instanceof Cell) {
			Cell c = (Cell) tabElem;
			return c.getColumn();
		} else if (tabElem instanceof org.n52.sos.importer.model.table.Column) {
			org.n52.sos.importer.model.table.Column c = (org.n52.sos.importer.model.table.Column) tabElem;
			return c.getNumber();
			// TODO What is the reason for having it in rows?
		} else if (tabElem instanceof Row) {
			logger.error("Element is stored in rows. NOT YET IMPLEMENTED");
			return -1;
		}
		return -1;
	}

	private TableElement getTableElementFromDateTime(DateAndTime dat) {
		if (logger.isTraceEnabled()) {
			logger.trace("getTableElementFromDateTime()");
		}
		TableElement tabElem = null;
		if (dat.getDay() != null && dat.getDay().getTableElement() != null) {
	
			tabElem = dat.getDay().getTableElement();
	
		} else if (dat.getHour() != null
				&& dat.getHour().getTableElement() != null) {
			tabElem = dat.getHour().getTableElement();
	
		} else if (dat.getMinute() != null
				&& dat.getMinute().getTableElement() != null) {
	
			tabElem = dat.getMinute().getTableElement();
	
		} else if (dat.getMonth() != null
				&& dat.getMonth().getTableElement() != null) {
	
			tabElem = dat.getMonth().getTableElement();
	
		} else if (dat.getSeconds() != null
				&& dat.getSeconds().getTableElement() != null) {
	
			tabElem = dat.getSeconds().getTableElement();
	
		} else if (dat.getTimeZone() != null
				&& dat.getTimeZone().getTableElement() != null) {
	
			tabElem = dat.getTimeZone().getTableElement();
	
		} else if (dat.getYear() != null
				&& dat.getYear().getTableElement() != null) {
	
			tabElem = dat.getYear().getTableElement();
	
		}
		return tabElem;
	}

	private TableElement getTableElementFromPosition(
			org.n52.sos.importer.model.position.Position pos) {
		if (logger.isTraceEnabled()) {
			logger.trace("getTableElementFromPosition()");
		}
		TableElement tabElem = null;
		if (pos.getEPSGCode() != null && pos.getEPSGCode().getTableElement() != null) {
			
			tabElem = pos.getEPSGCode().getTableElement();
			
		} else if (pos.getLongitude() != null && pos.getLongitude().getTableElement() != null) {
			
			tabElem = pos.getLongitude().getTableElement();
			
		} else if (pos.getLatitude() != null && pos.getLatitude().getTableElement() != null) {
			
			tabElem = pos.getLatitude().getTableElement();
			
		} else if (pos.getHeight() != null && pos.getHeight().getTableElement() != null) {
			
			tabElem = pos.getHeight().getTableElement();
			
		}
		return tabElem;
	}

	/**
	 * Check for each date element is the given <code>DateAndTime</code> object
	 * and create final string for the time stamp using this rule:<br/>
	 * IF
	 * <ul><li><b>null</b> -> add default value to String</li>
	 * <li><b>else</b> -> add value to String</li></ul>
	 * Used format: <code>"yyyy-MM-dd'T'HH:mm:ssZ"</code><br/>
	 * The colon bug is fixed, so the time zone looks like "<code>+02:00</code>" for example.<br/>
	 * Default value is: "<code>1970-01-01T00:00:00:+00:00</code>"
	 * @param dAT
	 * @return a String or null, if dAT is null.
	 */
	private String getTimeStampFromDateAndTime(DateAndTime dAT) {
		if (logger.isTraceEnabled()) {
			logger.trace("getTimeStampFromDateAndTime()");
		}
		if(dAT == null) {
			return null;
		}
		/*
		 * 	LOCAL FIELDS
		 */
		GregorianCalendar cal;
		int year = 1970, 
				month = 0, 
				dayOfMonth = 1, 
				hourOfDay = 0, 
				minute = 0, 
				seconds = 0,
				timezone = 0;
		String timeStamp, timeZoneString, sign;
		SimpleDateFormat format;
		TimeZone tz;
		/*
		 * 	YEAR
		 */
		if(dAT.getYear() != null) {
			if(dAT.getYear().getValue() != Integer.MIN_VALUE) {
				year = dAT.getYear().getValue();
			}
		}
		/*
		 * 	MONTH
		 */
		if(dAT.getMonth() != null) {
			if(dAT.getMonth().getValue() != Integer.MIN_VALUE) {
				month = dAT.getMonth().getValue();
			}
		}
		/*
		 * DAY OF MONTH
		 */
		if(dAT.getDay() != null) {
			if(dAT.getDay().getValue() != Integer.MIN_VALUE) {
				dayOfMonth = dAT.getDay().getValue();
			}
		}
		/*
		 * HOUR OF DAY
		 */
		if(dAT.getHour() != null) {
			if(dAT.getHour().getValue() != Integer.MIN_VALUE) {
				hourOfDay = dAT.getHour().getValue();
			}
		}
		/*
		 * 	MINUTE
		 */
		if(dAT.getMinute() != null) {
			if(dAT.getMinute().getValue() != Integer.MIN_VALUE) {
				minute = dAT.getMinute().getValue();
			}
		}
		/*
		 * SECONDS
		 */
		if(dAT.getSeconds() != null) {
			if(dAT.getSeconds().getValue() != Integer.MIN_VALUE) {
				seconds = dAT.getSeconds().getValue();
			}
		}
		/*
		 * 	TIMEZONE
		 */
		if(dAT.getTimeZone() != null) {
			if(dAT.getTimeZone().getValue() != Integer.MIN_VALUE) {
				timezone = dAT.getTimeZone().getValue();
			}
		}
		// Get right timezone
		timeZoneString = "GMT";
		sign = "+";
		if(timezone <= 0) {
			sign = "";
		}
		timeZoneString = timeZoneString + sign + timezone;
		if (logger.isDebugEnabled()) {
			logger.debug("timeZoneString: " + timeZoneString);
		}
		tz = TimeZone.getTimeZone(timeZoneString);
		if (logger.isDebugEnabled()) {
			logger.debug("TimeZone: " + tz);
		}
		cal = new GregorianCalendar(tz);
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
		cal.set(GregorianCalendar.MINUTE, minute);
		cal.set(GregorianCalendar.SECOND, seconds);
		format = new SimpleDateFormat(Constants.DATE_FORMAT_STRING);
		format.setTimeZone(tz);
		timeStamp = format.format(cal.getTime());
		/*
		 * 	FIX colon bug
		 */
		timeStamp = timeStamp.substring(0, timeStamp.length()-2) + ":" + timeStamp.substring(timeStamp.length()-2);
		return timeStamp;
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
			String[] splittedEncodedMetadata = encodedMetadata
					.split(Constants.SEPARATOR_STRING);
			// order of the next two params is defined in
			// org.n52.sos.importer.view.step3.NumericValuePanel
			String decimalSepartor = splittedEncodedMetadata[0], thousandsSeparator = splittedEncodedMetadata[1];
			//
			value = Constants.NUMERIC;
			//
			this.addOrUpdateColumnMetadata(Key.DECIMAL_SEPARATOR, decimalSepartor, col);
			//
			this.addOrUpdateColumnMetadata(Key.THOUSANDS_SEPARATOR, thousandsSeparator, col);
			//
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValBoolean())) {
			value = Constants.BOOLEAN;
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValCount())) {
			value = Constants.COUNT;
		} else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValText())) {
			value = Constants.TEXT;
		}
		this.addOrUpdateColumnMetadata(Key.TYPE, value, col);
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
			this.addOrUpdateColumnMetadata(Key.GROUP, group, col);
			//
			this.addOrUpdateColumnMetadata(Key.PARSE_PATTERN,parsePattern, col);
			//
			this.addOrUpdateColumnMetadata(Key.TYPE,Constants.COMBINATION,col);
			//
			splittedMetadata = null;
			parsePattern = null;
			group = null;
		} else if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeUnixTime())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unix time selected");
			}
			this.addOrUpdateColumnMetadata(Key.TYPE, Constants.UNIX_TIME,col);
		}
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
			this.setComplexColumnTypeDateAndTime(col, 
					Lang.l().step3DateAndTimeUnixTime(), null);
		} else {
			logger.error("Type not known to schema : " + type);
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

	/*
	 * 
	 * Private Helper methods
	 */
	private ArrayList<StepModel> createArrayListFromArray(StepModel[] models) {
		if (logger.isTraceEnabled()) {
			logger.trace("\tcreateArrayListFromArray()");
		}
		//
		ArrayList<StepModel> result;
		//
		result = new ArrayList<StepModel>(stepModells.length + 1);
		for (StepModel stepModel : this.stepModells) {
			if (stepModel != null) {
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
