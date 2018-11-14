/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step5aModel implements StepModel {


    private DateAndTime dateAndTime;

    private List<Component> missingDateAndTimeComponents;

    /**
     * <p>Constructor for Step5aModel.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public Step5aModel(DateAndTime dateAndTime) {
        this.dateAndTime = dateAndTime;
        missingDateAndTimeComponents = new ArrayList<>();
    }

    /**
     * <p>setDateAndTimeModel.</p>
     *
     * @param newDateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public void setDateAndTimeModel(DateAndTime newDateAndTime) {
        this.dateAndTime = newDateAndTime;
    }

    /**
     * <p>Getter for the field <code>dateAndTime</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public DateAndTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return Lang.l().step5aModelDescription();
    }

    /**
     * saves the components which are missing for this step
     *
     * @param missingDateAndTimeComponents a {@link java.util.List} object.
     */
    public void setMissingDateAndTimeComponents(List<Component> missingDateAndTimeComponents) {
        this.missingDateAndTimeComponents = missingDateAndTimeComponents;
    }

    /**
     * returns the components which were missing for this step
     *
     * @return a {@link java.util.List} object.
     */
    public List<Component> getMissingDateAndTimeComponents() {
        return missingDateAndTimeComponents;
    }

}
