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
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PositionComponent extends Component {

    private static final String DEGREE = "degree";
    private static final String FT = "ft";
    private static final String N_A = "n/a";

    private static final Logger LOG = LoggerFactory.getLogger(PositionComponent.class);

    private TableElement tableElement;

    private String pattern;

    private double value = -1;

    private String unit;

    /**
     * <p>Constructor for PositionComponent.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     * @param pattern a {@link java.lang.String} object.
     */
    public PositionComponent(final TableElement tableElement, final String pattern) {
        this.tableElement = tableElement;
        this.pattern = pattern;
    }

    /**
     * <p>Constructor for PositionComponent.</p>
     *
     * @param value a double.
     * @param unit a {@link java.lang.String} object.
     */
    public PositionComponent(final double value, final String unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a double.
     */
    public void setValue(final double value) {
        LOG.info("Assign Value to " + this.getClass().getName());
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a double.
     */
    public double getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>unit</code>.</p>
     *
     * @param unit a {@link java.lang.String} object.
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

    /**
     * <p>Getter for the field <code>unit</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * <p>Setter for the field <code>tableElement</code>.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void setTableElement(final TableElement tableElement) {
        LOG.info("Assign Column to " + this.getClass().getName());
        this.tableElement = tableElement;
    }

    /**
     * <p>Getter for the field <code>tableElement</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public TableElement getTableElement() {
        return tableElement;
    }

    /**
     * colors this particular component
     */
    public void mark() {
        if (tableElement != null) {
            tableElement.mark();
        }
    }

    /**
     * <p>getParsedUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getParsedUnit() {
        if (null == unit) {
            return N_A;
        } else {
            switch (unit) {
                case "":
                    return N_A;
                case "m":
                case "meters":
                    return "m";
                case FT:
                case "feet":
                    return FT;
                case DEGREE:
                case "°":
                    return DEGREE;
                default:
                    return N_A;
            }
        }
    }

    @Override
    public String toString() {
        if (getTableElement() == null) {
            return " " + getValue() + getUnit();
        } else {
            return " " + getTableElement();
        }
    }

    /**
     * <p>Setter for the field <code>pattern</code>.</p>
     *
     * @param pattern a {@link java.lang.String} object.
     */
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * <p>Getter for the field <code>pattern</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * returns the corresponding position component for a feature of interest cell
     *
     * @param featureOfInterestPosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a {@link org.n52.sos.importer.model.position.PositionComponent} object.
     */
    public abstract PositionComponent forThis(Cell featureOfInterestPosition);
}
