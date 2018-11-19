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
package org.n52.sos.importer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.table.Column;

public class PositionControllerTest {

    @Test
    public void testMergePositions() {
        int firstLineWithData = 1;
        String groupId = "1";
        EPSGCode epsgCode = new EPSGCode(4326);
        //
        // first element
        Column c1 = new Column(0, firstLineWithData);
        Position p1 = new Position();
        p1.setGroup(groupId);
        p1.setEPSGCode(epsgCode);
        PositionComponent coord0 = new PositionComponent(Position.Id.COORD_0, c1, Position.Id.COORD_0.name());
        p1.addCoordinate(coord0);
        //
        // second element
        Column c2 = new Column(1, firstLineWithData);
        Position p2 = new Position();
        p2.setGroup(groupId);
        p2.setEPSGCode(epsgCode);
        PositionComponent coord1 = new PositionComponent(Position.Id.COORD_1, c2, Position.Id.COORD_1.name());
        p2.addCoordinate(coord1);
        //
        // add elements to modelstore
        ModelStore ms = ModelStore.getInstance();
        ms.add(p1);
        ms.add(p2);
        // Position controller
        PositionController pc = new PositionController();
        pc.mergePositions();

        assertThat(ms.getPositions(), is(notNullValue()));
        assertThat(ms.getPositions().size(), is(1));
        Position pos = ms.getPositions().get(0);
        assertThat(pos.getGroup(), is(groupId));
        assertThat(pos.getEPSGCode(), is(epsgCode));
        assertThat(pos.getCoordinate(Position.Id.COORD_0), is(coord0));
        assertThat(pos.getCoordinate(Position.Id.COORD_1), is(coord1));
    }

    @Test
    public void mergeShouldThrowExceptionWhenOneGroupIsNull() {
        int firstLineWithData = 1;
        String groupId = "1";
        EPSGCode epsgCode = new EPSGCode(4326);
        //
        // first element
        Column c1 = new Column(0, firstLineWithData);
        Position p1 = new Position();
        p1.setGroup(groupId);
        p1.setEPSGCode(epsgCode);
        PositionComponent coord0 = new PositionComponent(Position.Id.COORD_0, c1, Position.Id.COORD_0.name());
        p1.addCoordinate(coord0);
        //
        // second element
        Column c2 = new Column(1, firstLineWithData);
        Position p2 = new Position();
        p2.setGroup(groupId);
        p2.setEPSGCode(epsgCode);
        PositionComponent coord1 = new PositionComponent(Position.Id.COORD_1, c2, Position.Id.COORD_1.name());
        p2.addCoordinate(coord1);
        //
        // add elements to modelstore
        ModelStore ms = ModelStore.getInstance();
        ms.add(p1);
        ms.add(p2);
        // Position controller
        PositionController pc = new PositionController();
        pc.mergePositions();
    }

}
