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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;
import org.x52North.sensorweb.sos.importer.x02.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x02.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * During this step, the relations between resource columns and the measured
 * value column are saved.
 * 
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * 
 * TODO check functionality
 *
 */
public class Step4bModelHandler implements ModelHandler<Step4bModel> {

	private static final Logger logger = Logger.getLogger(Step4bModelHandler.class);
	
	@Override
	public void handleModel(Step4bModel s4M,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
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
		relatedColumnsIds = s4M.getSelectedColumns();
		csvMeta = sosImportConf.getCsvMetadata();
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
			if(isIntInArray(relatedColumnsIds, column.getNumber()) ) {
				// add column to result set	
				relCol.add(column);
			}
		}
		relCol.trimToSize();
		relatedCols = relCol.toArray(new Column[relCol.size()]);
		
		// identify type of resource that is linked to the given row and or columns
		res = s4M.getResource();
		
		// add relation to the related column
		addRelatedResourceColumn(res, relatedCols);
		
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
				addNew = !isFoiColIdInArray(relFois, colId);
				if (addNew) {
					column.addNewRelatedFOI().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related foi by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related foi was already there");
				}
				result = result && isFoiColIdInArray(relFois, colId);
			} else
			/*
			 * 	SENSOR
			 */
			if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
				RelatedSensor[] relSensors = column.getRelatedSensorArray();
				addNew = !isSensorInArray(relSensors, colId);
				if (addNew) {
					column.addNewRelatedSensor().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related sensor by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related sensor was already there");
				}
				result = result && isSensorInArray(relSensors, colId);
			} else
			/*
			 * 	UNIT_OF_MEASUREMENT
			 */
			if (res instanceof 
					org.n52.sos.importer.model.resources.UnitOfMeasurement) {
				RelatedUnitOfMeasurement[] relUOMs = column.getRelatedUnitOfMeasurementArray();
				addNew = !isUOMInArray(relUOMs, colId);
				if (addNew) {
					column.addNewRelatedUnitOfMeasurement().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related UOM by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related UOM was already there");
				}
				result = result && isUOMInArray(relUOMs, colId);
			} else
			/*
			 * 	OBSERVED_PROPERTY
			 */
			if (res instanceof 
					org.n52.sos.importer.model.resources.ObservedProperty) {
				RelatedObservedProperty[] relObsProps = column.getRelatedObservedPropertyArray();
				addNew = !isObsPropInArray(relObsProps, colId);
				if (addNew) {
					column.addNewRelatedObservedProperty().setNumber(colId);
					if (logger.isDebugEnabled()) {
						logger.debug("Added new related observed property by column");
					}
				} else if (logger.isDebugEnabled()) {
					logger.debug("Related observed property was already there");
				}
				result = result && isObsPropInArray(relObsProps, colId);
			}
		}
		return result;
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


}
