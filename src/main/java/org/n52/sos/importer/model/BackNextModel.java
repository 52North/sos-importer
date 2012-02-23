package org.n52.sos.importer.model;

import java.util.Stack;

import org.n52.sos.importer.interfaces.StepController;

/**
 * stores past, future and present step controller
 * @author Raimund
 *
 */
public class BackNextModel {

	private Stack<StepController> previousStepControllers;
	
	private Stack<StepController> followingStepControllers;
	
	private StepController currentStepController;
	
	public BackNextModel() {
		previousStepControllers = new Stack<StepController>();
		followingStepControllers = new Stack<StepController>();
	}
	
	public StepController getPreviousStepController() {
		return previousStepControllers.pop();
	}
	
	public void addPreviousStepController(StepController sc) {
		previousStepControllers.push(sc);
	}

	public void setCurrentStepController(StepController currentStepController) {
		this.currentStepController = currentStepController;
	}

	public StepController getCurrentStepController() {
		return currentStepController;
	}
	
	public StepController getFollowingStepController() {
		if (followingStepControllers.size() == 0) return null;
		return followingStepControllers.pop();
	}
	
	public void addFollowingStepController(StepController sc) {
		followingStepControllers.push(sc);
	}
}
