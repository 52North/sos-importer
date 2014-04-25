/**
 * Copyright (C) 2013
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
package org.n52.sos.importer.model.xml;

import static java.lang.Boolean.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.model.Step7Model;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step7ModelHandlerTest {

	@Test
	public void should_add_binding_if_set_in_model()
	{
		final String binding = "test-binding";
		final Step7Model stepModel = new Step7Model(null, null, false, null, null, binding);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getSosMetadata().isSetBinding(), is(TRUE));
		assertThat(importConf.getSosMetadata().getBinding(), is(binding));
	}

	@Test
	public void should_not_add_binding_if_empty_or_null()
	{
		final Step7Model stepModelEmpty = new Step7Model(null, null, false, null, null, "");
		final SosImportConfiguration importConfEmpty = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModelEmpty, importConfEmpty);

		final Step7Model stepModelNull = new Step7Model(null, null, false, null, null, null);
		final SosImportConfiguration importConfNull = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModelNull, importConfNull);

		assertThat(importConfEmpty.getSosMetadata().isSetBinding(), is(FALSE));
		assertThat(importConfNull.getSosMetadata().isSetBinding(), is(FALSE));
	}

	@Test
	public void should_add_version_if_set_in_model()
	{
		final String version = "test-version";
		final Step7Model stepModel = new Step7Model(null, null, false, null, version, null);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModel, importConf);

		assertThat(importConf.getSosMetadata().getVersion(), is(version));
	}

	@Test
	public void should_not_add_version_if_empty_or_null()
	{
		final Step7Model stepModelEmpty = new Step7Model(null, null, false, null, "",null);
		final SosImportConfiguration importConfEmpty = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModelEmpty, importConfEmpty);

		final Step7Model stepModelNull = new Step7Model(null, null, false, null, null, null);
		final SosImportConfiguration importConfNull = SosImportConfiguration.Factory.newInstance();
		new Step7ModelHandler().handleModel(stepModelNull, importConfNull);

		assertThat(importConfEmpty.getSosMetadata().getVersion(), is(nullValue()));
		assertThat(importConfNull.getSosMetadata().getVersion(), is(nullValue()));
	}

}
