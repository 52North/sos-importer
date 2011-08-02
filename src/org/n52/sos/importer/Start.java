package org.n52.sos.importer;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step1Controller;

public class Start {
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainController.getInstance().setStepController(new Step1Controller());
            }
        });
	}
}
