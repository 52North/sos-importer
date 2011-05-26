package org.n52.sos.importer;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import net.opengis.gml.LocationPropertyType;
import net.opengis.gml.StringOrRefType;
import net.opengis.om.x10.ObservationType;
import net.opengis.sos.x10.InsertObservationDocument;
import net.opengis.sos.x10.InsertObservationDocument.InsertObservation;
import net.opengis.sos.x10.RegisterSensorDocument;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor.SensorDescription;


public class XMLTest {

	public static void main(String[] args) {
		/*
		MainFrame bla= new MainFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getTablePanel().setContent(o);
		bla.setStepPanel(new Step5cPanel(bla));
*/
		
		/*
		InsertObservationDocument i = InsertObservationDocument.Factory.newInstance();
		InsertObservation io = i.addNewInsertObservation();
		ObservationType ot = io.addNewObservation();
		StringOrRefType s = ot.addNewDescription();
		s.setStringValue("TEST");
		LocationPropertyType l = ot.addNewLocation();
		*/
		
		RegisterSensorDocument r;
		try {
			r = RegisterSensorDocument.Factory.parse(new File("D://RegisterSensor_measurement.xml"));
			RegisterSensor rs = r.getRegisterSensor();	
			rs.setOffering("Test") ;
			r.save(new File("D://test.xml"));
		} catch (XmlException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		/*
		MainFrame bla= new MainFrame(); 
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getTablePanel().setTableContent(o);
		Step3Panel sp  = new Step3Panel(bla);

		bla.setStepPanel(sp);
		*/
	}
}
