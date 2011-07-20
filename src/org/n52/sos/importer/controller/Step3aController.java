package org.n52.sos.importer.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3aModel;
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
		List<String> selection = ModelStore.getInstance().getColumnFromStore(number);
		if (selection == null) {
			selection = new ArrayList<String>();
			selection.add("Undefined");
		}
		step3Panel = new Step3Panel();
		step3Panel.restore(selection);

		tableController.colorColumn(Color.lightGray, number);
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.turnSelectionOff();
	}
	
	@Override
	public void saveSettings() {
		int number = step3aModel.getSelectedColumn();
		List<String> selection = new ArrayList<String>();
		step3Panel.store(selection);
		ModelStore.getInstance().putColumnIntoStore(number, selection);	
		TableController.getInstance().setColumnHeading(number, selection.get(0));		
			
		if (selection.get(0).equals("Measured Value")) {
			MeasuredValue mv = null;
			if (selection.get(1).equals("Numeric Value")) {
				NumericValue nv = new NumericValue();
				String[] separators = selection.get(2).split(":");
				nv.setDecimalSeparator(separators[0]);
				nv.setThousandsSeparator(separators[1]);
				mv = nv;
			}
			mv.setTableElement(new Column(number));
			ModelStore.getInstance().add(mv);
		} else if (selection.get(0).equals("Date & Time")) {
			if (selection.get(1).equals("Combination")) {
				String pattern = selection.get(2);
				DateAndTimeController dtc = new DateAndTimeController();
				dtc.assignPattern(pattern, new Column(number));			
				DateAndTime dtm = dtc.getDateAndTime();
				ModelStore.getInstance().add(dtm);
			}
		} else if (selection.get(0).equals("Feature of Interest")) {
			FeatureOfInterest foi = new FeatureOfInterest();
			foi.setTableElement(new Column(number));
			ModelStore.getInstance().add(foi);
		} else if (selection.get(0).equals("Observed Property")) {
			ObservedProperty op = new ObservedProperty();
			op.setTableElement(new Column(number));
			ModelStore.getInstance().add(op);
		} else if (selection.get(0).equals("Unit of Measurement")) {
			UnitOfMeasurement uom = new UnitOfMeasurement();
			uom.setTableElement(new Column(number));
			ModelStore.getInstance().add(uom);
		} else if (selection.get(0).equals("Sensor Name")) {
			Sensor sm = new Sensor();
			sm.setTableElement(new Column(number));
			ModelStore.getInstance().add(sm);
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
		return true;
	}

	@Override
	public StepController getNext() {
		int nextColumn = step3aModel.getSelectedColumn() + 1;
		if (nextColumn == tableController.getColumnCount())
			return null;
		
		return new Step3aController(new Step3aModel(nextColumn));
	}	
}
