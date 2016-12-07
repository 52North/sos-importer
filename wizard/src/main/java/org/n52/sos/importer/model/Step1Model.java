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
package org.n52.sos.importer.model;

public class Step1Model implements StepModel{

	private String url, user, password, directory, filenameSchema;
	private boolean regex;
	private int feedingType;
	private String csvFilePath = "";
	private String fileEncoding = "";

	public int getFeedingType() {
		return feedingType;
	}

	public void setFeedingType(final int feedingType) {
		this.feedingType = feedingType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(final boolean regex) {
		this.regex = regex;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(final String directory) {
		this.directory = directory;
	}

	public String getFilenameSchema() {
		return filenameSchema;
	}

	public void setFilenameSchema(final String filenameSchema) {
		this.filenameSchema = filenameSchema;
	}

	public void setCSVFilePath(final String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}

	public String getCSVFilePath() {
		return csvFilePath;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(final String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

}
