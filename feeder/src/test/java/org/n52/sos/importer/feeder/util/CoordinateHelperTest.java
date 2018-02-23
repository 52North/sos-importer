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

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class CoordinateHelperTest {

    private double longitude = 7.5;
    private double latitude = 52.502;
    private double altitude = 60;

    // JTS: x,y,z
    // WGS84: lat (y), long(x), alt(z)

    @Test
    public void testCreatePoint3DWGS84() throws ParseException, NoSuchAuthorityCodeException, FactoryException {
        int epsgCode = 4979;
        Point jtsPoint = CoordinateHelper.createPoint(longitude, latitude, altitude, epsgCode);

        assertThat(jtsPoint, notNullValue());
        assertThat(jtsPoint.getX(), is(latitude));
        assertThat(jtsPoint.getY(), is(longitude));
        assertThat(jtsPoint.getCoordinates()[0].z, is(altitude));
        assertThat(jtsPoint.getSRID(), is(epsgCode));
    }

    @Test
    public void testCreatePoint2DWGS84() throws ParseException, NoSuchAuthorityCodeException, FactoryException {
        int epsgCode = 4326;
        Point jtsPoint = CoordinateHelper.createPoint(longitude, latitude, epsgCode);

        assertThat(jtsPoint, notNullValue());
        assertThat(jtsPoint.getX(), is(latitude));
        assertThat(jtsPoint.getY(), is(longitude));
        assertThat(jtsPoint.getCoordinates()[0].z, is(Double.NaN));
        assertThat(jtsPoint.getSRID(), is(epsgCode));
    }

}
