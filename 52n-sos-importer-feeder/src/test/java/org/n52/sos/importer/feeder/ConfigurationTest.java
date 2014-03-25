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
package org.n52.sos.importer.feeder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class ConfigurationTest {

	private SosImportConfiguration importConf;

	@Before
	public final void initImportConf() {
		importConf = SosImportConfiguration.Factory.newInstance();
		importConf.addNewDataFile();
	}

	@Test
	public final void shouldGetAdditionalTimestampValuesFromDatafileName() throws ParseException {
		final String fileName = "test-sensor_20140615.csv";
		final Timestamp ts = new Timestamp();
		importConf.getDataFile().setRegExDateInfoInFileName("test-sensor_(\\d{8})\\.csv");
		importConf.getDataFile().setDateInfoPattern("yyyyMMdd");
		final Configuration configuration = new Configuration(importConf);

		final Timestamp updatedTS = configuration.enrichTimestampByFilename(ts, fileName);

		assertThat(ts, is(updatedTS));
		assertThat(updatedTS.toString(), is("2014-06-15"));
	}

	@Test
	public final void shouldReturnSameValueIfTimestampIsNullOrFilenameIsInvalid() throws ParseException {
		Timestamp ts = new Timestamp();
		final Configuration configuration = new Configuration(importConf);
		ts = configuration.enrichTimestampByFilename(ts, null);
		final Timestamp ts2 = configuration.enrichTimestampByFilename(ts, "");
		final Timestamp ts3 = configuration.enrichTimestampByFilename(null, null);

		assertThat(ts.toString(), is(""));
		assertThat(ts2.toString(), is(""));
		assertThat(ts3, is(nullValue()));
	}

}
