package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step5bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step5bModel;
import org.n52.sos.importer.model.table.ColumnModel;

public class Step5bTest {

	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		
		DateAndTimeController dtc = new DateAndTimeController();
		dtc.assignPattern("HH-mm-ss", new ColumnModel(0));
		
		f.setStepController(new Step5bController(new Step5bModel(dtc.getModel())));
	}
}
