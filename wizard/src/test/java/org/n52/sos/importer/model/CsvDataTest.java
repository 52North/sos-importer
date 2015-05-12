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
package org.n52.sos.importer.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CsvDataTest {

	private CsvData data;

	@Before
	public void setupData() {
		data = new CsvData();
	}

	@Test
	public void shouldReturnRowCountZeroIfLinesNotSet() {
		assertThat(data.getRowCount(), is(0));
	}

	@Test
	public void shouldReturnColumnCountZeroIfLinesNotSet() {
		assertThat(data.getColumnCount(), is(0));
	}

	@Test
	public void shouldReturnZeroForCountsIfLinesIsNull() {
		data.setLines(null);

		assertThat(data.getRowCount(), is(0));
		assertThat(data.getColumnCount(), is(0));
	}

	@Test
	public void shouldReturnCorrectColumnCount() {
		final List<String[]> testData = new LinkedList<>();
		testData.add(new String[] {"col1","col2"});
		testData.add(new String[] {"col1"});
		testData.add(new String[] {"col1","col2", "col3"});
		data.setLines(testData);

		assertThat(data.getColumnCount(), is(3));
		assertThat(data.getRowCount(), is(3));
	}

	@Test
	public void shouldReturnLineFilledWithEmptyStringsIfColumnContainedLessValuesThanColumnCount() {
		final List<String[]> testData = new LinkedList<>();
		testData.add(new String[] {"col1","col2"});
		testData.add(new String[] {"col1"});
		testData.add(new String[] {"col1","col2", "col3"});
		data.setLines(testData);

		assertThat(data.getLine(1).length, is(data.getColumnCount()));
		for (int i = 1; i < data.getLine(1).length; i++) {
			final String string = data.getLine(1)[i];
			assertThat(string, is(""));
		}
	}
}
