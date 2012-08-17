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

import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * lets the user add missing metadata for identified positions
 * @author Raimund
 *
 */
public class Step5cController extends StepController {

	private static final Logger logger = Logger.getLogger(Step5cController.class);
	
	private Step5cModel step5cModel;
	
	private Step5Panel step5Panel;
	
	private PositionController positionController;
	
	private TableController tableController;
	
	private int firstLineWithData;
	
	public Step5cController(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		this.tableController = TableController.getInstance();
	}
	
	public Step5cController(Step5cModel step5cModel,int firstLineWithData) {
		this(firstLineWithData);
		this.step5cModel = step5cModel;
	}
	
	@Override
	public void loadSettings() {			
		Position position = step5cModel.getPosition();
		positionController = new PositionController(position);
		List<Component> components = step5cModel.getMissingPositionComponents();
		positionController.setMissingComponents(components);
		positionController.unassignMissingComponentValues();
		
		String description = step5cModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = positionController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		
		tableController.turnSelectionOff();
		positionController.markComponents();
	}
	
	
	@Override
	public void saveSettings() {
		positionController.assignMissingComponentValues();	
		
		List<Component> components = positionController.getMissingComponents();
		step5cModel.setMissingPositionComponents(components);
		
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		positionController = null;
		step5Panel = null;
	}
	
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.turnSelectionOn();
		
		positionController = null;
		step5Panel = null;
	}

	@Override
	public boolean isFinished() {
		return positionController.checkMissingComponentValues();
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step5cDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		positionController = new PositionController();
		Position p = positionController.getNextPositionWithMissingValues();
		
		if (p == null) {
			logger.info("Skip Step 5c since there are not any Positions" +
					" with missing values");
			return false;
		}
		
		step5cModel = new Step5cModel(p);
		return true;
	}
	
	@Override
	public StepController getNext() {
		positionController = new PositionController();
		Position p = positionController.getNextPositionWithMissingValues();
		if (p != null) return new Step5cController(new Step5cModel(p),this.firstLineWithData);
		
		positionController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step6aController(this.firstLineWithData);
	}

	@Override
	public StepModel getModel() {
		return this.step5cModel;
	}
}
