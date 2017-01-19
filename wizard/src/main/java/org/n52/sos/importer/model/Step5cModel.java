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

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.i18n.Lang;

public class Step5cModel implements StepModel {

    private Position position;

    private List<Component> missingPositionComponents;

    /**
     * <p>Constructor for Step5cModel.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Step5cModel(Position position) {
        this.setPosition(position);
        setMissingPositionComponents(new ArrayList<Component>());
    }

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * <p>Getter for the field <code>position</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return Lang.l().step5cModelDescription();
    }

    /**
     * saves the components which are missing for this step
     *
     * @param missingPositionComponents a {@link java.util.List} object.
     */
    public void setMissingPositionComponents(List<Component> missingPositionComponents) {
        this.missingPositionComponents = missingPositionComponents;
    }

    /**
     * returns the components which were missing for this step
     *
     * @return a {@link java.util.List} object.
     */
    public List<Component> getMissingPositionComponents() {
        return missingPositionComponents;
    }
}
