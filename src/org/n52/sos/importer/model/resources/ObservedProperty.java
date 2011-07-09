package org.n52.sos.importer.model.resources;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;

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
	public void unassign(MeasuredValue mv) {
		mv.setObservedProperty(null);		
	}
	
	@Override 
	public String toString() {
		return "Observed Property";
	}
	
	public ObservedProperty forThis(Cell measuredValuePosition) {
		ObservedProperty op = new ObservedProperty();
		if (getTableElement() == null) {
			op.setName(getName());
			op.setURI(getURI());
		} else {
			String name = getTableElement().getValueFor(measuredValuePosition);
			op.setName(name);
		}
		return op;
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getObservedPropertyNames();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getObservedPropertyURIs();
	}
}
