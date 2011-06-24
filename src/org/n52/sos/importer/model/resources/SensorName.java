package org.n52.sos.importer.model.resources;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;

public class SensorName extends Resource {

	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setSensorName(this);	
	}

	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getSensorName() != null;
	}
	
	@Override
	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getSensorName() == this)
				mv.setSensorName(null);
		}		
	}
	
	@Override 
	public String toString() {
		return "Sensor Name";
	}
}
