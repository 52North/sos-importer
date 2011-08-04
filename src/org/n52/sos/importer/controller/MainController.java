package org.n52.sos.importer.controller;

import org.n52.sos.importer.interfaces.StepController;
import org.n52.sos.importer.tooltips.ToolTips;
import org.n52.sos.importer.view.DescriptionPanel;
import org.n52.sos.importer.view.MainFrame;

/**
 * controls the main frame of the application,
 * changes step panels and exits the application
 * @author Raimund
 *
 */
public class MainController {
	
	private static MainController instance = null;

	private final MainFrame mainFrame = new MainFrame(this);
	
	private MainController() {
		ToolTips.loadSettings();
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
		mainFrame.showExitDialog();
	}
	
	public void pack() {
		mainFrame.pack();
	}
}
