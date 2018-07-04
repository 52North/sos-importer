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

import static java.util.Optional.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;

public class InsertObservationTest {


    @Test
    public void shouldReturnTrueIfParentFeatureIdentifierIsSet() {
        FeatureOfInterest foi = new FeatureOfInterest(null, "uri", null);
        String parentFeatureId = "parentFeatureId";
        foi.setParentFeature(parentFeatureId);

        InsertObservation io = new InsertObservation(null, foi, null, null, null, null, null, empty(), null);

        assertThat(io.hasFeatureParentFeature(), is(true));
        assertThat(io.getParentFeatureIdentifier(), is(parentFeatureId));
    }


    @Test
    public void shouldReturnFalseIfParentFeatureIdentifierIsNullOrEmpty() {
        FeatureOfInterest foi = new FeatureOfInterest(null, "uri", null);
        foi.setParentFeature(null);

        InsertObservation io = new InsertObservation(null, foi, null, null, null, null, null, empty(), null);

        assertThat(io.hasFeatureParentFeature(), is(false));

        foi.setParentFeature("");
        assertThat(io.hasFeatureParentFeature(), is(false));
    }

}
