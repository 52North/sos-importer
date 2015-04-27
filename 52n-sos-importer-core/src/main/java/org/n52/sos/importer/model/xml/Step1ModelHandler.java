/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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

import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.view.Step1Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.CredentialsDocument.Credentials;
import org.x52North.sensorweb.sos.importer.x04.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x04.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x04.RemoteFileDocument.RemoteFile;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Get updates from Step1Model<br />
 * Provided information:
 * <ul>
 * <li>DataFile.LocalFile.Path</li>
 * </ul>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step1ModelHandler implements ModelHandler<Step1Model> {

	private static final Logger logger = LoggerFactory.getLogger(Step1ModelHandler.class);

	@Override
	public void handleModel(final Step1Model stepModel, final SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}

		DataFile dF = sosImportConf.getDataFile();
		if (dF == null) {
			dF = sosImportConf.addNewDataFile();
		}

		if (stepModel.getFeedingType() == Step1Panel.CSV_FILE) {
			if (dF.getRemoteFile() != null) {
				dF.setRemoteFile(null);
			}
			final LocalFile lF = (dF.getLocalFile() == null) ? dF.addNewLocalFile() : dF.getLocalFile();
			final String path = stepModel.getCSVFilePath();
			if (path != null && !path.equals("")) {
				lF.setPath(path);
			} else {
				final String msg = "empty path to CSV file in Step1Model: " + path;
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			final String encoding = stepModel.getFileEncoding();
			if (encoding != null && !encoding.isEmpty()) {
				lF.setEncoding(encoding);
			}
		} else {
			if (dF.getLocalFile() != null) {
				dF.setLocalFile(null);
			}
			final RemoteFile rF = (dF.getRemoteFile() == null)? dF.addNewRemoteFile() : dF.getRemoteFile();
			String url = stepModel.getUrl();
			if (stepModel.getDirectory() != null && url.charAt(url.length() - 1) != '/'
					&& stepModel.getDirectory().charAt(0) != '/') {
				url += '/';
			}
			url += stepModel.getDirectory();
			if (url.charAt(url.length() - 1) != '/' && stepModel.getFilenameSchema().charAt(0) != '/') {
				url += '/';
			}
			url += stepModel.getFilenameSchema();
			rF.setURL(url);
			final Credentials cDoc = (rF.getCredentials() == null)? rF.addNewCredentials() : rF.getCredentials();
			cDoc.setUserName(stepModel.getUser());
			cDoc.setPassword(stepModel.getPassword());
		}
		dF.setReferenceIsARegularExpression(stepModel.isRegex());
	}
}
