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

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lets the user choose date&amp;time for all measured value columns
 * in case there are not any date&amp;times given in the CSV file
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class Step6aController extends StepController {

    private static final Logger logger = LoggerFactory.getLogger(Step6aController.class);

    private Step6aModel step6aModel;

    private Step5Panel step5Panel;

    private DateAndTimeController dateAndTimeController;

    private final TableController tableController;

    private final int firstLineWithData;

    /**
     * <p>Constructor for Step6aController.</p>
     *
     * @param firstLineWithData a int.
     */
    public Step6aController(final int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
        tableController = TableController.getInstance();
    }

    /**
     * <p>Constructor for Step6aController.</p>
     *
     * @param step6aModel a {@link org.n52.sos.importer.model.Step6aModel} object.
     * @param firstLineWithData a int.
     */
    public Step6aController(final Step6aModel step6aModel, final int firstLineWithData) {
        this(firstLineWithData);
        this.step6aModel = step6aModel;
    }

    /** {@inheritDoc} */
    @Override
    public void loadSettings() {
        tableController.turnSelectionOff();

        final DateAndTime dateAndTime = step6aModel.getDateAndTime();
        dateAndTimeController = new DateAndTimeController(dateAndTime);
        final List<Component> components = step6aModel.getMissingDateAndTimeComponents();
        dateAndTimeController.setMissingComponents(components);

        for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
            mv.setDateAndTime(null);
        }

        dateAndTimeController.unassignMissingComponentValues();

        final String description = step6aModel.getDescription();
        final List<MissingComponentPanel> mcp = dateAndTimeController.getMissingComponentPanels();
        step5Panel = new Step5Panel(description, mcp);
    }

    /** {@inheritDoc} */
    @Override
    public void saveSettings() {
        dateAndTimeController.assignMissingComponentValues();

        final List<Component> components = dateAndTimeController.getMissingComponents();
        step6aModel.setMissingDateAndTimeComponents(components);

        final DateAndTime dateAndTime = dateAndTimeController.getDateAndTime();

        for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
            mv.setDateAndTime(dateAndTime);
        }

        tableController.turnSelectionOn();

        step5Panel = null;
    }

    /** {@inheritDoc} */
    @Override
    public void back() {
        tableController.turnSelectionOn();

        step5Panel = null;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return Lang.l().step6aDescription();
    }

    /** {@inheritDoc} */
    @Override
    public JPanel getStepPanel() {
        return step5Panel;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNextStepController() {
        return new Step6bController(firstLineWithData);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNecessary() {
        final int n = ModelStore.getInstance().getDateAndTimes().size();
        if (n == 0) {
            final DateAndTime dateAndTime = new DateAndTime();
            step6aModel = new Step6aModel(dateAndTime);
            return true;
        }

        logger.info("Skip Step 6a since there is at least one Date&Time");

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinished() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNext() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public StepModel getModel() {
        return step6aModel;
    }

}
