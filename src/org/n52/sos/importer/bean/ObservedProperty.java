package org.n52.sos.importer.bean;

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
		for (MeasuredValue mv: Store.getInstance().getMeasuredValues()) {
			if (mv.getObservedProperty() == this)
				mv.setObservedProperty(null);
		}		
	}
	
	@Override 
	public String toString() {
		return "Observed Property";
	}
}
