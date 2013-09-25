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
package org.n52.sos.importer.feeder.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.feeder.util.FileHelper;

public class FileHelperTest {
	
	@Test
	public void shouldShortenALongString()
	{
		// Don't change this value or the test breaks!
		final String longString = "This is a very long string that should be shortened to a shorter one";
		final String shorterString = FileHelper.shortenStringViaMD5Hash(longString);
		assertThat(shorterString.length(),is(32));
		// Don't change this value or the test breaks!
		assertThat(shorterString,is("c42073bd88ccb323a36b57092093b139"));
	}

}
