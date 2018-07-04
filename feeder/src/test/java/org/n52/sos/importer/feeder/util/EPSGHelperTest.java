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
package org.n52.sos.importer.feeder.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.n52.sos.importer.feeder.util.EPSGHelper.areValid;
import static org.n52.sos.importer.feeder.util.EPSGHelper.areValidXBCoordinates;
import static org.n52.sos.importer.feeder.util.EPSGHelper.isValidEPSGCode;

import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.x52North.sensorweb.sos.importer.x05.CoordinateDocument.Coordinate;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class EPSGHelperTest {

    private static final String deg = "°";
    ErrorCollector errors = new ErrorCollector();

    @Test
    public void isValidEPSGCodeShouldReturnFalseForWrongValues() {
        assertThat(isValidEPSGCode(-1), is(false));
        assertThat(isValidEPSGCode(0), is(false));
        assertThat(isValidEPSGCode(Integer.MIN_VALUE), is(false));
        assertThat(isValidEPSGCode(Integer.MAX_VALUE), is(false));
    }

    @Test
    public void isValidEPSGCodeShouldReturnTrueForGivenValue4326() {
        assertThat(isValidEPSGCode(4326), is(true));
    }

    @Test
    public void isValidEPSGCodeShouldReturnTrueForGivenValue4979() {
        assertThat(isValidEPSGCode(4979), is(true));
    }

    @Test
    public void isValidEPSGCodeShouldReturnTrueForGivenValue31258() {
        assertThat(isValidEPSGCode(31258), is(true));
    }

    @Test
    public void areValidXBCoordinatesShouldReturnFalseOnInvalidEPSGCode() {
        Coordinate[] coordinates = null;
        assertThat(areValidXBCoordinates(-1, coordinates), is(false));
    }

    @Test
    public void areValidXBCoordinatesShouldReturnFalseOnEmptyCoordinates() {
        Coordinate[] coordinates = null;
        assertThat(areValidXBCoordinates(4326, coordinates), is(false));
    }

    @Test
    public void areValidXBCoordinatesShouldReturnFalseOnWrongCoordinatesDimension() {
        Coordinate[] coordinates = new Coordinate[3];
        assertThat(areValidXBCoordinates(4326, coordinates), is(false));
    }

    @Test
    public void areValidXBCoordinatesShouldReturnFalseForCoordinatesWithNullValues() {
        Coordinate[] coordinates = new Coordinate[2];
        assertThat(areValidXBCoordinates(4326, coordinates), is(false));
    }

    @Test
    public void areValidXBCoordinatesShouldReturnTrueForCorrectValues() {
        Coordinate[] coordinates = new Coordinate[2];
        coordinates[0] = Coordinate.Factory.newInstance();
        coordinates[0].setAxisAbbreviation("Long");
        coordinates[0].setUnit(deg);
        coordinates[0].setDoubleValue(52.0);
        coordinates[1] = Coordinate.Factory.newInstance();
        coordinates[1].setAxisAbbreviation("Lat");
        coordinates[1].setUnit(deg);
        coordinates[1].setDoubleValue(42.0);

        assertThat(areValidXBCoordinates(4326, coordinates), is(true));
    }

    @Test
    public void areValidShouldReturnFalseOnInvalidEPSGCode() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates = null;
        assertThat(areValid(-1, coordinates), is(false));
    }

    @Test
    public void areValidShouldReturnFalseOnEmptyCoordinates() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates = null;
        assertThat(areValid(4326, coordinates), is(false));
    }

    @Test
    public void areValidShouldReturnFalseOnWrongCoordinatesDimension() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates =
                new org.n52.sos.importer.feeder.model.Coordinate[3];
        assertThat(areValid(4326, coordinates), is(false));
    }

    @Test
    public void areValidShouldReturnFalseForCoordinatesWithNullValues() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates =
                new org.n52.sos.importer.feeder.model.Coordinate[2];
        assertThat(areValid(4326, coordinates), is(false));
    }

    @Test
    public void areValidShouldReturnTrueForCorrectValues() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates =
                new org.n52.sos.importer.feeder.model.Coordinate[2];
        coordinates[0] = new org.n52.sos.importer.feeder.model.Coordinate("Long", deg, 52.0);
        coordinates[1] = new org.n52.sos.importer.feeder.model.Coordinate("Lat", deg, 42.0);

        assertThat(areValid(4326, coordinates), is(true));
    }

    @Test
    public void areValidShouldReturnFalseForWrongAxisAbbreviation() {
        org.n52.sos.importer.feeder.model.Coordinate[] coordinates =
                new org.n52.sos.importer.feeder.model.Coordinate[2];
        coordinates[0] = new org.n52.sos.importer.feeder.model.Coordinate("Longitude", deg, 52.0);
        coordinates[1] = new org.n52.sos.importer.feeder.model.Coordinate("Lat", deg, 42.0);

        assertThat(areValid(4326, coordinates), is(false));
    }

    @Test
    public void isAxisAbbreviationValidShouldReturnFalseForEPSGCode4326AndAxisAbbrevationX() {
        assertThat(EPSGHelper.isAxisAbbreviationValid(4326, "X"), is(false));
    }

    @Test
    public void isAxisAbbreviationValidShouldReturnTrueForEPSGCode4326AndAxisAbbrevationLatAndLong() {
        assertThat(EPSGHelper.isAxisAbbreviationValid(4326, "Lat"), is(true));
        assertThat(EPSGHelper.isAxisAbbreviationValid(4326, "Long"), is(true));
    }

}
