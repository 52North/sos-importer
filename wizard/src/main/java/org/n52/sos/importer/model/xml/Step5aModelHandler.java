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
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Updates the metadata of the according time&amp;date column.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step5aModelHandler implements ModelHandler<Step5aModel> {

    private static final Logger logger = LoggerFactory.getLogger(Step5aModelHandler.class);

    /** {@inheritDoc} */
    @Override
    public void handleModel(final Step5aModel stepModel,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleModel()");
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
        dat = stepModel.getDateAndTime();
        //
        // get right element from configuration by column id
        // 1. get columnId
        tabElem = getTableElementFromDateTime(dat);
        columnId = Helper.getColumnIdFromTableElement(tabElem);
        //
        // 2. get the right element from configuration
        col = Helper.getColumnById(columnId, sosImportConf);
        //
        // 3. check group
        key = Key.GROUP;
        value = dat.getGroup();
        Helper.addOrUpdateColumnMetadata(key, value, col);
        //
        // 4.1 check Timezone
        if (dat.getTimeZone() != null &&
                dat.getTimeZone().getTableElement() == null) {
            val = dat.getTimeZone().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_ZONE;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.2 check day
        if (dat.getDay() != null &&
                dat.getDay().getTableElement() == null) {
            val = dat.getDay().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_DAY;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.3 check hour
        if (dat.getHour() != null &&
                dat.getHour().getTableElement() == null) {
            val = dat.getHour().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_HOUR;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.4 check minute
        if (dat.getMinute() != null &&
                dat.getMinute().getTableElement() == null) {
            val = dat.getMinute().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_MINUTE;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.5 check month
        if (dat.getMonth() != null &&
                dat.getMonth().getTableElement() == null) {
            val = dat.getMonth().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_MONTH;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.6 check seconds
        if (dat.getSeconds() != null &&
                dat.getSeconds().getTableElement() == null) {
            val = dat.getSeconds().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_SECOND;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
        // 4.7 check year
        if (dat.getYear() != null &&
                dat.getYear().getTableElement() == null) {
            val = dat.getYear().getValue();
            if (val != Constants.NO_INPUT_INT) {
                key = Key.TIME_YEAR;
                value = val + "";
                Helper.addOrUpdateColumnMetadata(key, value, col);
            }
        }
    }

    private TableElement getTableElementFromDateTime(final DateAndTime dat) {
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
}
