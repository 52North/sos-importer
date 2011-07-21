package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.Step5aPanel;
import org.n52.sos.importer.view.position.MissingComponentPanel;

public class Step5aController extends StepController {

	private static final Logger logger = Logger.getLogger(Step5aController.class);
	
	private static final long serialVersionUID = 1L;
	
	private Step5aModel step5aModel;
	
	private Step5aPanel step5aPanel;
	
	private DateAndTimeController dateAndTimeController;
	
	public Step5aController() {	
	}
	
	public Step5aController(Step5aModel step5bModel) {
		this.step5aModel = step5bModel;
	}
	
	@Override
	public void loadSettings() {		
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
		
		dateAndTimeController = new DateAndTimeController(step5aModel.getDateAndTime());
		dateAndTimeController.mark(TableController.getInstance().getMarkingColor());
		
		String description = step5aModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = dateAndTimeController.getMissingComponentPanels();		
		step5aPanel = new Step5aPanel(description, missingComponentPanels);
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public void saveSettings() {
		dateAndTimeController.assignMissingComponentValues();	
		
		step5aPanel = null;
	}
	
	@Override
	public String getDescription() {
		return "Step 5a: Complete time data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5aPanel;
	}

	@Override
	public boolean isNecessary() {
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		
		if (dtm == null) {
			logger.info("Skip Step 5c since there are not any Date&Times" +
				" with missing values");
			return false;
		}
		
		step5aModel = new Step5aModel(dtm);
		return true;
	}
	
	@Override
	public StepController getNext() {	
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		if (dtm != null) return new Step5aController(new Step5aModel(dtm));
		
		dateAndTimeController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step6bController();
	}
}
