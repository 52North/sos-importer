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
 * @version $Id: $Id
 */
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingLongitudePanel;
public class Longitude extends PositionComponent {

    /**
     * <p>Constructor for Longitude.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     * @param pattern a {@link java.lang.String} object.
     */
    public Longitude(TableElement tableElement, String pattern) {
        super(tableElement, pattern);
    }

    /**
     * <p>Constructor for Longitude.</p>
     *
     * @param value a double.
     * @param unit a {@link java.lang.String} object.
     */
    public Longitude(double value, String unit) {
        super(value, unit);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Longitude" + super.toString();
    }

    /** {@inheritDoc} */
    @Override
    public MissingComponentPanel getMissingComponentPanel(Combination c) {
        return new MissingLongitudePanel((Position)c);
    }

    /**
     * tries to convert a given String into a valid Longitude object
     *
     * @param s a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.position.Longitude} object.
     */
    // TODO units and Strings -> Constants
    public static Longitude parse(String s) {
        double value = 0.0;
        String unit = "";

        String number;
        //TODO handle inputs like degrees/minutes/seconds, n.Br.
        if (s.contains("°")) {
            unit = "°";
            String[] part = s.split("°");
            number = part[0];
        } else if (s.contains("m")) {
            unit = "m";
            number = s.replace("m", "");
        } else
            number = s;

        NumericValue nv = new NumericValue();

        value = nv.parse(number);

        if (unit.equals(""))
            if (value <= 180.0 && value >= -180.0)
                unit = "°";
            else
                unit = "m";

        return new Longitude(value, unit);
    }

    /** {@inheritDoc} */
    @Override
    public Longitude forThis(Cell featureOfInterestPosition) {
        if (getTableElement() == null)
            return new Longitude(getValue(), getParsedUnit());
        else {
            String longitudeString = getTableElement().getValueFor(featureOfInterestPosition);
            return Longitude.parse(longitudeString);
        }
    }
}
