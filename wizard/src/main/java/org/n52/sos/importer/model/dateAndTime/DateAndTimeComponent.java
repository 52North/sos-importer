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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DateAndTimeComponent extends Component {

    private static final Logger LOG = LoggerFactory.getLogger(DateAndTimeComponent.class);

    private TableElement tableElement;

    private String pattern;

    private int value = Constants.NO_INPUT_INT;

    /**
     * <p>Constructor for DateAndTimeComponent.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     * @param pattern a {@link java.lang.String} object.
     */
    public DateAndTimeComponent(final TableElement tableElement, final String pattern) {
        this.tableElement = tableElement;
        this.pattern = pattern;
    }

    /**
     * <p>Constructor for DateAndTimeComponent.</p>
     *
     * @param value a int.
     */
    public DateAndTimeComponent(final int value) {
        this.value = value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a int.
     */
    public void setValue(final int value) {
        LOG.info("Assign Value to " + this.getClass().getName());
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return the value of this DateAndTimeComponent, or Integer.MIN_VALUE
     */
    public int getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>tableElement</code>.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void setTableElement(final TableElement tableElement) {
        LOG.info("Assign Column to " + this.getClass().getName());
        this.tableElement = tableElement;
    }

    /**
     * <p>Getter for the field <code>tableElement</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public TableElement getTableElement() {
        return tableElement;
    }

    /**
     * Colours the particular date&amp;time component
     */
    public void mark() {
        if (tableElement != null) {
            tableElement.mark();
        }
    }

    /**
     * Returns either the manually set value or
     * the value of this component in the table
     *
     * @throws java.text.ParseException if any.
     * @param measuredValuePosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a int.
     */
    public int getParsedValue(final Cell measuredValuePosition) throws ParseException {
        if (tableElement == null) {
            return getValue();
        } else {
            return parse(tableElement.getValueFor(measuredValuePosition));
        }
    }

    /**
     * Converts a String along a given pattern into the value of this component
     *
     * @param s a {@link java.lang.String} object.
     * @throws java.text.ParseException if any.
     * @return a int.
     */
    public int parse(final String s) throws ParseException {
        Date date = null;
        final SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        try {
            date = formatter.parse(s);
        } catch (final ParseException e) {
            LOG.error("Given String could not be parsed: " + s, e);
            throw e;
        }

        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        return gc.get(getGregorianCalendarField());
    }

    /**
     * Returns the corresponding Gregorian calendar field
     * for this component
     *
     * @return a int.
     */
    public abstract int getGregorianCalendarField();

    @Override
    public String toString() {
        if (getTableElement() == null) {
            return " '" + getValue() + "'";
        } else {
            return " " + getTableElement();
        }
    }

}
