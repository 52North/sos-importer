package org.n52.sos.importer.view;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.config.EditableComboBoxItems;

public class Step2Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel columnSeparatorLabel = new JLabel("Column separator:");
	private final JLabel commentIndicatorLabel = new JLabel("Comment indicator:");
	private final JLabel textQualifierLabel = new JLabel("Text qualifier:");
	
	private final EditableJComboBoxPanel columnSeparatorCombobox;
	private final EditableJComboBoxPanel commentIndicatorCombobox;
	private final EditableJComboBoxPanel textQualifierCombobox;
	
	private final JTextArea csvFileTextArea = new JTextArea(7, 30); 
	
	public Step2Panel() {
		super();
		
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		columnSeparatorCombobox = new EditableJComboBoxPanel(items.getColumnSeparators());
		commentIndicatorCombobox = new EditableJComboBoxPanel(items.getCommentIndicators());
		textQualifierCombobox = new EditableJComboBoxPanel(items.getTextQualifiers());
		
		csvFileTextArea.setEditable(false);		
		
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
