package org.n52.sos.importer.controller;

import javax.swing.JPanel;

public class Step3bController extends StepController {

	@Override
	public void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return "Step 3b: Choose metadata for rows";
	}

	@Override
	public JPanel getStepPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StepController getNextStepController() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	private class RowSelectionChanged {
		@Override
		public void rowSelectionChanged(int newRow) {
			int oldRow = step3Model.getSelectedRow();
			List<String> selection = new ArrayList<String>();
			step3Panel.store(selection);
			step3Model.putRowIntoStore(oldRow, selection);
			
			selection = step3Model.getRowFromStore(newRow);
			step3Panel.clearAdditionalPanels();
			
			if (selection == null) step3Panel.restoreDefault();
			else step3Panel.restore(selection);
			
			MainController.getInstance().pack();
			step3Model.setSelectedRow(newRow);
		}
	}
	 */

}
