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
