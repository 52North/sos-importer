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
package org.n52.sos.importer.feeder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

import org.n52.sos.importer.feeder.model.InsertObservation;

/**
 * Interface for observation Collectors<br>
 * <br>
 * Collects {@link InsertObservation}s from the given {@link DataFile}.
 * The results of this process <b>MUST</b> be provided via the
 * {@link FeedingContext#addObservationForImporting(InsertObservation...)} method.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 * @since 0.5.0
 * @see Importer
 * @see FeedingContext
 * @see InsertObservation
 */
public interface Collector {

    void setConfiguration(Configuration configuration);

    void collectObservations(DataFile dataFile, CountDownLatch latch)
            throws IOException, ParseException, URISyntaxException;

    void setFeedingContext(FeedingContext context);

    void stopCollecting();

}
