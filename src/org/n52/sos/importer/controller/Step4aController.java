package org.n52.sos.importer.controller;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.view.Step4aPanel;

public class Step4aController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step4aController.class);
	
	private Step4aModel step4bModel;
	
	private Step4aPanel step4bPanel;
	
	private TableController tableController;
	
	private DateAndTimeController dateAndTimeController;
	
	public Step4aController() {
	}
	
	public Step4aController(Step4aModel step4bModel) {
		this.step4bModel = step4bModel;
	}
	
	@Override
	public void loadSettings() {
		String text = "Mark all measured value columns where this Date and Time corresponds to.";
		step4bPanel = new Step4aPanel(text);		

		dateAndTimeController = new DateAndTimeController(step4bModel.getDateAndTimeModel());
		dateAndTimeController.mark(step4bPanel.getMarkingColor());
		
		tableController = TableController.getInstance();
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.allowMultipleSelection();
		tableController.addMultipleSelectionListener(new SelectionChanged());
		
	}

	@Override
	public void saveSettings() {
		int[] selectedColumns = tableController.getSelectedColumns();
		
		for (int column: selectedColumns) {
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(column);
			dateAndTimeController.assign(mv);
		}
		
		step4bPanel = null;
		dateAndTimeController = null;
	}
	
	@Override
	public String getDescription() {
		return "Step 4a: Solve time ambiguities";
	}

	@Override
	public JPanel getStepPanel() {
		return step4bPanel;
	}
	
	@Override
	public boolean isNecessary() {
		int dateAndTimes = ModelStore.getInstance().getDateAndTimes().size();
		
		if (dateAndTimes == 0) return false;
		if (dateAndTimes == 1) {
			DateAndTime dateAndTime = ModelStore.getInstance().getDateAndTimes().get(0);
			
			for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
				mv.setDateAndTime(dateAndTime);
			
			return false;
		}
		
		DateAndTimeController dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextUnassignedDateAndTime();
		step4bModel = new Step4aModel(dtm);
		return true;
	}

	@Override
	public StepController getNext() {
		DateAndTime dtm = dateAndTimeController.getNextUnassignedDateAndTime();
		if (dtm != null) {
			Step4aModel step4bModel = new Step4aModel(dtm);
			return new Step4aController(step4bModel);
		} 
		
		return null;
	}
	
	@Override
	public StepController getNextStepController() {	
		return new Step5aController(); 
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

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
}
