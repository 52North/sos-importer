/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
 * Collects all information for the RegisterSensor request
 *
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

    /**
     * <p>fillTemplate.</p>
     *
     * @param template a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
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
     * <p>setFeatureOfInterstName.</p>
     *
     * @param newFoiName a {@link java.lang.String} object.
     */
    public void setFeatureOfInterstName(String newFoiName) {
        this.foiName = newFoiName;
    }

    /**
     * <p>Setter for the field <code>observedPropertyName</code>.</p>
     *
     * @param observedPropertyName a {@link java.lang.String} object.
     */
    public void setObservedPropertyName(String observedPropertyName) {
        this.observedPropertyName = observedPropertyName;
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
     * <p>Setter for the field <code>offeringName</code>.</p>
     *
     * @param offeringName a {@link java.lang.String} object.
     */
    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
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
     * <p>Setter for the field <code>latitudeUnit</code>.</p>
     *
     * @param latitudeUnit a {@link java.lang.String} object.
     */
    public void setLatitudeUnit(String latitudeUnit) {
        this.latitudeUnit = latitudeUnit;
    }

    /**
     * <p>Setter for the field <code>longitudeValue</code>.</p>
     *
     * @param longitudeValue a {@link java.lang.String} object.
     */
    public void setLongitudeValue(String longitudeValue) {
        this.longitudeValue = longitudeValue;
    }

    /**
     * <p>Setter for the field <code>longitudeUnit</code>.</p>
     *
     * @param longitudeUnit a {@link java.lang.String} object.
     */
    public void setLongitudeUnit(String longitudeUnit) {
        this.longitudeUnit = longitudeUnit;
    }

    /**
     * <p>Setter for the field <code>altitudeValue</code>.</p>
     *
     * @param heightValue a {@link java.lang.String} object.
     */
    public void setAltitudeValue(String heightValue) {
        this.altitudeValue = heightValue;
    }

    /**
     * <p>Setter for the field <code>altitudeUnit</code>.</p>
     *
     * @param heightUnit a {@link java.lang.String} object.
     */
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
     * <p>getFeatureOfInterestName.</p>
     *
     * @return the foiName
     */
    public String getFeatureOfInterestName() {
        return foiName;
    }

    /**
     * <p>Getter for the field <code>observedPropertyName</code>.</p>
     *
     * @return the observedPropertyName
     */
    public String getObservedPropertyName() {
        return observedPropertyName;
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
     * <p>Getter for the field <code>offeringName</code>.</p>
     *
     * @return the offeringName
     */
    public String getOfferingName() {
        return offeringName;
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
     * <p>Getter for the field <code>latitudeUnit</code>.</p>
     *
     * @return the latitudeUnit
     */
    public String getLatitudeUnit() {
        return latitudeUnit;
    }

    /**
     * <p>Getter for the field <code>longitudeValue</code>.</p>
     *
     * @return the longitudeValue
     */
    public String getLongitudeValue() {
        return longitudeValue;
    }

    /**
     * <p>Getter for the field <code>longitudeUnit</code>.</p>
     *
     * @return the longitudeUnit
     */
    public String getLongitudeUnit() {
        return longitudeUnit;
    }

    /**
     * <p>Getter for the field <code>altitudeValue</code>.</p>
     *
     * @return the altitudeValue
     */
    public String getAltitudeValue() {
        return altitudeValue;
    }

    /**
     * <p>Getter for the field <code>altitudeUnit</code>.</p>
     *
     * @return the altitudeUnit
     */
    public String getAltitudeUnit() {
        return altitudeUnit;
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
     * <p>Setter for the field <code>featureOfInterestURI</code>.</p>
     *
     * @param featureOfInterestURI the featureOfInterestURI to set
     */
    public void setFeatureOfInterestURI(String featureOfInterestURI) {
        this.featureOfInterestURI = featureOfInterestURI;
    }


    /**
     * <p>Getter for the field <code>defaultValue</code>.</p>
     *
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }


    /**
     * <p>Setter for the field <code>defaultValue</code>.</p>
     *
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    /**
     * <p>Getter for the field <code>offeringId</code>.</p>
     *
     * @return the offeringId
     */
    public String getOfferingId() {
        return offeringId;
    }


    /**
     * <p>Setter for the field <code>offeringId</code>.</p>
     *
     * @param offeringId the offeringId to set
     */
    public void setOfferingId(String offeringId) {
        this.offeringId = offeringId;
    }
}
