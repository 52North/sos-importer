/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.view.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>n52Utils class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class n52Utils {

	private static final Logger logger = LoggerFactory.getLogger(n52Utils.class);

	/**
	 * <p>parseDouble.</p>
	 *
	 * @deprecated who is using this method?
	 * @param text a {@link java.lang.String} object.
	 * @return a {@link java.lang.Double} object.
	 */
	public static Double parseDouble(final String text){
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());

		Number n;
		final DecimalFormat formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(symbols);
		try {
			n = formatter.parse(text);
			return n.doubleValue();
		} catch (final ParseException e) {
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		}
		return null;
	}

}
