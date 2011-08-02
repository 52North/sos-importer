package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step5aController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.table.Column;

public class Step5aTest {

	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		
		DateAndTime dtm1 = new DateAndTime();
		DateAndTimeController dtc = new DateAndTimeController(dtm1);
		dtc.assignPattern("HH-mm-ss", new Column(0));
		ModelStore.getInstance().add(dtm1);
		
		DateAndTime dtm2 = new DateAndTime();
		dtc = new DateAndTimeController(dtm2);
		dtc.assignPattern("dd-MM-yyyy", new Column(1));
		ModelStore.getInstance().add(dtm2);
		
		Step5aController controller = new Step5aController();
		controller.isNecessary();
		f.setStepController(controller);
	}
}
