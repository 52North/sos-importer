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
package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Component;

/**
 * Represents the view of a component (e.g. a day) which
 * is not available in the CSV file; therefore, it has to
 * be chosen manually
 *
 * @author Raimund Schn&uuml;rer
 */
public abstract class MissingComponentPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Allocate values of the missing component
     */
    public abstract void assignValues();

    /**
     * Release values of the missing component
     */
    public abstract void unassignValues();

    /**
     * Checks if all values are in the defined range
     * of this component; returns false, if not
     *
     * @return <b>true</b>, if all given values are in allowed ranges.<br>
     *          <b>false</b>, if not.
     */
    public abstract boolean checkValues();

    /**
     * Returns the missing component
     *
     * @return a {@link org.n52.sos.importer.model.Component} object.
     */
    public abstract Component getMissingComponent();

    /**
     * Initialises the missing component
     *
     * @param c a {@link org.n52.sos.importer.model.Component} object.
     */
    public abstract void setMissingComponent(Component c);
}
