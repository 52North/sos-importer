package org.n52.sos.importer.model.resources;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;

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
	public void unassign(MeasuredValue mv) {
		mv.setUnitOfMeasurement(null);		
	}
	
	@Override 
	public String toString() {
		return "Unit Of Measurement";
	}
	
	
	public UnitOfMeasurement forThis(Cell measuredValuePosition) {
		UnitOfMeasurement uom = new UnitOfMeasurement();
		if (getTableElement() == null) {
			uom.setName(getName());
			uom.setURI(getURI());
		} else {
			String name = getTableElement().getValueFor(measuredValuePosition);
			uom.setName(name);
		}
		return uom;
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getUnitOfMeasurementCodes();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getUnitOfMeasurementURIs();
	}
}
