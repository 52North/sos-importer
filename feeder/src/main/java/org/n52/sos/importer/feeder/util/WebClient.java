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
package org.n52.sos.importer.feeder.util;

import java.io.File;
import java.io.IOException;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebClient {

    protected static final String PROXY_PORT = "proxyPort";
    protected static final Logger LOG = LoggerFactory.getLogger(WebClient.class);

    protected File file;

    protected Configuration config;

    public WebClient(Configuration config) {
        this.config = config;
    }

    public abstract DataFile download();

    protected void createTempFile() {
        final String fileName = config.getFileName();
        try {
            file = File.createTempFile(fileName + "_", ".csv");
        } catch (IOException e1) {
            LOG.error("could not create TempFile '{}.csv'", fileName);
        }
    }

    public boolean deleteDownloadedFile() {
        return file != null && file.delete();
    }
}
