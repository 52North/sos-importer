package org.n52.sos.importer;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;


public class XMLTest {

	public static void main(String[] args) {
		
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(',');
		unusualSymbols.setGroupingSeparator('.');
		
		try {
			DecimalFormat weirdFormatter = new DecimalFormat();
			weirdFormatter.setDecimalFormatSymbols(unusualSymbols);
			String n = weirdFormatter.format(123456.78);
			System.out.println(n);
			Number nu = weirdFormatter.parse("1,234567.89");
			System.out.println(nu.doubleValue());
        } catch (IllegalArgumentException iae) {
        	iae.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		MainFrame bla= new MainFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getTablePanel().setContent(o);
		bla.setStepPanel(new Step3Panel(bla));

		/*
		InsertObservationDocument i = InsertObservationDocument.Factory.newInstance();
		InsertObservation io = i.addNewInsertObservation();
		ObservationType ot = io.addNewObservation();
		StringOrRefType s = ot.addNewDescription();
		s.setStringValue("TEST");
		LocationPropertyType l = ot.addNewLocation();
		*/
		/*
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

		*/
		/*
		MainFrame bla= new MainFrame(); 
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getTablePanel().setTableContent(o);
		Step3Panel sp  = new Step3Panel(bla);

		bla.setStepPanel(sp);
		*/
	}
}
