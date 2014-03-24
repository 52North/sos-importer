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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step1Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * chooses a CSV file
 * @author Raimund
 *
 */
public class Step1Controller extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step1Controller.class);

	private Step1Panel step1Panel;

	private final Step1Model step1Model;

	private String tmpCSVFileContent;

	private int csvFileRowCount = -1;

	public Step1Controller() {
		step1Model = new Step1Model();
	}

	@Override
	public String getDescription() {
		return Lang.l().step1Description();
	}

	@Override
	public void loadSettings() {
		//if (step1Panel == null) {
			step1Panel = new Step1Panel(this);

			//disable "back" button
			BackNextController.getInstance().setBackButtonVisible(false);

			final String csvFilePath = step1Model.getCSVFilePath();
			step1Panel.setCSVFilePath(csvFilePath);

			if(step1Panel.getCSVFilePath() == null ||
					step1Panel.getCSVFilePath().equals("")) {
				BackNextController.getInstance().setNextButtonEnabled(false);
			} else {
				BackNextController.getInstance().setNextButtonEnabled(true);
			}
		//}
		step1Panel.setFeedingType(step1Model.getFeedingType());
		step1Panel.setCSVFilePath(step1Model.getCSVFilePath());
		step1Panel.setUrl(step1Model.getUrl());
		step1Panel.setUser(step1Model.getUser());
		step1Panel.setPassword(step1Model.getPassword());
		step1Panel.setDirectory(step1Model.getDirectory());
		step1Panel.setFilenameSchema(step1Model.getFilenameSchema());
		step1Panel.setRegexStatus(step1Model.isRegex());
	}

	@Override
	public void saveSettings() {
		if (step1Panel != null) {
			step1Model.setFeedingType(step1Panel.getFeedingType());
			step1Model.setCSVFilePath(step1Panel.getCSVFilePath());
			step1Model.setUrl(step1Panel.getUrl());
			step1Model.setUser(step1Panel.getUser());
			step1Model.setPassword(step1Panel.getPassword());
			step1Model.setDirectory(step1Panel.getDirectory());
			step1Model.setFilenameSchema(step1Panel.getFilenameSchema());
			step1Model.setRegex(step1Panel.getRegexStatus());
		}
		// why shall always the gui be recreated and repainted? - too expensive
		// and complicates some method calls
		step1Panel = null;
	}

	public void browseButtonClicked() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new CSVFileFilter());
		if (fc.showOpenDialog(getStepPanel()) == JFileChooser.APPROVE_OPTION) {
			step1Panel.setCSVFilePath(fc.getSelectedFile().getAbsolutePath());
			checkInputFileValue();
		}
	}

	/*
	 * Checks the validity of the next button enablement after switching the
	 * feeding type cards.
	 */
	public void checkInputFileValue() {
		if (step1Panel.getCSVFilePath() != null &&
				!step1Panel.getCSVFilePath().trim().equals("")) {
			BackNextController.getInstance().setNextButtonEnabled(true);
			MainController.getInstance().updateTitle(step1Panel.getCSVFilePath());
		} else {
			BackNextController.getInstance().setNextButtonEnabled(false);
		}
	}

	private class CSVFileFilter extends FileFilter {
		@Override
	    public boolean accept(final File file) {
	        return file.isDirectory() ||
	        	   file.getName().toLowerCase().endsWith(".csv");
	    }
		@Override
	    public String getDescription() {
	        return "CSV files";
	    }
	}

	@Override
	public JPanel getStepPanel() {
		return step1Panel;
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean isFinished() {

		if (step1Panel != null &&  step1Panel.getFeedingType() == Step1Panel.CSV_FILE) {
			final String filePath = step1Panel.getCSVFilePath();
			if (filePath == null) {
				JOptionPane.showMessageDialog(null,
					    "Please choose a CSV file.",
					    "File missing",
					    JOptionPane.WARNING_MESSAGE);
				return false;
			}
			// checks one-time feed input data for validity
			if (filePath.equals("")) {
				JOptionPane.showMessageDialog(null,
					    "Please choose a CSV file.",
					    "File missing",
					    JOptionPane.WARNING_MESSAGE);
				return false;
			}

			final File f = new File(filePath);

			if (!f.exists()) {
				JOptionPane.showMessageDialog(null,
					    "The specified file does not exist.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (!f.isFile()) {
				JOptionPane.showMessageDialog(null,
					    "Please specify a file, not a directory.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (!f.canRead()) {
				JOptionPane.showMessageDialog(null,
					    "No reading access on the specified file.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}
			readFile(f);
		} else if (step1Panel != null && (step1Panel.getFeedingType() == Step1Panel.FTP_FILE)) {
			// checks repetitive feed input data for validity
			if (step1Panel.getUrl() == null || step1Panel.getUrl().equals("")) {
				JOptionPane.showMessageDialog(null,
					    "No ftp server was specified.",
					    "Server missing",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (step1Panel.getFilenameSchema() == null || step1Panel.getFilenameSchema().equals("")) {
				JOptionPane.showMessageDialog(null,
					    "No file/file schema was specified.",
					    "File/file schema missing",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// ftp client
			FTPClient client;

			// proxy
			final String pHost = System.getProperty("proxyHost","proxy");
			int pPort = -1;
			if (System.getProperty("proxyPort") != null) {
				pPort = Integer.parseInt(System.getProperty("proxyPort"));
			}
			final String pUser = System.getProperty( "http.proxyUser");
			final String pPassword = System.getProperty( "http.proxyPassword");
			if (pHost != null && pPort != -1) {
				if (pUser != null && pPassword != null) {
					client = new FTPHTTPClient(pHost, pPort, pUser, pPassword);
				}
				client = new FTPHTTPClient(pHost, pPort);
			} else {
				client = new FTPClient();
			}

			// get first file
			if(step1Panel.getFeedingType() == Step1Panel.FTP_FILE) {
				final String csvFilePath = System.getProperty("user.home")
						+ File.separator + ".SOSImporter" + File.separator + "tmp_"
						+ step1Panel.getFilenameSchema();

				// if back button was used: delete old file
				if (new File(csvFilePath).exists()) {
					new File(csvFilePath).delete();
				}

				try {
					client.connect(step1Panel.getUrl());
					final boolean login = client.login(step1Panel.getUser(), step1Panel.getPassword());
					if (login) {
						// download file
						final int result = client.cwd(step1Panel.getDirectory());
						if (result == 250) { // successfully connected
							final File outputFile = new File(csvFilePath);
							final FileOutputStream fos = new FileOutputStream(outputFile);
							client.retrieveFile(step1Panel.getFilenameSchema(), fos);
							fos.flush();
							fos.close();
						}
		                final boolean logout = client.logout();
		                if (logout) {
		                	logger.info("Step1Controller: cannot logout!");
		                }
		            } else {
		            	logger.info("Step1Controller: cannot login!");
		            }

					final File csv = new File(csvFilePath);
					if (csv.length() != 0) {
						step1Panel.setCSVFilePath(csvFilePath);
						readFile(new File(csvFilePath));
					} else {
						csv.delete();
						throw new IOException();
					}


				} catch (final SocketException e) {
					System.err.println(e);
					JOptionPane.showMessageDialog(null,
						    "The file you specified cannot be obtained.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return false;
				} catch (final IOException e) {
					System.err.println(e);
					JOptionPane.showMessageDialog(null,
						    "The file you specified cannot be obtained.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Reads the given file line by line. Returns the content as
	 * <code>{@link java.lang.String}</code> and sets the
	 * <code>csvFileRowCount</code> variable of this class.
	 * @param f the <code>{@link java.io.File}</code> to read
	 * @return a <code>{@link java.lang.String}</code> containing the content
	 * 				of the given file
	 */
	private String readFile(final File f) {
		logger.info("Read CSV file " + f.getAbsolutePath());
		final StringBuilder sb = new StringBuilder();
		try {
			final FileReader fr = new FileReader(f);
			final BufferedReader br = new BufferedReader(fr);
			String line;
			csvFileRowCount = 0;
			//
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				csvFileRowCount++;
			}
			br.close();
		} catch (final IOException ioe) {
			logger.error(
					new StringBuffer("Problem while reading CSV file \"")
					.append(f.getAbsolutePath())
					.append("\"").toString(),
					ioe);
		}
		return tmpCSVFileContent = sb.toString();
	}

	@Override
	public StepController getNext() {
		return null;
	}

	@Override
	public StepController getNextStepController() {
		final Step2Model s2m = new Step2Model(tmpCSVFileContent,csvFileRowCount);
		tmpCSVFileContent = null;

		return new Step2Controller(s2m);
	}

	@Override
	public StepModel getModel() {
		return step1Model;
	}
}
