/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
