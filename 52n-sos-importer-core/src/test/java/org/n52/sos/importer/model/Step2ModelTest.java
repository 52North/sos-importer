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

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2ModelTest {

	private Step2Model model;

	@Before
	public void init() {
		model = new Step2Model("", 0);
	}

	@Test
	public void shouldReturnTrueIfIsSampleBasedIsSet() {
		assertThat(model.setSampleBased(true).isSampleBased(), is(true));
	}

	@Test
	public void shouldReturnFalseAsDefaultValueForSampleBased() {
		assertThat(model.isSampleBased(), is(false));
	}

	@Test
	public void shouldReturnStringIfIsSampleBasedStartRegExIsSet() {
		final String sampleBasedStartRegEx = "test-regex";
		assertThat(model.setSampleBasedStartRegEx(sampleBasedStartRegEx).getSampleBasedStartRegEx(), is(sampleBasedStartRegEx));
	}

	@Test
	public void shouldReturnEmptyStringAsDefaultValueForSampleBasedStartRegEx() {
		assertThat(model.getSampleBasedStartRegEx(), is(""));
	}

	@Test
	public void shouldReturnValueIfIsSampleBasedDateOffsetIsSet() {
		final int dateOffset = 25;
		assertThat(model.setSampleBasedDateOffset(dateOffset).getSampleBasedDateOffset(), is(dateOffset));
	}

	@Test
	public void shouldReturnZeroAsDefaultValueForSampleBasedDateOffset() {
		assertThat(model.getSampleBasedDateOffset(), is(0));
	}
}
