package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step5bModel;
import org.n52.sos.importer.view.Step5bPanel;
import org.n52.sos.importer.view.dateAndTime.MissingComponentPanel;

public class Step5bController extends StepController {

	private static final long serialVersionUID = 1L;

	private Step5bModel step5bModel;
	
	private Step5bPanel step5bPanel;
	
	public Step5bController(DateAndTimeController dateAndTimeController) {
		step5bModel = new Step5bModel();
		step5bModel.setDateAndTimeController(dateAndTimeController);
		step5bPanel = new Step5bPanel();
		load();
	}
	
	@Override
	public String getDescription() {
		return "Step 5b: Add missing time data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5bPanel;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		step5bModel.getDateAndTimeController().assignMissingComponentValues();	
		System.out.println(step5bModel.getDateAndTimeController().getModel().getYearModel().getValue());
		System.out.println(step5bModel.getDateAndTimeController().getModel().getMonthModel().getValue());
		System.out.println(step5bModel.getDateAndTimeController().getModel().getDayModel().getValue());
	}
	
	public void load() {
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
		
		DateAndTimeController dateAndTimeController = step5bModel.getDateAndTimeController();
		dateAndTimeController.mark(step5bModel.getMarkingColor());
		
		List<MissingComponentPanel> addMissingComponentPanels = dateAndTimeController.getMissingComponentPanels();		
		step5bPanel.addMissingComponentPanels(addMissingComponentPanels);
	}

}
