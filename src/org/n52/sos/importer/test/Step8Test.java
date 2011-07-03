package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step8Controller;

public class Step8Test {
	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		
		f.setStepController(new Step8Controller());
	}
}
