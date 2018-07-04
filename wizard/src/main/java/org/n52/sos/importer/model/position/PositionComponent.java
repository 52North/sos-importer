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

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingPositionComponentPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionComponent extends Component {

    private static final String DEG = "°";
    private static final String FT = "ft";
    private static final String N_A = "n/a";

    private static final Logger LOG = LoggerFactory.getLogger(PositionComponent.class);

    private TableElement tableElement;

    private String pattern;

    private double value = Double.NaN;

    private String unit;

    private final Id id;

    public PositionComponent(Id id, TableElement tableElement, String pattern) {
        this.tableElement = tableElement;
        this.pattern = pattern;
        this.id = id;
    }

    public PositionComponent(Id id, double value, String unit) {
        this.value = value;
        this.unit = unit;
        this.id = id;
    }

    public void setValue(double value) {
        LOG.info("Assign Value to " + this.getClass().getName());
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setTableElement(TableElement tableElement) {
        LOG.info("Assign Column to " + this.getClass().getName());
        this.tableElement = tableElement;
    }

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

    public String getParsedUnit() {
        if (null == unit) {
            return N_A;
        } else {
            switch (unit) {
                case "m":
                case "meters":
                    return "m";
                case FT:
                case "feet":
                    return FT;
                case "degree":
                case DEG:
                    return DEG;
                case "":
                default:
                    return N_A;
            }
        }
    }

    @Override
    public String toString() {
        if (getTableElement() == null) {
            return " " + getValue();
        } else {
            return " " + getTableElement();
        }
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * returns the corresponding position component for a feature of interest cell
     *
     * @param featureOfInterestPosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a {@link org.n52.sos.importer.model.position.PositionComponent} object.
     */
    public PositionComponent forThis(Id id, Cell featureOfInterestPosition) {
        if (getTableElement() == null) {
            return new PositionComponent(getId(), getValue(), getParsedUnit());
        } else {
            String positionString = getTableElement().getValueFor(featureOfInterestPosition);
            return PositionComponent.parse(id, positionString);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (pattern == null ? 0 : pattern.hashCode());
        result = prime * result + (tableElement == null ? 0 : tableElement.hashCode());
        result = prime * result + (unit == null ? 0 : unit.hashCode());
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    /**
     * Tries to convert a given String into a valid {@link PositionComponent} object
     *
     * @param s a {@link String} object.
     * @return a {@link PositionComponent} object.
     */
    // TODO units and Strings -> Constants
    public static PositionComponent parse(Id id, String s) {
        double value = 0.0;
        String unit = "";

        String number;
        //TODO handle inputs like degrees/minutes/seconds, n.Br.
        if (s.contains(DEG)) {
            unit = DEG;
            String[] part = s.split(DEG);
            number = part[0];
        } else if (s.contains("m")) {
            unit = "m";
            number = s.replace("m", "");
        } else {
            number = s;
        }

        NumericValue nv = new NumericValue();

        value = nv.parse(number);

        if (unit.equals("")) {
            if (value <= 90.0 && value >= -90.0) {
                unit = DEG;
            } else {
                unit = "m";
            }
        }

        return new PositionComponent(id, value, unit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PositionComponent other = (PositionComponent) obj;
        if (pattern == null) {
            if (other.pattern != null) {
                return false;
            }
        } else if (!pattern.equals(other.pattern)) {
            return false;
        }
        if (tableElement == null) {
            if (other.tableElement != null) {
                return false;
            }
        } else if (!tableElement.equals(other.tableElement)) {
            return false;
        }
        if (unit == null) {
            if (other.unit != null) {
                return false;
            }
        } else if (!unit.equals(other.unit)) {
            return false;
        }
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    public Id getId() {
        return id;
    }

    @Override
    public MissingComponentPanel getMissingComponentPanel(Combination c) {
        return new MissingPositionComponentPanel(id, (Position) c);
    }
}
