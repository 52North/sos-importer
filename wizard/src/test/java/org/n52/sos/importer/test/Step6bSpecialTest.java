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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6bSpecialController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Step6bSpecialTest {

    private static final Logger logger = LoggerFactory.getLogger(Step6bSpecialTest.class);

    //CHECKSTYLE:OFF
    public static void main(final String[] args) throws URISyntaxException {
        //CHECKSTYLE:ON
        logger.debug("Start Test");
        Constants.setGuiDebug(false);
        /*
         * Set-Up Step specific data
         */
        final int firstLineWithData = 1;
        Column markedColumn = new Column(4, firstLineWithData);
        TableController tc = TableController.getInstance();
        tc.setContent(TestData.EXAMPLE_TABLE_NO_FOI);
        int i = 0;
        tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
        tc.setColumnHeading(++i, Lang.l().sensor());
        tc.setColumnHeading(++i, Lang.l().observedProperty());
        tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
        tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
        tc.mark(markedColumn);
        MeasuredValue mv = new NumericValue();
        mv.setFeatureOfInterest(TestData.EXAMPLE_FOI);
        mv.setObservedProperty(TestData.EXAMPLE_OBS_PROP);
        mv.setTableElement(markedColumn);
        ModelStore.getInstance().add(mv);
        Step6bSpecialModel s6bSM = new Step6bSpecialModel(mv, TestData.EXAMPLE_FOI, TestData.EXAMPLE_OBS_PROP);
        /*
         * Set-Up Column metadata
         */
        Step3Model s3M = new Step3Model(4, firstLineWithData, false);
        List<String> selection = new ArrayList<>(1);
        selection.add(Lang.l().step3ColTypeMeasuredValue());
        selection.add(Lang.l().step3MeasuredValNumericValue());
        selection.add(".SEP,");
        s3M.addSelection(selection);
        MainController mC = MainController.getInstance();
        mC.registerProvider(s3M);
        mC.updateModel();
        mC.removeProvider(s3M);
        //
        mC.setStepController(new Step6bSpecialController(s6bSM, firstLineWithData));
    }
}
