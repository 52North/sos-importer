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

import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;
import org.n52.sos.importer.feeder.model.Position;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class CoordinateHelper extends org.n52.shetland.util.JTSHelper {

    /**
     * WKT format String template for 3D points: <code>Point(%s %s %s)</code>.
     */
    public static final String FORMAT_POINT_3D = org.n52.shetland.util.JTSHelper.WKT_POINT + "(%s %s %s)";

    /**
     * WKT format String template for 2D points: <code>Point(%s %s)</code>.
     */
    public static final String FORMAT_POINT_2D = org.n52.shetland.util.JTSHelper.WKT_POINT + "(%s %s)";

    /**
     * Creates a JTS Point from given {@link Position}.
     *
     * @param position the <code>Position</code> to convert
     * @return a JTS Point with the coordinates in the order extracted from EPSG database.
     * @throws ParseException if the WKT could not be created.
     * @throws FactoryException if the creation of an internal object fails
     * @throws NoSuchAuthorityCodeException if the epsgCode could not be matched to any entry in the EPSG database
     *
     * @see #createGeometryFromWKT(String, int)
     * @see #FORMAT_POINT_3D
     */
    public static Point createPoint(Position position)
            throws ParseException, NoSuchAuthorityCodeException, FactoryException {
        CoordinateSystem cs = CRS.decode("EPSG:" + position.getEpsgCode()).getCoordinateSystem();
        GeometryFactory fac = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), position.getEpsgCode());
        if (cs.getDimension() == 2) {
            return fac.createPoint(new CoordinateArraySequence(new Coordinate[] {
                new Coordinate(position.getValueByAxisAbbreviation(cs.getAxis(0).getAbbreviation()),
                        position.getValueByAxisAbbreviation(cs.getAxis(1).getAbbreviation())), }, 2));
        } else if (cs.getDimension() == 3) {
            return fac.createPoint(new CoordinateArraySequence(new Coordinate[] {
                new Coordinate(position.getValueByAxisAbbreviation(cs.getAxis(0).getAbbreviation()),
                        position.getValueByAxisAbbreviation(cs.getAxis(1).getAbbreviation()),
                        position.getValueByAxisAbbreviation(cs.getAxis(2).getAbbreviation())), }, 3));
        }
        throw new IllegalArgumentException("Only CRS with 2 or 3 dimensions are supported!");
    }

}
