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
 * @version $Id: $Id
 */
public class Step3Controller extends StepController {

    /**
     * 
     */
    private static final String STEP3_MODEL = "Step3Model: ";

    /**
     * 
     */
    private static final String STEP3_PANEL = "Step3Panel: ";

    /**
     * 
     */
    private static final String NULL_STRING = "null";

    private static final Logger logger = LoggerFactory.getLogger(Step3Controller.class);

    private Step3Panel step3Panel;

    /**
     * Step3Model of this Step3Controllers
     */
    private final Step3Model step3Model;

    /**
     * reference to TableController singleton instance
     */
    private final TableController tabCtrlr = TableController.getInstance();

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
        step3Model = new Step3Model(currentColumn,
                firstLineWithData,
                useHeader);
        step3Panel = new Step3Panel(firstLineWithData);
    }


    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return Lang.l().step3aDescription();
    }

    /** {@inheritDoc} */
    @Override
    public JPanel getStepPanel() {
        return step3Panel;
    }

    /** {@inheritDoc} */
    @Override
    public void loadSettings() {
        if (logger.isTraceEnabled()) {
            logger.trace("loadSettings()");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(STEP3_MODEL + step3Model);
            logger.debug(STEP3_PANEL + (step3Panel != null
                    ? "[" + step3Panel.hashCode() + "]"
                    : NULL_STRING));
        }
        final int number = step3Model.getMarkedColumn();
        if (logger.isDebugEnabled()) {
            logger.debug("Loading settings for column# " + number);
        }
        final int fLWData = step3Model.getFirstLineWithData();
        final Column column = new Column(number, fLWData);
        final List<String> selection = step3Model.getSelectionForColumn(number);
        if (step3Panel == null) {
            step3Panel = new Step3Panel(step3Model.getFirstLineWithData());
        }
        if (selection != null) {
            step3Panel.restore(selection);
        }
        step3Panel.getLastChildPanel().unAssign(column);

        tabCtrlr.mark(column);
        tabCtrlr.setColumnHeading(number, "???");
        tabCtrlr.setTableSelectionMode(TableController.COLUMNS);
        tabCtrlr.turnSelectionOff();
    }

    /** {@inheritDoc} */
    @Override
    public void saveSettings() {
        if (logger.isTraceEnabled()) {
            logger.trace("saveSettings()");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Start:");
            logger.debug(STEP3_MODEL + step3Model);
            logger.debug(STEP3_PANEL + (step3Panel != null
                    ? "[" + step3Panel.hashCode() + "]"
                    : NULL_STRING));
        }
        //
        final List<String> selection = new ArrayList<String>();
        SelectionPanel selP;
        final int number = step3Model.getMarkedColumn();
        final int firstLineWithData = step3Model.getFirstLineWithData();
        //
        step3Panel.store(selection);
        step3Model.addSelection(selection);
        selP = step3Panel.getLastChildPanel();
        selP.assign(new Column(number, firstLineWithData));
        if (selection.size() == 2 && selection.get(1).equals("UNIX time")) {
            DateAndTime dtm = new DateAndTime();
            dtm.setGroup("1");
            ModelStore.getInstance().add(dtm);
        }
        //
        // when having reached the last column, merge positions and date&time
        if (step3Model.getMarkedColumn() + 1 ==
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
        step3Panel = null;

        if (logger.isDebugEnabled()) {
            logger.debug("End:");
            logger.debug(STEP3_MODEL + step3Model);
            logger.debug(STEP3_PANEL + (step3Panel != null
                    ? "[" + step3Panel.hashCode() + "]"
                    : NULL_STRING));
        }

    }

    /** {@inheritDoc} */
    @Override
    public void back() {
        final List<String> selection = new ArrayList<String>();
        step3Panel.store(selection);
        step3Model.addSelection(selection);
        final int number = step3Model.getMarkedColumn() - 1;
        if (number >= 0) {
            // TODO if being date&time or position column: add group to table heading
            tabCtrlr.setColumnHeading(number, selection.get(0));
            tabCtrlr.clearMarkedTableElements();
            tabCtrlr.setTableSelectionMode(TableController.CELLS);
            tabCtrlr.turnSelectionOn();
        }
        step3Panel = null;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNextStepController() {
        return new Step4aController(new Step4aModel(null, step3Model.getFirstLineWithData()));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNecessary() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinished() {
        final List<String> currentSelection = new ArrayList<String>();
        step3Panel.store(currentSelection);
        // check if the current column is the last in the file
        // if yes, check for at least one measured value column
        if ((step3Model.getMarkedColumn() + 1) ==
                TableController.getInstance().getColumnCount() &&
                ModelStore.getInstance().getMeasuredValues().size() == 0 &&
                !currentSelection.get(0).equalsIgnoreCase(Lang.l().measuredValue())) {
            JOptionPane.showMessageDialog(null,
                    Lang.l().step3aMeasureValueColMissingDialogMessage(),
                    Lang.l().step3aMeasureValueColMissingDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNext() {
        // check if we have reached the last column
        // if not, return a new Step3aController
        final int nextColumn = step3Model.getMarkedColumn() + 1;
        if (nextColumn == tabCtrlr.getColumnCount()) {
            return null;
        }
        return new Step3Controller(nextColumn,
                step3Model.getFirstLineWithData(),
                step3Model.getUseHeader());
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStillValid() {
        //TODO: check whether the CSV file parsing settings have been changed
        if (step3Model.getMarkedColumn() == 0) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public StepModel getModel() {
        return step3Model;
    }
}
