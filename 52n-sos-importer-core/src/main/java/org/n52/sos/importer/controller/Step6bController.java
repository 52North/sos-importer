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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.resources.MissingResourcePanel;

/**
 * Lets the user choose feature of interest, observed property, 
 * unit of measurement and sensor for each measured value column
 * in case they do not appear in the CSV file.
 * @author Raimund
 * 
 */
public class Step6bController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6bController.class);
		
	private Step6bModel step6bModel;
	
	private Step5Panel step5Panel;
	
	private MissingResourcePanel missingResourcePanel;
	
	private TableController tableController;

	private int firstLineWithData;
	
	public Step6bController(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		this.tableController = TableController.getInstance();
	}
	
	public Step6bController(Step6bModel step6bModel,int firstLineWithData) {
		this(firstLineWithData);
		this.step6bModel = step6bModel;
	}
	
	@Override
	public void loadSettings() {
		Resource resource = step6bModel.getResource();
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		
		//when this resource is still assigned with measured values,
		//do not remove it from the ModelStore
		int count = 0;
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (resource.isAssignedTo(mv)) {
				count++;
			}
		}
		if (count == 1) {
			ModelStore.getInstance().remove(resource);
		}
		
		resource.unassign(measuredValue);
		
		missingResourcePanel = new MissingResourcePanel(resource);
		missingResourcePanel.setMissingComponent(resource);
		missingResourcePanel.unassignValues();
		
		List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		String question = step6bModel.getDescription();
		question = question.replaceFirst(Constants.STRING_REPLACER, resource.toString());
		question = question.replaceFirst(Constants.STRING_REPLACER, tableController.getOrientationString());
		step5Panel = new Step5Panel(question, missingComponentPanels);
		
		tableController.turnSelectionOff();
		measuredValue.getTableElement().mark();		
	}	
	
	@Override
	public void saveSettings() {
		missingResourcePanel.assignValues();
		ModelStore ms = ModelStore.getInstance(); 
		Resource resource = step6bModel.getResource();
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		
		//check if there is already such a resource
		List<Resource> resources = resource.getList();
		int index = resources.indexOf(resource);
		if (index == -1) {
			ms.add(resource);
		} else { 
			resource = resources.get(index);
		}
		resource.assign(measuredValue);
		// TODO handle the case one manual/generated foi and one position in file
		if (resource instanceof FeatureOfInterest) {
			if (ms.getFeatureOfInterests() != null &&
					ms.getFeatureOfInterests().size() == 1 &&
					ms.getPositions() != null &&
					ms.getPositions().size() == 1) {
				((FeatureOfInterest) resource).setPosition(ms.getPositions().get(0));
			}
		}
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		step5Panel = null;
		missingResourcePanel = null;
	}
	
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		step5Panel = null;
		missingResourcePanel = null;
	}
	
	@Override
	public StepController getNextStepController() {		
		return new Step6bSpecialController(this.firstLineWithData);	
	}

	@Override
	public String getDescription() {
		return Lang.l().step6bDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		step6bModel = getMissingResourceForMeasuredValue();	
		if (step6bModel == null) {
			logger.info("Skip Step 6b since all Measured Values are already" +
					" assigned to Features Of Interest, Observed Properties," +
					" Unit Of Measurements and Sensors");
			return false;
		}
		
		return true;
	}
	
	@Override
	public StepController getNext() {
		Step6bModel model = getMissingResourceForMeasuredValue();	
		if (model != null) return new Step6bController(model,this.firstLineWithData);
			
		return null;
	}
	
	private Step6bModel getMissingResourceForMeasuredValue() {
		List<MeasuredValue> measuredValues = ModelStore.getInstance().getMeasuredValues();
		
		for (MeasuredValue mv: measuredValues) {
			if (mv.getFeatureOfInterest() == null) 
				return new Step6bModel(mv, new FeatureOfInterest());
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getObservedProperty() == null) {
				return new Step6bModel(mv, new ObservedProperty());
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getUnitOfMeasurement() == null) {
				return new Step6bModel(mv, new UnitOfMeasurement());
			}
		}
		
		if (ModelStore.getInstance().getFeatureOfInterestsInTable().size() == 0 &&
			ModelStore.getInstance().getObservedPropertiesInTable().size() == 0 &&
			ModelStore.getInstance().getSensorsInTable().size() == 0) {
			for (MeasuredValue mv: measuredValues) {
				if (mv.getSensor() == null) {
					return new Step6bModel(mv, new Sensor());
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
	public StepModel getModel() {
		return this.step6bModel;
	}
}
