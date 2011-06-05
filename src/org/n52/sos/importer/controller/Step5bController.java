package org.n52.sos.importer.controller;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step5bModel;
import org.n52.sos.importer.view.Step5bPanel;

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
		// TODO Auto-generated method stub
		
	}
	
	public void load() {
		Color color = step5bModel.getMarkingColor();
		/*
		for (DateAndTimeComponentController c: dateAndTimeController.getComponents()) {
			if (c.getColumnNumber() != -1) 
				TableController.getInstance().colorColumn(color, c.getColumnNumber());
			else if (c.getRowNumber() != -1)
				TableController.getInstance().colorRow(color, c.getRowNumber());
			else if (c.getCellCoordinates() != null)
				TableController.getInstance().colorCell(color, c.getCellCoordinates());
		}*/
		
		ArrayList<JPanel> missingComponents = new ArrayList<JPanel>();
		step5bModel.getDateAndTimeController().getMissingComponents(missingComponents);
		step5bPanel.addMissingComponents(missingComponents);
	}

}
