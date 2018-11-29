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
package org.n52.sos.importer.model.xml;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.net.URI;

import org.junit.Test;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.x52North.sensorweb.sos.importer.x05.CoordinateDocument.Coordinate;
import org.x52North.sensorweb.sos.importer.x05.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x05.ResourceType;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.SpatialResourceType;

public class Step6cModelHandlerTest {

    @Test
    public void shouldCreateFeatureOfInterestElementIfCompletelyManual() {
        double valueCoord0 = -25.511275;
        double valueCoord1 = -49.369742;
        String unit = "deg";
        Position position = new Position(new EPSGCode(4326),
                new PositionComponent(Position.Id.COORD_0, valueCoord0, unit),
                new PositionComponent(Position.Id.COORD_1, valueCoord1, unit));
        String uri = "Passauna-Platform";
        String name = new String(uri);
        FeatureOfInterest foi = new FeatureOfInterest();
        foi.setName(uri);
        foi.setURI(URI.create(uri));
        foi.setPosition(position);
        Step6cModel model = new Step6cModel(foi, null);
        SosImportConfiguration sosImportConf = SosImportConfiguration.Factory.newInstance();
        sosImportConf.addNewCsvMetadata().addNewColumnAssignments().addNewColumn().addNewRelatedFOI().setIdRef(foi.getXMLId());

        new Step6cModelHandler().handleModel(model, sosImportConf);

        assertThat(sosImportConf.isSetAdditionalMetadata(), is(true));
        assertThat(sosImportConf.getAdditionalMetadata().sizeOfFeatureOfInterestArray(), is(1));
        FeatureOfInterestType foiXB = sosImportConf.getAdditionalMetadata().getFeatureOfInterestArray(0);
        assertThat(foiXB.isNil(), is(false));
        ResourceType resource = foiXB.getResource();
        assertThat(resource, is(notNullValue()));
        assertThat(resource, is(instanceOf(SpatialResourceType.class)));
        SpatialResourceType spatialResource = (SpatialResourceType) resource;
        assertThat(spatialResource.getURI().getStringValue(), is(uri));
        assertThat(spatialResource.getName(), is(name));
        assertThat(spatialResource.getID(), is(foi.getXMLId()));
        assertThat(spatialResource.getPosition(), is(notNullValue()));
        org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position posXB = spatialResource.getPosition();
        assertThat(posXB.isSetEPSGCode(), is(true));
        assertThat(posXB.getEPSGCode(), is(position.getEPSGCode().getValue()));
        assertThat(posXB.isSetGroup(), is(false));
        assertThat(posXB.sizeOfCoordinateArray(), is(2));
        Coordinate coordinate0 = posXB.getCoordinateArray(0);
        assertThat(coordinate0.getAxisAbbreviation(), is("Lat"));
        assertThat(coordinate0.getDoubleValue(), is(valueCoord0));
        assertThat(coordinate0.getUnit(), is(unit));
        Coordinate coordinate1 = posXB.getCoordinateArray(1);
        assertThat(coordinate1.getAxisAbbreviation(), is("Long"));
        assertThat(coordinate1.getDoubleValue(), is(valueCoord1));
        assertThat(coordinate1.getUnit(), is(unit));
    }
}
