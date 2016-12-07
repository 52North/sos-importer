/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lets the user add missing metadata for identified positions
 * @author Raimund
 *
 */
public class Step5cController extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step5cController.class);
	
	private Step5cModel step5cModel;
	
	private Step5Panel step5Panel;
	
	private PositionController positionController;
	
	private final TableController tableController;
	
	private final int firstLineWithData;
	
	public Step5cController(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		tableController = TableController.getInstance();
	}
	
	public Step5cController(final Step5cModel step5cModel,final int firstLineWithData) {
		this(firstLineWithData);
		this.step5cModel = step5cModel;
	}
	
	@Override
	public void loadSettings() {			
		final Position position = step5cModel.getPosition();
		positionController = new PositionController(position);
		final List<Component> components = step5cModel.getMissingPositionComponents();
		positionController.setMissingComponents(components);
		positionController.unassignMissingComponentValues();
		
		final String description = step5cModel.getDescription();
		final List<MissingComponentPanel> missingComponentPanels = positionController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		
		tableController.turnSelectionOff();
		positionController.markComponents();
	}
	
	
	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();	
		
		final List<Component> components = positionController.getMissingComponents();
		step5cModel.setMissingPositionComponents(components);
		
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		positionController = null;
		step5Panel = null;
	}
	
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		positionController = null;
		step5Panel = null;
	}

	@Override
	public boolean isFinished() {
		return positionController.checkMissingComponentValues();
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step5cDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		positionController = new PositionController();
		final Position p = positionController.getNextPositionWithMissingValues();
		
		if (p == null) {
			logger.info("Skip Step 5c since there are not any Positions" +
					" with missing values");
			return false;
		}
		
		step5cModel = new Step5cModel(p);
		return true;
	}
	
	@Override
	public StepController getNext() {
		positionController = new PositionController();
		final Position p = positionController.getNextPositionWithMissingValues();
		if (p != null) {
			return new Step5cController(new Step5cModel(p),firstLineWithData);
		}
		
		positionController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step6aController(firstLineWithData);
	}

	@Override
	public StepModel getModel() {
		return step5cModel;
	}
}
