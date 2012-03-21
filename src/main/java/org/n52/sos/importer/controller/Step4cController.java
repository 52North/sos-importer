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

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;

/**
 * solves ambiguities in case there is more than one position column
 * (needs apparently to be implemented)
 * @author Raimund
 *
 */
public class Step4cController extends StepController {

	private static final Logger logger = Logger.getLogger(Step4cController.class);
	
	@Override
	public void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getStepPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StepController getNextStepController() {
		return new Step5aController();
	}

	@Override
	public boolean isNecessary() {
		int positions = ModelStore.getInstance().getPositions().size();
		
		if (positions == 0) {
			logger.info("Skip Step 4c since there are not any Positions");
			return false;
		}
		if (positions == 1) {
			Position position = ModelStore.getInstance().getPositions().get(0);
			logger.info("Skip Step 4c since there is just " + position);
			
			for (FeatureOfInterest foi: ModelStore.getInstance().getFeatureOfInterests())
				foi.setPosition(position);
			return false;
		}
		
		//TODO more than one position group
		return false;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StepController getNext() {
		// TODO Auto-generated method stub
		return null;
	}

}
