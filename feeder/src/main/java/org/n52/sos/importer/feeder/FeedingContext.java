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

import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.Timestamp;

public interface FeedingContext {

    /**
     * The {@link Collector} uses this method for publishing new observations to an {@link Importer} instance.
     *
     * @param insertObservations new collected {@link InsertObservation}s that can be imported to the SOS instance.
     */
    void addObservationForImporting(InsertObservation... insertObservations);

    /**
     *
     * @return the number of the last read line in the current {@link DataFile}.
     */
    int getLastReadLine();

    /**
     * Save the last read line for the current {@link DataFile}.
     *
     * @param lineCounter the last read line to store
     */
    void setLastReadLine(int lineCounter);

    void setLastUsedTimestamp(Timestamp timeStamp);

    Timestamp getLastUsedTimestamp();

    /**
     * Checks for <b>isUseLastTimestamp</b> and <b>newLastUsedTimestamp</b>
     * operations.
     *
     * @param newLastUsedTimestamp the timestamp to check
     *
     * @return <code>true</code> if isUseLastTimestamp is true and
     *         newLastUsedTimestamp is after lastUsedTimestamp
     *         else <code>false</code>.
     */
    boolean shouldUpdateLastUsedTimestamp(Timestamp newLastUsedTimestamp);

}
