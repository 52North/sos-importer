package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step6cPanel;

public class Step6cController extends StepController {

	private static final Logger logger = Logger.getLogger(Step6cController.class);
	
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
		Position position = step6cModel.getPosition();
		positionController = new PositionController(position);
		List<Component> components = step6cModel.getMissingPositionComponents();
		positionController.setMissingComponents(components);
		positionController.unassignMissingComponentValues();	

		String name = step6cModel.getFeatureOfInterestName();
		if (name == null) { //when this feature is not contained in the table
			FeatureOfInterest foi = step6cModel.getFeatureOfInterest();
			name = foi.getName();
		}
		
		String description = step6cModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = positionController.getMissingComponentPanels();
		step6cPanel = new Step6cPanel(description, name, null, missingComponentPanels);	
	}

	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();
		
		List<Component> components = positionController.getMissingComponents();
		step6cModel.setMissingPositionComponents(components);
		
		String name = step6cModel.getFeatureOfInterestName();
		Position position = step6cModel.getPosition();
		if (name == null) //when this feature is not contained in the table
			step6cModel.getFeatureOfInterest().setPosition(position);
		else 
			step6cModel.getFeatureOfInterest().setPositionFor(name, position);
		
		step6cPanel = null;
		positionController = null;
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
		if (step6cModel != null) return true;

		logger.info("Skip Step 6c since there is at least one position");
		return false;
	}

	@Override
	public boolean isFinished() {
		return positionController.checkMissingComponentValues();
	}

}
