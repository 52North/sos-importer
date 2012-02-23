package org.n52.sos.importer.interfaces;

import javax.swing.JPanel;

public abstract class StepController {

	private static final long serialVersionUID = 1L;
	
	/**
	 * called when the step controller is newly initialized
	 * or when the Back button is pressed, loads settings 
	 * from the model and creates the view
	 */
	public abstract void loadSettings();
	
	/**
	 * called when the Next button is pressed,
	 * checks if all information has been collected for this step
	 */
	public abstract boolean isFinished();
	
	/**
	 * called when the Next button is pressed and the step is 
	 * finished, saves all settings of this step in the model
	 * and releases all views
	 */
	public abstract void saveSettings();
	
	/**
	 * returns a short description of this step to be displayed
	 * on the description panel of the main frame
	 */
	public abstract String getDescription();
	
	/**
	 * returns the corresponding step panel
	 */
	public abstract JPanel getStepPanel();
	
	/**
	 * returns the controller for the next step
	 */
	public abstract StepController getNextStepController();
	
	/**
	 * checks before loading the settings if this 
	 * step is needed, if not it will be skipped
	 * @return
	 */
	public abstract boolean isNecessary();
	
	/**
	 * returns a StepController of the same type
	 * or null when this step is finished
	 * @return
	 */
	public abstract StepController getNext();
	
	/**
	 * contains actions when back button was pressed
	 */
	public void back() {	
	}
	
	/**
	 * checks if all conditions for this step controller 
	 * which has been already been displayed are up to date
	 */
	public boolean isStillValid() {
		return false;
	}
}
