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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step4Panel;

/**
 * solves ambiguities in case there is more than one date&time column
 * @author Raimund
 *
 */
public class Step4aController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step4aController.class);
	
	private Step4aModel step4aModel;
	
	private Step4Panel step4Panel;
	
	private TableController tableController = TableController.getInstance();

	public Step4aController() {
	}
	
	public Step4aController(Step4aModel step4bModel) {
		this.step4aModel = step4bModel;
	}
	
	@Override
	public void loadSettings() {
		String text = step4aModel.getDescription();
		String orientation = tableController.getOrientationString();
		text = text.replaceAll("ORIENTATION", orientation);
		step4Panel = new Step4Panel(text);		

		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addMultipleSelectionListener(new SelectionChanged());
		
		int[] selectedRowsOrColumns = step4aModel.getSelectedRowsOrColumns();
		for (int number: selectedRowsOrColumns) {
			Column c = new Column(number);
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
			mv.setDateAndTime(null);
			tableController.selectColumn(number);
		}
		
		DateAndTimeController dateAndTimeController = new DateAndTimeController(step4aModel.getDateAndTimeModel());	
		dateAndTimeController.markComponents();	
	}

	@Override
	public void saveSettings() {
		int[] selectedRowsOrColumns = tableController.getSelectedColumns();
		DateAndTime dateAndTime = step4aModel.getDateAndTimeModel();
		step4aModel.setSelectedRowsOrColumns(selectedRowsOrColumns);
		
		for (int number: selectedRowsOrColumns) {
			Column c = new Column(number);
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
			mv.setDateAndTime(dateAndTime);
		}
		
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();
		step4Panel = null;
	}
	
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();
		
		step4Panel = null;
	};
	
	@Override
	public String getDescription() {
		return "Step 4a: Solve Date & Time ambiguities";
	}

	@Override
	public JPanel getStepPanel() {
		return step4Panel;
	}
	
	@Override
	public boolean isNecessary() {
		int dateAndTimes = ModelStore.getInstance().getDateAndTimes().size();
		
		if (dateAndTimes == 0) {
			logger.info("Skip Step 4a since there are not any Date&Times");
			return false;
		}
		if (dateAndTimes == 1) {
			DateAndTime dateAndTime = ModelStore.getInstance().getDateAndTimes().get(0);
			logger.info("Skip Step 4a since there is just " + dateAndTime);
			
			for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
				mv.setDateAndTime(dateAndTime);
			
			return false;
		}
		
		DateAndTime dtm = getNextUnassignedDateAndTime();
		step4aModel = new Step4aModel(dtm);
		return true;
	}

	@Override
	public StepController getNext() {
		DateAndTime dtm = getNextUnassignedDateAndTime();
		if (dtm != null) {
			Step4aModel step4aModel = new Step4aModel(dtm);
			return new Step4aController(step4aModel);
		} 
	
		return null;
	}
	
	@Override
	public StepController getNextStepController() {	
		return new Step4bController(); 
	}
	
	private DateAndTime getNextUnassignedDateAndTime() {
		boolean unassignedMeasuredValues = areThereAnyUnassignedMeasuredValuesLeft();
		if (!unassignedMeasuredValues) return null;
		
		for (DateAndTime dateAndTime: ModelStore.getInstance().getDateAndTimes())
			if (!isAssignedToMeasuredValue(dateAndTime))
				return dateAndTime;	
		return null;
	}
	
	private boolean areThereAnyUnassignedMeasuredValuesLeft() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) 
			if (mv.getDateAndTime() == null)
				return true;
		return false;
	}
	
	private boolean isAssignedToMeasuredValue(DateAndTime dateAndTime) {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) 
			if (dateAndTime.equals(mv.getDateAndTime()))
				return true;
		return false;
	}
	
	private class SelectionChanged implements TableController.MultipleSelectionListener {

		@Override
		public void columnSelectionChanged(int[] selectedColumns) {
			for (int number: selectedColumns) {
				Column c = new Column(number);
				MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
				if (mv == null) {
					JOptionPane.showMessageDialog(null,
						    "This is not a measured value.",
						    "Info",
						    JOptionPane.INFORMATION_MESSAGE);
					tableController.deselectColumn(number);
					return;
				}

				//measured value has already assigned a date&time
				if (mv.getDateAndTime() != null) {
					JOptionPane.showMessageDialog(null,
						    "Date and Time are already set for this measured value.",
						    "Info",
						    JOptionPane.INFORMATION_MESSAGE);
					tableController.deselectColumn(number);
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
		return true;
	}
}
