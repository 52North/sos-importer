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
package org.n52.sos.importer.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class holds the data of an CSV file incl. work arounds for bad
 * formatted files:
 * <ul><li>having column count mismatch (e.g. sample based files)</li></ul>
 *
 * @since 0.4.0
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class CsvData {

    private List<String[]> lines = new LinkedList<>();
    private int columns;

    /**
     * <p>Setter for the field <code>lines</code>.</p>
     *
     * @param lines a {@link java.util.List} object.
     */
    public void setLines(final List<String[]> lines) {
        this.lines = lines;
        columns = 0;
        if (lines != null) {
            for (final String[] strings : lines) {
                if (columns < strings.length) {
                    columns = strings.length;
                }
            }
        }
    }

    /**
     * <p>getRowCount.</p>
     *
     * @return a int.
     */
    public int getRowCount() {
        if (lines == null) {
            return 0;
        }
        return lines.size();
    }

    /**
     * <p>getColumnCount.</p>
     *
     * @return a int.
     */
    public int getColumnCount() {
        return columns;
    }

    /**
     * <p>getLine.</p>
     *
     * @param i a int.
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getLine(final int i) {
        final String[] extended = new String[columns];
        if (lines == null) {
            Arrays.fill(extended, "");
            return extended;
        }
        String[] tmp = lines.get(i);
        if (tmp.length < columns) {
            Arrays.fill(extended, "");
            System.arraycopy(tmp, 0, extended, 0, tmp.length);
            tmp = extended;
        }
        return tmp;
    }

}
