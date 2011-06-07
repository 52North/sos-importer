package org.n52.sos.importer.bean;

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
		for (MeasuredValue mv: Store.getInstance().getMeasuredValues()) {
			if (mv.getUnitOfMeasurement() == this)
				mv.setUnitOfMeasurement(null);
		}		
	}
	
	@Override 
	public String toString() {
		return "Unit Of Measurement";
	}
}
