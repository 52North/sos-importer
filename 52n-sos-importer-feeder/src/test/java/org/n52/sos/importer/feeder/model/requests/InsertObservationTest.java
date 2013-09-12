/**
 * Copyright (C) 2013
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
package org.n52.sos.importer.feeder.model.requests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.Position;

public class InsertObservationTest {

	@Test public void
	shouldReturnTrueIfAltitudeIsAvailable()
	{
		final double alt = 2.0;
		final FeatureOfInterest foi = new FeatureOfInterest(null, null, new Position(new double[] {0.0, 1.0, alt},new String[] {"deg","deg","m"},4326));
		final InsertObservation insertObservation = new InsertObservation(null, foi, null, null, null, null, null, null);
		assertThat(insertObservation.isSetAltitudeValue(), is(true));
		assertThat(insertObservation.getAltitudeValue(),is(alt));
	}
	
	@Test public void
	shouldReturnFalseIfAltitudeIsNotAvailable()
	{
		final FeatureOfInterest foi = new FeatureOfInterest(null, null, new Position(new double[] {0.0, 1.0, Double.NEGATIVE_INFINITY},new String[] {"deg","deg",null},4326));
		InsertObservation insertObservation = new InsertObservation(null, foi, null, null, null, null, null, null);
		assertThat(insertObservation.isSetAltitudeValue(), is(false));
		
		insertObservation = new InsertObservation(null, null, null, null, null, null, null, null);
		assertThat(insertObservation.isSetAltitudeValue(), is(false));
	}
	
}
