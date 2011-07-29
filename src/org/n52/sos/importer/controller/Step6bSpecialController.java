package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.view.Step6cPanel;
import org.n52.sos.importer.view.resources.MissingResourcePanel;

public class Step6bSpecialController extends StepController {

	private Step6bSpecialModel step6bSpecialModel;
	
	private Step6cPanel step6cPanel;
	
	private MissingResourcePanel missingResourcePanel;

	public Step6bSpecialController() {
	}
	
	public Step6bSpecialController(Step6bSpecialModel step6bSpecialModel) {
		this.step6bSpecialModel = step6bSpecialModel;
	}
	
	@Override
	public void loadSettings() {
		String description = step6bSpecialModel.getDescription();
		String foiName = step6bSpecialModel.getFeatureOfInterestName();
		String opName = step6bSpecialModel.getObservedPropertyName();
		
		Sensor sensor = step6bSpecialModel.getSensor();
		missingResourcePanel = new MissingResourcePanel(sensor);
		missingResourcePanel.unassignValues();
		ModelStore.getInstance().remove(step6bSpecialModel);
		
		List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		step6cPanel = new Step6cPanel(description, foiName, opName, missingComponentPanels);
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
		return "Step 6b (Special): Add missing sensors";
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
		if (ModelStore.getInstance().getSensorsInTable().size() > 0)
			return false;
		
		if (ModelStore.getInstance().getFeatureOfInterestsInTable().size() == 0 &&
			ModelStore.getInstance().getObservedPropertiesInTable().size() == 0)
			return false;
		
		step6bSpecialModel = getNextModel();
		return true;
		
	}
		
	public Step6bSpecialModel getNextModel() {
		int rows = TableController.getInstance().getRowCount();

		//iterate through all measured value columns/rows
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			int rowOrColumnNumber;
			if (mv.getTableElement() instanceof Column) 
				rowOrColumnNumber = ((Column)mv.getTableElement()).getNumber();
			else 
				rowOrColumnNumber = ((Row)mv.getTableElement()).getNumber();
				
			for (int i = 0; i < rows; i++) {	
				//test if the measuredValue is parseable
				Cell cell = new Cell(i, rowOrColumnNumber);
				String value = TableController.getInstance().getValueAt(cell);
				try {
					mv.parse(value);
				} catch (Exception e) {
					continue;
				}	
				
				FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(cell);
				String featureOfInterestName = foi.getNameString();
				ObservedProperty op = mv.getObservedProperty().forThis(cell);
				String observedPropertyName = op.getNameString();
				Step6bSpecialModel model = new Step6bSpecialModel(featureOfInterestName, observedPropertyName);
				if (!ModelStore.getInstance().getStep6bSpecialModels().contains(model))
					return model;
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
		Step6bSpecialModel step6bSpecialModel = getNextModel();
		if (step6bSpecialModel != null)
			return new Step6bSpecialController(step6bSpecialModel);
		
		return null;
	}

}
