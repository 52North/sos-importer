package org.n52.sos.importer.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.Step6aPanel;

/**
 * Create missing resources for measured values
 * @author Raimund
 */
public class Step6aController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6aController.class);
	
	private Step6aPanel step6aView;
	
	private Step6aModel step6aModel;
	
	private TableController tableController = TableController.getInstance();
	
	public Step6aController(Step6aModel step6aModel) {
		this.step6aModel = step6aModel;
		Resource resource = step6aModel.getResource();
		resource.unassignFromMeasuredValues();
		String questionText = "New " + resource.toString();	//TODO
		String markingText = "Mark all measured value columns where the " + resource + " corresponds to:";
		
		step6aView = new Step6aPanel(questionText, markingText);
		step6aView.setResourceName(resource.getName());
		//step6aView.setResourceURI(resource.getURI().toString());
		
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addMultipleSelectionListener(new SelectionChanged());
		tableController.allowMultipleSelection();
		
		int[] selectedColumns = step6aModel.getSelectedColumns();
		for (int column: selectedColumns) 
			tableController.selectColumn(column);
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNextStepController() {		
		//get and check name
		String name = step6aView.getResourceName();
		if (name == null) {
			logger.error("No Name given.");
			return;
		}
		
		//get and check URI
		String uri = step6aView.getResourceURI();
		URI URI = null;
		try {
			URI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Wrong URI", e);
			return;
		}
		
		//get selected columns
		int[] columns = tableController.getSelectedColumns();
		if (columns.length == 0) {
			logger.warn("No Columns selected.");
			return;
		}
		step6aModel.setSelectedColumns(columns);
		
		//assign selected measured value columns or rows this type
		Resource resource = step6aModel.getResource();
		resource.setName(name);
		resource.setURI(URI);
		
		if (resource instanceof FeatureOfInterest)
			ModelStore.getInstance().addFeatureOfInterest((FeatureOfInterest)resource);
		
		for (int c: columns) {
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(c); 
			resource.assign(mv);
		}
		
		check();
	}
	
	public void check() {
		//check if there are any measured value rows or columns without this type
		//if yes, call the particular panel 
		//if no, go to the new step
		Resource r = getMissingResourceForMeasuredValues();
		
		if (r != null) {
			Step6aController step6aController = new Step6aController(new Step6aModel(r));
			MainController.getInstance().setStepController(step6aController);
		} else {
			Step7Controller step7Controller = new Step7Controller(new Step7Model(new FeatureOfInterest()));
			step7Controller.check();
		}
	}
	
	public Resource getMissingResourceForMeasuredValues() {
		List<MeasuredValue> measuredValues = ModelStore.getInstance().getMeasuredValues();
		
		for (MeasuredValue mv: measuredValues) {
			if (mv.getFeatureOfInterest() == null) 
				return new FeatureOfInterest();
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getObservedProperty() == null) {
				return new ObservedProperty();
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getUnitOfMeasurement() == null) {
				return new UnitOfMeasurement();
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getSensor() == null) {
				return new Sensor();
			}
		}
		return null;
	}
	
	protected void loadSettings() {
		// TODO Auto-generated method stub
		// rows or columns
		// which rows or columns were selected
		// name 
		// uri
		
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
				
				Resource resource = step6aModel.getResource();
				if (resource.isAssigned(mv)) {
					logger.error(resource + " already set for this measured value.");
					tableController.deselectColumn(column);
				}
			}
		}

		@Override
		public void rowSelectionChanged(int[] selectedRows) {
			// TODO Auto-generated method stub
			
		}	
	}

	@Override
	public String getDescription() {
		return "Step 6a: Add missing Metadata";
	}

	@Override
	public JPanel getStepPanel() {
		return step6aView;
	}
}
