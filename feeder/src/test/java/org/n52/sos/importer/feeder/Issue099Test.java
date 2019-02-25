/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.n52.sos.importer.feeder.model.Position;

public class Issue099Test {

    @Test(expected = OXFException.class)
    public void issue099TestForNoSuchMethodExceptionDuringSosWrapperInit() throws ExceptionReport, OXFException {
        try {
            SosWrapperFactory.newInstance("http://example.com:52/not/available", "2.0.0", Binding.POX);
        } catch (NoSuchMethodError e) {
            Assert.fail("NoSuchMethodError still happening: " + e.getLocalizedMessage());
        }
    }


    @Test
    public void getPositionShouldReturnValidPosition() throws XmlException, IOException, ParseException {
        Configuration configuration = new Configuration("src/test/resources/issue-099/config-2.xml");
        Position position = configuration.getPosition("A",
                new String[] { "4326", "52.0", "42.0", "timestamp", "value"});
        assertThat(position, notNullValue());
        assertThat(position.getEpsgCode(), is(4326));
        assertThat(position.getLatitude(), is(52.0));
        assertThat(position.getLongitude(), is(42.0));
    }

    @Test
    public void getFoiForColumnShouldReturnValidFoiWithPosition() throws XmlException, IOException, ParseException {
        Configuration configuration = new Configuration("src/test/resources/issue-099/config-2.xml");
        DataFile dataFile = new DataFile(configuration, new File("src/test/resources/issue-099/data.csv"));
        Position position = dataFile.getFoiForColumn(3,
                new String[] { "4326", "51.141977", "7.369473", "805", "property", "uom", "1970-01-01T11:00:00", "1"})
                .getPosition();
        assertThat(position, notNullValue());
        assertThat(position.getEpsgCode(), is(4326));
        assertThat(position.getLatitude(), is(51.141977));
        assertThat(position.getLongitude(), is(7.369473));
    }

}
