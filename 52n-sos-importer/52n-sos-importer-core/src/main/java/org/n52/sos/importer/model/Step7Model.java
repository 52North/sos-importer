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

public class Step7Model implements StepModel {
	
	private String sosURL;
	
	private File configFile;

	private boolean generateOfferingFromSensorName;

	private String offering;

	public Step7Model(String sosURL, 
			File configFile,
			boolean generateOfferingFromSensorName,
			String offering) {
		this.sosURL = sosURL;
		this.configFile = configFile;
		this.generateOfferingFromSensorName = generateOfferingFromSensorName;
		this.offering = offering;
	}
	
	public Step7Model() {
		this(null,null,true,null);
	}

	public String getSosURL() {
		return sosURL;
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

	public boolean isGenerateOfferingFromSensorName() {
		return generateOfferingFromSensorName;
	}

	public void setGenerateOfferingFromSensorName(
			boolean generateOfferingFromSensorName) {
		this.generateOfferingFromSensorName = generateOfferingFromSensorName;
	}

	public String getOffering() {
		return offering;
	}

	public void setOffering(String offering) {
		this.offering = offering;
	}
}
