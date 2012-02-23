package org.n52.sos.importer.model;

import org.n52.sos.importer.model.resources.Sensor;

public class Step6bSpecialModel {
	
	private final String description = "What is the sensor for";
	
	private final String featureOfInterestName;
	
	private final String observedPropertyName;
	
	private Sensor sensor;

	public Step6bSpecialModel(String featureOfInterestName, String observedPropertyName) {
		this.featureOfInterestName = featureOfInterestName;
		this.observedPropertyName = observedPropertyName;
		sensor = new Sensor();
	}

	public String getFeatureOfInterestName() {
		return featureOfInterestName;
	}

	public String getObservedPropertyName() {
		return observedPropertyName;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((featureOfInterestName == null) ? 0 : featureOfInterestName
						.hashCode());
		result = prime
				* result
				+ ((observedPropertyName == null) ? 0 : observedPropertyName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step6bSpecialModel other = (Step6bSpecialModel) obj;
		if (featureOfInterestName == null) {
			if (other.featureOfInterestName != null)
				return false;
		} else if (!featureOfInterestName.equals(other.featureOfInterestName))
			return false;
		if (observedPropertyName == null) {
			if (other.observedPropertyName != null)
				return false;
		} else if (!observedPropertyName.equals(other.observedPropertyName))
			return false;
		return true;
	}

}
