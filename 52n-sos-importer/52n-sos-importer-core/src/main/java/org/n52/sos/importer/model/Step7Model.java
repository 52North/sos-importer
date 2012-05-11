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

/**
 * @author e.h.juerrens@52north.org
 *
 */
public class Step7Model implements StepModel {
	
	private String sosURL;
	
	private boolean saveConfig;
	
	private boolean directImport;
	
	private File configFile;

	private boolean generateOfferingFromSensorName;

	private String offering;

	public Step7Model(String sosURL, 
			boolean directImport, 
			boolean saveConfig,
			File configFile,
			boolean generateOfferingFromSensorName,
			String offering) {
		this.sosURL = sosURL;
		this.directImport = directImport;
		this.saveConfig = saveConfig;
		this.configFile = configFile;
		this.generateOfferingFromSensorName = generateOfferingFromSensorName;
		this.offering = offering;
	}
	
	public Step7Model() {
		this(null,false,false,null,true,null);
	}

	public String getSosURL() {
		return sosURL;
	}

	public boolean isSaveConfig() {
		return saveConfig;
	}

	public boolean isDirectImport() {
		return directImport;
	}
	
	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void setSosURL(String sosURL) {
		this.sosURL = sosURL;
	}

	public void setSaveConfig(boolean isSaveConfig) {
		this.saveConfig = isSaveConfig;
	}

	public void setDirectImport(boolean isDirectImport) {
		this.directImport = isDirectImport;
	}

	/**
	 * @return the generateOfferingFromSensorName
	 */
	public boolean isGenerateOfferingFromSensorName() {
		return generateOfferingFromSensorName;
	}

	/**
	 * @param generateOfferingFromSensorName the generateOfferingFromSensorName to set
	 */
	public void setGenerateOfferingFromSensorName(
			boolean generateOfferingFromSensorName) {
		this.generateOfferingFromSensorName = generateOfferingFromSensorName;
	}

	/**
	 * @return the offering
	 */
	public String getOffering() {
		return offering;
	}

	/**
	 * @param offering the offering to set
	 */
	public void setOffering(String offering) {
		this.offering = offering;
	}
}
