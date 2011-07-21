package org.n52.sos.importer.controller;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.view.Step4aPanel;

public class Step4bController extends StepController {

	private static final Logger logger = Logger.getLogger(Step4bController.class);
	
	private Step4bModel step4bModel;
	
	private TableController tableController = TableController.getInstance();;
	
	private Step4aPanel step4aPanel;
	
	public Step4bController() {
	}
	
	public Step4bController(Step4bModel step4bModel) {
		this.step4bModel = step4bModel;
	}

	@Override
	public void loadSettings() {
		Resource resource = step4bModel.getResource();
		int[] selectedRowsOrColumns = step4bModel.getSelectedRowsOrColumns();
		
		String orientation = null;
		if (resource.getTableElement() instanceof Column) {
			tableController.setTableSelectionMode(TableController.COLUMNS);
			orientation = "column";		
			
			for (int column: selectedRowsOrColumns) {
				MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(column);
				resource.unassign(mv);
			}
		} else if (resource.getTableElement() instanceof Row) {
			tableController.setTableSelectionMode(TableController.ROWS);
			orientation = "row";
			
			//TODO
		}
			
		String text = step4bModel.getDescription();

		text = text.replaceAll("ORIENTATION", orientation);
		text = text.replaceAll("RESOURCE", resource.toString());
			
		resource.getTableElement().mark(tableController.getMarkingColor());
		
		tableController.allowMultipleSelection();
		tableController.addMultipleSelectionListener(new SelectionChanged());
		
		step4aPanel = new Step4aPanel(text);
	}
	
	@Override
	public void saveSettings() {
		Resource resource = step4bModel.getResource();
		
		if (resource.getTableElement() instanceof Column) {
			int[] selectedColumns = tableController.getSelectedColumns();
			
			for (int column: selectedColumns) {
				MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAtColumn(column);
				resource.assign(mv);
			}
			step4bModel.setSelectedRowsOrColumns(selectedColumns);
		} else if (resource.getTableElement() instanceof Row) {
			//TODO
		}

		step4aPanel = null;
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
				Resource resource = step4bModel.getResource();
				if (resource.isAssigned(mv)) {
					logger.error(resource + " already set for this measured value.");
					tableController.deselectColumn(column);
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
	public String getDescription() {
		return "Step 4b: Solve ambiguities";
	}


	@Override
	public JPanel getStepPanel() {
		return step4aPanel;
	}


	@Override
	public StepController getNextStepController() {
		return new Step5aController();
	}


	@Override
	public boolean isNecessary() {	
		Resource resourceType = new FeatureOfInterest();
		Resource resource = null;
		
		while (resourceType != null) {
			int number = resourceType.getList().size();
			// in case there is just one resource of this type:
			if (number == 1) {
				Resource oneResource = resourceType.getList().get(0);
				logger.info("Skip Step 4b for " + resourceType + "s" +
						" since there is just " + oneResource);
				
				for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
					oneResource.assign(mv);
			
			//in case there are more than two resources of this type:
			} else if (resource == null && number >= 2){
				ResourceController rc = new ResourceController();
				resource = rc.getNextUnassignedResource(resourceType);
			} else { //number == 0
				logger.info("Skip Step 4b for " + resourceType + "s" +
						" since there are not any " + resourceType + "s");
			}
			resourceType = resourceType.getNextResourceType();
		}
		
		step4bModel = new Step4bModel(resource);
		return resource != null;
	}
	
	@Override
	public StepController getNext() {
		Resource resourceType = step4bModel.getResource();
		ResourceController rc = new ResourceController();
		
		Resource nextResource = null;
		while (resourceType != null) {
			nextResource = rc.getNextUnassignedResource(resourceType);
			if (nextResource != null)
				return new Step4bController(new Step4bModel(nextResource));
			resourceType = resourceType.getNextResourceType();
		}
		return null;
	}	

	@Override
	public boolean isFinished() {
		return true;
	}
}
