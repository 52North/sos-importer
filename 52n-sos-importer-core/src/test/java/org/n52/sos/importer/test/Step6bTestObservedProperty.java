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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;

public class Step6bTestObservedProperty {

	public static void main(String[] args) {		
		MainController f = MainController.getInstance();
		Object[][] o = {
				{"Datum", "Station", "Komp", "Wert/myg/m3"},
				{"01.01.03", "Klinthal", "PM10", "-999"}, 
				{"02.01.03", "Klinthal", "PM10", "-999"}, 
				{"03.01.03", "Zwickau", "PM10", "-999"}, 
				{"04.01.03", "Zwickau", "PM12", "-999"}};
		TableController.getInstance().setContent(o);
		Constants.GUI_DEBUG = false;
		
		DateAndTime dtm1 = new DateAndTime();
		dtm1.setGroup("1");
		DateAndTimeController dtc1 = new DateAndTimeController(dtm1);
		int firstLineWithData = 0;
		dtc1.assignPattern("dd.MM.yy", new Column(0,firstLineWithData ));
		dtm1.setHour(new Hour(0));
		dtm1.setMinute(new Minute(0));
		dtm1.setSecond(new Second(0));
		dtm1.setTimeZone(new TimeZone(1));
		ModelStore.getInstance().add(dtm1);
		
		FeatureOfInterest foi = new FeatureOfInterest();
		foi.setTableElement(new Column(1,firstLineWithData));
		ModelStore.getInstance().add(foi);
		
		//ObservedProperty op = new ObservedProperty();
		//op.setTableElement(new Column(2));
		//ModelStore.getInstance().add(op);
		
		NumericValue nv = new NumericValue();
		nv.setTableElement(new Column(3,firstLineWithData));
		nv.setDateAndTime(dtm1);
		nv.setFeatureOfInterest(foi);
		//nv.setObservedProperty(op);
		ModelStore.getInstance().add(nv);

		Step6bController s6c = new Step6bController(firstLineWithData);
		s6c.isNecessary(); 
		f.setStepController(s6c);
	}
}
