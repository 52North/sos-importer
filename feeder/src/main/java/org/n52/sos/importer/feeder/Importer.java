/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.feeder;

import java.util.List;

import org.n52.sos.importer.feeder.model.InsertObservation;

/**
 * <b>Interface for Observation Importer</b><br>
 * <br>
 * Observations are received by {@link FeedingContext#addObservationForImporting(InsertObservation...)}.
 * These should be imported using the given {@link Configuration} and {@link SosClient}. The process is managed by
 * {@link #startImporting()} and {@link #stopImporting()}.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public interface Importer extends FeedingParticipant {

    void addObservations(InsertObservation... insertObservations) throws Exception;

    List<InsertObservation> getFailedObservations();

    boolean hasFailedObservations();

    void setSosClient(SosClient sosClient);

    void startImporting();

    default void stopImporting() throws Exception {
    }

}
