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
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureOfInterest extends Resource implements Comparable<FeatureOfInterest> {

    private static final String FROM = " from ";
    private static final String UNASSIGN = "Unassign ";
    private static final String TO = " to ";
    private static final String ASSIGN = "Assign ";

    private static final Logger LOG = LoggerFactory.getLogger(FeatureOfInterest.class);

    /** single position or position column/row */
    private Position position;

    /** corresponding positions for each feature of interest in this column/row */
    private final HashMap<String, Position> positions = new HashMap<>();

    private String parentFeatureIdentifier;

    @Override
    public void assign(final MeasuredValue measuredValue) {
        measuredValue.setFeatureOfInterest(this);
    }

    @Override
    public boolean isAssigned(final MeasuredValue measuredValue) {
        return measuredValue.getFeatureOfInterest() != null;
    }

    @Override
    public boolean isAssignedTo(final MeasuredValue measuredValue) {
        return equals(measuredValue.getFeatureOfInterest());
    }

    @Override
    public void unassign(final MeasuredValue mv) {
        mv.setFeatureOfInterest(null);
    }

    @Override
    public FeatureOfInterest forThis(final Cell measuredValuePosition) {
        /*
         * case A: this is not a feature of interest row or column;
         *          it is a global foi or a generated one
         */
        if (getTableElement() == null || isGenerated()) {
            return this;
        } else {
            // TODO check position handling here!
            // each row/column has its own foi, so return new instances
            final FeatureOfInterest foi = new FeatureOfInterest();
            final String name = getTableElement().getValueFor(measuredValuePosition);
            foi.setName(name);
            // TODO check, if the next line break any logic
            foi.setTableElement(getTableElement());

            /*
             * case B: this is a feature of interest row or column
             *
             * case B1: associated with a position row or column in the table
             */
            if (position != null) {
                final PositionController pc = new PositionController(position);
                final Cell c = getTableElement().getCellFor(measuredValuePosition);
                final Position p = pc.forThis(c);
                foi.setPosition(p);
            /*
             * case B2: not associated with a position row or column in the table
             */
            } else {
                final Position p = getPositionFor(name);
                foi.setPosition(p);
            }
            return foi;
        }
    }

    @Override
    public DefaultComboBoxModel<String> getNames() {
        return EditableComboBoxItems.getInstance().getFeatureOfInterestNames();
    }

    @Override
    public DefaultComboBoxModel<String> getURIs() {
        return EditableComboBoxItems.getInstance().getFeatureOfInterestURIs();
    }

    @Override
    public List<Resource> getList() {
        final List<Resource> resources = new ArrayList<>();
        resources.addAll(ModelStore.getInstance().getFeatureOfInterests());
        return resources;
    }

    @Override
    public Resource getNextResourceType() {
        return new ObservedProperty();
    }

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void setPosition(final Position position) {
        this.position = position;
    }

    /**
     * <p>Getter for the field <code>position</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * <p>assignPosition.</p>
     *
     * @param newPosition a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void assignPosition(final Position newPosition) {
        LOG.info(ASSIGN + newPosition + TO + this);
        setPosition(newPosition);
    }

    /**
     * <p>unassignPosition.</p>
     */
    public void unassignPosition() {
        if (position != null) {
            LOG.info(UNASSIGN + position + FROM + this);
            setPosition(null);
        }
    }

    /**
     * <p>setPositionFor.</p>
     *
     * @param featureOfInterestName a {@link java.lang.String} object.
     * @param newPosition a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void setPositionFor(final String featureOfInterestName, final Position newPosition) {
        LOG.info(ASSIGN + newPosition + TO + featureOfInterestName);
        positions.put(featureOfInterestName, newPosition);
    }

    /**
     * <p>removePositionFor.</p>
     *
     * @param featureOfInterestName a {@link java.lang.String} object.
     */
    public void removePositionFor(final String featureOfInterestName) {
        final Position p = getPositionFor(featureOfInterestName);
        if (p != null) {
            LOG.info(UNASSIGN + p + FROM + featureOfInterestName);
        }
        positions.remove(featureOfInterestName);
    }

    /**
     * <p>getPositionFor.</p>
     *
     * @param featureOfInterestName a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Position getPositionFor(final String featureOfInterestName) {
        return positions.get(featureOfInterestName);
    }

    @Override
    public String toString() {
        return "Feature Of Interest" + super.toString();
    }

    @Override
    public String getTypeName() {
        return Lang.l().featureOfInterest();
    }

    @Override
    public String XML_PREFIX() {
        return "foi";
    }

    @Override
    public int compareTo(final FeatureOfInterest o) {
        // try to compare by name
        if (getName() != null && o.getName() != null) {
            return getName().compareTo(o.getName());
        }
        // compare by xmlId
        return getXMLId().compareTo(o.getXMLId());
    }

    @Override
    public String getName() {
        if (isGenerated()) {
            return getXMLId();
        } else {
            return super.getName();
        }
    }

    public FeatureOfInterest setParentFeatureIdentifier(String parentFeatureIdentifier) {
        this.parentFeatureIdentifier = parentFeatureIdentifier;
        return this;
    }

    public String getParentFeatureIdentifier() {
        return parentFeatureIdentifier;
    }

    public boolean hasParentFeature() {
        return parentFeatureIdentifier != null &&
                !parentFeatureIdentifier.isEmpty() &&
                parentFeatureIdentifier.length() > 3;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (parentFeatureIdentifier == null ? 0 : parentFeatureIdentifier.hashCode());
        result = prime * result + (position == null ? 0 : position.hashCode());
        result = prime * result + (positions == null ? 0 : positions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FeatureOfInterest other = (FeatureOfInterest) obj;
        if (parentFeatureIdentifier == null) {
            if (other.parentFeatureIdentifier != null) {
                return false;
            }
        } else if (!parentFeatureIdentifier.equals(other.parentFeatureIdentifier)) {
            return false;
        }
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!position.equals(other.position)) {
            return false;
        }
        if (positions == null) {
            if (other.positions != null) {
                return false;
            }
        } else if (!positions.equals(other.positions)) {
            return false;
        }
        return true;
    }

}
