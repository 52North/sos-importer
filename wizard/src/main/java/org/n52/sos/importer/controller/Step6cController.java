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

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step6cPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lets the user choose the position for each feature of interest
 * (either stored in a column or manually selected)
 * in case there are not any positions given in the CSV file
 *
 * @author Raimund
 * FIXME already defined positions are not identified
 */
public class Step6cController extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step6cController.class);

    private Step6cModel step6cModel;

    private Step6cPanel step6cPanel;

    /**
     * <p>Constructor for Step6cController.</p>
     */
    public Step6cController() { }

    /**
     * <p>Constructor for Step6cController.</p>
     *
     * @param step6cModel a {@link org.n52.sos.importer.model.Step6cModel} object.
     */
    public Step6cController(final Step6cModel step6cModel) {
        this.step6cModel = step6cModel;
    }

    @Override
    public void loadSettings() {
        final FeatureOfInterest foi = step6cModel.getFeatureOfInterest();
        String name = step6cModel.getFeatureOfInterestName();
        // TODO is this assumption still valid? better check for TableElement?!
        //when this feature is not contained in the table
        if (name == null) {
            name = foi.getNameString();
            foi.unassignPosition();
        } else {
            foi.removePositionFor(name);
        }

        final String description = step6cModel.getDescription();
        step6cPanel = new Step6cPanel(description,
                // to indicate to the user, that this resource is generated
                foi.isGenerated() ? Lang.l().generated() + ": " + name : name,
                step6cModel);
        step6cPanel.loadSettings();
    }

    @Override
    public void saveSettings() {
        step6cPanel.saveSettings();

        final String name = step6cModel.getFeatureOfInterestName();
        final Position position = step6cModel.getPosition();
        //when this feature is not contained in the table
        if (name == null) {
            step6cModel.getFeatureOfInterest().assignPosition(position);
        } else {
            step6cModel.getFeatureOfInterest().setPositionFor(name, position);
        }
        step6cPanel = null;
    }


    @Override
    public String getDescription() {
        return Lang.l().step6cDescription();
    }

    @Override
    public JPanel getStepPanel() {
        return step6cPanel;
    }

    @Override
    public StepController getNext() {
        final Step6cModel foiWithoutPosition = getNextFeatureOfInterestWithUnCompletePosition();
        if (foiWithoutPosition == null) {
            return null;
        }

        return new Step6cController(foiWithoutPosition);
    }

    @Override
    public StepController getNextStepController() {
        return new Step7Controller();
    }

    private Step6cModel getNextFeatureOfInterestWithUnCompletePosition() {
        final List<FeatureOfInterest> featureOfInterests = ModelStore.getInstance().getFeatureOfInterests();
        // TODO check if the current foi is in the no data area of the column
        for (final FeatureOfInterest foi: featureOfInterests) {

            if (foi.getTableElement() == null) {
                // FIXME implement handling of generated fois
                if (foi.getPosition() == null) {
                    return new Step6cModel(foi);
                    //otherwise the feature has already a position
                }
            } else {
                if (foi.getPosition() == null) {
                    for (final String name: foi.getTableElement().getValues()) {

                        final Position p = foi.getPositionFor(name);
                        if (p == null) {
                            return new Step6cModel(foi, name);
                        }
                        //otherwise the feature in this row/column has already a position
                    }
                }
                //otherwise the feature row/column has already a position row/column
            }
        }

        return null;
    }

    @Override
    public boolean isNecessary() {
        step6cModel = getNextFeatureOfInterestWithUnCompletePosition();
        if (step6cModel != null) {
            return true;
        }

        LOG.info("Skip Step 6c since there is at least one position");
        return false;
    }

    @Override
    public boolean isFinished() {
        return step6cPanel.isFinished();
    }

    @Override
    public StepModel getModel() {
        return step6cModel;
    }

}
