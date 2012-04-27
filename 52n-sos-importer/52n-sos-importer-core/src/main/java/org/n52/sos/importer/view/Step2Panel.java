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

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.apache.log4j.Logger;
import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.Constants;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * offers settings for parsing the CSV file and 
 * displays a preview of the CSV file
 * @author Raimund, Eike
 *
 */
public class Step2Panel extends JPanel {
	
	private static final Logger logger = Logger.getLogger(Step2Panel.class);
	
	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel columnSeparatorCombobox;
	private final EditableJComboBoxPanel commentIndicatorCombobox;
	private final EditableJComboBoxPanel textQualifierCombobox;
	
	private final JTextArea csvFileTextArea;
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
		columnSeparatorCombobox = new EditableJComboBoxPanel(
				items.getColumnSeparators(),
				Lang.l().step2ColumnSeparator(), 
				ToolTips.get(ToolTips.COLUMN_SEPARATOR));
		commentIndicatorCombobox = new EditableJComboBoxPanel(
				items.getCommentIndicators(),
				Lang.l().step2CommentIndicator(),
				ToolTips.get(ToolTips.COMMENT_INDICATOR));
		textQualifierCombobox = new EditableJComboBoxPanel(
				items.getTextQualifiers(),
				Lang.l().step2TextQualifier(),
				ToolTips.get(ToolTips.TEXT_QUALIFIER));
		//
		// FirstLineWithData
		//
		lineModel = new SpinnerNumberModel(0, 0, this.csvFileRowCount-1, 1);
		firstDataJS = new JSpinner(lineModel);
		// Highlight the current selected line in the file preview
		firstDataJS.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int number = lineModel.getNumber().intValue();
				if(number < 0) {
					lineModel.setValue(0);
				} else if (number > (csvFileRowCount-1)){
					lineModel.setValue((csvFileRowCount-1));
					setFirstLineWithData(number);
				} else {
					setCSVFileHighlight(number);
				}
			}
		});
		firstDataJL = new JLabel(Lang.l().step2FirstLineWithData() + " :");
		JPanel firstLineWithDataJPanel = new JPanel();
		firstLineWithDataJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		firstLineWithDataJPanel.add(firstDataJL);
		firstLineWithDataJPanel.add(firstDataJS);
		//
		//	useHeader Checkbox
		//
		useHeaderJL = new JLabel(Lang.l().step2ParseHeader() + "?");
		useHeaderJCB = new JCheckBox();
		useHeaderJCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// do nothing, state of check box will be saved during step 
				// controller switch
				if (logger.isDebugEnabled()) {
					logger.debug("useHeader state changed. is selected?" + 
							useHeaderJCB.isSelected());
				}
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
		this.csvFileTextArea = new JTextArea(20, 70);
		this.csvFileTextArea.setEditable(false);	
		JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		JPanel csvDataPanel = new JPanel();
		csvDataPanel.setLayout(new BoxLayout(csvDataPanel, BoxLayout.Y_AXIS));
		csvDataPanel.add(new JLabel(Lang.l().step2DataPreviewLabel()));
		csvDataPanel.add(scrollPane);
		//
		//	Final add
		//
		this.add(csvSettingsPanel);
		this.add(csvDataPanel);
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
			tmp = tmp.substring(tmp.indexOf(Constants.RAW_DATA_SEPARATOR)+2) + "\n";
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
			tmp = tmp + count + Constants.RAW_DATA_SEPARATOR + " " + tok.nextToken() + "\n";
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
	
	public void setCSVFileHighlight(int number) {
		if (logger.isTraceEnabled()) {
			logger.trace("setCSVFileHighlight()");
		}
		try {
			csvFileTextArea.getHighlighter().removeAllHighlights();
			if (number > 0) {
				int lineStart = csvFileTextArea.getLineStartOffset(0);
				int lineEnd = csvFileTextArea.getLineEndOffset(number-1);
				HighlightPainter painter = new DefaultHighlighter.
						DefaultHighlightPainter(
								Constants.DEFAULT_HIGHLIGHT_COLOR);
				csvFileTextArea.getHighlighter().addHighlight(lineStart, lineEnd, painter);
			}
		} catch (BadLocationException e1) {
			logger.error("Exception thrown: " + e1.getMessage(), e1);
		}
	}

	/**
	 * @return user input or <code>-1</code> if invalid input is defined
	 */
	public int getFirstLineWithData() {
		return lineModel.getNumber().intValue();
	}

	public void setFirstLineWithData(int firstLineWithData) {
		lineModel.setValue(firstLineWithData);
		this.setCSVFileHighlight(firstLineWithData);
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
