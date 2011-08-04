package org.n52.sos.importer.controller;

import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.view.BackNextPanel;

/**
 * controls the actions when the back or next button is pressed
 * @author Raimund
 *
 */
public class BackNextController {
	
	private static BackNextController instance = null;

	private BackNextModel backNextModel = new BackNextModel();
	
	private BackNextPanel backNextPanel = BackNextPanel.getInstance();
	
	private BackNextController() {
	}

	public static BackNextController getInstance() {
		if (instance == null)
			instance = new BackNextController();
		return instance;
	}
	
	public void setBackButtonVisible(boolean aFlag) {
		backNextPanel.setBackButtonVisible(aFlag);
	}
	
	public void changeNextToFinish() {
		backNextPanel.changeNextToFinish();
	}
	
	public void changeFinishToNext() {
		backNextPanel.changeFinishToNext();
	}
	
	public void setFinishButtonEnabled(boolean aFlag) {
		backNextPanel.setFinishButtonEnabled(aFlag);
	}
	
	public void backButtonPressed() {
		StepController currentSC = backNextModel.getCurrentStepController();
		currentSC.back();
		backNextModel.addFollowingStepController(currentSC);
		StepController previousSC = backNextModel.getPreviousStepController();
		MainController.getInstance().setStepController(previousSC);
	}
	
	public void nextButtonClicked() {
		StepController currentSC = backNextModel.getCurrentStepController();
		if (!currentSC.isFinished()) return;
		
		currentSC.saveSettings();
		backNextModel.addPreviousStepController(currentSC);	//put controller on stack
		
		//when has already been to the next step
		StepController followingSC = backNextModel.getFollowingStepController();
		if (followingSC != null && followingSC.isStillValid()) {
			MainController.getInstance().setStepController(followingSC);
			return;
		}
		
		//next step controller of this type	
		StepController nextSC = currentSC.getNext(); 
		if (nextSC != null) {
			MainController.getInstance().setStepController(nextSC);
			return;
		}

		//next step controller of another type
		nextSC = currentSC.getNextStepController();		
		
		while (!nextSC.isNecessary())
			nextSC = nextSC.getNextStepController();

		MainController.getInstance().setStepController(nextSC);
	}
	
	public void finishButtonClicked() {
		MainController.getInstance().exit();
	}
	
	public BackNextModel getModel() {
		return backNextModel;
	}
}
