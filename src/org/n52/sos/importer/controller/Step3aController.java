package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3aModel;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step3Panel;

public class Step3aController extends StepController {

	private Step3Panel step3Panel;
	
	private Step3aModel step3aModel;
	
	private TableController tableController = TableController.getInstance();
	
	public Step3aController() {
		step3Panel = new Step3Panel();
		step3aModel = new Step3aModel();
	}
	
	@Override
	public String getDescription() {
		return "Step 3a: Choose Metadata for Columns";
	}

	@Override
	public JPanel getStepPanel() {
		return step3Panel;
	}
	
	@Override
	public void loadSettings() {		
		tableController.allowSingleSelection();
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addSingleSelectionListener(new TableSelectionChanged());
		
		int number = step3aModel.getSelectedColumn();
		tableController.selectColumn(number);
	}
	
	@Override
	public void saveSettings() {
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		step3aModel.putColumnIntoStore(step3aModel.getSelectedColumn(), selection);	
	}

	@Override
	public StepController getNextStepController() {

		List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
		List<ObservedProperty> observedProperties = new ArrayList<ObservedProperty>();
		List<UnitOfMeasurement> unitOfMeasurements = new ArrayList<UnitOfMeasurement>();
		List<Sensor> sensorNames = new ArrayList<Sensor>();
		LinkedList<DateAndTime> dateAndTimes = new LinkedList<DateAndTime>();
		
		for (Integer k: step3aModel.getStoredColumns()) {
			System.out.print(k + ": ");
			List<String> column = step3aModel.getColumnFromStore(k);
			for (String c: column)  {
				System.out.print(c + " ");
			}
			System.out.println();
		}
		
		for (Integer k: step3aModel.getStoredColumns()) {
			List<String> column = step3aModel.getColumnFromStore(k);
			TableController.getInstance().setColumnHeading(k, column.get(0));
			
			if (column.get(0).equals("Measured Value")) {
				MeasuredValue mv = null;
				if (column.get(1).equals("Numeric Value"))
					mv = new NumericValue();
				mv.setTableElement(new Column(k));
				ModelStore.getInstance().addMeasuredValue(mv);
			} else if (column.get(0).equals("Date & Time")) {
				if (column.get(1).equals("Combination")) {
					String pattern = column.get(2);
					DateAndTimeController dtc = new DateAndTimeController();
					dtc.assignPattern(pattern, new Column(k));
					
					DateAndTime dtm = dtc.getDateAndTime();
					dateAndTimes.add(dtm);
				}
			} else if (column.get(0).equals("Feature Of Interest")) {
				FeatureOfInterest foi = new FeatureOfInterest();
				foi.setTableElement(new Column(k));
				featuresOfInterest.add(foi);
			} else if (column.get(0).equals("Observed Property")) {
				ObservedProperty op = new ObservedProperty();
				op.setTableElement(new Column(k));
				observedProperties.add(op);
			} else if (column.get(0).equals("Unit of Measurement")) {
				UnitOfMeasurement uom = new UnitOfMeasurement();
				uom.setTableElement(new Column(k));
				unitOfMeasurements.add(uom);
			} else if (column.get(0).equals("Sensor Name")) {
				Sensor sm = new Sensor();
				sm.setTableElement(new Column(k));
				sensorNames.add(sm);
			}
		}
		
		ModelStore.getInstance().setDateAndTimeModelIterator(dateAndTimes.listIterator());
		
		DateAndTime dtm = ModelStore.getInstance().getNextUnassignedDateAndTime();
		if (dtm != null) {
			Step4bModel step4bModel = new Step4bModel(dtm);
			return new Step4bController(step4bModel);
		}
		
		return null;
		
		
		// TODO Auto-generated method stub
		
	}
	
	private class TableSelectionChanged implements TableController.SingleSelectionListener {

		@Override
		public void columnSelectionChanged(int newColumn) {
			int oldColumn = step3aModel.getSelectedColumn();
			List<String> selection = new ArrayList<String>();
			step3Panel.store(selection);
			step3aModel.putColumnIntoStore(oldColumn, selection);
			    
			selection = step3aModel.getColumnFromStore(newColumn);
			step3Panel.clearAdditionalPanels();
			
			if (selection == null) step3Panel.restoreDefault();
			else step3Panel.restore(selection);
			
			MainController.getInstance().pack();
			step3aModel.setSelectedColumn(newColumn);
		}

		@Override
		public void rowSelectionChanged(int selectedRow) {
			// TODO Auto-generated method stub
			
		}
	}	
}
