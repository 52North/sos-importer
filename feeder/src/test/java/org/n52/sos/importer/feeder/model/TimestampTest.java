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
package org.n52.sos.importer.feeder.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class TimestampTest {

	private static final int millisPerDay = 1000 * 60 * 60 * 24;

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
		final int minutesTime = Integer.parseInt(minutesString)+1;
		final String timeMinutesString = minutesTime < 10? "0"+minutesTime : Integer.toString(minutesTime);
		final String asExpected = String.format("1970-01-02T%s:00:%s%s%s:%s",
				hoursString,
				timeMinutesString,
				sign, hoursString, minutesString);

		assertThat(timestamp.toString(),is(asExpected));
	}

	@Test public void
	shouldCreateDateFromTimestamp() {
		final long time = getCurrentTimeMillisTimestampCompatible();
		final Date dateFromTimestamp = timestamp.set(time).toDate();
		final Date dateFromSystem = new Date(time);
		assertThat(dateFromTimestamp.compareTo(dateFromSystem), is(0));

	}

	@Test public final void
	shouldGetAdditionalTimestampValuesFromFileName()
			throws ParseException {
		final String fileName = "test-sensor_20140615.csv";
		final Timestamp ts = new Timestamp();

		ts.enrich(fileName,"test-sensor_(\\d{8})\\.csv","yyyyMMdd");

		assertThat(ts.toString(), is("2014-06-15"));
	}

	@Test public final void
	shouldReturnSameValueIfParametersAreInvalid()
			throws ParseException {
		Timestamp ts = new Timestamp();
		ts.enrich(null, null, null);
		assertThat(ts.toString(), is(""));

		ts = new Timestamp();
		ts.enrich("", null, null);
		assertThat(ts.toString(), is(""));

		ts = new Timestamp();
		ts.enrich("-", null, null);
		assertThat(ts.toString(), is(""));

		ts = new Timestamp();
		ts.enrich("-", "", null);
		assertThat(ts.toString(), is(""));

		ts = new Timestamp();
		ts.enrich("-", "-", null);
		assertThat(ts.toString(), is(""));

		ts = new Timestamp();
		ts.enrich("-", "-", "");
		assertThat(ts.toString(), is(""));
	}

	@Test public final void
	shouldEnrichWithLastModificationDate() {
		final long lastModified = getCurrentTimeMillisTimestampCompatible();
		timestamp.enrich(lastModified, -1);
		final Timestamp expected = new Timestamp().set(lastModified);
		assertThat(timestamp.getYear(), is(expected.getYear()));
		assertThat(timestamp.getMonth(), is(expected.getMonth()));
		assertThat(timestamp.getDay(), is(expected.getDay()));
	}

	@Test public final void
	shouldEnrichWithLastModificationDateWithLastModifiedDayDelta() {
		final long lastModified = getCurrentTimeMillisTimestampCompatible();
		final int lastModifiedDelta = 2;
		timestamp.enrich(lastModified, lastModifiedDelta);
		final long expectedMillis = lastModified - (lastModifiedDelta * millisPerDay);
		final Timestamp expected = new Timestamp().set(expectedMillis);
		assertThat(timestamp.getYear(), is(expected.getYear()));
		assertThat(timestamp.getMonth(), is(expected.getMonth()));
		assertThat(timestamp.getDay(), is(expected.getDay()));
	}

	@Test public final void
	shouldEnrichWithLastModificationDateWithLastModifiedDayDeltaWithYearChange() {
		final long lastModified = 0;
		final int lastModifiedDelta = 2;
		final long expectedMillis = lastModified - (lastModifiedDelta * millisPerDay);
		timestamp.enrich(lastModified, lastModifiedDelta);
		final Timestamp expected = new Timestamp().set(expectedMillis);
		assertThat(timestamp.getYear(), is(expected.getYear()));
		assertThat(timestamp.getMonth(), is(expected.getMonth()));
		assertThat(timestamp.getDay(), is(expected.getDay()));
	}

	@Test public final void
	shouldEnrichDateInformationFromOtherTimeStamp() {
		final Timestamp other = new Timestamp().set(getCurrentTimeMillisTimestampCompatible());
		timestamp.set(0).enrich(other);

		assertThat(timestamp.getYear(), is(other.getYear()));
		assertThat(timestamp.getMonth(), is(other.getMonth()));
		assertThat(timestamp.getDay(), is(other.getDay()));
	}

	@Test public void
	shouldAddDayDelta() {
		timestamp.set(0).applyDayDelta(2);

		assertThat(timestamp.getYear(), is((short)1970));
		assertThat(timestamp.getMonth(), is((byte)1));
		assertThat(timestamp.getDay(), is((byte)3));

		timestamp.set(0).applyDayDelta(-2);

		assertThat(timestamp.getYear(), is((short)1969));
		assertThat(timestamp.getMonth(), is((byte)12));
		assertThat(timestamp.getDay(), is((byte)30));
	}

	@Test public void
	shouldWorkWithoutTimezonesAddDayDelta() throws ParseException {
		timestamp.enrich(new Timestamp().enrich("1970-01-01", "(\\d{4}-\\d{2}-\\d{2})", "yyyy-MM-dd").applyDayDelta(1));

		assertThat(timestamp.getYear(), is((short)1970));
		assertThat(timestamp.getMonth(), is((byte)1));
		assertThat(timestamp.getDay(), is((byte)2));
		assertThat(timestamp.getSeconds(), is(Byte.MIN_VALUE));
		assertThat(timestamp.getMinute(), is(Byte.MIN_VALUE));
		assertThat(timestamp.getHour(), is(Byte.MIN_VALUE));
		assertThat(timestamp.getTimezone(), is(Byte.MIN_VALUE));
	}

	private long getCurrentTimeMillisTimestampCompatible() {
		// Timestamp is not storing milliseconds now => remove them
		return (System.currentTimeMillis() / 1000) * 1000;
	}
}
