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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
package org.n52.sos.importer.model;

import java.io.File;

import org.n52.sos.importer.Constants.ImportStrategy;

public class Step7Model implements StepModel {

    private String sosURL;

    private String version;

    private String binding;

    private File configFile;

    private boolean generateOfferingFromSensorName;

    private String offering;

    private ImportStrategy importStrategy = ImportStrategy.SingleObservation;

    private int sendBuffer = 25000;

    private int hunkSize = 5000;

    private boolean ignoreColumnMismatch;

    /**
     * <p>Constructor for Step7Model.</p>
     *
     * @param sosURL a {@link java.lang.String} object.
     * @param configFile a {@link java.io.File} object.
     * @param generateOfferingFromSensorName a boolean.
     * @param offering a {@link java.lang.String} object.
     * @param version a {@link java.lang.String} object.
     * @param binding a {@link java.lang.String} object.
     * @param ignoreColumnMismatch a boolean
     */
    public Step7Model(final String sosURL,
            final File configFile,
            final boolean generateOfferingFromSensorName,
            final String offering,
            final String version,
            final String binding,
            final boolean ignoreColumnMismatch) {
        this.sosURL = sosURL;
        this.configFile = configFile;
        this.generateOfferingFromSensorName = generateOfferingFromSensorName;
        this.offering = offering;
        this.binding = binding;
        this.version = version;
        this.ignoreColumnMismatch = ignoreColumnMismatch;
    }

    /**
     * <p>Constructor for Step7Model.</p>
     */
    public Step7Model() {
        this(null, null, false, null, null, null, false);
    }

    /**
     * <p>Getter for the field <code>sosURL</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSosURL() {
        return sosURL;
    }

    /**
     * <p>Getter for the field <code>configFile</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * <p>Setter for the field <code>configFile</code>.</p>
     *
     * @param configFile a {@link java.io.File} object.
     */
    public void setConfigFile(final File configFile) {
        this.configFile = configFile;
    }

    /**
     * <p>Setter for the field <code>sosURL</code>.</p>
     *
     * @param sosURL a {@link java.lang.String} object.
     */
    public void setSosURL(final String sosURL) {
        this.sosURL = sosURL;
    }

    /**
     * <p>isGenerateOfferingFromSensorName.</p>
     *
     * @return a boolean.
     */
    public boolean isGenerateOfferingFromSensorName() {
        return generateOfferingFromSensorName;
    }

    /**
     * <p>Setter for the field <code>generateOfferingFromSensorName</code>.</p>
     *
     * @param generateOfferingFromSensorName a boolean.
     */
    public void setGenerateOfferingFromSensorName(
            final boolean generateOfferingFromSensorName) {
        this.generateOfferingFromSensorName = generateOfferingFromSensorName;
    }

    /**
     * <p>Getter for the field <code>offering</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOffering() {
        return offering;
    }

    /**
     * <p>Setter for the field <code>offering</code>.</p>
     *
     * @param offering a {@link java.lang.String} object.
     */
    public void setOffering(final String offering) {
        this.offering = offering;
    }

    /**
     * <p>Getter for the field <code>binding</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBinding() {
        return binding;
    }

    /**
     * <p>setSosVersion.</p>
     *
     * @param newVersion a {@link java.lang.String} object.
     */
    public void setSosVersion(final String newVersion) {
        version = newVersion;
    }

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>setSosBinding.</p>
     *
     * @param newBinding a {@link java.lang.String} object.
     */
    public void setSosBinding(final String newBinding) {
        binding = newBinding;
    }

    /**
     * <p>Getter for the field <code>importStrategy</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.Constants.ImportStrategy} object.
     */
    public ImportStrategy getImportStrategy() {
        return importStrategy;
    }

    /**
     * <p>Setter for the field <code>importStrategy</code>.</p>
     *
     * @param importStrategy a {@link org.n52.sos.importer.Constants.ImportStrategy} object.
     * @return a {@link org.n52.sos.importer.model.Step7Model} object.
     */
    public Step7Model setImportStrategy(final ImportStrategy importStrategy) {
        this.importStrategy = importStrategy;
        return this;
    }

    /**
     * <p>Getter for the field <code>sendBuffer</code>.</p>
     *
     * @return a int.
     */
    public int getSendBuffer() {
        return sendBuffer;
    }

    /**
     * <p>Setter for the field <code>sendBuffer</code>.</p>
     *
     * @param sendBuffer a int.
     * @return a {@link org.n52.sos.importer.model.Step7Model} object.
     */
    public Step7Model setSendBuffer(final int sendBuffer) {
        this.sendBuffer = sendBuffer;
        return this;
    }

    /**
     * <p>Getter for the field <code>hunkSize</code>.</p>
     *
     * @return a int.
     */
    public int getHunkSize() {
        return hunkSize;
    }

    /**
     * <p>Setter for the field <code>hunkSize</code>.</p>
     *
     * @param hunkSize a int.
     * @return a {@link org.n52.sos.importer.model.Step7Model} object.
     */
    public Step7Model setHunkSize(final int hunkSize) {
        this.hunkSize = hunkSize;
        return this;
    }

    public boolean isIgnoreColumnMismatch() {
        return ignoreColumnMismatch;
    }

    public Step7Model setIgnoreColumnMismatch(boolean ignoreColumnMismatch) {
        this.ignoreColumnMismatch = ignoreColumnMismatch;
        return this;
    }

}
