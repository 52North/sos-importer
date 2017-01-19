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
package org.n52.sos.importer.feeder.model.requests;

import org.junit.Test;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.Position;

public class InsertObservationTest {

    private static final String DEG = "deg";

    @Test public void
    shouldReturnTrueIfAltitudeIsAvailable() {
        final double alt = 2.0;
        final FeatureOfInterest foi =
                new FeatureOfInterest(null, null,
                        new Position(new double[] {0.0, 1.0, alt}, new String[] {DEG, DEG, "m"}, 4326));
        final InsertObservation insertObservation =
                new InsertObservation(null, foi, null, null, null, null, null, null);
        org.junit.Assert.assertThat(insertObservation.isSetAltitudeValue(), org.hamcrest.CoreMatchers.is(true));
        org.junit.Assert.assertThat(insertObservation.getAltitudeValue(), org.hamcrest.CoreMatchers.is(alt));
    }

    @Test public void
    shouldReturnFalseIfAltitudeIsNotAvailable() {
        final FeatureOfInterest foi =
                new FeatureOfInterest(null, null,
                        new Position(
                                new double[] {0.0, 1.0, Double.NEGATIVE_INFINITY},
                                new String[] {DEG, DEG, null},
                                4326));
        InsertObservation insertObservation = new InsertObservation(null, foi, null, null, null, null, null, null);
        org.junit.Assert.assertThat(insertObservation.isSetAltitudeValue(), org.hamcrest.CoreMatchers.is(false));

        insertObservation = new InsertObservation(null, null, null, null, null, null, null, null);
        org.junit.Assert.assertThat(insertObservation.isSetAltitudeValue(), org.hamcrest.CoreMatchers.is(false));
    }

}
