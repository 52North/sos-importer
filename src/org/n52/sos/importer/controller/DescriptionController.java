package org.n52.sos.importer.controller;

import org.n52.sos.importer.view.DescriptionPanel;

public class DescriptionController {
	
	private static DescriptionController instance = null;

	private final DescriptionPanel descriptionPanel = new DescriptionPanel();
	
	private DescriptionController() {
	}

	public static DescriptionController getInstance() {
		if (instance == null)
			instance = new DescriptionController();
		return instance;
	}
	
	public void setText(String stepDescription) {
		descriptionPanel.setText(stepDescription);
	}
}
