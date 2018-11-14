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
package org.n52.sos.importer.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.sos.importer.model.Step2Model;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step2ControllerTest {

    private static final String TEST_PATTERN = "test-pattern";
    private static final String TEST_REGEX_2 = "(test)-regex-2";
    private static final String TEST_REGEX = "test-regex";
    private static final String AQUOT = "\"";

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
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfDateOffsetIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfDateOffsetIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfDateExtractionRegExIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfDateExtractionRegExIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
        ((Step2Model) controller.getModel()).setSampleBasedDateExtractionRegEx(TEST_REGEX);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfDatePatternIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfDatePatternIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5)
            .setSampleBasedDateExtractionRegEx(TEST_REGEX_2);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfDataOffsetIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfDataOffsetIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5)
            .setSampleBasedDateExtractionRegEx(TEST_REGEX_2)
            .setSampleBasedDatePattern(TEST_PATTERN);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfSampleSizeOffsetIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfSampleSizeOffsetIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5)
            .setSampleBasedDateExtractionRegEx(TEST_REGEX_2)
            .setSampleBasedDatePattern(TEST_PATTERN)
            .setSampleBasedDataOffset(6);
        controller.loadSettings();
        org.junit.Assert. assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnFalseIfSampleSizeRegExIsMissing.</p>
     */
    @Test
    public void shouldReturnFalseIfSampleSizeRegExIsMissing() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5)
            .setSampleBasedDateExtractionRegEx(TEST_REGEX_2)
            .setSampleBasedDatePattern(TEST_PATTERN)
            .setSampleBasedDataOffset(6)
            .setSampleBasedSampleSizeOffset(7);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
        ((Step2Model) controller.getModel()).setSampleBasedSampleSizeRegEx(TEST_REGEX);
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(false));
    }

    /**
     * <p>shouldReturnTrueIfSampleBasedValuesAreSet.</p>
     */
    @Test
    public void shouldReturnTrueIfSampleBasedValuesAreSet() {
        ((Step2Model) controller.getModel())
            .setColumnSeparator(",")
            .setCommentIndicator("#")
            .setDecimalSeparator('.')
            .setTextQualifier(AQUOT)
            .setSampleBased(true)
            .setSampleBasedStartRegEx(TEST_REGEX)
            .setSampleBasedDateOffset(5)
            .setSampleBasedDateExtractionRegEx("(test)-regex2-with-one-group")
            .setSampleBasedDatePattern("date-pattern")
            .setSampleBasedDataOffset(6)
            .setSampleBasedSampleSizeOffset(7)
            .setSampleBasedSampleSizeRegEx("(test-regex)-with-one-group");
        // TODO extend with other sample based parameters
        controller.loadSettings();
        org.junit.Assert.assertThat(controller.isFinished(), org.hamcrest.CoreMatchers.is(true));
    }

}
