package org.n52.sos.importer.controller;
import javax.swing.JPanel;


public abstract class StepController extends JPanel {

	private static final long serialVersionUID = 1L;

	public StepController() {
		super();
	}
	
	//protected abstract void loadSettings();
	
	public abstract String getDescription();
	
	public abstract JPanel getStepPanel();
	
	public abstract void back();
	
	public abstract void next();
}
