package org.n52.sos.importer.controller;

import javax.swing.JPanel;

public abstract class StepController {

	private static final long serialVersionUID = 1L;
	
	/**
	 * called when Back is pressed or
	 * when step controller is newly initialized
	 */
	public abstract void loadSettings();
	
	/**
	 * called when Next is pressed
	 */
	public abstract void saveSettings();
	
	public abstract String getDescription();
	
	public abstract JPanel getStepPanel();
	
	/**
	 * returns the controller for the next step
	 * and null when this step is not finished yet
	 * @return
	 */
	public abstract StepController getNextStepController();
}
