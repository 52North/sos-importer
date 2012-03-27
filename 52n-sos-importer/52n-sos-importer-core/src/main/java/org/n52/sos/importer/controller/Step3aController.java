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
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step3Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.step3.SelectionPanel;

/**
 * lets the user identify different types of metadata 
 * for each column in the CSV file
 * @author Raimund
 *
 */
public class Step3aController extends StepController {

	private Step3Panel step3Panel;
	
	private Step3Model step3Model;
	
	private TableController tableController = TableController.getInstance();	
	
	public Step3aController(int firstLineWithData, boolean useHeader) {
		this(new Step3Model(0, firstLineWithData, useHeader));		
	}
	
	public Step3aController(Step3Model step3Model) {
		this.step3Model = step3Model;
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step3aDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step3Panel;
	}
	
	@Override
	public void loadSettings() {	
		int number = step3Model.getMarkedColumn();
		int fLWData = step3Model.getFirstLineWithData();
		Column column = new Column(number,fLWData);
		List<String> selection = step3Model.getSelectionForColumn(number);
		step3Panel = new Step3Panel(step3Model.getFirstLineWithData());
		if(selection != null) {
			step3Panel.restore(selection);
		}
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
		step3Model.addSelection(selection);
		
		int number = step3Model.getMarkedColumn();
		int firstLineWithData = step3Model.getFirstLineWithData();
		SelectionPanel selP = step3Panel.getLastChildPanel();
		selP.assign(new Column(number,firstLineWithData));
		
		if (step3Model.getMarkedColumn() + 1 == TableController.getInstance().getColumnCount()) {			
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
		step3Model.addSelection(selection);
		int number = step3Model.getMarkedColumn()-1;
		if(number >= 0) {
			step3Model.setMarkedColumn(number);
			tableController.setColumnHeading(number, selection.get(0));	
			tableController.clearMarkedTableElements();
			tableController.setTableSelectionMode(TableController.CELLS);
			tableController.turnSelectionOn();
		}
		step3Panel = null;
	}

	@Override
	public StepController getNextStepController() {		
		return new Step4aController( new Step4aModel( null, step3Model.getFirstLineWithData() ) );	
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		// check if the current column is the last in the file
		// if yes, check for at least one measured value column
		if (step3Model.getMarkedColumn() + 1 == TableController.getInstance().getColumnCount()) {
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
		// check if we have reached the last column
		// if not, return a new Step3aController handing over the current 
		// Step3Model
		int nextColumn = step3Model.getMarkedColumn() + 1;
		if (nextColumn == tableController.getColumnCount()) {
			return null;
		}
		this.step3Model.setMarkedColumn(nextColumn);
		return new Step3aController(this.step3Model);
	}	
	
	@Override
	public boolean isStillValid() {
		//TODO: check whether the CSV file parsing settings have been changed
		if (step3Model.getMarkedColumn() == 0) return false;
		return true;
	}

	@Override
	public StepModel getModel() {
		return this.step3Model;
	}
}
