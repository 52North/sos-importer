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
import org.n52.sos.importer.model.Step4dModel;
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
import org.x52North.sensorweb.sos.importer.x05.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x05.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x05.RemoteFileDocument.RemoteFile;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * In this class the XML model for an CSV file is stored for later re-use by
 * another application.
 *
 * @author e.h.juerrens@52north.org
 * @since 0.2
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
        logger.trace("updateModel()");
        /*
         * check each provider and update the internal model
         * using ModelHandler for each StepModel
         */

        if (stepModells != null && stepModells.length > 0) {
            //
            for (final StepModel model : stepModells) {
                //
                if (model instanceof Step1Model) {
                    //
                    new Step1ModelHandler().handleModel((Step1Model) model, sosImpConf);
                    //
                } else if (model instanceof Step2Model) {
                    //
                    new Step2ModelHandler().handleModel((Step2Model) model, sosImpConf);
                    //
                } else if (model instanceof Step3Model) {
                    //
                    new Step3ModelHandler().handleModel((Step3Model) model, sosImpConf);
                    //
                } else if (model instanceof Step4aModel) {
                    //
                    new Step4aModelHandler().handleModel((Step4aModel) model, sosImpConf);
                    //
                } else if (model instanceof Step4bModel) {
                    //
                    new Step4bModelHandler().handleModel((Step4bModel) model, sosImpConf);
                    //
                } else if (model instanceof Step4dModel) {
                    //
                    new Step4dModelHandler().handleModel((Step4dModel) model, sosImpConf);
                } else if (model instanceof Step5aModel) {
                    //
                    new Step5aModelHandler().handleModel((Step5aModel) model, sosImpConf);
                    //
                } else if (model instanceof Step5cModel) {
                    //
                    new Step5cModelHandler().handleModel((Step5cModel) model, sosImpConf);
                    //
                } else if (model instanceof Step6aModel) {
                    //
                    new Step6aModelHandler().handleModel((Step6aModel) model, sosImpConf);
                    //
                } else if (model instanceof Step6bModel) {
                    //
                    new Step6bModelHandler().handleModel((Step6bModel) model, sosImpConf);
                    //
                } else if (model instanceof Step6bSpecialModel) {
                    //
                    new Step6bSpecialModelHandler().handleModel((Step6bSpecialModel) model, sosImpConf);
                    //
                } else if (model instanceof Step6cModel) {
                    //
                    new Step6cModelHandler().handleModel((Step6cModel) model, sosImpConf);
                    //
                } else if (model instanceof Step7Model) {
                    //
                    new Step7ModelHandler().handleModel((Step7Model) model, sosImpConf);
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
        if (exs.isEmpty()) {
            return true;
        }
        logger.error("XML of configuration model is not valid. See the following output for details");
        for (final XmlError xmlError : exs) {
            logger.error("Xml error: Message: {}; Location: Line: {}, Column: {}; ErrorCode: {}",
                    xmlError.getMessage(),
                    xmlError.getLine(),
                    xmlError.getColumn(),
                    xmlError.getErrorCode());
        }
        return false;
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
        result = new ArrayList<>(stepModells.length + 1);
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
