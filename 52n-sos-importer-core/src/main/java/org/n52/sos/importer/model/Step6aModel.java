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

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6aModel implements StepModel {
	
	private final String description = Lang.l().step6aModelDescription();
	
	private DateAndTime dateAndTime;

	private List<Component> missingDateAndTimeComponents;
	
	public Step6aModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
		missingDateAndTimeComponents = new ArrayList<Component>();
	}
	
	public void setDateAndTime(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * saves the components which are missing for this step
	 * (= all date&time components)
	 */
	public void setMissingDateAndTimeComponents(
			List<Component> missingDateAndTimeComponents) {
		this.missingDateAndTimeComponents = missingDateAndTimeComponents;
	}

	/**
	 * returns the components which were missing for this step 
	 * (= all date&time components)
	 */
	public List<Component> getMissingDateAndTimeComponents() {
		return missingDateAndTimeComponents;
	}
}
