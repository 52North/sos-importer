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

import java.text.ParseException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimestampTest {

    // 12:01 UTZ in millis
    private static final int UTC_12_01 = 43260000;

    private static final int MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    private Timestamp timestamp;

    private final int millisPerMinute = 1000 * 60;

    private final int millisPerHour = millisPerMinute * 60;

    @Before
    public void createTimestamp() throws Exception {
        timestamp = new Timestamp();
    }

    @Test
    public void shouldSetAllValuesViaSetLong() {
        shouldSetAllValuesViaSetLongUsingTimeZone(TimeZone.getDefault());
    }

    /*
     * Test for Issue #63: Cannot build importer when host in timezone MST (-07:00)
     *
     * https://github.com/52North/sos-importer/issues/63
     */
    @Test
    public void shouldSetAllValuesViaSetLongUsingTimeZoneMST() {
        timestamp.setTimezone((byte) -7);
        shouldSetAllValuesViaSetLongUsingTimeZone(TimeZone.getTimeZone("MST"));
    }

    private void shouldSetAllValuesViaSetLongUsingTimeZone(final TimeZone tz) {
        // given
        String sign = "-";
        int rawOffset = tz.getRawOffset();
        if (rawOffset >= 0) {
            sign = "+";
        }
        final int offsetInHours = rawOffset / millisPerHour;
        final int hours = 12 + offsetInHours;
        final int minutes = (rawOffset - (offsetInHours * millisPerHour)) / millisPerMinute;
        final String minutesString = minutes < 10 ? "0" + minutes : minutes < 60 ? Integer.toString(minutes) : "00";
        final String DECIMAL = "%02d";
        final String hoursString = String.format(DECIMAL, hours);
        // why minutes+1?
        final int minutesTime = Integer.parseInt(minutesString) + 1;
        final String timeMinutesString = String.format(DECIMAL, minutesTime);
        final String offsetInHoursString = String.format(DECIMAL, Math.abs(offsetInHours));
        final String asExpected = String.format("1970-01-01T%s:%s:00.000%s%s:%s",
                hoursString,
                timeMinutesString,
                sign,
                offsetInHoursString,
                minutesString);

        // when
        timestamp.ofUnixTimeMillis(UTC_12_01);
        ZonedDateTime a = ZonedDateTime.parse(timestamp.toISO8601String());
        ZonedDateTime b = ZonedDateTime.parse(asExpected);

        // then
        Assert.assertThat(a.isEqual(b), CoreMatchers.is(true));
    }

    @Test
    public void shouldCreateInstantFromTimestamp() {
        final long time = UTC_12_01;
        final Instant instantFromTimestamp = timestamp.ofUnixTimeMillis(time).toInstant();
        final Instant instantFromSystem = Instant.ofEpochMilli(time);

        Assert.assertThat(instantFromTimestamp.compareTo(instantFromSystem), CoreMatchers.is(0));
    }

    @Test
    public final void shouldGetAdditionalTimestampValuesFromFileName()
            throws ParseException {
        final String fileName = "test-sensor_20140615.csv";
        final Timestamp ts = new Timestamp();

        ts.enrich(fileName, "test-sensor_(\\d{8})\\.csv", "yyyyMMdd");

        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is("2014-06-15"));
    }

    @Test
    public final void shouldReturnSameValueIfParametersAreInvalid()
            throws ParseException {
        Timestamp ts = new Timestamp();
        ts.enrich(null, null, null);
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));

        ts = new Timestamp();
        ts.enrich("", null, null);
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));

        ts = new Timestamp();
        ts.enrich("-", null, null);
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));

        ts = new Timestamp();
        ts.enrich("-", "", null);
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));

        ts = new Timestamp();
        ts.enrich("-", "-", null);
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));

        ts = new Timestamp();
        ts.enrich("-", "-", "");
        Assert.assertThat(ts.toISO8601String(), CoreMatchers.is(""));
    }

    @Test
    public final void shouldEnrichWithLastModificationDate() {
        final long lastModified = UTC_12_01;
        timestamp.adjustBy(lastModified, -1);
        final Timestamp expected = new Timestamp().ofUnixTimeMillis(lastModified);
        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(expected.getYear()));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(expected.getMonth()));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(expected.getDay()));
    }

    @Test
    public final void shouldEnrichWithLastModificationDateWithLastModifiedDayDelta() {
        final long lastModified = UTC_12_01;
        final int lastModifiedDelta = 2;
        timestamp.adjustBy(lastModified, lastModifiedDelta);
        final long expectedMillis = lastModified - (lastModifiedDelta * MILLIS_PER_DAY);
        final Timestamp expected = new Timestamp().ofUnixTimeMillis(expectedMillis);
        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(expected.getYear()));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(expected.getMonth()));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(expected.getDay()));
    }

    @Test
    public final void shouldEnrichWithLastModificationDateWithLastModifiedDayDeltaWithYearChange() {
        final long lastModified = 0;
        final int lastModifiedDelta = 2;
        final long expectedMillis = lastModified - (lastModifiedDelta * MILLIS_PER_DAY);
        timestamp.adjustBy(lastModified, lastModifiedDelta);
        final Timestamp expected = new Timestamp().ofUnixTimeMillis(expectedMillis);
        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(expected.getYear()));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(expected.getMonth()));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(expected.getDay()));
    }

    @Test
    public final void shouldEnrichDateInformationFromOtherTimeStamp() {
        final Timestamp other = new Timestamp().ofUnixTimeMillis(UTC_12_01);
        timestamp.ofUnixTimeMillis(0).enrich(other);

        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(other.getYear()));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(other.getMonth()));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(other.getDay()));
    }

    @Test
    public void shouldAddDayDelta() {
        timestamp.ofUnixTimeMillis(0).applyDayDelta(2);

        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(1970));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(1));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(3));

        timestamp.ofUnixTimeMillis(0).applyDayDelta(-2);

        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(1969));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(12));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is(30));
    }

    @Test
    public void shouldWorkWithoutTimezonesAddDayDelta() throws ParseException {
        timestamp.enrich(new Timestamp().enrich("1970-01-01", "(\\d{4}-\\d{2}-\\d{2})", "yyyy-MM-dd").applyDayDelta(1));

        Assert.assertThat(timestamp.getYear(), CoreMatchers.is(1970));
        Assert.assertThat(timestamp.getMonth(), CoreMatchers.is(1));
        Assert.assertThat(timestamp.getDay(), CoreMatchers.is( 2));
        Assert.assertThat(timestamp.getSeconds(), CoreMatchers.is(Integer.MIN_VALUE));
        Assert.assertThat(timestamp.getMinute(), CoreMatchers.is(Integer.MIN_VALUE));
        Assert.assertThat(timestamp.getHour(), CoreMatchers.is(Integer.MIN_VALUE));
        Assert.assertThat(timestamp.getTimezone(), CoreMatchers.is(Integer.MIN_VALUE));
    }

}
