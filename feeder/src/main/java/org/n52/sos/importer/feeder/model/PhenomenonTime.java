/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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

/**
 * POJO class for phenomenon times holding one or two {@link Timestamp} values.
 * The one value that might be <code>null</code> will be set as
 * <code>indeterminate</code> value.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class PhenomenonTime {

    public static final Timestamp INDETERMINATE = new Timestamp();

    private static final String OPEN_START = "../";
    private static final String OPEN_END = "/..";
    private final Timestamp start;
    private final Timestamp end;

    /**
     * Create an time instant.
     *
     * @param instant {@link Timestamp}
     */
    public PhenomenonTime(Timestamp instant) {
        start = instant;
        end = instant;
    }

    /**
     * Create an interval with potential indeterminate values.
     *
     * @param start <code>null</code> or {@link Timestamp}
     * @param end <code>null</code> or {@link Timestamp}
     */
    public PhenomenonTime(Timestamp start, Timestamp end) {
        if (end == null && start == null) {
            throw new IllegalArgumentException("At least one value MUST not be null");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * @return <code>true</code> in the case of the phenomenon time being
     *  a point in time and not a period.
     */
    public boolean isInstant() {
        return getStart().equals(getEnd());
    }

    public Timestamp getEnd() {
        if (end == null) {
            return INDETERMINATE;
        }
        return end;
    }

    public Timestamp getStart() {
        if (start == null) {
            return INDETERMINATE;
        }
        return start;
    }

    public String toISO8601String() {
        if (isInstant()) {
            return start.toISO8601String();
        }
        // FIXME the next two forms might be not supported by ISO8601 decoder
        if (end == null) {
            return start.toISO8601String() + OPEN_END;
        }
        if (start == null) {
            return OPEN_START + end.toISO8601String();
        }
        return start.toISO8601String() + "/" + end.toISO8601String();
    }

    @Override
    public String toString() {
        return toISO8601String();
    }

    public String toEpochSeconds() {
        if (isInstant()) {
            return Long.toString(start.toEpochSeconds());
        }
        // FIXME the next two forms might be not supported by ISO8601 decoder
        if (end == null) {
            return start.toEpochSeconds() + OPEN_END;
        }
        if (start == null) {
            return OPEN_START + end.toEpochSeconds();
        }
        return start.toEpochSeconds() + "/" + end.toEpochSeconds();
    }

}
