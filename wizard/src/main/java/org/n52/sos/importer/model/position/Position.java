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
package org.n52.sos.importer.model.position;

import java.text.MessageFormat;
import java.text.ParseException;

import org.n52.sos.importer.model.Combination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Position extends Combination {

    /**
     * 
     */
    private static final String EPSG = "EPSG";

    /**
     * 
     */
    private static final String ALT = "ALT";

    /**
     * 
     */
    private static final String LON = "LON";

    /**
     * 
     */
    private static final String LAT = "LAT";

    /**
     * 
     */
    private static final String FROM = " from ";

    /**
     * 
     */
    private static final String REMOVE = "Remove ";

    /**
     * 
     */
    private static final String TO = " to ";

    /**
     * 
     */
    private static final String ADD = "Add ";

    private static final Logger logger = LoggerFactory.getLogger(Position.class);

    private Latitude latitude;

    private Longitude longitude;

    private Height height;

    private EPSGCode epsgCode;

    private String group;

    /**
     * <p>Constructor for Position.</p>
     */
    public Position() {
        super();
    }

    /**
     * <p>Constructor for Position.</p>
     *
     * @param latitude a {@link org.n52.sos.importer.model.position.Latitude} object.
     * @param longitude a {@link org.n52.sos.importer.model.position.Longitude} object.
     * @param height a {@link org.n52.sos.importer.model.position.Height} object.
     * @param epsgCode a {@link org.n52.sos.importer.model.position.EPSGCode} object.
     */
    public Position(final Latitude latitude, final Longitude longitude, final Height height,
            final EPSGCode epsgCode) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.epsgCode = epsgCode;
    }

    /**
     * <p>Getter for the field <code>height</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Height} object.
     */
    public Height getHeight() {
        return height;
    }

    /**
     * <p>Setter for the field <code>height</code>.</p>
     *
     * @param height a {@link org.n52.sos.importer.model.position.Height} object.
     */
    public void setHeight(final Height height) {
        if (getGroup() != null) {
            if (height != null) {
                logger.info(ADD + height + TO + this);
            } else {
                logger.info(REMOVE + this.height + FROM + this);
            }
        }
        this.height = height;
    }

    /**
     * <p>getEPSGCode.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.EPSGCode} object.
     */
    public EPSGCode getEPSGCode() {
        return epsgCode;
    }

    /**
     * <p>setEPSGCode.</p>
     *
     * @param newEpsgCode a {@link org.n52.sos.importer.model.position.EPSGCode} object.
     */
    public void setEPSGCode(final EPSGCode newEpsgCode) {
        if (getGroup() != null) {
            if (newEpsgCode != null) {
                logger.info(ADD + newEpsgCode + TO + this);
            } else {
                logger.info(REMOVE + this.epsgCode + FROM + this);
            }
        }
        this.epsgCode = newEpsgCode;
    }

    /**
     * <p>Setter for the field <code>longitude</code>.</p>
     *
     * @param longitude a {@link org.n52.sos.importer.model.position.Longitude} object.
     */
    public void setLongitude(final Longitude longitude) {
        if (getGroup() != null) {
            if (longitude != null) {
                logger.info(ADD + longitude + TO + this);
            } else {
                logger.info(REMOVE + this.longitude + FROM + this);
            }
        }
        this.longitude = longitude;
    }

    /**
     * <p>Getter for the field <code>longitude</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Longitude} object.
     */
    public Longitude getLongitude() {
        return longitude;
    }

    /**
     * <p>Setter for the field <code>latitude</code>.</p>
     *
     * @param latitude a {@link org.n52.sos.importer.model.position.Latitude} object.
     */
    public void setLatitude(final Latitude latitude) {
        if (getGroup() != null) {
            if (latitude != null) {
                logger.info(ADD + latitude + TO + this);
            } else {
                logger.info(REMOVE + this.latitude + FROM + this);
            }
        }
        this.latitude = latitude;
    }

    /**
     * <p>Getter for the field <code>latitude</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Latitude} object.
     */
    public Latitude getLatitude() {
        return latitude;
    }

    /** {@inheritDoc} */
    @Override
    public void setGroup(final String group) {
        this.group = group;
    }

    /** {@inheritDoc} */
    @Override
    public String getGroup() {
        return group;
    }

    /** {@inheritDoc} */
    @Override
    public String format(final Object o) {
        final Position p = (Position) o;
        String positionString = getPattern();
        // TODO remove explicit string from here
        positionString = positionString.replaceAll(LAT, p.getLatitude().getValue() + p.getLatitude().getUnit());
        positionString = positionString.replaceAll(LON, p.getLongitude().getValue() + p.getLongitude().getUnit());
        positionString = positionString.replaceAll(ALT, p.getHeight().getValue() + p.getHeight().getUnit());
        positionString = positionString.replaceAll(EPSG, p.getEPSGCode().getValue() + "");
        return positionString;
    }

    /** {@inheritDoc} */
    @Override
    public Position parse(final String s) {
        String pattern = getPattern();

        pattern = pattern.replaceAll(LAT, "{0}");
        pattern = pattern.replaceAll(LON, "{1}");
        pattern = pattern.replaceAll(ALT, "{2}");
        pattern = pattern.replaceAll(EPSG, "{3}");

        final MessageFormat mf = new MessageFormat(pattern);
        Object[] o = null;
        try {
            o = mf.parse(s);
        } catch (final ParseException e) {
            throw new NumberFormatException();
        }

        if (o == null) {
            throw new NumberFormatException();
        }

        Latitude newLatitude = null;
        Longitude newLongitude = null;
        Height newHeight = null;
        EPSGCode newEpsgCode = null;

        if (o.length > 0 && o[0] != null) {
            newLatitude = Latitude.parse((String) o[0]);
        }
        if (o.length > 1 && o[1] != null) {
            newLongitude = Longitude.parse((String) o[1]);
        }
        if (o.length > 2 && o[2] != null) {
            newHeight = Height.parse((String) o[2]);
        }
        if (o.length > 3 && o[3] != null) {
            newEpsgCode = EPSGCode.parse((String) o[3]);
        }
        return new Position(newLatitude, newLongitude, newHeight, newEpsgCode);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (getGroup() == null) {
            return "Position (" + latitude + ", " + longitude + ", "
                    + height + ", " + epsgCode + ")";
        } else {
            return "Position group " + getGroup();
        }
    }
}
