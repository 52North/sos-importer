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
package org.n52.sos.importer.model.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.Step5aModel;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.StepModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x04.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x04.RemoteFileDocument.RemoteFile;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * In this class the XML model for an CSV file is stored for later re-use by
 * another application.
 *
 * @author e.h.juerrens@52north.org
 * @since 0.2
 * @version $Id: $Id
 */
public class Model {

    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    private final SosImportConfiguration sosImpConf;

    private StepModel[] stepModells = new StepModel[1];

    /**
     * Create a new and empty model
     */
    public Model() {
        sosImpConf = SosImportConfiguration.Factory.newInstance();
    }

    /**
     * Create model based on xml file
     *
     * @param xmlFileWithModel
     *            the file containing the <code>Model</code>
     * @throws org.apache.xmlbeans.XmlException
     *             thrown while parsing the file &rarr; <code>Model</code>
     *             file is <b>not</b> valid.
     * @throws java.io.IOException
     *             having any problems while reading file
     */
    public Model(final File xmlFileWithModel) throws XmlException, IOException {
        final SosImportConfigurationDocument sosImpConfDoc = SosImportConfigurationDocument.Factory
                .parse(xmlFileWithModel);
        sosImpConf = sosImpConfDoc.getSosImportConfiguration();
    }

    /**
     * Create model based on an existing one
     *
     * @param sosImpConf a {@link SosImportConfiguration} object.
     */
    public Model(final SosImportConfiguration sosImpConf) {
        this.sosImpConf = sosImpConf;
    }

    /**
     * <p>getFileName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileName() {
        if (logger.isTraceEnabled()) {
            logger.trace("getFileName()");
        }
        final DataFile df = sosImpConf.getDataFile();
        String result = null;
        if (df != null) {
            if (df.isSetLocalFile()) {
                final LocalFile lf = df.getLocalFile();
                result = lf.getPath();
                result = result.substring(result.lastIndexOf(File.separatorChar) + 1);
            } else if (df.isSetRemoteFile()) {
                final RemoteFile rf = df.getRemoteFile();
                result = rf.getURL();
                result = result.substring(result.lastIndexOf("/") + 1);
            }
        }
        return result;
    }

    /**
     * <p>registerProvider.</p>
     *
     * @param sm a {@link org.n52.sos.importer.model.StepModel} object.
     * @return a boolean.
     */
    public boolean registerProvider(final StepModel sm) {
        if (logger.isTraceEnabled()) {
            logger.trace("registerProvider(" +
                    (sm == null
                    ? null
                            : sm.getClass().getSimpleName()) +
                    ")");
        }
        //
        ArrayList<StepModel> sMs;
        //
        sMs = createArrayListFromArray(stepModells);
        final boolean result = sMs.add(sm);
        saveProvidersInArray(sMs);
        //
        return result;
    }

    /**
     * <p>removeProvider.</p>
     *
     * @param sm a {@link org.n52.sos.importer.model.StepModel} object.
     * @return a boolean.
     */
    public boolean removeProvider(final StepModel sm) {
        if (logger.isTraceEnabled()) {
            logger.trace("removeProvider(" +
                    (sm == null
                    ? null
                            : sm.getClass().getSimpleName()) +
                                ")");
        }
        //
        ArrayList<StepModel> provider;
        //
        provider = createArrayListFromArray(stepModells);
        final boolean result = provider.remove(sm);
        saveProvidersInArray(provider);
        //
        return result;
    }

    /**
     * <p>save.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a boolean.
     * @throws java.io.IOException if any.
     */
    public boolean save(final File file) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("save(" + (file != null ? file.getName() : null) + ")");
        }
        // laxValidate or validate?
        if (!laxValidate() ||
                sosImpConf.getCsvMetadata() == null ||
                sosImpConf.getDataFile() == null ||
                sosImpConf.getSosMetadata() == null) {
            return false;
        }
        //
        // check write access to file
        if (file != null) {
            if (!file.exists()) {
                final String fileString = "File ";
                if (logger.isDebugEnabled()) {
                    logger.debug(fileString + file
                            + " does not exist. Try to create it.");
                }
                if (!file.createNewFile()) {
                    logger.error("Could not create file " + file);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(fileString + file + " created");
                    }
                }
            }
            if (file.isFile()) {
                if (file.canWrite()) {
                    final SosImportConfigurationDocument doc =
                            SosImportConfigurationDocument.Factory.newInstance();
                    // insert schema location
                    final XmlCursor c = sosImpConf.newCursor();
                    c.toFirstChild();
                    c.insertNamespace(Constants.XML_SCHEMA_PREFIX,
                            Constants.XML_SCHEMA_NAMESPACE);
                    c.insertAttributeWithValue(Constants.XML_SCHEMALOCATION_QNAME,
                            Constants.XML_SOS_IMPORTER_SCHEMA_LOCATION);
                    final XmlOptions xmlOpts = new XmlOptions()
                            .setSavePrettyPrint()
                            .setSavePrettyPrintIndent(4)
                            .setUseDefaultNamespace();
                    doc.setSosImportConfiguration(sosImpConf);
                    doc.save(file, xmlOpts);
                    return true;
                } else {
                    logger.error("model not saved: could not write to file: "
                            + file);
                }
            } else {
                logger.error("model not saved: file is not a file: " + file);
            }
        } else {
            logger.error("model not saved: file is null");
        }
        // }
        return false;
    }

    /**
     * Updates the model. Should be called when one of the providers has
     * changed.
     */
    public void updateModel() {
        if (logger.isTraceEnabled()) {
            logger.trace("updateModel()");
        }
        /*
         * check each provider and update the internal model
         * using ModelHandler for each StepModel
         */

        if (stepModells != null && stepModells.length > 0) {
            //
            for (final StepModel sm : stepModells) {
                //
                if (sm instanceof Step1Model) {
                    //
                    final Step1Model s1M = (Step1Model) sm;
                    new Step1ModelHandler().handleModel(s1M, sosImpConf);
                    //
                } else if (sm instanceof Step2Model) {
                    //
                    final Step2Model s2M = (Step2Model) sm;
                    new Step2ModelHandler().handleModel(s2M, sosImpConf);
                    //
                } else if (sm instanceof Step3Model) {
                    //
                    final Step3Model s3M = (Step3Model) sm;
                    new Step3ModelHandler().handleModel(s3M, sosImpConf);
                    //
                } else if (sm instanceof Step4aModel) {
                    //
                    final Step4aModel s4aM = (Step4aModel) sm;
                    new Step4aModelHandler().handleModel(s4aM, sosImpConf);
                    //
                } else if (sm instanceof Step4bModel) {
                    //
                    final Step4bModel s4bM = (Step4bModel) sm;
                    new Step4bModelHandler().handleModel(s4bM, sosImpConf);
                    //
                } else if (sm instanceof Step5aModel) {
                    //
                    final Step5aModel s5aM = (Step5aModel) sm;
                    new Step5aModelHandler().handleModel(s5aM, sosImpConf);
                    //
                } else if (sm instanceof Step5cModel) {
                    //
                    final Step5cModel s5cM = (Step5cModel) sm;
                    new Step5cModelHandler().handleModel(s5cM, sosImpConf);
                    //
                } else if (sm instanceof Step6aModel) {
                    //
                    final Step6aModel s6aM = (Step6aModel) sm;
                    new Step6aModelHandler().handleModel(s6aM, sosImpConf);
                    //
                } else if (sm instanceof Step6bModel) {
                    //
                    final Step6bModel s6bM = (Step6bModel) sm;
                    new Step6bModelHandler().handleModel(s6bM, sosImpConf);
                    //
                } else if (sm instanceof Step6bSpecialModel) {
                    //
                    final Step6bSpecialModel s6bSM = (Step6bSpecialModel) sm;
                    new Step6bSpecialModelHandler().handleModel(s6bSM, sosImpConf);
                    //
                } else if (sm instanceof Step6cModel) {
                    //
                    final Step6cModel s6cM = (Step6cModel) sm;
                    new Step6cModelHandler().handleModel(s6cM, sosImpConf);
                    //
                } else if (sm instanceof Step7Model) {
                    //
                    final Step7Model s7M = (Step7Model) sm;
                    new Step7ModelHandler().handleModel(s7M, sosImpConf);
                }
            }
        }
    }

    /**
     * Should be called after final step to validate the final model.
     *
     * @return a boolean.
     */
    public boolean validate() {
        if (logger.isTraceEnabled()) {
            logger.trace("validate()");
        }
        //
        final SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory.newInstance();
        doc.setSosImportConfiguration(sosImpConf);
        final boolean modelValid = doc.validate();
        if (!modelValid) {
            logger.error("The model is not valid. Please update your values.");
        }
        return modelValid;
    }

    /**
     * <p>laxValidate.</p>
     *
     * @return a boolean.
     */
    public boolean laxValidate() {
        final SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory.newInstance();
        doc.setSosImportConfiguration(sosImpConf);
        final Collection<XmlError> exs = XMLBeansParser.validate(doc);
        for (final XmlError xmlError : exs) {
            logger.error("Xml error: {}", xmlError);
        }
        return (exs.size() == 0) ? true : false;
    }

    /*
     * Private Helper methods for provider and model handling
     */
    private ArrayList<StepModel> createArrayListFromArray(final StepModel[] models) {
        if (logger.isTraceEnabled()) {
            logger.trace("\tcreateArrayListFromArray()");
        }
        //
        ArrayList<StepModel> result;
        //
        result = new ArrayList<StepModel>(stepModells.length + 1);
        for (final StepModel stepModel : stepModells) {
            if (stepModel != null) {
                result.add(stepModel);
            }
        }
        result.trimToSize();
        //
        return result;
    }

    private void saveProvidersInArray(final ArrayList<StepModel> aL) {
        if (logger.isTraceEnabled()) {
            logger.trace("\tsaveProvidersInArray()");
        }
        //
        aL.trimToSize();
        stepModells = aL.toArray(new StepModel[aL.size()]);
    }

}
