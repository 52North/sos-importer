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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.sos.importer.feeder.Configuration.COORDINATE_UNIT;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.util.EPSGHelper;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CoordinateDocument.Coordinate;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.SpatialResourceType;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument.Type;

public class ConfigurationTest {

    @Test
    public void isSetOmParameterShouldReturnFalseIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");
        assertThat(configuration.isOmParameterAvailableFor(4), is(false));
    }

    @Test
    public void isSetOmParameterShoulodReturnTrueIfRelatedOmParameterIsSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");
        assertThat(configuration.isOmParameterAvailableFor(4), is(true));
    }

    @Test
    public void isSetOmParameterShouldReturnTrueIfAtLeastOneOmParameterColumnIsDefined()
            throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");
        assertThat(configuration.isOmParameterAvailableFor(4), is(true));
    }

    @Test
    public void getColumnsForOmParametersShouldReturnEmptyListIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");
        assertThat(configuration.getColumnsForOmParameter(4), notNullValue());
        assertThat(configuration.getColumnsForOmParameter(4).isEmpty(), is(true));
    }

    @Test
    public void getColumnsForOmParameterShouldReturnCorrectValues() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");

        List<Column> omParameterColumns = configuration.getColumnsForOmParameter(4);
        assertThat(omParameterColumns.isEmpty(), is(false));
        assertThat(omParameterColumns.size(), is(1));
        Column column = omParameterColumns.get(0);
        assertThat(column.getType(), is(Type.OM_PARAMETER));
        assertThat(column.getMetadataArray().length, is(1));
        assertThat(column.getMetadataArray(0).getKey(), is(Key.TYPE));
        assertThat(column.getMetadataArray(0).getValue(), is("COUNT"));

        Configuration configuration2 = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");

        List<Column> omParameterColumns2 = configuration2.getColumnsForOmParameter(4);
        assertThat(omParameterColumns2.isEmpty(), is(false));
        assertThat(omParameterColumns2.size(), is(2));
        Column column2 = omParameterColumns2.get(0);
        assertThat(column2.getType(), is(Type.OM_PARAMETER));
        assertThat(column2.getMetadataArray().length, is(2));
        assertThat(column2.getMetadataArray(0).getKey(), is(Key.TYPE));
        assertThat(column2.getMetadataArray(0).getValue(), is("BOOLEAN"));

        Column column3 = omParameterColumns2.get(1);
        assertThat(column3.getMetadataArray().length, is(2));
        assertThat(column3.getMetadataArray(0).getKey(), is(Key.TYPE));
        assertThat(column3.getMetadataArray(0).getValue(), is("BOOLEAN"));
    }

    @Test
    public void isNoDataValueDefinedAndMatchingShouldReturnFalseIfNotSet() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_set_as_related.xml");
        Column column = configuration.getColumnById(4);
        assertThat(configuration.isNoDataValueDefinedAndMatching(column, "14.8"), is(false));
    }

    @Test
    public void isNoDataValueDefinedAndMatchingShouldReturnTrueIfSetAndMatching() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/no_data_value_set.xml");
        Column column = configuration.getColumnById(4);
        assertThat(configuration.isNoDataValueDefinedAndMatching(column, "---"), is(true));
    }

    @Test
    public void shouldReturnURLFromRemoteFileURL() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/configWithRemoteFile.xml");
        // URL taken from config file
        assertThat(configuration.getRemoteFileURL(), is("http://www.example.com/my/fancy/directoryTree/data.csv"));
    }

    @Test
    public void hasReferenceValuesShouldReturnFalse() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/reference-values_without.xml");
        assertThat(configuration.hasReferenceValues(), is(false));
    }

    @Test
    public void hasReferenceValuesShouldReturnTrueIfPresent() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/reference-values_with.xml");
        assertThat(configuration.hasReferenceValues(), is(true));
    }

    @Test
    public void getReferenceValuesShouldReturnEmptyListWhenNoneAreAvailable() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/reference-values_with.xml");
        URI sensorURI = URI.create("not-existing-sensor"); // "http://example.com/krypto-graph";
        assertThat(configuration.getReferenceValues(sensorURI).size(), is(0));
    }

    @Test
    public void getReferenceValuesTest() throws XmlException, IOException {
        org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position position =
                SpatialResourceType.Factory.newInstance().addNewPosition();
        position.setEPSGCode(4979);
        Coordinate coordinate = position.addNewCoordinate();
        coordinate.setUnit("unit");
        coordinate.setAxisAbbreviation("axisabbrev");
        coordinate.setDoubleValue(52.0);
        coordinate = position.addNewCoordinate();
        coordinate.setUnit("unit2");
        coordinate.setAxisAbbreviation("axisabbrev2");
        coordinate.setDoubleValue(52.02);
        coordinate = position.addNewCoordinate();
        coordinate.setUnit("unit3");
        coordinate.setAxisAbbreviation("axisabbrev3");
        coordinate.setDoubleValue(52.03);
        Configuration configuration = new Configuration("src/test/resources/features/reference-values_with.xml");
        URI sensorURI = URI.create("http://example.com/krypto-graph");
        Map<ObservedProperty, List<SimpleEntry<String, String>>> referenceValueMap = configuration
                .getReferenceValues(sensorURI);
        assertThat(referenceValueMap.size(), is(1));
        List<SimpleEntry<String, String>> referenceValues = referenceValueMap
                .get(referenceValueMap.keySet().iterator().next());
        assertThat(referenceValues, hasSize(2));
        assertThat(referenceValues, hasItem(new SimpleEntry<>("ref-value-1-label", "42.0")));
        assertThat(referenceValues, hasItem(new SimpleEntry<>("ref-value-2-label", "23.0")));
    }

    @Test
    public void getPositionShouldReturnValidPosition() throws XmlException, IOException, ParseException {
        Configuration configuration = new Configuration("src/test/resources/features/feature-position-in-data.xml");
        Position position = configuration.getPosition("A",
                new String[] { "4326", "52.0", "42.0", "timestamp", "value"});
        assertThat(position, notNullValue());
        assertThat(position.getEpsgCode(), is(4326));
        assertThat(EPSGHelper.isValidEPSGCode(position.getEpsgCode()), is(true));
        assertThat(position.getValueByAxisAbbreviation("Lat"), is(52.0));
        assertThat(position.getUnitByAxisAbbreviation("Lat"), is(COORDINATE_UNIT));
        assertThat(position.getValueByAxisAbbreviation("Long"), is(42.0));
        assertThat(position.getUnitByAxisAbbreviation("Long"), is(COORDINATE_UNIT));
    }

    @Test
    public void arePhenomenonTimeAvailableShouldReturnFalseIfNotDefined() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/feature-position-in-data.xml");
        assertThat(configuration.arePhenomenonTimesAvailable(5), is(false));
    }

    @Test
    public void arePhenomenonTimeAvailableShouldReturnTrueIfIntervalIsDefined() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_all_config.xml");
        assertThat(configuration.arePhenomenonTimesAvailable(5), is(true));
    }

    @Test
    public void arePhenomenonTimeAvailableShouldReturnTrueIfInstantIsDefined() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        assertThat(configuration.arePhenomenonTimesAvailable(5), is(true));
    }


    @Test
    public void getColumnsByTypeShouldReturn1() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/example-data/example-1-config.xml");
        assertThat(configuration.getColumnsByType(Type.DATE_TIME).size(), is(1));
    }

    @Test
    public void getColumnsByTypeShouldReturn3() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_all_config.xml");
        assertThat(configuration.getColumnsByType(Type.DATE_TIME).size(), is(3));
    }

    @Test
    public void getColumnsByTypeShouldReturn5() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        assertThat(configuration.getColumnsByType(Type.DATE_TIME).size(), is(2));
    }

    @Test
    public void getMetadataValueWithColumnShouldReturnAbsentValueIfNotAvailable() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        Column column = Column.Factory.newInstance();
        assertThat(configuration.getMetadataValue(column, Key.GROUP).isPresent(), is(false));
    }

    @Test
    public void getMetadataValueWithIndexShouldReturnAbsentValueIfNotAvailable() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        assertThat(configuration.getMetadataValue(0, Key.GROUP).isPresent(), is(false));
    }

    @Test
    public void getMetadataValueWithIndexShouldReturnValueIfAvailable() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_instant_config.xml");
        Optional<String> metadataValue = configuration.getMetadataValue(1, Key.TYPE);
        assertThat(metadataValue.isPresent(), is(true));
        assertThat(metadataValue.get(), is("NUMERIC"));
    }

    @Test
    public void getDateTimeColumnsForMeasureValueShouldReturn3ColumnsWhenConfigured() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/timestamps_all_config.xml");
        List<Column> columns = configuration.getDateTimeColumnsForMeasureValue(1);
        assertThat(columns, is(notNullValue()));
        assertThat(columns.size(), is(3));
        columns.stream().forEach(c -> assertThat(c.getType(), is(Type.DATE_TIME)));
    }

    @Test
    public void getDataFileEncodingShouldReturnLatin1IfSetInConfguration() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/data-file-encoding_latin1.xml");
        assertThat(configuration.getDataFileEncoding(), is("Latin1"));
    }

    @Test
    public void getDataFileEncodingShouldReturnUTF8IfNotSetInConfguration() throws IllegalArgumentException, XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/data-file-encoding_not-set.xml");
        assertThat(configuration.getDataFileEncoding(), is("UTF-8"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void getDataFileEncodingShouldThrowIllegalArgumentExpectionIfInvalidCharsetNameSetInConfguration() throws XmlException, IOException {
        Configuration configuration = new Configuration("src/test/resources/features/data-file-encoding_invalid.xml");
        configuration.getDataFileEncoding();
    }

}
