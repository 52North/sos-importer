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

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.i18n.Lang;

public class Step4aModel implements StepModel {
	
	private final String description = Lang.l().step4aModelDescription();
	
	private int[] selectedRowsOrColumns;
	
	private int firstLineWithData = -1;
	
	private DateAndTime dateAndTimeModel;
	
	public Step4aModel(DateAndTime dateAndTimeModel, int firstLineWithData) {
		this.dateAndTimeModel = dateAndTimeModel;
		this.selectedRowsOrColumns = new int[0];
		this.firstLineWithData = firstLineWithData;
	}

	public void setDateAndTimeModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTime getDateAndTimeModel() {
		return dateAndTimeModel;
	}

	public void setSelectedRowsOrColumns(int[] selectedRowsOrColumns) {
		this.selectedRowsOrColumns = selectedRowsOrColumns;
	}

	public int[] getSelectedRowsOrColumns() {
		return selectedRowsOrColumns;
	}

	public String getDescription() {
		return description;
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
}
