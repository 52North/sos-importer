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
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

public class Step3aModel implements StepModel{
	
	private final int markedColumn;
	
	private List<String> selection;
	
	private final int firstLineWithData;
	
	public Step3aModel(int markedColumn,int firstLineWithData) {
		this.markedColumn = markedColumn;
		this.firstLineWithData = firstLineWithData;
		selection = new ArrayList<String>();
		selection.add("Undefined");
	}

	public int getMarkedColumn() { return markedColumn; }

	/**
	 * saves the current selection of the radio button panel
	 */
	public void setSelection(List<String> selection) {
		this.selection = selection;
	}

	/**
	 * returns the saved selection of the radio button panel
	 */
	public List<String> getSelection() { return selection; }

	/**
	 * @return the firstLineWithData
	 */
	public int getFirstLineWithData() {	return firstLineWithData; }
	
}
