package org.n52.sos.importer;

import javax.swing.JFrame;

import org.n52.sos.importer.bean.FeatureOfInterest;
import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.Store;
import org.n52.sos.importer.controller.Step4aController;
import org.n52.sos.importer.controller.Step6aController;
import org.n52.sos.importer.controller.TableController;


public class XMLTest {

	public static void main(String[] args) {

		JFrame f = new JFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		MeasuredValue mv = new MeasuredValue("tes");
		mv.setColumnNumber(2);
		Store.getInstance().addMeasuredValue(mv);
		f.add(new Step6aController(new FeatureOfInterest()).getView());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		
		/*
		MainFrame bla= new MainFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getTablePanel().setContent(o);
		bla.setStepPanel(new Step3Panel(bla));
*/
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
