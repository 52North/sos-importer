package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step3Controller;
import org.n52.sos.importer.controller.TableController;

public class Step3Test {
	
	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"2011-10-12", "2011-10-13"},{"12.12", "123.123"},{"123.123", "32.123"}};
		TableController.getInstance().setContent(o); 

		f.setStepController(new Step3Controller());
	}
}
