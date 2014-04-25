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

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step2Controller;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Step2Model;

public class Step2Test {

	public static void main(final String[] args) {
		final MainController f = MainController.getInstance();
		final TableController tc = TableController.getInstance();
		tc.setContent(TestData.EXAMPLE_TABLE);
		final Step2Model s2M = new Step2Model("line 1\nline2", 2);
		final Step2Controller s2C = new Step2Controller(
				s2M);
		//
		f.setStepController(s2C);
	}
}
