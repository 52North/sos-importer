package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.bean.FeatureOfInterest;
import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.ObservedProperty;
import org.n52.sos.importer.bean.Resource;
import org.n52.sos.importer.bean.SensorName;
import org.n52.sos.importer.bean.Store;
import org.n52.sos.importer.bean.UnitOfMeasurement;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.view.Step3Panel;

public class Step3Controller extends StepController {

	private Step3Panel step3Panel;
	
	private Step3Model step3Model;
	
	private TableController tableController = TableController.getInstance();
	
	public Step3Controller() {
		step3Panel = new Step3Panel();
		step3Model = new Step3Model();
		load();
	}
	
	@Override
	public String getDescription() {
		return "Step 3: Choose Metadata";
	}

	@Override
	public JPanel getStepPanel() {
		return step3Panel;
	}

	@Override
	public void back() {
		switch(step3Model.getSelectionMode()) {
		case TableController.COLUMNS:
			MainController.getInstance().setPreviousStepController();
			break;
		case TableController.ROWS:
			changeSelectionMode(TableController.COLUMNS);
			break;
		case TableController.CELLS:
			changeSelectionMode(TableController.ROWS);
			break;
		}
		
	}

	@Override
	public void next() {
		switch(step3Model.getSelectionMode()) {
		case TableController.COLUMNS:
			List<String> selection = new ArrayList<String>();
			step3Panel.store(selection);
			step3Model.putColumnIntoStore(step3Model.getSelectedColumn(), selection);
			
			changeSelectionMode(TableController.ROWS);
			break;
		case TableController.ROWS:
			changeSelectionMode(TableController.CELLS);
			break;
		case TableController.CELLS:
			
			List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
			List<ObservedProperty> observedProperties = new ArrayList<ObservedProperty>();
			List<UnitOfMeasurement> unitOfMeasurements = new ArrayList<UnitOfMeasurement>();
			List<SensorName> sensorNames = new ArrayList<SensorName>();
			
			for (Integer k: step3Model.getStoredColumns()) {
				List<String> column = step3Model.getColumnFromStore(k);
				if (column.get(0).equals("Measured Value")) {
					MeasuredValue mvc = new MeasuredValue(column.get(1));
					mvc.setColumnNumber(k);
					Store.getInstance().addMeasuredValue(mvc);
				} else if (column.get(0).equals("Date & Time")) {
					if (column.get(1).equals("Combination")) {
						String pattern = column.get(1);
						DateAndTimeController dtc = new DateAndTimeController();
						dtc.assignPattern(pattern);
						Store.getInstance().addDateAndTimeController(dtc);
					}
				} else if (column.get(0).equals("Feature Of Interest")) {
					FeatureOfInterest foi = new FeatureOfInterest();
					foi.setColumnNumber(k);
					featuresOfInterest.add(foi);
				} else if (column.get(0).equals("Observed Property")) {
					ObservedProperty op = new ObservedProperty();
					op.setColumnNumber(k);
					observedProperties.add(op);
				} else if (column.get(0).equals("Unit of Measurement")) {
					UnitOfMeasurement uom = new UnitOfMeasurement();
					uom.setColumnNumber(k);
					unitOfMeasurements.add(uom);
				} else if (column.get(0).equals("Sensor Name")) {
					SensorName sm = new SensorName();
					sm.setColumnNumber(k);
					sensorNames.add(sm);
				}
			}
			
			for (DateAndTimeController dtc: Store.getInstance().getDateAndTimeController()){
				List<JPanel> missingComponents = dtc.getMissingComponents();
				if (missingComponents.size() > 0) {
					Step5bController step5bController = new Step5bController(dtc);
					MainController.getInstance().setStepController(step5bController);
				}
			}
			
			//if there is a measurement column without any feature of interest,
			//observed property, unit of measurement or sensor name do:
			Resource r = Store.getInstance().getMissingResourceForMeasuredValues();
			if (r != null) {
				Step6aController step6aController = new Step6aController(new Step6aModel(r));
				MainController.getInstance().setStepController(step6aController);
			}

			
			//else if 1:1 mapping
			
			//featuresOfInterest is not empty
				//for each foi columns or row choose measurement column:
				//getMainFrame().setStepPanel(new Step4aPanel(getMainFrame(), Step4aPanel.FEATURE_OF_INTEREST));
				
				//while there are measurement columns or rows without any fois do:
				//getMainFrame().setStepPanel(new Step6aController(getMainFrame(), Step6aController.FEATURE_OF_INTEREST));				
			
			
			// TODO Auto-generated method stub
			break;
		}	
		
	}
	
	public void load() {
		tableController.allowSingleSelection();
		changeSelectionMode(TableController.COLUMNS);
		tableController.addSingleSelectionListener(new TableSelectionChanged());
	}

	private void changeSelectionMode(int sm) {
		switch(sm) {
		case TableController.COLUMNS:
			tableController.allowColumnSelection();
			step3Panel.setSelectionModeLabelText("For Columns");
			step3Model.setSelectionMode(TableController.COLUMNS);
			tableController.selectColumn(step3Model.getSelectedColumn());	
			break;
		case TableController.ROWS:
			tableController.allowRowSelection();
			step3Panel.setSelectionModeLabelText("For Rows");
			step3Model.setSelectionMode(TableController.ROWS);
			tableController.selectRow(step3Model.getSelectedRow());
			break;
		case TableController.CELLS:
			tableController.allowCellSelection();
			step3Panel.setSelectionModeLabelText("For Cells");
			step3Model.setSelectionMode(TableController.CELLS);
			break;
		}
	}
	
	private class TableSelectionChanged implements TableController.SingleSelectionListener {

		@Override
		public void columnSelectionChanged(int newColumn) {
			int oldColumn = step3Model.getSelectedColumn();
			List<String> selection = new ArrayList<String>();
			step3Panel.store(selection);
			step3Model.putColumnIntoStore(oldColumn, selection);
			    
			selection = step3Model.getColumnFromStore(newColumn);
			step3Panel.clearAdditionalPanels();
			
			if (selection == null) step3Panel.restoreDefault();
			else step3Panel.restore(selection);
			
			MainController.getInstance().pack();
			step3Model.setSelectedColumn(newColumn);
		}

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
	
}
