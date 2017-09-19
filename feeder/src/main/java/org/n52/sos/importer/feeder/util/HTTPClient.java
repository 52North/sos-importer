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

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class HTTPClient extends WebClient {

    public HTTPClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        if (config.isRemoteFileURLRegex()) {
            LOG.error("Regex in remote file URLs are not supported.");
            return null;
        }
        // HttpClient
        CloseableHttpClient client;

        // proxy
        final String pHost = System.getProperty("proxyHost", "proxy");
        int pPort = -1;
        if (System.getProperty(PROXY_PORT) != null) {
            pPort = Integer.parseInt(System.getProperty(PROXY_PORT));
        }
        if (pHost != null && pPort != -1) {
            LOG.info("Using proxy for HTTP connection!");
            client = HttpClientBuilder.create().setProxy(new HttpHost(pHost, pPort)).build();
        } else {
            client = HttpClients.createMinimal();
        }

        createTempFile();

        if (file == null) {
            return null;
        }

        try (FileOutputStream fos = new FileOutputStream(file);) {
            CloseableHttpResponse response = client.execute(new HttpGet(config.getRemoteFileURL()));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity.writeTo(fos);
            }
        } catch (ClientProtocolException e) {
            LOG.error("A HTTP Protocol error occured '{}'", e);
            return null;
        } catch (IOException e) {
            LOG.error("A IO error occured '{}'", e);
            return null;
        }
        return new DataFile(config, file);
    }
}
