package org.n52.sos.importer.controller;

import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.view.DescriptionPanel;
import org.n52.sos.importer.view.MainFrame;

public class MainController {
	
	private static MainController instance = null;

	private final MainFrame mainFrame = new MainFrame(this);
	
	private MainController() {
	}
	
	public static MainController getInstance() {
		if (instance == null)
			instance = new MainController();
		return instance;
	}
	
	public void setStepController(StepController stepController) {
	    DescriptionPanel.getInstance().setText(stepController.getDescription());
	    stepController.loadSettings();
	    mainFrame.setStepPanel(stepController.getStepPanel());
		BackNextController.getInstance().getModel().setCurrentStepController(stepController);
	}
	
	public void exit() {
		Settings.getInstance().save();
		//mainFrame.showExitDialog();
	}
	
	public void pack() {
		mainFrame.pack();
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainController.getInstance().setStepController(new Step1Controller());
            }
        });
	}
}
