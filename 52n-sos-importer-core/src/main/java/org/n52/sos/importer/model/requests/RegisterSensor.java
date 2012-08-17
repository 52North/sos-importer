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
 * Collects all information for the RegisterSensor request
 * @author Raimund
 */
public class RegisterSensor {

	private String sensorName;
	
	private String sensorURI;
	
	private String foiName;
	
	private String featureOfInterestURI;
	
	private String observedPropertyName;
	
	private String observedPropertyURI;
	
	private String offeringName;

	private String unitOfMeasurementCode;
	
	private String epsgCode;
	
	private String latitudeValue;
	
	private String latitudeUnit;
	
	private String longitudeValue;
	
	private String longitudeUnit;
	
	private String altitudeValue;
	
	private String altitudeUnit;

	private String defaultValue;
	
	private String offeringId;
	
	public String fillTemplate(String template) {
		String filledTemplate = template;
		// TODO move tags into config
		filledTemplate = filledTemplate.replaceAll("THISsensorName", sensorName);
		filledTemplate = filledTemplate.replaceAll("THISsensorURI", sensorURI);
		filledTemplate = filledTemplate.replaceAll("THISfoiName", foiName);
		filledTemplate = filledTemplate.replaceAll("THISfoiURI", featureOfInterestURI);
		filledTemplate = filledTemplate.replaceAll("THISobservedPropertyName", observedPropertyName);
		filledTemplate = filledTemplate.replaceAll("THISobservedPropertyURI", observedPropertyURI);
		filledTemplate = filledTemplate.replaceAll("THISofferingName", offeringName);
		filledTemplate = filledTemplate.replaceAll("THISofferingId", offeringId);
		filledTemplate = filledTemplate.replaceAll("THISunitOfMeasurementCode", unitOfMeasurementCode);
		filledTemplate = filledTemplate.replaceAll("THISlatitudeValue", latitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISlatitudeUnit", latitudeUnit);
		filledTemplate = filledTemplate.replaceAll("THISlongitudeValue", longitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISlongitudeUnit", longitudeUnit);
		filledTemplate = filledTemplate.replaceAll("THISheightValue", altitudeValue);
		filledTemplate = filledTemplate.replaceAll("THISheightUnit", altitudeUnit);
		filledTemplate = filledTemplate.replaceAll("THISepsgCode", epsgCode);
		filledTemplate = filledTemplate.replaceAll("THISdefaultValue", getDefaultValue());
		return filledTemplate; 
	}
	
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public void setSensorURI(String sensorURI) {
		this.sensorURI = sensorURI;
	}

    public void setFeatureOfInterstName(String foiName) {
        this.foiName = foiName;
    }

	public void setObservedPropertyName(String observedPropertyName) {
		this.observedPropertyName = observedPropertyName;
	}

	public void setObservedPropertyURI(String observedPropertyURI) {
		this.observedPropertyURI = observedPropertyURI;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public void setUnitOfMeasurementCode(String unitOfMeasurementCode) {
		this.unitOfMeasurementCode = unitOfMeasurementCode;
	}

	public void setEpsgCode(String epsgCode) {
		this.epsgCode = epsgCode;
	}

	public void setLatitudeValue(String latitudeValue) {
		this.latitudeValue = latitudeValue;
	}

	public void setLatitudeUnit(String latitudeUnit) {
		this.latitudeUnit = latitudeUnit;
	}

	public void setLongitudeValue(String longitudeValue) {
		this.longitudeValue = longitudeValue;
	}

	public void setLongitudeUnit(String longitudeUnit) {
		this.longitudeUnit = longitudeUnit;
	}

	public void setAltitudeValue(String heightValue) {
		this.altitudeValue = heightValue;
	}

	public void setAltitudeUnit(String heightUnit) {
		this.altitudeUnit = heightUnit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegisterSensor [sensorName=" + sensorName + ", sensorURI="
				+ sensorURI + ", foiName=" + foiName
				+ ", featureOfInterestURI=" + featureOfInterestURI
				+ ", observedPropertyName=" + observedPropertyName
				+ ", observedPropertyURI=" + observedPropertyURI
				+ ", offeringName=" + offeringName + ", unitOfMeasurementCode="
				+ unitOfMeasurementCode + ", epsgCode=" + epsgCode
				+ ", latitudeValue=" + latitudeValue + ", latitudeUnit="
				+ latitudeUnit + ", longitudeValue=" + longitudeValue
				+ ", longitudeUnit=" + longitudeUnit + ", altitudeValue="
				+ altitudeValue + ", altitudeUnit=" + altitudeUnit
				+ ", defaultValue=" + defaultValue + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((altitudeUnit == null) ? 0 : altitudeUnit.hashCode());
		result = prime * result
				+ ((altitudeValue == null) ? 0 : altitudeValue.hashCode());
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result
				+ ((epsgCode == null) ? 0 : epsgCode.hashCode());
		result = prime
				* result
				+ ((featureOfInterestURI == null) ? 0 : featureOfInterestURI
						.hashCode());
		result = prime * result + ((foiName == null) ? 0 : foiName.hashCode());
		result = prime * result
				+ ((latitudeUnit == null) ? 0 : latitudeUnit.hashCode());
		result = prime * result
				+ ((latitudeValue == null) ? 0 : latitudeValue.hashCode());
		result = prime * result
				+ ((longitudeUnit == null) ? 0 : longitudeUnit.hashCode());
		result = prime * result
				+ ((longitudeValue == null) ? 0 : longitudeValue.hashCode());
		result = prime
				* result
				+ ((observedPropertyName == null) ? 0 : observedPropertyName
						.hashCode());
		result = prime
				* result
				+ ((observedPropertyURI == null) ? 0 : observedPropertyURI
						.hashCode());
		result = prime * result
				+ ((offeringName == null) ? 0 : offeringName.hashCode());
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RegisterSensor)) {
			return false;
		}
		RegisterSensor other = (RegisterSensor) obj;
		if (altitudeUnit == null) {
			if (other.altitudeUnit != null) {
				return false;
			}
		} else if (!altitudeUnit.equals(other.altitudeUnit)) {
			return false;
		}
		if (altitudeValue == null) {
			if (other.altitudeValue != null) {
				return false;
			}
		} else if (!altitudeValue.equals(other.altitudeValue)) {
			return false;
		}
		if (defaultValue == null) {
			if (other.defaultValue != null) {
				return false;
			}
		} else if (!defaultValue.equals(other.defaultValue)) {
			return false;
		}
		if (epsgCode == null) {
			if (other.epsgCode != null) {
				return false;
			}
		} else if (!epsgCode.equals(other.epsgCode)) {
			return false;
		}
		if (featureOfInterestURI == null) {
			if (other.featureOfInterestURI != null) {
				return false;
			}
		} else if (!featureOfInterestURI.equals(other.featureOfInterestURI)) {
			return false;
		}
		if (foiName == null) {
			if (other.foiName != null) {
				return false;
			}
		} else if (!foiName.equals(other.foiName)) {
			return false;
		}
		if (latitudeUnit == null) {
			if (other.latitudeUnit != null) {
				return false;
			}
		} else if (!latitudeUnit.equals(other.latitudeUnit)) {
			return false;
		}
		if (latitudeValue == null) {
			if (other.latitudeValue != null) {
				return false;
			}
		} else if (!latitudeValue.equals(other.latitudeValue)) {
			return false;
		}
		if (longitudeUnit == null) {
			if (other.longitudeUnit != null) {
				return false;
			}
		} else if (!longitudeUnit.equals(other.longitudeUnit)) {
			return false;
		}
		if (longitudeValue == null) {
			if (other.longitudeValue != null) {
				return false;
			}
		} else if (!longitudeValue.equals(other.longitudeValue)) {
			return false;
		}
		if (observedPropertyName == null) {
			if (other.observedPropertyName != null) {
				return false;
			}
		} else if (!observedPropertyName.equals(other.observedPropertyName)) {
			return false;
		}
		if (observedPropertyURI == null) {
			if (other.observedPropertyURI != null) {
				return false;
			}
		} else if (!observedPropertyURI.equals(other.observedPropertyURI)) {
			return false;
		}
		if (offeringName == null) {
			if (other.offeringName != null) {
				return false;
			}
		} else if (!offeringName.equals(other.offeringName)) {
			return false;
		}
		if (sensorName == null) {
			if (other.sensorName != null) {
				return false;
			}
		} else if (!sensorName.equals(other.sensorName)) {
			return false;
		}
		if (sensorURI == null) {
			if (other.sensorURI != null) {
				return false;
			}
		} else if (!sensorURI.equals(other.sensorURI)) {
			return false;
		}
		if (unitOfMeasurementCode == null) {
			if (other.unitOfMeasurementCode != null) {
				return false;
			}
		} else if (!unitOfMeasurementCode.equals(other.unitOfMeasurementCode)) {
			return false;
		}
		return true;
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
	 * @return the foiName
	 */
	public String getFeatureOfInterestName() {
		return foiName;
	}

	/**
	 * @return the observedPropertyName
	 */
	public String getObservedPropertyName() {
		return observedPropertyName;
	}

	/**
	 * @return the observedPropertyURI
	 */
	public String getObservedPropertyURI() {
		return observedPropertyURI;
	}

	/**
	 * @return the offeringName
	 */
	public String getOfferingName() {
		return offeringName;
	}

	/**
	 * @return the unitOfMeasurementCode
	 */
	public String getUnitOfMeasurementCode() {
		return unitOfMeasurementCode;
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
	 * @return the latitudeUnit
	 */
	public String getLatitudeUnit() {
		return latitudeUnit;
	}

	/**
	 * @return the longitudeValue
	 */
	public String getLongitudeValue() {
		return longitudeValue;
	}

	/**
	 * @return the longitudeUnit
	 */
	public String getLongitudeUnit() {
		return longitudeUnit;
	}

	/**
	 * @return the altitudeValue
	 */
	public String getAltitudeValue() {
		return altitudeValue;
	}

	/**
	 * @return the altitudeUnit
	 */
	public String getAltitudeUnit() {
		return altitudeUnit;
	}


	/**
	 * @return the featureOfInterestURI
	 */
	public String getFeatureOfInterestURI() {
		return featureOfInterestURI;
	}


	/**
	 * @param featureOfInterestURI the featureOfInterestURI to set
	 */
	public void setFeatureOfInterestURI(String featureOfInterestURI) {
		this.featureOfInterestURI = featureOfInterestURI;
	}


	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}


	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	/**
	 * @return the offeringId
	 */
	public String getOfferingId() {
		return offeringId;
	}


	/**
	 * @param offeringId the offeringId to set
	 */
	public void setOfferingId(String offeringId) {
		this.offeringId = offeringId;
	}
}
