package org.n52.sos.importer.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.view.Step5aPanel;
import org.n52.sos.importer.view.position.MissingComponentPanel;
import org.n52.sos.importer.view.resources.MissingResourcePanel;

/**
 * Create missing resources for measured values
 * @author Raimund
 */
public class Step6bController extends StepController {
		
	private Step6bModel step6bModel;
	
	private TableController tableController = TableController.getInstance();
	
	private Step5aPanel step5aPanel;
	
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
		missingResourcePanel.unassignValues();
		List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		
		String question = "What is the <b>" + resource.toString() + "</b> for " + 
		"the marked measured value " + tableController.getOrientationString() + "?";
		step5aPanel = new Step5aPanel(question, missingComponentPanels);
		
		tableController.turnSelectionOff();
		measuredValue.getTableElement().mark(Color.yellow);		
	}	
	
	@Override
	public void saveSettings() {
		Resource resource = step6bModel.getResource();
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		resource.assign(measuredValue);
		missingResourcePanel.assignValues();
		
		step5aPanel = null;
		missingResourcePanel = null;
	}
	
	@Override
	public StepController getNextStepController() {		
		return new Step6cController();	
	}

	@Override
	public String getDescription() {
		return "Step 6b: Add missing Metadata";
	}

	@Override
	public JPanel getStepPanel() {
		return step5aPanel;
	}

	@Override
	public boolean isNecessary() {
		MeasuredValueController measuredValueController = new MeasuredValueController();
		step6bModel = measuredValueController.getMissingResourceForMeasuredValue();	
		if (step6bModel == null) return false;
		
		return true;
	}
	
	@Override
	public StepController getNext() {
		MeasuredValueController measuredValueController = new MeasuredValueController();		
		Step6bModel model = measuredValueController.getMissingResourceForMeasuredValue();	
		if (model != null) return new Step6bController(model);
			
		return null;
	}

	@Override
	public boolean isFinished() {
		return missingResourcePanel.checkValues();
	}
}
