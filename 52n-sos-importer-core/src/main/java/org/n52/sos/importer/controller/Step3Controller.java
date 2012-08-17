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

import org.apache.log4j.Logger;
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
public class Step3Controller extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step3Controller.class);

	private Step3Panel step3Panel;
	
	/**
	 * Step3Model of this Step3Controllers
	 */
	private Step3Model step3Model;
	
	/**
	 * reference to TableController singleton instance
	 */
	private TableController tabCtrlr = TableController.getInstance();	
	
	public Step3Controller(int currentColumn, 
			int firstLineWithData, 
			boolean useHeader) {
		step3Model = new Step3Model(currentColumn, 
				firstLineWithData, 
				useHeader);	
		step3Panel = new Step3Panel(firstLineWithData);
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
		if (logger.isTraceEnabled()) {
			logger.trace("loadSettings()");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Step3Model: " + this.step3Model);
			logger.debug("Step3Panel: " + (this.step3Panel!=null?
					"[" + this.step3Panel.hashCode() + "]"
					:"null"));
		}
		int number = this.step3Model.getMarkedColumn();
		if (logger.isDebugEnabled()) {
			logger.debug("Loading settings for column# " + number);
		}
		int fLWData = this.step3Model.getFirstLineWithData();
		Column column = new Column(number,fLWData);
		List<String> selection = this.step3Model.getSelectionForColumn(number);
		if (step3Panel == null) {
			step3Panel = new Step3Panel(step3Model.getFirstLineWithData());
		}
		if(selection != null) {
			step3Panel.restore(selection);
		}
		step3Panel.getLastChildPanel().unAssign(column);

		tabCtrlr.mark(column);
		tabCtrlr.setColumnHeading(number, "???");
		tabCtrlr.setTableSelectionMode(TableController.COLUMNS);
		tabCtrlr.turnSelectionOff();
	}
	
	@Override
	public void saveSettings() {
		if (logger.isTraceEnabled()) {
			logger.trace("saveSettings()");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Start:");
			logger.debug("Step3Model: " + this.step3Model);
			logger.debug("Step3Panel: " + (this.step3Panel!=null?
					"[" + this.step3Panel.hashCode() + "]"
					:"null"));
		}
		//
		List<String> selection = new ArrayList<String>();
		SelectionPanel selP;
		int number = this.step3Model.getMarkedColumn();
		int firstLineWithData = this.step3Model.getFirstLineWithData();
		//
		this.step3Panel.store(selection);
		this.step3Model.addSelection(selection);
		selP = this.step3Panel.getLastChildPanel();
		selP.assign(new Column(number,firstLineWithData));
		//
		// when having reached the last column, merge positions and date&time
		if (this.step3Model.getMarkedColumn() + 1 == 
				tabCtrlr.getColumnCount()) {	
			DateAndTimeController dtc = new DateAndTimeController();
			dtc.mergeDateAndTimes();
			//
			PositionController pc = new PositionController();
			pc.mergePositions();
		} 
		// TODO if being date&time or position column: add group to table heading
		tabCtrlr.setColumnHeading(number, selection.get(0));
		tabCtrlr.clearMarkedTableElements();
		tabCtrlr.setTableSelectionMode(TableController.CELLS);
		tabCtrlr.turnSelectionOn();
		this.step3Panel = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("End:");
			logger.debug("Step3Model: " + this.step3Model);
			logger.debug("Step3Panel: " + (this.step3Panel!=null?
					"[" + this.step3Panel.hashCode() + "]"
					:"null"));
		}
		
	}
	
	@Override
	public void back() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3Model.addSelection(selection);
		int number = step3Model.getMarkedColumn()-1;
		if(number >= 0) {
			// TODO if being date&time or position column: add group to table heading
			tabCtrlr.setColumnHeading(number, selection.get(0));	
			tabCtrlr.clearMarkedTableElements();
			tabCtrlr.setTableSelectionMode(TableController.CELLS);
			tabCtrlr.turnSelectionOn();
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
		List<String> currentSelection = new ArrayList<String>();
		step3Panel.store(currentSelection);
		// check if the current column is the last in the file
		// if yes, check for at least one measured value column
		if ( (step3Model.getMarkedColumn() + 1) == 
				TableController.getInstance().getColumnCount() &&
				ModelStore.getInstance().getMeasuredValues().size() == 0 && 
				!currentSelection.get(0).equalsIgnoreCase(Lang.l().measuredValue())) {
			JOptionPane.showMessageDialog(null,
					Lang.l().step3aMeasureValueColMissingDialogMessage(),
					Lang.l().step3aMeasureValueColMissingDialogTitle(),
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public StepController getNext() {
		// check if we have reached the last column
		// if not, return a new Step3aController 
		int nextColumn = step3Model.getMarkedColumn() + 1;
		if (nextColumn == tabCtrlr.getColumnCount()) {
			return null;
		}
		return new Step3Controller(nextColumn, 
				this.step3Model.getFirstLineWithData(), 
				this.step3Model.getUseHeader());
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
