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
package org.n52.sos.importer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.sos.importer.model.Step2Model;

/*
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step2ControllerTest {

	private Step2Controller controller;

	/**
	 * <p>init.</p>
	 */
	@Before
	public void init() {
		controller = new Step2Controller(new Step2Model("", 1));
	}

	/**
	 * <p>shouldReturnFalseIfStartRegExIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfStartRegExIsMissing() {
		((Step2Model)controller.getModel())
			.setColumnSeparator(",")
			.setCommentIndicator("#")
			.setDecimalSeparator('.')
			.setTextQualifier("\"")
			.setSampleBased(true);
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfDateOffsetIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfDateOffsetIsMissing() {
		((Step2Model)controller.getModel())
			.setColumnSeparator(",")
			.setCommentIndicator("#")
			.setDecimalSeparator('.')
			.setTextQualifier("\"")
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex");
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfDateExtractionRegExIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfDateExtractionRegExIsMissing() {
		((Step2Model)controller.getModel())
			.setColumnSeparator(",")
			.setCommentIndicator("#")
			.setDecimalSeparator('.')
			.setTextQualifier("\"")
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(5);
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
		((Step2Model)controller.getModel()).setSampleBasedDateExtractionRegEx("test-regex");
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfDatePatternIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfDatePatternIsMissing() {
		((Step2Model)controller.getModel())
			.setColumnSeparator(",")
			.setCommentIndicator("#")
			.setDecimalSeparator('.')
			.setTextQualifier("\"")
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(5)
			.setSampleBasedDateExtractionRegEx("(test)-regex-2");
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfDataOffsetIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfDataOffsetIsMissing() {
		((Step2Model)controller.getModel())
		.setColumnSeparator(",")
		.setCommentIndicator("#")
		.setDecimalSeparator('.')
		.setTextQualifier("\"")
		.setSampleBased(true)
		.setSampleBasedStartRegEx("test-regex")
		.setSampleBasedDateOffset(5)
		.setSampleBasedDateExtractionRegEx("(test)-regex-2")
		.setSampleBasedDatePattern("test-pattern");
	controller.loadSettings();
	assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfSampleSizeOffsetIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfSampleSizeOffsetIsMissing() {
		((Step2Model)controller.getModel())
		.setColumnSeparator(",")
		.setCommentIndicator("#")
		.setDecimalSeparator('.')
		.setTextQualifier("\"")
		.setSampleBased(true)
		.setSampleBasedStartRegEx("test-regex")
		.setSampleBasedDateOffset(5)
		.setSampleBasedDateExtractionRegEx("(test)-regex-2")
		.setSampleBasedDatePattern("test-pattern")
		.setSampleBasedDataOffset(6);
	controller.loadSettings();
	assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnFalseIfSampleSizeRegExIsMissing.</p>
	 */
	@Test
	public void shouldReturnFalseIfSampleSizeRegExIsMissing() {
		((Step2Model)controller.getModel())
			.setColumnSeparator(",")
			.setCommentIndicator("#")
			.setDecimalSeparator('.')
			.setTextQualifier("\"")
			.setSampleBased(true)
			.setSampleBasedStartRegEx("test-regex")
			.setSampleBasedDateOffset(5)
			.setSampleBasedDateExtractionRegEx("(test)-regex-2")
			.setSampleBasedDatePattern("test-pattern")
			.setSampleBasedDataOffset(6)
			.setSampleBasedSampleSizeOffset(7);
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
		((Step2Model)controller.getModel()).setSampleBasedSampleSizeRegEx("test-regex");
		controller.loadSettings();
		assertThat(controller.isFinished(), is(false));
	}

	/**
	 * <p>shouldReturnTrueIfSampleBasedValuesAreSet.</p>
	 */
	@Test
	public void shouldReturnTrueIfSampleBasedValuesAreSet() {
		((Step2Model)controller.getModel())
    		.setColumnSeparator(",")
    		.setCommentIndicator("#")
    		.setDecimalSeparator('.')
    		.setTextQualifier("\"")
    		.setSampleBased(true)
    		.setSampleBasedStartRegEx("test-regex")
    		.setSampleBasedDateOffset(5)
    		.setSampleBasedDateExtractionRegEx("(test)-regex2-with-one-group")
    		.setSampleBasedDatePattern("date-pattern")
    		.setSampleBasedDataOffset(6)
    		.setSampleBasedSampleSizeOffset(7)
    		.setSampleBasedSampleSizeRegEx("(test-regex)-with-one-group");
		// TODO extend with other sample based parameters
		controller.loadSettings();
		assertThat(controller.isFinished(), is(true));
	}

}
