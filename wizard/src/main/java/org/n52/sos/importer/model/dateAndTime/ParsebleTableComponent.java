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

package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public abstract class ParsebleTableComponent extends Component {

    private static final Logger LOG = LoggerFactory.getLogger(ParsebleTableComponent.class);

    private TableElement tableElement;

    private String pattern;

    public ParsebleTableComponent() {
        super();
    }

    public ParsebleTableComponent(TableElement tableElement, String pattern) {
        this.tableElement = tableElement;
        this.pattern = pattern;
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
     * Returns the corresponding Gregorian calendar field
     * for this component
     *
     * @return a int.
     */
    public abstract int getGregorianCalendarField();

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}