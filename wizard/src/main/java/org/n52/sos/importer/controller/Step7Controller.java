/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.n52.janmayen.NcName;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
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

    private List<String> absentParentFeatures;

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
        } else if (!ensureExistenceOfRequiredParentFeatures()) {
            return !showErrorDialogAndLogIt(Lang.l().step7RequiredParentFeatureAbsent(absentParentFeatures));
        }
        return true;
    }

    private boolean ensureExistenceOfRequiredParentFeatures() {
        List<FeatureOfInterest> foisToCheck = new ArrayList<>();
        for (FeatureOfInterest foi : ModelStore.getInstance().getFeatureOfInterests()) {
            if (foi.hasParentFeature()) {
                foisToCheck.add(foi);
            }
        }
        if (foisToCheck.isEmpty()) {
            return true;
        }
        // for each parent feature identifier
        StringBuilder postRequestContent = new StringBuilder()
                .append("<sos:GetFeatureOfInterest xmlns:sos=\"http://www.opengis.net/sos/2.0\" ")
                .append("service=\"SOS\" version=\"")
                .append(panel.getSosVersion())
                .append("\">\n");
        for (FeatureOfInterest featureOfInterest : foisToCheck) {
            postRequestContent.append("\t<sos:featureOfInterest>")
                .append(featureOfInterest.getParentFeatureIdentifier())
                .append("</sos:featureOfInterest>\n");
        }
        postRequestContent.append("</sos:GetFeatureOfInterest>");
        // check if not present is SOS
        String responseString = "";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpPost postRequest = new HttpPost(panel.getSOSURL());
        postRequest.setEntity(new StringEntity(postRequestContent.toString(), ContentType.APPLICATION_XML));
        try {
            CloseableHttpResponse response = client.execute(postRequest);
            LOG.debug("HTTP response: {}", response.getStatusLine());
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity);
            client.close();
        } catch (IOException e) {
            LOG.debug("Exception thrown: ", e);
            showErrorDialogAndLogIt("Checking parent features with SOS failed:\n\n" + e.getMessage());
            absentParentFeatures = new ArrayList<>();
            for (FeatureOfInterest foi : foisToCheck) {
                absentParentFeatures.add(foi.getParentFeatureIdentifier());
            }
        }
        // add to list of absent features
        if (responseString != null && !responseString.isEmpty()) {
            absentParentFeatures = new ArrayList<>();
            for (FeatureOfInterest foi : foisToCheck) {
                if (responseString.contains("The value '" +
                        foi.getParentFeatureIdentifier() +
                        "' of the parameter 'featureOfInterest' is invalid")) {
                    absentParentFeatures.add(foi.getParentFeatureIdentifier());
                }
            }
        }
        if (absentParentFeatures == null || absentParentFeatures.isEmpty()) {
            return true;
        }
        return false;
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
                !NcName.isValid(panel.getOfferingName());
    }

    private boolean isOfferingNameNotGiven() {
        return !panel.isGenerateOfferingFromSensorName() &&
                (panel.getOfferingName() == null ||
                panel.getOfferingName().equalsIgnoreCase(""));
    }

    private boolean showErrorDialogAndLogIt(final String msg) {
        JOptionPane.showMessageDialog(panel, msg, Lang.l().errorDialogTitle(), JOptionPane.ERROR_MESSAGE);
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
