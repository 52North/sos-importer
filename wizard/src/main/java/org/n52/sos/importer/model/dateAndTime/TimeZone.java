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

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.GregorianCalendar;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;

/**
 * TimeZone wrapper
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class TimeZone extends ParsebleTableComponent {

    private ZoneId zoneId;

    private ZoneOffset offset;

    /**
     * <p>Constructor for TimeZone.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     * @param pattern a {@link java.lang.String} object.
     */
    public TimeZone(TableElement tableElement, String pattern) {
        super(tableElement, pattern);
    }

    public TimeZone(ZoneId zoneId) {
        super();
        this.zoneId = zoneId;
    }

    public TimeZone(ZoneOffset offset) {
        super();
        this.offset = offset;
    }

    public void setValue(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneId getValue() {
        return zoneId;
    }

    @Override
    public void setTableElement(TableElement tableElement) {
        // TODO Auto-generated method stub
        super.setTableElement(tableElement);
    }

    @Override
    public TableElement getTableElement() {
        // TODO Auto-generated method stub
        return super.getTableElement();
    }

    @Override
    public void mark() {
        // TODO Auto-generated method stub
        super.mark();
    }

    @Override
    public int getGregorianCalendarField() {
        return GregorianCalendar.ZONE_OFFSET;
    }

    @Override
    public String toString() {
        return "Timezone" + super.toString();
    }

    @Override
    public MissingComponentPanel getMissingComponentPanel(Combination c) {
        return new MissingTimeZonePanel((DateAndTime) c);
    }
}
