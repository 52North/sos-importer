/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step3Panel;
import org.n52.sos.importer.view.step3.SelectionPanel;

/**
 * lets the user identify different types of metadata 
 * for each column in the CSV file
 * @author Raimund
 *
 */
public class Step3aController extends StepController {

	private Step3Panel step3Panel;
	
	private Step3aModel step3aModel;
	
	private TableController tableController = TableController.getInstance();	
	
	public Step3aController(int firstLineWithData) {
		this(new Step3aModel(0, firstLineWithData));		
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
		int number = step3aModel.getMarkedColumn();
		Column column = new Column(number);
		List<String> selection = step3aModel.getSelection();

		step3Panel = new Step3Panel(step3aModel.getFirstLineWithData());
		step3Panel.restore(selection);
		step3Panel.getLastChildPanel().unAssign(column);

		tableController.mark(column);
		tableController.setColumnHeading(number, "???");
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.turnSelectionOff();
	}
	
	@Override
	public void saveSettings() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3aModel.setSelection(selection);
		
		int number = step3aModel.getMarkedColumn();
		SelectionPanel selP = step3Panel.getLastChildPanel();
		selP.assign(new Column(number));
		// FIXME store the selection in the XMLModel (add ref to MainController)
		
		if (step3aModel.getMarkedColumn() + 1 == TableController.getInstance().getColumnCount()) {			
			DateAndTimeController dtc = new DateAndTimeController();
			dtc.mergeDateAndTimes();
			
			PositionController pc = new PositionController();
			pc.mergePositions();
		}
		
		tableController.setColumnHeading(number, selection.get(0));
		tableController.clearMarkedTableElements();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.turnSelectionOn();
		
		step3Panel = null;
	}
	
	@Override
	public void back() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3aModel.setSelection(selection);
		int number = step3aModel.getMarkedColumn();
		
		tableController.setColumnHeading(number, selection.get(0));	
		tableController.clearMarkedTableElements();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.turnSelectionOn();
		
		step3Panel = null;
	}

	@Override
	public StepController getNextStepController() {		
		return new Step4aController();	
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		// check if the current column is the last in the file
		// if yes, check for at least one measured value column
		if (step3aModel.getMarkedColumn() + 1 == TableController.getInstance().getColumnCount()) {
			List<String> currentSelection = new ArrayList<String>();
			step3Panel.store(currentSelection);
			
			if (ModelStore.getInstance().getMeasuredValues().size() == 0 && currentSelection.get(0) != "Measured Value") {
				JOptionPane.showMessageDialog(null,
					    "You have to specify at least one measured value column.",
					    "Measured value column missing",
					    JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		return true;
	}

	@Override
	public StepController getNext() {
		int nextColumn = step3aModel.getMarkedColumn() + 1;
		if (nextColumn == tableController.getColumnCount()) {
			return null;
		}
		return new Step3aController(new Step3aModel(nextColumn, this.step3aModel.getFirstLineWithData()));
	}	
	
	@Override
	public boolean isStillValid() {
		//TODO: check whether the CSV file parsing settings have been changed
		if (step3aModel.getMarkedColumn() == 0) return false;
		return true;
	}

	@Override
	public StepModel getModel() {
		return this.step3aModel;
	}
}
