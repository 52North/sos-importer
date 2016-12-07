/**
 * ﻿Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
import org.x52North.sensorweb.sos.importer.x04.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x04.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x04.TypeDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step5CModelHandlerTest {

	@Test
	public void testStoringOfAltitudeWithUnit() {
		//given
		final double altitude = 52.0;
		final String unit = "m";
		final String pattern = "LAT LON";
		final int positionColumnId = 3;
		Position position = new Position(
				new Latitude(new Column(positionColumnId, 0), pattern),
				new Longitude(new Column(positionColumnId, 0),pattern),
				new Height(altitude, unit),
				new EPSGCode(4326));
		position.setGroup("A");
		final Step5cModel stepModel = new Step5cModel(position);
		final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
		org.x52North.sensorweb.sos.importer.x04.ColumnDocument.Column column = importConf.addNewCsvMetadata().addNewColumnAssignments().addNewColumn();
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
		for (Metadata metadata : importConf.getCsvMetadata().getColumnAssignments().getColumnArray(0).getMetadataArray()) {
			if (metadata.getKey().equals(Key.POSITION_ALTITUDE)) {
				altitudeMetadata = metadata;
				break;
			}
		}
		Assert.assertThat(altitudeMetadata, Is.is(CoreMatchers.notNullValue()));
		Assert.assertThat(altitudeMetadata.getValue(), Is.is(altitude + " " + unit));
	}

}
