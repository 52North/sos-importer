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

public class Step1Model implements StepModel{

	private String url, user, password, directory, filenameSchema, intervallUnit;
	private int intervallValue, feedingType;
	private String csvFilePath = "";
	
	public int getFeedingType() {
		return feedingType;
	}

	public void setFeedingType(int feedingType) {
		this.feedingType = feedingType;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public String getFilenameSchema() {
		return filenameSchema;
	}
	
	public void setFilenameSchema(String filenameSchema) {
		this.filenameSchema = filenameSchema;
	}
	
	public int getIntervallValue() {
		return intervallValue;
	}
	
	public void setIntervallValue(int intervallValue) {
		this.intervallValue = intervallValue;
	}
	
	public String getIntervallUnit() {
		return intervallUnit;
	}
	
	public void setIntervallUnit(String intervallUnit) {
		this.intervallUnit = intervallUnit;
	}
	
	public void setCSVFilePath(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}

	public String getCSVFilePath() {
		return csvFilePath;
	}
	
}
