package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step7Panel;

public class Step7Controller extends StepController {

	private Step7Model step7Model;
	
	private PositionController positionController;
	
	private Step7Panel step7Panel;
	
	public Step7Controller(Step7Model step7Model) {
		this.step7Model = step7Model;
		step7Panel = new Step7Panel();

		FeatureOfInterest foi = step7Model.getFeatureOfInterest();
		step7Panel.setFeatureOfInterestName(foi.getName());
		
		Position p = step7Model.getPosition();
		positionController = new PositionController(p);
		
		step7Panel.addMissingComponentPanels(positionController.getMissingComponentPanels());
	}
	
	@Override
	public String getDescription() {
		return "Step 7: Add missing positions";
	}

	@Override
	public JPanel getStepPanel() {
		return step7Panel;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		positionController.assignMissingComponentValues();
		step7Model.getFeatureOfInterest().setPosition(step7Model.getPosition());
		check();
	}
	
	public void check() {
		FeatureOfInterest foiWithoutPosition = getNextFeatureOfInterestWithoutPosition();

		if (foiWithoutPosition != null) {
			MainController.getInstance().setStepController(new Step7Controller(new Step7Model(foiWithoutPosition)));
		} else {
			for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
				mv.print();
			}
		}	
	}
	
	private FeatureOfInterest getNextFeatureOfInterestWithoutPosition() {
		List<FeatureOfInterest> featureOfInterests = ModelStore.getInstance().getFeatureOfInterests();
		
		for (FeatureOfInterest foi: featureOfInterests) 
			if (foi.getPosition() == null) 
				return foi;
		return null;
	}

}
