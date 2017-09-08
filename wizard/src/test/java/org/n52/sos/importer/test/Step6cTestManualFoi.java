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
 * @since 0.5.0
 */
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6cController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

public class Step6cTestManualFoi {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        TableController.getInstance().setContent(TestData.EXAMPLE_TABLE_MINI);
        final int firstLineWithData = 0;
        final DateAndTime dtm = new DateAndTime();
        final DateAndTimeController dtc = new DateAndTimeController(dtm);
        dtc.assignPattern("dd/MM/yyyy HH:mm", new Column(0, firstLineWithData));
        dtm.setSecond(new Second(0));
        dtm.setTimeZone(new TimeZone(1));

        final ObservedProperty op = new ObservedProperty();
        op.setName("Temperature");
        final UnitOfMeasurement uom = new UnitOfMeasurement();
        uom.setName("Degree Celsius");
        final FeatureOfInterest foi = new FeatureOfInterest();
        foi.setName("Weatherstation Muenster");
        final Sensor sn = new Sensor();
        sn.setName("Thermometer xy");

        final NumericValue nv1 = new NumericValue();
        nv1.setTableElement(new Column(1, firstLineWithData));
        nv1.setDateAndTime(dtm);
        nv1.setObservedProperty(op);
        nv1.setFeatureOfInterest(foi);
        nv1.setSensor(sn);
        nv1.setUnitOfMeasurement(uom);

        final NumericValue nv2 = new NumericValue();
        nv2.setTableElement(new Column(2, firstLineWithData));
        nv2.setDateAndTime(dtm);
        nv2.setObservedProperty(op);
        nv2.setFeatureOfInterest(foi);
        nv2.setSensor(sn);
        nv2.setUnitOfMeasurement(uom);

        ModelStore.getInstance().add(nv1);
        ModelStore.getInstance().add(nv2);
        ModelStore.getInstance().add(foi);

        final MainController f = MainController.getInstance();

        final Step6cModel step7Model = new Step6cModel(foi);
        f.setStepController(new Step6cController(step7Model));
    }

}
