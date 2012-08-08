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

import java.io.File;
import java.io.IOException;

import org.n52.sos.importer.model.BackNextModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.xml.Model;
import org.n52.sos.importer.view.DescriptionPanel;
import org.n52.sos.importer.view.MainFrame;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * controls the main frame of the application,
 * changes step panels and exits the application
 * @author Raimund
 *
 */
public class MainController {
	
	private static MainController instance = null;

	private final MainFrame mainFrame = new MainFrame(this);
	
	private Model xmlModel;
	
	private MainController() {
		LoggingController.getInstance();
		//
		// Load the tooltips
		ToolTips.loadSettings();
		//
		// load the configuration for the ComboBoxItems at startup 
		// first call to getInstance() calls ComboBoxItems.load()
		ComboBoxItems.getInstance();
		//
		// init xmlmodel TODO load from configuration
		xmlModel = new Model();
	}
	
	public static MainController getInstance() {
		if (MainController.instance == null)
			MainController.instance = new MainController();
		return MainController.instance;
	}
	
	/**
	 * Method is called each time a button ("Next","Back", or "Finish") is clicked
	 * in the GUI.
	 * @param stepController
	 */
	public void setStepController(StepController stepController) {
	    DescriptionPanel descP = DescriptionPanel.getInstance();
	    BackNextModel bNM = BackNextController.getInstance().getModel();
	    //
	    descP.setText(stepController.getDescription());
	    stepController.loadSettings();
	    mainFrame.setStepPanel(stepController.getStepPanel());
	    xmlModel.registerProvider(stepController.getModel());
		bNM.setCurrentStepController(stepController);
	}
	
	public void updateModel() {
		xmlModel.updateModel();
	}
	
	public boolean removeProvider(StepModel sm) {
		return xmlModel.removeProvider(sm);
	}
	
	public boolean registerProvider(StepModel sm) {
		return xmlModel.registerProvider(sm);
	}
	
	public void exit() {
		mainFrame.showExitDialog();
	}
	
	public void updateTitle(String csvFilePath) {
		mainFrame.updateTitle(csvFilePath);
	}

	public boolean saveModel(File file) throws IOException {
		return xmlModel.save(file);
	}

	public String getFilename() {
		return xmlModel.getFileName();
	}
}
