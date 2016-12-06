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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step4Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solves ambiguities in case there is more than one feature of interest,
 * observed property, unit of measurement or sensor column
 * @author Raimund
 *
 */
public class Step4bController extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step4bController.class);
	
	private Step4bModel step4bModel;
	
	private final TableController tableController;
	
	private Step4Panel step4Panel;
	
	private int firstLineWithData = -1;
	
	public Step4bController(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		tableController = TableController.getInstance();
	}
	
	public Step4bController(final Step4bModel step4bModel,final int firstLineWithData) {
		this(firstLineWithData);
		this.step4bModel = step4bModel;
	}

	/*
	 * {@link org.n52.sos.importer.view.i18n.Lang#step4bModelDescription()}
	 */
	@Override
	public void loadSettings() {
		final Resource resource = step4bModel.getResource();
		final int[] selectedRowsOrColumns = step4bModel.getSelectedColumns();
		final int fLWData = step4bModel.getFirstLineWithData();
		
		String text = step4bModel.getDescription();
		final String orientation = tableController.getOrientationString();
		
		/*
		 * List how to replace the org.n52.sos.importer.view.utils.Constants.STRING_REPLACER in the correct order: 
		 * 	1 The table element type of the measured values, maybe "column"
		 * 	2 The resource type, that is linked to the measured value table element
		 * 	3 Table element of element to be selected
		 * 	4 Table element of element to be selected
		 * 	5 The resource type, that is linked to the measured value table element
		 * 	6 Table element of element to be selected 
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("Text: " + text);
		}
		text = text.replaceFirst(Constants.STRING_REPLACER, orientation);
		text = text.replaceFirst(Constants.STRING_REPLACER, resource.getTypeName());
		text = text.replaceFirst(Constants.STRING_REPLACER, orientation);
		text = text.replaceFirst(Constants.STRING_REPLACER, orientation);
		text = text.replaceFirst(Constants.STRING_REPLACER, resource.getTypeName());
		text = text.replaceFirst(Constants.STRING_REPLACER, orientation);
		
		step4Panel = new Step4Panel(text);
		
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addMultipleSelectionListener(new SelectionChanged());
		
		for (final int number: selectedRowsOrColumns) {
			final Column column = new Column(number,fLWData);
			final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(column);
			resource.unassign(mv);
			tableController.selectColumn(number);
		}		
		
		resource.getTableElement().mark();
	}
	
	@Override
	public void saveSettings() {
		final Resource resource = step4bModel.getResource();	
		final int[] selectedColumns = tableController.getSelectedColumns();
		final int fLWData = step4bModel.getFirstLineWithData();
		step4bModel.setSelectedColumns(selectedColumns);
		
		for (final int number: selectedColumns) {
			final Column column = new Column(number,fLWData);
			final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(column);
			resource.assign(mv);
		}
		
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();

		step4Panel = null;
	}
	
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();
		
		step4Panel = null;
	};
	
	private class SelectionChanged implements TableController.MultipleSelectionListener {

		@Override
		public void columnSelectionChanged(final int[] selectedColumns) {
			for (final int number: selectedColumns) {
				final int fLWData = step4bModel.getFirstLineWithData();
				final Column column = new Column(number,fLWData);
				final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(column);
				if (mv == null) {
					JOptionPane.showMessageDialog(null,
						    Lang.l().step4bInfoNotMeasuredValue(),
						    Lang.l().infoDialogTitle(),
						    JOptionPane.INFORMATION_MESSAGE);
					tableController.deselectColumn(number);
					return;
				}
				
				final Resource resource = step4bModel.getResource();
				if (resource.isAssigned(mv)) {
					JOptionPane.showMessageDialog(null,
						    Lang.l().step4bInfoResoureAlreadySet(resource),
						    Lang.l().infoDialogTitle(),
						    JOptionPane.INFORMATION_MESSAGE);
					tableController.deselectColumn(number);
					return;
				}
			}
		}

		@Override
		public void rowSelectionChanged(final int[] selectedRows) {
			// Do nothing -> only columns are used.
		}	
	}

	@Override
	public String getDescription() {
		return Lang.l().step4bDescription();
	}


	@Override
	public JPanel getStepPanel() {
		return step4Panel;
	}


	@Override
	public StepController getNextStepController() {
		return new Step4cController(firstLineWithData);
	}


	@Override
	public boolean isNecessary() {	
		Resource resourceType = new FeatureOfInterest();
		Resource resource = null;
		
		//find how many Feature of Interests, Observed Properties, Units of 
		//Measurement or Sensors there are and handle the cases 0, 1 and n
		while (resourceType != null) {
			final int number = resourceType.getList().size();
			// in case there is just one resource of this type:
			if (number == 1) {
				final Resource oneResource = resourceType.getList().get(0);
				logger.info("Skip Step 4b for " + resourceType + "s" +
						" since there is just " + oneResource);
				
				for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
					oneResource.assign(mv);
				}
			
			//in case there are more than two resources of this type:
			} else if (resource == null && number >= 2){
				resource = getNextUnassignedResource(resourceType);
			} else { //number == 0
				logger.info("Skip Step 4b for " + resourceType + "s" +
						" since there are not any " + resourceType + "s");
			}
			resourceType = resourceType.getNextResourceType();
		}
		
		step4bModel = new Step4bModel(resource,firstLineWithData);
		return resource != null;
	}
	
	@Override
	public StepController getNext() {
		Resource resourceType = step4bModel.getResource();
		
		Resource nextResource = null;
		while (resourceType != null) {
			nextResource = getNextUnassignedResource(resourceType);
			if (nextResource != null) {
				return new Step4bController(new Step4bModel(nextResource,firstLineWithData),firstLineWithData);
			}
			
			resourceType = resourceType.getNextResourceType();
		}
		return null;
	}	
	
	private Resource getNextUnassignedResource(final Resource resourceType) {
		final boolean unassignedMeasuredValues = areThereAnyUnassignedMeasuredValuesOf(resourceType);
		if (!unassignedMeasuredValues) {
			return null;
		}
		
		for (final Resource resource: resourceType.getList()) {
			if (!isAssignedToMeasuredValues(resource)) {
				return resource;
			}
		}	
		return null;
	}
	
	private boolean areThereAnyUnassignedMeasuredValuesOf(final Resource resourceType) {
		for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (!resourceType.isAssigned(mv)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isAssignedToMeasuredValues(final Resource resource) {
		for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (resource.isAssignedTo(mv)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public StepModel getModel() {
		return step4bModel;
	}
}
