/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.controller;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.view.BackNextPanel;

/**
 * controls the actions when the back or next button is pressed
 * @author Raimund
 *
 */
public class BackNextController {
	
	private static BackNextController instance = null;
	
	private static final Logger logger = Logger.getLogger(BackNextController.class);
	
	private BackNextModel bNModel = null;
	
	private BackNextPanel backNextPanel = null;
	
	private BackNextController() {
		this.bNModel = new BackNextModel();
		this.backNextPanel = BackNextPanel.getInstance();
	}

	public static BackNextController getInstance() {
		if (instance == null)
			instance = new BackNextController();
		return instance;
	}
	
	public void setBackButtonVisible(boolean isBackButtonVisible) {
		this.backNextPanel.setBackButtonVisible(isBackButtonVisible);
	}
	
	/**
	 * Change label of next button to become a finish button
	 */
	public void changeNextToFinish() {
		this.backNextPanel.changeNextToFinish();
	}
	
	public void changeFinishToNext() {
		this.backNextPanel.changeFinishToNext();
	}
	
	public void setFinishButtonEnabled(boolean isFinishButtonEnabled) {
		this.backNextPanel.setFinishButtonEnabled(isFinishButtonEnabled);
	}
	
	public void setNextButtonEnabled(boolean isNextButtonEnabled) {
		this.backNextPanel.setNextButtonEnabled(isNextButtonEnabled);
	}
	
	public void backButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("backButtonClicked()\n\n");
		}
		StepController currentSC = this.bNModel.getCurrentStepController();
		currentSC.back();
		this.bNModel.addFollowingStepController(currentSC);
		StepController previousSC = this.bNModel.getPreviousStepController();
		MainController mC = MainController.getInstance();
		mC.setStepController(previousSC);
		mC.removeProvider(currentSC.getModel());
	}
	
	public void nextButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("nextButtonClicked()\n\n");
		}
		StepController currentSC = this.bNModel.getCurrentStepController();
		// handle potential language switch
		if(currentSC instanceof Step1Controller) {
			this.backNextPanel = BackNextPanel.getInstance();
		}
		MainController mC = MainController.getInstance();
		//
		if (!currentSC.isFinished()) {
			return;
		}
		//
		currentSC.saveSettings();
		// update the XML model, too
		mC.updateModel();
		// put controller on stack
		this.bNModel.addPreviousStepController(currentSC);
		//
		// when has already been to the next step
		StepController followingSC = this.bNModel.getFollowingStepController();
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
	
	public void finishButtonClicked() {
		if (logger.isTraceEnabled()) {
			logger.trace("finishButtonClicked()\n\n");
		}
		MainController.getInstance().exit();
	}
	
	public BackNextModel getModel() {
		return this.bNModel;
	}

	/**
	 * At least only required by Step 1 during the language switch.
	 * 
	 */
	public void restartCurrentStep() {
		StepController currentSC = this.bNModel.getCurrentStepController();
		MainController mc = MainController.getInstance();
		mc.setStepController(currentSC);
		// Update Frame-Title if possible
		if (currentSC instanceof Step1Controller) {
			Step1Controller s1C = (Step1Controller) currentSC;
			Step1Model s1M = (Step1Model) s1C.getModel();
			mc.updateTitle(s1M.getCSVFilePath());
			// Update Back-Next-Buttons
			this.backNextPanel = BackNextPanel.getInstance();
		}

	}
}
