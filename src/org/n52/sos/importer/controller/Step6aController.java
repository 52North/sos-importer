package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.view.Step5Panel;

/**
 * in case of no date&time 
 * @author Raimund
 *
 */
public class Step6aController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6aController.class);
	
	private Step6aModel step6aModel;
	
	private Step5Panel step5Panel;
	
	private DateAndTimeController dateAndTimeController;
	
	private TableController tableController = TableController.getInstance();
	
	public Step6aController() {
	}
	
	public Step6aController(Step6aModel step6aModel) {
		this.step6aModel = step6aModel;
	}

	@Override
	public void loadSettings() {
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.turnSelectionOff();
		
		DateAndTime dateAndTime = step6aModel.getDateAndTime();
		dateAndTimeController = new DateAndTimeController(dateAndTime);
		List<Component> components = step6aModel.getMissingDateAndTimeComponents();
		dateAndTimeController.setMissingComponents(components);
		dateAndTimeController.unassignMissingComponentValues();
		
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			mv.setDateAndTime(null);
		
		String description = step6aModel.getDescription();
		List<MissingComponentPanel> mcp = dateAndTimeController.getMissingComponentPanels();
		step5Panel = new Step5Panel(description, mcp);
	}

	@Override
	public void saveSettings() {
		dateAndTimeController.assignMissingComponentValues();	
		
		List<Component> components = dateAndTimeController.getMissingComponents();
		step6aModel.setMissingDateAndTimeComponents(components);

		DateAndTime dateAndTime = dateAndTimeController.getDateAndTime();
		
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			mv.setDateAndTime(dateAndTime);
		
		step5Panel = null;
	}

	@Override
	public String getDescription() {
		return "Step 6a: Add missing dates and times";
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public StepController getNextStepController() {
		return new Step6bController();
	}

	@Override
	public boolean isNecessary() {
		int n = ModelStore.getInstance().getDateAndTimes().size();
		if (n == 0) {
			DateAndTime dateAndTime = new DateAndTime();
			step6aModel = new Step6aModel(dateAndTime);
			return true;
		}
		
		logger.info("Skip Step 6a since there is at least one Date&Time");
			
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public StepController getNext() {
		return null;
	}

}
