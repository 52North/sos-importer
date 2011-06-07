package org.n52.sos.importer.test;

import javax.swing.JFrame;

import org.n52.sos.importer.controller.Step5bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;

public class Step5bTest {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		
		DateAndTimeController dtc = new DateAndTimeController();
		dtc.assignPattern("yyyy-MM-dd");
		
		f.add(new Step5bController(dtc).getStepPanel());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
