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
import org.apache.xmlbeans.XmlException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Sensor;

public class TestIssue76ServerGeneratedOfferingIdentifier {

    @Test
    public void procedureAndOfferingShouldDifferWhenGenerated() throws XmlException, IOException {
        // given
        Configuration config = new Configuration("src/test/resources/issue-076/config.xml");
        Sensor sensor = new Sensor("test-sensor-name", "test-sensor-uri");

        // when
        Offering offering = config.getOffering(sensor);

        // then
        Assert.assertThat(sensor.getName(), Matchers.not(offering.getName()));
        Assert.assertThat(sensor.getUri(), Matchers.not(offering.getUri()));
        Assert.assertThat(offering.getName(), Matchers.startsWith(sensor.getName()));
        Assert.assertThat(offering.getUri().toString(), Matchers.startsWith(sensor.getUri().toString()));
        Assert.assertThat(offering.getName(), Matchers.is("test-sensor-name-offering"));
        Assert.assertThat(offering.getUri().toString(), Matchers.is("test-sensor-uri-offering"));
    }

}
