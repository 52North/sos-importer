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

import org.n52.sos.importer.combobox.EditableComboBoxItems;

public class Step2Model {

	private String csvFileContent;
	
	private String selectedColumnSeparator;
	
	private String selectedCommentIndicator;
	
	private String selectedTextQualifier;
	
	private int firstLineWithData;
	
	public Step2Model(String csvFileContent) {
		this.csvFileContent = csvFileContent;
		
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		selectedColumnSeparator = (String) items.getColumnSeparators().getElementAt(0);
		selectedCommentIndicator = (String) items.getCommentIndicators().getElementAt(0);
		selectedTextQualifier = (String) items.getTextQualifiers().getElementAt(0);
		
		firstLineWithData = 1;
	}

	public String getSelectedColumnSeparator() {
		return selectedColumnSeparator;
	}

	public void setSelectedColumnSeparator(String selectedColumnSeparator) {
		this.selectedColumnSeparator = selectedColumnSeparator;
	}

	public String getSelectedCommentIndicator() {
		return selectedCommentIndicator;
	}

	public void setSelectedCommentIndicator(String selectedCommentIndicator) {
		this.selectedCommentIndicator = selectedCommentIndicator;
	}

	/**
	 * @return the firstLineWithData
	 */
	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	/**
	 * @param firstLineWithData the firstLineWithData to set
	 */
	public void setFirstLineWithData(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
	}

	public String getSelectedTextQualifier() {
		return selectedTextQualifier;
	}

	public void setSelectedTextQualifier(String selectedTextQualifier) {
		this.selectedTextQualifier = selectedTextQualifier;
	}

	public String getCSVFileContent() {
		return csvFileContent;
	}

	public void setCSVFileContent(String cSVFileContent) {
		csvFileContent = cSVFileContent;
	}
}