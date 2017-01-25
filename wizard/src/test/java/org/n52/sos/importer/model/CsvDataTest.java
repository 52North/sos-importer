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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 * @since 0.5.0
 */
package org.n52.sos.importer.model;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CsvDataTest {

    private static final String COL1 = "col1";
    private static final String COL2 = "col2";
    private static final String COL3 = "col3";
    
    private CsvData data;

    /**
     * <p>setupData.</p>
     */
    @Before
    public void setupData() {
        data = new CsvData();
    }

    /**
     * <p>shouldReturnRowCountZeroIfLinesNotSet.</p>
     */
    @Test
    public void shouldReturnRowCountZeroIfLinesNotSet() {
        Assert.assertThat(data.getRowCount(), Is.is(0));
    }

    /**
     * <p>shouldReturnColumnCountZeroIfLinesNotSet.</p>
     */
    @Test
    public void shouldReturnColumnCountZeroIfLinesNotSet() {
        Assert.assertThat(data.getColumnCount(), Is.is(0));
    }

    /**
     * <p>shouldReturnZeroForCountsIfLinesIsNull.</p>
     */
    @Test
    public void shouldReturnZeroForCountsIfLinesIsNull() {
        data.setLines(null);

        Assert.assertThat(data.getRowCount(), Is.is(0));
        Assert.assertThat(data.getColumnCount(), Is.is(0));
    }

    /**
     * <p>shouldReturnCorrectColumnCount.</p>
     */
    @Test
    public void shouldReturnCorrectColumnCount() {
        final List<String[]> testData = new LinkedList<>();
        testData.add(new String[] {COL1, COL2});
        testData.add(new String[] {COL1});
        testData.add(new String[] {COL1, COL2, COL3});
        data.setLines(testData);

        Assert.assertThat(data.getColumnCount(), Is.is(3));
        Assert.assertThat(data.getRowCount(), Is.is(3));
    }

    /**
     * <p>shouldReturnLineFilledWithEmptyStringsIfColumnContainedLessValuesThanColumnCount.</p>
     */
    @Test
    public void shouldReturnLineFilledWithEmptyStringsIfColumnContainedLessValuesThanColumnCount() {
        final List<String[]> testData = new LinkedList<>();
        testData.add(new String[] {COL1, COL2});
        testData.add(new String[] {COL1});
        testData.add(new String[] {COL1, COL2, COL3});
        data.setLines(testData);

        Assert.assertThat(data.getLine(1).length, Is.is(data.getColumnCount()));
        for (int i = 1; i < data.getLine(1).length; i++) {
            final String string = data.getLine(1)[i];
            Assert.assertThat(string, Is.is(""));
        }
    }
}
