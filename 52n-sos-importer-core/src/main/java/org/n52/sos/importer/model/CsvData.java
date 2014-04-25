/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class holds the data of an CSV file incl. work arounds for bad
 * formatted files:
 * <ul><li>having column count mismatch (e.g. sample based files)</li></ul>
 *
 * @since 0.4.0
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class CsvData {

	private List<String[]> lines = new LinkedList<>();
	private int columns = 0;

	public void setLines(final List<String[]> lines) {
		this.lines = lines;
		columns = 0;
		if (lines != null) {
			for (final String[] strings : lines) {
				if (columns < strings.length) {
					columns = strings.length;
				}
			}
		}
	}

	public int getRowCount() {
		if (lines == null) {
			return 0;
		}
		return lines.size();
	}

	public int getColumnCount() {
		return columns;
	}

	public String[] getLine(final int i) {
		final String[] extended = new String[columns];
		if (lines == null) {
			Arrays.fill(extended, "");
			return extended;
		}
		String[] tmp = lines.get(i);
		if (tmp.length < columns) {
			Arrays.fill(extended, "");
			for (int j = 0; j < tmp.length; j++) {
				extended[j] = tmp[j];
			}
			tmp = extended;
		}
		return tmp;
	}

}
