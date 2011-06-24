package org.n52.sos.importer.controller;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.Step5bModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.view.Step4bPanel;

public class Step4bController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step4bController.class);
	
	private Step4bPanel step4bPanel;
	
	private TableController tableController;
	
	private DateAndTimeController dateAndTimeController;
	
	public Step4bController(Step4bModel step4bModel) {
		String text = "Mark all measured value columns where this Date and Time corresponds to.";
		step4bPanel = new Step4bPanel(text);
		
		dateAndTimeController = new DateAndTimeController(step4bModel.getDateAndTimeModel());
		dateAndTimeController.mark(step4bPanel.getMarkingColor());
		
		tableController = TableController.getInstance();
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.allowMultipleSelection();
		tableController.addMultipleSelectionListener(new SelectionChanged());

	}
	
	@Override
	public String getDescription() {
		return "Step 4: Solve ambiguities";
	}

	@Override
	public JPanel getStepPanel() {
		return step4bPanel;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		int[] selectedColumns = tableController.getSelectedColumns();
		
		for (int column: selectedColumns) {
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(column);
			dateAndTimeController.assign(mv);
		}
		DateAndTime dtm = ModelStore.getInstance().getNextUnassignedDateAndTime();
		if (dtm != null) {
			Step4bModel step4bModel = new Step4bModel(dtm);
			MainController.getInstance().setStepController(new Step4bController(step4bModel));
		} else {
			DateAndTime dtm2 = ModelStore.getInstance().getNextDateAndTimeModelWithMissingValues();
			
			if (dtm2 != null) 
				MainController.getInstance().setStepController(new Step5bController(new Step5bModel(dtm2)));
		}		
	}
	
	private class SelectionChanged implements TableController.MultipleSelectionListener {

		@Override
		public void columnSelectionChanged(int[] selectedColumns) {
			for (int column: selectedColumns) {
				MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(column);
				if (mv == null) {
					logger.error("This is not a measured value.");
					tableController.deselectColumn(column);
					return;
				}

				if (dateAndTimeController.isAssigned(mv)) {
					logger.error("Date and Time already set for this measured value.");
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
