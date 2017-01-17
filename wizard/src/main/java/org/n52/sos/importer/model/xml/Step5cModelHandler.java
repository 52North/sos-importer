/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model.xml;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x04.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x04.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Updates the according position column with the given metadata
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step5cModelHandler implements ModelHandler<Step5cModel> {

	private static final Logger logger = LoggerFactory.getLogger(Step5cModelHandler.class);

	/** {@inheritDoc} */
	@Override
	public void handleModel(final Step5cModel stepModel,
			final SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		Enum key = null;
		String value = null;
		org.n52.sos.importer.model.position.Position pos = null;
		TableElement tabElem = null;
		Column col = null;
		int columnId;

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
			final double val = pos.getLatitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LATITUDE;
				value = val + " " + pos.getLatitude().getParsedUnit();
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.2 check longitude
		if (pos.getLongitude() != null &&
				pos.getLongitude().getTableElement() == null) {
			final double val = pos.getLongitude().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_LONGITUDE;
				value = val + " " + pos.getLongitude().getParsedUnit();
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.3 check altitude
		if (pos.getHeight() != null &&
				pos.getHeight().getTableElement() == null) {
			final double val = pos.getHeight().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_ALTITUDE;
				value = val + " " + pos.getHeight().getParsedUnit();
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
		// 4.4 check EPSG code
		if (pos.getEPSGCode() != null &&
				pos.getEPSGCode().getTableElement() == null) {
			final int val = pos.getEPSGCode().getValue();
			if (val != Constants.NO_INPUT_INT) {
				key = Key.POSITION_EPSG_CODE;
				value = val + "";
				Helper.addOrUpdateColumnMetadata(key,value,col);
			}
		}
	}

	private TableElement getTableElementFromPosition(
			final org.n52.sos.importer.model.position.Position pos) {
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
