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
package org.n52.sos.importer.model.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.model.Step2Model;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2ModelHandlerTest {

	@Test
	public void shouldSetSampleBasedStartRegEx() {
		final String sampleBasedStartRegEx = "test-regex";
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx(sampleBasedStartRegEx);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleStartRegEx(), is(true));
		assertThat(importConf.getDataFile().getSampleStartRegEx(), is(sampleBasedStartRegEx));
	}

	@Test
	public void shouldSetSampleBasedDateOffset() {
		final int dateOffset = 25;
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(dateOffset);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleDateOffset(), is(true));
		assertThat(importConf.getDataFile().getSampleDateOffset(), is(dateOffset));
	}

	@Test
	public void shouldSetSampleBasedDateExtractionRegEx() {
		final String dateExtractionRegEx = "test-regex-2";
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(25)
			.setSampleBasedDateExtractionRegEx(dateExtractionRegEx);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleDateExtractionRegEx(), is(true));
		assertThat(importConf.getDataFile().getSampleDateExtractionRegEx(), is(dateExtractionRegEx));
	}

	@Test
	public void shouldSetSampleBasedDatePattern() {
		final String datePattern = "test-regex-2";
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(25)
			.setSampleBasedDateExtractionRegEx("test-regex-2")
			.setSampleBasedDatePattern(datePattern);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleDatePattern(), is(true));
		assertThat(importConf.getDataFile().getSampleDatePattern(), is(datePattern));
	}

	@Test
	public void shouldSetSampleBasedDataOffset() {
		final int dataOffset = 42;
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(25)
			.setSampleBasedDateExtractionRegEx("test-regex-2")
			.setSampleBasedDatePattern("test-pattern")
			.setSampleBasedDataOffset(dataOffset);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleDataOffset(), is(true));
		assertThat(importConf.getDataFile().getSampleDataOffset(), is(dataOffset));
	}

	@Test
	public void shouldSetSampleBasedSampleSizeOffset() {
		final int sampleSizeOffset = 42;
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(25)
			.setSampleBasedDateExtractionRegEx("test-regex-2")
			.setSampleBasedDatePattern("test-pattern")
			.setSampleBasedDataOffset(6)
			.setSampleBasedSampleSizeOffset(sampleSizeOffset);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleSizeOffset(), is(true));
		assertThat(importConf.getDataFile().getSampleSizeOffset(), is(sampleSizeOffset));
	}

	@Test
	public void shouldSetSampleBasedSampleSizeRegEx() {
		final String sampleSizeRegEx = "test-regex";
		final Step2Model stepModel = new Step2Model("",2)
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(25)
			.setSampleBasedDateExtractionRegEx("test-regex-2")
			.setSampleBasedDatePattern("test-pattern")
			.setSampleBasedDataOffset(6)
			.setSampleBasedSampleSizeOffset(42)
			.setSampleBasedSampleSizeRegEx(sampleSizeRegEx);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step2ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getDataFile().isSetSampleSizeRegEx(), is(true));
		assertThat(importConf.getDataFile().getSampleSizeRegEx(), is(sampleSizeRegEx));
	}

}
