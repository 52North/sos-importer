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
package org.n52.sos.importer.feeder.model;

import java.util.Arrays;

/**
 * <p>Position class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public final class Position {

    /** Constant <code>LONG=0</code> */
    public static final int LONG = 0;

    /** Constant <code>LAT=1</code> */
    public static final int LAT = 1;

    /** Constant <code>ALT=2</code> */
    public static final int ALT = 2;

    /** Constant <code>DEFAULT_UNIT_LAT="null"</code> */
    public static final String DEFAULT_UNIT_LAT = null;

    /** Constant <code>UNIT_NOT_SET="UNIT_NOT_SET"</code> */
    public static final String UNIT_NOT_SET = "UNIT_NOT_SET";

    /** Constant <code>VALUE_NOT_SET=Double.NEGATIVE_INFINITY</code> */
    public static final double VALUE_NOT_SET = Double.NEGATIVE_INFINITY;

    private final double[] values;

    private final String[] units;

    private final int epsgCode;

    /**
     * <p>Constructor for Position.</p>
     *
     * @param values order: long, lat, alt
     * @param units order: long, lat, alt
     * @param epsgCode a int.
     */
    public Position(final double[] values, final String[] units, final int epsgCode) {
        if (values == null || units == null) {
            throw new IllegalArgumentException("values/units must not be null");
        }
        this.values = values.clone();
        this.units = units.clone();
        this.epsgCode = epsgCode;
    }

    /**
     * <p>Getter for the field <code>epsgCode</code>.</p>
     *
     * @return a int.
     */
    public int getEpsgCode() {
        return epsgCode;
    }

    /**
     * <p>getAltitude.</p>
     *
     * @return a double.
     */
    public double getAltitude() {
        return values[Position.ALT];
    }

    /**
     * <p>getAltitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAltitudeUnit() {
        return units[Position.ALT];
    }

    /**
     * <p>getLongitude.</p>
     *
     * @return a double.
     */
    public double getLongitude() {
        return values[Position.LONG];
    }

    /**
     * <p>getLongitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLongitudeUnit() {
        return units[Position.LONG];
    }

    /**
     * <p>getLatitude.</p>
     *
     * @return a double.
     */
    public double getLatitude() {
        return values[Position.LAT];
    }

    /**
     * <p>getLatitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLatitudeUnit() {
        return units[Position.LAT];
    }

    @Override
    public String toString() {
        return String.format("Position [values=%s, units=%s, epsgCode=%s]",
                Arrays.toString(values), Arrays.toString(units), epsgCode);
    }


}
