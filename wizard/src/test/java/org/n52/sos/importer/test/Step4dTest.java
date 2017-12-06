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
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step4dController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4dModel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Step4bTest class.</p>
 *
 * @author e.h.juerrens@52north.org
 * @since 0.5.0
 */
public class Step4dTest {

    private static final Logger logger = LoggerFactory.getLogger(Step4dTest.class);

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        logger.trace("main()");
        final MainController f = MainController.getInstance();
        // Lang.setCurrentLocale(Locale.GERMAN);
        final int firstLineWithData = 1;
        int i = 0;
        final OmParameter op = TestData.EXAMPLE_OM_PARAMETER;
        final TableController tc = TableController.getInstance();
        tc.setContent(TestData.EXAMPLE_TABLE);
        tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
        tc.setColumnHeading(++i, Lang.l().sensor());
        tc.setColumnHeading(++i, Lang.l().observedProperty());
        tc.setColumnHeading(++i, Lang.l().featureOfInterest());
        tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
        tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
        tc.setColumnHeading(++i, Lang.l().featureOfInterest());
        tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
        tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());

        final ModelStore ms = ModelStore.getInstance();
        final NumericValue nV1 = new NumericValue();
        final NumericValue nV2 = new NumericValue();
        nV1.setTableElement(new Column(5, firstLineWithData));
        nV2.setTableElement(new Column(8, firstLineWithData));

        ms.add(nV1);
        ms.add(nV2);

        f.setStepController(new Step4dController(firstLineWithData, new Step4dModel(op)));
    }
}
