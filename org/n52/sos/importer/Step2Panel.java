package org.n52.sos.importer;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import au.com.bytecode.opencsv.CSVReader;

public class Step2Panel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel columnSeparatorLabel = new JLabel("Column separator:");
	private final JLabel commentIndicatorLabel = new JLabel("Comment indicator:");
	private final JLabel textQualifierLabel = new JLabel("Text qualifier:");
	private final JLabel decimalSeparatorLabel = new JLabel("Decimal separator:");
	private final JLabel thousandsSeparatorLabel = new JLabel("Thousands separator:");
	
	private final String[] columnSeparatorValues = { ";", ",", ":", "Tab"};
	private final String[] commentIndicatorValues = { "#" };
	private final String[] textQualifierValues = { "\"", "'" };	
	private final String[] decimalSeparatorValues = { ",", "." };
	private final String[] thousandsSeparatorValues = { ".", ",", "'", " " };
	
	private final JComboBox columnSeparatorCombobox = new JComboBox(columnSeparatorValues);
	private final JComboBox commentIndicatorCombobox = new JComboBox(commentIndicatorValues);
	private final JComboBox textQualifierCombobox = new JComboBox(textQualifierValues);
	private final JComboBox decimalSeparatorCombobox = new JComboBox(decimalSeparatorValues);
	private final JComboBox thousandsSeparatorCombobox = new JComboBox(thousandsSeparatorValues);
	
	private final JTextArea csvFileTextArea = new JTextArea(7, 30); 
	
	public Step2Panel(MainFrame mainFrame) {
		super(mainFrame);
		this.loadSettings();

		csvFileTextArea.setEditable(true);
		
		JPanel csvSettingsPanel = new JPanel();
		csvSettingsPanel.setLayout(new GridLayout(5,2));
		csvSettingsPanel.add(columnSeparatorLabel);
		csvSettingsPanel.add(columnSeparatorCombobox);
		csvSettingsPanel.add(commentIndicatorLabel);
		csvSettingsPanel.add(commentIndicatorCombobox);
		csvSettingsPanel.add(textQualifierLabel);
		csvSettingsPanel.add(textQualifierCombobox);
		csvSettingsPanel.add(decimalSeparatorLabel);
		csvSettingsPanel.add(decimalSeparatorCombobox);
		csvSettingsPanel.add(thousandsSeparatorLabel);
		csvSettingsPanel.add(thousandsSeparatorCombobox);
		this.add(csvSettingsPanel);
		
		JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		this.add(scrollPane);
	}
	
	public void setCSVFileContent(String csvFileContent) {
		csvFileTextArea.setText(csvFileContent);
		csvFileTextArea.setCaretPosition(0);
	}
	
	private Object[][] parseCSVFile() {
		Object[][] content = null;
		try {	
			String separator = (String) columnSeparatorCombobox.getSelectedItem();
			if (separator.equals("Tab")) separator = "\t";
			String quoteChar = (String) commentIndicatorCombobox.getSelectedItem();
			String escape = (String) textQualifierCombobox.getSelectedItem();
			StringReader sr = new StringReader(csvFileTextArea.getText());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	@Override
	protected String getDescription() {
		return "Step 2: Import CSV file";
	}

	@Override
	protected void back() {
		saveSettings();
		getMainFrame().setStepPanel(getMainFrame().getStep1Panel());
	}

	@Override
	protected void next() {
		Object[][] content = parseCSVFile();
		saveSettings();
		getMainFrame().getTablePanel().setTableContent(content);
		getMainFrame().setStepPanel(getMainFrame().getStep3Panel());
	}
	
	protected void saveSettings() {
		String columnSeparator = (String) columnSeparatorCombobox.getSelectedItem();
		Settings.setColumnSeparator(columnSeparator);
		String commentIndicator = (String) commentIndicatorCombobox.getSelectedItem();
		Settings.setCommentIndicator(commentIndicator);
		String textQualifier = (String) textQualifierCombobox.getSelectedItem();
		Settings.setTextQualifier(textQualifier);
		String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
		Settings.setThousandsSeparator(thousandsSeparator);
		String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
		Settings.setDecimalSeparator(decimalSeparator);
	}

	@Override
	protected void loadSettings() {
		if (Settings.getColumnSeparator() != null)
			columnSeparatorCombobox.setSelectedItem(Settings.getColumnSeparator());
		if (Settings.getCommentIndicator() != null)
			commentIndicatorCombobox.setSelectedItem(Settings.getCommentIndicator());
		if (Settings.getTextQualifier() != null) 
			textQualifierCombobox.setSelectedItem(Settings.getTextQualifier());
		if (Settings.getThousandsSeparator() != null)
			thousandsSeparatorCombobox.setSelectedItem(Settings.getThousandsSeparator());
		if (Settings.getDecimalSeparator() != null)
			decimalSeparatorCombobox.setSelectedItem(Settings.getDecimalSeparator());
	}
}
