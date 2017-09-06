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
package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.i18n.Lang;

public class OmParameter extends Resource {

    private String subType;

    public OmParameter(String type) {
        subType = type;
    }

    @Override
    public String XML_PREFIX() {
        return "omparameter";
    }

    @Override
    public String getTypeName() {
        return Lang.l().step3ColTypeOmParameter();
    }

    @Override
    public void assign(MeasuredValue mv) {
        mv.addOmParameter(this);
    }

    @Override
    public boolean isAssigned(MeasuredValue mv) {
        return mv.getOmParameters() != null && !mv.getOmParameters().isEmpty();
    }

    @Override
    public boolean isAssignedTo(MeasuredValue mv) {
        return mv.getOmParameters().contains(this);
    }

    @Override
    public void unassign(MeasuredValue mv) {
        mv.removeOmParameter(this);
    }

    @Override
    public DefaultComboBoxModel<String> getNames() {
        return new DefaultComboBoxModel<>();
    }

    @Override
    public DefaultComboBoxModel<String> getURIs() {
        return new DefaultComboBoxModel<>();
    }

    @Override
    public List<Resource> getList() {
        List<Resource> resources = new ArrayList<>();
        resources.addAll(ModelStore.getInstance().getOmParameters());
        return resources;
    }

    @Override
    public Resource getNextResourceType() {
        return null;
    }

    @Override
    public OmParameter forThis(Cell measuredValuePosition) {
        if (getTableElement() == null || isGenerated()) {
            return this;
        } else {
            OmParameter op = new OmParameter(subType);
            String name = getTableElement().getValueFor(measuredValuePosition);
            op.setName(name);
            op.setTableElement(getTableElement());
            return op;
        }
    }

    @Override
    public String toString() {
        return "om:Parameter[type:" + subType + "; " + super.toString() + "]";
    }

}
