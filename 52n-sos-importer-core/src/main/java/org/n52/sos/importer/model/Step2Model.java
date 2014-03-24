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

import org.n52.sos.importer.view.combobox.EditableComboBoxItems;

public class Step2Model implements StepModel {

	private String csvFileContent;

	private String columnSeparator;

	private String commentIndicator;

	private String textQualifier;

	private char decimalSeparator;

	private int firstLineWithData;

	private int csvFileRowRount;

	private boolean useHeader;

	public Step2Model(final String csvFileContent, final int csvFileRowCount) {
		this.csvFileContent = csvFileContent;

		final EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		columnSeparator = (String) items.getColumnSeparators().getElementAt(0);
		commentIndicator = (String) items.getCommentIndicators().getElementAt(0);
		textQualifier = (String) items.getTextQualifiers().getElementAt(0);
		firstLineWithData = 0;
		csvFileRowRount = csvFileRowCount;
	}

	public int getCsvFileRowRount() {
		return csvFileRowRount;
	}

	public void setCsvFileRowRount(final int csvFileRowRount) {
		this.csvFileRowRount = csvFileRowRount;
	}

	public String getColumnSeparator() {
		return columnSeparator;
	}

	public void setColumnSeparator(final String selectedColumnSeparator) {
		columnSeparator = selectedColumnSeparator;
	}

	public String getCommentIndicator() {
		return commentIndicator;
	}

	public void setCommentIndicator(final String selectedCommentIndicator) {
		commentIndicator = selectedCommentIndicator;
	}

	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	public void setFirstLineWithData(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
	}

	public String getTextQualifier() {
		return textQualifier;
	}

	public void setTextQualifier(final String selectedTextQualifier) {
		textQualifier = selectedTextQualifier;
	}

	public String getCSVFileContent() {
		return csvFileContent;
	}

	public void setCSVFileContent(final String cSVFileContent) {
		csvFileContent = cSVFileContent;
	}

	public boolean getUseHeader() {
		return useHeader;
	}

	public void setUseHeader(final boolean useHeader) {
		this.useHeader = useHeader;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(final char decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
}