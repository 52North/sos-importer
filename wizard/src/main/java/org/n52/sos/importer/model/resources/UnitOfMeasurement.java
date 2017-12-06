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
 */
package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

public class UnitOfMeasurement extends Resource {

    @Override
    public void assign(MeasuredValue measuredValue) {
        measuredValue.setUnitOfMeasurement(this);
    }

    @Override
    public boolean isAssigned(MeasuredValue measuredValue) {
        return measuredValue.getUnitOfMeasurement() != null;
    }

    @Override
    public boolean isAssignedTo(MeasuredValue measuredValue) {
        return measuredValue.getUnitOfMeasurement() == this;
    }

    @Override
    public void unassign(MeasuredValue mv) {
        mv.setUnitOfMeasurement(null);
    }

    @Override
    public UnitOfMeasurement forThis(Cell measuredValuePosition) {
        if (getTableElement() == null) {
            return this;
        } else {
            UnitOfMeasurement uom = new UnitOfMeasurement();
            String name = getTableElement().getValueFor(measuredValuePosition);
            uom.setName(name);
            return uom;
        }
    }

    @Override
    public DefaultComboBoxModel<String> getNames() {
        return EditableComboBoxItems.getInstance().getUnitOfMeasurementCodes();
    }

    @Override
    public DefaultComboBoxModel<String> getURIs() {
        return EditableComboBoxItems.getInstance().getUnitOfMeasurementURIs();
    }

    @Override
    public List<Resource> getList() {
        List<Resource> resources = new ArrayList<>();
        resources.addAll(ModelStore.getInstance().getUnitOfMeasurements());
        return resources;
    }

    @Override
    public Resource getNextResourceType() {
        return new Sensor();
    }

    @Override
    public String toString() {
        return "Unit Of Measurement" + super.toString();
    }

    @Override
    public String getTypeName() {
        return Lang.l().unitOfMeasurement();
    }

    @Override
    public String XML_PREFIX() {
        return "uom";
    }
}
