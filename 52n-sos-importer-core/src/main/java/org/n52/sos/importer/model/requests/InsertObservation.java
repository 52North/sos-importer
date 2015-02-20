/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
