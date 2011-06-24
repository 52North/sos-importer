package org.n52.sos.importer.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.view.Step2Panel;

import au.com.bytecode.opencsv.CSVReader;

public class Step2Controller extends StepController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(Step2Controller.class);

	private Step2Model step2Model;
	
	private Step2Panel step2Panel;
	
	public Step2Controller(Step2Model step2Model) {
		this.step2Model = step2Model;
		step2Panel = new Step2Panel();
		
		load();
	}
	
	@Override
	public String getDescription() {
		return "Step 2: Import CSV file"; 
	}

	@Override
	public void back() {
		save();
		MainController.getInstance().setStepController(new Step1Controller());
	}
	
	@Override
	public void next() {
		Object[][] content = parseCSVFile();
		save();
		TableController.getInstance().setContent(content);
		MainController.getInstance().setStepController(new Step3Controller());
	}
	
	public void save() {
		String columnSeparator = step2Panel.getSelectedColumnSeparator();
		step2Model.setSelectedColumnSeparator(columnSeparator);
		String commentIndicator = step2Panel.getSelectedCommentIndicator();
		step2Model.setSelectedCommentIndicator(commentIndicator);
		String textQualifier = step2Panel.getSelectedTextQualifier();
		step2Model.setSelectedTextQualifier(textQualifier);
		String csvFileContent = step2Panel.getCSVFileContent();
		step2Model.setCSVFileContent(csvFileContent);
	}
	
	public void load() {
		String columnSeparator = step2Model.getSelectedColumnSeparator();
		step2Panel.setSelectedColumnSeparator(columnSeparator);
		String commentIndicator = step2Model.getSelectedCommentIndicator();
		step2Panel.setSelectedCommentIndicator(commentIndicator);
		String textQualifier = step2Model.getSelectedTextQualifier();
		step2Panel.setSelectedTextQualifier(textQualifier);
		String csvFileContent = step2Model.getCSVFileContent();
		step2Panel.setCSVFileContent(csvFileContent);
	}
	
	private Object[][] parseCSVFile() {
		Object[][] content = null;
		try {	
			String separator = step2Panel.getSelectedColumnSeparator();
			if (separator.equals("Tab")) separator = "\t"; 
			String quoteChar = step2Panel.getSelectedCommentIndicator();
			String escape = step2Panel.getSelectedTextQualifier();
			StringReader sr = new StringReader(step2Panel.getCSVFileContent());
			CSVReader reader = new CSVReader(sr, separator.charAt(0), quoteChar.charAt(0), escape.charAt(0));
			List<String[]> lines = reader.readAll();
			int rows = lines.size();
			String[] firstLine = lines.get(0);
			int columns = firstLine.length;
			content = new Object[rows][columns];

			for (int i = 0; i < rows; i++) {
				content[i] = lines.get(i);
			}
		} catch (IOException e) {
			logger.error("Error while parsing CSV file.", e);
		}
		return content;
	}

	@Override
	public JPanel getStepPanel() {
		return step2Panel;
	}
}
