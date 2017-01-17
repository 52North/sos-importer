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
package org.n52.sos.importer.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>Step2PanelTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 * @since 0.5.0
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step2PanelTest {

    private static final int MAX_ROWS = 30;

    private Step2Panel panel;

    /**
     * <p>init.</p>
     */
    @Before
    public void init() {
        panel = new Step2Panel(MAX_ROWS);
    }

    /**
     * <p>shouldReturnTrueIfIsSampleBasedIsSet.</p>
     */
    @Test
    public void shouldReturnTrueIfIsSampleBasedIsSet() {
        assertThat(panel.setSampleBased(true).isSampleBased(), is(true));
    }

    /**
     * <p>shouldReturnFalseAsDefaultValueForSampleBased.</p>
     */
    @Test
    public void shouldReturnFalseAsDefaultValueForSampleBased() {
        assertThat(panel.isSampleBased(), is(false));
    }

    /**
     * <p>shouldReturnStringIfIsSampleBasedStartRegExIsSet.</p>
     */
    @Test
    public void shouldReturnStringIfIsSampleBasedStartRegExIsSet() {
        final String sampleBasedStartRegEx = "test-regex";
        assertThat(panel.setSampleBasedStartRegEx(sampleBasedStartRegEx).getSampleBasedStartRegEx(), is(sampleBasedStartRegEx));
    }

    /**
     * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedStartRegEx.</p>
     */
    @Test
    public void shouldReturnEmptyStringAsDefaultValueForSampleBasedStartRegEx() {
        assertThat(panel.getSampleBasedStartRegEx(), is(""));
    }

    /**
     * <p>shouldReturnValueIfIsSampleBasedDateOffsetIsSet.</p>
     */
    @Test
    public void shouldReturnValueIfIsSampleBasedDateOffsetIsSet() {
        final int dateOffset = MAX_ROWS-5;
        assertThat(panel.setSampleBasedDateOffset(dateOffset).getSampleBasedDateOffset(), is(dateOffset));
    }

    /**
     * <p>shouldReturnOneAsDefaultValueForDateOffset.</p>
     */
    @Test
    public void shouldReturnOneAsDefaultValueForDateOffset() {
        assertThat(panel.getSampleBasedDateOffset(), is(1));
    }

    /**
     * <p>shouldReturnStringIfIsSampleBasedDateExtractionRegExIsSet.</p>
     */
    @Test
    public void shouldReturnStringIfIsSampleBasedDateExtractionRegExIsSet() {
        final String dateExtractionRegEx = "test-regex";
        assertThat(panel.setSampleBasedDateExtractionRegEx(dateExtractionRegEx)
                .getSampleBasedDateExtractionRegEx(),
                is(dateExtractionRegEx));
    }

    /**
     * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedDateExtractionRegEx.</p>
     */
    @Test
    public void shouldReturnEmptyStringAsDefaultValueForSampleBasedDateExtractionRegEx() {
        assertThat(panel.getSampleBasedDateExtractionRegEx(), is(""));
    }

    /**
     * <p>shouldReturnStringIfIsSampleBasedDatePatternIsSet.</p>
     */
    @Test
    public void shouldReturnStringIfIsSampleBasedDatePatternIsSet() {
        final String datePattern = "test-regex";
        assertThat(panel.setSampleBasedDatePattern(datePattern).getSampleBasedDatePattern(), is(datePattern));
    }

    /**
     * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedDatePattern.</p>
     */
    @Test
    public void shouldReturnEmptyStringAsDefaultValueForSampleBasedDatePattern() {
        assertThat(panel.getSampleBasedDatePattern(), is(""));
    }

    /**
     * <p>shouldReturnValueIfIsSampleBasedDataOffsetIsSet.</p>
     */
    @Test
    public void shouldReturnValueIfIsSampleBasedDataOffsetIsSet() {
        final int dataOffset = MAX_ROWS-5;
        assertThat(panel.setSampleBasedDataOffset(dataOffset).getSampleBasedDataOffset(), is(dataOffset));
    }

    /**
     * <p>shouldReturnOneAsDefaultValueForDataOffset.</p>
     */
    @Test
    public void shouldReturnOneAsDefaultValueForDataOffset() {
        assertThat(panel.getSampleBasedDataOffset(), is(1));
    }

    /**
     * <p>shouldReturnValueIfIsSampleBasedSampleSizeOffsetIsSet.</p>
     */
    @Test
    public void shouldReturnValueIfIsSampleBasedSampleSizeOffsetIsSet() {
        final int sampleSizeOffset = MAX_ROWS-5;
        assertThat(panel.setSampleBasedSampleSizeOffset(sampleSizeOffset).getSampleBasedSampleSizeOffset(), is(sampleSizeOffset));
    }

    /**
     * <p>shouldReturnOneAsDefaultValueForSampleSizeOffset.</p>
     */
    @Test
    public void shouldReturnOneAsDefaultValueForSampleSizeOffset() {
        assertThat(panel.getSampleBasedSampleSizeOffset(), is(1));
    }

    /**
     * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedSampleSizeRegEx.</p>
     */
    @Test
    public void shouldReturnEmptyStringAsDefaultValueForSampleBasedSampleSizeRegEx() {
        assertThat(panel.getSampleBasedSampleSizeRegEx(), is(""));
    }

    /**
     * <p>shouldReturnStringIfIsSampleBasedSampleSizeRegExIsSet.</p>
     */
    @Test
    public void shouldReturnStringIfIsSampleBasedSampleSizeRegExIsSet() {
        final String sampleSizeRegEx = "test-regex";
        assertThat(panel.setSampleBasedSampleSizeRegEx(sampleSizeRegEx).getSampleBasedSampleSizeRegEx(), is(sampleSizeRegEx));
    }
}
