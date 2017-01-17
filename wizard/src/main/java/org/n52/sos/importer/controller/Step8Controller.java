/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
/**
h * Copyright (C) 2012
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
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step8Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assembles all information from previous steps,
 * fills XML template files with it and uploads
 * them to the Sensor Observation Service,
 * displays the configuration,
 * and the log file
 * depending on the options from step 7.
 *
 * @author Raimund
 * @author e.h.juerrens@52north.org
 * @version $Id: $Id
 */
public class Step8Controller extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step8Controller.class);

	private Step8Panel step8Panel;

	private final Step7Model step7Model;

	/**
	 * <p>Constructor for Step8Controller.</p>
	 *
	 * @param step7Model a {@link org.n52.sos.importer.model.Step7Model} object.
	 */
	public Step8Controller(final Step7Model step7Model) {
		this.step7Model = step7Model;
	}

	/** {@inheritDoc} */
	@Override
	public void loadSettings() {
		step8Panel = new Step8Panel(step7Model,this);
		BackNextController.getInstance().changeNextToFinish();
		File logFile = null;

		// FIXME get path to log file
		/*
		final Logger rL = Logger.getRootLogger();
		final Enumeration<?> appender = rL.getAllAppenders();
		while(appender.hasMoreElements()) {
			final Object o = appender.nextElement();
			if(o instanceof FileAppender) {
				final FileAppender fA = (FileAppender) o;
				logFile = new File(fA.getFile());
				step8Panel.setLogFileURI(logFile.toURI());
				logger.info("Log saved to file: " + logFile.getAbsolutePath());
			}
		}
		*/
		logFile = new File("logs/sos-importer-core.log");
		step8Panel.setLogFileURI(logFile.toURI());
		logger.info("Log saved to file: " + logFile.getAbsolutePath());

		// save model always
		try {
			if (MainController.getInstance().saveModel(step7Model.getConfigFile())) {
				logger.info("Configuration saved to file: '{}'", step7Model.getConfigFile().getAbsolutePath());
			} else {
				logger.error("File could not be saved. See log file!");
				JOptionPane.showMessageDialog(step8Panel,
						Lang.l().step8SaveModelFailed(step7Model.getConfigFile()),
						Lang.l().errorDialogTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (final IOException e) {
			logger.error(new StringBuffer("Exception thrown: ").append(e.getMessage()).toString(), e);
			JOptionPane.showMessageDialog(step8Panel,
					Lang.l().step8SaveModelFailed(logFile,e.getLocalizedMessage()),
					Lang.l().errorDialogTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void saveSettings() {}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return Lang.l().step8Description();
	}

	/** {@inheritDoc} */
	@Override
	public JPanel getStepPanel() {
		return step8Panel;
	}

	/** {@inheritDoc} */
	@Override
	public StepController getNextStepController() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNecessary() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isFinished() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public StepController getNext() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void back() {
		BackNextController.getInstance().changeFinishToNext();
	}

	/** {@inheritDoc} */
	@Override
	public StepModel getModel() {
		return step7Model;
	}

	/**
	 * <p>directImport.</p>
	 */
	public void directImport() {
		final StringBuilder pathToJavaExecutable = new StringBuilder(System.getProperty("java.home"));
		pathToJavaExecutable.append(File.separator);
		pathToJavaExecutable.append("bin");
		pathToJavaExecutable.append(File.separator);
		pathToJavaExecutable.append("java");
		final File jvm = new File(pathToJavaExecutable.toString());
		if (! jvm.exists() && System.getProperty("os.name").indexOf("Windows") != -1) {
			pathToJavaExecutable.append(".exe");
		}

		String pathToFeederJar = System.getProperty("user.dir") + File.separator;
		pathToFeederJar = searchForFeederJarWithDefaultFileNameStart(pathToFeederJar);
		final File feederJar = new File(pathToFeederJar.toString());

		if (!feederJar.exists()) {
			JOptionPane.showMessageDialog(step8Panel,
					Lang.l().step8FeederJarNotFound(feederJar.getAbsolutePath()),
					Lang.l().errorDialogTitle(),
					JOptionPane.ERROR_MESSAGE);
		} else {
			step8Panel.setDirectImportExecuteButtonEnabled(false);

			final ProcessBuilder builder = new ProcessBuilder(pathToJavaExecutable.toString(),
					"-jar",
					pathToFeederJar.toString(),
					"-c",
					step7Model.getConfigFile().getAbsolutePath());
			builder.redirectErrorStream(true);
			final DirectImportWorker directImporter = new DirectImportWorker(step8Panel.getDirectImportOutputTextArea(),builder);
			directImporter.execute();
		}
	}

	private String searchForFeederJarWithDefaultFileNameStart(
			final String pathToDirectoryWithFeederJar) {
		if (logger.isTraceEnabled()) {
			logger.trace("searchForFeederJarWithDefaultFileNameStart()");
		}
		final File directoryWithFeederJar = new File(pathToDirectoryWithFeederJar);
		if (directoryWithFeederJar != null &&
				directoryWithFeederJar.exists() &&
				directoryWithFeederJar.isDirectory()) {
			final String[] files = directoryWithFeederJar.list(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return (name.indexOf(Constants.DEFAULT_FEEDER_JAR_NAME_START) != -1 && name.endsWith(".jar"));
				}
			});
			if (files != null && files.length > 0)
			{
				return files[0]; // returns the first matching feeder.jar
			}
			else {
				final int userChoice = JOptionPane.showConfirmDialog(step8Panel,
						Lang.l().step8FeederJarNotFoundSelectByUser(pathToDirectoryWithFeederJar),
						Lang.l().errorDialogTitle(), JOptionPane.YES_NO_OPTION);
				if (userChoice == JOptionPane.YES_OPTION)
				{
					final JFileChooser chooser = new JFileChooser(pathToDirectoryWithFeederJar);
				    final FileNameExtensionFilter filter = new FileNameExtensionFilter("Java ARchives - *.jar","jar");
				    chooser.setFileFilter(filter);
				    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				    chooser.setMultiSelectionEnabled(false);
				    final int returnVal = chooser.showOpenDialog(step8Panel);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				       logger.debug(String.format("Choosen file: %s",chooser.getSelectedFile().getAbsolutePath()));
				       return chooser.getSelectedFile().getAbsolutePath();
				    }
				}
			}
		}
		return pathToDirectoryWithFeederJar;
	}

	private class DirectImportWorker extends SwingWorker<String, String>{

		private final JTextArea processOutPut;
		private final ProcessBuilder procBuilder;

		public DirectImportWorker(final JTextArea processOutPut,
				final ProcessBuilder procBuilder) {
			this.processOutPut = processOutPut;
			this.procBuilder = procBuilder;
		}

		@Override
		protected void process(final List<String> chunks) {
			final Iterator<String> it = chunks.iterator();
			while (it.hasNext()) {
				processOutPut.append(it.next());
			}
		}

		@Override
		protected String doInBackground() throws Exception {
			Process importProcess;
			try {
				importProcess = procBuilder.start();
				final InputStream res = importProcess.getInputStream();
				final byte[] buffer = new byte[128];
				int len;
				while ( (len=res.read(buffer,0,buffer.length))!=-1) {
					publish(new String(buffer,0,len));
					if (isCancelled()) {
						importProcess.destroy();
						return "";
					}
				}
			}
			catch (final Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void done() {
			if (logger.isDebugEnabled()) {
				logger.debug("Import Task finished");
			}
		}

	}
}
