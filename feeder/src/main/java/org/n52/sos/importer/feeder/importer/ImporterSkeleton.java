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
package org.n52.sos.importer.feeder.importer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.FeedingContext;
import org.n52.sos.importer.feeder.Importer;
import org.n52.sos.importer.feeder.SosClient;
import org.n52.sos.importer.feeder.model.InsertObservation;

public abstract class ImporterSkeleton implements Importer {

    protected Configuration configuration;

    protected SosClient sosClient;

    protected List<InsertObservation> failedObservations = new LinkedList<>();

    protected List<String> failedSensorInsertions = new LinkedList<>();

    protected FeedingContext context;

    public ImporterSkeleton() {
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setFeedingContext(FeedingContext context) {
        this.context = context;
    }

    @Override
    public void setSosClient(SosClient sosClient) {
        this.sosClient = sosClient;
    }

    @Override
    public void startImporting() {
        if (sosClient == null) {
            throw new IllegalArgumentException("Field 'sosClient' MUST not be null!");
        }
        if (configuration == null) {
            throw new IllegalArgumentException("Field 'configuration' MUST not be null!");
        }
        if (context == null) {
            throw new IllegalArgumentException("Field 'context' MUST not be null!");
        }
    }

    @Override
    public boolean hasFailedObservations() {
        return !failedObservations.isEmpty();
    }

    @Override
    public List<InsertObservation> getFailedObservations() {
        return hasFailedObservations() ? Collections.unmodifiableList(failedObservations) : Collections.emptyList();
    }

}