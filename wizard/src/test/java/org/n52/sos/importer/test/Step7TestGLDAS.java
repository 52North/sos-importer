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
package org.n52.sos.importer.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step7TestGLDAS {

    //CHECKSTYLE:OFF
    public static void main(final String[] args) throws URISyntaxException {
        //CHECKSTLYE:ON
        // init logger
        //
        final ModelStore ms = ModelStore.getInstance();
        final TableController tc = TableController.getInstance();
        Constants.setDecimalSeparator('.');
        Constants.setThousandsSeparator(',');
        tc.setContent(TestData.EXMAPLE_TABLE_GLDAS);
        final int firstLineWithData = 1;
        tc.setFirstLineWithData(firstLineWithData);

        final DateAndTime dtm1 = new DateAndTime();
        dtm1.setGroup("1");
        final DateAndTimeController dtc1 = new DateAndTimeController(dtm1);
        dtc1.assignPattern("d.M.yyyy", new Column(4, firstLineWithData));
        ms.add(dtm1);

        final DateAndTime dtm2 = new DateAndTime();
        dtm2.setGroup("1");
        final DateAndTimeController dtc2 = new DateAndTimeController(dtm1);
        dtc2.assignPattern("H,00", new Column(5, firstLineWithData));
        ms.add(dtm2);

        final DateAndTime dtm3 = new DateAndTime();
        dtm3.setGroup("1");
        final DateAndTimeController dtc3 = new DateAndTimeController(dtm1);
        dtc3.assignPattern("m,00", new Column(6, firstLineWithData));
        ms.add(dtm3);

        dtc2.mergeDateAndTimes();

        final DateAndTime dtm = ModelStore.getInstance().getDateAndTimes().get(0);
        dtm.setSecond(new Second(0));
        dtm.setTimeZone(new TimeZone(1));

        final Position p = new Position();
        p.setGroup("A");
        final PositionComponent lat = new PositionComponent(Id.COORD_0, new Column(2, firstLineWithData), Id.COORD_0.name());
        final PositionComponent lon = new PositionComponent(Id.COORD_1, new Column(1, firstLineWithData), Id.COORD_1.name());
        final PositionComponent h = new PositionComponent(Id.COORD_2, 100, "m");
        final EPSGCode epsgCode = new EPSGCode(4326);
        p.addCoordinate(lat);
        p.addCoordinate(lon);
        p.addCoordinate(h);
        p.setEPSGCode(epsgCode);

        final ObservedProperty op = new ObservedProperty();
        op.setName("near_surface_air_temperature");
        op.setURI(new URI("http://www.eo2heaven.org/classifier/parameter/NearSurfaceAirTemperature"));

        final UnitOfMeasurement uom = new UnitOfMeasurement();
        uom.setName("degC");
        uom.setURI(new URI("http://ucum.org/temp/celcsius"));

        final Column[] relatedCols = {
                new Column(2, firstLineWithData),
                new Column(1, firstLineWithData),
        };

        final FeatureOfInterest foi = new FeatureOfInterest();
        foi.setGenerated(true);
        foi.setUseNameAfterPrefixAsURI(true);
        foi.setUriPrefix("http://www.eo2heaven.org/feature/");
        foi.setConcatString("/");
        foi.setPosition(p);
        foi.setRelatedCols(relatedCols);

        final Sensor sn = new Sensor();
        sn.setName("GLDAS");
        sn.setURI(new URI("http://nasa.gov/gldas"));

        final NumericValue nv1 = new NumericValue();
        nv1.setTableElement(new Column(3, firstLineWithData));
        nv1.setDateAndTime(dtm);
        nv1.setObservedProperty(op);
        nv1.setFeatureOfInterest(foi);
        nv1.setSensor(sn);
        nv1.setUnitOfMeasurement(uom);

        ms.add(nv1);
        ms.add(foi);

        final MainController f = MainController.getInstance();

        f.setStepController(new Step7Controller());
    }
}
