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
package org.n52.sos.importer.feeder.csv;

import java.io.BufferedReader;
import java.io.IOException;

import org.n52.sos.importer.feeder.Configuration;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class WrappedCSVReader implements CsvParser {

	private CSVReader csvReader;

	@Override
	public String[] readNext() throws IOException {
		return csvReader.readNext();
	}

	@Override
	public void init(final BufferedReader bufferedReader,
			final Configuration configuration) {
		final int flwd = configuration.getFirstLineWithData();
		final char separator = configuration.getCsvSeparator(),
				quotechar = configuration.getCsvQuoteChar(),
				escape = configuration.getCsvEscape();
		csvReader = new CSVReader(bufferedReader, separator, quotechar, escape, flwd);
	}

	@Override
	public int getSkipLimit() {
		return 1;
	}

}
