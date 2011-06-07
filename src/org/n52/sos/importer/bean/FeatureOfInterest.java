package org.n52.sos.importer.bean;

import java.util.List;

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
		for (MeasuredValue mv: Store.getInstance().getMeasuredValues()) {
			if (mv.getFeatureOfInterest() == this)
				mv.setFeatureOfInterest(null);
		}		
	}
}
