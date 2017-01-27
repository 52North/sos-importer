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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
package org.n52.sos.importer.model;

import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.view.i18n.Lang;

public class Step4bModel implements StepModel {

    private Resource resource;

    private int[] selectedColumns;

    private int firstLineWithData = -1;

    /**
     * <p>Constructor for Step4bModel.</p>
     *
     * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
     * @param firstLineWithData a int.
     */
    public Step4bModel(Resource resource, int firstLineWithData) {
        this.resource = resource;
        this.firstLineWithData = firstLineWithData;
        this.selectedColumns = new int[0];
    }

    /**
     * <p>Setter for the field <code>resource</code>.</p>
     *
     * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * <p>Getter for the field <code>resource</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return Lang.l().step4bModelDescription();
    }

    /**
     * <p>Setter for the field <code>selectedColumns</code>.</p>
     *
     * @param selectedColumns an array of int.
     */
    public void setSelectedColumns(int[] selectedColumns) {
        this.selectedColumns = selectedColumns.clone();
    }

    /**
     * <p>Getter for the field <code>selectedColumns</code>.</p>
     *
     * @return an array of int.
     */
    public int[] getSelectedColumns() {
        return selectedColumns.clone();
    }

    /**
     * <p>Getter for the field <code>firstLineWithData</code>.</p>
     *
     * @return the firstLineWithData
     */
    public int getFirstLineWithData() {
        return firstLineWithData;
    }

    /**
     * <p>Setter for the field <code>firstLineWithData</code>.</p>
     *
     * @param firstLineWithData the firstLineWithData to set
     */
    public void setFirstLineWithData(int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
    }
}
