/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
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
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public void setSensorURI(String sensorURI) {
		this.sensorURI = sensorURI;
	}

	public void setFeatureOfInterestName(String featureOfInterestName) {
		this.featureOfInterestName = featureOfInterestName;
	}

	public void setFeatureOfInterestURI(String featureOfInterestURI) {
		this.featureOfInterestURI = featureOfInterestURI;
	}

	public void setObservedPropertyURI(String observedPropertyURI) {
		this.observedPropertyURI = observedPropertyURI;
	}

	public void setUnitOfMeasurementCode(String unitOfMeasurementCode) {
		this.unitOfMeasurementCode = unitOfMeasurementCode;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setEpsgCode(String epsgCode) {
		this.epsgCode = epsgCode;
	}

	public void setLatitudeValue(String latitudeValue) {
		this.latitudeValue = latitudeValue;
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

	/**
	 * @return the sensorName
	 */
	public String getSensorName() {
		return sensorName;
	}

	/**
	 * @return the sensorURI
	 */
	public String getSensorURI() {
		return sensorURI;
	}

	/**
	 * @return the featureOfInterestName
	 */
	public String getFeatureOfInterestName() {
		return featureOfInterestName;
	}

	/**
	 * @return the featureOfInterestURI
	 */
	public String getFeatureOfInterestURI() {
		return featureOfInterestURI;
	}

	/**
	 * @return the observedPropertyURI
	 */
	public String getObservedPropertyURI() {
		return observedPropertyURI;
	}

	/**
	 * @return the unitOfMeasurementCode
	 */
	public String getUnitOfMeasurementCode() {
		return unitOfMeasurementCode;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return the epsgCode
	 */
	public String getEpsgCode() {
		return epsgCode;
	}

	/**
	 * @return the latitudeValue
	 */
	public String getLatitudeValue() {
		return latitudeValue;
	}

	/**
	 * @return the longitudeValue
	 */
	public String getLongitudeValue() {
		return longitudeValue;
	}
	
}
