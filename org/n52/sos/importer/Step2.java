package org.n52.sos.importer;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Step2 extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel columnSeparatorLabel = new JLabel("Column separator:");
	private final JLabel rowSeparatorLabel = new JLabel("Row separator:");
	private final JLabel decimalSeparatorLabel = new JLabel("Decimal separator:");
	private final JLabel thousandsSeparatorLabel = new JLabel("Thousands separator:");
	private final JLabel textQualifierLabel = new JLabel("Text qualifier:");
	
	private final String[] columnSeparatorValues = { ";", ",", ":", "Tab"};
	private final String[] rowSeparatorValues = { "\n" };
	private final String[] decimalSeparatorValues = { ",", "." };
	private final String[] thousandsSeparatorValues = { ".", ",", "'", " " };
	private final String[] textQualifierValues = { "\"\"" };	
	
	private final JComboBox columnSeparatorCombobox = new JComboBox(columnSeparatorValues);
	private final JComboBox rowSeparatorCombobox = new JComboBox(rowSeparatorValues);
	private final JComboBox decimalSeparatorCombobox = new JComboBox(decimalSeparatorValues);
	private final JComboBox thousandsSeparatorCombobox = new JComboBox(thousandsSeparatorValues);
	private final JComboBox textQualifierCombobox = new JComboBox(textQualifierValues);
	
	private final JTextArea csvFileTextArea = new JTextArea(7, 30); 
	
	public Step2(MainFrame mainFrame, String csvFileContent) {
		super(mainFrame);
		
		csvFileTextArea.setEditable(false);
		csvFileTextArea.setText(csvFileContent);
		
		JPanel csvSettingsPanel = new JPanel();
		csvSettingsPanel.setLayout(new GridLayout(5,2));
		csvSettingsPanel.add(columnSeparatorLabel);
		csvSettingsPanel.add(columnSeparatorCombobox);
		csvSettingsPanel.add(rowSeparatorLabel);
		csvSettingsPanel.add(rowSeparatorCombobox);
		csvSettingsPanel.add(decimalSeparatorLabel);
		csvSettingsPanel.add(decimalSeparatorCombobox);
		csvSettingsPanel.add(thousandsSeparatorLabel);
		csvSettingsPanel.add(thousandsSeparatorCombobox);
		csvSettingsPanel.add(textQualifierLabel);
		csvSettingsPanel.add(textQualifierCombobox);
		this.add(csvSettingsPanel);
		this.add(csvFileTextArea);
	}

	@Override
	protected String getDescription() {
		return "Step 2: Import CSV file";
	}

	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void next() {
		// TODO Auto-generated method stub
		
	}
}
