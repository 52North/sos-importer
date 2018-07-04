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
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Updates the according position column with the given metadata
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step5cModelHandler implements ModelHandler<Step5cModel> {

    private static final Logger logger = LoggerFactory.getLogger(Step5cModelHandler.class);

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
        col = Helper.getColumnById(columnId, sosImportConf);
        // 3. check group
        key = Key.GROUP;
        value = pos.getGroup();
        Helper.addOrUpdateColumnMetadata(key, value, col);
        //
        // 4.1 check COORD_0
        if (pos.getCoordinate(Id.COORD_0) != null &&
                pos.getCoordinate(Id.COORD_0).getTableElement() == null) {
            final double val = pos.getCoordinate(Id.COORD_0).getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.POSITION_COORD_0;
                value = val + " " + pos.getCoordinate(Id.COORD_0).getParsedUnit();
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.2 check COORD_1
        if (pos.getCoordinate(Id.COORD_1) != null &&
                pos.getCoordinate(Id.COORD_1).getTableElement() == null) {
            final double val = pos.getCoordinate(Id.COORD_1).getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.POSITION_COORD_1;
                value = val + " " + pos.getCoordinate(Id.COORD_1).getParsedUnit();
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.3 check COORD_2
        if (pos.getCoordinate(Id.COORD_2) != null &&
                pos.getCoordinate(Id.COORD_2).getTableElement() == null) {
            final double val = pos.getCoordinate(Id.COORD_2).getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.POSITION_COORD_2;
                value = val + " " + pos.getCoordinate(Id.COORD_2).getParsedUnit();
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.4 check EPSG code
        if (pos.getEPSGCode() != null &&
                pos.getEPSGCode().getTableElement() == null) {
            final int val = pos.getEPSGCode().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.POSITION_EPSG_CODE;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
    }

    private TableElement getTableElementFromPosition(
            final org.n52.sos.importer.model.position.Position pos) {
        if (logger.isTraceEnabled()) {
            logger.trace("getTableElementFromPosition()");
        }
        if (pos.getEPSGCode() != null && pos.getEPSGCode().getTableElement() != null) {
            return pos.getEPSGCode().getTableElement();
        } else {
            for (Id id : Id.values()) {
                if (pos.getCoordinate(id) != null && pos.getCoordinate(id).getTableElement() != null) {
                    return pos.getCoordinate(id).getTableElement();
                }
            }
        }
        return null;
    }
}
