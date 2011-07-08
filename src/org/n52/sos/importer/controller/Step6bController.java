package org.n52.sos.importer.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.Step6bPanel;

/**
 * Create missing resources for measured values
 * @author Raimund
 */
public class Step6bController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6bController.class);
	
	private Step6bPanel step6bView;
	
	private Step6bModel step6bModel;
	
	private TableController tableController = TableController.getInstance();
	
	public Step6bController() {	
	}
	
	public Step6bController(Step6bModel step6aModel) {
		this.step6bModel = step6aModel;
	}
	
	@Override
	public void loadSettings() {
		Resource resource = step6bModel.getResource();
		resource.unassignFromMeasuredValues();
		String questionText = "New " + resource.toString();	//TODO
		String markingText = "Mark all measured value columns where the " + resource + " corresponds to:";
		
		step6bView = new Step6bPanel(questionText, markingText);
		step6bView.setResourceName(resource.getName());
		step6bView.setResourceURI(resource.getURIString());
		
		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addMultipleSelectionListener(new SelectionChanged());
		tableController.allowMultipleSelection();
		
		int[] selectedColumns = step6bModel.getSelectedColumns();
		for (int column: selectedColumns) 
			tableController.selectColumn(column);
	}	
	
	@Override
	public void saveSettings() {
		//assign selected measured value columns or rows this type
		Resource resource = step6bModel.getResource();
		String name = step6bView.getResourceName();
		resource.setName(name);
	
		String uri = step6bView.getResourceURI();
		
		if (!uri.equals("")) {
			URI URI = null;
			try {
				URI = new URI(uri);
			} catch (URISyntaxException e) {
			}
			resource.setURI(URI);
		}
		logger.info("Resource URI:" + resource.getURIString());
		
		int[] columns = tableController.getSelectedColumns();	
		step6bModel.setSelectedColumns(columns);
		
		if (resource instanceof FeatureOfInterest)
			ModelStore.getInstance().addFeatureOfInterest((FeatureOfInterest)resource);
		
		for (int c: columns) {
			MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(c); 
			resource.assign(mv);
		}
	}
	
	@Override
	public StepController getNext() {
		Resource r = getMissingResourceForMeasuredValues();
		
		if (r != null) return new Step6bController(new Step6bModel(r));
			
		return null;
	}
	
	@Override
	public StepController getNextStepController() {		
		return new Step6cController();	
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
				
				Resource resource = step6bModel.getResource();
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
		return "Step 6b: Add missing Metadata";
	}

	@Override
	public JPanel getStepPanel() {
		return step6bView;
	}

	@Override
	public boolean isNecessary() {
		Resource r = getMissingResourceForMeasuredValues();	
		if (r == null) return false;
		
		step6bModel = new Step6bModel(r);
		
		return true;
	}

	@Override
	public boolean isFinished() {
		//get and check name
		String name = step6bView.getResourceName();
		if (name == null) {
			logger.error("No Name given.");
			return false;
		}
		
		//get and check URI
		String uri = step6bView.getResourceURI();
		URI URI = null;
		try {
			URI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Wrong URI", e);
			return false;
		}
		
		//get selected columns
		int[] columns = tableController.getSelectedColumns();
		if (columns.length == 0) {
			logger.warn("No Columns selected.");
			return false;
		}
		
		return true;
	}
}
