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

import javax.swing.JPanel;

import org.n52.sos.importer.model.StepModel;

/**
 * <p>Abstract StepController class.</p>
 *
 * @author r.schnuerer@uni-muenster.de
 * <br>
 * A StepController is the controller for a step in the workflow.
 * It holds a model and a <code>StepPanel</code> for its step.
 * @version $Id: $Id
 */
public abstract class StepController {

    /**
     * called when the step controller is newly initialized
     * or when the Back button is pressed, loads settings
     * from the model and creates the view
     */
    public abstract void loadSettings();

    /**
     * called when the Next button is pressed,
     * checks if all information has been collected for this step
     *
     * @return a boolean.
     */
    public abstract boolean isFinished();

    /**
     * Sets the missing values in the step model using a method of GUI elements.
     * <br>
     * Called when the Next button is pressed and the step is
     * finished, saves all settings of this step in the model
     * and releases all views.
     * <br>
     * Called before {@link #getNextStepController()} in
     * {@link org.n52.sos.importer.controller.BackNextController#nextButtonClicked()}
     */
    public abstract void saveSettings();

    /**
     * returns a short description of this step to be displayed
     * on the description panel of the main frame
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String getDescription();

    /**
     * returns the corresponding step panel
     *
     * @return a {@link javax.swing.JPanel} object.
     */
    public abstract JPanel getStepPanel();

    /**
     * Returns the controller for the next step:<br>
     * n.? &rarr; (n+1).a
     *
     * @return a {@link org.n52.sos.importer.controller.StepController}
     */
    public abstract StepController getNextStepController();

    /**
     * checks before loading the settings if this
     * step is needed, if not it will be skipped
     *
     * @return <code>true</code>, if this step is required by the current
     *          set-up, else <code>false</code>.
     */
    public abstract boolean isNecessary();

    /**
     * returns a StepController of the same type (n.a &rarr; n.b) or
     * <b><code>null</code></b> when this step is finished and the next step
     * level can be reached (n.a &rarr; (n+1).a).
     *
     * @return a {@link org.n52.sos.importer.controller.StepController} or
     *      <b><code>null</code></b>
     */
    public abstract StepController getNext();

    /**
     * contains actions when back button was pressed. Default is do nothing.
     */
    public void back() {}

    /**
     * checks if all conditions for this step controller
     * which has been already been displayed are up to date
     *
     * @return a boolean.
     */
    public boolean isStillValid() {
        return false;
    }

    /**
     * Returns the model of this step
     *
     * @return a {@link org.n52.sos.importer.model.StepModel} object.
     */
    public abstract StepModel getModel();
}
