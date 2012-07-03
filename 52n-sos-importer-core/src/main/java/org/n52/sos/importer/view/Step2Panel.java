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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * offers settings for parsing the CSV file and 
 * displays a preview of the CSV file
 * @author Raimund, Eike
 *
 */
/*
 * To enable useHeader, search for USE_HEADER
 */
public class Step2Panel extends JPanel {
	
	private static final Logger logger = Logger.getLogger(Step2Panel.class);
	
	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel columnSeparatorCombobox;
	private final EditableJComboBoxPanel commentIndicatorCombobox;
	private final EditableJComboBoxPanel textQualifierCombobox;
	
	private JComboBox decimalSeparatorCombobox;
	
	private final JTextArea csvFileTextArea;
	private int csvFileRowCount = 0;
	
	private SpinnerNumberModel lineModel;
	private JSpinner firstDataJS;
	private final JLabel firstDataJL;
	
	private JLabel useHeaderJL;
	private JCheckBox useHeaderJCB;
	private JPanel panel;
	
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
					setCSVFileHighlight(number);
				} else {
					setCSVFileHighlight(number);
					// USE_HEADER
					/*if (number > 0) {
						useHeaderJCB.setEnabled(true);
						useHeaderJL.setVisible(true);
					} else {
						useHeaderJCB.setEnabled(false);
						useHeaderJCB.setSelected(false);
					}*/
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
		if (logger.isTraceEnabled()) {
			useHeaderJCB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.trace("useHeader state changed. is selected?" + 
							useHeaderJCB.isSelected());
				}
			});
		}
		useHeaderJCB.setSelected(false);
		// will be enabled if firstLineWithdata is set to > 0
		useHeaderJCB.setEnabled(false);
		JPanel useHeaderPanel = new JPanel();
		useHeaderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		useHeaderPanel.add(useHeaderJL);
		useHeaderPanel.add(useHeaderJCB);
		/*
		 * Decimal Separator
		 */
		JLabel decimalSeparatorLabel = new JLabel(Lang.l().step2DecimalSeparator() + " : ");
		String[] decimalSeparators = Constants.DECIMAL_SEPARATORS;
		decimalSeparatorCombobox = new JComboBox(decimalSeparators);
		decimalSeparatorCombobox.setSelectedIndex(0);
		JPanel decimalSeparatorPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) decimalSeparatorPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		decimalSeparatorPanel.add(decimalSeparatorLabel);
		decimalSeparatorPanel.add(decimalSeparatorCombobox);
		//
		//		
		//	CSV-setting panel
		//
		JPanel csvSettingsPanel = new JPanel();
		csvSettingsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		csvSettingsPanel.setLayout(new GridLayout(6,1));
		csvSettingsPanel.add(columnSeparatorCombobox);
		csvSettingsPanel.add(commentIndicatorCombobox);
		csvSettingsPanel.add(textQualifierCombobox);
		csvSettingsPanel.add(firstLineWithDataJPanel);
		// USE_HEADER uncomment next line to enable again
		// csvSettingsPanel.add(useHeaderPanel);
		csvSettingsPanel.add(decimalSeparatorPanel);
		//
		//	CSV text area
		//
		csvFileTextArea = new JTextArea(20, 70);
		csvFileTextArea.setEditable(false);	
		JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		JPanel csvDataPanel = new JPanel();
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		JLabel label = new JLabel(Lang.l().step2DataPreviewLabel());
		label.setFont(Constants.DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD);
		panel.add(label);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(csvSettingsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(csvDataPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(csvSettingsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(csvDataPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		GroupLayout gl_csvDataPanel = new GroupLayout(csvDataPanel);
		gl_csvDataPanel.setHorizontalGroup(
			gl_csvDataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_csvDataPanel.createSequentialGroup()
					.addGroup(gl_csvDataPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_csvDataPanel.setVerticalGroup(
			gl_csvDataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_csvDataPanel.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		csvDataPanel.setLayout(gl_csvDataPanel);
		setLayout(groupLayout);
	}
	
	public String getCommentIndicator() {
		return (String) commentIndicatorCombobox.getSelectedItem();
	}
	
	public void setCommentIndicator(String commentIndicator) {
		commentIndicatorCombobox.setSelectedItem(commentIndicator);
	}
	
	public String getDecimalSeparator() {
		return decimalSeparatorCombobox.getSelectedItem().toString();
	}
	
	public void setDecimalSeparator(String decimalSeparator) {
		decimalSeparatorCombobox.setSelectedItem(decimalSeparator);
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
