package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step6cPanel;

public class Step6cController extends StepController {

	private Step6cModel step6cModel;
	
	private PositionController positionController;
	
	private Step6cPanel step6cPanel;
	
	public Step6cController() {
	}
	
	public Step6cController(Step6cModel step7Model) {
		this.step6cModel = step7Model;
	}

	@Override
	public void loadSettings() {
		step6cPanel = new Step6cPanel();

		FeatureOfInterest foi = step6cModel.getFeatureOfInterest();
		step6cPanel.setFeatureOfInterestName(foi.getName());
		
		Position p = step6cModel.getPosition();
		positionController = new PositionController(p);
		
		step6cPanel.addMissingComponentPanels(positionController.getMissingComponentPanels());	
	}

	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();
		step6cModel.getFeatureOfInterest().setPosition(step6cModel.getPosition());
		step6cPanel = null;
	}
	
	
	@Override
	public String getDescription() {
		return "Step 6c: Add missing positions";
	}

	@Override
	public JPanel getStepPanel() {
		return step6cPanel;
	}

	@Override
	public StepController getNext() {
		FeatureOfInterest foiWithoutPosition = getNextFeatureOfInterestWithoutPosition();
		if (foiWithoutPosition == null) return null;
		
		return new Step6cController(new Step6cModel(foiWithoutPosition));
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step7Controller();
	}
	
	private FeatureOfInterest getNextFeatureOfInterestWithoutPosition() {
		List<FeatureOfInterest> featureOfInterests = ModelStore.getInstance().getFeatureOfInterests();
		
		for (FeatureOfInterest foi: featureOfInterests) 
			if (foi.getPosition() == null) 
				return foi;
		return null;
	}

	@Override
	public boolean isNecessary() {
		FeatureOfInterest foiWithoutPosition = getNextFeatureOfInterestWithoutPosition();
		if (foiWithoutPosition == null) return false;
		
		step6cModel = new Step6cModel(foiWithoutPosition);
		return true;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
