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
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.Position;

public class DataFileTest {

    private static final String[] EXAMPLE_DATA_ALL_PARAMETER_TYPES =
            new String[]{"0", "52", "42.0", "true", "test-text", "test-category"};

    @Test
    public void shouldReturnEmptyListIfNothingIsAvailable() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");

        Assert.assertThat(
                new DataFile(configuration, null).getOmParameters(4, null),
                CoreMatchers.is(Optional.empty()));
        Assert.assertThat(
                new DataFile(configuration, null).getOmParameters(4, new String[0]),
                CoreMatchers.is(Optional.empty()));
        Assert.assertThat(
                new DataFile(configuration, null).getOmParameters(4,
                        new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9"}),
                CoreMatchers.is(Optional.empty()));
    }

    @Test
    public void shouldReturnBooleanParameter() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(4,
                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "true", "false", "10"});

        Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        Assert.assertThat(omParameters.get(0).getName().getTitleOrFromHref(), CoreMatchers.is("number-of-reviews"));
        Assert.assertThat(omParameters.get(0), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(0).getValue(), CoreMatchers.instanceOf(BooleanValue.class));
        Assert.assertThat(((BooleanValue)omParameters.get(0).getValue()).getValue().booleanValue(),
                CoreMatchers.is(true));
        Assert.assertThat(omParameters.get(1).getName().getTitleOrFromHref(),
                CoreMatchers.is("observation-quality-reviewed"));
        Assert.assertThat(omParameters.get(1), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(1).getValue(), CoreMatchers.instanceOf(BooleanValue.class));
        Assert.assertThat(((BooleanValue)omParameters.get(1).getValue()).getValue().booleanValue(),
                CoreMatchers.is(false));
    }

    @Test
    public void shouldReturnCountParameter() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        Assert.assertThat(omParameters.get(0).getName().getTitleOrFromHref(), CoreMatchers.is("count-parameter"));
        Assert.assertThat(omParameters.get(0), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(0).getValue(), CoreMatchers.instanceOf(CountValue.class));
        Assert.assertThat(((CountValue)omParameters.get(0).getValue()).getValue().intValue(), CoreMatchers.is(52));
    }

    @Test
    public void shouldReturnQuantityParameter() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        Assert.assertThat(omParameters.get(1).getName().getTitleOrFromHref(), CoreMatchers.is("numeric-parameter"));
        Assert.assertThat(omParameters.get(1), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(1).getValue(), CoreMatchers.instanceOf(QuantityValue.class));
        Assert.assertThat(((QuantityValue)omParameters.get(1).getValue()).getValue().doubleValue(),
                CoreMatchers.is(42.0));
        Assert.assertThat(((QuantityValue)omParameters.get(1).getValue()).getUom(), CoreMatchers.is("test-uom-uri"));
    }

    @Test
    public void shouldReturnTextParameter() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        Assert.assertThat(omParameters.get(3).getName().getTitleOrFromHref(), CoreMatchers.is("text-parameter"));
        Assert.assertThat(omParameters.get(3), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(3).getValue(), CoreMatchers.instanceOf(TextValue.class));
        Assert.assertThat(((TextValue)omParameters.get(3).getValue()).getValue(), CoreMatchers.is("test-text"));
    }

    @Test
    public void shouldReturnCategoryParameter() throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        Assert.assertThat(omParameters.get(4).getName().getTitleOrFromHref(), CoreMatchers.is("category-parameter"));
        Assert.assertThat(omParameters.get(4), CoreMatchers.instanceOf(NamedValue.class));
        Assert.assertThat(omParameters.get(4).getValue(), CoreMatchers.instanceOf(CategoryValue.class));
        Assert.assertThat(((CategoryValue)omParameters.get(4).getValue()).getValue(), CoreMatchers.is("test-category"));
    }

    @Test
    public void shouldReturnFeatureOfInterestWithParentFeatureIdentifier()
            throws XmlException, IOException, ParseException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/features/parent-feature-identifier_set.xml");

        FeatureOfInterest featureOfInterest = new DataFile(configuration, null)
                .getFoiForColumn(4,
                        new String[]{"feature", "sensor", "52.0", "42.0", "123.45", "property", "12/24/2002 12.40 PM"});

        Assert.assertThat(featureOfInterest.hasParentFeature(), Is.is(true));
        Assert.assertThat(featureOfInterest.getParentFeature(), Is.is("test-parent-feature"));
    }

    @Test
    public void shouldReturnFeatureOfInterest()
            throws XmlException, IOException, ParseException, URISyntaxException {
        Configuration configuration =
                new Configuration("src/test/resources/features/parent-feature-identifier_set.xml");

        FeatureOfInterest featureOfInterest = new DataFile(configuration, null)
                .getFoiForColumn(4,
                        new String[]{"feature", "sensor", "52.0", "42.0", "123.45", "property", "uom",
                                "12/24/2002 12.40 PM"});

        Assert.assertThat(featureOfInterest.getName(), Is.is("feature"));
        Assert.assertThat(featureOfInterest.getUri().toString(), Is.is("feature"));
        Assert.assertThat(featureOfInterest.getPosition(), Is.is(Matchers.notNullValue()));
        Position position = featureOfInterest.getPosition();
        Assert.assertThat(position.getEpsgCode(), Is.is(4326));
        // next value is from configuration file
        Assert.assertThat(position.getAltitude(), Is.is(0.0));
        Assert.assertThat(position.getAltitudeUnit(), Is.is("m"));
        Assert.assertThat(position.getLatitude(), Is.is(52.0));
        Assert.assertThat(position.getLatitudeUnit(), Is.is("deg"));
        Assert.assertThat(position.getLongitude(), Is.is(42.0));
        Assert.assertThat(position.getLongitudeUnit(), Is.is("deg"));
    }

}
