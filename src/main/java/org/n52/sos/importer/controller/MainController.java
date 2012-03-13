/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
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
		LoggingController.getInstance();
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

	public void updateTitle(String csvFilePath) {
		this.mainFrame.updateTitle(csvFilePath);
	}
}
