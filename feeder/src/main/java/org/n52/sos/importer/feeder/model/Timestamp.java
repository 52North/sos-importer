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
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>Timestamp class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 * J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Timestamp {

    private static final String UTC = "UTC";
    private static final String SINGLE_ZERO = "0";
    private static final String DOUBLE_ZERO = SINGLE_ZERO + SINGLE_ZERO;
    private static final String PARAMETER_TIME_STAMP_IS_MANDATORY = "parameter timeStamp is mandatory.";
    private static final String SS = "ss";
    private static final String MM = "mm:";
    private static final long MILLIS_PER_HOUR = 1000 * 60 * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    private int year = Integer.MIN_VALUE;
    private int month = Integer.MIN_VALUE;
    private int day = Integer.MIN_VALUE;
    private int hour = Integer.MIN_VALUE;
    private int minute = Integer.MIN_VALUE;
    private int seconds = Integer.MIN_VALUE;
    private int millis = Integer.MIN_VALUE;
    private int timezone = Integer.MIN_VALUE;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return toISO8601String();
    }

    public String toISO8601String() {
        // yyyy-MM-ddTHH:mm:ss+hh:mm => 31 chars
        final StringBuilder ts = new StringBuilder(31);
        if (year != Integer.MIN_VALUE) {
            ts.append(year);
            if (month != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (month != Integer.MIN_VALUE) {
            ts.append(month < 10 ? SINGLE_ZERO + month : month);
            if (day != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (day != Integer.MIN_VALUE) {
            ts.append(day < 10 ? SINGLE_ZERO + day : day);
        }
        if ((year != Integer.MIN_VALUE || month != Integer.MIN_VALUE || day != Integer.MIN_VALUE)
                && (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || seconds != Integer.MIN_VALUE)) {
            ts.append("T");
        }
        if (hour != Integer.MIN_VALUE) {
            ts.append(hour < 10 ? SINGLE_ZERO + hour : hour);
            if (minute != Integer.MIN_VALUE) {
                ts.append(":");
            }
        }
        if (minute != Integer.MIN_VALUE) {
            ts.append(minute < 10 ? SINGLE_ZERO + minute : minute).append(":");
        } else if (hour != Integer.MIN_VALUE) {
            ts.append(DOUBLE_ZERO);
            ts.append(":");
        }
        if (seconds != Integer.MIN_VALUE) {
            ts.append(seconds < 10 ? SINGLE_ZERO + seconds : seconds);
        } else if (minute != Integer.MIN_VALUE && hour != Integer.MIN_VALUE) {
            ts.append(DOUBLE_ZERO);
        }
        if (millis != Integer.MIN_VALUE) {
            ts.append(".").append(millis < 10 ? DOUBLE_ZERO + millis : (millis < 100 ? SINGLE_ZERO + millis : millis));
        }
        if (timezone != Integer.MIN_VALUE &&
                (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || seconds != Integer.MIN_VALUE)) {
            if (timezone >= 0) {
                if (timezone >= 10) {
                    ts.append("+").append(timezone).append(":").append(DOUBLE_ZERO);
                } else {
                    ts.append("+0").append(timezone).append(":").append(DOUBLE_ZERO);
                }
            } else {
                if (timezone <= -10) {
                    ts.append(timezone).append(":").append(DOUBLE_ZERO);
                } else {
                    ts.append("-0").append(Math.abs(timezone)).append(":").append(DOUBLE_ZERO);
                }
            }
        }
        return ts.toString();
    }

    /**
     * <p>ofUnixTimeMillis</p>
     * @param unixTimeMillis Milliseconds since 1970-01-01T00:00:00Z.
     *
     * @return a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     */
    public Timestamp ofUnixTimeMillis(final long unixTimeMillis) {
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(Instant.ofEpochMilli(unixTimeMillis), ZoneId.of(UTC));
        year = timestamp.getYear();
        month = timestamp.getMonthValue();
        day = timestamp.getDayOfMonth();
        hour = timestamp.getHour();
        minute = timestamp.getMinute();
        seconds = timestamp.getSecond();
        millis = timestamp.getNano() / 1000;
        timezone = timestamp.getOffset().getTotalSeconds() / 3600;
        return this;
    }

    /**
     * <p>enrich with values from another timestamp.</p>
     *
     * @param other a {@link Timestamp} object.
     * @return a {@link Timestamp} object.
     */
    public Timestamp enrich(final Timestamp other) {
        if (other != null) {
            if (other.getYear() > Integer.MIN_VALUE) {
                setYear(other.getYear());
            }
            if (other.getMonth() > Integer.MIN_VALUE) {
                setMonth(other.getMonth());
            }
            if (other.getDay() > Integer.MIN_VALUE) {
                setDay(other.getDay());
            }
            if (other.getHour() > Integer.MIN_VALUE) {
                setHour(other.getHour());
            }
            if (other.getMinute() > Integer.MIN_VALUE) {
                setMinute(other.getMinute());
            }
            if (other.getSeconds() > Integer.MIN_VALUE) {
                setSeconds(other.getSeconds());
            }
            if (other.getTimezone() > Integer.MIN_VALUE) {
                setTimezone(other.getTimezone());
            }
            if (other.getMillis() > Integer.MIN_VALUE) {
                setMillis(other.getMillis());
            }
        }
        return this;
    }

    /**
     * <p>enrich.</p>
     * @param timestampInformation the filename that might contain additional information
     *          for the {@link org.n52.sos.importer.feeder.model.Timestamp}
     * @param regExToExtractFileInfo a {@link java.lang.String} object.
     * @param dateInfoPattern a {@link java.lang.String} object.
     * @throws java.text.ParseException in the case the filename could not be parsed using
     *          the Datafile attributes "regExDateInfoInFileName" and
     *          "DateInfoPattern".
     * @throws PatternSyntaxException in the case of not being able to parse the
     *          value of the Datafile attribute "regExDateInfoInFileName".
     * @throws java.lang.IndexOutOfBoundsException in the case of no group is found using
     *          the value of the Datafile attribute "regExDateInfoInFileName".
     * @return a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     */
    public Timestamp enrich(
            final String timestampInformation,
            final String regExToExtractFileInfo,
            final String dateInfoPattern)
                    throws ParseException {
        if (timestampInformation == null || timestampInformation.isEmpty() ||
                regExToExtractFileInfo == null || regExToExtractFileInfo.isEmpty() ||
                dateInfoPattern == null || dateInfoPattern.isEmpty()) {
            return this;
        }
        final Pattern pattern = Pattern.compile(regExToExtractFileInfo);
        final Matcher matcher = pattern.matcher(timestampInformation);
        if (matcher.matches() && matcher.groupCount() == 1) {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateInfoPattern);
            final String dateInformation = matcher.group(1);

            TemporalAccessor ta = dtf.parse(dateInformation);

            if (dateInfoPattern.contains("y")) {
                setYear(ta.get(ChronoField.YEAR));
            }
            if (dateInfoPattern.contains("M")) {
                setMonth(ta.get(ChronoField.MONTH_OF_YEAR));
            }
            if (dateInfoPattern.contains("d")) {
                setDay(ta.get(ChronoField.DAY_OF_MONTH));
            }
            if (dateInfoPattern.contains("H")) {
                setHour(ta.get(ChronoField.HOUR_OF_DAY));
            }
            if (dateInfoPattern.contains("m")) {
                setMinute(ta.get(ChronoField.MINUTE_OF_HOUR));
            }
            if (dateInfoPattern.contains("s")) {
                setSeconds(ta.get(ChronoField.SECOND_OF_MINUTE));
            }
            if (dateInfoPattern.contains("S")) {
                setMillis(ta.get(ChronoField.MILLI_OF_SECOND));
            }
            if (dateInfoPattern.contains("Z")) {
                setTimezone(ta.get(ChronoField.OFFSET_SECONDS) / 3600);
            }
        }
        return this;
    }

    /**
     * <p>enrich.</p>
     * @param lastModified long
     * @param lastModifiedDeltaDays -1, if it should be ignored, else &gt; 0.
     * @return a {@link Timestamp} object.
     */
    public Timestamp adjustBy(
            final long lastModified,
            final int lastModifiedDeltaDays) {
        long lastModifiedTmp = lastModified;
        if (lastModifiedDeltaDays > 0) {
            lastModifiedTmp = lastModified - (lastModifiedDeltaDays * MILLIS_PER_DAY);
        }
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastModifiedTmp), ZoneId.of(UTC));
        setYear(zdt.get(ChronoField.YEAR));
        setMonth(zdt.get(ChronoField.MONTH_OF_YEAR));
        setDay(zdt.get(ChronoField.DAY_OF_MONTH));
        return this;
    }

    /**
     * @return Instant of the current {@link Timestamp} object.
     */
    protected Instant toInstant() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(getDatePattern());
        String iso8601String = toISO8601String();
        try {
            return dtf.parse(iso8601String, ZonedDateTime::from).toInstant();
        } catch (DateTimeException e) {
            //
        }
        try {
            return dtf.parse(iso8601String, OffsetDateTime::from).toInstant();
        } catch (DateTimeException e) {
            //
        }
        try {
            return dtf.parse(iso8601String, LocalDateTime::from).toInstant(ZoneOffset.of(UTC));
        } catch (DateTimeException e) {
            //
        }
        try {
            return Instant.ofEpochMilli(dtf.parse(iso8601String, LocalDate::from).toEpochDay() * MILLIS_PER_DAY);
        } catch (DateTimeException e) {
            //
        }
        return Instant.ofEpochMilli(0);
    }

    private String getDatePattern() {
        // "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        final StringBuffer ts = new StringBuffer(31);
        if (year != Integer.MIN_VALUE) {
            ts.append("yyyy");
            if (month != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (month != Integer.MIN_VALUE) {
            ts.append("MM");
            if (day != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (day != Integer.MIN_VALUE) {
            ts.append("dd");
        }
        if ((year != Integer.MIN_VALUE || month != Integer.MIN_VALUE || day != Integer.MIN_VALUE)
                && (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || seconds != Integer.MIN_VALUE)) {
            ts.append("'T'");
        }
        if (hour != Integer.MIN_VALUE) {
            ts.append("HH");
            if (minute != Integer.MIN_VALUE) {
                ts.append(":");
            }
        }
        if (minute != Integer.MIN_VALUE) {
            ts.append(MM);
        } else if (hour != Integer.MIN_VALUE) {
            ts.append(MM);
        }
        if (seconds != Integer.MIN_VALUE) {
            ts.append(SS);
        } else if (minute != Integer.MIN_VALUE && hour != Integer.MIN_VALUE) {
            ts.append(SS);
        }
        if (millis != Integer.MIN_VALUE) {
            ts.append(".SSS");
        }
        if (timezone != Integer.MIN_VALUE &&
                (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || seconds != Integer.MIN_VALUE)) {
            ts.append("XXX");
        }
        final String datePattern = ts.toString();
        if (datePattern.isEmpty()) {
            return "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        }
        return datePattern;
    }

    /**
     * <p>after.</p>
     * @param timeStamp a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     * @return a boolean.
     */
    public boolean isAfter(final Timestamp timeStamp) {
        if (timeStamp == null) {
            throw new IllegalArgumentException(PARAMETER_TIME_STAMP_IS_MANDATORY);
        }
        return toInstant().isAfter(timeStamp.toInstant());
    }

    /**
     * <p>before.</p>
     * @param timeStamp a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     * @return a boolean.
     */
    public boolean isBefore(final Timestamp timeStamp) {
        if (timeStamp == null) {
            throw new IllegalArgumentException(PARAMETER_TIME_STAMP_IS_MANDATORY);
        }
        return toInstant().isBefore(timeStamp.toInstant());
    }

    /**
     * <p>applyDayDelta.</p>
     * @param daysToAdd a int.
     * @return a {@link Timestamp} object.
     */
    public Timestamp applyDayDelta(final int daysToAdd) {
        TemporalAccessor ta = DateTimeFormatter.ofPattern(getDatePattern()).parse(toISO8601String());
        if (ta.isSupported(ChronoField.YEAR) &&
                ta.isSupported(ChronoField.MONTH_OF_YEAR) &&
                ta.isSupported(ChronoField.DAY_OF_MONTH)) {
            LocalDate ld = LocalDate.of(
                    ta.get(ChronoField.YEAR),
                    ta.get(ChronoField.MONTH_OF_YEAR),
                    ta.get(ChronoField.DAY_OF_MONTH));
            ld = ld.plusDays(daysToAdd);
            setYear(ld.getYear());
            setMonth(ld.getMonthValue());
            setDay(ld.getDayOfMonth());
        }
        return this;
    }

    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a short.
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /**
     * <p>Setter for the field <code>month</code>.</p>
     *
     * @param month a byte.
     */
    public void setMonth(final int month) {
        this.month = month;
    }

    /**
     * <p>Setter for the field <code>day</code>.</p>
     *
     * @param day a byte.
     */
    public void setDay(final int day) {
        this.day = day;
    }

    /**
     * <p>Setter for the field <code>hour</code>.</p>
     *
     * @param hour a byte.
     */
    public void setHour(final int hour) {
        this.hour = hour;
    }

    /**
     * <p>Setter for the field <code>minute</code>.</p>
     *
     * @param minute a byte.
     */
    public void setMinute(final int minute) {
        this.minute = minute;
    }

    /**
     * <p>Setter for the field <code>seconds</code>.</p>
     *
     * @param seconds a byte.
     */
    public void setSeconds(final int seconds) {
        this.seconds = seconds;
    }

    /**
     * <p>Setter for the field <code>timezone</code>.</p>
     *
     * @param timezone a byte.
     */
    public void setTimezone(final int timezone) {
        this.timezone = timezone;
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return a short.
     */
    public int getYear() {
        return year;
    }

    /**
     * <p>Getter for the field <code>month</code>.</p>
     *
     * @return a byte.
     */
    public int getMonth() {
        return month;
    }

    /**
     * <p>Getter for the field <code>day</code>.</p>
     *
     * @return a byte.
     */
    public int getDay() {
        return day;
    }

    /**
     * <p>Getter for the field <code>hour</code>.</p>
     *
     * @return a byte.
     */
    public int getHour() {
        return hour;
    }

    /**
     * <p>Getter for the field <code>minute</code>.</p>
     *
     * @return a byte.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * <p>Getter for the field <code>seconds</code>.</p>
     *
     * @return a byte.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * <p>Getter for the field <code>timezone</code>.</p>
     *
     * @return a byte.
     */
    public int getTimezone() {
        return timezone;
    }

    private void setMillis(int millis) {
        this.millis = millis;
    }

    private int getMillis() {
        return millis;
    }

}
