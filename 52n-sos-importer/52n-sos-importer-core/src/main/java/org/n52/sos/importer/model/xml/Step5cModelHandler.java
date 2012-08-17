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
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.table.TableElement;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Updates the according position column with the given metadata
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step5cModelHandler implements ModelHandler<Step5cModel> {
	
	private static final Logger logger = Logger.getLogger(Step5cModelHandler.class);

	@Override
	public void handleModel(Step5cModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		Enum key = null;
		String value = null;
		org.n52.sos.importer.model.position.Position pos = null;
		TableElement tabElem = null;
		Column col = null;
		int columnId;
		double val;
	
		pos = stepModel.getPosition();
		//
		// get right element from configuration by column id
		// 1. get columnId
		tabElem = getTableElementFromPosition(pos);
		columnId = Helper.getColumnIdFromTableElement(tabElem);
		//
		// 2. get the right element from configuration
		col = Helper.getColumnById(columnId,sosImportConf);
		// 3. check group
		key = Key.GROUP;
		value = pos.getGroup();
		Helper.addOrUpdateColumnMetadata(key, value, col);
		//
		// 4.1 check latitude
		if (pos.getLatitude() != null &&
				pos.getLatitude().getTableElement() == null) {
			val = pos.getLatitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LATITUDE;
				value = val + "";
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.2 check longitude
		if (pos.getLongitude() != null &&
				pos.getLongitude().getTableElement() == null) {
			val = pos.getLongitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LONGITUDE;
				value = val + "";
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.3 check altitude
		if (pos.getHeight() != null &&
				pos.getHeight().getTableElement() == null) {
			val = pos.getHeight().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_ALTITUDE;
				value = val + "";
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.4 check EPSG code
		if (pos.getEPSGCode() != null &&
				pos.getEPSGCode().getTableElement() == null) {
			int valI = pos.getEPSGCode().getValue();
			if (valI != Constants.NO_INPUT_INT) {
				key = Key.POSITION_EPSG_CODE;
				value = valI + "";
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
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

}
