package org.n52.sos.importer.bean;

public class FeatureOfInterest extends Resource {
	
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setFeatureOfInterest(this);
	}
	
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getFeatureOfInterest() != null;
	}
	
	public String toString() {
		return "Feature Of Interest";
	}

	@Override
	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getFeatureOfInterest() == this)
				mv.setFeatureOfInterest(null);
		}		
	}
}
