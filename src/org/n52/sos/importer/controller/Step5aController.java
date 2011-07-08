package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.Step5aPanel;
import org.n52.sos.importer.view.dateAndTime.MissingDateAndTimePanel;

public class Step5aController extends StepController {

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
		step5aPanel = new Step5aPanel();
		
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
		
		dateAndTimeController = new DateAndTimeController(step5aModel.getDateAndTimeModel());
		dateAndTimeController.mark(step5aPanel.getMarkingColor());
		
		List<MissingDateAndTimePanel> addMissingComponentPanels = dateAndTimeController.getMissingComponentPanels();		
		step5aPanel.addMissingComponentPanels(addMissingComponentPanels);
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
		return "Step 5a: Add missing time data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5aPanel;
	}

	@Override
	public boolean isNecessary() {
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		
		if (dtm == null) return false;
		
		step5aModel = new Step5aModel(dtm);
		dateAndTimeController.setDateAndTime(dtm);
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
