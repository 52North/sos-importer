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
import org.junit.Ignore;
import org.junit.Test;
import org.n52.sos.importer.Constants.ImportStrategy;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.Step7Panel;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step7ControllerTest {

	private Step7Controller controller;

	@Before
	public void init() {
		controller = new Step7Controller();
	}

	@Test
	public void shouldSetHunkSizeInModel() {
		controller.loadSettings();
		((Step7Panel) controller.getStepPanel())
			.setHunkSize(42)
			.setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
		controller.saveSettings();
		assertThat(((Step7Model) controller.getModel()).getHunkSize(), is(42));
	}

	@Test
	public void shouldSetSendBufferInModel() {
		controller.loadSettings();
		((Step7Panel) controller.getStepPanel())
			.setSendBuffer(42)
			.setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
		controller.saveSettings();
		assertThat(((Step7Model) controller.getModel()).getSendBuffer(), is(42));
	}

	@Test
	public void shouldSetImportStrategyInModel() {
		controller.loadSettings();
		((Step7Panel) controller.getStepPanel())
			.setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
		controller.saveSettings();
		assertThat(((Step7Model) controller.getModel()).getImportStrategy(), is(ImportStrategy.SweArrayObservationWithSplitExtension));
	}

}
