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
package org.n52.sos.importer.wizard.utils;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.CoordinateSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EPSGHelper {

    private static final String EXCEPTION_THROWN = "Exception thrown:";
    private static final String EPSG = "EPSG:";
    private static final Logger LOG = LoggerFactory.getLogger(EPSGHelper.class);

    public static CoordinateSystem getCoordinateSystem(int epsgCode) {
        CoordinateSystem cs = null;
        try {
            cs = CRS.decode(EPSG + epsgCode).getCoordinateSystem();
        } catch (FactoryException e) {
            LOG.trace("This should never happen because of isValidEPSGCode(epsgCode) call");
        }
        return cs;
    }

    /**
     * Check, if the EPSG is contained in the gt EPSG database
     * @param epsgCode the epsg code to check
     * @return true, if a CRS could be decoded from <code>EPSG:epsgCode</code>, else false.
     */
    public static boolean isValidEPSGCode(int epsgCode) {
        try {
            CRS.decode(EPSG + epsgCode);
            return true;
        } catch (NoSuchAuthorityCodeException e) {
            LOG.error("Given EPSG code '{}' not supported!", epsgCode);
        } catch (FactoryException e) {
            LOG.error("Error in EPSG code library: '{}'!", e.getMessage());
            LOG.debug(EXCEPTION_THROWN, e);
        } catch (Throwable e) {
            LOG.error("Any other Error in EPSG code library: '{}'!", e.getMessage());
            LOG.debug(EXCEPTION_THROWN, e);
        }
        return false;
    }
}
