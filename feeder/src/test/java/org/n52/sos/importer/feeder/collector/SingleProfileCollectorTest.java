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
package org.n52.sos.importer.feeder.collector;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

import org.apache.xmlbeans.XmlException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.n52.sos.importer.feeder.FeedingContext;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.Timestamp;

public class SingleProfileCollectorTest {

    InsertObservation[] insertObservations;

    @Before
    public void setUpTestData() throws IOException, ParseException, XmlException, URISyntaxException {
        SingleProfileCollector collector = new SingleProfileCollector();
        Configuration configuration = new Configuration("src/test/resources/example-data/example-3-config_profile.xml");
        collector.setConfiguration(configuration);
        FeedingContext context = new MyFeedingContext();
        collector.setFeedingContext(context);
        DataFile dataFile = new DataFile(configuration, new File("src/test/resources/example-data/example-3-data_profile.csv"));
        CountDownLatch latch = new CountDownLatch(1);
        collector.collectObservations(dataFile, latch);
    }

    @Test
    public void parseProfileHeader() {
        assertThat(insertObservations, Matchers.notNullValue());
        InsertObservation insertObservation = insertObservations[0];
        assertThat(insertObservation, Matchers.notNullValue());
        assertThat(insertObservation.getFeatureOfInterestName(), is("profile-observation-at--25_1450175--48_8652158"));
        assertThat(insertObservation.getFeatureOfInterestURI().toString(), is("profile-observation-at--25_1450175--48_8652158"));
        assertThat(insertObservation.hasFeatureParentFeature(), is(true));
        assertThat(insertObservation.getParentFeatureIdentifier(), is("profile-parent-feature"));
        assertThat(insertObservation.getEpsgCode(), is(4979));
        assertThat(insertObservation.getFeatureOfInterest().getPosition().getValueByAxisAbbreviation("Lat"), is(-25.1450175));
        assertThat(insertObservation.getFeatureOfInterest().getPosition().getValueByAxisAbbreviation("Long"), is(-48.8652158));
        assertThat(insertObservation.getFeatureOfInterest().getPosition().getValueByAxisAbbreviation("h"), is(790.719970703125));
        assertThat(insertObservation.getResultTime(), is(new Timestamp("2013-03-12T18:52:11+00:00")));
        assertThat(insertObservation.getSensorName(), is("CC1305009"));
        assertThat(insertObservation.getSensorURI().toString(), is("CC1305009"));
    }

    @Test
    public void collectObservationsFromSampleData() {
        assertThat(insertObservations, Matchers.notNullValue());
        assertThat(insertObservations.length, is(8));
        InsertObservation io = insertObservations[3];
        assertThat(io.getMeasuredValueType(), is("NUMERIC"));
        assertThat(io.getUnitOfMeasurementCode(), is("µs/cm"));
        assertThat(io.getResultValue(), is(65.400440916474579));
        assertThat(io.getObservedProperty().getName(), is("Conductivity"));
        assertThat(io.getObservedProperty().getUri().toString(), is("Conductivity"));
        assertThat(io.getOffering().getUri().toString(), is("CC1305009-offering"));
        assertThat(io.getOffering().getName(), is("CC1305009-offering"));
    }

    private class MyFeedingContext implements FeedingContext {

        @Override
        public void addObservationForImporting(InsertObservation... iobs) {
            insertObservations = iobs;
        }

        @Override
        public int getLastReadLine() {
            return 0;
        }

        @Override
        public void setLastReadLine(int lineCounter) {
        }

        @Override
        public void setLastUsedTimestamp(Timestamp timeStamp) {
        }

        @Override
        public Timestamp getLastUsedTimestamp() {
            return null;
        }

        @Override
        public boolean shouldUpdateLastUsedTimestamp(Timestamp newLastUsedTimestamp) {
            return false;
        }

    }

}
