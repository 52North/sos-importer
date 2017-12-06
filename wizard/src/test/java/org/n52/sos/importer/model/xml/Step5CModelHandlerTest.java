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
package org.n52.sos.importer.model.xml;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.model.Step5cModel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Column;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument;

/**
 * <p>Step5CModelHandlerTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step5CModelHandlerTest {

    /**
     * <p>testStoringOfAltitudeWithUnit.</p>
     */
    @Test
    public void testStoringOfAltitudeWithUnit() {
        //given
        final double altitude = 52.0;
        final String unit = "m";
        final String pattern = "LAT LON";
        final int positionColumnId = 3;
        Position position = new Position(
                new Latitude(new Column(positionColumnId, 0), pattern),
                new Longitude(new Column(positionColumnId, 0), pattern),
                new Height(altitude, unit),
                new EPSGCode(4326));
        position.setGroup("A");
        final Step5cModel stepModel = new Step5cModel(position);
        final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column column =
                importConf.addNewCsvMetadata().addNewColumnAssignments().addNewColumn();
        column.setNumber(positionColumnId);
        column.setType(TypeDocument.Type.POSITION);
        Metadata meta = column.addNewMetadata();
        meta.setKey(Key.GROUP);
        meta.setValue("A");
        meta = column.addNewMetadata();
        meta.setKey(Key.TYPE);
        meta.setValue("COMBINATION");
        meta = column.addNewMetadata();
        meta.setKey(Key.PARSE_PATTERN);
        meta.setValue(pattern);

        //when
        new Step5cModelHandler().handleModel(stepModel, importConf);

        // then
        Metadata altitudeMetadata = null;
        for (Metadata metadata :
                importConf.getCsvMetadata().getColumnAssignments().getColumnArray(0).getMetadataArray()) {
            if (metadata.getKey().equals(Key.POSITION_ALTITUDE)) {
                altitudeMetadata = metadata;
                break;
            }
        }
        Assert.assertThat(altitudeMetadata, Is.is(CoreMatchers.notNullValue()));
        Assert.assertThat(altitudeMetadata.getValue(), Is.is(altitude + " " + unit));
    }

}
