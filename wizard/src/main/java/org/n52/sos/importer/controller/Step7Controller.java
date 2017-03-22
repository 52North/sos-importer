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

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.controller.utils.XMLTools;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step7Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lets the user choose a URL of a Sensor Observation Service (and test the
 * connection), define the offering, and save the configuration.
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class Step7Controller extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step7Controller.class);

    private Step7Panel s7P;

    private Step7Model s7M;

    /**
     * <p>Constructor for Step7Controller.</p>
     */
    public Step7Controller() {
        s7M = new Step7Model();
    }

    /** {@inheritDoc} */
    @Override
    public void loadSettings() {
        s7P = new Step7Panel();
        if (s7M != null) {
            if (s7M.getSosURL() != null) {
                s7P.setSOSURL(s7M.getSosURL());
            }
            if (s7M.getConfigFile() != null) {
                s7P.setConfigFile(s7M.getConfigFile().getAbsolutePath());
            }
            if (s7M.getBinding() != null && !s7M.getBinding().isEmpty()) {
                s7P.setBinding(s7M.getBinding());
            }
            if (s7M.getVersion() != null && !s7M.getVersion().isEmpty()) {
                s7P.setSosVersion(s7M.getVersion());
            }
            s7P.setIgnoreColumnMismatch(s7M.isIgnoreColumnMismatch());
            switch (s7M.getImportStrategy()) {
                case SweArrayObservationWithSplitExtension:
                    loadImportStrategyBasedSettings();
                    break;
                default:
                    loadImportStrategyBasedSettings();
                    s7P.setImportStrategy(s7M.getImportStrategy());
                    break;
            }
        }
        BackNextController.getInstance().changeFinishToNext();
    }

    private void loadImportStrategyBasedSettings() {
        s7P.setHunkSize(s7M.getHunkSize());
        s7P.setSendBuffer(s7M.getSendBuffer());
    }

    /** {@inheritDoc} */
    @Override
    public void back() {
        final BackNextController bnc = BackNextController.getInstance();
        bnc.setNextButtonEnabled(true);
        bnc.changeFinishToNext();
    }

    /** {@inheritDoc} */
    @Override
    public void saveSettings() {
        final String sosURL = s7P.getSOSURL();
        final String offering = s7P.getOfferingName();
        final String binding = s7P.getSosBinding();
        final String version = s7P.getSosVersion();
        final boolean generateOfferingFromSensorName = s7P.isGenerateOfferingFromSensorName();
        final File configFile = new File(s7P.getConfigFile());
        final boolean ignoreColumnMismatch = s7P.isIgnoreColumnMismatch();
        if (s7M == null) {
            s7M = new Step7Model(sosURL,
                    configFile,
                    generateOfferingFromSensorName,
                    offering,
                    version,
                    binding,
                    ignoreColumnMismatch);
        } else {
            s7M.setSosURL(sosURL);
            s7M.setSosVersion(version);
            s7M.setSosBinding(binding);
            s7M.setConfigFile(configFile);
            s7M.setGenerateOfferingFromSensorName(generateOfferingFromSensorName);
            s7M.setIgnoreColumnMismatch(ignoreColumnMismatch);
            if (!generateOfferingFromSensorName) {
                s7M.setOffering(offering);
            }
        }
        switch (s7P.getImportStrategy()) {
            case SweArrayObservationWithSplitExtension:
                loadImportStrategyBasedSetting();
                break;
            default:
                loadImportStrategyBasedSetting();
                s7M.setImportStrategy(s7P.getImportStrategy());
                break;
        }
    }

    private void loadImportStrategyBasedSetting() {
        s7M.setHunkSize(s7P.getHunkSize());
        s7M.setSendBuffer(s7P.getSendBuffer());
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return Lang.l().step7Description();
    }

    /** {@inheritDoc} */
    @Override
    public JPanel getStepPanel() {
        return s7P;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNextStepController() {
        return new Step8Controller(s7M);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNecessary() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinished() {
        if (isOfferingNameNotGiven()) {
            // user decided to give input but (s)he did NOT, so tell him
            return showErrorDialogAndLogIt(Lang.l().step7OfferingNameNotGiven());
        } else if (isOfferingNameInvalid()) {
            // user gave input but it is not valid
            return showErrorDialogAndLogIt(Lang.l().step7OfferingNameNotValid(s7P.getOfferingName()));
        } else if (isSosVersionNotGiven()) {
            return showErrorDialogAndLogIt(Lang.l().step7SosVersionInstructions());
        } else if (isBindingNotGivenButRequired()) {
            return showErrorDialogAndLogIt(Lang.l().step7SosBindingInstructions());
        } else if (isConfigFileNotSet()) {
            return showErrorDialogAndLogIt(Lang.l().step7ConfigFileInstructions());
        }
        return true;
    }

    private boolean isConfigFileNotSet() {
        String configPath = s7P.getConfigFile();
        if (configPath == null || configPath.isEmpty()) {
            return true;
        }
        File configFile = new File(configPath);
        if (configFile.exists()) {
            return !configFile.canWrite();
        } else {
            File configFileFolder = new File(configPath.substring(0, configPath.lastIndexOf(File.separatorChar)));
            if (configFileFolder.exists()) {
                return !configFileFolder.canWrite();
            } else {
                return true;
            }
        }
    }

    private boolean isBindingNotGivenButRequired() {
        return s7P.getSosVersion() != null &&
                s7P.getSosVersion().equalsIgnoreCase("2.0.0") &&
                (s7P.getSosBinding() == null || s7P.getSosBinding().isEmpty());
    }

    private boolean isSosVersionNotGiven() {
        return s7P.getSosVersion() == null || s7P.getSosVersion().isEmpty();
    }

    private boolean isOfferingNameInvalid() {
        return !s7P.isGenerateOfferingFromSensorName() &&
                !XMLTools.isNCName(s7P.getOfferingName());
    }

    private boolean isOfferingNameNotGiven() {
        return !s7P.isGenerateOfferingFromSensorName() &&
                (s7P.getOfferingName() == null ||
                s7P.getOfferingName().equalsIgnoreCase(""));
    }

    private boolean showErrorDialogAndLogIt(final String msg) {
        JOptionPane.showMessageDialog(null, msg, Lang.l().errorDialogTitle(), JOptionPane.ERROR_MESSAGE);
        LOG.error(msg);
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNext() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public StepModel getModel() {
        return s7M;
    }

}
