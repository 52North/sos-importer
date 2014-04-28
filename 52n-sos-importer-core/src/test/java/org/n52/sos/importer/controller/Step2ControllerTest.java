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
package org.n52.sos.importer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.n52.sos.importer.model.Step2Model;

/*
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2ControllerTest {

	private Step2Controller controller;

	@Before
	public void init() {
		controller = new Step2Controller(new Step2Model("", 1));
	}

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
    		.setSampleBasedSampleSizeOffset(7);
		// TODO extend with other sample based parameters
		controller.loadSettings();
		assertThat(controller.isFinished(), is(true));
	}

}
