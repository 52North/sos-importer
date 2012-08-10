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
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step8Panel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * Assembles all information from previous steps, 
 * fills XML template files with it and uploads
 * them to the Sensor Observation Service,
 * displays the configuration,
 * and the log file 
 * depending on the options from step 7.
 * @author Raimund
 * @author e.h.juerrens@52north.org
 *
 */
public class Step8Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step8Controller.class);

	private Step8Panel step8Panel;
	
	private Step7Model step7Model;
	
	public Step8Controller(Step7Model step7Model) {
		this.step7Model = step7Model;
	}
	
	@Override
	public void loadSettings() {		
		step8Panel = new Step8Panel(step7Model);
		BackNextController.getInstance().changeNextToFinish();
		File logFile = null;
		
		Logger rL = Logger.getRootLogger();
		@SuppressWarnings("rawtypes")
		Enumeration appender = rL.getAllAppenders();
		while(appender.hasMoreElements()) {
			Object o = appender.nextElement();
			if(o instanceof FileAppender) {
				FileAppender fA = (FileAppender) o;
				logFile = new File(fA.getFile());
				step8Panel.setLogFileURI(logFile.toURI());
				logger.info("Log saved to file: " + logFile.getAbsolutePath());
			}
		}
		
		// save model always
		try {
			if (MainController.getInstance().saveModel(step7Model.getConfigFile())) {
				if (logger.isInfoEnabled()) {
					logger.info("Configuration saved to file: " + step7Model.getConfigFile().getAbsolutePath());
				}
			}
		} catch (IOException e) {
			logger.error("Exception thrown: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(step8Panel, 
					Lang.l().step8SaveModelFailed(logFile,e.getLocalizedMessage()), 
					Lang.l().errorDialogTitle(), 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void saveSettings() {}

	@Override
	public String getDescription() {
		return Lang.l().step8Description();
	}

	@Override
	public JPanel getStepPanel() {
		return step8Panel;
	}

	@Override
	public StepController getNextStepController() {
		return null;
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public StepController getNext() {
		return null;
	}
 
	@Override
	public void back() {
		BackNextController.getInstance().changeFinishToNext();
	}

	@Override
	public StepModel getModel() {
		return this.step7Model;
	}
}
