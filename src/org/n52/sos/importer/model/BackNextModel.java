package org.n52.sos.importer.model;

import java.util.Stack;

import org.n52.sos.importer.controller.StepController;

public class BackNextModel {

	private Stack<StepController> stepControllers;
	
	private StepController currentStepController;
	
	public BackNextModel() {
		stepControllers = new Stack<StepController>();
	}
	
	public StepController getPreviousStepController() {
		return stepControllers.pop();
	}
	
	public void addStepController(StepController sc) {
		stepControllers.push(sc);
	}

	public void setCurrentStepController(StepController currentStepController) {
		this.currentStepController = currentStepController;
	}

	public StepController getCurrentStepController() {
		return currentStepController;
	}
}
