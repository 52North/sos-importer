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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>Timestamp class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * TODO switch to Joda time or java 8 to get time zone problems fixed
 * @version $Id: $Id
 */
public class Timestamp {

    private static final int millisPerHour = 1000 * 60 * 60;
    private static final int millisPerDay = millisPerHour * 24;
    private short year = Short.MIN_VALUE;
    private byte month = Byte.MIN_VALUE;
    private byte day = Byte.MIN_VALUE;
    private byte hour = Byte.MIN_VALUE;
    private byte minute = Byte.MIN_VALUE;
    private byte seconds = Byte.MIN_VALUE;
    private byte timezone = Byte.MIN_VALUE;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        // yyyy-MM-ddTHH:mm:ss+hh:mm => 31 chars
        final StringBuffer ts = new StringBuffer(31);
        if (year != Short.MIN_VALUE) {
            ts.append(year);
            if (month != Byte.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (month != Byte.MIN_VALUE) {
            ts.append(month<10?"0"+month:month);
            if (day != Byte.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (day != Byte.MIN_VALUE) {
            ts.append(day<10?"0"+day:day);
        }
        if ( (year != Short.MIN_VALUE || month != Byte.MIN_VALUE || day != Byte.MIN_VALUE )
                && (hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
            ts.append("T");
        }
        if (hour != Byte.MIN_VALUE) {
            ts.append(hour<10?"0"+hour:hour);
            if (minute != Byte.MIN_VALUE) {
                ts.append(":");
            }
        }
        if (minute != Byte.MIN_VALUE) {
            ts.append( (minute<10?"0"+minute:minute)+":");
        } else if (hour != Byte.MIN_VALUE) {
            ts.append("00:");
        }
        if (seconds != Byte.MIN_VALUE ) {
            ts.append(seconds<10?"0"+seconds:seconds);
        } else if (minute != Byte.MIN_VALUE && hour != Byte.MIN_VALUE) {
            ts.append("00");
        }
        if (timezone != Byte.MIN_VALUE &&
                (hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
            ts.append(convertTimeZone(timezone));
        }
        return ts.toString();
    }

    private String convertTimeZone(final int timeZone) {
        if (timeZone >= 0) {
            if (timeZone >= 10) {
                return "+" + timeZone + ":00";
            } else {
                return "+0" + timeZone + ":00";
            }
        } else {
            if (timeZone <= -10) {
                return timeZone + ":00";
            } else {
                return "-0" + Math.abs(timeZone) + ":00";
            }
        }
    }

    /**
     * <p>set.</p>
     *
     * @param dateToSet a long.
     * @return a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     */
    public Timestamp set(final long dateToSet) {
        final Calendar cal = new GregorianCalendar();
        if (timezone != Byte.MIN_VALUE) {
            cal.setTimeZone(TimeZone.getTimeZone(TimeZone.getAvailableIDs(timezone*millisPerHour)[0]));
        }
        cal.setTimeInMillis(dateToSet);
        year = (short) cal.get(Calendar.YEAR);
        month = (byte) (cal.get(Calendar.MONTH)+1);
        day = (byte) cal.get(Calendar.DAY_OF_MONTH);
        hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
        minute = (byte) cal.get(Calendar.MINUTE);
        seconds = (byte) cal.get(Calendar.SECOND);
        timezone = (byte) (cal.getTimeZone().getOffset(dateToSet)/millisPerHour);
        return this;
    }

    /**
     * <p>enrich.</p>
     *
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
            final SimpleDateFormat sdf = new SimpleDateFormat(dateInfoPattern);
            final String dateInformation = matcher.group(1);
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(sdf.parse(dateInformation));

            if (dateInfoPattern.indexOf("y") > -1) {
                setYear(Short.parseShort(Integer.toString(cal.get(GregorianCalendar.YEAR))));
            }
            if (dateInfoPattern.indexOf("M") > -1) {
                setMonth(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MONTH)+1)));
            }
            if (dateInfoPattern.indexOf("d") > -1) {
                setDay(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.DAY_OF_MONTH))));
            }
            if (dateInfoPattern.indexOf("H") > -1) {
                setHour(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.HOUR_OF_DAY))));
            }
            if (dateInfoPattern.indexOf("m") > -1) {
                setMinute(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MINUTE))));
            }
            if (dateInfoPattern.indexOf("s") > -1) {
                setSeconds(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.SECOND))));
            }
            if (dateInfoPattern.indexOf("z") > -1 || dateInfoPattern.indexOf("Z") > -1) {
                setTimezone(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.ZONE_OFFSET))));
            }
        }
        return this;
    }

    /**
     * <p>toDate.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    protected Date toDate() {
        final String datePattern = getDatePattern();
        try {
            return new SimpleDateFormat(datePattern).parse(toString());
        } catch (final ParseException e) {
            throw new RuntimeException("Could not execute toDate()",e);
        }
    }

    private String getDatePattern() {
        // "yyyy-MM-dd'T'HH:mm:ssX";
        final StringBuffer ts = new StringBuffer(31);
        if (year != Short.MIN_VALUE) {
            ts.append("yyyy");
            if (month != Byte.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (month != Byte.MIN_VALUE) {
            ts.append("MM");
            if (day != Byte.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (day != Byte.MIN_VALUE) {
            ts.append("dd");
        }
        if ( (year != Short.MIN_VALUE || month != Byte.MIN_VALUE || day != Byte.MIN_VALUE )
                && (hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
            ts.append("'T'");
        }
        if (hour != Byte.MIN_VALUE) {
            ts.append("HH");
            if (minute != Byte.MIN_VALUE) {
                ts.append(":");
            }
        }
        if (minute != Byte.MIN_VALUE) {
            ts.append("mm:");
        } else if (hour != Byte.MIN_VALUE) {
            ts.append("mm:");
        }
        if (seconds != Byte.MIN_VALUE ) {
            ts.append("ss");
        } else if (minute != Byte.MIN_VALUE && hour != Byte.MIN_VALUE) {
            ts.append("ss");
        }
        if (timezone != Byte.MIN_VALUE &&
                (hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
            ts.append("X");
        }
        final String datePattern = ts.toString();
        if (datePattern.isEmpty()) {
            return "yyyy-MM-dd'T'HH:mm:ssX";
        }
        return datePattern;
    }

    /**
     * <p>after.</p>
     *
     * @param timeStamp a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     * @return a boolean.
     */
    public boolean after(final Timestamp timeStamp) {
        if (timeStamp == null) {
            throw new IllegalArgumentException("parameter timeStamp is mandatory.");
        }
        return toDate().after(timeStamp.toDate());
    }

    /**
     * <p>before.</p>
     *
     * @param timeStamp a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     * @return a boolean.
     */
    public boolean before(final Timestamp timeStamp) {
        if (timeStamp == null) {
            throw new IllegalArgumentException("parameter timeStamp is mandatory.");
        }
        return toDate().before(timeStamp.toDate());
    }

    /**
     * <p>enrich.</p>
     *
     * @param lastModified long
     * @param lastModifiedDelta -1, if it should be ignored, else &gt; 0.
     * @param lastModifiedDelta -1, if it should be ignored, else &gt; 0.
     * @return a {@link Timestamp} object.
     */
    public Timestamp enrich(long lastModified,
            final int lastModifiedDelta) {
        final GregorianCalendar cal = new GregorianCalendar();
        if (lastModifiedDelta > 0) {
            lastModified = lastModified - (lastModifiedDelta * millisPerDay);
        }
        cal.setTime(new Date(lastModified));
        setYear(Short.parseShort(Integer.toString(cal.get(GregorianCalendar.YEAR))));
        setMonth(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MONTH)+1)));
        setDay(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.DAY_OF_MONTH))));
        return this;
    }

    /**
     * <p>applyDayDelta.</p>
     *
     * @param daysToAdd a int.
     * @return a {@link Timestamp} object.
     */
    public Timestamp applyDayDelta(final int daysToAdd) {
        final Timestamp tmp = new Timestamp().set(toDate().getTime() + (daysToAdd * millisPerDay));
        setYear(tmp.getYear());
        setMonth(tmp.getMonth());
        setDay(tmp.getDay());
        return this;
    }

    /**
     * <p>enrich.</p>
     *
     * @param other a {@link Timestamp} object.
     * @return a {@link Timestamp} object.
     */
    public Timestamp enrich(final Timestamp other) {
        if (other != null) {
            if (other.getYear() > Short.MIN_VALUE) {
                setYear(other.getYear());
            }
            if (other.getMonth() > Byte.MIN_VALUE) {
                setMonth(other.getMonth());
            }
            if (other.getDay() > Byte.MIN_VALUE) {
                setDay(other.getDay());
            }
            if (other.getHour() > Byte.MIN_VALUE) {
                setHour(other.getHour());
            }
            if (other.getMinute() > Byte.MIN_VALUE) {
                setMinute(other.getMinute());
            }
            if (other.getSeconds() > Byte.MIN_VALUE) {
                setSeconds(other.getSeconds());
            }
            if (other.getTimezone() > Byte.MIN_VALUE) {
                setTimezone(other.getTimezone());
            }
        }
        return this;
    }

    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a short.
     */
    public void setYear(final short year) {
        this.year = year;
    }

    /**
     * <p>Setter for the field <code>month</code>.</p>
     *
     * @param month a byte.
     */
    public void setMonth(final byte month) {
        this.month = month;
    }

    /**
     * <p>Setter for the field <code>day</code>.</p>
     *
     * @param day a byte.
     */
    public void setDay(final byte day) {
        this.day = day;
    }

    /**
     * <p>Setter for the field <code>hour</code>.</p>
     *
     * @param hour a byte.
     */
    public void setHour(final byte hour) {
        this.hour = hour;
    }

    /**
     * <p>Setter for the field <code>minute</code>.</p>
     *
     * @param minute a byte.
     */
    public void setMinute(final byte minute) {
        this.minute = minute;
    }

    /**
     * <p>Setter for the field <code>seconds</code>.</p>
     *
     * @param seconds a byte.
     */
    public void setSeconds(final byte seconds) {
        this.seconds = seconds;
    }

    /**
     * <p>Setter for the field <code>timezone</code>.</p>
     *
     * @param timezone a byte.
     */
    public void setTimezone(final byte timezone) {
        this.timezone = timezone;
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return a short.
     */
    public short getYear() {
        return year;
    }

    /**
     * <p>Getter for the field <code>month</code>.</p>
     *
     * @return a byte.
     */
    public byte getMonth() {
        return month;
    }

    /**
     * <p>Getter for the field <code>day</code>.</p>
     *
     * @return a byte.
     */
    public byte getDay() {
        return day;
    }

    /**
     * <p>Getter for the field <code>hour</code>.</p>
     *
     * @return a byte.
     */
    public byte getHour() {
        return hour;
    }

    /**
     * <p>Getter for the field <code>minute</code>.</p>
     *
     * @return a byte.
     */
    public byte getMinute() {
        return minute;
    }

    /**
     * <p>Getter for the field <code>seconds</code>.</p>
     *
     * @return a byte.
     */
    public byte getSeconds() {
        return seconds;
    }

    /**
     * <p>Getter for the field <code>timezone</code>.</p>
     *
     * @return a byte.
     */
    public byte getTimezone() {
        return timezone;
    }

}
