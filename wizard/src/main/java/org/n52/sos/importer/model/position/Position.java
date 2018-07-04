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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.n52.sos.importer.model.Combination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Position extends Combination {

    public static final String EPSG = "EPSG";

    public enum Id {
        COORD_0,
        COORD_1,
        COORD_2
    }

    private static final String FROM = " from ";
    private static final String REMOVE = "Remove ";
    private static final String TO = " to ";
    private static final String ADD = "Add ";

    private static final Logger LOG = LoggerFactory.getLogger(Position.class);

    private Map<Id, PositionComponent> coordinates = new HashMap<>(4);

    private EPSGCode epsgCode;

    private String group;

    public Position() {
        super();
    }

    public Position(EPSGCode epsgCode, PositionComponent... coordinates) {
        super();
        this.epsgCode = epsgCode;
        for (PositionComponent coordinate : coordinates) {
            if (coordinate != null) {
                this.coordinates.put(coordinate.getId(), coordinate);
            }
        }
    }

    public void addCoordinate(PositionComponent coordinate) {
        if (getGroup() != null) {
            if (coordinate != null) {
                if (coordinates.containsKey(coordinate.getId())) {
                    coordinates.remove(coordinate.getId());
                }
                coordinates.put(coordinate.getId(), coordinate);
                LOG.info(ADD + coordinate + TO + this);
            }
        }
    }

    public PositionComponent getCoordinate(Id id) {
        return coordinates.get(id);
    }

    public EPSGCode getEPSGCode() {
        return epsgCode;
    }

    public void setEPSGCode(final EPSGCode newEpsgCode) {
        if (getGroup() != null) {
            if (newEpsgCode != null) {
                LOG.info(ADD + newEpsgCode + TO + this);
            } else {
                LOG.info(REMOVE + epsgCode + FROM + this);
            }
        }
        epsgCode = newEpsgCode;
    }

    @Override
    public void setGroup(final String group) {
        this.group = group;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String format(final Object o) {
        final Position p = (Position) o;
        String positionString = getPattern();
        // TODO remove explicit string from here
        positionString = positionString.replaceAll(Id.COORD_0.name(),
                p.coordinates.get(Id.COORD_0).getValue() + p.coordinates.get(Id.COORD_0).getUnit());
        positionString = positionString.replaceAll(Id.COORD_1.name(),
                p.coordinates.get(Id.COORD_1).getValue() + p.coordinates.get(Id.COORD_1).getUnit());
        if (p.coordinates.containsKey(Id.COORD_2)) {
            positionString = positionString.replaceAll(Id.COORD_2.name(),
                    p.coordinates.get(Id.COORD_2).getValue() + p.coordinates.get(Id.COORD_2).getUnit());
        }
        positionString = positionString.replaceAll(EPSG, Integer.toString(p.getEPSGCode().getValue()));
        return positionString;
    }

    @Override
    public Position parse(String cellValueToParse) {
        LOG.trace("parse('{}')", cellValueToParse);
        String pattern = getPattern();

        pattern = pattern.replaceAll(Id.COORD_0.name(), "{0}");
        pattern = pattern.replaceAll(Id.COORD_1.name(), "{1}");
        pattern = pattern.replaceAll(Id.COORD_2.name(), "{2}");
        pattern = pattern.replaceAll(EPSG, "{3}");

        final MessageFormat mf = new MessageFormat(pattern);
        Object[] tokens = null;
        try {
            tokens = mf.parse(cellValueToParse);
        } catch (final ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }

        if (tokens == null) {
            throw new NumberFormatException(String.format(
                    "could not parse position values from string '%s'.",
                    cellValueToParse));
        }

        PositionComponent newCoord0 = null;
        PositionComponent newCoord1 = null;
        PositionComponent newCoord2 = null;
        EPSGCode newEpsgCode = null;

        if (tokens.length > 0 && tokens[0] != null) {
            newCoord0 = PositionComponent.parse(Id.COORD_0, (String) tokens[0]);
        }
        if (tokens.length > 1 && tokens[1] != null) {
            newCoord1 = PositionComponent.parse(Id.COORD_1, (String) tokens[1]);
        }
        if (tokens.length > 2 && tokens[2] != null) {
            newCoord2 = PositionComponent.parse(Id.COORD_2, (String) tokens[2]);
        }
        if (tokens.length > 3 && tokens[3] != null) {
            newEpsgCode = EPSGCode.parse((String) tokens[3]);
        }

        return new Position(newEpsgCode, newCoord0, newCoord1, newCoord2);
    }

    @Override
    public String toString() {
        if (getGroup() == null) {
            return "Position (" + coordinates.get(Id.COORD_0) + ", " + coordinates.get(Id.COORD_1) + ", "
                    + coordinates.get(Id.COORD_2) + ", " + epsgCode + ")";
        } else {
            return "Position group " + getGroup();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (coordinates == null ? 0 : coordinates.hashCode());
        result = prime * result + (epsgCode == null ? 0 : epsgCode.hashCode());
        result = prime * result + (group == null ? 0 : group.hashCode());
        return result;
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
        Position other = (Position) obj;
        if (coordinates == null) {
            if (other.coordinates != null) {
                return false;
            }
        } else if (!coordinates.equals(other.coordinates)) {
            return false;
        }
        if (epsgCode == null) {
            if (other.epsgCode != null) {
                return false;
            }
        } else if (!epsgCode.equals(other.epsgCode)) {
            return false;
        }
        if (group == null) {
            if (other.group != null) {
                return false;
            }
        } else if (!group.equals(other.group)) {
            return false;
        }
        return true;
    }

    public PositionComponent removeCoordinate(Id id) {
        if (coordinates.containsKey(id)) {
            return coordinates.remove(id);
        }
        return null;
    }

}
