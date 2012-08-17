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
package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

public class UnitOfMeasurement extends Resource {
	
	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setUnitOfMeasurement(this);	
	}

	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getUnitOfMeasurement() != null;
	}
	
	@Override
	public boolean isAssignedTo(MeasuredValue measuredValue) {
		return measuredValue.getUnitOfMeasurement() == this;
	}
	
	@Override
	public void unassign(MeasuredValue mv) {
		mv.setUnitOfMeasurement(null);		
	}	
	
	@Override
	public UnitOfMeasurement forThis(Cell measuredValuePosition) {
		if (getTableElement() == null) {
			return this;
		} else {
			UnitOfMeasurement uom = new UnitOfMeasurement();
			String name = getTableElement().getValueFor(measuredValuePosition);
			uom.setName(name);
			return uom;
		}	
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getUnitOfMeasurementCodes();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getUnitOfMeasurementURIs();
	}
	
	@Override
	public List<Resource> getList() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.addAll(ModelStore.getInstance().getUnitOfMeasurements());
		return resources;
	}
	
	@Override
	public Resource getNextResourceType() {
		return new Sensor();
	}
	
	@Override
	public String toString() {
		return "Unit Of Measurement" + super.toString();
	}

	@Override
	public String getTypeName() {
		return Lang.l().unitOfMeasurement();
	}

	@Override
	public String XML_PREFIX() {
		return "uom";
	}
}
