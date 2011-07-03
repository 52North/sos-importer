package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Second;
import org.n52.sos.importer.model.dateAndTime.TimeZone;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

public class Step7Test {
	
	public static void main(String[] args) {
		Object[][] o = {{"01/06/2010 00:00", "12.12", "23.123"},{"01/06/2010 00:00", "323.123", "432.123"}};
		TableController.getInstance().setContent(o); 
		
		DateAndTime dtm = new DateAndTime();
		DateAndTimeController dtc = new DateAndTimeController(dtm);
		dtc.assignPattern("dd/MM/yyyy HH:mm", new Column(0));
		dtm.setSecond(new Second(0));
		dtm.setTimeZone(new TimeZone(0));

		ObservedProperty op = new ObservedProperty();
		op.setName("Temperature");
		UnitOfMeasurement uom = new UnitOfMeasurement();
		uom.setName("Degree Celsius");
		FeatureOfInterest foi = new FeatureOfInterest();
		foi.setName("Air");
		Sensor sn = new Sensor();
		sn.setName("Thermometer xy");
		
		NumericValue nv1 = new NumericValue();
		nv1.setTableElement(new Column(1));
		nv1.setDateAndTime(dtm);
		nv1.setObservedProperty(op);
		nv1.setFeatureOfInterest(foi);
		nv1.setSensor(sn);
		nv1.setUnitOfMeasurement(uom);
		
		NumericValue nv2 = new NumericValue();
		nv2.setTableElement(new Column(2));
		nv2.setDateAndTime(dtm);
		nv2.setObservedProperty(op);
		nv2.setFeatureOfInterest(foi);
		nv2.setSensor(sn);
		nv2.setUnitOfMeasurement(uom);	
		
		ModelStore.getInstance().addMeasuredValue(nv1);
		ModelStore.getInstance().addMeasuredValue(nv2);
		ModelStore.getInstance().addFeatureOfInterest(foi);
		
		MainController f = MainController.getInstance();

		Step7Model step7Model = new Step7Model(foi);
		f.setStepController(new Step7Controller(step7Model));
	}

}
