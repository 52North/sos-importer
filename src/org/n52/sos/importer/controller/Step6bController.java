package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.resources.MissingResourcePanel;

/**
 * Create missing resources for measured values
 * @author Raimund
 */
public class Step6bController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6bController.class);
		
	private Step6bModel step6bModel;
	
	private TableController tableController = TableController.getInstance();
	
	private Step5Panel step5Panel;
	
	private MissingResourcePanel missingResourcePanel;
	
	public Step6bController() {	
	}
	
	public Step6bController(Step6bModel step6bModel) {
		this.step6bModel = step6bModel;
	}
	
	@Override
	public void loadSettings() {
		Resource resource = step6bModel.getResource();
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		
		resource.unassign(measuredValue);
		
		missingResourcePanel = new MissingResourcePanel(resource);
		missingResourcePanel.setMissingComponent(resource);
		missingResourcePanel.unassignValues();
		
		List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		String question = step6bModel.getDescription();
		question = question.replaceAll("RESOURCE", resource.toString());
		question = question.replaceAll("ORIENTATION", tableController.getOrientationString());
		step5Panel = new Step5Panel(question, missingComponentPanels);
		
		tableController.turnSelectionOff();
		measuredValue.getTableElement().mark(tableController.getMarkingColor());		
	}	
	
	@Override
	public void saveSettings() {
		missingResourcePanel.assignValues();
		
		Resource resource = step6bModel.getResource();
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		
		//check if there is already such a resource
		List<Resource> resources = resource.getList();
		int index = resources.indexOf(resource);
		if (index == -1)
			ModelStore.getInstance().add(resource);
		else 
			resource = resources.get(index);
		
		resource.assign(measuredValue);
		
		step5Panel = null;
		missingResourcePanel = null;
	}
	
	@Override
	public StepController getNextStepController() {		
		return new Step6bSpecialController();	
	}

	@Override
	public String getDescription() {
		return "Step 6b: Add missing metadata";
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
		if (model != null) return new Step6bController(model);
			
		return null;
	}
	
	public Step6bModel getMissingResourceForMeasuredValue() {
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
}
