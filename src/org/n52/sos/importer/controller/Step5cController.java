package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.Step5Panel;

public class Step5cController extends StepController {

	private static final Logger logger = Logger.getLogger(Step5cController.class);
	
	private static final long serialVersionUID = 1L;
	
	private Step5cModel step5cModel;
	
	private Step5Panel step5Panel;
	
	private PositionController positionController;
	
	private TableController tableController = TableController.getInstance();
	
	public Step5cController() {	
	}
	
	public Step5cController(Step5cModel step5cModel) {
		this.step5cModel = step5cModel;
	}
	
	@Override
	public void loadSettings() {			
		Position position = step5cModel.getPosition();
		positionController = new PositionController(position);
		List<Component> components = step5cModel.getMissingPositionComponents();
		positionController.setMissingComponents(components);
		positionController.unassignMissingComponentValues();
		
		String description = step5cModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = positionController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		
		tableController.turnSelectionOff();
		positionController.markComponents();
	}
	
	
	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();	
		
		List<Component> components = positionController.getMissingComponents();
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
		return "Step 5c: Complete position data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		positionController = new PositionController();
		Position p = positionController.getNextPositionWithMissingValues();
		
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
		Position p = positionController.getNextPositionWithMissingValues();
		if (p != null) return new Step5cController(new Step5cModel(p));
		
		positionController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step6aController();
	}
}
