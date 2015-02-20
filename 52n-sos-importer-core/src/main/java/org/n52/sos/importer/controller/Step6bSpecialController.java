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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step6Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.resources.MissingResourcePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * used to determine sensors in case there is one of the following 
 * relationships between feature of interest and observed property
 * column: 0:1, 0:n, n:0, 1:0, 1:1, n:n
 * @author Raimund
 *
 */
public class Step6bSpecialController extends StepController {
	
	private static final Logger logger = LoggerFactory.getLogger(Step6bSpecialController.class);

	private Step6bSpecialModel step6bSpecialModel;
	
	private Step6Panel step6cPanel;
	
	private MissingResourcePanel missingResourcePanel;

	private final TableController tableController;

	private final int firstLineWithData;
	
	public Step6bSpecialController(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		tableController = TableController.getInstance();
	}
	
	public Step6bSpecialController(final Step6bSpecialModel step6bSpecialModel, final int firstLineWithData) {
		this(firstLineWithData);
		this.step6bSpecialModel = step6bSpecialModel;
	}
	
	@Override
	public void loadSettings() {
		// FIXME does not work with generated
		final String description = step6bSpecialModel.getDescription();
		final String foiName = step6bSpecialModel.getFeatureOfInterest().getName();
		final String opName = step6bSpecialModel.getObservedProperty().getName();
		
		final Sensor sensor = step6bSpecialModel.getSensor();
		missingResourcePanel = new MissingResourcePanel(sensor);
		missingResourcePanel.setMissingComponent(sensor);
		ModelStore.getInstance().remove(step6bSpecialModel);
		missingResourcePanel.unassignValues();
		
		final List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		step6cPanel = new Step6Panel(description, foiName, opName, missingComponentPanels);
	}

	@Override
	public void saveSettings() {
		missingResourcePanel.assignValues();
		ModelStore.getInstance().add(step6bSpecialModel);
		
		step6cPanel = null;
		missingResourcePanel = null;
	}

	@Override
	public String getDescription() {
		return Lang.l().step6bSpecialDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step6cPanel;
	}

	@Override
	public StepController getNextStepController() {
		return new Step6cController();
	}

	@Override
	public boolean isNecessary() {
		if (ModelStore.getInstance().getSensorsInTable().size() > 0) {
			logger.info("Skip 6b (Special) since there are sensors in the table.");
			return false;
		}
		
		if (ModelStore.getInstance().getFeatureOfInterestsInTable().size() == 0 &&
			ModelStore.getInstance().getObservedPropertiesInTable().size() == 0) {
			logger.info("Skip 6b (Special) since there are not any features of interest" +
					"and observed properties in the table.");
			return false;
		}
		
		step6bSpecialModel = getNextModel();
		return true;
		
	}
		
	public Step6bSpecialModel getNextModel() {
		final int rows = tableController.getRowCount();
		final int flwd = tableController.getFirstLineWithData();
		final ModelStore ms = ModelStore.getInstance();

		//iterate through all measured value columns/rows
		mvLoop:
			for (final MeasuredValue mv: ms.getMeasuredValues()) {
				
				rowsLoop:
					for (int i = flwd; i < rows; i++) {	
						//test if the measuredValue can be parsed
						final Cell cell = new Cell(i, ((Column)mv.getTableElement()).getNumber());
						final String value = tableController.getValueAt(cell);
						if (i >= firstLineWithData) {
							try {
								mv.parse(value);
							} catch (final Exception e) {
								if (logger.isTraceEnabled()) {
									logger.trace("Value could not be parsed: " + value, e);
								}
								continue rowsLoop; // it okay this way because parsing test happened during step 3
							}	

							final FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(cell);
							final ObservedProperty op = mv.getObservedProperty().forThis(cell);
							final Step6bSpecialModel model = new Step6bSpecialModel(foi, op);
							// check, if for the column of foi and obsProp a model is available and if the sensor is generated in this model
							for (final Step6bSpecialModel s6bsM : ms.getStep6bSpecialModels()) {
								if (s6bsM.getSensor().isGenerated() &&
										s6bsM.getFeatureOfInterest().getTableElement().equals(foi.getTableElement()) && // XXX maybe own equals
										s6bsM.getObservedProperty().getTableElement().equals(op.getTableElement())) { // XXX check if foi and obsprop 
									continue mvLoop;
								}
							}
							if (!ms.getStep6bSpecialModels().contains(model)) {
								return model;
							}
						}
					}
			}
		return null;
	}

	@Override
	public boolean isFinished() {
		return missingResourcePanel.checkValues();
	}

	@Override
	public StepController getNext() {
		final Step6bSpecialModel step6bSpecialModel = getNextModel();
		if (step6bSpecialModel != null) {
			return new Step6bSpecialController(step6bSpecialModel,firstLineWithData);
		}
		
		return null;
	}

	@Override
	public StepModel getModel() {
		return step6bSpecialModel;
	}

}
