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

import java.util.Locale;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step4bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author e.h.juerrens@52north.org
 *
 */
public class Step4bTest {
	
	private static final Logger logger = Logger.getLogger(Step4bTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (logger.isTraceEnabled()) {
			logger.trace("main()");
		}
		MainController f = MainController.getInstance();
		Lang.setCurrentLocale(Locale.GERMAN);
		int firstLineWithData = 1, i = 0;
		FeatureOfInterest foi = TestData.EXAMPLE_FOI;
		Object[][] o = TestData.EXAMPLE_TABLE;
		TableController tc = TableController.getInstance();
		tc.setContent(o); 
		tc.setColumnHeading(i, Lang.l().step3ColTypeDateTime());
		tc.setColumnHeading(++i, Lang.l().sensor());
		tc.setColumnHeading(++i, Lang.l().observedProperty());
		tc.setColumnHeading(++i, Lang.l().featureOfInterest());
		tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
		tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
		tc.setColumnHeading(++i, Lang.l().featureOfInterest());
		tc.setColumnHeading(++i, Lang.l().unitOfMeasurement());
		tc.setColumnHeading(++i, Lang.l().step3ColTypeMeasuredValue());
		
		ModelStore ms = ModelStore.getInstance();
		NumericValue nV1 = new NumericValue(), nV2 = new NumericValue();
		nV1.setTableElement(new Column(5, firstLineWithData));
		nV2.setTableElement(new Column(8, firstLineWithData));
		
		ms.add(nV1);
		ms.add(nV2);
		
		f.setStepController(
				new Step4bController(
						new Step4bModel(foi,firstLineWithData),firstLineWithData));
	}
}
