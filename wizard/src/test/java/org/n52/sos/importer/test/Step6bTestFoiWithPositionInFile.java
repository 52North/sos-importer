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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.controller.Step6bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bTestFoiWithPositionInFile {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        final MainController mC = MainController.getInstance();
        final TableController tc = TableController.getInstance();
        final ModelStore ms = ModelStore.getInstance();
        final int firstLineWithData = 0;
        int i = 0;
        Step6bModel s6bM;
        MeasuredValue mv;
        Column markedColumn;
        Constants.setGuiDebug(false);
        //
        markedColumn = new Column(4, firstLineWithData);
        tc.setContent(TestData.EXAMPLE_TABLE_NO_FOI_BUT_POSITION);
        tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
        tc.setColumnHeading(++i, Lang.l().sensor());
        tc.setColumnHeading(++i, Lang.l().observedProperty());
        tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
        tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
        tc.setColumnHeading(++i, Lang.l().position());
        tc.setColumnHeading(++i, Lang.l().position());
        tc.mark(markedColumn);
        mv = new NumericValue();
        mv.setTableElement(markedColumn);
        ms.add(mv);
        s6bM = new Step6bModel(mv, new FeatureOfInterest());
        /*
         * Set-Up Column metadata
         */
        Step3Model s3M = new Step3Model(4, firstLineWithData, false);
        List<String> selection = new ArrayList<String>(3);
        selection.add(Lang.l().step3ColTypeMeasuredValue());
        selection.add(Lang.l().step3MeasuredValNumericValue());
        selection.add(".SEP,");
        s3M.addSelection(selection);
        mC.registerProvider(s3M);
        mC.updateModel();
        mC.removeProvider(s3M);
        /*
         * add position metadata to model
         */
        final String group = "A";
        String pattern = "LON";
        int colId = 5;
        s3M = new Step3Model(colId, firstLineWithData, false);
        selection = new ArrayList<String>(3);
        selection.add(Lang.l().position());
        selection.add(Lang.l().step3PositionCombination());
        // set parse pattern and group separated by
        selection.add(pattern + Constants.SEPARATOR_STRING + group);
        Position position = new Position();
        position.setGroup(group);
        PositionController pc = new PositionController(position);
        TableElement tabE = new Column(colId, firstLineWithData);
        pc.assignPattern(pattern, tabE);
        ModelStore.getInstance().add(position);
        s3M.addSelection(selection);
        mC.registerProvider(s3M);
        mC.updateModel();
        mC.removeProvider(s3M);
        // 2nd position column
        colId = 6;
        s3M = new Step3Model(colId, firstLineWithData, false);
        pattern = "LAT";
        tabE = new Column(colId, firstLineWithData);
        selection = new ArrayList<String>(3);
        selection.add(Lang.l().position());
        selection.add(Lang.l().step3PositionCombination());
        // set parse pattern and group separated by
        selection.add(pattern + Constants.SEPARATOR_STRING + group);
        position = new Position();
        position.setGroup(group);
        pc = new PositionController(position);
        pc.assignPattern(pattern, tabE);
        ModelStore.getInstance().add(position);
        s3M.addSelection(selection);
        // before update with last model
        new PositionController().mergePositions();
        mC.registerProvider(s3M);
        mC.updateModel();
        mC.removeProvider(s3M);
        //
        mC.setStepController(new Step6bController(s6bM, firstLineWithData));
    }
}
