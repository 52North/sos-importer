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

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.sos.importer.feeder.Configuration.COORDINATE_UNIT;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.PhenomenonTime;
import org.n52.sos.importer.feeder.model.Position;

public class DataFileTest {

    private static final String[] EXAMPLE_DATA_ALL_PARAMETER_TYPES = new String[] {
            "0", "52", "42.0", "true", "test-text", "test-category" };

    @Test
    public void shouldReturnEmptyListIfNothingIsAvailable()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");
        assertThat(new DataFile(configuration, null).getOmParameters(4, null), is(empty()));
        assertThat(new DataFile(configuration, null).getOmParameters(4, new String[0]),
                is(empty()));
        assertThat(
                new DataFile(configuration, null).getOmParameters(4,
                        new String[] { "0", "1", "2", "3", "4", "5", "7", "8", "9" }),
                is(empty()));
    }

    @Test
    public void shouldReturnBooleanParameter()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(4,
                new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "true", "false", "10" });

        assertThat(omParameter.isPresent(), is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        assertThat(omParameters.get(0).getName().getTitleOrFromHref(), is("number-of-reviews"));
        assertThat(omParameters.get(0), instanceOf(NamedValue.class));
        assertThat(omParameters.get(0).getValue(), instanceOf(BooleanValue.class));
        assertThat(((BooleanValue) omParameters.get(0).getValue()).getValue().booleanValue(),
                is(true));
        assertThat(omParameters.get(1).getName().getTitleOrFromHref(),
                is("observation-quality-reviewed"));
        assertThat(omParameters.get(1), instanceOf(NamedValue.class));
        assertThat(omParameters.get(1).getValue(), instanceOf(BooleanValue.class));
        assertThat(((BooleanValue) omParameters.get(1).getValue()).getValue().booleanValue(),
                is(false));
    }

    @Test
    public void shouldReturnCountParameter()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        assertThat(omParameter.isPresent(), is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        assertThat(omParameters.get(0).getName().getTitleOrFromHref(), is("count-parameter"));
        assertThat(omParameters.get(0), instanceOf(NamedValue.class));
        assertThat(omParameters.get(0).getValue(), instanceOf(CountValue.class));
        assertThat(((CountValue) omParameters.get(0).getValue()).getValue().intValue(), is(52));
    }

    @Test
    public void shouldReturnQuantityParameter()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        assertThat(omParameter.isPresent(), is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        assertThat(omParameters.get(1).getName().getTitleOrFromHref(), is("numeric-parameter"));
        assertThat(omParameters.get(1), instanceOf(NamedValue.class));
        assertThat(omParameters.get(1).getValue(), instanceOf(QuantityValue.class));
        assertThat(((QuantityValue) omParameters.get(1).getValue()).getValue().doubleValue(),
                is(42.0));
        assertThat(((QuantityValue) omParameters.get(1).getValue()).getUom(), is("test-uom-uri"));
    }

    @Test
    public void shouldReturnTextParameter()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        assertThat(omParameter.isPresent(), is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        assertThat(omParameters.get(3).getName().getTitleOrFromHref(), is("text-parameter"));
        assertThat(omParameters.get(3), instanceOf(NamedValue.class));
        assertThat(omParameters.get(3).getValue(), instanceOf(TextValue.class));
        assertThat(((TextValue) omParameters.get(3).getValue()).getValue(), is("test-text"));
    }

    @Test
    public void shouldReturnCategoryParameter()
            throws XmlException, IOException, NumberFormatException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

        Optional<List<NamedValue<?>>> omParameter = new DataFile(configuration, null).getOmParameters(0,
                EXAMPLE_DATA_ALL_PARAMETER_TYPES);

        assertThat(omParameter.isPresent(), is(true));
        List<NamedValue<?>> omParameters = omParameter.get();
        assertThat(omParameters.get(4).getName().getTitleOrFromHref(), is("category-parameter"));
        assertThat(omParameters.get(4), instanceOf(NamedValue.class));
        assertThat(omParameters.get(4).getValue(), instanceOf(CategoryValue.class));
        assertThat(((CategoryValue) omParameters.get(4).getValue()).getValue(), is("test-category"));
    }

    @Test
    public void shouldReturnFeatureOfInterestWithParentFeatureIdentifier()
            throws XmlException, IOException, ParseException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/features/parent-feature-identifier_set.xml");

        FeatureOfInterest featureOfInterest = new DataFile(configuration, null)
                .getFoiForColumn(4,
                        new String[] { "feature", "sensor", "52.0", "42.0", "123.45", "property",
                                "12/24/2002 12.40 PM" });

        assertThat(featureOfInterest.hasParentFeature(), is(true));
        assertThat(featureOfInterest.getParentFeature(), is("test-parent-feature"));
    }

    @Test
    public void shouldReturnFeatureOfInterest()
            throws XmlException, IOException, ParseException, URISyntaxException {
        Configuration configuration = new Configuration(
                "src/test/resources/features/parent-feature-identifier_set.xml");

        FeatureOfInterest featureOfInterest = new DataFile(configuration, null)
                .getFoiForColumn(4,
                        new String[] { "feature", "sensor", "52.0", "42.0", "123.45", "property", "uom",
                                "12/24/2002 12.40 PM" });

        assertThat(featureOfInterest.getName(), is("feature"));
        assertThat(featureOfInterest.getUri().toString(), is("feature"));
        assertThat(featureOfInterest.getPosition(), is(Matchers.notNullValue()));
        Position position = featureOfInterest.getPosition();
        assertThat(position.getEpsgCode(), is(4979));
        // next value is from configuration file
        assertThat(position.getValueByAxisAbbreviation("h"), is(0.0));
        assertThat(position.getUnitByAxisAbbreviation("h"), is("m"));
        assertThat(position.getValueByAxisAbbreviation("Lat"), is(52.0));
        assertThat(position.getUnitByAxisAbbreviation("Lat"), is(COORDINATE_UNIT));
        assertThat(position.getValueByAxisAbbreviation("Long"), is(42.0));
        assertThat(position.getUnitByAxisAbbreviation("Long"), is(COORDINATE_UNIT));
    }

    @Test
    public void shouldReturnValidPhentimeForAnInterval()
            throws IllegalArgumentException, XmlException, IOException, ParseException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_all_config.xml");
        DataFile dataFile = new DataFile(configuration, null);
        PhenomenonTime phenomenonTime = dataFile.getPhenomenonTime(1, new String[] {"lead", "0.87", "µg/l",
                "lead-sensor-0815", "18-07-2018 12:00", "15-01-2018 12:00", "17-01-2018 12:00",
                "http://example.com/feature/0815-52-42", "42.0815", ";7.52"});
        assertThat(phenomenonTime.isInstant(), is(false));
        assertThat(phenomenonTime.toISO8601String(), is("2018-01-15T12:00:00+01:00/2018-01-17T12:00:00+01:00"));
    }

    @Test
    public void shouldReturnValidPhentimeForAnInstant()
            throws IllegalArgumentException, XmlException, IOException, ParseException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        DataFile dataFile = new DataFile(configuration, null);
        PhenomenonTime phenomenonTime = dataFile.getPhenomenonTime(1, new String[] {"lead", "0.87", "µg/l",
                "lead-sensor-0815", "18-07-2018 12:00", "15-01-2018 12:00", "17-01-2018 12:00",
                "http://example.com/feature/0815-52-42", "42.0815", ";7.52"});
        assertThat(phenomenonTime.isInstant(), is(true));
        assertThat(phenomenonTime.toISO8601String(), is("2018-01-15T12:00:00+01:00"));
    }
}
