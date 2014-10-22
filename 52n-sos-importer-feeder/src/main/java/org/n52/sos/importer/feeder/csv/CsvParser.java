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
package org.n52.sos.importer.feeder.csv;

import java.io.BufferedReader;
import java.io.IOException;

import org.n52.sos.importer.feeder.Configuration;

/**
 * Interface to allow different CSVParser implementations
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public interface CsvParser {

	/**
     * Reads the next line and converts to a string array.
     *
     * @return a string array with each comma-separated element as a separate
     *         entry.
     *
     * @throws IOException
     *             if errors happen during the read
     */
    String[] readNext() throws IOException;

	/**
	 * MUST be called before first call of {@link #readNext()}!
	 *
	 * @param bufferedReader
	 * @param configuration
	 * @throws IOException
	 */
	void init(BufferedReader bufferedReader, Configuration configuration) throws IOException;

	/**
	 * Should return 0, if number of lines == number of observations,<br>
	 * 				else the difference between line number and line index.
	 *  
	 * @return 0, if number of lines == number of observations,<br>
	 * 				else the difference between line number and line index.
	 */
	int getSkipLimit();

}
