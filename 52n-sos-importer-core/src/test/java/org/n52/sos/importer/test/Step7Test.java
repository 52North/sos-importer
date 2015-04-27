/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

public class Step7Test {
	public static void main(final String[] args) {
		TableController.getInstance().setContent(TestData.EXAMPLE_TABLE_MINI);
		final int firstLineWithData = 0;

		final DateAndTime dtm1 = new DateAndTime();
		dtm1.setGroup("1");
		final DateAndTimeController dtc1 = new DateAndTimeController(dtm1);
		dtc1.assignPattern("dd/MM/yyyy", new Column(0,firstLineWithData));
		ModelStore.getInstance().add(dtm1);

		final DateAndTime dtm2 = new DateAndTime();
		dtm2.setGroup("1");
		final DateAndTimeController dtc2 = new DateAndTimeController(dtm1);
		dtc2.assignPattern("HH:mm", new Column(1,firstLineWithData));
		ModelStore.getInstance().add(dtm2);

		dtc2.mergeDateAndTimes();

		final DateAndTime dtm = ModelStore.getInstance().getDateAndTimes().get(0);
		dtm.setSecond(new Second(0));
		dtm.setTimeZone(new TimeZone(0));

		final Position p = new Position();
		p.setGroup("A");
		final Latitude lat = new Latitude(52.5, "°");
		final Longitude lon = new Longitude(7.5, "°");
		final Height h = new Height(100, "m");
		final EPSGCode epsgCode = new EPSGCode(4236);
		p.setLatitude(lat);
		p.setLongitude(lon);
		p.setHeight(h);
		p.setEPSGCode(epsgCode);

		final ObservedProperty op = new ObservedProperty();
		op.setName("Temperature");
		final UnitOfMeasurement uom = new UnitOfMeasurement();
		uom.setName("degC");
		final FeatureOfInterest foi = new FeatureOfInterest();
		foi.setName("Weatherstation Muenster");
		final Sensor sn = new Sensor();
		sn.setName("Thermometer 1");

		final Sensor sn2 = new Sensor();
		sn2.setName("Thermometer 2");
		try {
			sn2.setURI(new URI("http://thermo.org/123"));
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}

		final NumericValue nv1 = new NumericValue();
		nv1.setTableElement(new Column(1,firstLineWithData));
		nv1.setDateAndTime(dtm);
		nv1.setObservedProperty(op);
		nv1.setFeatureOfInterest(foi);
		nv1.setSensor(sn);
		nv1.setUnitOfMeasurement(uom);

		final NumericValue nv2 = new NumericValue();
		nv2.setTableElement(new Column(2,firstLineWithData));
		nv2.setDateAndTime(dtm);
		nv2.setObservedProperty(op);
		nv2.setFeatureOfInterest(foi);
		nv2.setSensor(sn2);
		nv2.setUnitOfMeasurement(uom);

		foi.setPosition(p);

		ModelStore.getInstance().add(nv1);
		ModelStore.getInstance().add(nv2);
		ModelStore.getInstance().add(foi);

		final MainController f = MainController.getInstance();

		f.setStepController(new Step7Controller());
	}
}
