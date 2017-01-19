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
package org.n52.sos.importer.model;

import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bSpecialModel implements StepModel {

    private final FeatureOfInterest foi;

    private final ObservedProperty obsProp;

    private Sensor sensor;

    /**
     * <p>Constructor for Step6bSpecialModel.</p>
     *
     * @param foi a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
     * @param obsProp a {@link org.n52.sos.importer.model.resources.ObservedProperty} object.
     */
    public Step6bSpecialModel(FeatureOfInterest foi, ObservedProperty obsProp) {
        this.foi = foi;
        this.obsProp = obsProp;
        sensor = new Sensor();
    }

    /**
     * <p>Setter for the field <code>sensor</code>.</p>
     *
     * @param sensor a {@link org.n52.sos.importer.model.resources.Sensor} object.
     */
    public void setSensor(Sensor sensor) {
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
     * <p>getDescription.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return Lang.l().step6bSpecialModelDescription();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((foi == null) ? 0 : foi
                        .hashCode());
        result = prime
                * result
                + ((obsProp == null) ? 0 : obsProp
                        .hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Step6bSpecialModel)) {
            return false;
        }
        Step6bSpecialModel other = (Step6bSpecialModel) obj;
        if (foi == null) {
            if (other.foi != null) {
                return false;
            }
        } else if (!foi.equals(other.foi)) {
            return false;
        }
        if (obsProp == null) {
            if (other.obsProp != null) {
                return false;
            }
        } else if (!obsProp.equals(other.obsProp)) {
            return false;
        }
        return true;
    }

    /**
     * <p>getFeatureOfInterest.</p>
     *
     * @return the foi
     */
    public FeatureOfInterest getFeatureOfInterest() {
        return foi;
    }

    /**
     * <p>getObservedProperty.</p>
     *
     * @return the obsProp
     */
    public ObservedProperty getObservedProperty() {
        return obsProp;
    }

}
