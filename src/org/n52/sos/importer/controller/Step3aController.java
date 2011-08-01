package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.Step3aModel;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step3Panel;

public class Step3aController extends StepController {

	private Step3Panel step3Panel;
	
	private Step3aModel step3aModel;
	
	private TableController tableController = TableController.getInstance();	
	
	public Step3aController() {
	}
	
	public Step3aController(Step3aModel step3aModel) {
		this.step3aModel = step3aModel;
	}
	
	@Override
	public String getDescription() {
		return "Step 3a: Choose Metadata for the selected column";
	}

	@Override
	public JPanel getStepPanel() {
		return step3Panel;
	}
	
	@Override
	public void loadSettings() {	
		int number = step3aModel.getSelectedColumn();
		Column column = new Column(number);
		List<String> selection = step3aModel.getSelection();

		step3Panel = new Step3Panel();
		step3Panel.restore(selection);
		step3Panel.getLastChildPanel().unassign(column);

		tableController.mark(column);
		tableController.setColumnHeading(number, "???");
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.turnSelectionOff();
	}
	
	@Override
	public void back() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3aModel.setSelection(selection);
		
		int number = step3aModel.getSelectedColumn();
		tableController.setColumnHeading(number, selection.get(0));	
		tableController.clearMarkedTableElements();
		
		step3Panel = null;
	}
	
	@Override
	public void saveSettings() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3aModel.setSelection(selection);
		
		int number = step3aModel.getSelectedColumn();
		step3Panel.getLastChildPanel().assign(new Column(number));
		
		tableController.setColumnHeading(number, selection.get(0));
		tableController.clearMarkedTableElements();
		
		if (step3aModel.getSelectedColumn() + 1 == TableController.getInstance().getColumnCount()) {			
			DateAndTimeController dtc = new DateAndTimeController();
			dtc.mergeDateAndTimes();
			
			PositionController pc = new PositionController();
			pc.mergePositions();
		}
		
		step3Panel = null;
	}

	@Override
	public StepController getNextStepController() {		
		return new Step4aController();	
	}

	@Override
	public boolean isNecessary() {
		step3aModel = new Step3aModel(0);
		return true;
	}

	@Override
	public boolean isFinished() {
		/*
		if (step3aModel.getSelectedColumn() + 1 == TableController.getInstance().getColumnCount()) {
			if (ModelStore.getInstance().getMeasuredValues().size() == 0) {
				JOptionPane.showMessageDialog(null,
					    "You have to specify at least one measured value column.",
					    "Measured value column missing",
					    JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}*/
		
		return true;
	}

	@Override
	public StepController getNext() {
		int nextColumn = step3aModel.getSelectedColumn() + 1;
		if (nextColumn == tableController.getColumnCount())
			return null;
		
		return new Step3aController(new Step3aModel(nextColumn));
	}	
	
	@Override
	public boolean isStillValid() {
		//TODO: check whether the CSV file parsing settings have been changed
		if (step3aModel.getSelectedColumn() == 0) return false;
		return true;
	}
}
