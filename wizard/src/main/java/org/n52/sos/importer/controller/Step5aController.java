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
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lets the user add missing metadata for identified date&times
 * @author Raimund
 *
 */
public class Step5aController extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step5aController.class);
	
	private Step5aModel step5aModel;
	
	private Step5Panel step5Panel;
	
	private DateAndTimeController dateAndTimeController;
	
	private final TableController tabController;

	private final int firstLineWithData;

	
	public Step5aController(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		tabController =  TableController.getInstance();
	}
	
	public Step5aController(final Step5aModel step5aModel,final int firstLineWithData) {
		this(firstLineWithData);
		this.step5aModel = step5aModel;
	}
	
	@Override
	public void loadSettings() {				
		final DateAndTime dateAndTime = step5aModel.getDateAndTime();
		dateAndTimeController = new DateAndTimeController(dateAndTime);
		final List<Component> components = step5aModel.getMissingDateAndTimeComponents();
		dateAndTimeController.setMissingComponents(components);
		dateAndTimeController.unassignMissingComponentValues();
		
		final String description = step5aModel.getDescription();
		final List<MissingComponentPanel> missingComponentPanels = dateAndTimeController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		
		tabController.turnSelectionOff();
		dateAndTimeController.markComponents();	
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public void saveSettings() {
		dateAndTimeController.assignMissingComponentValues();	
		
		final List<Component> components = dateAndTimeController.getMissingComponents();
		step5aModel.setMissingDateAndTimeComponents(components);
		
		tabController.clearMarkedTableElements();
		tabController.turnSelectionOn();
		
		dateAndTimeController = null;
		step5Panel = null;
	}
	
	@Override
	public void back() {
		tabController.clearMarkedTableElements();
		tabController.turnSelectionOn();
		
		dateAndTimeController = null;
		step5Panel = null;
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step5aDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		dateAndTimeController = new DateAndTimeController();
		final DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		
		if (dtm == null) {
			logger.info("Skip Step 5a since there are not any Date&Times" +
				" with missing values");
			return false;
		}
		
		step5aModel = new Step5aModel(dtm);
		return true;
	}
	
	@Override
	public StepController getNext() {	
		dateAndTimeController = new DateAndTimeController();
		final DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		if (dtm != null) {
			return new Step5aController(new Step5aModel(dtm),firstLineWithData);
		}
		
		dateAndTimeController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step5cController(firstLineWithData);
	}

	@Override
	public StepModel getModel() {
		return step5aModel;
	}
}
