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
 * @since 0.5.0
 */
package org.n52.sos.importer.test;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6cController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step6bModel;
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
import org.n52.sos.importer.view.i18n.Lang;
public class Step6cTestGeneratedFoi {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(final String[] args) {
        final MainController mC = MainController.getInstance();

        TableController.getInstance().setContent(TestData.EXAMPLE_TABLE_MINI);
        final int firstLineWithData = 0;
        final DateAndTime dtm = new DateAndTime();
        final DateAndTimeController dtc = new DateAndTimeController(dtm);
        dtc.assignPattern("dd/MM/yyyy HH:mm", new Column(0,firstLineWithData));
        dtm.setSecond(new Second(0));
        dtm.setTimeZone(new TimeZone(1));

        final ObservedProperty op = new ObservedProperty();
        op.setName("Temperature");

        final UnitOfMeasurement uom = new UnitOfMeasurement();
        uom.setName("Degree Celsius");

        final Column c1 = new Column(1,firstLineWithData),
                c2 = new Column(2,firstLineWithData);
        final Column[] cols = {c1,c2};

        final FeatureOfInterest foi = new FeatureOfInterest();
        foi.setGenerated(true);
        foi.setConcatString("/");
        foi.setUseNameAfterPrefixAsURI(true);
        foi.setRelatedCols(cols);
        foi.setUriPrefix("http://example.com/");

        final Sensor sn = new Sensor();
        sn.setName("Thermometer xy");

        final NumericValue nv1 = new NumericValue();
        nv1.setTableElement(c1);
        nv1.setDateAndTime(dtm);
        nv1.setObservedProperty(op);
        nv1.setFeatureOfInterest(foi);
        nv1.setSensor(sn);
        nv1.setUnitOfMeasurement(uom);

        final NumericValue nv2 = new NumericValue();
        nv2.setTableElement(c2);
        nv2.setDateAndTime(dtm);
        nv2.setObservedProperty(op);
        nv2.setFeatureOfInterest(foi);
        nv2.setSensor(sn);
        nv2.setUnitOfMeasurement(uom);


        ModelStore.getInstance().add(nv1);
        ModelStore.getInstance().add(nv2);
        ModelStore.getInstance().add(foi);

        /*
         * Set-Up Column metadata
         */
        final Step3Model s3M = new Step3Model(1, firstLineWithData, false);
        final List<String> selection = new ArrayList<String>(1);
        selection.add(Lang.l().step3ColTypeMeasuredValue());
        selection.add(Lang.l().step3MeasuredValNumericValue());
        selection.add(".SEP,");
        s3M.addSelection(selection);
        mC.registerProvider(s3M);
        mC.updateModel();
        mC.removeProvider(s3M);
        final Step6bModel s6bM = new Step6bModel(nv1,foi);
        mC.registerProvider(s6bM);
        mC.updateModel();
        mC.removeProvider(s6bM);

        final Step6cModel step7Model = new Step6cModel(foi);
        mC.setStepController(new Step6cController(step7Model));
    }

}
