package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.view.Step5aPanel;
import org.n52.sos.importer.view.position.MissingComponentPanel;

/**
 * in case of no date&time 
 * @author Raimund
 *
 */
public class Step6aController extends StepController {
	
	private Step6aModel step6aModel;
	
	private Step5aPanel step5aPanel;
	
	private DateAndTimeController dtc;
	
	public Step6aController() {
	}
	
	public Step6aController(Step6aModel step6aModel) {
		this.step6aModel = step6aModel;
	}

	@Override
	public void loadSettings() {
		DateAndTime dateAndTime = step6aModel.getDateAndTime();
		String description = step6aModel.getDescription();
		
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			mv.setDateAndTime(null);
		
		dtc = new DateAndTimeController(dateAndTime);
		List<MissingComponentPanel> mcp = dtc.getMissingComponentPanels();
		step5aPanel = new Step5aPanel(description, mcp);
		
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
	}

	@Override
	public void saveSettings() {
		dtc.assignMissingComponentValues();
		DateAndTime dateAndTime = dtc.getDateAndTime();
		
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			mv.setDateAndTime(dateAndTime);
		
		dtc = null;
		step5aPanel = null;
	}

	@Override
	public String getDescription() {
		return "Step 6a: Add missing time information";
	}

	@Override
	public JPanel getStepPanel() {
		return step5aPanel;
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
