package org.n52.sos.importer.model.resources;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;

public class UnitOfMeasurement extends Resource {
	
	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setUnitOfMeasurement(this);	
	}

	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getUnitOfMeasurement() != null;
	}
	
	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getUnitOfMeasurement() == this)
				mv.setUnitOfMeasurement(null);
		}		
	}
	
	@Override 
	public String toString() {
		return "Unit Of Measurement";
	}
}
