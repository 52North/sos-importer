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
package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step6Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.resources.MissingResourcePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to determine sensors in case there is one of the following
 * relationships between feature of interest and observed property
 * column: 0:1, 0:n, n:0, 1:0, 1:1, n:n
 *
 * @author Raimund
 */
public class Step6bSpecialController extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step6bSpecialController.class);

    private Step6bSpecialModel model;

    private Step6Panel panel;

    private MissingResourcePanel missingResourcePanel;

    private final TableController tableController;

    private final int firstLineWithData;

    public Step6bSpecialController(final int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
        tableController = TableController.getInstance();
    }

    public Step6bSpecialController(final Step6bSpecialModel step6bSpecialModel, final int firstLineWithData) {
        this(firstLineWithData);
        model = step6bSpecialModel;
    }

    @Override
    public void loadSettings() {
        // FIXME does not work with generated
        final String description = model.getDescription();
        final String foiName = model.getFeatureOfInterest().getName();
        final String opName = model.getObservedProperty().getName();

        final Sensor sensor = model.getSensor();
        missingResourcePanel = new MissingResourcePanel(sensor);
        missingResourcePanel.setMissingComponent(sensor);
        ModelStore.getInstance().remove(model);
        missingResourcePanel.unassignValues();

        final List<MissingComponentPanel> missingComponentPanels = new ArrayList<>();
        missingComponentPanels.add(missingResourcePanel);

        panel = new Step6Panel(description, foiName, opName, missingComponentPanels);
    }

    @Override
    public void saveSettings() {
        missingResourcePanel.assignValues();
        ModelStore.getInstance().add(model);

        panel = null;
        missingResourcePanel = null;
    }

    @Override
    public boolean isNecessary() {
        if (ModelStore.getInstance().getSensorsInTable().size() > 0) {
            LOG.info("Skip 6b (Special) since there are sensors in the table.");
            return false;
        }

        if (ModelStore.getInstance().getFeatureOfInterestsInTable().isEmpty() &&
                ModelStore.getInstance().getObservedPropertiesInTable().isEmpty()) {
            LOG.info("Skip 6b (Special) since there are not any features of interest" +
                    "and observed properties in the table.");
            return false;
        }

        model = getNextModel();
        return true;

    }

    public Step6bSpecialModel getNextModel() {
        final ModelStore ms = ModelStore.getInstance();

        // iterate through all measured value columns/rows
        mvLoop:
            for (final MeasuredValue mv: ms.getMeasuredValues()) {

                for (int i = tableController.getFirstLineWithData(); i < tableController.getRowCount(); i++) {
                    // test if the measuredValue can be parsed
                    final Cell cell = new Cell(i, ((Column) mv.getTableElement()).getNumber());
                    final String value = tableController.getValueAt(cell);
                    if (i >= firstLineWithData) {
                        try {
                            mv.parse(value);
                        } catch (final Exception e) {
                            LOG.trace("Value could not be parsed: " + value, e);
                            // it is okay this way because parsing test happened during step 3
                            continue;
                        }

                        final FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(cell);
                        final ObservedProperty op = mv.getObservedProperty().forThis(cell);
                        final Step6bSpecialModel tmpModel = new Step6bSpecialModel(mv, foi, op);
                        /*
                         * check, if for the column of foi and obsProp a model is
                         * available and if the sensor is generated in this model
                         */
                        for (final Step6bSpecialModel s6bsM : ms.getStep6bSpecialModels()) {
                            if (s6bsM.getSensor().isGenerated() &&
                                    s6bsM.getFeatureOfInterest().equals(foi) &&
                                    s6bsM.getObservedProperty().equals(op)) {
                                continue mvLoop;
                            }
                        }
                        if (!ms.getStep6bSpecialModels().contains(tmpModel)) {
                            return tmpModel;
                        }
                    }
                }
            }
        return null;
    }

    @Override
    public boolean isFinished() {
        return missingResourcePanel.checkValues();
    }

    @Override
    public StepController getNext() {
        final Step6bSpecialModel tmpModel = getNextModel();
        if (tmpModel != null) {
            return new Step6bSpecialController(tmpModel, firstLineWithData);
        }
        return null;
    }

    @Override
    public StepModel getModel() {
        return model;
    }

    @Override
    public String getDescription() {
        return Lang.l().step6bSpecialDescription();
    }

    @Override
    public JPanel getStepPanel() {
        return panel;
    }

    @Override
    public StepController getNextStepController() {
        return new Step6cController();
    }

}
