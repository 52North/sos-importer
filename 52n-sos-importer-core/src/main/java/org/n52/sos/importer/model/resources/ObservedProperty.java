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

public class ObservedProperty extends Resource {

	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setObservedProperty(this);	
	}

	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getObservedProperty() != null;
	}
	
	@Override
	public boolean isAssignedTo(MeasuredValue measuredValue) {
		return measuredValue.getObservedProperty() == this;
	}
	
	@Override
	public void unassign(MeasuredValue mv) {
		mv.setObservedProperty(null);		
	}
	
	@Override
	public ObservedProperty forThis(Cell measuredValuePosition) {
		if (getTableElement() == null || isGenerated()) {
			return this;
		} else {
			ObservedProperty op = new ObservedProperty();
			String name = getTableElement().getValueFor(measuredValuePosition);
			op.setName(name);
			// TODO check, if the next line break any logic
			op.setTableElement(getTableElement());
			return op;
		}
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getObservedPropertyNames();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getObservedPropertyURIs();
	}
	
	@Override
	public List<Resource> getList() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.addAll(ModelStore.getInstance().getObservedProperties());
		return resources;
	}
	
	@Override
	public Resource getNextResourceType() {
		return new UnitOfMeasurement();
	}
	
	@Override
	public String toString() {
		return "Observed Property" + super.toString();
	}

	@Override
	public String getTypeName() {
		return Lang.l().observedProperty();
	}

	@Override
	public String XML_PREFIX() {
		return "obsprop";
	}
}
