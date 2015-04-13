/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.n52.sos.importer.Constants;
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

public class Step7TestGLDAS {
	public static void main(final String[] args) throws URISyntaxException {
		// init logger
		//
		final ModelStore ms = ModelStore.getInstance();
		final TableController tc = TableController.getInstance();
		Constants.DECIMAL_SEPARATOR = ',';
		Constants.THOUSANDS_SEPARATOR = '.';
		tc.setContent(TestData.EXMAPLE_TABLE_GLDAS);
		final int firstLineWithData = 1;
		tc.setFirstLineWithData(firstLineWithData);

		final DateAndTime dtm1 = new DateAndTime();
		dtm1.setGroup("1");
		final DateAndTimeController dtc1 = new DateAndTimeController(dtm1);
		dtc1.assignPattern("d.M.yyyy", new Column(4,firstLineWithData));
		ms.add(dtm1);

		final DateAndTime dtm2 = new DateAndTime();
		dtm2.setGroup("1");
		final DateAndTimeController dtc2 = new DateAndTimeController(dtm1);
		dtc2.assignPattern("H,00", new Column(5,firstLineWithData));
		ms.add(dtm2);

		final DateAndTime dtm3 = new DateAndTime();
		dtm3.setGroup("1");
		final DateAndTimeController dtc3 = new DateAndTimeController(dtm1);
		dtc3.assignPattern("m,00", new Column(6,firstLineWithData));
		ms.add(dtm3);

		dtc2.mergeDateAndTimes();

		final DateAndTime dtm = ModelStore.getInstance().getDateAndTimes().get(0);
		dtm.setSecond(new Second(0));
		dtm.setTimeZone(new TimeZone(1));

		final Position p = new Position();
		p.setGroup("A");
		final Latitude lat = new Latitude(new Column(2, firstLineWithData),"LAT");
		final Longitude lon = new Longitude(new Column(1,firstLineWithData),"LON");
		final Height h = new Height(100, "m");
		final EPSGCode epsgCode = new EPSGCode(4326);
		p.setLatitude(lat);
		p.setLongitude(lon);
		p.setHeight(h);
		p.setEPSGCode(epsgCode);

		final ObservedProperty op = new ObservedProperty();
		op.setName("near_surface_air_temperature");
		op.setURI(new URI("http://www.eo2heaven.org/classifier/parameter/NearSurfaceAirTemperature"));

		final UnitOfMeasurement uom = new UnitOfMeasurement();
		uom.setName("degC");
		uom.setURI(new URI("http://ucum.org/temp/celcsius"));

		final Column[] relatedCols = {
				new Column(2, firstLineWithData),
				new Column(1, firstLineWithData),
		};

		final FeatureOfInterest foi = new FeatureOfInterest();
		foi.setGenerated(true);
		foi.setUseNameAfterPrefixAsURI(true);
		foi.setUriPrefix("http://www.eo2heaven.org/feature/");
		foi.setConcatString("/");
		foi.setPosition(p);
		foi.setRelatedCols(relatedCols);

		final Sensor sn = new Sensor();
		sn.setName("GLDAS");
		sn.setURI(new URI("http://nasa.gov/gldas"));

		final NumericValue nv1 = new NumericValue();
		nv1.setTableElement(new Column(3,firstLineWithData));
		nv1.setDateAndTime(dtm);
		nv1.setObservedProperty(op);
		nv1.setFeatureOfInterest(foi);
		nv1.setSensor(sn);
		nv1.setUnitOfMeasurement(uom);

		ms.add(nv1);
		ms.add(foi);

		final MainController f = MainController.getInstance();

		f.setStepController(new Step7Controller());
	}
}
