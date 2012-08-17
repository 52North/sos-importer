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
package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step2Panel;
import org.n52.sos.importer.view.i18n.Lang;

import au.com.bytecode.opencsv.CSVReader;

/**
 * offers settings for parsing the CSV file
 * @author Raimund, Eike
 *
 */
public class Step2Controller extends StepController {

	private static final Logger logger = Logger.getLogger(Step2Controller.class);

	private Step2Model step2Model;
	
	private Step2Panel step2Panel;
	
	public Step2Controller(Step2Model step2Model) {
		this.step2Model = step2Model;
	}
	
	@Override
	public String getDescription() {
		return Lang.l().step2Description(); 
	}
	
	@Override
	public boolean isFinished() {
		String columnSeparator = step2Panel.getColumnSeparator();
		if (columnSeparator == null || columnSeparator.equals(""))
			return false;
		String commentIndicator = step2Panel.getCommentIndicator();
		if (commentIndicator == null || commentIndicator.equals(""))
			return false;
		String textQualifier = step2Panel.getTextQualifier();
		if (textQualifier == null || textQualifier.equals(""))
			return false;
		int firstLineWithData = step2Panel.getFirstLineWithData();
		if (firstLineWithData < 0 || 
				firstLineWithData > (step2Model.getCsvFileRowRount()-1))
			return false;
		
		return true;
	}
	
	@Override
	public StepController getNextStepController() {
		Object[][] content = parseCSVFile();
		TableController.getInstance().setContent(content);
		TableController.getInstance().setFirstLineWithData(
				this.step2Model.getFirstLineWithData());
		return new Step3Controller(0,
				this.step2Model.getFirstLineWithData(),
				this.step2Model.getUseHeader());
	}
	
	@Override
	public void loadSettings() {
		if (logger.isTraceEnabled()) {
			logger.trace("loadSettings()");
		}
		step2Panel = new Step2Panel(step2Model.getCsvFileRowRount());
		
		String columnSeparator = step2Model.getColumnSeparator();
		step2Panel.setColumnSeparator(columnSeparator);
		
		String commentIndicator = step2Model.getCommentIndicator();
		step2Panel.setCommentIndicator(commentIndicator);
		
		String textQualifier = step2Model.getTextQualifier();
		step2Panel.setTextQualifier(textQualifier);
		
		int firstLineWithData = step2Model.getFirstLineWithData();
		step2Panel.setFirstLineWithData(firstLineWithData);
		
		String csvFileContent = step2Model.getCSVFileContent();
		step2Panel.setCSVFileContent(csvFileContent);
		
		boolean useHeader = step2Model.getUseHeader();
		step2Panel.setUseHeader(useHeader);
		step2Panel.setCSVFileHighlight(firstLineWithData);
		
		char decimalSeparator = step2Model.getDecimalSeparator();
		step2Panel.setDecimalSeparator(decimalSeparator+"");
	}
	
	@Override
	public void saveSettings() {
		if (logger.isTraceEnabled()) {
			logger.trace("saveSettings()");
		}
		String columnSeparator = step2Panel.getColumnSeparator();
		step2Model.setColumnSeparator(columnSeparator);
		
		String commentIndicator = step2Panel.getCommentIndicator();
		step2Model.setCommentIndicator(commentIndicator);
		
		String textQualifier = step2Panel.getTextQualifier();
		step2Model.setTextQualifier(textQualifier);
		
		int firstLineWithData = step2Panel.getFirstLineWithData();
		if(firstLineWithData < 0 || firstLineWithData > (step2Model.getCsvFileRowRount()-1)) {
			logger.info("FirstLineWithData is to large. Set to 0");
			step2Model.setFirstLineWithData(0);
		} else {
			step2Model.setFirstLineWithData(firstLineWithData);
		}
		
		boolean useHeader = step2Panel.getUseHeader();
		step2Model.setUseHeader(useHeader);
		
		String csvFileContent = step2Panel.getCSVFileContent();
		step2Model.setCSVFileContent(csvFileContent);
		
		String decimalSeparator = step2Panel.getDecimalSeparator();
		step2Model.setDecimalSeparator(decimalSeparator.charAt(0));
		// Update global decimal separator
		Constants.DECIMAL_SEPARATOR = decimalSeparator.charAt(0);
		if (Constants.DECIMAL_SEPARATOR == '.') {
			Constants.THOUSANDS_SEPARATOR = ',';
		} else {
			Constants.THOUSANDS_SEPARATOR = '.';
		}
		
		step2Panel = null;
	}
	
	public String convertSpaceSeparatedText(String text, String separator) {
		StringBuilder replacedText = new StringBuilder();
		StringReader sr = new StringReader(text);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				line = replaceWhiteSpace(line, separator);
				replacedText.append(line + "\n");
			}
		} catch (IOException e) {
			logger.info("Error while parsing space-separated file", e);
		}
		return replacedText.toString();
	}
	
	/**
	 * replaces any whitespace in the text by the given separator
	 * @param text
	 * @param replacement
	 * @return
	 */
	public String replaceWhiteSpace(String text, String separator) {
		StringBuilder replacedText = new StringBuilder();
		boolean lastCharacterWasAWhiteSpace = false;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (Character.isWhitespace(ch)) {
				if (lastCharacterWasAWhiteSpace) continue;
				else {
					replacedText.append(separator);
					lastCharacterWasAWhiteSpace = true;
				}
			} else {
				replacedText.append(ch);
				lastCharacterWasAWhiteSpace = false;
			}
		}
		return replacedText.toString();
	}

	@Override
	public JPanel getStepPanel() { return step2Panel; }

	/* (non-Javadoc)
	 * @see org.n52.sos.importer.controller.StepController#isNecessary()
	 * this step is always required
	 */
	@Override
	public boolean isNecessary() { return true; }

	@Override
	public StepController getNext() { return null; }
	
	@Override
	public boolean isStillValid() {
		//TODO: check whether the CSV file has changed
		return false;
	}

	@Override
	public StepModel getModel() {
		return this.step2Model;
	}

	private Object[][] parseCSVFile() {
		Object[][] content = null;
		String csvFileContent = step2Model.getCSVFileContent();
		String separator = step2Model.getColumnSeparator();
		String quoteChar = step2Model.getCommentIndicator();
		String escape = step2Model.getTextQualifier();
		int firstLineWithData = step2Model.getFirstLineWithData();
		boolean useHeader = step2Model.getUseHeader();
		
		logger.info("Parse CSV file: " +
				"column separator: '"    + separator         + "', " +
				"comment indicator: '"   + quoteChar         + "', " +
				"text qualifier: '"      + escape            + "', " +
				"first line with data: " + firstLineWithData + "; "  +  
				"use header? " + useHeader);
		
		try {	
			if (separator.equals("Tab")) {
				separator = "\t"; 
			} else if (separator.equals(Constants.SPACE_STRING)) {
				separator = ";";
				csvFileContent = convertSpaceSeparatedText(csvFileContent, separator);
			}
			StringReader sr = new StringReader(csvFileContent);
			CSVReader reader = new CSVReader(sr, separator.charAt(0), quoteChar.charAt(0), escape.charAt(0));
			List<String[]> lines = reader.readAll();
			int rows = lines.size();
			String[] firstLine = lines.get(0);
			int columns = firstLine.length;
			content = new String[rows][columns];
	
			for (int i = 0; i < rows; i++) {
				content[i] = lines.get(i);
			}
		} catch (IOException e) {
			logger.error("Error while parsing CSV file.", e);
		}
		return content;
	}
}
