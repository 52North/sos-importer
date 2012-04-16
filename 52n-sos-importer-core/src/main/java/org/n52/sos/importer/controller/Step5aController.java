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
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step5Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * lets the user add missing metadata for identified date&times
 * @author Raimund
 *
 */
public class Step5aController extends StepController {

	private static final Logger logger = Logger.getLogger(Step5aController.class);
	
	private Step5aModel step5aModel;
	
	private Step5Panel step5Panel;
	
	private DateAndTimeController dateAndTimeController;
	
	private TableController tabController;

	private int firstLineWithData;

	
	public Step5aController(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		this.tabController =  TableController.getInstance();
	}
	
	public Step5aController(Step5aModel step5aModel,int firstLineWithData) {
		this(firstLineWithData);
		this.step5aModel = step5aModel;
	}
	
	@Override
	public void loadSettings() {				
		DateAndTime dateAndTime = step5aModel.getDateAndTime();
		dateAndTimeController = new DateAndTimeController(dateAndTime);
		List<Component> components = step5aModel.getMissingDateAndTimeComponents();
		dateAndTimeController.setMissingComponents(components);
		dateAndTimeController.unassignMissingComponentValues();
		
		String description = step5aModel.getDescription();
		List<MissingComponentPanel> missingComponentPanels = dateAndTimeController.getMissingComponentPanels();	
		step5Panel = new Step5Panel(description, missingComponentPanels);
		
		tabController.turnSelectionOff();
		dateAndTimeController.markComponents();	
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public void saveSettings() {
		dateAndTimeController.assignMissingComponentValues();	
		
		List<Component> components = dateAndTimeController.getMissingComponents();
		step5aModel.setMissingDateAndTimeComponents(components);
		
		tabController.clearMarkedTableElements();
		tabController.turnSelectionOn();
		
		dateAndTimeController = null;
		step5Panel = null;
	}
	
	@Override
	public void back() {
		tabController.clearMarkedTableElements();
		tabController.turnSelectionOn();
		
		dateAndTimeController = null;
		step5Panel = null;
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step5aDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step5Panel;
	}

	@Override
	public boolean isNecessary() {
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		
		if (dtm == null) {
			logger.info("Skip Step 5a since there are not any Date&Times" +
				" with missing values");
			return false;
		}
		
		step5aModel = new Step5aModel(dtm);
		return true;
	}
	
	@Override
	public StepController getNext() {	
		dateAndTimeController = new DateAndTimeController();
		DateAndTime dtm = dateAndTimeController.getNextDateAndTimeWithMissingValues();
		if (dtm != null) return new Step5aController(new Step5aModel(dtm),this.firstLineWithData);
		
		dateAndTimeController = null;
		return null;	
	}
	
	@Override
	public StepController getNextStepController() {
		return new Step5cController(this.firstLineWithData);
	}

	@Override
	public StepModel getModel() {
		return this.step5aModel;
	}
}
