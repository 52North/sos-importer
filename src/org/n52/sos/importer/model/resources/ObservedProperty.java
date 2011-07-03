package org.n52.sos.importer.model.resources;

import org.n52.sos.importer.model.ModelStore;
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
	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getObservedProperty() == this)
				mv.setObservedProperty(null);
		}		
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
}
