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
package org.n52.sos.importer.model.measuredValue;

import java.util.Iterator;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Parseable;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MeasuredValue implements Parseable {
	
	private static final Logger logger = LoggerFactory.getLogger(MeasuredValue.class);
	
	private TableElement tableElement;
	
	private DateAndTime dateAndTime;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private Sensor sensor;
	
	public void setFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
		if (featureOfInterest == null) {
			logger.info("Unassign Feature Of Interest from " + this);
		} else {
			logger.info("Assign " + featureOfInterest + " to " + this);
		}
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setObservedProperty(final ObservedProperty observedProperty) {
		if (observedProperty != null) {
			logger.info("Assign " + observedProperty + " to " + this);
		} else {
			logger.info("Unassign Observed Property from " + this);
		}
		this.observedProperty = observedProperty;
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	public void setUnitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
		if (unitOfMeasurement != null) {
			logger.info("Assign " + unitOfMeasurement + " to " + this);
		} else {
			logger.info("Unassign Unit of Measurement from " + this);
		}
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setSensor(final Sensor sensor) {
		if (sensor != null) {
			logger.info("Assign " + sensor + " to " + this);
		} else {
			logger.info("Unassign Sensor from " + this);
		}
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setTableElement(final TableElement tableElement) {
		logger.info("In " + tableElement + " are " + this + "s");
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}

	public void setDateAndTime(final DateAndTime dateAndTime) {
		if (dateAndTime != null) {
			logger.info("Assign " + dateAndTime + " to " + this);
		} else {
			logger.info("Unassign " + this.dateAndTime + " from " + this);
		}
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	/**
	 * Returns a sensor for the given feature of interest name and observed property
	 * name; this method is called when a sensor has been assigned to them in
	 * step 6b (special)
	 */
	public Sensor getSensorFor(final String featureOfInterestName, final String observedPropertyName) {
		final Iterator<Step6bSpecialModel> iterator = 
			ModelStore.getInstance().getStep6bSpecialModels().iterator();
		
		Step6bSpecialModel step6bSpecialModel;
		while (iterator.hasNext()) {
			step6bSpecialModel = iterator.next();
			if (step6bSpecialModel.getFeatureOfInterest().getName().equals(featureOfInterestName) &&
				step6bSpecialModel.getObservedProperty().getName().equals(observedPropertyName)) {
				return step6bSpecialModel.getSensor();
			}
		}
		
		//should never get here
		return null;
	}
	
	@Override
	public String toString() {
		if (getTableElement() == null) {
			return "";
		} else {
			return " " + getTableElement();
		}
	}
}
