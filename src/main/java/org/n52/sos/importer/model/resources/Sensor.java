package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;

public class Sensor extends Resource {

	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setSensor(this);	
	}

	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getSensor() != null;
	}
	
	@Override
	public boolean isAssignedTo(MeasuredValue measuredValue) {
		return measuredValue.getSensor() == this;
	}
	
	@Override
	public void unassign(MeasuredValue mv) {
		mv.setSensor(null);		
	}
	
	@Override
	public Sensor forThis(Cell measuredValuePosition) {
		Sensor s = new Sensor();
		if (getTableElement() == null) {
			s.setName(getName());
			s.setURI(getURI());
		} else {
			String name = getTableElement().getValueFor(measuredValuePosition);
			s.setName(name);
		}
		return s;
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getSensorNames();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getSensorURIs();
	}
	
	@Override
	public List<Resource> getList() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.addAll(ModelStore.getInstance().getSensors());
		return resources;
	}
	
	@Override
	public Resource getNextResourceType() {
		return null;
	}
	
	
	@Override
	public String toString() {
		return "Sensor" + super.toString();
	}
}
