package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.model.ModelStore;
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
	public boolean isAssignedTo(MeasuredValue measuredValue) {
		return measuredValue.getUnitOfMeasurement() == this;
	}
	
	@Override
	public void unassign(MeasuredValue mv) {
		mv.setUnitOfMeasurement(null);		
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
}
