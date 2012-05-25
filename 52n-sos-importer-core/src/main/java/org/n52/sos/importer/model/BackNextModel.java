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

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.StepController;

/**
 * stores past, future and present step controller
 * @author Raimund
 *
 */
public class BackNextModel {

	private Stack<StepController> previousStepControllers;
	
	private static final Logger logger = Logger.getLogger(BackNextModel.class);
	
	private Stack<StepController> followingStepControllers;
	
	private StepController currentStepController;
	
	public BackNextModel() {
		previousStepControllers = new Stack<StepController>();
		followingStepControllers = new Stack<StepController>();
	}
	
	public StepController getPreviousStepController() {
		if (logger.isTraceEnabled()) {
			logger.trace("getPreviousStepController()");
		}
		StepController sc = previousStepControllers.pop();
		if (logger.isTraceEnabled()) {
			logger.trace("result: " + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		return sc;
	}
	
	public void addPreviousStepController(StepController sc) {
		if (logger.isTraceEnabled()) {
			logger.trace("addPreviousStepController(" + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		previousStepControllers.push(sc);
	}

	public void setCurrentStepController(StepController currentSC) {
		if (logger.isTraceEnabled()) {
			logger.trace("setCurrentStepController(" + 
					currentSC.getClass().getSimpleName() + 
					"[" + currentSC.hashCode() + "])");
		}
		this.currentStepController = currentSC;
	}

	public StepController getCurrentStepController() {
		if (logger.isTraceEnabled()) {
			logger.trace("getCurrentStepController()" +
					(this.currentStepController != null? ": result:" +
					this.currentStepController.getClass().getSimpleName() + 
					"[" + this.currentStepController.hashCode() + "]":""));
		}
		return currentStepController;
	}
	
	public StepController getFollowingStepController() {
		if (logger.isTraceEnabled()) {
			logger.trace("getFollowingStepController()");
		}
		StepController sc = null;
		if (followingStepControllers.size() > 0) {
			sc = followingStepControllers.pop(); 
		}
		if (logger.isTraceEnabled()) {
			logger.trace("followingSC: " + 
					(sc!=null?
							sc.getClass().getSimpleName() + 
							"[" + sc.hashCode() + "]"
							:sc)
					);
		}
		return sc;
	}
	
	public void addFollowingStepController(StepController sc) {
		if (logger.isTraceEnabled()) {
			logger.trace("addFollowingStepController(" + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		followingStepControllers.push(sc);
	}
}
