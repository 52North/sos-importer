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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x02.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x02.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
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
	
	private StepModel[] stepModells = new Step3Model[1];
	
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
		saveProvidersInArray(sMs);
		//
		return result;
	}
	
	public boolean removeProvider(StepModel sm) {
		ArrayList<StepModel> provider;
		//
		provider = createArrayListFromArray(stepModells);
		boolean result = provider.remove(sm);
		saveProvidersInArray(provider);
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
			//
			for (StepModel sm : this.stepModells) {
				//
				if (sm instanceof Step1Model) {
					//
					Step1Model s1M = (Step1Model) sm;
					handleStep1Model(s1M);
					//
				} else if (sm instanceof Step2Model) {
					//
					Step2Model s2M = (Step2Model) sm;
					handleStep2Model(s2M);
					//
				} else if (sm instanceof Step3Model) {
					//
					Step3Model s3M = (Step3Model) sm;
					handleStep3Model(s3M);
					//
				}
			}
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

	/**
	 * Get updates from Step1Model
	 * Provided information: <ul>
	 * 		<li>DataFile.LocalFile.Path</li></ul>
	 * @param s1M instance of {@linkplain org.n52.sos.importer.model.Step1Model}
	 */
	private void handleStep1Model(Step1Model s1M) {
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
	}

	/**
	 * Get updates from Step2Model
	 * Provided information: <ul>
	 * 		<li>CsvMeta.Parameter.*</li>
	 * 		<li>CsvMeta.FirstLineWithData</li>
	 * 		<li>CsvMeta.UseHeader</li></ul>
	 * @param s2M instance of {@linkplain org.n52.sos.importer.model.Step2Model}
	 */
	private void handleStep2Model(Step2Model s2M) {
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
		cM.setUseHeader(s2M.getUseHeader());
		p.setCommentIndicator(s2M.getCommentIndicator());
		p.setElementSeparator(s2M.getColumnSeparator());
		p.setTextIndicator(s2M.getTextQualifier());
	}

	/**
	 * Get updates from Step3Model
	 * Provided information for each column:<ul>
	 * 		<li>column index</li>
	 * 		<li>column type</li>
	 * 		<li>the type depending metadata</li></ul>
	 * Allowed column types are:<ul>
	 * 		<li>DO_NOT_EXPORT</li>
	 * 		<li>MEASURED_VALUE</li>
	 * 		<li>DATE_TIME</li>
	 * 		<li>POSITION</li>
	 * 		<li>FOI</li>
	 * 		<li>OBSERVED_PROPERTY</li>
	 * 		<li>UOM</li>
	 * 		<li>SENSOR</li></ul>
	 * The metadata consists of key value pairs. The allowed keys are:<ul>
	 * 		<li>TYPE</li>
	 * 		<li>GROUP</li>
	 * 		<li>PARSE_PATTERN</li>
	 * 		<li>DECIMAL_SEPARATOR</li>
	 * 		<li>THOUSANDS_SEPARATOR</li>
	 * 		<li>SOS_FOI</li>
	 * 		<li>SOS_OBSERVED_PROPERTY</li>
	 * 		<li>SOS_SENSOR</li>
	 * 		<li>OTHER</li></ul>
	 * For the latest configuration set-up and schema check: 
	 * 	{@link 52n-sos-importer-bindings/src/main/xsd/}
	 * @param s3M instance of {@linkplain org.n52.sos.importer.model.Step3Model}
	 */
	private void handleStep3Model(Step3Model s3M) {
		HashMap<Integer, List<String>> colAssignments = s3M.getAllSelections();
		Set<Integer> keySet = colAssignments.keySet();
		Integer[] keys = keySet.toArray(new Integer[keySet.size()]);
		for (int i = 0; i < keys.length; i++) {
			/*
			 * key = columnIndex
			 * List<String> contains:
			 * 			list.get(0) = type
			 * 			list.get(n) = endcoded meta data
			 */
		}
		// TODO implement (Store results from ..assign(..) in Step3*Model
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

	private void saveProvidersInArray(ArrayList<StepModel> aL) {
		aL.trimToSize();
		this.stepModells = aL.toArray(new StepModel[aL.size()]);
	}
	
}
