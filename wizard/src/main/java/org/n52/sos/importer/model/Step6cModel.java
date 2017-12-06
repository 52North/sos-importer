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
 */
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6cModel implements StepModel {

    private FeatureOfInterest featureOfInterest;

    /** a feature of interest name in the feature of interest column/row */
    private String featureOfInterestName;

    private Position position;

    private List<Component> missingPositionComponents;

    /**
     * <p>Constructor for Step6cModel.</p>
     *
     * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     */
    public Step6cModel(FeatureOfInterest featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
        position = this.featureOfInterest.getPosition() != null
                ? this.featureOfInterest.getPosition()
                        : new Position();
        missingPositionComponents = new ArrayList<>();
    }

    /**
     * <p>Constructor for Step6cModel.</p>
     *
     * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     * @param featureOfInterestName a {@link java.lang.String} object.
     */
    public Step6cModel(FeatureOfInterest featureOfInterest, String featureOfInterestName) {
        this(featureOfInterest);
        this.featureOfInterestName = featureOfInterestName;
    }

    /**
     * <p>Setter for the field <code>featureOfInterest</code>.</p>
     *
     * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     */
    public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
    }

    /**
     * <p>Getter for the field <code>featureOfInterest</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     */
    public FeatureOfInterest getFeatureOfInterest() {
        return featureOfInterest;
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
     * <p>Setter for the field <code>featureOfInterestName</code>.</p>
     *
     * @param featureOfInterestName a {@link java.lang.String} object.
     */
    public void setFeatureOfInterestName(String featureOfInterestName) {
        this.featureOfInterestName = featureOfInterestName;
    }

    /**
     * <p>Getter for the field <code>featureOfInterestName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeatureOfInterestName() {
        return featureOfInterestName;
    }

    /**
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return Lang.l().step6cModelDescription();
    }

    /**
     * saves the components which are missing for this step
     * (= all position components)
     *
     * @param missingPositionComponents a {@link java.util.List} object.
     */
    public void setMissingPositionComponents(List<Component> missingPositionComponents) {
        this.missingPositionComponents = missingPositionComponents;
    }

    /**
     * returns the components which were missing for this step
     * (= all position components)
     *
     * @return a {@link java.util.List} object.
     */
    public List<Component> getMissingPositionComponents() {
        return missingPositionComponents;
    }

}
