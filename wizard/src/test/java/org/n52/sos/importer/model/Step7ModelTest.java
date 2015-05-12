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

import org.junit.Before;
import org.junit.Test;
import org.n52.sos.importer.Constants.ImportStrategy;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step7ModelTest {

	private Step7Model model;

	@Before
	public void init() {
		model = new Step7Model();
	}

	@Test
	public void shouldReturnSingleObservationAsDefaultImportStrategie() {
		assertThat(model.getImportStrategy(), is(ImportStrategy.SingleObservation));
	}

	@Test
	public void shouldReturnImportStrategie() {
		model.setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
		assertThat(model.getImportStrategy(), is(ImportStrategy.SweArrayObservationWithSplitExtension));
	}

	@Test
	public void shouldReturn25AsDefaultSendBuffer() {
		assertThat(model.getSendBuffer(), is(25));
	}

	@Test
	public void shouldReturnSendBuffer() {
		model.setSendBuffer(42);
		assertThat(model.getSendBuffer(), is(42));
	}

	@Test
	public void shouldReturn5000AsDefaultHunkSize() {
		assertThat(model.getHunkSize(), is(5000));
	}

	@Test
	public void shouldReturnHunkSize() {
		model.setHunkSize(52);
		assertThat(model.getHunkSize(), is(52));
	}
}
