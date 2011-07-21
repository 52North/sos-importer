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
	
	public Step6cController(Step6cModel step6cModel) {
		this.step6cModel = step6cModel;
	}

	@Override
	public void loadSettings() {
		step6cPanel = new Step6cPanel();

		String name = step6cModel.getFeatureOfInterestName();
		if (name != null)
			step6cPanel.setFeatureOfInterestName(name);
		else {
			FeatureOfInterest foi = step6cModel.getFeatureOfInterest();
			step6cPanel.setFeatureOfInterestName(foi.getName());
		}
		
		Position p = step6cModel.getPosition();
		positionController = new PositionController(p);
		
		step6cPanel.addMissingComponentPanels(positionController.getMissingComponentPanels());	
	}

	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();
		
		String name = step6cModel.getFeatureOfInterestName();
		Position position = step6cModel.getPosition();
		if (name != null)
			step6cModel.getFeatureOfInterest().setPositionFor(name, position);
		else 
			step6cModel.getFeatureOfInterest().setPosition(position);
		
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
		Step6cModel foiWithoutPosition = getNextFeatureOfInterestWithoutPosition();
		if (foiWithoutPosition == null) return null;
		
		return new Step6cController(foiWithoutPosition);
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step7Controller();
	}
	
	private Step6cModel getNextFeatureOfInterestWithoutPosition() {
		List<FeatureOfInterest> featureOfInterests = ModelStore.getInstance().getFeatureOfInterests();

		for (FeatureOfInterest foi: featureOfInterests) {
			
			if (foi.getTableElement() == null) {
				if (foi.getPosition() == null) 
					return new Step6cModel(foi);
				//otherwise the feature has already a position
			} else {
				if (foi.getPosition() == null) {
					for (String name: foi.getTableElement().getValues()) {
						
						Position p = foi.getPositionFor(name);
						if (p == null)
							return new Step6cModel(foi, name); 
						//otherwise the feature in this row/column has already a position
					}
				}
				//otherwise the feature row/column has already a position row/column
			}
		}

		return null;
	}

	@Override
	public boolean isNecessary() {
		step6cModel = getNextFeatureOfInterestWithoutPosition();
		if (step6cModel == null) return false;

		return true;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
