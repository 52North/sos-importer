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
package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.table.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionControllerTest {

    private static final Logger logger =
            LoggerFactory.getLogger(PositionControllerTest.class);

    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        logger.debug("Start Test");
        final int firstLineWithData = 1;
        final String g = "1";
        //
        // first element
        final Column c1 = new Column(0, firstLineWithData);
        final Position p1 = new Position();
        p1.addCoordinate(new PositionComponent(Position.Id.COORD_0, c1, Position.Id.COORD_0.name()));
        p1.setGroup(g);
        //
        // second element
        final Column c2 = new Column(1, firstLineWithData);
        final Position p2 = new Position();
        p2.addCoordinate(new PositionComponent(Position.Id.COORD_1, c2, Position.Id.COORD_1.name()));
        p2.setGroup(g);
        //
        // add elements to modelstore
        final ModelStore ms = ModelStore.getInstance();
        ms.add(p1);
        ms.add(p2);
        //
        logger.debug("Example data created");
        //
        // Position controller
        final PositionController pc = new PositionController();
        pc.mergePositions();
    }

}
