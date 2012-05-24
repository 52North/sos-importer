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

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.importer.model.xml.Step1ModelHandler;
import org.n52.sos.importer.model.xml.Step2ModelHandler;
import org.n52.sos.importer.model.xml.Step3ModelHandler;
import org.n52.sos.importer.model.xml.Step4bModelHandler;
import org.n52.sos.importer.model.xml.Step5aModelHandler;
import org.n52.sos.importer.model.xml.Step5cModelHandler;
import org.n52.sos.importer.model.xml.Step6aModelHandler;
import org.n52.sos.importer.model.xml.Step6bModelHandler;
import org.n52.sos.importer.model.xml.Step6bSpecialModelHandler;
import org.n52.sos.importer.model.xml.Step6cModelHandler;
import org.n52.sos.importer.model.xml.Step7ModelHandler;
import org.x52North.sensorweb.sos.importer.x02.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x02.LocalFileDocument.LocalFile;
import org.x52North.sensorweb.sos.importer.x02.RemoteFileDocument.RemoteFile;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * In this class the XML model for an CSV file is stored for later re-use by
 * another application.
 * 
 * @author e.h.juerrens@52north.org
 * @since 0.2
 * @version
 * 
 */
public class XMLModel {

	private static final Logger logger = Logger.getLogger(XMLModel.class);

	private SosImportConfiguration sosImpConf;

	private StepModel[] stepModells = new Step3Model[1];

	/**
	 * Create a new and empty model
	 */
	public XMLModel() {
		this.sosImpConf = SosImportConfiguration.Factory.newInstance();
	}

	/**
	 * Create model based on xml file
	 * 
	 * @param xmlFileWithModel
	 *            the file containing the <code>XMLModel</code>
	 * @throws XmlException
	 *             thrown while parsing the file &rarr; <code>XMLModel</code>
	 *             file is <b>not</b> valid.
	 * @throws IOException
	 *             having any problems while reading file
	 */
	public XMLModel(File xmlFileWithModel) throws XmlException, IOException {
		SosImportConfigurationDocument sosImpConfDoc = SosImportConfigurationDocument.Factory
				.parse(xmlFileWithModel);
		this.sosImpConf = sosImpConfDoc.getSosImportConfiguration();
	}

	/**
	 * Create model based on an existing one
	 * 
	 * @param sosImpConf
	 */
	public XMLModel(SosImportConfiguration sosImpConf) {
		this.sosImpConf = sosImpConf;
	}

	public String getFileName() {
		if (logger.isTraceEnabled()) {
			logger.trace("getFileName()");
		}
		DataFile df = this.sosImpConf.getDataFile();
		String result = null;
		if (df != null) {
			if (df.isSetLocalFile()) {
				LocalFile lf = df.getLocalFile();
				result = lf.getPath();
				result = result.substring(result.lastIndexOf(File.separatorChar)+1);
			} else if (df.isSetRemoteFile()) {
				RemoteFile rf = df.getRemoteFile();
				result = rf.getURL();
				result = result.substring(result.lastIndexOf("/")+1);
			}
		}
		return result;
	}

	public boolean registerProvider(StepModel sm) {
		if (logger.isTraceEnabled()) {
			logger.trace("registerProvider(" + 
					(sm.getClass().getSimpleName()==null?
							sm.getClass():
								sm.getClass().getSimpleName()) +
					")");
		}
		//
		ArrayList<StepModel> sMs;
		//
		sMs = createArrayListFromArray(stepModells);
		boolean result = sMs.add(sm);
		saveProvidersInArray(sMs);
		//
		return result;
	}

	public boolean removeProvider(StepModel sm) {
		if (logger.isTraceEnabled()) {
			logger.trace("removeProvider(" +  
					(sm.getClass().getSimpleName()==null? 
							sm.getClass(): 
								sm.getClass().getSimpleName()) + 
								")");
		}
		//
		ArrayList<StepModel> provider;
		//
		provider = createArrayListFromArray(stepModells);
		boolean result = provider.remove(sm);
		saveProvidersInArray(provider);
		//
		return result;
	}

	public boolean save(File file) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("save(" + file!=null?file.getName():file + ")");
		}
		//
		// we save only valid configurations
		// if(this.validate()) {
		//
		// check write access to file
		if (file != null) {
			if (!file.exists()) {
				if (logger.isDebugEnabled()) {
					logger.debug("File " + file
							+ " does not exist. Try to create it.");
				}
				if (!file.createNewFile()) {
					logger.error("Could not create file " + file);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("File " + file + " created");
					}
				}
			}
			if (file.isFile()) {
				if (file.canWrite()) {
					SosImportConfigurationDocument doc = 
						SosImportConfigurationDocument.Factory.newInstance();
					XmlOptions xmlOpts = new XmlOptions()
						.setSavePrettyPrint()
						.setSavePrettyPrintIndent(4)
						.setUseDefaultNamespace();
					doc.setSosImportConfiguration(sosImpConf);
					doc.save(file, xmlOpts);
					return true;
				} else {
					logger.error("model not saved: could not write to file: "
							+ file);
				}
			} else {
				logger.error("model not saved: file is not a file: " + file);
			}
		} else {
			logger.error("model not saved: file is null");
		}
		// }
		return false;
	}

	/**
	 * Updates the model. Should be called when one of the providers has
	 * changed.
	 */
	public void updateModel() {
		if (logger.isTraceEnabled()) {
			logger.trace("updateModel()");
		}
		/*
		 * check each provider and update the internal model
		 * using ModelHandler for each StepModel
		 */
		
		if (stepModells != null && stepModells.length > 0) {
			//
			for (StepModel sm : this.stepModells) {
				//
				if (sm instanceof Step1Model) {
					//
					Step1Model s1M = (Step1Model) sm;
					new Step1ModelHandler().handleModel(s1M, sosImpConf);
					//
				} else if (sm instanceof Step2Model) {
					//
					Step2Model s2M = (Step2Model) sm;
					new Step2ModelHandler().handleModel(s2M, sosImpConf);
					//
				} else if (sm instanceof Step3Model) {
					//
					Step3Model s3M = (Step3Model) sm;
					new Step3ModelHandler().handleModel(s3M, sosImpConf);
					//
				} else if (sm instanceof Step4bModel) {
					//
					Step4bModel s4bM = (Step4bModel) sm;
					new Step4bModelHandler().handleModel(s4bM, sosImpConf);
					//
				} else if (sm instanceof Step5aModel) {
					//
					Step5aModel s5aM = (Step5aModel) sm;
					new Step5aModelHandler().handleModel(s5aM, sosImpConf);
					//
				} else if (sm instanceof Step5cModel) {
					//
					Step5cModel s5cM = (Step5cModel) sm;
					new Step5cModelHandler().handleModel(s5cM, sosImpConf);
					//
				} else if (sm instanceof Step6aModel) {
					//
					Step6aModel s6aM = (Step6aModel) sm;
					new Step6aModelHandler().handleModel(s6aM, sosImpConf);
					//
				} else if (sm instanceof Step6bModel) {
					//
					Step6bModel s6bM = (Step6bModel) sm;
					new Step6bModelHandler().handleModel(s6bM, sosImpConf);
					//
				} else if (sm instanceof Step6bSpecialModel) {
					//
					Step6bSpecialModel s6bSM = (Step6bSpecialModel) sm;
					new Step6bSpecialModelHandler().handleModel(s6bSM, sosImpConf);
					//
				} else if (sm instanceof Step6cModel) {
					//
					Step6cModel s6cM = (Step6cModel) sm;
					new Step6cModelHandler().handleModel(s6cM, sosImpConf);
					//
				} else if (sm instanceof Step7Model) {
					//
					Step7Model s7M = (Step7Model) sm;
					new Step7ModelHandler().handleModel(s7M, sosImpConf);
				}
			}
		}
	}

	/**
	 * Should be called after final step to validate the final model.
	 * 
	 * @return
	 */
	public boolean validate() {
		if (logger.isTraceEnabled()) {
			logger.trace("validate()");
		}
		//
		SosImportConfigurationDocument doc = SosImportConfigurationDocument.Factory
				.newInstance();
		doc.setSosImportConfiguration(sosImpConf);
		boolean modelValid = doc.validate();
		if (!modelValid) {
			logger.error("The model is not valid. Please update your values.");
		}
		return modelValid;
	}

	/*
	 * Private Helper methods for provider and model handling
	 */
	private ArrayList<StepModel> createArrayListFromArray(StepModel[] models) {
		if (logger.isTraceEnabled()) {
			logger.trace("\tcreateArrayListFromArray()");
		}
		//
		ArrayList<StepModel> result;
		//
		result = new ArrayList<StepModel>(stepModells.length + 1);
		for (StepModel stepModel : this.stepModells) {
			if (stepModel != null) {
				result.add(stepModel);
			}
		}
		result.trimToSize();
		//
		return result;
	}

	private void saveProvidersInArray(ArrayList<StepModel> aL) {
		if (logger.isTraceEnabled()) {
			logger.trace("\tsaveProvidersInArray()");
		}
		//
		aL.trimToSize();
		this.stepModells = aL.toArray(new StepModel[aL.size()]);
	}

}
