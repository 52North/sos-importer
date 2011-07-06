package org.n52.sos.importer.model.requests;

public class RegisterSensor {

	private String sensorName;
	
	private String sensorURI;
	
	private String observedPropertyName;
	
	private String observedPropertyURI;

	private String unitOfMeasurementCode;
	
	private String epsgCode;
	
	private String latitudeValue;
	
	private String latitudeUnit;
	
	private String longitudeValue;
	
	private String longitudeUnit;
	
	private String heightValue;
	
	private String heightUnit;
	
	public String fillTemplate(String template) {
		String filledTemplate = template;
		filledTemplate = filledTemplate.replaceAll("THISsensorName", sensorName);
		filledTemplate = filledTemplate.replaceAll("THISsensorURI", sensorURI);
		filledTemplate = filledTemplate.replaceAll("THISobservedPropertyName", observedPropertyName);
		filledTemplate = filledTemplate.replaceAll("THISobservedPropertyURI", observedPropertyURI);
		filledTemplate = filledTemplate.replaceAll("THISunitOfMeasurementCode", unitOfMeasurementCode);
		filledTemplate = filledTemplate.replaceAll("THISlatitudeValue", latitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISlatitudeUnit", latitudeUnit);
		filledTemplate = filledTemplate.replaceAll("THISlongitudeValue", longitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISlongitudeUnit", longitudeUnit);
		filledTemplate = filledTemplate.replaceAll("THISheightValue", longitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISheightUnit", heightUnit);
		filledTemplate = filledTemplate.replaceAll("THISepsgCode", epsgCode);
		return filledTemplate; 
	}
	
	
	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getSensorURI() {
		return sensorURI;
	}

	public void setSensorURI(String sensorURI) {
		this.sensorURI = sensorURI;
	}

	public String getObservedPropertyName() {
		return observedPropertyName;
	}

	public void setObservedPropertyName(String observedPropertyName) {
		this.observedPropertyName = observedPropertyName;
	}

	public String getObservedPropertyURI() {
		return observedPropertyURI;
	}

	public void setObservedPropertyURI(String observedPropertyURI) {
		this.observedPropertyURI = observedPropertyURI;
	}

	public String getUnitOfMeasurementCode() {
		return unitOfMeasurementCode;
	}

	public void setUnitOfMeasurementCode(String unitOfMeasurementCode) {
		this.unitOfMeasurementCode = unitOfMeasurementCode;
	}

	public String getEpsgCode() {
		return epsgCode;
	}

	public void setEpsgCode(String epsgCode) {
		this.epsgCode = epsgCode;
	}

	public String getLatitudeValue() {
		return latitudeValue;
	}

	public void setLatitudeValue(String latitudeValue) {
		this.latitudeValue = latitudeValue;
	}

	public String getLatitudeUnit() {
		return latitudeUnit;
	}

	public void setLatitudeUnit(String latitudeUnit) {
		this.latitudeUnit = latitudeUnit;
	}

	public String getLongitudeValue() {
		return longitudeValue;
	}

	public void setLongitudeValue(String longitudeValue) {
		this.longitudeValue = longitudeValue;
	}

	public String getLongitudeUnit() {
		return longitudeUnit;
	}

	public void setLongitudeUnit(String longitudeUnit) {
		this.longitudeUnit = longitudeUnit;
	}

	public String getHeightValue() {
		return heightValue;
	}

	public void setHeightValue(String heightValue) {
		this.heightValue = heightValue;
	}

	public String getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	@Override
	public String toString() {
		return "RegisterSensor [sensorName=" + sensorName + ", sensorURI="
				+ sensorURI + ", observedPropertyName=" + observedPropertyName
				+ ", observedPropertyURI=" + observedPropertyURI
				+ ", unitOfMeasurementCode=" + unitOfMeasurementCode
				+ ", epsgCode=" + epsgCode + ", latitudeValue=" + latitudeValue
				+ ", latitudeUnit=" + latitudeUnit + ", longitudeValue=" + longitudeValue
				+ ", longitudeUnit=" + longitudeUnit + ", heightValue=" + heightValue
				+ ", heightUnit=" + heightUnit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((epsgCode == null) ? 0 : epsgCode.hashCode());
		result = prime * result + ((heightValue == null) ? 0 : heightValue.hashCode());
		result = prime * result
				+ ((heightUnit == null) ? 0 : heightUnit.hashCode());
		result = prime * result
				+ ((latitudeValue == null) ? 0 : latitudeValue.hashCode());
		result = prime * result
				+ ((latitudeUnit == null) ? 0 : latitudeUnit.hashCode());
		result = prime * result
				+ ((longitudeValue == null) ? 0 : longitudeValue.hashCode());
		result = prime * result
				+ ((longitudeUnit == null) ? 0 : longitudeUnit.hashCode());
		result = prime
				* result
				+ ((observedPropertyName == null) ? 0 : observedPropertyName
						.hashCode());
		result = prime
				* result
				+ ((observedPropertyURI == null) ? 0 : observedPropertyURI
						.hashCode());
		result = prime * result
				+ ((sensorName == null) ? 0 : sensorName.hashCode());
		result = prime * result
				+ ((sensorURI == null) ? 0 : sensorURI.hashCode());
		result = prime
				* result
				+ ((unitOfMeasurementCode == null) ? 0 : unitOfMeasurementCode
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
		RegisterSensor other = (RegisterSensor) obj;
		if (epsgCode == null) {
			if (other.epsgCode != null)
				return false;
		} else if (!epsgCode.equals(other.epsgCode))
			return false;
		if (heightValue == null) {
			if (other.heightValue != null)
				return false;
		} else if (!heightValue.equals(other.heightValue))
			return false;
		if (heightUnit == null) {
			if (other.heightUnit != null)
				return false;
		} else if (!heightUnit.equals(other.heightUnit))
			return false;
		if (latitudeValue == null) {
			if (other.latitudeValue != null)
				return false;
		} else if (!latitudeValue.equals(other.latitudeValue))
			return false;
		if (latitudeUnit == null) {
			if (other.latitudeUnit != null)
				return false;
		} else if (!latitudeUnit.equals(other.latitudeUnit))
			return false;
		if (longitudeValue == null) {
			if (other.longitudeValue != null)
				return false;
		} else if (!longitudeValue.equals(other.longitudeValue))
			return false;
		if (longitudeUnit == null) {
			if (other.longitudeUnit != null)
				return false;
		} else if (!longitudeUnit.equals(other.longitudeUnit))
			return false;
		if (observedPropertyName == null) {
			if (other.observedPropertyName != null)
				return false;
		} else if (!observedPropertyName.equals(other.observedPropertyName))
			return false;
		if (observedPropertyURI == null) {
			if (other.observedPropertyURI != null)
				return false;
		} else if (!observedPropertyURI.equals(other.observedPropertyURI))
			return false;
		if (sensorName == null) {
			if (other.sensorName != null)
				return false;
		} else if (!sensorName.equals(other.sensorName))
			return false;
		if (sensorURI == null) {
			if (other.sensorURI != null)
				return false;
		} else if (!sensorURI.equals(other.sensorURI))
			return false;
		if (unitOfMeasurementCode == null) {
			if (other.unitOfMeasurementCode != null)
				return false;
		} else if (!unitOfMeasurementCode.equals(other.unitOfMeasurementCode))
			return false;
		return true;
	}	
}
