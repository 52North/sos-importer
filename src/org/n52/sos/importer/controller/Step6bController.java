package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
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
	
	private static final Logger logger = Logger.getLogger(Step6bController.class);
		
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
		
		String question = step6bModel.getDescription();
		question = question.replaceAll("RESOURCE", resource.toString());
		question = question.replaceAll("ORIENTATION", tableController.getOrientationString());
		step5aPanel = new Step5aPanel(question, missingComponentPanels);
		
		tableController.turnSelectionOff();
		measuredValue.getTableElement().mark(tableController.getMarkingColor());		
	}	
	
	@Override
	public void saveSettings() {
		missingResourcePanel.assignValues();
		
		Resource resource = step6bModel.getResource();
		logger.info(resource);
		MeasuredValue measuredValue = step6bModel.getMeasuredValue();
		resource.assign(measuredValue);
		ModelStore.getInstance().add(resource);
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
