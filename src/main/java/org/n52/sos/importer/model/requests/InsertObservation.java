package org.n52.sos.importer.model.requests;

/**
 * collects all information for the InsertObservation request
 * @author Raimund
 */
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
	
	private String latitudeValue;

	private String longitudeValue;
	
	public String fillTemplate(String template) {
		String filledTemplate = template;
		filledTemplate = filledTemplate.replaceAll("THISsensorName", sensorName);
		filledTemplate = filledTemplate.replaceAll("THISsensorURI", sensorURI);
		filledTemplate = filledTemplate.replaceAll("THISfeatureOfInterestName", featureOfInterestName);
		filledTemplate = filledTemplate.replaceAll("THISfeatureOfInterestURI", featureOfInterestURI);
		filledTemplate = filledTemplate.replaceAll("THISobservedPropertyURI", observedPropertyURI);
		filledTemplate = filledTemplate.replaceAll("THISunitOfMeasurementCode", unitOfMeasurementCode);
		filledTemplate = filledTemplate.replaceAll("THISvalue", value);
		filledTemplate = filledTemplate.replaceAll("THIStimeStamp", timeStamp);
		filledTemplate = filledTemplate.replaceAll("THISepsgCode", epsgCode);
		filledTemplate = filledTemplate.replaceAll("THISlatitudeValue", latitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISlongitudeValue", longitudeValue);
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

	public String getLatitudeValue() {
		return latitudeValue;
	}

	public void setLatitudeValue(String latitudeValue) {
		this.latitudeValue = latitudeValue;
	}

	public String getLongitudeValue() {
		return longitudeValue;
	}

	public void setLongitudeValue(String longitudeValue) {
		this.longitudeValue = longitudeValue;
	}
	

	@Override
	public String toString() {
		return "InsertObservation [sensorName=" + sensorName + ", sensorURI="
				+ sensorURI + ", featureOfInterestName="
				+ featureOfInterestName + ", featureOfInterestURI="
				+ featureOfInterestURI + ", observedPropertyURI="
				+ observedPropertyURI + ", unitOfMeasurementCode="
				+ unitOfMeasurementCode + ", value=" + value + ", timeStamp="
				+ timeStamp + ", epsgCode=" + epsgCode + ", latitudeValue="
				+ latitudeValue + ", longitudeValue=" + longitudeValue + "]";
	}
	
}
