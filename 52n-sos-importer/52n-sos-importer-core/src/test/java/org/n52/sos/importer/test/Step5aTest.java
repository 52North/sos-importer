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
import org.n52.sos.importer.controller.Step5aController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.table.Column;

public class Step5aTest {

	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"bla", "bla2"},{"bla3", "bla4"},{"bla5", "bla6"}};
		TableController.getInstance().setContent(o);
		int firstLineWithData = 0;
		
		DateAndTime dtm1 = new DateAndTime();
		DateAndTimeController dtc = new DateAndTimeController(dtm1);
		dtc.assignPattern("HH-mm-ss", new Column(0,firstLineWithData));
		ModelStore.getInstance().add(dtm1);
		
		DateAndTime dtm2 = new DateAndTime();
		dtc = new DateAndTimeController(dtm2);
		dtc.assignPattern("dd-MM-yyyy", new Column(1,firstLineWithData));
		ModelStore.getInstance().add(dtm2);
		
		Step5aController controller = new Step5aController(firstLineWithData);
		controller.isNecessary();
		f.setStepController(controller);
	}
}
