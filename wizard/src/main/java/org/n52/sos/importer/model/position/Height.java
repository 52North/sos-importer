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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingHeightPanel;
public class Height extends PositionComponent {

	/**
	 * <p>Constructor for Height.</p>
	 *
	 * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
	 * @param pattern a {@link java.lang.String} object.
	 */
	public Height(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}

	/**
	 * <p>Constructor for Height.</p>
	 *
	 * @param value a double.
	 * @param unit a {@link java.lang.String} object.
	 */
	public Height(double value, String unit) {
		super(value, unit);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Height" + super.toString();
	}

	/** {@inheritDoc} */
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingHeightPanel((Position)c);
	}

	/**
	 * tries to convert a given String into a valid Height object
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link org.n52.sos.importer.model.position.Height} object.
	 */
	// TODO units and Strings -> Constants
	public static Height parse(String s) {
		double value = 0;
		String unit = "m";

		String number;
		if (s.contains("km")) {
			unit = "km";
			number = s.replace("km", "");
		} else if (s.contains("mi")) {
			unit = "mi";
			number = s.replace("mi", "");
		} else if (s.contains("m")) {
			unit = "m";
			number = s.replace("m", "");
		} else if (s.contains("ft")) {
			unit = "ft";
			number = s.replace("ft", "");
		} else
			number = s;

		NumericValue nv = new NumericValue();

		value = nv.parse(number);

		return new Height(value, unit);
	}

	/** {@inheritDoc} */
	@Override
	public Height forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Height(getValue(), getParsedUnit());
		else {
			String heightString = getTableElement().getValueFor(featureOfInterestPosition);
			return Height.parse(heightString);
		}
	}
}
