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

public class Step1Model implements StepModel {

    private String url;
    private String user;
    private String password;
    private String directory;
    private String filenameSchema;
    private boolean regex;
    private int feedingType;
    private String csvFilePath = "";
    private String fileEncoding = "";

    /**
     * <p>Getter for the field <code>feedingType</code>.</p>
     *
     * @return a int.
     */
    public int getFeedingType() {
        return feedingType;
    }

    /**
     * <p>Setter for the field <code>feedingType</code>.</p>
     *
     * @param feedingType a int.
     */
    public void setFeedingType(final int feedingType) {
        this.feedingType = feedingType;
    }

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl() {
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * <p>Getter for the field <code>user</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUser() {
        return user;
    }

    /**
     * <p>Setter for the field <code>user</code>.</p>
     *
     * @param user a {@link java.lang.String} object.
     */
    public void setUser(final String user) {
        this.user = user;
    }

    /**
     * <p>Getter for the field <code>password</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password a {@link java.lang.String} object.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * <p>isRegex.</p>
     *
     * @return a boolean.
     */
    public boolean isRegex() {
        return regex;
    }

    /**
     * <p>Setter for the field <code>regex</code>.</p>
     *
     * @param regex a boolean.
     */
    public void setRegex(final boolean regex) {
        this.regex = regex;
    }

    /**
     * <p>Getter for the field <code>directory</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * <p>Setter for the field <code>directory</code>.</p>
     *
     * @param directory a {@link java.lang.String} object.
     */
    public void setDirectory(final String directory) {
        this.directory = directory;
    }

    /**
     * <p>Getter for the field <code>filenameSchema</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFilenameSchema() {
        return filenameSchema;
    }

    /**
     * <p>Setter for the field <code>filenameSchema</code>.</p>
     *
     * @param filenameSchema a {@link java.lang.String} object.
     */
    public void setFilenameSchema(final String filenameSchema) {
        this.filenameSchema = filenameSchema;
    }

    /**
     * <p>setCSVFilePath.</p>
     *
     * @param newCsvFilePath a {@link java.lang.String} object.
     */
    public void setCSVFilePath(final String newCsvFilePath) {
        this.csvFilePath = newCsvFilePath;
    }

    /**
     * <p>getCSVFilePath.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCSVFilePath() {
        return csvFilePath;
    }

    /**
     * <p>Getter for the field <code>fileEncoding</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * <p>Setter for the field <code>fileEncoding</code>.</p>
     *
     * @param fileEncoding a {@link java.lang.String} object.
     */
    public void setFileEncoding(final String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

}
