package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step3Controller;
import org.n52.sos.importer.controller.TableController;

public class Step3Test {
	
	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o); 

		f.setStepController(new Step3Controller());

		
	}
}
