package org.n52.sos.importer.controller;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.bean.Resource;
import org.n52.sos.importer.bean.ModelStore;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step5bModel;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;
import org.n52.sos.importer.view.Step5bPanel;
import org.n52.sos.importer.view.dateAndTime.MissingComponentPanel;

public class Step5bController extends StepController {

	private static final long serialVersionUID = 1L;
	
	private Step5bPanel step5bPanel;
	
	private DateAndTimeController dateAndTimeController;
	
	public Step5bController(Step5bModel step5bModel) {
		dateAndTimeController = new DateAndTimeController(step5bModel.getDateAndTimeModel());
		step5bPanel = new Step5bPanel();
		
		TableController.getInstance().deselectAllColumns();
		TableController.getInstance().turnSelectionOff();
		
		dateAndTimeController.mark(step5bModel.getMarkingColor());
		
		List<MissingComponentPanel> addMissingComponentPanels = dateAndTimeController.getMissingComponentPanels();		
		step5bPanel.addMissingComponentPanels(addMissingComponentPanels);
	}
	
	@Override
	public String getDescription() {
		return "Step 5b: Add missing time data";
	}

	@Override
	public JPanel getStepPanel() {
		return step5bPanel;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		dateAndTimeController.assignMissingComponentValues();	
		
		DateAndTimeModel dtm = ModelStore.getInstance().getNextDateAndTimeModelWithMissingValues();
		
		if (dtm != null) {
			MainController.getInstance().setStepController(new Step5bController(new Step5bModel(dtm)));
		} else {
			//if there is a measurement column without any feature of interest,
			//observed property, unit of measurement or sensor name do:
			Resource r = ModelStore.getInstance().getMissingResourceForMeasuredValues();
			if (r != null) {
				Step6aController step6aController = new Step6aController(new Step6aModel(r));
				MainController.getInstance().setStepController(step6aController);
			}
		}
	}
}
