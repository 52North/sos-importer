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

import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.view.BackNextPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * controls the actions when the back or next button is pressed
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class BackNextController {

	private static BackNextController instance = null;

	private static final Logger logger = LoggerFactory.getLogger(BackNextController.class);

	private BackNextModel bNModel = null;

	private BackNextPanel backNextPanel = null;

	private BackNextController() {
		bNModel = new BackNextModel();
		backNextPanel = BackNextPanel.getInstance();
	}

	/**
	 * <p>Getter for the field <code>instance</code>.</p>
	 *
	 * @return a {@link org.n52.sos.importer.controller.BackNextController} object.
	 */
	public static BackNextController getInstance() {
		if (instance == null) {
			instance = new BackNextController();
		}
		return instance;
	}

	/**
	 * <p>setBackButtonVisible.</p>
	 *
	 * @param isBackButtonVisible a boolean.
	 */
	public void setBackButtonVisible(final boolean isBackButtonVisible) {
		backNextPanel.setBackButtonVisible(isBackButtonVisible);
	}

	/**
	 * Change label of next button to become a finish button
	 */
	public void changeNextToFinish() {
		backNextPanel.changeNextToFinish();
	}

	/**
	 * <p>changeFinishToNext.</p>
	 */
	public void changeFinishToNext() {
		backNextPanel.changeFinishToNext();
	}

	/**
	 * <p>setFinishButtonEnabled.</p>
	 *
	 * @param isFinishButtonEnabled a boolean.
	 */
	public void setFinishButtonEnabled(final boolean isFinishButtonEnabled) {
		backNextPanel.setFinishButtonEnabled(isFinishButtonEnabled);
	}

	/**
	 * <p>setNextButtonEnabled.</p>
	 *
	 * @param isNextButtonEnabled a boolean.
	 */
	public void setNextButtonEnabled(final boolean isNextButtonEnabled) {
		backNextPanel.setNextButtonEnabled(isNextButtonEnabled);
	}

	/**
	 * <p>backButtonClicked.</p>
	 */
	public void backButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("backButtonClicked()\n\n");
		}
		final StepController currentSC = bNModel.getCurrentStepController();
		currentSC.back();
		bNModel.addFollowingStepController(currentSC);
		final StepController previousSC = bNModel.getPreviousStepController();
		final MainController mC = MainController.getInstance();
		mC.setStepController(previousSC);
		mC.removeProvider(currentSC.getModel());
	}

	/**
	 * <p>nextButtonClicked.</p>
	 */
	public void nextButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("nextButtonClicked()\n\n");
		}
		final StepController currentSC = bNModel.getCurrentStepController();
		// handle potential language switch
		if(currentSC instanceof Step1Controller) {
			backNextPanel = BackNextPanel.getInstance();
		}
		final MainController mC = MainController.getInstance();
		//
		if (!currentSC.isFinished()) {
			return;
		}
		//
		currentSC.saveSettings();

		//show "back" button
		BackNextController.getInstance().setBackButtonVisible(true);

		// update the XML model, too
		mC.updateModel();
		// put controller on stack
		bNModel.addPreviousStepController(currentSC);
		//
		// when has already been to the next step
		final StepController followingSC = bNModel.getFollowingStepController();
		if (followingSC != null && followingSC.isStillValid()) {
			mC.setStepController(followingSC);
		} else {
			//
			// used to get 4a,b,c,d steps before getting 5a, for example
			//
			// next step controller of this type
			StepController nextSC = currentSC.getNext();
			if (nextSC != null) {
				mC.setStepController(nextSC);
			} else {
				// When is this code called? In which situation?
				// When step n+1 is called after step n
				//
				// next step controller of another type
				nextSC = currentSC.getNextStepController();

				while (!nextSC.isNecessary()) {
					nextSC = nextSC.getNextStepController();
				}
				//
				mC.setStepController(nextSC);
			}
		}
		mC.removeProvider(currentSC.getModel());
	}

	/**
	 * <p>finishButtonClicked.</p>
	 */
	public void finishButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("finishButtonClicked()\n\n");
		}
		MainController.getInstance().exit();
	}

	/**
	 * <p>getModel.</p>
	 *
	 * @return a {@link org.n52.sos.importer.model.BackNextModel} object.
	 */
	public BackNextModel getModel() {
		return bNModel;
	}

	/**
	 * At least only required by Step 1 during the language switch.
	 */
	public void restartCurrentStep() {
		final StepController currentSC = bNModel.getCurrentStepController();
		final MainController mc = MainController.getInstance();
		mc.setStepController(currentSC);
		// Update Frame-Title if possible
		if (currentSC instanceof Step1Controller) {
			final Step1Controller s1C = (Step1Controller) currentSC;
			final Step1Model s1M = (Step1Model) s1C.getModel();
			mc.updateTitle(s1M.getCSVFilePath());
			// Update Back-Next-Buttons
			backNextPanel = BackNextPanel.getInstance();
		}

	}
}
