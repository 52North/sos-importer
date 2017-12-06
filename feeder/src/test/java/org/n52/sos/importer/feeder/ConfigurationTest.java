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
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument.Type;

public class ConfigurationTest {

    @Test
    public void isSetOmParameterShouldReturnFalseIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");
        Assert.assertThat(configuration.isOmParameterAvailableFor(4), CoreMatchers.is(false));
    }

    @Test
    public void isSetOmParameterShoulodReturnTrueIfRelatedOmParameterIsSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");
        Assert.assertThat(configuration.isOmParameterAvailableFor(4), CoreMatchers.is(true));
    }

    @Test
    public void isSetOmParameterShouldReturnTrueIfAtLeastOneOmParameterColumnIsDefined()
            throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");
        Assert.assertThat(configuration.isOmParameterAvailableFor(4), CoreMatchers.is(true));
    }

    @Test
    public void getColumnsForOmParametersShouldReturnEmptyListIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");
        Assert.assertThat(configuration.getColumnsForOmParameter(4), CoreMatchers.notNullValue());
        Assert.assertThat(configuration.getColumnsForOmParameter(4).isEmpty(), CoreMatchers.is(true));
    }

    @Test
    public void getColumnsForOmParameterShouldReturnCorrectValues() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");

        List<Column> omParameterColumns = configuration.getColumnsForOmParameter(4);
        Assert.assertThat(omParameterColumns.isEmpty(), CoreMatchers.is(false));
        Assert.assertThat(omParameterColumns.size(), CoreMatchers.is(1));
        Column column = omParameterColumns.get(0);
        Assert.assertThat(column.getType(), CoreMatchers.is(Type.OM_PARAMETER));
        Assert.assertThat(column.getMetadataArray().length, CoreMatchers.is(1));
        Assert.assertThat(column.getMetadataArray(0).getKey(), CoreMatchers.is(Key.TYPE));
        Assert.assertThat(column.getMetadataArray(0).getValue(), CoreMatchers.is("COUNT"));

        Configuration configuration2 = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");

        List<Column> omParameterColumns2 = configuration2.getColumnsForOmParameter(4);
        Assert.assertThat(omParameterColumns2.isEmpty(), CoreMatchers.is(false));
        Assert.assertThat(omParameterColumns2.size(), CoreMatchers.is(2));
        Column column2 = omParameterColumns2.get(0);
        Assert.assertThat(column2.getType(), CoreMatchers.is(Type.OM_PARAMETER));
        Assert.assertThat(column2.getMetadataArray().length, CoreMatchers.is(2));
        Assert.assertThat(column2.getMetadataArray(0).getKey(), CoreMatchers.is(Key.TYPE));
        Assert.assertThat(column2.getMetadataArray(0).getValue(), CoreMatchers.is("BOOLEAN"));

        Column column3 = omParameterColumns2.get(1);
        Assert.assertThat(column3.getMetadataArray().length, CoreMatchers.is(2));
        Assert.assertThat(column3.getMetadataArray(0).getKey(), CoreMatchers.is(Key.TYPE));
        Assert.assertThat(column3.getMetadataArray(0).getValue(), CoreMatchers.is("BOOLEAN"));
    }

    @Test
    public void isNoDataValueDefinedAndMatchingShouldReturnFalseIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");
        Column column = configuration.getColumnById(4);
        Assert.assertThat(configuration.isNoDataValueDefinedAndMatching(column, "14.8"), Is.is(false));
    }

    @Test
    public void isNoDataValueDefinedAndMatchingShouldReturnTrueIfSetAndMatching() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/features/no_data_value_set.xml");
        Column column = configuration.getColumnById(4);
        Assert.assertThat(configuration.isNoDataValueDefinedAndMatching(column, "---"), Is.is(true));
    }

    @Test
    public void shouldReturnURLFromRemoteFileURL() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/features/configWithRemoteFile.xml");
        // URL taken from config file
        Assert.assertThat(configuration.getRemoteFileURL(), Is.is("http://www.example.com/my/fancy/directoryTree/data.csv"));

    }

}
