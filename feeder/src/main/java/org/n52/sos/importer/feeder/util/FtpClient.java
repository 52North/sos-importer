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

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class FtpClient extends WebClient {

    public FtpClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        if (config.isRemoteFileURLRegex()) {
            LOG.error("Regex in remote file URLs are not supported.");
            return null;
        }
        // ftp client
        FTPClient client;

        // proxy
        final String pHost = System.getProperty("proxyHost");
        int pPort = -1;
        if (System.getProperty(PROXY_PORT) != null) {
            pPort = Integer.parseInt(System.getProperty(PROXY_PORT));
        }
        if (pHost != null && pPort != -1) {
            LOG.info("Using proxy for FTP connection!");
            client = new FTPHTTPClient(pHost, pPort);
        } else {
            LOG.info("Using no proxy for FTP connection!");
            client = new FTPClient();
        }

        createTempFile();

        if (file == null) {
            return null;
        }

        try (FileOutputStream fos = new FileOutputStream(file);) {
            client.connect(config.getRemoteFileURL());
            if (config.areRemoteFileCredentialsSet()) {
                client.login(config.getFtpUser(), config.getFtpPassword());
            }
            client.enterLocalPassiveMode();
            URL remoteFileURL = new URL(config.getRemoteFileURL());
            client.retrieveFile(remoteFileURL.getFile(), fos);
            if (config.areRemoteFileCredentialsSet() && !client.logout()) {
                LOG.info("FTP: cannot logout!");
            }
            fos.flush();
        } catch (final IOException e) {
            LOG.error("The file you specified cannot be obtained.");
            return null;
        }
        return new DataFile(config, file);
    }
}
