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
package org.n52.sos.importer.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.dateAndTime.Year;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles operations on DateAndTime objects
 *
 * @author Raimund
 */
public class DateAndTimeController {

    private static final Logger LOG = LoggerFactory.getLogger(DateAndTimeController.class);

    private static final String NULL = "null";

    private DateAndTime dateAndTime;

    private final List<MissingComponentPanel> missingComponentPanels;

    /**
     * <p>Constructor for DateAndTimeController.</p>
     */
    public DateAndTimeController() {
        dateAndTime = new DateAndTime();
        missingComponentPanels = new ArrayList<>();
    }

    /**
     * <p>Constructor for DateAndTimeController.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public DateAndTimeController(final DateAndTime dateAndTime) {
        this.dateAndTime = dateAndTime;
        missingComponentPanels = new ArrayList<>();
    }

    /**
     * <p>Getter for the field <code>missingComponentPanels</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<MissingComponentPanel> getMissingComponentPanels() {
        if (!missingComponentPanels.isEmpty()) {
            return missingComponentPanels;
        }
        /*
         *  DATE SECTION
         *
         * whole date component is missing
         */
        /* Disabled because the user might enter observations on year or month level without giving a date
        if (dateAndTime.getDay() == null &&
                dateAndTime.getMonth() == null &&
                dateAndTime.getYear() == null) {
            missingComponentPanels.add(new MissingDatePanel(dateAndTime));
        } else {
            // not all elements of date are missing
            if (dateAndTime.getDay() == null) {
                missingComponentPanels.add(new MissingDayPanel(dateAndTime));
            }
            if (dateAndTime.getMonth() == null) {
                missingComponentPanels.add(new MissingMonthPanel(dateAndTime));
            }
            if (dateAndTime.getYear() == null) {
                missingComponentPanels.add(new MissingYearPanel(dateAndTime));
            }
        }
        */
        /*
         *  TIME SECTION
         *
         * whole time is missing
         */
        /*
         * Disabled because the user might enter observations on year,
         * month, or day level without giving a specific time
        if (dateAndTime.getHour() == null &&
                dateAndTime.getMinute() == null &&
                dateAndTime.getSeconds() == null) {
            missingComponentPanels.add(new MissingTimePanel(dateAndTime));
        }else {
            // not all elements of time are missing
            if (dateAndTime.getHour() == null) {
                missingComponentPanels.add(new MissingHourPanel(dateAndTime));
            }
            if (dateAndTime.getMinute() == null) {
                missingComponentPanels.add(new MissingMinutePanel(dateAndTime));
            }
            if (dateAndTime.getSeconds() == null) {
                missingComponentPanels.add(new MissingSecondPanel(dateAndTime));
            }
        }
        */
        /*
         *  TIME_ZONE SECTION
         *  Only add if at least one time element is already set
         */
        if (dateAndTime.getTimeZone() == null && (
                dateAndTime.getHour() != null && dateAndTime.getHour().getTableElement() != null ||
                dateAndTime.getMinute() != null && dateAndTime.getMinute().getTableElement() != null ||
                dateAndTime.getSeconds() != null && dateAndTime.getSeconds().getTableElement() != null)) {
            missingComponentPanels.add(new MissingTimeZonePanel(dateAndTime));
        }
        //
        return missingComponentPanels;
    }

    /**
     * <p>setMissingComponents.</p>
     *
     * @param components a {@link java.util.List} object.
     */
    public void setMissingComponents(final List<Component> components) {
        for (final Component c: components) {
            final MissingComponentPanel mcp = c.getMissingComponentPanel(dateAndTime);
            mcp.setMissingComponent(c);
            missingComponentPanels.add(mcp);
        }
    }

    /**
     * <p>getMissingComponents.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Component> getMissingComponents() {
        final List<Component> components = new ArrayList<>();
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            components.add(mcp.getMissingComponent());
        }
        return components;
    }

    /**
     * <p>Getter for the field <code>dateAndTime</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public DateAndTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * <p>Setter for the field <code>dateAndTime</code>.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public void setDateAndTime(final DateAndTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    /**
     * Assigns for each MissingComponentPanel the values
     */
    public void assignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.assignValues();
        }
    }

    /**
     * <p>unassignMissingComponentValues.</p>
     */
    public void unassignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.unassignValues();
        }
    }


    /**
     * Method checks the <code>pattern</code> and sets the members of the
     * controlled <code>DateAndTime</code> instance.
     *
     * @param pattern a {@link java.lang.String} object.
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void assignPattern(final String pattern, final TableElement tableElement) {
        LOG.info("Assign pattern '{}' to {} in {}", pattern, dateAndTime, tableElement);

        if (pattern.contains("y")) {
            dateAndTime.setYear(new Year(tableElement, pattern));
        }
        if (pattern.contains("M") || pattern.contains("w") || pattern.contains("D")) {
            dateAndTime.setMonth(new Month(tableElement, pattern));
        }
        if (pattern.contains("d") || pattern.contains("W") && pattern.contains("d")) {
            dateAndTime.setDay(new Day(tableElement, pattern));
        }
        if (pattern.contains("H") ||
                pattern.contains("k") ||
                pattern.contains("K") ||
                pattern.contains("h") &&
                pattern.contains("a")) {
            dateAndTime.setHour(new Hour(tableElement, pattern));
        }
        if (pattern.contains("m")) {
            dateAndTime.setMinute(new Minute(tableElement, pattern));
        }
        if (pattern.contains("s")) {
            dateAndTime.setSecond(new Second(tableElement, pattern));
        }
        if (pattern.contains("z") || pattern.contains("Z")) {
            dateAndTime.setTimeZone(new TimeZone(tableElement, pattern));
        }
    }

    /**
     * <p>getNextDateAndTimeWithMissingValues.</p>
     *
     * @return a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public DateAndTime getNextDateAndTimeWithMissingValues() {
        List<MissingComponentPanel> missingComponentPanelsTmp;

        for (final DateAndTime dateAndTimeTmp: ModelStore.getInstance().getDateAndTimes()) {
            setDateAndTime(dateAndTimeTmp);
            missingComponentPanelsTmp = getMissingComponentPanels();
            if (missingComponentPanelsTmp.size() > 0) {
                return dateAndTimeTmp;
            }
        }
        return null;
    }

    /**
     * <p>markComponents.</p>
     */
    public void markComponents() {
        if (dateAndTime.getSeconds() != null) {
            dateAndTime.getSeconds().mark();
        }
        if (dateAndTime.getMinute() != null) {
            dateAndTime.getMinute().mark();
        }
        if (dateAndTime.getHour() != null) {
            dateAndTime.getHour().mark();
        }
        if (dateAndTime.getDay() != null) {
            dateAndTime.getDay().mark();
        }
        if (dateAndTime.getMonth() != null) {
            dateAndTime.getMonth().mark();
        }
        if (dateAndTime.getYear() != null) {
            dateAndTime.getYear().mark();
        }
        if (dateAndTime.getTimeZone() != null) {
            dateAndTime.getTimeZone().mark();
        }
    }

    public void markColumn() {
        if (dateAndTime.isUnixTime()) {
            new Column(Integer.parseInt(dateAndTime.getGroup()) - 1, 0).mark();
        } else {
            markComponents();
        }
    }

    /**
     * <p>forThis.</p>
     *
     * @param measuredValuePosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a {@link java.lang.String} object.
     * @throws java.text.ParseException if any.
     */
    public String forThis(final Cell measuredValuePosition) throws ParseException {
        final int second = dateAndTime.getSeconds() != null
                ? dateAndTime.getSeconds().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        final int minute = dateAndTime.getMinute() != null
                ? dateAndTime.getMinute().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        final int hour = dateAndTime.getHour() != null
                ? dateAndTime.getHour().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        final int day = dateAndTime.getDay() != null
                ? dateAndTime.getDay().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        final int month = dateAndTime.getMonth() != null
                ? dateAndTime.getMonth().getParsedValue(measuredValuePosition) + 1
                        : Integer.MIN_VALUE;
        final int year = dateAndTime.getYear() != null
                ? dateAndTime.getYear().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        final int timezone = dateAndTime.getTimeZone() != null
                ? dateAndTime.getTimeZone().getParsedValue(measuredValuePosition)
                        : Integer.MIN_VALUE;
        // yyyy-mm-ddThh:mm:ss+hh:mm
        final StringBuffer ts = new StringBuffer(25);
        if (year != Integer.MIN_VALUE) {
            ts.append(year);
            if (month != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (month != Integer.MIN_VALUE) {
            ts.append(month < 10 ? "0" + month : month);
            if (day != Integer.MIN_VALUE) {
                ts.append("-");
            }
        }
        if (day != Integer.MIN_VALUE) {
            ts.append(day < 10 ? "0" + day : day);
        }
        if ((year != Integer.MIN_VALUE || month != Integer.MIN_VALUE || day != Integer.MIN_VALUE)
                && (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || second != Integer.MIN_VALUE)) {
            ts.append("T");
        }
        if (hour != Integer.MIN_VALUE) {
            ts.append(hour < 10 ? "0" + hour : hour);
            if (minute != Integer.MIN_VALUE) {
                ts.append(":");
            }
        }
        if (minute != Integer.MIN_VALUE) {
            ts.append(minute < 10 ? "0" + minute : minute);
            if (second != Integer.MIN_VALUE) {
                ts.append("");
            }
        }
        if (second != Integer.MIN_VALUE) {
            ts.append(second < 10 ? "0" + second : second);
        }
        if (timezone != Integer.MIN_VALUE &&
                (hour != Integer.MIN_VALUE || minute != Integer.MIN_VALUE || second != Integer.MIN_VALUE)) {
            ts.append(convertTimeZone(timezone));
        }

        return ts.toString();
    }

    private String convertTimeZone(final int timeZone) {
        final String zeros = ":00";
        if (timeZone >= 0) {
            if (timeZone >= 10) {
                return "+" + timeZone + zeros;
            } else {
                return "+0" + timeZone + zeros;
            }
        } else {
            if (timeZone <= -10) {
                return timeZone + zeros;
            } else {
                return "-0" + Math.abs(timeZone) + zeros;
            }
        }
    }


    /**
     * <p>mergeDateAndTimes.</p>
     */
    public void mergeDateAndTimes() {
        LOG.info("Merge Date & Times");
        final List<DateAndTime> dateAndTimes = ModelStore.getInstance().getDateAndTimes();
        final ArrayList<DateAndTime> mergedDateAndTimes = new ArrayList<>(dateAndTimes.size());
        while (!dateAndTimes.isEmpty()) {
            final DateAndTime dt1 = dateAndTimes.get(0);
            dateAndTimes.remove(dt1);
            // create tmp list from left over dts
            final List<DateAndTime> list2 = new ArrayList<>(dateAndTimes);
            final Iterator<DateAndTime> dATIter = list2.iterator();
            while (dATIter.hasNext()) {
                final DateAndTime dt2 = dATIter.next();
                if (!dt1.getGroup().equalsIgnoreCase(NULL) &&
                        !dt2.getGroup().equalsIgnoreCase(NULL) &&
                        dt1.getGroup().equals(dt2.getGroup())) {
                    merge(dt1, dt2);
                    dateAndTimes.remove(dt2);
                }
            }
            mergedDateAndTimes.add(dt1);
        }
        mergedDateAndTimes.trimToSize();
        ModelStore.getInstance().setDateAndTimes(mergedDateAndTimes);
    }

    private void merge(final DateAndTime dateAndTime1, final DateAndTime dateAndTime2) {
        if (dateAndTime1.getSeconds() == null && dateAndTime2.getSeconds() != null) {
            dateAndTime1.setSecond(dateAndTime2.getSeconds());
        }
        if (dateAndTime1.getMinute() == null && dateAndTime2.getMinute() != null) {
            dateAndTime1.setMinute(dateAndTime2.getMinute());
        }
        if (dateAndTime1.getHour() == null && dateAndTime2.getHour() != null) {
            dateAndTime1.setHour(dateAndTime2.getHour());
        }
        if (dateAndTime1.getDay() == null && dateAndTime2.getDay() != null) {
            dateAndTime1.setDay(dateAndTime2.getDay());
        }
        if (dateAndTime1.getMonth() == null && dateAndTime2.getMonth() != null) {
            dateAndTime1.setMonth(dateAndTime2.getMonth());
        }
        if (dateAndTime1.getYear() == null && dateAndTime2.getYear() != null) {
            dateAndTime1.setYear(dateAndTime2.getYear());
        }
        if (dateAndTime1.getTimeZone() == null && dateAndTime2.getTimeZone() != null) {
            dateAndTime1.setTimeZone(dateAndTime2.getTimeZone());
        }
    }
}
