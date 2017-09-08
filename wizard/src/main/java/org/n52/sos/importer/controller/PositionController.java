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
package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.referencing.CRS;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;
import org.n52.sos.importer.view.position.MissingHeightPanel;
import org.n52.sos.importer.view.position.MissingLatitudePanel;
import org.n52.sos.importer.view.position.MissingLongitudePanel;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * handles operations on Position objects
 *
 * @author Raimund
 */
public class PositionController {

    private static final Logger LOG = LoggerFactory.getLogger(PositionController.class);

    private Position position;

    private final List<MissingComponentPanel> missingComponentPanels;

    /**
     * <p>Constructor for PositionController.</p>
     */
    public PositionController() {
        position = new Position();
        missingComponentPanels = new ArrayList<>();
    }

    /**
     * <p>Constructor for PositionController.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public PositionController(final Position position) {
        this.position = position;
        missingComponentPanels = new ArrayList<>();
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
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void setPosition(final Position position) {
        this.position = position;
    }

    /**
     * <p>assignPattern.</p>
     *
     * @param pattern a {@link java.lang.String} object.
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public void assignPattern(final String pattern, final TableElement tableElement) {
        LOG.info("Assign pattern " + pattern + " to " + position + " in " + tableElement);

        if (pattern.contains("LAT")) {
            position.setLatitude(new Latitude(tableElement, pattern));
        }
        if (pattern.contains("LON")) {
            position.setLongitude(new Longitude(tableElement, pattern));
        }
        if (pattern.contains("ALT")) {
            position.setHeight(new Height(tableElement, pattern));
        }
        if (pattern.contains("EPSG")) {
            position.setEPSGCode(new EPSGCode(tableElement, pattern));
        }
    }

    /**
     * <p>getNextPositionWithMissingValues.</p>
     *
     * @return a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Position getNextPositionWithMissingValues() {
        List<MissingComponentPanel> missingComponentPanelsTmp;

        for (final Position positionTmp : ModelStore.getInstance().getPositions()) {
            setPosition(positionTmp);
            missingComponentPanelsTmp = getMissingComponentPanels();
            if (missingComponentPanelsTmp.size() > 0) {
                return positionTmp;
            }
        }
        return null;
    }

    /**
     * <p>Getter for the field <code>missingComponentPanels</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<MissingComponentPanel> getMissingComponentPanels() {
        if (!missingComponentPanels.isEmpty()) {
            return missingComponentPanels;
        }
        if (position.getLatitude() == null) {
            missingComponentPanels.add(new MissingLatitudePanel(position));
        }
        if (position.getLongitude() == null) {
            missingComponentPanels.add(new MissingLongitudePanel(position));
        }
        if (position.getEPSGCode() == null) {
            missingComponentPanels.add(new MissingEPSGCodePanel(position));
        }
        if (position.getHeight() == null && shouldHeightPanelBeAddedForEPSG(position.getEPSGCode())) {
            missingComponentPanels.add(new MissingHeightPanel(position));
        }

        return missingComponentPanels;
    }

    /**
     * @return returns <tt>true</tt>,
     *      if height is allowed, or
     *      we could not say:"it is not allowed" because of parsing errors...
     */
    private boolean shouldHeightPanelBeAddedForEPSG(final EPSGCode epsgCode) {
        if (epsgCode == null) {
            return true;
        }
        // try to create gt-CRS from code and check for height axis
        // 1 try to create CRS object
        String epsgString = "EPSG:";
        final TableElement epsgCodeTableElem = epsgCode.getTableElement();
        if (epsgCodeTableElem != null && epsgCodeTableElem instanceof Column) {
            final int row = ((Column) epsgCodeTableElem).getFirstLineWithData();
            final int column = ((Column) epsgCodeTableElem).getNumber();
            final String cellValue = TableController.getInstance().getValueAt(row, column);
            epsgString = epsgString.concat(cellValue);
        } else if (epsgCode.getValue() > 0) {
            epsgString = epsgString.concat(Integer.toString(epsgCode.getValue()));
        }
        try {
            LOG.debug(String.format("Trying to decode CRS from EPSG string : '%s'", epsgString));
            final CoordinateReferenceSystem crs = CRS.decode(epsgString);
            // 2 check for axis Z -> if present -> yes
            LOG.debug(String.format("CRS decoded to '%s' with %s dimensions.",
                    crs.getName(),
                    crs.getCoordinateSystem().getDimension()));
            if (crs.getCoordinateSystem().getDimension() == 3) {
                return true;
            }
            // TODO what about user feedback?
        } catch (final FactoryException e) {
            LOG.error(String.format("Exception thrown: %s",
                    e.getMessage()),
                    e);
        }
        return false;
    }

    /**
     * <p>setMissingComponents.</p>
     *
     * @param components a {@link java.util.List} object.
     */
    public void setMissingComponents(final List<Component> components) {
        for (final Component c: components) {
            final MissingComponentPanel mcp = c.getMissingComponentPanel(position);
            mcp.setMissingComponent(c);
            missingComponentPanels.add(mcp);
        }
    }

    /**
     * <p>getMissingComponents.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Component> getMissingComponents() {
        final List<Component> components = new ArrayList<>();
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            components.add(mcp.getMissingComponent());
        }
        return components;
    }

    /**
     * <p>assignMissingComponentValues.</p>
     */
    public void assignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.assignValues();
        }
    }

    /**
     * <p>checkMissingComponentValues.</p>
     *
     * @return a boolean.
     */
    public boolean checkMissingComponentValues() {
        boolean ok;
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            ok = mcp.checkValues();
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>unassignMissingComponentValues.</p>
     */
    public void unassignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.unassignValues();
        }
    }

    /**
     * <p>forThis.</p>
     *
     * @param featureOfInterestPosition a {@link org.n52.sos.importer.model.table.Cell} object.
     * @return a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public Position forThis(final Cell featureOfInterestPosition) {
        final Latitude latitude = position.getLatitude().forThis(featureOfInterestPosition);
        final Longitude longitude = position.getLongitude().forThis(featureOfInterestPosition);
        final Height height = position.getHeight().forThis(featureOfInterestPosition);
        final EPSGCode epsgCode = position.getEPSGCode().forThis(featureOfInterestPosition);
        return new Position(latitude, longitude, height, epsgCode);
    }

    /**
     * <p>markComponents.</p>
     */
    public void markComponents() {
        if (position.getLatitude() != null) {
            position.getLatitude().mark();
        }

        if (position.getLongitude() != null) {
            position.getLongitude().mark();
        }

        if (position.getHeight() != null) {
            position.getHeight().mark();
        }

        if (position.getEPSGCode() != null) {
            position.getEPSGCode().mark();
        }
    }

    /**
     * <p>mergePositions.</p>
     */
    public void mergePositions() {
        LOG.info("Merge Positions");
        final List<Position> positions = ModelStore.getInstance().getPositions();
        final ArrayList<Position> mergedPositions = new ArrayList<>();
        while (!positions.isEmpty()) {
            final Position p1 = positions.get(0);
            positions.remove(p1);
            // create tmp list from left over ps
            final List<Position> list2 = new ArrayList<>(positions);
            final Iterator<Position> pIter = list2.iterator();
            while (pIter.hasNext()) {
                final Position p2 = pIter.next();
                if (p1.getGroup().equals(p2.getGroup())) {
                    merge(p1, p2);
                    positions.remove(p2);
                }
            }
            mergedPositions.add(p1);
        }
        mergedPositions.trimToSize();
        ModelStore.getInstance().setPositions(mergedPositions);
    }

    /**
     * <p>merge.</p>
     *
     * @param position1 a {@link org.n52.sos.importer.model.position.Position} object.
     * @param position2 a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public void merge(final Position position1, final Position position2) {
        if (position1.getLatitude() == null && position2.getLatitude() != null) {
            position1.setLatitude(position2.getLatitude());
        }

        if (position1.getLongitude() == null && position2.getLongitude() != null) {
            position1.setLongitude(position2.getLongitude());
        }

        if (position1.getHeight() == null && position2.getHeight() != null) {
            position1.setHeight(position2.getHeight());
        }

        if (position1.getEPSGCode() == null && position2.getEPSGCode() != null) {
            position1.setEPSGCode(position2.getEPSGCode());
        }
    }

}
