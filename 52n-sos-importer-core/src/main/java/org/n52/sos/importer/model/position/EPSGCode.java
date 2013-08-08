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
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EPSGCode extends Component {

	private static final Logger logger = LoggerFactory.getLogger(EPSGCode.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private int value = -1;

	public EPSGCode(final TableElement tableElement, final String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public EPSGCode(final int value) {
		this.value = value;
	}
	
	public void setValue(final int value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setTableElement(final TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	/**
	 * returns the corresponding position component for a feature of interest cell
	 */
	public EPSGCode forThis(final Cell featureOfInterestPosition) {
		if (tableElement == null) {
			return new EPSGCode(value);
		} else {
			final String epsgString = tableElement.getValueFor(featureOfInterestPosition);
			return EPSGCode.parse(epsgString);
		}
	}
	
	/**
	 * colors this particular component
	 */
	public void mark() {
		if (tableElement != null) {
			tableElement.mark();
		}
	}
	
	@Override 
	public String toString() {
		if (getTableElement() == null) {
			return "EPSG-Code "  + getValue();
		} else {
			return "EPSG-Code " + getTableElement();
		}
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	@Override
	public MissingComponentPanel getMissingComponentPanel(final Combination c) {
		return new MissingEPSGCodePanel((Position)c);
	}
	
	/**
	 * converts a given String into an EPSG code object
	 */
	public static EPSGCode parse(final String s) {
		final int value = Integer.valueOf(s);
		return new EPSGCode(value);
	}
	
}
