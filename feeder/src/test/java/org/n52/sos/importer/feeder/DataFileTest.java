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
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.om.x20.BooleanParameter;
import org.n52.oxf.om.x20.CountParameter;
import org.n52.oxf.om.x20.OmParameter;
import org.n52.oxf.om.x20.QuantityParameter;
import org.n52.oxf.om.x20.TextParameter;

public class DataFileTest {

    @Test
    public void shouldReturnEmptyListIfNothingIsAvailable() throws XmlException, IOException {
        Configuration configuration = new Configuration(
                "src/test/resources/feature_om-parameter/omparameter_not_set.xml");

	Assert.assertThat(
		new DataFile(configuration, null).getOmParameter(4, null),
		CoreMatchers.is(Optional.empty()));
	Assert.assertThat(
		new DataFile(configuration, null).getOmParameter(4, new String[0]),
		CoreMatchers.is(Optional.empty()));
	Assert.assertThat(
		new DataFile(configuration, null).getOmParameter(4,
			new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9"}),
		CoreMatchers.is(Optional.empty()));
    }

    @Test
    public void shouldReturnBooleanParameter() throws XmlException, IOException {
	Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column.xml");

	Optional<List<OmParameter<?>>> omParameter = new DataFile(configuration, null).getOmParameter(4,
		new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "true", "false", "10"});

	Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
	List<OmParameter<?>> omParameters = omParameter.get();
	Assert.assertThat(omParameters.get(0).getName(), CoreMatchers.is("urn:ogc:number-of-reviews"));
	Assert.assertThat(omParameters.get(0), CoreMatchers.instanceOf(BooleanParameter.class));
	Assert.assertThat(omParameters.get(0).getValue(), CoreMatchers.instanceOf(Boolean.class));
	Assert.assertThat(((Boolean)omParameters.get(0).getValue()).booleanValue(), CoreMatchers.is(true));
	Assert.assertThat(omParameters.get(1).getName(), CoreMatchers.is("urn:ogc:observation-quality-reviewed"));
	Assert.assertThat(omParameters.get(1), CoreMatchers.instanceOf(BooleanParameter.class));
	Assert.assertThat(omParameters.get(1).getValue(), CoreMatchers.instanceOf(Boolean.class));
	Assert.assertThat(((Boolean)omParameters.get(1).getValue()).booleanValue(), CoreMatchers.is(false));
    }

    @Test
    public void shouldReturnCountParameter() throws XmlException, IOException {
	Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

	Optional<List<OmParameter<?>>> omParameter = new DataFile(configuration, null).getOmParameter(0,
		EXAMPLE_DATA_ALL_PARAMETER_TYPES);

	Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
	List<OmParameter<?>> omParameters = omParameter.get();
	Assert.assertThat(omParameters.get(0).getName(), CoreMatchers.is("count-parameter"));
	Assert.assertThat(omParameters.get(0), CoreMatchers.instanceOf(CountParameter.class));
	Assert.assertThat(omParameters.get(0).getValue(), CoreMatchers.instanceOf(BigInteger.class));
	Assert.assertThat(((BigInteger)omParameters.get(0).getValue()).intValue(), CoreMatchers.is(52));
    }

    @Test
    public void shouldReturnQuantityParameter() throws XmlException, IOException {
	Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

	Optional<List<OmParameter<?>>> omParameter = new DataFile(configuration, null).getOmParameter(0,
		EXAMPLE_DATA_ALL_PARAMETER_TYPES);

	Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
	List<OmParameter<?>> omParameters = omParameter.get();
	Assert.assertThat(omParameters.get(1).getName(), CoreMatchers.is("numeric-parameter"));
	Assert.assertThat(omParameters.get(1), CoreMatchers.instanceOf(QuantityParameter.class));
	Assert.assertThat(omParameters.get(1).getValue(), CoreMatchers.instanceOf(Double.class));
	Assert.assertThat(((Double)omParameters.get(1).getValue()).doubleValue(), CoreMatchers.is(42.0));
	Assert.assertThat(((QuantityParameter)omParameters.get(1)).getUOM(), CoreMatchers.is("test-uom-uri"));
    }

    @Test
    public void shouldReturnTextParameter() throws XmlException, IOException {
	Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

	Optional<List<OmParameter<?>>> omParameter = new DataFile(configuration, null).getOmParameter(0,
		EXAMPLE_DATA_ALL_PARAMETER_TYPES);

	Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
	List<OmParameter<?>> omParameters = omParameter.get();
	Assert.assertThat(omParameters.get(3).getName(), CoreMatchers.is("text-parameter"));
	Assert.assertThat(omParameters.get(3), CoreMatchers.instanceOf(TextParameter.class));
	Assert.assertThat(omParameters.get(3).getValue(), CoreMatchers.instanceOf(String.class));
	Assert.assertThat(omParameters.get(3).getValue(), CoreMatchers.is("test-text"));
    }

    @Test
    public void shouldReturnCategoryParameter() throws XmlException, IOException {
	Configuration configuration =
                new Configuration("src/test/resources/feature_om-parameter/omparameter_set_in_column_all.xml");

	Optional<List<OmParameter<?>>> omParameter = new DataFile(configuration, null).getOmParameter(0,
		EXAMPLE_DATA_ALL_PARAMETER_TYPES);

	Assert.assertThat(omParameter.isPresent(), CoreMatchers.is(true));
	List<OmParameter<?>> omParameters = omParameter.get();
	Assert.assertThat(omParameters.get(4).getName(), CoreMatchers.is("category-parameter"));
	Assert.assertThat(omParameters.get(4), CoreMatchers.instanceOf(TextParameter.class));
	Assert.assertThat(omParameters.get(4).getValue(), CoreMatchers.instanceOf(String.class));
	Assert.assertThat(omParameters.get(4).getValue(), CoreMatchers.is("test-category"));
    }

    private static final String[] EXAMPLE_DATA_ALL_PARAMETER_TYPES =
	    new String[]{"0", "52", "42.0", "true", "test-text", "test-category"};


}
