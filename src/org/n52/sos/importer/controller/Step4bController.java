package org.n52.sos.importer.controller;

import java.awt.Color;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.Store;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeComponentController;
import org.n52.sos.importer.view.Step4bPanel;

public class Step4bController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step4bController.class);

	private Step4bPanel step4bPanel;
	
	private TableController tableController;
	
	public Step4bController(DateAndTimeController dateAndTime) {

		for (DateAndTimeComponentController c: dateAndTime.getComponents()) {
			if (c.getColumnNumber() != -1)
				tableController.colorColumn(Color.yellow, c.getColumnNumber());
			if (c.getRowNumber() != -1)
				tableController.colorRow(Color.yellow, c.getRowNumber());
		}
		
		tableController.allowColumnSelection();

		
		tableController = TableController.getInstance();
		tableController.allowMultipleSelection();
		tableController.addMultipleSelectionListener(new SelectionChanged());
		String text = "Mark all measured value columns where this Date & Time corresponds to.";
		step4bPanel = new Step4bPanel(text);
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
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}
	
	private class SelectionChanged implements TableController.MultipleSelectionListener {

		@Override
		public void columnSelectionChanged(int[] selectedColumns) {
			for (int column: selectedColumns) {
				MeasuredValue mv = Store.getInstance().getMeasuredValueAtColumn(column);
				if (mv == null) {
					logger.error("This is not a measured value.");
					tableController.deselectColumn(column);
					return;
				}

				if (resource.isAssigned(mv)) {
					logger.error(resource + " already set for this measured value.");
					tableController.deselectColumn(column);
					return;
				}
			}
		}

		@Override
		public void rowSelectionChanged(int[] selectedRows) {
			// TODO Auto-generated method stub
			
		}	
	}

}
