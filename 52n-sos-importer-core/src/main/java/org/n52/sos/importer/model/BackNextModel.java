/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model;

import java.util.Stack;

import org.n52.sos.importer.controller.StepController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * stores past, future and present step controller
 * @author Raimund
 *
 */
public class BackNextModel {

	private final Stack<StepController> previousStepControllers;
	
	private static final Logger logger = LoggerFactory.getLogger(BackNextModel.class);
	
	private final Stack<StepController> followingStepControllers;
	
	private StepController currentStepController;
	
	public BackNextModel() {
		previousStepControllers = new Stack<StepController>();
		followingStepControllers = new Stack<StepController>();
	}
	
	public StepController getPreviousStepController() {
		if (logger.isTraceEnabled()) {
			logger.trace("getPreviousStepController()");
		}
		final StepController sc = previousStepControllers.pop();
		if (logger.isTraceEnabled()) {
			logger.trace("result: " + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		return sc;
	}
	
	public void addPreviousStepController(final StepController sc) {
		if (logger.isTraceEnabled()) {
			logger.trace("addPreviousStepController(" + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		previousStepControllers.push(sc);
	}

	public void setCurrentStepController(final StepController currentSC) {
		if (logger.isTraceEnabled()) {
			logger.trace("setCurrentStepController(" + 
					currentSC.getClass().getSimpleName() + 
					"[" + currentSC.hashCode() + "])");
		}
		currentStepController = currentSC;
	}

	public StepController getCurrentStepController() {
		if (logger.isTraceEnabled()) {
			logger.trace("getCurrentStepController()" +
					(currentStepController != null? ": result:" +
					currentStepController.getClass().getSimpleName() + 
					"[" + currentStepController.hashCode() + "]":""));
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
	
	public void addFollowingStepController(final StepController sc) {
		if (logger.isTraceEnabled()) {
			logger.trace("addFollowingStepController(" + 
					(sc!=null?sc.getClass().getSimpleName():sc) + 
					"[" + (sc!=null?sc.hashCode():sc) + "])");
		}
		followingStepControllers.push(sc);
	}
}
