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
