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
package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
 *
 * @author Raimund Schnürer
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @author Eric Fiedler
 * @version $Id: $Id
 */
public class Step1Controller extends StepController {

    private static final String ERROR = "Error";

    private static final Logger LOG = LoggerFactory.getLogger(Step1Controller.class);

    private Step1Panel step1Panel;

    private final Step1Model step1Model;

    private String tmpCSVFileContent;

    private int csvFileRowCount = -1;

    /**
     * <p>Constructor for Step1Controller.</p>
     */
    public Step1Controller() {
        step1Model = new Step1Model();
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return Lang.l().step1Description();
    }

    /** {@inheritDoc} */
    @Override
    public void loadSettings() {
        step1Panel = new Step1Panel(this);

        //disable "back" button
        BackNextController.getInstance().setBackButtonVisible(false);

        final String csvFilePath = step1Model.getCSVFilePath();
        step1Panel.setCSVFilePath(csvFilePath);

        if (step1Panel.getCSVFilePath() == null ||
                step1Panel.getCSVFilePath().equals("")) {
            BackNextController.getInstance().setNextButtonEnabled(false);
        } else {
            BackNextController.getInstance().setNextButtonEnabled(true);
        }
        step1Panel.setFeedingType(step1Model.getFeedingType());
        step1Panel.setCSVFilePath(step1Model.getCSVFilePath());
        step1Panel.setUrl(step1Model.getUrl());
        step1Panel.setUser(step1Model.getUser());
        step1Panel.setPassword(step1Model.getPassword());
        step1Panel.setDirectory(step1Model.getDirectory());
        step1Panel.setFilenameSchema(step1Model.getFilenameSchema());
        step1Panel.setRegexStatus(step1Model.isRegex());
        step1Panel.setFileEncoding(step1Model.getFileEncoding());
    }

    /** {@inheritDoc} */
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
            step1Model.setFileEncoding(step1Panel.getFileEncoding());
        }
        // why shall always the gui be recreated and repainted? - too expensive
        // and complicates some method calls
        step1Panel = null;
    }

    /**
     * <p>browseButtonClicked.</p>
     */
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
    /**
     * <p>checkInputFileValue.</p>
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

    /** {@inheritDoc} */
    @Override
    public JPanel getStepPanel() {
        return step1Panel;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNecessary() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinished() {

        if (step1Panel != null &&  step1Panel.getFeedingType() == Step1Panel.CSV_FILE) {
            final String filePath = step1Panel.getCSVFilePath();
            final String instruction = "Please choose a CSV file.";
            final String title = "File missing";
            // checks one-time feed input data for validity
            if (filePath == null || filePath.equals("")) {
                JOptionPane.showMessageDialog(null,
                        instruction,
                        title,
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            final File dataFile = new File(filePath);
            if (!dataFile.exists()) {
                JOptionPane.showMessageDialog(null,
                        "The specified file does not exist.",
                        ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!dataFile.isFile()) {
                JOptionPane.showMessageDialog(null,
                        "Please specify a file, not a directory.",
                        ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!dataFile.canRead()) {
                JOptionPane.showMessageDialog(null,
                        "No reading access on the specified file.",
                        ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            readFile(dataFile, step1Panel.getFileEncoding());
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
            final String pHost = System.getProperty("proxyHost", "proxy");
            int pPort = -1;
            final String proxyPort = "proxyPort";
            if (System.getProperty(proxyPort) != null) {
                pPort = Integer.parseInt(System.getProperty(proxyPort));
            }
            final String pUser = System.getProperty("http.proxyUser");
            final String pPassword = System.getProperty("http.proxyPassword");
            if (pHost != null && pPort != -1) {
                if (pUser != null && pPassword != null) {
                    client = new FTPHTTPClient(pHost, pPort, pUser, pPassword);
                } else {
                    client = new FTPHTTPClient(pHost, pPort);
                }
            } else {
                client = new FTPClient();
            }

            // get first file
            if (step1Panel.getFeedingType() == Step1Panel.FTP_FILE) {
                final String csvFilePath = System.getProperty("user.home")
                        + File.separator + ".SOSImporter" + File.separator + "tmp_"
                        + step1Panel.getFilenameSchema();

                // if back button was used: delete old file
                if (new File(csvFilePath).exists()) {
                    if (!new File(csvFilePath).delete()) {
                        LOG.error("Could not delete file '{}'. Check permissions", csvFilePath);
                    }
                }

                FileOutputStream fos = null;
                try {
                    client.connect(step1Panel.getUrl());
                    final boolean login = client.login(step1Panel.getUser(), step1Panel.getPassword());
                    if (login) {
                        // download file
                        final int result = client.cwd(step1Panel.getDirectory());
                        // successfully connected
                        if (result == 250) {
                            final File outputFile = new File(csvFilePath);
                            fos = new FileOutputStream(outputFile);
                            client.retrieveFile(step1Panel.getFilenameSchema(), fos);
                            fos.flush();
                            fos.close();
                        }
                        final boolean logout = client.logout();
                        if (logout) {
                            LOG.info("Step1Controller: cannot logout!");
                        }
                    } else {
                        LOG.info("Step1Controller: cannot login!");
                    }

                    final File csv = new File(csvFilePath);
                    if (csv.exists() && csv.isFile()) {
                        step1Panel.setCSVFilePath(csvFilePath);
                        readFile(new File(csvFilePath), step1Panel.getFileEncoding());
                    } else {
                        if (!csv.delete()) {
                            LOG.error("Could not delete CSV file '{}'", csvFilePath);
                        }
                        throw new IOException();
                    }
                } catch (final IOException e) {
                    System.err.println(e);
                    JOptionPane.showMessageDialog(null,
                            "The file you specified cannot be obtained.",
                            ERROR,
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            LOG.error("Exception thrown", e.getMessage());
                            LOG.debug("Stackstrace", e);
                        }
                    }
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
     * @param encoding the name of charset encoding of the {@link File} f
     * @return a <code>{@link java.lang.String}</code> containing the content
     *              of the given file
     */
    private String readFile(final File f, final String encoding) {
        LOG.info("Read CSV file " + f.getAbsolutePath());
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding))) {

            String line;
            csvFileRowCount = 0;
            //
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                csvFileRowCount++;
            }
            br.close();
        } catch (final IOException ioe) {
            LOG.error(
                    new StringBuffer("Problem while reading CSV file \"")
                    .append(f.getAbsolutePath())
                    .append("\"").toString(),
                    ioe);
        }
        return tmpCSVFileContent = sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNext() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNextStepController() {
        final Step2Model s2m = new Step2Model(tmpCSVFileContent, csvFileRowCount);
        tmpCSVFileContent = null;

        return new Step2Controller(s2m);
    }

    /** {@inheritDoc} */
    @Override
    public StepModel getModel() {
        return step1Model;
    }


    private static class CSVFileFilter extends FileFilter {
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

}
