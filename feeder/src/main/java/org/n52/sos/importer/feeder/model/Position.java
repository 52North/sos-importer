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
package org.n52.sos.importer.feeder.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.n52.sos.importer.feeder.util.EPSGHelper;

/**
 * <p>Position class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public final class Position {

    /** Constant <code>UNIT_NOT_SET="UNIT_NOT_SET"</code> */
    public static final String UNIT_NOT_SET = "UNIT_NOT_SET";

    /** Constant <code>VALUE_NOT_SET=Double.NEGATIVE_INFINITY</code> */
    public static final double VALUE_NOT_SET = Double.NEGATIVE_INFINITY;

    private final Coordinate[] coordinates;

    private final int epsgCode;

    public Position(int epsgCode, Coordinate[] coordinates) {
        if (!EPSGHelper.isValidEPSGCode(epsgCode)) {
            throw new IllegalArgumentException(String.format("EPSG code '%s' is invalid!", epsgCode));
        }
        if (!EPSGHelper.areValid(epsgCode, coordinates)) {
            throw new IllegalArgumentException(String.format("Coordinates '%s' for given EPSG code '%s' are invalid.",
                    Arrays.toString(coordinates),
                    epsgCode));
        }
        this.epsgCode = epsgCode;
        this.coordinates = coordinates.clone();
    }

    public int getEpsgCode() {
        return epsgCode;
    }

    public double getValueByAxisAbbreviation(String axisAbbreviation) {
        Optional<Coordinate> findFirst = getCoordinateByAxisAbbreviation(axisAbbreviation);
        return findFirst.get().getValue();
    }

    public String getUnitByAxisAbbreviation(String axisAbbreviation) {
        Optional<Coordinate> findFirst = getCoordinateByAxisAbbreviation(axisAbbreviation);
        return findFirst.get().getUnit();
    }

    private Optional<Coordinate> getCoordinateByAxisAbbreviation(String axisAbbreviation)
            throws IllegalArgumentException {
        if (!EPSGHelper.isAxisAbbreviationValid(epsgCode, axisAbbreviation)) {
            throw new IllegalArgumentException(
                    String.format("Axis abbreviation '%s' is invalid in combination with EPSG code '%s'!",
                            axisAbbreviation, epsgCode));
        }
        Optional<Coordinate> firstMatchingCoordinate = Stream.of(coordinates)
                .filter(c -> c.getAxisAbbrevation().equals(axisAbbreviation))
                .findFirst();
        if (!firstMatchingCoordinate.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Coordinate with axis abbreviation '%s' not found in '%s'!",
                    axisAbbreviation, Arrays.toString(coordinates)));
        }
        return firstMatchingCoordinate;
    }

    public Coordinate[] getCoordinates() {
        return coordinates.clone();
    }

    @Override
    public String toString() {
        return String.format("Position [epsgCode=%s, coordinates=%s]", epsgCode, Arrays.toString(coordinates));
    }

    public boolean isValid() {
        return EPSGHelper.isValidEPSGCode(epsgCode) &&
                EPSGHelper.areValid(epsgCode, coordinates);
    }


}
