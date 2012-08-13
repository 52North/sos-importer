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
package org.n52.sos.importer.view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * shows progress while assembling data, registering sensors
 * and inserting observations, provides a link to the log file
 * @author Raimund
 *
 */
public class Step8Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(Step8Panel.class);

	private JButton logFileButton;

	private JButton configFileButton;
	private JTextArea configurationFileInstructions;
	private JTextArea directImportOutput;

	private final Step7Model s7M;

	public Step8Panel(final Step7Model s7M) {
		this.s7M = s7M;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{50, 50, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JPanel logfilePanel = initLogFilePanel();

		GridBagConstraints gbc_logfilePanel = new GridBagConstraints();
		gbc_logfilePanel.fill = GridBagConstraints.BOTH;
		gbc_logfilePanel.insets = new Insets(0, 0, 5, 0);
		gbc_logfilePanel.gridx = 0;
		gbc_logfilePanel.gridy = 0;
		add(logfilePanel, gbc_logfilePanel);

		JPanel configurationFilePanel = initConfigurationFilePanel(s7M);

		GridBagConstraints gbc_configurationFilePanel = new GridBagConstraints();
		gbc_configurationFilePanel.insets = new Insets(0, 0, 5, 0);
		gbc_configurationFilePanel.fill = GridBagConstraints.BOTH;
		gbc_configurationFilePanel.gridx = 0;
		gbc_configurationFilePanel.gridy = 1;
		add(configurationFilePanel, gbc_configurationFilePanel);

		JPanel directImportPanel = initDirectImportPanel();

		GridBagConstraints gbc_directImportPanel = new GridBagConstraints();
		gbc_directImportPanel.fill = GridBagConstraints.BOTH;
		gbc_directImportPanel.gridx = 0;
		gbc_directImportPanel.gridy = 2;
		add(directImportPanel, gbc_directImportPanel);

	}

	private JPanel initDirectImportPanel() {
		JPanel directImportPanel = new JPanel();
		directImportPanel.setBorder(new TitledBorder(null, Lang.l().step8DirectImportStartButton(), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagLayout gbl_directImportPanel = new GridBagLayout();
		gbl_directImportPanel.columnWidths = new int[] {700, 0, 0};
		gbl_directImportPanel.rowHeights = new int[]{23, 0, 0, 0};
		gbl_directImportPanel.columnWeights = new double[]{1.0, 0.0};
		gbl_directImportPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		directImportPanel.setLayout(gbl_directImportPanel);

		JTextArea directImportInstructions = new JTextArea(Lang.l().step8DirectImportInstructions());
		directImportInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		directImportInstructions.setFocusable(false);
		directImportInstructions.setEditable(false);
		directImportInstructions.setWrapStyleWord(true);
		directImportInstructions.setLineWrap(true);
		directImportInstructions.setFont(Constants.DEFAULT_LABEL_FONT);

		GridBagConstraints gbc_directImportInstructions = new GridBagConstraints();
		gbc_directImportInstructions.fill = GridBagConstraints.HORIZONTAL;
		gbc_directImportInstructions.anchor = GridBagConstraints.NORTH;
		gbc_directImportInstructions.insets = new Insets(0, 0, 5, 5);
		gbc_directImportInstructions.gridx = 0;
		gbc_directImportInstructions.gridy = 0;
		directImportPanel.add(directImportInstructions, gbc_directImportInstructions);

		final JButton directImportExecuteButton = new JButton(Lang.l().step8StartImportButton());
		directImportExecuteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder pathToJavaExecutable = new StringBuilder(System.getProperty("java.home"));
				pathToJavaExecutable.append(File.separator);
				pathToJavaExecutable.append("bin");
				pathToJavaExecutable.append(File.separator);
				pathToJavaExecutable.append("java");
				File jvm = new File(pathToJavaExecutable.toString());
				if (! jvm.exists() && System.getProperty("os.name").indexOf("Windows") != -1) {
					pathToJavaExecutable.append(".exe");
				}
				StringBuilder pathToFeederJar = new StringBuilder(System.getProperty("user.dir"));
				pathToFeederJar.append(File.separator);
				pathToFeederJar.append(Constants.DEFAULT_FEEDER_JAR_NAME);
				File feederJar = new File(pathToFeederJar.toString());
				if (!feederJar.exists()) {
					JOptionPane.showMessageDialog(Step8Panel.this,
							Lang.l().step8FeederJarNotFound(feederJar.getAbsolutePath()),
							Lang.l().errorDialogTitle(),
							JOptionPane.ERROR_MESSAGE);
				} else {
					directImportExecuteButton.setEnabled(false);

					ProcessBuilder builder = new ProcessBuilder(pathToJavaExecutable.toString(),
							"-jar",
							pathToFeederJar.toString(),
							"-c",
							s7M.getConfigFile().getAbsolutePath());
					builder.redirectErrorStream(true);
					DirectImportWorker directImporter = new DirectImportWorker(directImportOutput,builder);
					directImporter.execute();
				}
			}
		});
		GridBagConstraints gbc_directImportExecuteButton = new GridBagConstraints();
		gbc_directImportExecuteButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_directImportExecuteButton.insets = new Insets(0, 0, 5, 5);
		gbc_directImportExecuteButton.gridx = 0;
		gbc_directImportExecuteButton.gridy = 1;
		directImportPanel.add(directImportExecuteButton, gbc_directImportExecuteButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		directImportPanel.add(scrollPane, gbc_scrollPane);

		directImportOutput = new JTextArea();
		directImportOutput.setEditable(false);
		directImportOutput.setFont(Constants.DEFAULT_LABEL_FONT);
		scrollPane.setViewportView(directImportOutput);

		return directImportPanel;
	}

	private JPanel initConfigurationFilePanel(final Step7Model s7M) {
		JPanel configurationFilePanel = new JPanel();
		configurationFilePanel.setBorder(new TitledBorder(null, Lang.l().step7ConfigurationFile(), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagLayout gbl_configurationFilePanel = new GridBagLayout();
		gbl_configurationFilePanel.columnWidths = new int[]{450, 0, 0};
		gbl_configurationFilePanel.rowHeights = new int[]{0, 23, 0};
		gbl_configurationFilePanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_configurationFilePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		configurationFilePanel.setLayout(gbl_configurationFilePanel);

		configurationFileInstructions = new JTextArea(Lang.l().step8ConfigurationFileInstructions());
		configurationFileInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		configurationFileInstructions.setFocusable(false);
		configurationFileInstructions.setEditable(false);
		configurationFileInstructions.setWrapStyleWord(true);
		configurationFileInstructions.setLineWrap(true);
		configurationFileInstructions.setFont(Constants.DEFAULT_LABEL_FONT);

		GridBagConstraints gbc_configurationFileInstructions = new GridBagConstraints();
		gbc_configurationFileInstructions.insets = new Insets(0, 0, 5, 5);
		gbc_configurationFileInstructions.fill = GridBagConstraints.BOTH;
		gbc_configurationFileInstructions.gridx = 0;
		gbc_configurationFileInstructions.gridy = 0;
		configurationFilePanel.add(configurationFileInstructions, gbc_configurationFileInstructions);
		
		configFileButton = new JButton();
		configFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		configFileButton.setText(Lang.l().step8ConfigFileButton());
		configFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(s7M.getConfigFile().toURI());
				} catch (IOException ioe) {
					logger.error("Unable to open log file: " + ioe.getMessage());
				}
			}
		});
		configFileButton.setEnabled(true);
		configFileButton.setVisible(true);
		
		GridBagConstraints gbc_configFileButton = new GridBagConstraints();
		gbc_configFileButton.anchor = GridBagConstraints.WEST;
		gbc_configFileButton.insets = new Insets(0, 0, 0, 5);
		gbc_configFileButton.gridx = 0;
		gbc_configFileButton.gridy = 1;
		configurationFilePanel.add(configFileButton, gbc_configFileButton);

		return configurationFilePanel;
	}

	private JPanel initLogFilePanel() {
		JPanel logfilePanel = new JPanel();
		logfilePanel.setBorder(new TitledBorder(null, Lang.l().step8LogFile(), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagLayout gbl_logfilePanel = new GridBagLayout();
		gbl_logfilePanel.columnWidths = new int[]{177, 95, 0};
		gbl_logfilePanel.rowHeights = new int[] {25, 25, 0};
		gbl_logfilePanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_logfilePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		logfilePanel.setLayout(gbl_logfilePanel);

		JTextArea logfileInstructionsJT = new JTextArea(Lang.l().step8LogFileInstructions());
		logfileInstructionsJT.setFocusable(false);
		logfileInstructionsJT.setLineWrap(true);
		logfileInstructionsJT.setWrapStyleWord(true);
		logfileInstructionsJT.setEditable(false);
		logfileInstructionsJT.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		logfileInstructionsJT.setFont(Constants.DEFAULT_LABEL_FONT);

		GridBagConstraints gbc_logfileInstructionsJT = new GridBagConstraints();
		gbc_logfileInstructionsJT.fill = GridBagConstraints.BOTH;
		gbc_logfileInstructionsJT.insets = new Insets(0, 0, 5, 5);
		gbc_logfileInstructionsJT.gridx = 0;
		gbc_logfileInstructionsJT.gridy = 0;
		logfilePanel.add(logfileInstructionsJT, gbc_logfileInstructionsJT);

		GridBagConstraints gbc_logFileButton = new GridBagConstraints();
		gbc_logFileButton.insets = new Insets(0, 0, 0, 5);
		gbc_logFileButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_logFileButton.gridx = 0;
		gbc_logFileButton.gridy = 1;

		logFileButton = new JButton();
		logFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		logFileButton.setText(Lang.l().step8LogFileButton());

		logfilePanel.add(logFileButton, gbc_logFileButton);

		return logfilePanel;
	}

	public void setLogFileURI(final URI uri) {
		if (logger.isTraceEnabled()) {
			logger.trace("setLogFileURI(" + uri + ")");
		}
		logFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(uri);
				} catch (IOException ioe) {
					logger.error("Unable to open log file: " + ioe.getMessage());
				}
			}
		});
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
