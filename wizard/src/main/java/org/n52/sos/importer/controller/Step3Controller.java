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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step3Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.step3.SelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lets the user identify different types of metadata
 * for each column in the CSV file
 *
 * @author Raimund
 */
public class Step3Controller extends StepController {

    private static final String NULL = "null";
    private static final Logger LOG = LoggerFactory.getLogger(Step3Controller.class);

    private final Step3Model model;
    private final TableController tabCtrlr = TableController.getInstance();

    private Step3Panel panel;

    /**
     * <p>Constructor for Step3Controller.</p>
     *
     * @param currentColumn a int.
     * @param firstLineWithData a int.
     * @param useHeader a boolean.
     */
    public Step3Controller(final int currentColumn,
            final int firstLineWithData,
            final boolean useHeader) {
        model = new Step3Model(currentColumn,
                firstLineWithData,
                useHeader);
        panel = new Step3Panel(firstLineWithData);
    }

    @Override
    public String getDescription() {
        return Lang.l().step3Description();
    }

    @Override
    public JPanel getStepPanel() {
        return panel;
    }

    @Override
    public void loadSettings() {
        LOG.trace("loadSettings()");
        logAttributes();

        final int number = model.getMarkedColumn();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading settings for column# " + number);
        }
        final int fLWData = model.getFirstLineWithData();
        final Column column = new Column(number, fLWData);
        final List<String> selection = model.getSelectionForColumn(number);
        if (panel == null) {
            panel = new Step3Panel(model.getFirstLineWithData());
        }
        if (selection != null) {
            panel.restore(selection);
        }
        panel.getLastChildPanel().unAssign(column);

        tabCtrlr.mark(column);
        tabCtrlr.setColumnHeading(number, "???");
        tabCtrlr.setTableSelectionMode(TableController.COLUMNS);
        tabCtrlr.turnSelectionOff();
    }

    @Override
    public void saveSettings() {
        LOG.trace("saveSettings()");
        logAttributes();
        //
        final List<String> selection = new ArrayList<>();
        SelectionPanel selP;
        final int number = model.getMarkedColumn();
        final int firstLineWithData = model.getFirstLineWithData();
        //
        panel.store(selection);
        model.addSelection(selection);
        selP = panel.getLastChildPanel();
        selP.assign(new Column(number, firstLineWithData));
        if (shouldAddDateAndTime(selection)) {
            final DateAndTime dtm = new DateAndTime();
            dtm.setGroup(Integer.toString(model.getMarkedColumn() + 1));
            ModelStore.getInstance().add(dtm);
        }
        //
        // when having reached the last column, merge positions and date&time
        if (model.getMarkedColumn() + 1 ==
                tabCtrlr.getColumnCount()) {
            final DateAndTimeController dtc = new DateAndTimeController();
            dtc.mergeDateAndTimes();
            //
            final PositionController pc = new PositionController();
            pc.mergePositions();
        }
        // TODO if being date&time or position column: add group to table heading
        tabCtrlr.setColumnHeading(number, selection.get(0));
        tabCtrlr.clearMarkedTableElements();
        tabCtrlr.setTableSelectionMode(TableController.CELLS);
        tabCtrlr.turnSelectionOn();
        panel = null;

        LOG.debug("End:");
        LOG.debug("{}: {}", model.getClass().getSimpleName(), model);
    }

    @Override
    public void back() {
        final List<String> selection = new ArrayList<>();
        panel.store(selection);
        model.addSelection(selection);
        final int number = model.getMarkedColumn() - 1;
        if (number >= 0) {
            // TODO if being date&time or position column: add group to table heading
            tabCtrlr.setColumnHeading(number, selection.get(0));
            tabCtrlr.clearMarkedTableElements();
            tabCtrlr.setTableSelectionMode(TableController.CELLS);
            tabCtrlr.turnSelectionOn();
        }
        panel = null;
    }

    @Override
    public StepController getNextStepController() {
        return new Step4aController(new Step4aModel(null, model.getFirstLineWithData()));
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    @Override
    public boolean isFinished() {
        final List<String> currentSelection = new ArrayList<>();
        panel.store(currentSelection);
        // show info that name value is missing or too short
        if (isSelectionOfTypeAndSubParameterSet(currentSelection, Lang.l().step3ColTypeOmParameter(), 2)) {
            showInvalidSelectionParameterInput(currentSelection.get(2), Lang.l().step3OmParameterNameLabel());
            return false;
        }
        // check if feature column with checked parent feature has an identifier value
        if (isSelectionOfTypeAndSubParameterSet(currentSelection, Lang.l().featureOfInterest(), 1)) {
            showInvalidSelectionParameterInput(currentSelection.get(1), Lang.l().step3ParentFeatureIdentifierLabel());
            return false;
        }
        // check if the current column is the last in the file
        // if yes, check for at least one measured value column
        if (model.getMarkedColumn() + 1 ==
                TableController.getInstance().getColumnCount() &&
                ModelStore.getInstance().getMeasuredValues().isEmpty() &&
                !currentSelection.get(0).equalsIgnoreCase(Lang.l().measuredValue())) {
            JOptionPane.showMessageDialog(null,
                    Lang.l().step3MeasureValueColMissingDialogMessage(),
                    Lang.l().step3MeasureValueColMissingDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public StepController getNext() {
        // check if we have reached the last column
        // if not, return a new Step3aController
        final int nextColumn = model.getMarkedColumn() + 1;
        if (nextColumn == tabCtrlr.getColumnCount()) {
            return null;
        }
        return new Step3Controller(nextColumn,
                model.getFirstLineWithData(),
                model.getUseHeader());
    }

    @Override
    public boolean isStillValid() {
        //TODO: check whether the CSV file parsing settings have been changed
        return model.getMarkedColumn() != 0;
    }

    @Override
    public StepModel getModel() {
        return model;
    }

    private boolean shouldAddDateAndTime(final List<String> selection) {
        return selection.size() > 1 && selection.get(1) != null && !selection.get(1).isEmpty() &&
                (selection.get(1).equals(Lang.l().step3DateAndTimeUnixTime()) ||
                selection.get(1).equals(Lang.l().step3DateAndTimeCombination()) &&
                        selection.get(2).endsWith(NULL));
    }

    private void showInvalidSelectionParameterInput(final String givenValue,
            final String parameterIdentifier) {
        JOptionPane.showMessageDialog(panel,
                Lang.l().step3InvalidSelectionParameterDialogMessage(parameterIdentifier, givenValue),
                Lang.l().step3InvalidSelectionParameterDialogTitle(parameterIdentifier),
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean isSelectionOfTypeAndSubParameterSet(final List<String> currentSelection,
            final String typ,
            final int subParamIndex) {
        return !currentSelection.isEmpty() &&
                currentSelection.get(0).equals(typ) &&
                        currentSelection.size() == subParamIndex + 1 && (
                        currentSelection.get(subParamIndex) == null ||
                        currentSelection.get(subParamIndex).isEmpty() ||
                        currentSelection.get(subParamIndex).trim().length() <= 3);
    }

    private void logAttributes() {
        LOG.debug("{}: {}", model.getClass().getSimpleName(), model);
        LOG.debug("{}: [{}]", panel.getClass().getSimpleName(), panel != null? panel.hashCode() : NULL);
    }
}
