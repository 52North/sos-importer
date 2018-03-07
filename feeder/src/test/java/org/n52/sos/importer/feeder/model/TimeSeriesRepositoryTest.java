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
package org.n52.sos.importer.feeder.model;

import java.net.URI;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.n52.sos.importer.feeder.Configuration;

public class TimeSeriesRepositoryTest {

    @Test
    public void shouldReturnRegisterSensorForCorrectSensor() {
        TimeSeriesRepository tsr = new TimeSeriesRepository(Mockito.mock(Configuration.class));
        URI sensorURI = URI.create("test-sensor-1-uri");
        ObservedProperty observedProperty1 = new ObservedProperty("test-obs-prop-1-name", "test-obs-prop-1-uri");
        ObservedProperty observedProperty2 = new ObservedProperty("test-obs-prop-2-name", "test-obs-prop-2-uri");
        UnitOfMeasurement uom = new UnitOfMeasurement("uom-code", "uom-uri");
        Sensor sensor = new Sensor("test-sensor-1-name", sensorURI.toString());
        FeatureOfInterest foi = new FeatureOfInterest("foi-name", "foi-uri", null);
        Object value = 1;
        Timestamp timeStamp = new Timestamp().ofUnixTimeMillis(System.currentTimeMillis());
        Offering off = new Offering("offering-name", "offering-uri");
        String mvType = "mv-type";
        InsertObservation io =
                new InsertObservation(sensor,
                    foi,
                    value,
                    timeStamp,
                    uom,
                    observedProperty1,
                    off,
                    Optional.empty(),
                    mvType);
        InsertObservation io2 = new InsertObservation(sensor,
            foi,
            2,
            timeStamp,
            uom,
            observedProperty2,
            off,
            Optional.empty(),
            mvType);
        InsertObservation[] ios = {io, io2 };
        tsr.addObservations(ios);
        InsertSensor insertSensor = tsr.getInsertSensor(sensorURI);
        Assert.assertThat(insertSensor.getSensorURI(), org.hamcrest.CoreMatchers.is(sensorURI));
        Assert.assertThat(insertSensor.getObservedProperties(), org.hamcrest.Matchers.hasSize(2));
        Assert.assertThat(insertSensor.getObservedProperties(),
                Matchers.containsInAnyOrder(observedProperty1, observedProperty2));
    }

}
