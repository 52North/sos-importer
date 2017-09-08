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

import org.n52.sos.importer.model.StepModel;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * <p>ModelHandler interface.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public interface ModelHandler<T extends StepModel> {
    /**
     * Handles the given StepModel.<br>
     * The given SosImportConfiguration is updated with the values from the
     * given StepModel.
     *
     * @param stepModel The StepModel to update the SosImportConfiguation
     * @param sosImportConf The SosImportConfiguration to be updated with the given StepModel
     */
    void handleModel(T stepModel, SosImportConfiguration sosImportConf);

    /**
     * @param number
     *            the number of the column in the data file
     * @param cols
     *            all columns in the configuration
     * @return the
     *         <code>org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column</code>
     *         with the given number
     */
    default Column getColumnForKey(final int number, final Column[] cols) {
        for (final Column col : cols) {
            if (col.getNumber() == number) {
                return col;
            }
        }
        return null;
    }
}
