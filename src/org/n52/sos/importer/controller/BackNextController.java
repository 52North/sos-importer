package org.n52.sos.importer.controller;

import org.n52.sos.importer.view.BackNextPanel;

public class BackNextController {
	
	private static BackNextController instance = null;

	private StepController stepController;
	
	private BackNextPanel backNextPanel = BackNextPanel.getInstance();
	
	private BackNextController() {
	}

	public static BackNextController getInstance() {
		if (instance == null)
			instance = new BackNextController();
		return instance;
	}
	
	public void setStepController(StepController stepController) {
		this.stepController = stepController;
	}
	
	public void setBackButtonEnabled(boolean flag) {
		backNextPanel.setBackButtonEnabled(flag);
	}
	
	public void backButtonPressed() {
		if (stepController != null)
			stepController.back();
	}
	
	public void nextButtonClicked() {
		if (stepController != null)
			stepController.next();
	}
}
