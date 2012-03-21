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
package org.n52.sos.importer.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x02.FirstLineWithDataDocument.FirstLineWithData;
import org.x52North.sensorweb.sos.importer.x02.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x02.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * In this class the XML model for an CSV file is stored for later re-use by 
 * another application.
 * @author e.h.juerrens@52north.org
 * @since 0.2
 * @version 
 *
 */
public class XMLModel {

	private SosImportConfiguration sosImpConf;
	
	private StepModel[] stepModells = new Step3aModel[1];
	
	private static final Logger logger = Logger.getLogger(XMLModel.class);

	/**
	 * Create a new and empty model
	 */
	public XMLModel() {
		this.sosImpConf = SosImportConfiguration.Factory.newInstance();
	}
	
	/**
	 * Create model based on an existing one
	 * @param sosImpConf
	 */
	public XMLModel(SosImportConfiguration sosImpConf) {
		this.sosImpConf = sosImpConf;
	}
	
	/**
	 * Create model based on xml file
	 * @param xmlFileWithModel the file containing the <code>XMLModel</code>
	 * @throws XmlException thrown while parsing the file &rarr; <code>XMLModel</code> is file is <b>not</b> valid.
	 * @throws IOException having any problems while reading file
	 */
	public XMLModel(File xmlFileWithModel) throws XmlException, IOException {
		SosImportConfigurationDocument sosImpConfDoc = SosImportConfigurationDocument.Factory.parse(xmlFileWithModel);
		this.sosImpConf = sosImpConfDoc.getSosImportConfiguration();
	}
	
	public boolean registerProvider(StepModel sm) {
		ArrayList<StepModel> sMs;
		//
		sMs = createArrayListFromArray(stepModells);
		boolean result = sMs.add(sm);
		saveProviders(sMs);
		//
		return result;
	}
	
	public boolean removeProvider(StepModel sm) {
		ArrayList<StepModel> provider;
		//
		provider = createArrayListFromArray(stepModells);
		boolean result = provider.remove(sm);
		saveProviders(provider);
		//
		return result;
	}
	
	/**
	 * Updates the model. Should be called when one of the providers has changed.
	 */
	public void updateModel() {
		if (logger.isTraceEnabled()) {
			logger.trace("updateModel()");
		}
		// check each provider and update the internal model
		if(stepModells != null && stepModells.length > 0) {
			for (StepModel sm : this.stepModells) {
				/*
				 * 
				 * get updates from Step1Model
				 * Provided information: DataFile.LocalFile.Path
				 *  
				 */
				if (sm instanceof Step1Model) {
					//
					Step1Model s1M = (Step1Model) sm;
					String path = s1M.getCSVFilePath();
					DataFile dF = this.sosImpConf.getDataFile();
					LocalFile lF = null;
					//
					if(dF == null) {
						dF = this.sosImpConf.addNewDataFile();
						lF = dF.addNewLocalFile();
					} else if(dF.isSetLocalFile()){
						lF = dF.getLocalFile();
					}
					if(path != null && !path.equals("")) {
						lF.setPath(path);
					} else {
						logger.error("empty path to CSV file in Step1Model");
					}
					/*
					 * 
					 * get updates from Step2Model
					 * Provided information: CsvMeta.Parameter.*, 
					 * 					CsvMeta.FirstLineWithData,
					 * 					CsvMeta.UseHeader
					 */
				} else if (sm instanceof Step2Model) {
					//
					Step2Model s2M = (Step2Model) sm;
					CsvMetadata cM = sosImpConf.getCsvMetadata();
					Parameter p = null;
					//
					if(cM == null) {
						cM = this.sosImpConf.addNewCsvMetadata();
						p = cM.addNewParameter();
					} else {
						p = cM.getParameter();
					}
					cM.setFirstLineWithData(s2M.getFirstLineWithData());
					cM.setUseHeader(s2M.isUseHeader());
					p.setCommentIndicator(s2M.getCommentIndicator());
					p.setElementSeparator(s2M.getColumnSeparator());
					p.setTextIndicator(s2M.getTextQualifier());
					/*
					 * 
					 * get updates from Step3aModel
					 * Provided information: 
					 */
				} else if (sm instanceof Step3aModel) {
					Step3aModel s3M = (Step3aModel) sm;

				}
			}
			this.validate();
		}
	}
	
	/**
	 * Should be called after final step to validate the final model.
	 * @return
	 */
	public boolean validate() {
		SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory.newInstance();
		doc.setSosImportConfiguration(sosImpConf);
		boolean modelValid = doc.validate();
		if(!modelValid) {
			logger.error("The model is not valid. Please update your values.");
		}
		return modelValid;
	}
	
	/*
	 * 
	 * 	Private Helper methods
	 * 
	 * 
	 */
	private ArrayList<StepModel> createArrayListFromArray(StepModel[] models) {
		ArrayList<StepModel> result;
		//
		result = new ArrayList<StepModel>(stepModells.length+1);
		for (StepModel stepModel : this.stepModells) {
			if(stepModel != null) {
				result.add(stepModel);
			}
		}
		result.trimToSize();
		//
		return result;
	}

	private void saveProviders(ArrayList<StepModel> aL) {
		aL.trimToSize();
		this.stepModells = aL.toArray(new StepModel[aL.size()]);
	}
	
}
