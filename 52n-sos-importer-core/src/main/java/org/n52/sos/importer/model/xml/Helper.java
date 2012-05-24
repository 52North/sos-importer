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

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.model.table.TableElement;
import org.x52North.sensorweb.sos.importer.x02.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Offering methods used by different ModelHandler classes.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Helper {
	
	private static final Logger logger = Logger.getLogger(Helper.class);
	
	/**
	 * Checks, if a Metadata element with the given <b>key</b> exists,<br />
	 * 		if <b>yes</b>, update this one, <br />
	 * 		<b>else</b> add a new metadata element.
	 * @param key
	 * @param value
	 * @param col
	 * @return
	 */
	protected static boolean addOrUpdateColumnMetadata(Enum key, 
			String value, 
			Column col) {
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
	
	/**
	 * @param tabElem
	 * @return the id of the column of this TableElement or -1
	 */
	protected static int getColumnIdFromTableElement(TableElement tabElem) {
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
	
	/**
	 * @param columnId
	 * @param sosImportConf
	 * @return the Column from the configuration having id columnId
	 */
	protected static Column getColumnById(int columnId,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("getColumnById()");
		}
		CsvMetadata csvMeta = sosImportConf.getCsvMetadata();
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
	 * @param relatedSensors
	 * @param sensorXmlId
	 * @return <b>true</b>, if the <code>sensorXmlId</code> is contained in the 
	 * 		given <code>RelatedSensors[]</code> , <br />
	 * 		else <b>false</b>
	 */
	protected static boolean isSensorInArray(RelatedSensor[] relatedSensors,
			String sensorXmlId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isSensorInArray()");
		}
		for (RelatedSensor relatedSensorFromArray : relatedSensors) {
			if (relatedSensorFromArray.isSetIdRef() && 
					relatedSensorFromArray.getIdRef().equalsIgnoreCase(sensorXmlId) ) {
				return true;
			}
		}
		return false;
	}
	
	


}
