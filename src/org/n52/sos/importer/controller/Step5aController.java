package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.Step5Panel;

public class Step5aController extends StepController {

	private static final Logger logger = Logger.getLogger(Step5aController.class);
	
	private static final long serialVersionUID = 1L;
	
	private Step5aModel step5aModel;
	
	private Step5Panel step5Panel;
	
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
		
		DateAndTime dateAndTime = step5aModel.getDateAndTime();
		dateAndTimeController = new DateAndTimeController(dateAndTime);
		List<Component> components = step5aModel.getMissingDateAndTimeComponents();
		dateAndTimeController.setMissingComponents(components);
		dateAndTimeController.unassignMissingComponentValues();
		
		String description = step5aModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = dateAndTimeController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		dateAndTimeController.mark(TableController.getInstance().getMarkingColor());
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public void saveSettings() {
		dateAndTimeController.assignMissingComponentValues();	
		
		List<Component> components = dateAndTimeController.getMissingComponents();
		step5aModel.setMissingDateAndTimeComponents(components);
		
		dateAndTimeController = null;
		step5Panel = null;
	}
	
	@Override
	public String getDescription() {
		return "Step 5a: Complete time data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		
		if (dtm == null) {
			logger.info("Skip Step 5a since there are not any Date&Times" +
				" with missing values");
			return false;
		}
		
		step5aModel = new Step5aModel(dtm);
		return true;
	}
	
	@Override
	public StepController getNext() {	
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		if (dtm != null) return new Step5aController(new Step5aModel(dtm));
		
		dateAndTimeController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step5cController();
	}
}
