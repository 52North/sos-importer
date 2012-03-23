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
package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * offers settings for parsing the CSV file and 
 * displays a preview of the CSV file
 * @author Raimund, Eike
 *
 */
public class Step2Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel columnSeparatorCombobox;
	private final EditableJComboBoxPanel commentIndicatorCombobox;
	private final EditableJComboBoxPanel textQualifierCombobox;
	
	private final JTextArea csvFileTextArea = new JTextArea(20, 70);
	private int csvFileRowCount = 0;
	
	private SpinnerNumberModel lineModel;
	private JSpinner firstDataJS;
	private final JLabel firstDataJL;
	
	private JLabel useHeaderJL;
	private JCheckBox useHeaderJCB;
	
	public Step2Panel(final int csvFileRowCount) {
		super();
		//
		this.csvFileRowCount = csvFileRowCount;
		//
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		columnSeparatorCombobox = new EditableJComboBoxPanel(items.getColumnSeparators(), "Column separator", ToolTips.get("ColumnSeparator"));
		commentIndicatorCombobox = new EditableJComboBoxPanel(items.getCommentIndicators(), "Comment indicator", ToolTips.get("CommentIndicator"));
		textQualifierCombobox = new EditableJComboBoxPanel(items.getTextQualifiers(), "Text qualifier", ToolTips.get("TextQualifier"));
		//
		// FirstLineWithData
		//
		lineModel = new SpinnerNumberModel(0, 0, this.csvFileRowCount-1, 1);
		firstDataJS = new JSpinner(lineModel);
		/*
		firstDataJS.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int number = lineModel.getNumber().intValue();
				if(number < 0) {
					lineModel.setValue(0);
				} else if (number > (csvFileRowCount-1)){
					lineModel.setValue((csvFileRowCount-1));
				}
			}
		});
		*/
		firstDataJL = new JLabel("First Line with data:");
		JPanel firstLineWithDataJPanel = new JPanel();
		firstLineWithDataJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		firstLineWithDataJPanel.add(firstDataJL);
		firstLineWithDataJPanel.add(firstDataJS);
		//
		//	useHeader Checkbox
		//
		useHeaderJL = new JLabel("Parse Header?");
		useHeaderJCB = new JCheckBox();
		useHeaderJCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub generated on 23.03.2012 around 10:51:36
				
			}
		});
		useHeaderJCB.setSelected(false);
		useHeaderJCB.setEnabled(true);
		JPanel useHeaderPanel = new JPanel();
		useHeaderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		useHeaderPanel.add(useHeaderJL);
		useHeaderPanel.add(useHeaderJCB);
		//
		//		
		//	CSV-setting panel
		//
		JPanel csvSettingsPanel = new JPanel();
		csvSettingsPanel.setLayout(new GridLayout(5,1));
		csvSettingsPanel.add(columnSeparatorCombobox);
		csvSettingsPanel.add(commentIndicatorCombobox);
		csvSettingsPanel.add(textQualifierCombobox);
		csvSettingsPanel.add(firstLineWithDataJPanel);
		csvSettingsPanel.add(useHeaderPanel);
		//
		//	CSV text area
		//
		csvFileTextArea.setEditable(false);	
		JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		//
		//	Final add
		//
		this.add(csvSettingsPanel);
		this.add(scrollPane);
	}
	
	public String getCommentIndicator() {
		return (String) commentIndicatorCombobox.getSelectedItem();
	}
	
	public void setCommentIndicator(String commentIndicator) {
		commentIndicatorCombobox.setSelectedItem(commentIndicator);
	}

	public String getColumnSeparator() {
		return (String) columnSeparatorCombobox.getSelectedItem();
	}

	public void setColumnSeparator(String columnSeparator) {
		columnSeparatorCombobox.setSelectedItem(columnSeparator);
	}

	public String getCSVFileContent() {
		// remove line numbers from each row before returning data
		String txt = csvFileTextArea.getText();
		StringTokenizer tok = new StringTokenizer(txt,"\n");
		StringBuffer buf = new StringBuffer(txt.length());
		String tmp;
		while(tok.hasMoreTokens()) {
			tmp = tok.nextToken();
			tmp = tmp.substring(tmp.indexOf(":")+2) + "\n";
			buf.append(tmp);
			tmp = "";
		}
		buf.trimToSize();
		return buf.toString();
	}

	public void setCSVFileContent(String content) {
		// add line numbers to content
		StringTokenizer tok = new StringTokenizer(content,"\n");
		StringBuffer buf = new StringBuffer(content.length());
		String tmp = "", contentWithNumbers;
		int count = 0;
		int maxLevel = ("" + this.csvFileRowCount).length();
		int levelOfCount = ("" + count).length();
		while(tok.hasMoreTokens()) {
			for (int i = levelOfCount; i < maxLevel; i++) {
				tmp = " " + tmp;
			}
			tmp = tmp + count + ": " + tok.nextToken() + "\n";
			buf.append(tmp);
			// 
			//	preparation for next round
			tmp = "";
			count++;
			levelOfCount = ("" + count).length();
		}
		buf.trimToSize();
		contentWithNumbers = buf.toString(); 
		csvFileTextArea.setText(contentWithNumbers);
		csvFileTextArea.setCaretPosition(0);
	}

	/**
	 * @return user input or <code>-1</code> if invalid input is defined
	 */
	public int getFirstLineWithData() {
		return lineModel.getNumber().intValue();
	}

	public void setFirstLineWithData(int firstLineWithData) {
		lineModel.setValue(firstLineWithData);
	}
	
	public String getTextQualifier() {
		return (String) textQualifierCombobox.getSelectedItem();
	}

	public void setTextQualifier(String textQualifier) {
		textQualifierCombobox.setSelectedItem(textQualifier);
	}
		
	public boolean getUseHeader() {
		return this.useHeaderJCB.isSelected();
	}
	
	public void setUseHeader(boolean useHeader) {
		this.useHeaderJCB.setSelected(useHeader);
	}
}
