package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.Step5aPanel;
import org.n52.sos.importer.view.position.MissingComponentPanel;

public class Step5cController extends StepController {

	private static final long serialVersionUID = 1L;
	
	private Step5cModel step5cModel;
	
	private Step5aPanel step5aPanel;
	
	private PositionController positionController;
	
	public Step5cController() {	
	}
	
	public Step5cController(Step5cModel step5cModel) {
		this.step5cModel = step5cModel;
	}
	
	@Override
	public void loadSettings() {		
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
		
		positionController = new PositionController(step5cModel.getPosition());
		positionController.mark(TableController.getInstance().getMarkingColor());
		
		String description = step5cModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = positionController.getMissingComponentPanels();		
		step5aPanel = new Step5aPanel(description, missingComponentPanels);
	}
	
	
	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();	
		
		step5aPanel = null;
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public String getDescription() {
		return "Step 5c: Add missing position data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5aPanel;
	}

	@Override
	public boolean isNecessary() {
		positionController = new PositionController();
		Position p = positionController.getNextPositionWithMissingValues();
		
		if (p == null) return false;
		
		step5cModel = new Step5cModel(p);
		return true;
	}
	
	@Override
	public StepController getNext() {	
		Position p = positionController.getNextPositionWithMissingValues();
		if (p != null) return new Step5cController(new Step5cModel(p));
		
		positionController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step6bController();
	}
}
