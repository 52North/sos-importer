/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
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

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step4aModel;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Solves ambiguities in case there is more than one date&time column.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step4aModelHandler implements ModelHandler<Step4aModel> {

	private static final Logger logger = Logger.getLogger(Step4aModelHandler.class);
	
	@Override
	public void handleModel(Step4aModel s4aM,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		// for each element in s4aM.selectedRowsOrColumns -> set RelatedDateTimeGroup to s4aM.dateAndTimeModel.getGroup()
		for (int mvColumnId : s4aM.getSelectedRowsOrColumns()) {
			Column c = Helper.getColumnById(mvColumnId, sosImportConf);
			if (c.isSetRelatedDateTimeGroup() && logger.isDebugEnabled()) {
				String dateTimeGroup = c.getRelatedDateTimeGroup();
				logger.debug(String.format("Element RelatedDateTimeGroup already set to: %s",
						dateTimeGroup));
			}
			c.setRelatedDateTimeGroup(s4aM.getDateAndTimeModel().getGroup());
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Element RelatedDateTimeGroup set to: %s", 
						c.getRelatedDateTimeGroup()));
			}
		}
	}

}
