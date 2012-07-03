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

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;

public class EPSGCode extends Component {

	private static final Logger logger = Logger.getLogger(EPSGCode.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private int value = -1;

	public EPSGCode(TableElement tableElement, String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public EPSGCode(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	/**
	 * returns the corresponding position component for a feature of interest cell
	 */
	public EPSGCode forThis(Cell featureOfInterestPosition) {
		if (tableElement == null)
			return new EPSGCode(value);
		else {
			String epsgString = tableElement.getValueFor(featureOfInterestPosition);
			return EPSGCode.parse(epsgString);
		}
	}
	
	/**
	 * colors this particular component
	 */
	public void mark() {
		if (tableElement != null)
			tableElement.mark();
	}
	
	@Override 
	public String toString() {
		if (getTableElement() == null)
			return "EPSG-Code "  + getValue();
		else 
			return "EPSG-Code " + getTableElement();
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingEPSGCodePanel((Position)c);
	}
	
	/**
	 * converts a given String into an EPSG code object
	 */
	public static EPSGCode parse(String s) {
		int value = Integer.valueOf(s);
		return new EPSGCode(value);
	}
	
}
