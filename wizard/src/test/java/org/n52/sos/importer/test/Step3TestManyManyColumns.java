/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.test;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step3Controller;
import org.n52.sos.importer.controller.TableController;

public class Step3TestManyManyColumns {

	public static void main(final String[] args) {
		final MainController f = MainController.getInstance();
		Constants.DECIMAL_SEPARATOR = '.';
		Constants.THOUSANDS_SEPARATOR = ',';
		Constants.GUI_DEBUG = false;
		final TableController tc = TableController.getInstance();
		tc.setContent(TestData.EXAMPLE_TABLE_MANY_MANY_COLUMNS);
		final int markedColumn = 0;
		final int firstLineWithData = 1;
		final boolean useHeader = false;
		final Step3Controller s3C = new Step3Controller(markedColumn,
				firstLineWithData,
				useHeader);
		f.setStepController(s3C);
	}
}
