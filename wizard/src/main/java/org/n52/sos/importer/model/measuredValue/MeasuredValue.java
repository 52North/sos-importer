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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
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

    /**
     * 
     */
    private static final String TO = " to ";

    /**
     * 
     */
    private static final String ASSIGN = "Assign ";

    private static final Logger logger = LoggerFactory.getLogger(MeasuredValue.class);

    private TableElement tableElement;

    private DateAndTime dateAndTime;

    private ObservedProperty observedProperty;

    private UnitOfMeasurement unitOfMeasurement;

    private FeatureOfInterest featureOfInterest;

    private Sensor sensor;

    /**
     * <p>Setter for the field <code>featureOfInterest</code>.</p>
     *
     * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     */
    public void setFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
        if (featureOfInterest == null) {
            logger.info("Unassign Feature Of Interest from " + this);
        } else {
            logger.info(ASSIGN + featureOfInterest + TO + this);
        }
        this.featureOfInterest = featureOfInterest;
    }

    /**
     * <p>Getter for the field <code>featureOfInterest</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     */
    public FeatureOfInterest getFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * <p>Setter for the field <code>observedProperty</code>.</p>
     *
     * @param observedProperty a {@link org.n52.sos.importer.model.resources.ObservedProperty} object.
     */
    public void setObservedProperty(final ObservedProperty observedProperty) {
        if (observedProperty != null) {
            logger.info(ASSIGN + observedProperty + TO + this);
        } else {
            logger.info("Unassign Observed Property from " + this);
        }
        this.observedProperty = observedProperty;
    }

    /**
     * <p>Getter for the field <code>observedProperty</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.ObservedProperty} object.
     */
    public ObservedProperty getObservedProperty() {
        return observedProperty;
    }

    /**
     * <p>Setter for the field <code>unitOfMeasurement</code>.</p>
     *
     * @param unitOfMeasurement a {@link org.n52.sos.importer.model.resources.UnitOfMeasurement} object.
     */
    public void setUnitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
        if (unitOfMeasurement != null) {
            logger.info(ASSIGN + unitOfMeasurement + TO + this);
        } else {
            logger.info("Unassign Unit of Measurement from " + this);
        }
        this.unitOfMeasurement = unitOfMeasurement;
    }

    /**
     * <p>Getter for the field <code>unitOfMeasurement</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.UnitOfMeasurement} object.
     */
    public UnitOfMeasurement getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    /**
     * <p>Setter for the field <code>sensor</code>.</p>
     *
     * @param sensor a {@link org.n52.sos.importer.model.resources.Sensor} object.
     */
    public void setSensor(final Sensor sensor) {
        if (sensor != null) {
            logger.info(ASSIGN + sensor + TO + this);
        } else {
            logger.info("Unassign Sensor from " + this);
        }
        this.sensor = sensor;
    }

    /**
     * <p>Getter for the field <code>sensor</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.resources.Sensor} object.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * <p>Setter for the field <code>tableElement</code>.</p>
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void setTableElement(final TableElement tableElement) {
        logger.info("In " + tableElement + " are " + this + "s");
        this.tableElement = tableElement;
    }

    /**
     * <p>Getter for the field <code>tableElement</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public TableElement getTableElement() {
        return tableElement;
    }

    /**
     * <p>Setter for the field <code>dateAndTime</code>.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public void setDateAndTime(final DateAndTime dateAndTime) {
        if (dateAndTime != null) {
            logger.info(ASSIGN + dateAndTime + TO + this);
        } else {
            logger.info("Unassign " + this.dateAndTime + " from " + this);
        }
        this.dateAndTime = dateAndTime;
    }

    /**
     * <p>Getter for the field <code>dateAndTime</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public DateAndTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Returns a sensor for the given feature of interest name and observed property
     * name; this method is called when a sensor has been assigned to them in
     * step 6b (special)
     *
     * @param featureOfInterestName a {@link java.lang.String} object.
     * @param observedPropertyName a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.resources.Sensor} object.
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (getTableElement() == null) {
            return "";
        } else {
            return " " + getTableElement();
        }
    }
}
