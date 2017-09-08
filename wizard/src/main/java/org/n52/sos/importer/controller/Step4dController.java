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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4dModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step4Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In the case of multiple measured value column, each om:parameter has to be assigned to a measured value column.
 *
 * TODO implement analogous to {@link Step4bController}
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class Step4dController extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step4dController.class);

    private int firstLineWithData = -1;

    private TableController tableController;

    private Step4dModel model;

    private Step4Panel panel;

    public Step4dController(final int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
        tableController = TableController.getInstance();
    }

    public Step4dController(final int firstLineWithData, final Step4dModel model) {
        this(firstLineWithData);
        this.model = model;
    }

    @Override
    public void loadSettings() {
        OmParameter parameter = model.getOmParameter();
        int[] seletedColumns = model.getSelectedColumns();
        model.setOrientation(tableController.getOrientationString());
        String text = model.getDescription();
        LOG.debug("Text: '{}'", text);

        panel = new Step4Panel(text);
        tableController.setTableSelectionMode(TableController.COLUMNS);
        tableController.addMultipleSelectionListener(new SelectionChanged());

        for (final int number: seletedColumns) {
            final Column column = new Column(number, firstLineWithData);
            final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(column);
            parameter.unassign(mv);
            tableController.selectColumn(number);
        }

        parameter.getTableElement().mark();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void saveSettings() {
        final OmParameter parameter = model.getOmParameter();
        final int[] selectedColumns = tableController.getSelectedColumns();
        model.setSelectedColumns(selectedColumns);
        for (int i : selectedColumns) {
            MeasuredValue measuredValue = ModelStore.getInstance().getMeasuredValueAt(new Column(i, firstLineWithData));
            parameter.assign(measuredValue);
        }

        tableController.clearMarkedTableElements();
        tableController.deselectAllColumns();
        tableController.setTableSelectionMode(TableController.CELLS);
        tableController.removeMultipleSelectionListener();

        panel = null;
    }

    @Override
    public String getDescription() {
        return Lang.l().step4bDescription().replace("4b", "4d");
    }

    @Override
    public JPanel getStepPanel() {
        return panel;
    }

    @Override
    public StepController getNextStepController() {
        return new Step5aController(firstLineWithData);
    }

    @Override
    public boolean isNecessary() {
        if (ModelStore.getInstance().getOmParameters().size() < 1) {
            LOG.info("Skip step 4d because not om:Parameter columns defined.");
            return false;
        }
        if (ModelStore.getInstance().getMeasuredValues().size() < 2) {
            LOG.info("Skip step 4d because only one measured value column.");
            return false;
        }
        return true;
    }

    @Override
    public StepController getNext() {
        OmParameter nextParameter = getNextUnassignedOmParameter();
        if (nextParameter != null) {
            return new Step4dController(firstLineWithData,
                    new Step4dModel(nextParameter));
        }
        return null;
    }

    private OmParameter getNextUnassignedOmParameter() {
        for (OmParameter parameter : ModelStore.getInstance().getOmParameters()) {
            if (!parameter.isAssigned()) {
                return parameter;
            }
        }
        return null;
    }

    @Override
    public StepModel getModel() {
        return model;
    }

    /** {@inheritDoc} */
    @Override
    public void back() {
        tableController.clearMarkedTableElements();
        tableController.deselectAllColumns();
        tableController.setTableSelectionMode(TableController.CELLS);
        tableController.removeMultipleSelectionListener();

        panel = null;
    }

    private class SelectionChanged implements TableController.MultipleSelectionListener {

        @Override
        public void columnSelectionChanged(final int[] selectedColumns) {
            for (final int number: selectedColumns) {
                final Column column = new Column(number, firstLineWithData);
                final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(column);
                if (mv == null) {
                    JOptionPane.showMessageDialog(null,
                            Lang.l().step4bInfoNotMeasuredValue(),
                            Lang.l().infoDialogTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                    tableController.deselectColumn(number);
                    return;
                }

                final OmParameter parameter = model.getOmParameter();
                if (parameter.isAssigned(mv)) {
                    JOptionPane.showMessageDialog(null,
                            Lang.l().step4bInfoResoureAlreadySet(parameter),
                            Lang.l().infoDialogTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                    tableController.deselectColumn(number);
                    return;
                }
            }
        }

        @Override
        public void rowSelectionChanged(final int[] selectedRows) {
            // Do nothing -> only columns are used.
        }
    }

}
