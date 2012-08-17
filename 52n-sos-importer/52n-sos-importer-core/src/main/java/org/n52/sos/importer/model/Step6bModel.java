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

import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bModel implements StepModel {
	
	private MeasuredValue measuredValue;
	
	private Resource resource;

	public Step6bModel(MeasuredValue measuredValue, Resource resource) {
		setMeasuredValue(measuredValue);
		setResource(resource);
	}
	
	public void setMeasuredValue(MeasuredValue measuredValue) {
		this.measuredValue = measuredValue;
	}

	/**
	 * @return the related measure value object
	 */
	public MeasuredValue getMeasuredValue() {
		return measuredValue;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	/**
	 * ... RESOURCE ... ORIENTATION
	 * @return
	 */
	public String getDescription() {
		return Lang.l().step6bModelDescription();
	}
}
