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
package org.n52.sos.importer.feeder.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
/**
 *
 */
public class TimestampTest {

	private Timestamp timestamp;

	private final int millisPerMinute = 1000 * 60;

	private final int millisPerHour = millisPerMinute * 60;

	@Before
	public void createTimestamp() throws Exception
	{
		timestamp = new Timestamp();
	}

	@Test public void
	shouldSetAllValuesViaSetLong() {
		timestamp.set(86401000);

		final TimeZone tz = TimeZone.getDefault();
		String sign = "-";
		int rawOffset = tz.getRawOffset();
		if (rawOffset>= 0) {
			sign = "+";
		}
		rawOffset = Math.abs(rawOffset);
		final int hours = rawOffset / millisPerHour;
		final int minutes = (rawOffset - (hours * millisPerHour)) / millisPerMinute;
		final String minutesString = minutes < 10? "0"+minutes : minutes < 60? Integer.toString(minutes) : "00";
		final String hoursString = hours < 10? "0"+hours : Integer.toString(hours);

		final String asExpected = String.format("1970-01-02T01:00:01%s%s:%s",sign, hoursString, minutesString);
		assertThat(timestamp.toString(),is(asExpected));
	}

}
