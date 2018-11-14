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
package org.n52.sos.importer.feeder;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.n52.sos.importer.feeder.Configuration.COORDINATE_UNIT;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
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
    public void testGetFoiForColumn() throws XmlException, IOException, ParseException, URISyntaxException {
        // given
        final Configuration config = new Configuration("src/test/resources/issue-057/data_config.xml");
        DataFile dataFile = new DataFile(config, config.getDataFile());

        // when
        int mvColumnId = 4;
        final String foiId = "SE10_0AB_1";
        double longValue = 0.00441;
        double latValue = 51.48790;
        String[] values = { foiId, "Sensor1", Double.toString(latValue), Double.toString(longValue), "0.71",
                "Rel_Humidity", "Percent", "3/14/2016 1.30 PM", "", "", "", };
        FeatureOfInterest foi = dataFile.getFoiForColumn(mvColumnId, values);

        // then
        assertThat(foi.getPosition(), is(notNullValue()));
        assertThat(foi.getUri().toString(), is(foiId));
        assertThat(foi.getPosition().getValueByAxisAbbreviation("h"), is(0.0));
        assertThat(foi.getPosition().getUnitByAxisAbbreviation("h"), is("m"));
        assertThat(foi.getPosition().getValueByAxisAbbreviation("Lat"), is(latValue));
        assertThat(foi.getPosition().getUnitByAxisAbbreviation("Lat"), is(COORDINATE_UNIT));
        assertThat(foi.getPosition().getValueByAxisAbbreviation("Long"), is(longValue));
        assertThat(foi.getPosition().getUnitByAxisAbbreviation("Long"), is(COORDINATE_UNIT));
    }
}
