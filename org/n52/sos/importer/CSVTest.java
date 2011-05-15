package org.n52.sos.importer;

import javax.swing.JFrame;


public class CSVTest {

	public static void main(String[] args) {
		/*
		JFrame bla= new JFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.getContentPane().add(new Step4cPanel(null, 0, o));
		bla.setVisible(true);
		bla.pack();
		*/
		
		MainFrame bla= new MainFrame();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		bla.setStepPanel(new Step3Panel(bla, o));
	}
}
