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
import org.n52.sos.importer.model.Step2Model;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Get updates from Step2Model Provided information:
 * <ul>
 * <li>CsvMeta.Parameter.*</li>
 * <li>CsvMeta.FirstLineWithData</li>
 * <li>CsvMeta.UseHeader</li>
 * </ul>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2ModelHandler implements ModelHandler<Step2Model> {
	
	private static final Logger logger = Logger.getLogger(Step2ModelHandler.class);

	@Override
	public void handleModel(Step2Model stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		CsvMetadata cM = sosImportConf.getCsvMetadata();
		Parameter p = null;
		//
		if (cM == null) {
			cM = sosImportConf.addNewCsvMetadata();
			p = cM.addNewParameter();
		} else {
			p = cM.getParameter();
		}
		if (p == null) {
			p = cM.addNewParameter();
		}
		cM.setFirstLineWithData(stepModel.getFirstLineWithData());
		cM.setUseHeader(stepModel.getUseHeader());
		cM.setDecimalSeparator(stepModel.getDecimalSeparator()+"");
		p.setCommentIndicator(stepModel.getCommentIndicator());
		p.setColumnSeparator(stepModel.getColumnSeparator());
		p.setTextIndicator(stepModel.getTextQualifier());
	}

}
