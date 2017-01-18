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
package org.n52.sos.importer.feeder;


import java.io.IOException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;

/**
 * Test for Issue #57: Feeder fails to read FoI position
 *
 * https://github.com/52North/sos-importer/issues/57
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class TestIssue57ReadFoiPosition {


    @Test
    public void testGetFoiForColumn() throws XmlException, IOException, ParseException {
        // given
        final Configuration config = new Configuration("src/test/resources/issue-057/data_config.xml");
        DataFile dataFile = new DataFile(config, config.getDataFile());

        // when
        int mvColumnId = 4;
        final String foiId = "SE10_0AB_1";
        String[] values = { foiId, "Sensor1", "51.48790", "0.00441", "0.71",
                "Rel_Humidity", "Percent", "3/14/2016 1.30 PM", "", "", "", };
        FeatureOfInterest foi = dataFile.getFoiForColumn(mvColumnId, values);

        // then
        final String deg = "deg";
        Assert.assertThat(foi.getPosition(), Is.is(org.hamcrest.core.IsNull.notNullValue()));
        Assert.assertThat(foi.getUri(), Is.is(foiId));
        Assert.assertThat(foi.getPosition().getAltitude(), Is.is(0.0));
        Assert.assertThat(foi.getPosition().getAltitudeUnit(), Is.is("m"));
        Assert.assertThat(foi.getPosition().getLatitude(), Is.is(51.48790));
        Assert.assertThat(foi.getPosition().getLatitudeUnit(), Is.is(deg));
        Assert.assertThat(foi.getPosition().getLongitude(), Is.is(0.00441));
        Assert.assertThat(foi.getPosition().getLongitudeUnit(), Is.is(deg));
    }
}
