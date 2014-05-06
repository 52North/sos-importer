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
