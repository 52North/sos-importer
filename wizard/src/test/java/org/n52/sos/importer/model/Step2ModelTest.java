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
package org.n52.sos.importer.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>Step2ModelTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 * @since 0.5.0
 */
public class Step2ModelTest {

	private Step2Model model;

	/**
	 * <p>init.</p>
	 */
	@Before
	public void init() {
		model = new Step2Model("", 0);
	}

	/**
	 * <p>shouldReturnTrueIfIsSampleBasedIsSet.</p>
	 */
	@Test
	public void shouldReturnTrueIfIsSampleBasedIsSet() {
		assertThat(model.setSampleBased(true).isSampleBased(), is(true));
	}

	/**
	 * <p>shouldReturnFalseAsDefaultValueForSampleBased.</p>
	 */
	@Test
	public void shouldReturnFalseAsDefaultValueForSampleBased() {
		assertThat(model.isSampleBased(), is(false));
	}

	/**
	 * <p>shouldReturnStringIfIsSampleBasedStartRegExIsSet.</p>
	 */
	@Test
	public void shouldReturnStringIfIsSampleBasedStartRegExIsSet() {
		final String sampleBasedStartRegEx = "test-regex";
		assertThat(model.setSampleBasedStartRegEx(sampleBasedStartRegEx).getSampleBasedStartRegEx(), is(sampleBasedStartRegEx));
	}

	/**
	 * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedStartRegEx.</p>
	 */
	@Test
	public void shouldReturnEmptyStringAsDefaultValueForSampleBasedStartRegEx() {
		assertThat(model.getSampleBasedStartRegEx(), is(""));
	}

	/**
	 * <p>shouldReturnValueIfIsSampleBasedDateOffsetIsSet.</p>
	 */
	@Test
	public void shouldReturnValueIfIsSampleBasedDateOffsetIsSet() {
		final int dateOffset = 25;
		assertThat(model.setSampleBasedDateOffset(dateOffset).getSampleBasedDateOffset(), is(dateOffset));
	}

	/**
	 * <p>shouldReturnZeroAsDefaultValueForSampleBasedDateOffset.</p>
	 */
	@Test
	public void shouldReturnZeroAsDefaultValueForSampleBasedDateOffset() {
		assertThat(model.getSampleBasedDateOffset(), is(0));
	}

	/**
	 * <p>shouldReturnStringIfIsSampleBasedDateExtractionRegExIsSet.</p>
	 */
	@Test
	public void shouldReturnStringIfIsSampleBasedDateExtractionRegExIsSet() {
		final String sampleBasedDateExtractionRegEx = "test-regex";
		assertThat(model.setSampleBasedDateExtractionRegEx(sampleBasedDateExtractionRegEx).getSampleBasedDateExtractionRegEx(), is(sampleBasedDateExtractionRegEx));
	}

	/**
	 * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedDateExtractionRegEx.</p>
	 */
	@Test
	public void shouldReturnEmptyStringAsDefaultValueForSampleBasedDateExtractionRegEx() {
		assertThat(model.getSampleBasedDateExtractionRegEx(), is(""));
	}

	/**
	 * <p>shouldReturnStringIfIsSampleBasedDatePatternIsSet.</p>
	 */
	@Test
	public void shouldReturnStringIfIsSampleBasedDatePatternIsSet() {
		final String datePattern = "test-regex";
		assertThat(model.setSampleBasedDatePattern(datePattern).getSampleBasedDatePattern(), is(datePattern));
	}

	/**
	 * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedDatePattern.</p>
	 */
	@Test
	public void shouldReturnEmptyStringAsDefaultValueForSampleBasedDatePattern() {
		assertThat(model.getSampleBasedDatePattern(), is(""));
	}

	/**
	 * <p>shouldReturnValueIfIsSampleBasedDataOffsetIsSet.</p>
	 */
	@Test
	public void shouldReturnValueIfIsSampleBasedDataOffsetIsSet() {
		final int dataOffset = 25;
		assertThat(model.setSampleBasedDataOffset(dataOffset).getSampleBasedDataOffset(), is(dataOffset));
	}

	/**
	 * <p>shouldReturnZeroAsDefaultValueForSampleBasedDataOffset.</p>
	 */
	@Test
	public void shouldReturnZeroAsDefaultValueForSampleBasedDataOffset() {
		assertThat(model.getSampleBasedDataOffset(), is(0));
	}

	/**
	 * <p>shouldReturnValueIfIsSampleBasedSampleSizeOffsetIsSet.</p>
	 */
	@Test
	public void shouldReturnValueIfIsSampleBasedSampleSizeOffsetIsSet() {
		final int sampleSizeOffset = 25;
		assertThat(model.setSampleBasedSampleSizeOffset(sampleSizeOffset).getSampleBasedSampleSizeOffset(), is(sampleSizeOffset));
	}

	/**
	 * <p>shouldReturnZeroAsDefaultValueForSampleBasedSampleSizeOffset.</p>
	 */
	@Test
	public void shouldReturnZeroAsDefaultValueForSampleBasedSampleSizeOffset() {
		assertThat(model.getSampleBasedSampleSizeOffset(), is(0));
	}

	/**
	 * <p>shouldReturnStringIfIsSampleBasedSampleSizeRegExIsSet.</p>
	 */
	@Test
	public void shouldReturnStringIfIsSampleBasedSampleSizeRegExIsSet() {
		final String sampleBasedSampleSizeRegEx = "test-regex";
		assertThat(model.setSampleBasedSampleSizeRegEx(sampleBasedSampleSizeRegEx).getSampleBasedSampleSizeRegEx(), is(sampleBasedSampleSizeRegEx));
	}

	/**
	 * <p>shouldReturnEmptyStringAsDefaultValueForSampleBasedSampleSizeRegEx.</p>
	 */
	@Test
	public void shouldReturnEmptyStringAsDefaultValueForSampleBasedSampleSizeRegEx() {
		assertThat(model.getSampleBasedSampleSizeRegEx(), is(""));
	}
}
