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
package org.n52.sos.importer.feeder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.Timestamp;

/**
 * Test for Issue #66: Parsing Uninx time fails
 * 
 * https://github.com/52North/sos-importer/issues/58
 * 
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Issue66ParseUnixtime {

	@Test
	public void shouldParseUnixtimeColumnContent() throws XmlException, IOException, ParseException {
		// given
		Configuration configuration = new Configuration("src/test/resources/issue-066/data_config.xml");
		DataFile dataFile = new DataFile(configuration, new File("src/test/resources/issue-066/data_obs.csv"));
		int mVColumnId = 2;
		// Thu, 09 Jun 2016 10:29:40 GMT
		String[] values = {"Wind Speed", "1465468180", "4.830000", "Kph"};
		
		// when
		final Timestamp timeStamp = dataFile.getTimeStamp(mVColumnId, values);
		
		// then
		Assert.assertThat(timeStamp, Is.is(Matchers.notNullValue()));
		Assert.assertThat(timeStamp.getYear(), Is.is((short)2016));
		Assert.assertThat(timeStamp.getMonth(), Is.is((byte)6));
		Assert.assertThat(timeStamp.getDay(), Is.is((byte)9));
		Assert.assertThat(timeStamp.getHour(), Is.is((byte)10));
		Assert.assertThat(timeStamp.getMinute(), Is.is((byte)29));
		Assert.assertThat(timeStamp.getSeconds(), Is.is((byte)40));
		Assert.assertThat(timeStamp.getTimezone(), Is.is((byte)0));
	}

}
