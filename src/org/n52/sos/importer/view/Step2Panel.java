package org.n52.sos.importer.view;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Step2Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel columnSeparatorLabel = new JLabel("Column separator:"); //$NON-NLS-1$
	private final JLabel commentIndicatorLabel = new JLabel("Comment indicator:"); //$NON-NLS-1$
	private final JLabel textQualifierLabel = new JLabel("Text qualifier:"); //$NON-NLS-1
	
	private final JComboBox columnSeparatorCombobox = new JComboBox();
	private final JComboBox commentIndicatorCombobox = new JComboBox();
	private final JComboBox textQualifierCombobox = new JComboBox();
	
	private final JTextArea csvFileTextArea = new JTextArea(7, 30); 
	
	public Step2Panel() {
		super();

		csvFileTextArea.setEditable(false);		
		columnSeparatorCombobox.setEditable(true);
		commentIndicatorCombobox.setEditable(true);
		textQualifierCombobox.setEditable(true);
		
		JPanel csvSettingsPanel = new JPanel();
		csvSettingsPanel.setLayout(new GridLayout(5,2));
		csvSettingsPanel.add(columnSeparatorLabel);
		csvSettingsPanel.add(columnSeparatorCombobox);
		csvSettingsPanel.add(commentIndicatorLabel);
		csvSettingsPanel.add(commentIndicatorCombobox);
		csvSettingsPanel.add(textQualifierLabel);
		csvSettingsPanel.add(textQualifierCombobox);
		this.add(csvSettingsPanel);
		
		JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		this.add(scrollPane);
	}
	
	public String getSelectedColumnSeparator() {
		return (String) columnSeparatorCombobox.getSelectedItem();
	}
	
	public String getSelectedCommentIndicator() {
		return (String) commentIndicatorCombobox.getSelectedItem();
	}
	
	public String getSelectedTextQualifier() {
		return (String) textQualifierCombobox.getSelectedItem();
	}
	
	public void setSelectedColumnSeparator(String columnSeparator) {
		columnSeparatorCombobox.setSelectedItem(columnSeparator);
	}
	
	public void setSelectedCommentIndicator(String commentIndicator) {
		commentIndicatorCombobox.setSelectedItem(commentIndicator);
	}
	
	public void setSelectedTextQualifier(String textQualifier) {
		textQualifierCombobox.setSelectedItem(textQualifier);
	}
	
	public String getCSVFileContent() {
		return csvFileTextArea.getText();
	}
	
	public void setCSVFileContent(String content) {
		csvFileTextArea.setText(content);
		csvFileTextArea.setCaretPosition(0);	
	}
}
