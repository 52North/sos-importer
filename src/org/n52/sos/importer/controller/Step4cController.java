package org.n52.sos.importer.controller;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;

public class Step4cController extends StepController {

	private static final Logger logger = Logger.getLogger(Step4cController.class);
	
	@Override
	public void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getStepPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StepController getNextStepController() {
		return new Step5aController();
	}

	@Override
	public boolean isNecessary() {
		int positions = ModelStore.getInstance().getPositions().size();
		
		if (positions == 0) {
			logger.info("Skip Step 4c since there are not any Positions");
			return false;
		}
		if (positions == 1) {
			Position position = ModelStore.getInstance().getPositions().get(0);
			logger.info("Skip Step 4c since there is just " + position);
			
			for (FeatureOfInterest foi: ModelStore.getInstance().getFeatureOfInterests())
				foi.setPosition(position);
			return false;
		}
		
		//TODO more than one position group
		return false;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StepController getNext() {
		// TODO Auto-generated method stub
		return null;
	}

}
