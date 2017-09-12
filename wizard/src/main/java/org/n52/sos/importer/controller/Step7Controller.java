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
import org.n52.sos.importer.controller.utils.XMLTools;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.Step7Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lets the user choose a URL of a Sensor Observation Service (and test the
 * connection), define the offering, and save the configuration.
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk Juuml;rrens</a>
 */
public class Step7Controller extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step7Controller.class);

    private Step7Panel panel;

    private Step7Model model;

    public Step7Controller() {
        model = new Step7Model();
    }

    @Override
    public void loadSettings() {
        panel = new Step7Panel();
        if (model != null) {
            if (model.getSosURL() != null) {
                panel.setSOSURL(model.getSosURL());
            }
            if (model.getConfigFile() != null) {
                panel.setConfigFile(model.getConfigFile().getAbsolutePath());
            }
            if (model.getBinding() != null && !model.getBinding().isEmpty()) {
                panel.setBinding(model.getBinding());
            }
            if (model.getVersion() != null && !model.getVersion().isEmpty()) {
                panel.setSosVersion(model.getVersion());
            }
            panel.setIgnoreColumnMismatch(model.isIgnoreColumnMismatch());
            switch (model.getImportStrategy()) {
                case SweArrayObservationWithSplitExtension:
                    loadImportStrategyBasedSettings();
                    break;
                default:
                    loadImportStrategyBasedSettings();
                    panel.setImportStrategy(model.getImportStrategy());
                    break;
            }
        }
        BackNextController.getInstance().changeFinishToNext();
    }

    private void loadImportStrategyBasedSettings() {
        panel.setHunkSize(model.getHunkSize());
        panel.setSendBuffer(model.getSendBuffer());
    }

    @Override
    public void back() {
        final BackNextController bnc = BackNextController.getInstance();
        bnc.setNextButtonEnabled(true);
        bnc.changeFinishToNext();
    }

    @Override
    public void saveSettings() {
        final String sosURL = panel.getSOSURL();
        final String offering = panel.getOfferingName();
        final String binding = panel.getSosBinding();
        final String version = panel.getSosVersion();
        final boolean generateOfferingFromSensorName = panel.isGenerateOfferingFromSensorName();
        final File configFile = new File(panel.getConfigFile());
        final boolean ignoreColumnMismatch = panel.isIgnoreColumnMismatch();
        if (model == null) {
            model = new Step7Model(sosURL,
                    configFile,
                    generateOfferingFromSensorName,
                    offering,
                    version,
                    binding,
                    ignoreColumnMismatch);
        } else {
            model.setSosURL(sosURL);
            model.setSosVersion(version);
            model.setSosBinding(binding);
            model.setConfigFile(configFile);
            model.setGenerateOfferingFromSensorName(generateOfferingFromSensorName);
            model.setIgnoreColumnMismatch(ignoreColumnMismatch);
            if (!generateOfferingFromSensorName) {
                model.setOffering(offering);
            }
        }
        switch (panel.getImportStrategy()) {
            case SweArrayObservationWithSplitExtension:
                loadImportStrategyBasedSetting();
                model.setImportStrategy(panel.getImportStrategy());
                break;
            default:
                model.setImportStrategy(panel.getImportStrategy());
                break;
        }
    }

    private void loadImportStrategyBasedSetting() {
        model.setHunkSize(panel.getHunkSize());
        model.setSendBuffer(panel.getSendBuffer());
    }

    @Override
    public String getDescription() {
        return Lang.l().step7Description();
    }

    @Override
    public Step7Panel getStepPanel() {
        return panel;
    }

    @Override
    public StepController getNextStepController() {
        return new Step8Controller(model);
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    @Override
    public boolean isFinished() {
        if (isOfferingNameNotGiven()) {
            // user decided to give input but (s)he did NOT, so tell him
            return showErrorDialogAndLogIt(Lang.l().step7OfferingNameNotGiven());
        } else if (isOfferingNameInvalid()) {
            // user gave input but it is not valid
            return showErrorDialogAndLogIt(Lang.l().step7OfferingNameNotValid(panel.getOfferingName()));
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
        String configPath = panel.getConfigFile();
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
        return panel.getSosVersion() != null &&
                panel.getSosVersion().equalsIgnoreCase("2.0.0") &&
                (panel.getSosBinding() == null || panel.getSosBinding().isEmpty());
    }

    private boolean isSosVersionNotGiven() {
        return panel.getSosVersion() == null || panel.getSosVersion().isEmpty();
    }

    private boolean isOfferingNameInvalid() {
        return !panel.isGenerateOfferingFromSensorName() &&
                !XMLTools.isNCName(panel.getOfferingName());
    }

    private boolean isOfferingNameNotGiven() {
        return !panel.isGenerateOfferingFromSensorName() &&
                (panel.getOfferingName() == null ||
                panel.getOfferingName().equalsIgnoreCase(""));
    }

    private boolean showErrorDialogAndLogIt(final String msg) {
        JOptionPane.showMessageDialog(null, msg, Lang.l().errorDialogTitle(), JOptionPane.ERROR_MESSAGE);
        LOG.error(msg);
        return false;
    }

    @Override
    public StepController getNext() {
        return null;
    }

    @Override
    public Step7Model getModel() {
        return model;
    }

}
