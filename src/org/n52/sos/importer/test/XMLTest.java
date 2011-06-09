package org.n52.sos.importer.test;

import javax.swing.JFrame;

import org.n52.sos.importer.bean.FeatureOfInterest;
import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.bean.ModelStore;
import org.n52.sos.importer.controller.Step6aController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.table.ColumnModel;


public class XMLTest {

	public static void main(String[] args) {

		JFrame f = new JFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		MeasuredValue mv = new MeasuredValue("tes");
		mv.setTableElement(new ColumnModel(2));
		ModelStore.getInstance().addMeasuredValue(mv);
		Step6aModel step6aModel = new Step6aModel(new FeatureOfInterest());
		f.add(new Step6aController(step6aModel).getStepPanel());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);

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
	}
}
