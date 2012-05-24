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

import javax.swing.JPanel;

import org.n52.sos.importer.model.StepModel;

/**
 * @author r.schnuerer@uni-muenster.de
 * <br />
 * A StepController is the controller for a step in the workflow. 
 * It holds a model and a <code>StepPanel</code> for its step.
 *
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
	 */
	public abstract boolean isFinished();
	
	/**
	 * Sets the missing values in the step model using a method of GUI elements.
	 * <br />Called when the Next button is pressed and the step is 
	 * finished, saves all settings of this step in the model
	 * and releases all views.
	 * <br />Called before {@link #getNextStepController()} in {@link org.n52.sos.importer.controller.BackNextController#nextButtonClicked()}
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
	 * Returns the controller for the next step:<br />
	 * n.? &rarr; (n+1).a
	 * @return a {@link org.n52.sos.importer.controller.StepController}
	 */
	public abstract StepController getNextStepController();
	
	/**
	 * checks before loading the settings if this 
	 * step is needed, if not it will be skipped
	 * @return <code>true</code>, if this step is required by the current 
	 * 			set-up, else <code>false</code>.
	 */
	public abstract boolean isNecessary();
	
	/**
	 * returns a StepController of the same type (n.a &rarr; n.b) or 
	 * <b><code>null</code></b> when this step is finished and the next step 
	 * level can be reached (n.a &rarr; (n+1).a).
	 * @return a {@link org.n52.sos.importer.controller.StepController} or
	 * 		<b><code>null</code></b>
	 */
	public abstract StepController getNext();
	
	/**
	 * contains actions when back button was pressed. Default is do nothing.
	 */
	public void back() {}
	
	/**
	 * checks if all conditions for this step controller 
	 * which has been already been displayed are up to date
	 */
	public boolean isStillValid() {	return false; }
	
	/**
	 * Returns the model of this step
	 * @return
	 */
	public abstract StepModel getModel();
}
