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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
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
		step8Panel = new Step8Panel(step7Model,this);
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
	
	public void directImport() {
		StringBuilder pathToJavaExecutable = new StringBuilder(System.getProperty("java.home"));
		pathToJavaExecutable.append(File.separator);
		pathToJavaExecutable.append("bin");
		pathToJavaExecutable.append(File.separator);
		pathToJavaExecutable.append("java");
		File jvm = new File(pathToJavaExecutable.toString());
		if (! jvm.exists() && System.getProperty("os.name").indexOf("Windows") != -1) {
			pathToJavaExecutable.append(".exe");
		}
		
		String pathToFeederJar = System.getProperty("user.dir") + File.separator;
		pathToFeederJar = searchForFeederJarWithDefaultFileNameStart(pathToFeederJar);
		File feederJar = new File(pathToFeederJar.toString());
		
		if (!feederJar.exists()) {
			JOptionPane.showMessageDialog(step8Panel,
					Lang.l().step8FeederJarNotFound(feederJar.getAbsolutePath()),
					Lang.l().errorDialogTitle(),
					JOptionPane.ERROR_MESSAGE);
		} else {
			step8Panel.setDirectImportExecuteButtonEnabled(false);

			ProcessBuilder builder = new ProcessBuilder(pathToJavaExecutable.toString(),
					"-jar",
					pathToFeederJar.toString(),
					"-c",
					step7Model.getConfigFile().getAbsolutePath());
			builder.redirectErrorStream(true);
			DirectImportWorker directImporter = new DirectImportWorker(step8Panel.getDirectImportOutputTextArea(),builder);
			directImporter.execute();
		}
	}
	
	private String searchForFeederJarWithDefaultFileNameStart(
			String pathToDirectoryWithFeederJar) {
		if (logger.isTraceEnabled()) {
			logger.trace("searchForFeederJarWithDefaultFileNameStart()");
		}
		File directoryWithFeederJar = new File(pathToDirectoryWithFeederJar);
		if (directoryWithFeederJar != null &&
				directoryWithFeederJar.exists() &&
				directoryWithFeederJar.isDirectory()) {
			String[] files = directoryWithFeederJar.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.indexOf(Constants.DEFAULT_FEEDER_JAR_NAME_START) != -1 && name.endsWith(".jar"));
				}
			});
			if (files != null && files.length > 0) {
				return files[0]; // returns the first matching feeder.jar
			}
		}
		return pathToDirectoryWithFeederJar;
	}

	private class DirectImportWorker extends SwingWorker<String, String>{

		private JTextArea processOutPut;
		private ProcessBuilder procBuilder;

		public DirectImportWorker(JTextArea processOutPut,
				ProcessBuilder procBuilder) {
			this.processOutPut = processOutPut;
			this.procBuilder = procBuilder;
		}

		protected void process(List<String> chunks) {
			Iterator<String> it = chunks.iterator();
			while (it.hasNext()) {
				processOutPut.append(it.next());
			}
		}

		protected String doInBackground() throws Exception {
			Process importProcess;
			try {
				importProcess = procBuilder.start();
				InputStream res = importProcess.getInputStream();
				byte[] buffer = new byte[128];
				int len;
				while ( (len=res.read(buffer,0,buffer.length))!=-1) {
					publish(new String(buffer,0,len));
					if (isCancelled()) {
						importProcess.destroy();
						return "";
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		protected void done() {
			if (logger.isDebugEnabled()) {
				logger.debug("Import Task finished");
			}
		}

	}
}
