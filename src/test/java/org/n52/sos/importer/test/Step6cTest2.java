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

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6cController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

public class Step6cTest2 {
	
	public static void main(String[] args) {
		Object[][] o = {{"31.12.07", "Klinthal", "PM10", "11.25"},
				{"01.01.03", "PlauSued", "PM10", "19.91"}};
		TableController.getInstance().setContent(o); 
		
		DateAndTime dtm = new DateAndTime();
		DateAndTimeController dtc = new DateAndTimeController(dtm);
		dtc.assignPattern("dd.MM.yy", new Column(0));
		dtm.setHour(new Hour(0));
		dtm.setMinute(new Minute(0));
		dtm.setSecond(new Second(0));
		dtm.setTimeZone(new TimeZone(1));
		
		FeatureOfInterest foi = new FeatureOfInterest();
		foi.setTableElement(new Column(1));
		
		ObservedProperty op = new ObservedProperty();
		op.setTableElement(new Column(2));

		UnitOfMeasurement uom = new UnitOfMeasurement();
		uom.setName("myg/m3");
		Sensor sn = new Sensor();
		sn.setName("PM10Sensor");
		
		NumericValue nv1 = new NumericValue();
		nv1.setTableElement(new Column(3));
		nv1.setDecimalSeparator(".");
		nv1.setThousandsSeparator(",");
		nv1.setDateAndTime(dtm);
		nv1.setFeatureOfInterest(foi);
		nv1.setObservedProperty(op);
		nv1.setSensor(sn);
		nv1.setUnitOfMeasurement(uom);	
		
		ModelStore.getInstance().add(nv1);
		ModelStore.getInstance().add(foi);
		
		MainController f = MainController.getInstance();

		Step6cController s6c = new Step6cController();
		s6c.isNecessary();
		f.setStepController(s6c);
	}

}
