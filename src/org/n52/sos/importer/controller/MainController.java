package org.n52.sos.importer.controller;

import org.n52.sos.importer.view.MainFrame;

public class MainController {
	
	private static MainController instance = null;

	private final MainFrame mainFrame = new MainFrame();
	
	private MainController() {
	}
	
	public void setStepController(StepController stepController) {
	    DescriptionController.getInstance().setText(stepController.getDescription());
	    mainFrame.setStepPanel(stepController.getStepPanel());
		BackNextController.getInstance().setStepController(stepController);
	}

	public static MainController getInstance() {
		if (instance == null)
			instance = new MainController();
		return instance;
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainController.getInstance().setStepController(new Step1Controller());
            }
        });
	}
}
