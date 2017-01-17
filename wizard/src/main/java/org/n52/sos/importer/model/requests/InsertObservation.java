/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
 *
 * @author Raimund
 * @version $Id: $Id
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

	/**
	 * <p>fillTemplate.</p>
	 *
	 * @param template a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
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

	/**
	 * <p>Setter for the field <code>sensorName</code>.</p>
	 *
	 * @param sensorName a {@link java.lang.String} object.
	 */
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	/**
	 * <p>Setter for the field <code>sensorURI</code>.</p>
	 *
	 * @param sensorURI a {@link java.lang.String} object.
	 */
	public void setSensorURI(String sensorURI) {
		this.sensorURI = sensorURI;
	}

	/**
	 * <p>Setter for the field <code>featureOfInterestName</code>.</p>
	 *
	 * @param featureOfInterestName a {@link java.lang.String} object.
	 */
	public void setFeatureOfInterestName(String featureOfInterestName) {
		this.featureOfInterestName = featureOfInterestName;
	}

	/**
	 * <p>Setter for the field <code>featureOfInterestURI</code>.</p>
	 *
	 * @param featureOfInterestURI a {@link java.lang.String} object.
	 */
	public void setFeatureOfInterestURI(String featureOfInterestURI) {
		this.featureOfInterestURI = featureOfInterestURI;
	}

	/**
	 * <p>Setter for the field <code>observedPropertyURI</code>.</p>
	 *
	 * @param observedPropertyURI a {@link java.lang.String} object.
	 */
	public void setObservedPropertyURI(String observedPropertyURI) {
		this.observedPropertyURI = observedPropertyURI;
	}

	/**
	 * <p>Setter for the field <code>unitOfMeasurementCode</code>.</p>
	 *
	 * @param unitOfMeasurementCode a {@link java.lang.String} object.
	 */
	public void setUnitOfMeasurementCode(String unitOfMeasurementCode) {
		this.unitOfMeasurementCode = unitOfMeasurementCode;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * <p>Setter for the field <code>timeStamp</code>.</p>
	 *
	 * @param timeStamp a {@link java.lang.String} object.
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * <p>Setter for the field <code>epsgCode</code>.</p>
	 *
	 * @param epsgCode a {@link java.lang.String} object.
	 */
	public void setEpsgCode(String epsgCode) {
		this.epsgCode = epsgCode;
	}

	/**
	 * <p>Setter for the field <code>latitudeValue</code>.</p>
	 *
	 * @param latitudeValue a {@link java.lang.String} object.
	 */
	public void setLatitudeValue(String latitudeValue) {
		this.latitudeValue = latitudeValue;
	}

	/**
	 * <p>Setter for the field <code>longitudeValue</code>.</p>
	 *
	 * @param longitudeValue a {@link java.lang.String} object.
	 */
	public void setLongitudeValue(String longitudeValue) {
		this.longitudeValue = longitudeValue;
	}


	/** {@inheritDoc} */
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
	 * <p>Getter for the field <code>sensorName</code>.</p>
	 *
	 * @return the sensorName
	 */
	public String getSensorName() {
		return sensorName;
	}

	/**
	 * <p>Getter for the field <code>sensorURI</code>.</p>
	 *
	 * @return the sensorURI
	 */
	public String getSensorURI() {
		return sensorURI;
	}

	/**
	 * <p>Getter for the field <code>featureOfInterestName</code>.</p>
	 *
	 * @return the featureOfInterestName
	 */
	public String getFeatureOfInterestName() {
		return featureOfInterestName;
	}

	/**
	 * <p>Getter for the field <code>featureOfInterestURI</code>.</p>
	 *
	 * @return the featureOfInterestURI
	 */
	public String getFeatureOfInterestURI() {
		return featureOfInterestURI;
	}

	/**
	 * <p>Getter for the field <code>observedPropertyURI</code>.</p>
	 *
	 * @return the observedPropertyURI
	 */
	public String getObservedPropertyURI() {
		return observedPropertyURI;
	}

	/**
	 * <p>Getter for the field <code>unitOfMeasurementCode</code>.</p>
	 *
	 * @return the unitOfMeasurementCode
	 */
	public String getUnitOfMeasurementCode() {
		return unitOfMeasurementCode;
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <p>Getter for the field <code>timeStamp</code>.</p>
	 *
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * <p>Getter for the field <code>epsgCode</code>.</p>
	 *
	 * @return the epsgCode
	 */
	public String getEpsgCode() {
		return epsgCode;
	}

	/**
	 * <p>Getter for the field <code>latitudeValue</code>.</p>
	 *
	 * @return the latitudeValue
	 */
	public String getLatitudeValue() {
		return latitudeValue;
	}

	/**
	 * <p>Getter for the field <code>longitudeValue</code>.</p>
	 *
	 * @return the longitudeValue
	 */
	public String getLongitudeValue() {
		return longitudeValue;
	}

}
