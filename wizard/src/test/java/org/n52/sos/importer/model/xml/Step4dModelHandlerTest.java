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
package org.n52.sos.importer.model.xml;

import java.math.BigInteger;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.sos.importer.model.Step4dModel;
import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.table.Column;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

public class Step4dModelHandlerTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void shouldThrowExceptionOnWrongInput1() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(Matchers.containsString("Received null values: stepModel: "));
        new Step4dModelHandler().handleModel(null, null);
    }

    @Test
    public void shouldThrowExceptionOnWrongInput2() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(Matchers.containsString("Received null values: stepModel: "));
        new Step4dModelHandler().handleModel(null, SosImportConfiguration.Factory.newInstance());
    }

    @Test
    public void shouldThrowExceptionOnWrongInput3() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(Matchers.containsString("Received null values: stepModel: "));
        new Step4dModelHandler().handleModel(new Step4dModel(null), null);
    }

    @Test
    public void shouldStoreRelatedOmParameterInMeasureValueColumn() {
        OmParameter parameter = new OmParameter("Measured Value");
        parameter.setTableElement(new Column(42, 52));
        Step4dModel model = new Step4dModel(parameter);
        model.setOrientation("column");
        model.setSelectedColumns(new int[] {23} );

        SosImportConfiguration conf = SosImportConfiguration.Factory.newInstance();

        new Step4dModelHandler().handleModel(model, conf);

        BigInteger[] relatedOmParameters = conf
                .getCsvMetadata()
                .getColumnAssignments()
                .getColumnArray(0)
                .getRelatedOmParameterArray();

        Assert.assertThat(relatedOmParameters, CoreMatchers.notNullValue());
        Assert.assertThat(relatedOmParameters.length, Is.is(1));
        Assert.assertThat(relatedOmParameters[0].intValue(), Is.is(42));
    }

}
