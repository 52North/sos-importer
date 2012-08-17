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

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bTestUOM {

	public static void main(String[] args) {
		MainController mC = MainController.getInstance();
		TableController tc = TableController.getInstance();
		ModelStore ms = ModelStore.getInstance(); 
		Object[][] o = TestData.EXAMPLE_TABLE_NO_UOM;
		int firstLineWithData = 1, i = 0;
		Step6bModel s6bM;
		MeasuredValue mv;
		Column markedColumn;
		Constants.GUI_DEBUG = false;
		//
		markedColumn = new Column(4,firstLineWithData );
		tc.setContent(o);
		tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
		tc.setColumnHeading(++i, Lang.l().sensor());
		tc.setColumnHeading(++i, Lang.l().observedProperty());
		tc.setColumnHeading(++i, Lang.l().featureOfInterest());
		tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
		tc.mark(markedColumn);
		mv = new NumericValue();
		mv.setTableElement(markedColumn);
		mv.setFeatureOfInterest(TestData.EXAMPLE_FOI);
		ms.add(mv);
		ms.add(TestData.EXAMPLE_FOI);
		s6bM = new Step6bModel(mv, new UnitOfMeasurement());
		/*
		 * Set-Up Column metadata 
		 */
		Step3Model s3M = new Step3Model(4, firstLineWithData, false);
		List<String> selection = new ArrayList<String>(1);
		selection.add(Lang.l().step3ColTypeMeasuredValue());
		selection.add(Lang.l().step3MeasuredValNumericValue());
		selection.add(".SEP,");
		s3M.addSelection(selection);
		mC.registerProvider(s3M);
		mC.updateModel();
		mC.removeProvider(s3M);
		// add observed property to Modelstore
		ObservedProperty op = new ObservedProperty();
		op.setTableElement(new Column(2,firstLineWithData));
		ms.add(op);
		//
		mC.setStepController(new Step6bController(s6bM,firstLineWithData));
	}
}
