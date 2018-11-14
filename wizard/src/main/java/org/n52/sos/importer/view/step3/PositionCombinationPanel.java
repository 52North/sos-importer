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
package org.n52.sos.importer.view.step3;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.wizard.utils.ToolTips;

/**
 * selection panel in step 3 for position combinations
 *
 * @author Raimund
 */
public class PositionCombinationPanel extends CombinationPanel {

    private static final String DEG = "";

    private static final long serialVersionUID = 1L;

    private Position position;

    /**
     * <p>Constructor for PositionCombinationPanel.</p>
     *
     * @param containerPanel a {@link javax.swing.JPanel} object.
     * @param firstLineWithData a int.
     */
    public PositionCombinationPanel(JPanel containerPanel, int firstLineWithData) {
        super(containerPanel, firstLineWithData);
    }

    @Override
    public String[] getGroupItems() {
        return ComboBoxItems.getInstance().getPositionGroups();
    }

    @Override
    public DefaultComboBoxModel<String> getPatterns() {
        return EditableComboBoxItems.getInstance().getPositionPatterns();
    }

    @Override
    public Object getTestValue() {
        PositionComponent latitude = new PositionComponent(Id.COORD_0, 52.4, DEG);
        PositionComponent longitude = new PositionComponent(Id.COORD_1, 7.52, DEG);
        PositionComponent height = new PositionComponent(Id.COORD_2, 126.2, "m");
        EPSGCode epsgCode = new EPSGCode(4979);
        Position p = new Position(epsgCode, latitude, longitude, height);
        return p;
    }

    @Override
    public Combination getCombination() {
        if (position == null) {
            position = new Position();
        }
        return position;
    }

    @Override
    public String getPatternToolTip() {
        return ToolTips.get(ToolTips.POSITION_PATTERNS);
    }

    @Override
    public String getGroupToolTip() {
        return ToolTips.get(ToolTips.POSITION_GROUPS);
    }

    @Override
    public void assign(TableElement tableElement) {
        String[] part = getSelection().split(Constants.SEPARATOR_STRING);
        String pattern = part[0];
        String group = part[1];

        Position newPosition = new Position();
        newPosition.setGroup(group);
        PositionController pc = new PositionController(newPosition);
        pc.assignPattern(pattern, tableElement);
        ModelStore.getInstance().add(newPosition);
    }

    @Override
    public void unAssign(TableElement tableElement) {
        Position positionToRemove = null;
        for (Position p: ModelStore.getInstance().getPositions()) {
            PositionComponent coord0 = p.getCoordinate(Id.COORD_0);
            PositionComponent coord1 = p.getCoordinate(Id.COORD_1);
            PositionComponent coord2 = p.getCoordinate(Id.COORD_2);
            EPSGCode epsg = p.getEPSGCode();

            if (coord0 != null && tableElement.equals(coord0.getTableElement()) ||
                    coord1 != null && tableElement.equals(coord1.getTableElement()) ||
                    coord2 != null && tableElement.equals(coord2.getTableElement()) ||
                    epsg != null && tableElement.equals(epsg.getTableElement())) {
                positionToRemove = p;
                break;
            }
        }

        ModelStore.getInstance().remove(positionToRemove);
    }
}
