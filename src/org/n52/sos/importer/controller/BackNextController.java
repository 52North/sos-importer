package org.n52.sos.importer.controller;

import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.view.BackNextPanel;

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
	
	public void setBackButtonEnabled(boolean flag) {
		backNextPanel.setBackButtonEnabled(flag);
	}
	
	public void backButtonPressed() {
		StepController previousSC = backNextModel.getPreviousStepController();
		MainController.getInstance().setStepController(previousSC);
	}
	
	public void nextButtonClicked() {
		StepController currentSC = backNextModel.getCurrentStepController();
		currentSC.saveSettings();
		
		StepController nextSC = currentSC.getNextStepController();
		if (nextSC == null) return;
		
		backNextModel.addStepController(currentSC);	
		
		MainController.getInstance().setStepController(nextSC);
	}
	
	public BackNextModel getModel() {
		return backNextModel;
	}
}
