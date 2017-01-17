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
import org.n52.sos.importer.Constants;

/**
 * <p>Step7PanelTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 * @since 0.5.0
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step7PanelTest {

	private static final String POX = "POX";
	private static final String V_20 = "2.0.0";
	private static final String V_10 = "1.0.0";
	private Step7Panel panel;

	/**
	 * <p>init.</p>
	 */
	@Before
	public void init() {
		panel = new Step7Panel();
	}

	/**
	 * <p>shouldReturnSosVersion100AsDefault.</p>
	 */
	@Test
	public void shouldReturnSosVersion100AsDefault() {
		assertThat(panel.getSosVersion(), is(V_10));
	}

	/**
	 * <p>shouldReturnSosVersion.</p>
	 */
	@Test
	public void shouldReturnSosVersion() {
		panel.setSosVersion(V_20);
		assertThat(panel.getSosVersion(), is(V_20));
	}

	/**
	 * <p>shouldReturnBindingPOXAsDefault.</p>
	 */
	@Test
	public void shouldReturnBindingPOXAsDefault() {
		assertThat(panel.getSosBinding(), is(POX));
	}

	/**
	 * <p>shouldReturnBinding.</p>
	 */
	@Test
	public void shouldReturnBinding() {
		panel.setBinding(POX);
		assertThat(panel.getSosBinding(), is(POX));
	}

	/**
	 * <p>shouldReturnImportStrategySingleObservationByDefault.</p>
	 */
	@Test
	public void shouldReturnImportStrategySingleObservationByDefault() {
		assertThat(panel.getImportStrategy(), is(Constants.ImportStrategy.SingleObservation));
	}

	/**
	 * <p>shouldReturnImportStrategySweArrayIfSet.</p>
	 */
	@Test
	public void shouldReturnImportStrategySweArrayIfSet() {
		panel.setImportStrategy(Constants.ImportStrategy.SweArrayObservationWithSplitExtension);
		assertThat(panel.getImportStrategy(), is(Constants.ImportStrategy.SweArrayObservationWithSplitExtension));
	}

	/**
	 * <p>shouldReturn5000AsDefaultHunksize.</p>
	 */
	@Test
	public void shouldReturn5000AsDefaultHunksize() {
		assertThat(panel.getHunkSize(), is(5000));
	}

	/**
	 * <p>shouldReturnHunksize.</p>
	 */
	@Test
	public void shouldReturnHunksize() {
		final int hunkSize = 2500;
		panel.setHunkSize(hunkSize);
		assertThat(panel.getHunkSize(), is(hunkSize));
	}

	/**
	 * <p>shouldReturn25AsDefaultSweArraySendBuffer.</p>
	 */
	@Test
	public void shouldReturn25AsDefaultSweArraySendBuffer() {
		assertThat(panel.getSendBuffer(), is(25));
	}

	/**
	 * <p>shouldReturnSweArraySendBuffe.</p>
	 */
	@Test
	public void shouldReturnSweArraySendBuffe() {
		final int buffer = 42;
		panel.setSendBuffer(buffer);
		assertThat(panel.getSendBuffer(), is(buffer));
	}
}
