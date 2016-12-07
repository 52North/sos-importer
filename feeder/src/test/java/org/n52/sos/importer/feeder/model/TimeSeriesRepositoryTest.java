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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;


public class TimeSeriesRepositoryTest {
	
	@Test
	public void shouldReturnRegisterSensorForCorrectSensor() {
		TimeSeriesRepository tsr = new TimeSeriesRepository(2);
		String sensorURI = "test-sensor-1-uri";
		ObservedProperty observedProperty1 = new ObservedProperty("test-obs-prop-1-name", "test-obs-prop-1-uri");
		ObservedProperty observedProperty2 = new ObservedProperty("test-obs-prop-2-name", "test-obs-prop-2-uri");
		UnitOfMeasurement uom = new UnitOfMeasurement("uom-code", "uom-uri");
		Sensor sensor = new Sensor("test-sensor-1-name", sensorURI);
		FeatureOfInterest foi = new FeatureOfInterest("foi-name", "foi-uri", null);
		Object value = 1;
		Timestamp timeStamp = new Timestamp().set(System.currentTimeMillis());
		Offering off = new Offering("offering-name", "offering-uri");
		String mvType = "mv-type";
		InsertObservation io = new InsertObservation(sensor, foi, value, timeStamp, uom , observedProperty1, off, mvType);
		InsertObservation io2 = new InsertObservation(sensor, foi, 2, timeStamp, uom, observedProperty2, off, mvType);
		InsertObservation[] ios = {io, io2 };
		tsr.addObservations(ios);
		RegisterSensor registerSensor = tsr.getRegisterSensor(sensorURI);
		assertThat(registerSensor.getSensorURI(), is(sensorURI));
		assertThat(registerSensor.getObservedProperties(), hasSize(2));
		assertThat(registerSensor.getObservedProperties(), Matchers.containsInAnyOrder(observedProperty1,observedProperty2));
	}

}
