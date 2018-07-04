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
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;
import org.n52.sos.importer.view.position.MissingPositionComponentPanel;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles operations on Position objects
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class PositionController {

    private static final Logger LOG = LoggerFactory.getLogger(PositionController.class);

    private Position position;

    private final List<MissingComponentPanel> missingComponentPanels;

    public PositionController() {
        position = new Position();
        missingComponentPanels = new ArrayList<>();
    }

    public PositionController(final Position position) {
        this.position = position;
        missingComponentPanels = new ArrayList<>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public void assignPattern(final String pattern, final TableElement tableElement) {
        LOG.info("Assign pattern " + pattern + " to " + position + " in " + tableElement);

        if (pattern.contains(Position.Id.COORD_0.name())) {
            position.addCoordinate(new PositionComponent(Position.Id.COORD_0, tableElement, pattern));
        }
        if (pattern.contains(Position.Id.COORD_1.name())) {
            position.addCoordinate(new PositionComponent(Position.Id.COORD_1, tableElement, pattern));
        }
        if (pattern.contains(Position.Id.COORD_2.name())) {
            position.addCoordinate(new PositionComponent(Position.Id.COORD_2, tableElement, pattern));
        }
        if (pattern.contains(Position.EPSG)) {
            position.setEPSGCode(new EPSGCode(tableElement, pattern));
        }
    }

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

    public List<MissingComponentPanel> getMissingComponentPanels() {
        if (!missingComponentPanels.isEmpty()) {
            return missingComponentPanels;
        }
        List<MissingComponentPanel> tmpPanelList = new ArrayList<>(3);
        for (Id id : Position.Id.values()) {
            if (position.getCoordinate(id) == null &&
                    (!id.equals(Id.COORD_2) || is3DCRS(position.getEPSGCode()))) {
                tmpPanelList.add(new MissingPositionComponentPanel(id, position));
            }
        }
        if (position.getEPSGCode() == null) {
            MissingEPSGCodePanel mecp = new MissingEPSGCodePanel(position);
            mecp.addCoordinatePanels(tmpPanelList);
            missingComponentPanels.add(mecp);

        }
        missingComponentPanels.addAll(tmpPanelList);
        return missingComponentPanels;
    }

    /**
     * @return returns <tt>true</tt>,
     *      if height is allowed, or
     *      we could not say:"it is not allowed" because of parsing errors...
     */
    private boolean is3DCRS(EPSGCode epsgCode) {
        if (epsgCode == null) {
            return true;
        }
        // try to create gt-CRS from code and check for height axis
        // 1 try to create CRS object
        String epsgString = Position.EPSG.concat(":");
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

    public void setMissingComponents(final List<Component> components) {
        MissingEPSGCodePanel mecp = null;
        for (final Component c: components) {
            final MissingComponentPanel mcp = c.getMissingComponentPanel(position);
            mcp.setMissingComponent(c);
            if (mcp instanceof MissingEPSGCodePanel) {
                mecp = (MissingEPSGCodePanel) mcp;
            }
            missingComponentPanels.add(mcp);
        }
        if (mecp != null) {
            mecp.addCoordinatePanels(missingComponentPanels);
        }
    }

    public List<Component> getMissingComponents() {
        final List<Component> components = new ArrayList<>();
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            components.add(mcp.getMissingComponent());
        }
        return components;
    }

    public void assignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.assignValues();
        }
    }

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

    public void unassignMissingComponentValues() {
        for (final MissingComponentPanel mcp: missingComponentPanels) {
            mcp.unassignValues();
        }
    }

    public Position forThis(final Cell foiPosition) {
        final PositionComponent latitude = position.getCoordinate(Id.COORD_0).forThis(Id.COORD_0, foiPosition);
        final PositionComponent longitude = position.getCoordinate(Id.COORD_1).forThis(Id.COORD_1, foiPosition);
        final PositionComponent height = position.getCoordinate(Id.COORD_2).forThis(Id.COORD_2, foiPosition);
        final EPSGCode epsgCode = position.getEPSGCode().forThis(foiPosition);
        return new Position(epsgCode, latitude, longitude, height);
    }

    public void markComponents() {
        for (Id id : Position.Id.values()) {
            if (position.getCoordinate(id) != null) {
                position.getCoordinate(id).mark();
            }
        }

        if (position.getEPSGCode() != null) {
            position.getEPSGCode().mark();
        }
    }

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

    public void merge(final Position position1, final Position position2) {
        for (Id id : Position.Id.values()) {
            if (position1.getCoordinate(id) == null && position2.getCoordinate(id) != null) {
                position1.addCoordinate(position2.getCoordinate(id));
            }
        }

        if (position1.getEPSGCode() == null && position2.getEPSGCode() != null) {
            position1.setEPSGCode(position2.getEPSGCode());
        }
    }

}
