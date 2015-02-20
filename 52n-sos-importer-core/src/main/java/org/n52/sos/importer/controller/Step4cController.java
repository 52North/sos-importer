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
package org.n52.sos.importer.controller;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solves ambiguities in case there is more than one position group
 * (needs apparently to be implemented)
 * <br />
 * TODO Implement
 * @author Raimund
 *
 */
public class Step4cController extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step4cController.class);
	
	private final int firstLineWithData;
	
	/**
	 * @param firstLineWithData
	 */
	public Step4cController(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
	}
	@Override
	public void loadSettings() {
	}
	@Override
	public void saveSettings() {
	}
	@Override
	public String getDescription() {
		return null;
	}
	@Override
	public JPanel getStepPanel() {
		return null;
	}
	@Override
	public StepController getNextStepController() {
		return new Step5aController(firstLineWithData);
	}

	@Override
	public boolean isNecessary() {
		final int positions = ModelStore.getInstance().getPositions().size();
		
		if (positions == 0) {
			logger.info("Skip Step 4c since there are not any Positions");
			return false;
		}
		if (positions == 1) {
			final Position position = ModelStore.getInstance().getPositions().get(0);
			logger.info("Skip Step 4c since there is just one " + position);
			
			for (final FeatureOfInterest foi: ModelStore.getInstance().getFeatureOfInterests()) {
				foi.setPosition(position);
			}
			return false;
		}
		//TODO implement handling of more than one position group
		throw new RuntimeException("NOT YET IMPLEMENTED");
//		return true;
	}
	@Override
	public boolean isFinished() {
		return false;
	}
	@Override
	public StepController getNext() {
		return null;
	}
	@Override
	public StepModel getModel() {
		return null;
	}
}
