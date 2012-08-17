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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.Step6Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.resources.MissingResourcePanel;

/**
 * used to determine sensors in case there is one of the following 
 * relationships between feature of interest and observed property
 * column: 0:1, 0:n, n:0, 1:0, 1:1, n:n
 * @author Raimund
 *
 */
public class Step6bSpecialController extends StepController {
	
	private static final Logger logger = Logger.getLogger(Step6bSpecialController.class);

	private Step6bSpecialModel step6bSpecialModel;
	
	private Step6Panel step6cPanel;
	
	private MissingResourcePanel missingResourcePanel;

	private TableController tableController;

	private int firstLineWithData;
	
	public Step6bSpecialController(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		tableController = TableController.getInstance();
	}
	
	public Step6bSpecialController(Step6bSpecialModel step6bSpecialModel, int firstLineWithData) {
		this(firstLineWithData);
		this.step6bSpecialModel = step6bSpecialModel;
	}
	
	@Override
	public void loadSettings() {
		// FIXME does not work with generated
		String description = step6bSpecialModel.getDescription();
		String foiName = step6bSpecialModel.getFeatureOfInterest().getName();
		String opName = step6bSpecialModel.getObservedProperty().getName();
		
		Sensor sensor = step6bSpecialModel.getSensor();
		missingResourcePanel = new MissingResourcePanel(sensor);
		missingResourcePanel.setMissingComponent(sensor);
		ModelStore.getInstance().remove(step6bSpecialModel);
		missingResourcePanel.unassignValues();
		
		List<MissingComponentPanel> missingComponentPanels = new ArrayList<MissingComponentPanel>();
		missingComponentPanels.add(missingResourcePanel);
		
		step6cPanel = new Step6Panel(description, foiName, opName, missingComponentPanels);
	}

	@Override
	public void saveSettings() {
		missingResourcePanel.assignValues();
		ModelStore.getInstance().add(step6bSpecialModel);
		
		step6cPanel = null;
		missingResourcePanel = null;
	}

	@Override
	public String getDescription() {
		return Lang.l().step6bSpecialDescription();
	}

	@Override
	public JPanel getStepPanel() {
		return step6cPanel;
	}

	@Override
	public StepController getNextStepController() {
		return new Step6cController();
	}

	@Override
	public boolean isNecessary() {
		if (ModelStore.getInstance().getSensorsInTable().size() > 0) {
			logger.info("Skip 6b (Special) since there are sensors in the table.");
			return false;
		}
		
		if (ModelStore.getInstance().getFeatureOfInterestsInTable().size() == 0 &&
			ModelStore.getInstance().getObservedPropertiesInTable().size() == 0) {
			logger.info("Skip 6b (Special) since there are not any features of interest" +
					"and observed properties in the table.");
			return false;
		}
		
		step6bSpecialModel = getNextModel();
		return true;
		
	}
		
	public Step6bSpecialModel getNextModel() {
		int rows = tableController.getRowCount();
		int flwd = tableController.getFirstLineWithData();
		ModelStore ms = ModelStore.getInstance();

		//iterate through all measured value columns/rows
		mvLoop:
			for (MeasuredValue mv: ms.getMeasuredValues()) {
				
				rowsLoop:
					for (int i = flwd; i < rows; i++) {	
						//test if the measuredValue can be parsed
						Cell cell = new Cell(i, ((Column)mv.getTableElement()).getNumber());
						String value = tableController.getValueAt(cell);
						if (i >= firstLineWithData) {
							try {
								mv.parse(value);
							} catch (Exception e) {
								if (logger.isTraceEnabled()) {
									logger.trace("Value could not be parsed: " + value, e);
								}
								continue rowsLoop; // it okay this way because parsing test happened during step 3
							}	

							FeatureOfInterest foi = mv.getFeatureOfInterest().forThis(cell);
							ObservedProperty op = mv.getObservedProperty().forThis(cell);
							Step6bSpecialModel model = new Step6bSpecialModel(foi, op);
							// check, if for the column of foi and obsProp a model is available and if the sensor is generated in this model
							for (Step6bSpecialModel s6bsM : ms.getStep6bSpecialModels()) {
								if (s6bsM.getSensor().isGenerated() &&
										s6bsM.getFeatureOfInterest().getTableElement().equals(foi.getTableElement()) && // XXX maybe own equals
										s6bsM.getObservedProperty().getTableElement().equals(op.getTableElement())) { // XXX check if foi and obsprop 
									continue mvLoop;
								}
							}
							if (!ms.getStep6bSpecialModels().contains(model)) {
								return model;
							}
						}
					}
			}
		return null;
	}

	@Override
	public boolean isFinished() {
		return missingResourcePanel.checkValues();
	}

	@Override
	public StepController getNext() {
		Step6bSpecialModel step6bSpecialModel = getNextModel();
		if (step6bSpecialModel != null)
			return new Step6bSpecialController(step6bSpecialModel,firstLineWithData);
		
		return null;
	}

	@Override
	public StepModel getModel() {
		return step6bSpecialModel;
	}

}
