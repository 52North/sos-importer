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
package org.n52.sos.importer.feeder.web;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

public class SimpleHttpClient implements HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    private static final ContentType CONTENT_TYPE_TEXT_XML = ContentType.create("text/xml", Consts.UTF_8);

    // TODO is this retry policy okay for us?
    private static final RetryPolicy RETRY_POLICY = new RetryPolicy()
            .retryOn(ConnectException.class)
            .withDelay(10, TimeUnit.SECONDS)
            .withMaxDuration(15, TimeUnit.MINUTES);
    private CloseableHttpClient httpclient;
    private int connectionTimeout;
    private int socketTimeout;

    /**
     * Creates an instance with <code>timeout = {@value #DEFAULT_CONNECTION_TIMEOUT}</code> ms.
     */
    public SimpleHttpClient() {
        this(DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates an instance with a given connection timeout.
     *
     * @param connectionTimeout the connection timeout in milliseconds.
     */
    public SimpleHttpClient(int connectionTimeout) {
        this(connectionTimeout, DEFAULT_SOCKET_TIMEOUT);
    }

    /**
     * Creates an instance with the given timeouts.
     *
     * @param connectionTimeout the connection timeout in milliseconds.
     * @param socketTimeout     the socket timeout in milliseconds.
     */
    public SimpleHttpClient(int connectionTimeout, int socketTimeout) {
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        recreateClient();
    }

    protected SimpleHttpClient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    @Override
    public HttpResponse executeGet(String uri) throws IOException {
        LOG.debug("executing GET method '{}'", uri);
        return executeMethod(new HttpGet(uri));
    }

    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws IOException {
        return executePost(uri, payloadToSend.xmlText(), CONTENT_TYPE_TEXT_XML);
    }

    @Override
    public HttpResponse executePost(String uri, String payloadToSend) throws IOException {
        return executePost(uri, payloadToSend, CONTENT_TYPE_TEXT_XML);
    }

    @Override
    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws IOException {
        StringEntity requestEntity = new StringEntity(payloadToSend, contentType);
        LOG.trace("payload to send: {}", payloadToSend);
        return executePost(uri, requestEntity);
    }

    @Override
    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws IOException {
        LOG.debug("executing POST method to '{}'.", uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(payloadToSend);
        return executeMethod(post);
    }

    @Override
    public HttpResponse executeMethod(HttpRequestBase method) throws IOException {
        return Failsafe.with(RETRY_POLICY)
                .onFailedAttempt(ex -> LOG.warn("Could not connect to host; retrying", ex))
                .get(() -> httpclient.execute(method));
    }

    public void setConnectionTimout(int timeout) {
        connectionTimeout = timeout;
        recreateClient();
    }

    public void setSocketTimout(int timeout) {
        socketTimeout = timeout;
        recreateClient();
    }

    private void recreateClient() {
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException ex) {
                LOG.warn("Error closing client", ex);
            }
            httpclient = null;
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
        httpclient = HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

}