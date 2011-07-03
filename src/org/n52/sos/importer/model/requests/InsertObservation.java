package org.n52.sos.importer.model.requests;

public class InsertObservation {
	
	private String sensorName;
	
	private String sensorURI;
	
	private String featureOfInterestName;
	
	private String featureOfInterestURI;
	
	private String observedPropertyURI;
	
	private String unitOfMeasurementCode;
	
	private String value;
	
	private String timeStamp;
	
	private String epsgCode;
	
	private String latitude;
	
	private String longitude;
	
	public String fillTemplate(String template) {
		template.replaceAll("sensorName", sensorName);
		template.replaceAll("sensorURI", sensorURI);
		template.replaceAll("featureOfInterestName", featureOfInterestName);
		template.replaceAll("featureOfInterestURI", featureOfInterestURI);
		template.replaceAll("observedPropertyURI", observedPropertyURI);
		template.replaceAll("unitOfMeasurementCode", unitOfMeasurementCode);
		template.replaceAll("value", value);
		template.replaceAll("timeStamp", timeStamp);
		template.replaceAll("epsgCode", epsgCode);
		template.replaceAll("latitude", latitude);
		template.replaceAll("longitude", longitude);
		return template;
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

	public String getFeatureOfInterestName() {
		return featureOfInterestName;
	}

	public void setFeatureOfInterestName(String featureOfInterestName) {
		this.featureOfInterestName = featureOfInterestName;
	}

	public String getFeatureOfInterestURI() {
		return featureOfInterestURI;
	}

	public void setFeatureOfInterestURI(String featureOfInterestURI) {
		this.featureOfInterestURI = featureOfInterestURI;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEpsgCode() {
		return epsgCode;
	}

	public void setEpsgCode(String epsgCode) {
		this.epsgCode = epsgCode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	@Override
	public String toString() {
		return "InsertObservation [sensorName=" + sensorName + ", sensorURI="
				+ sensorURI + ", featureOfInterestName="
				+ featureOfInterestName + ", featureOfInterestURI="
				+ featureOfInterestURI + ", observedPropertyURI="
				+ observedPropertyURI + ", unitOfMeasurementCode="
				+ unitOfMeasurementCode + ", value=" + value + ", timeStamp="
				+ timeStamp + ", epsgCode=" + epsgCode + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
	
}
