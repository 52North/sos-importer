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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step3Controller;
import org.n52.sos.importer.controller.TableController;

public class Step3TestManyManyColumns {

	public static void main(final String[] args) {
		final MainController f = MainController.getInstance();
		final Object[][] o = TestData.EXAMPLE_TABLE_MANY_MANY_COLUMNS;
		Constants.DECIMAL_SEPARATOR = '.';
		Constants.THOUSANDS_SEPARATOR = ',';
		Constants.GUI_DEBUG = false;
		final TableController tc = TableController.getInstance();
		tc.setContent(o); 
		final int markedColumn = 0;
		final int firstLineWithData = 1;
		final boolean useHeader = false;
		final Step3Controller s3C = new Step3Controller(markedColumn,
				firstLineWithData,
				useHeader);
		f.setStepController(s3C);
	}
}
