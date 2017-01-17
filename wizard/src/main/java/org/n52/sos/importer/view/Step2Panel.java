/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.view;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * offers settings for parsing the CSV file and
 * displays a preview of the CSV file
 *
 * @version $Id: $Id
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
/*
 * To enable useHeader, search for USE_HEADER
 */
public class Step2Panel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(Step2Panel.class);

	private static final long serialVersionUID = 1L;

	private EditableJComboBoxPanel columnSeparatorCombobox;
	private EditableJComboBoxPanel commentIndicatorCombobox;
	private EditableJComboBoxPanel textQualifierCombobox;

	private JComboBox<String> decimalSeparatorCombobox;

	private JTextArea csvFileTextArea;
	private int csvFileRowCount = 0;

	private SpinnerNumberModel lineModel;
	private JSpinner firstDataJS;
	private JLabel firstDataJL;

	private JLabel useHeaderJL;
	private JCheckBox useHeaderJCB;

	private int firstLineWithDataTmp = -1;
	private JCheckBox isSampleBasedCheckBox;

	private JPanel startRegExPanel;
	private JTextField startRegExTF;

	private JPanel dateOffsetPanel;
	private JSpinner dateOffset;
	private SpinnerNumberModel dateOffsetModel;

	private JTextField dateExtractionRegExTF;
	private JPanel dateExtractionRegExPanel;

	private JTextField datePatternTF;
	private JPanel datePatternPanel;

	private SpinnerNumberModel dataOffsetModel;
	private JSpinner dataOffset;
	private JPanel dataOffsetPanel;

	private SpinnerNumberModel sampleSizeOffsetModel;
	private JSpinner sampleSizeOffset;
	private JPanel sampleSizeOffsetPanel;

	private JTextField sampleSizeRegExTF;
	private JPanel sampleSizeRegExPanel;

	/**
	 * <p>Constructor for Step2Panel.</p>
	 *
	 * @param csvFileRowCount a int.
	 */
	public Step2Panel(final int csvFileRowCount) {
		super();
		this.csvFileRowCount = csvFileRowCount;

		final JPanel csvSettingsPanel = new JPanel();
		setupStyle(csvSettingsPanel);
		final EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		int gridY = 0;
		addColumnSeparator(csvSettingsPanel, items, gridY);
		addCommentIndicator(csvSettingsPanel, items, gridY++);
		addTextQualifier(csvSettingsPanel, items, gridY++);
		addFirstLineWithData(csvFileRowCount, csvSettingsPanel, gridY++);
		addDecimalSeparator(csvSettingsPanel, gridY++);
		addUseHeaderCheckbox(csvSettingsPanel, gridY/*++*/);
		addElementsForSampleBasedFiles(csvSettingsPanel, gridY++);
		final JPanel csvDataPanel = new JPanel();
		addCsvDataPanel(csvDataPanel);

		add(csvSettingsPanel);
		add(csvDataPanel);
	}

	private void addCsvDataPanel(final JPanel csvDataPanel) {
		csvFileTextArea = new JTextArea(20, 60);
		csvFileTextArea.setEditable(false);
		final JScrollPane scrollPane = new JScrollPane(csvFileTextArea);
		csvDataPanel.setBorder(new TitledBorder(null, Lang.l().step2DataPreviewLabel(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		csvDataPanel.setLayout(new BorderLayout(0, 0));
		csvDataPanel.add(scrollPane);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	private void addElementsForSampleBasedFiles(final JPanel csvSettingsPanel,
			int gridY) {
		addIsSampleBased(csvSettingsPanel, gridY++);
		addStartRegEx(csvSettingsPanel, gridY++);
		addDateOffset(csvSettingsPanel, gridY++);
		addDateExtractionRegEx(csvSettingsPanel, gridY++);
		addDatePattern(csvSettingsPanel, gridY++);
		addDataOffset(csvSettingsPanel, gridY++);
		addSampleSizeOffset(csvSettingsPanel, gridY++);
		addSampleSizeRegEx(csvSettingsPanel, gridY++);
	}

	private void setSampleBasedElementsEnabled(final boolean state) {
		startRegExPanel.setVisible(state);
		dateOffsetPanel.setVisible(state);
		dateExtractionRegExPanel.setVisible(state);
		datePatternPanel.setVisible(state);
		dataOffsetPanel.setVisible(state);
		sampleSizeOffsetPanel.setVisible(state);
		sampleSizeRegExPanel.setVisible(state);
		firstDataJS.setEnabled(!state);
	}

	private void addSampleSizeRegEx(final JPanel csvSettingsPanel,
			final int gridY) {
		sampleSizeRegExTF = new JTextField(28);
		sampleSizeRegExPanel = new JPanel();
		sampleSizeRegExPanel.setToolTipText(Lang.l().step2SampleBasedSampleSizeRegExTooltip());
		sampleSizeRegExPanel.setLayout(new GridLayout(2, 1));
		sampleSizeRegExPanel.add(new JLabel(Lang.l().step2SampleBasedSampleSizeRegExLabel() + ":"));
		sampleSizeRegExPanel.add(sampleSizeRegExTF);
		sampleSizeRegExPanel.setVisible(false);
		csvSettingsPanel.add(sampleSizeRegExPanel, simpleConstraints(gridY));
	}

	private void addSampleSizeOffset(final JPanel csvSettingsPanel,
			final int gridY) {
		sampleSizeOffsetModel = new SpinnerNumberModel(1, 1, csvFileRowCount, 1);
		sampleSizeOffsetModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				final int number = sampleSizeOffsetModel.getNumber().intValue();
				if(number < 0) {
					sampleSizeOffsetModel.setValue(0);
				} else if (number > (csvFileRowCount-1)){
					sampleSizeOffsetModel.setValue((csvFileRowCount-1));
				}
			}
		});
		sampleSizeOffset = new JSpinner(sampleSizeOffsetModel);
		sampleSizeOffsetPanel = new JPanel();
		sampleSizeOffsetPanel.setToolTipText(Lang.l().step2SampleBasedSampleSizeOffsetToolTip());
		sampleSizeOffsetPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		sampleSizeOffsetPanel.add(new JLabel(Lang.l().step2SampleBasedSampleSizeOffsetLabel() + ":"));
		sampleSizeOffsetPanel.add(sampleSizeOffset);
		sampleSizeOffsetPanel.setVisible(false);
		csvSettingsPanel.add(sampleSizeOffsetPanel, simpleConstraints(gridY));
	}

	private void addDataOffset(final JPanel csvSettingsPanel,
			final int gridY) {
		dataOffsetModel = new SpinnerNumberModel(1, 1, csvFileRowCount, 1);
		dataOffsetModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				final int number = dataOffsetModel.getNumber().intValue();
				if(number < 0) {
					dataOffsetModel.setValue(0);
				} else if (number > (csvFileRowCount-1)){
					dataOffsetModel.setValue((csvFileRowCount-1));
				}
			}
		});
		dataOffset = new JSpinner(dataOffsetModel);
		dataOffsetPanel = new JPanel();
		dataOffsetPanel.setToolTipText(Lang.l().step2SampleBasedDataOffsetToolTip());
		dataOffsetPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		dataOffsetPanel.add(new JLabel(Lang.l().step2SampleBasedDataOffsetLabel() + ":"));
		dataOffsetPanel.add(dataOffset);
		dataOffsetPanel.setVisible(false);
		csvSettingsPanel.add(dataOffsetPanel, simpleConstraints(gridY));
	}

	private void addDatePattern(final JPanel csvSettingsPanel,
			final int gridY) {
		datePatternTF = new JTextField(28);
		datePatternPanel = new JPanel();
		datePatternPanel.setToolTipText(Lang.l().step2SampleBasedDatePatternTooltip());
		datePatternPanel.setLayout(new GridLayout(2, 1));
		datePatternPanel.add(new JLabel(Lang.l().step2SampleBasedDatePatternLabel() + ":"));
		datePatternPanel.add(datePatternTF);
		datePatternPanel.setVisible(false);
		csvSettingsPanel.add(datePatternPanel, simpleConstraints(gridY));
	}

	private void addDateExtractionRegEx(final JPanel csvSettingsPanel,
			final int gridY) {
		dateExtractionRegExTF = new JTextField(28);
		dateExtractionRegExPanel = new JPanel();
		dateExtractionRegExPanel.setToolTipText(Lang.l().step2SampleBasedDateExtractionRegExTooltip());
		dateExtractionRegExPanel.setLayout(new GridLayout(2, 1));
		dateExtractionRegExPanel.add(new JLabel(Lang.l().step2SampleBasedDateExtractionRegExLabel() + ":"));
		dateExtractionRegExPanel.add(dateExtractionRegExTF);
		dateExtractionRegExPanel.setVisible(false);
		csvSettingsPanel.add(dateExtractionRegExPanel, simpleConstraints(gridY));
	}

	private void addIsSampleBased(final JPanel csvSettingsPanel, final int gridY) {
		// isSampleBasedFile
		isSampleBasedCheckBox = new JCheckBox();
		final JPanel isSampleBasedFilePanel = new JPanel();
		isSampleBasedFilePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		isSampleBasedFilePanel.add(new JLabel(Lang.l().step2IsSampleBased() + "?"));
		isSampleBasedFilePanel.add(isSampleBasedCheckBox);
		isSampleBasedCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (isSampleBasedCheckBox.isSelected()) {
					// TODO activateSampleBasedGuiElements
					setSampleBasedElementsEnabled(true);
					firstLineWithDataTmp = Integer.parseInt(firstDataJS.getValue().toString());
					firstDataJS.setValue(0);
				} else {
					// TODO disable all sample based elements
					setSampleBasedElementsEnabled(false);
					firstDataJS.setValue(firstLineWithDataTmp);
				}
			}

		});
		csvSettingsPanel.add(isSampleBasedFilePanel, simpleConstraints(gridY));
	}

	private void addStartRegEx(final JPanel csvSettingsPanel,
			final int gridY) {
		startRegExTF = new JTextField(28);
		startRegExPanel = new JPanel();
		startRegExPanel.setToolTipText(Lang.l().step2SampleBasedStartRegExTooltip());
		startRegExPanel.setLayout(new GridLayout(2, 1));
		startRegExPanel.add(new JLabel(Lang.l().step2SampleBasedStartRegExLabel() + ":"));
		startRegExPanel.add(startRegExTF);
		startRegExPanel.setVisible(false);
		csvSettingsPanel.add(startRegExPanel, simpleConstraints(gridY));
	}

	private void addDateOffset(final JPanel csvSettingsPanel,
			final int gridY) {
		dateOffsetModel = new SpinnerNumberModel(1, 1, csvFileRowCount, 1);
		dateOffsetModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				final int number = dateOffsetModel.getNumber().intValue();
				if(number < 0) {
					dateOffsetModel.setValue(0);
				} else if (number > (csvFileRowCount-1)){
					dateOffsetModel.setValue((csvFileRowCount-1));
				}
			}
		});
		dateOffset = new JSpinner(dateOffsetModel);
		dateOffsetPanel = new JPanel();
		dateOffsetPanel.setToolTipText(Lang.l().step2SampleBasedDateOffsetToolTip());
		dateOffsetPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		dateOffsetPanel.add(new JLabel(Lang.l().step2SampleBasedDateOffsetLabel() + ":"));
		dateOffsetPanel.add(dateOffset);
		dateOffsetPanel.setVisible(false);
		csvSettingsPanel.add(dateOffsetPanel, simpleConstraints(gridY));
	}

	private GridBagConstraints simpleConstraints(final int gridY) {
		final GridBagConstraints simpleConstraints = new GridBagConstraints();
		simpleConstraints.fill = GridBagConstraints.HORIZONTAL;
		simpleConstraints.anchor = GridBagConstraints.NORTHWEST;
		simpleConstraints.gridx = 0;
		simpleConstraints.gridy = gridY;
		return simpleConstraints;
	}

	private void addDecimalSeparator(final JPanel csvSettingsPanel, final int gridY) {
		final JLabel decimalSeparatorLabel = new JLabel(Lang.l().step2DecimalSeparator() + " : ");
		decimalSeparatorCombobox = new JComboBox<String>(Constants.DECIMAL_SEPARATORS);
		decimalSeparatorCombobox.setSelectedIndex(0);
		final JPanel decimalSeparatorPanel = new JPanel();
		decimalSeparatorPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
		decimalSeparatorPanel.add(decimalSeparatorLabel);
		decimalSeparatorPanel.add(decimalSeparatorCombobox);
		csvSettingsPanel.add(decimalSeparatorPanel, simpleConstraints(gridY));
	}

	private void addFirstLineWithData(final int csvFileRowCount,
			final JPanel csvSettingsPanel, final int gridY) {
		lineModel = new SpinnerNumberModel(0, 0, csvFileRowCount-1, 1);
		firstDataJS = new JSpinner(lineModel);
		// Highlight the current selected line in the file preview
		firstDataJS.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				final int number = lineModel.getNumber().intValue();
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
		final JPanel firstLineWithDataJPanel = new JPanel();
		firstLineWithDataJPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
		firstLineWithDataJPanel.add(firstDataJL);
		firstLineWithDataJPanel.add(firstDataJS);
		final GridBagConstraints gbc_firstLineWithDataJPanel = new GridBagConstraints();
		gbc_firstLineWithDataJPanel.fill = GridBagConstraints.BOTH;
		gbc_firstLineWithDataJPanel.insets = new Insets(0, 0, 5, 0);
		gbc_firstLineWithDataJPanel.gridx = 0;
		gbc_firstLineWithDataJPanel.gridy = gridY;
		csvSettingsPanel.add(firstLineWithDataJPanel, gbc_firstLineWithDataJPanel);
	}

	private void addTextQualifier(final JPanel csvSettingsPanel,
			final EditableComboBoxItems items, final int gridY) {
		textQualifierCombobox = new EditableJComboBoxPanel(
				items.getTextQualifiers(),
				Lang.l().step2TextQualifier(),
				ToolTips.get(ToolTips.TEXT_QUALIFIER));
		final GridBagConstraints gbc_textQualifierCombobox = new GridBagConstraints();
		gbc_textQualifierCombobox.fill = GridBagConstraints.BOTH;
		gbc_textQualifierCombobox.insets = new Insets(0, 0, 5, 0);
		gbc_textQualifierCombobox.gridx = 0;
		gbc_textQualifierCombobox.gridy = gridY;
		csvSettingsPanel.add(textQualifierCombobox, gbc_textQualifierCombobox);
	}

	private void addCommentIndicator(final JPanel csvSettingsPanel,
			final EditableComboBoxItems items, final int gridY) {
		commentIndicatorCombobox = new EditableJComboBoxPanel(
				items.getCommentIndicators(),
				Lang.l().step2CommentIndicator(),
				ToolTips.get(ToolTips.COMMENT_INDICATOR));
		final GridBagConstraints gbc_commentIndicatorCombobox = new GridBagConstraints();
		gbc_commentIndicatorCombobox.fill = GridBagConstraints.BOTH;
		gbc_commentIndicatorCombobox.insets = new Insets(0, 0, 5, 0);
		gbc_commentIndicatorCombobox.gridx = 0;
		gbc_commentIndicatorCombobox.gridy = gridY;
		csvSettingsPanel.add(commentIndicatorCombobox, gbc_commentIndicatorCombobox);
	}

	private void setupStyle(final JPanel csvSettingsPanel) {
		csvSettingsPanel.setBorder(new TitledBorder(null, Lang.l().metadata(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		final GridBagLayout gbl_csvSettingsPanel = new GridBagLayout();
		gbl_csvSettingsPanel.columnWidths = new int[]{239, 0};
		gbl_csvSettingsPanel.rowHeights = new int[]{25, 25, 25, 25, 25, 0};
		gbl_csvSettingsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_csvSettingsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		csvSettingsPanel.setLayout(gbl_csvSettingsPanel);
	}

	private void addColumnSeparator(final JPanel csvSettingsPanel,
			final EditableComboBoxItems items,
			final int gridY) {
		columnSeparatorCombobox = new EditableJComboBoxPanel(
				items.getColumnSeparators(),
				Lang.l().step2ColumnSeparator(),
				ToolTips.get(ToolTips.COLUMN_SEPARATOR));
		final GridBagConstraints gbc_columnSeparatorCombobox = new GridBagConstraints();
		gbc_columnSeparatorCombobox.fill = GridBagConstraints.BOTH;
		gbc_columnSeparatorCombobox.insets = new Insets(0, 0, 5, 0);
		gbc_columnSeparatorCombobox.gridx = 0;
		gbc_columnSeparatorCombobox.gridy = gridY;
		csvSettingsPanel.add(columnSeparatorCombobox, gbc_columnSeparatorCombobox);
	}

	private void addUseHeaderCheckbox(final Container csvSettingsPanel, final int gridY) {
		useHeaderJL = new JLabel(Lang.l().step2ParseHeader() + "?");
		useHeaderJCB = new JCheckBox();
		if (logger.isTraceEnabled()) {
			useHeaderJCB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					logger.trace("useHeader state changed. is selected?" +
							useHeaderJCB.isSelected());
				}
			});
		}
		useHeaderJCB.setSelected(false);
		// will be enabled if firstLineWithdata is set to > 0
		useHeaderJCB.setEnabled(false);
		final JPanel useHeaderPanel = new JPanel();
		useHeaderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		useHeaderPanel.add(useHeaderJL);
		useHeaderPanel.add(useHeaderJCB);
		final GridBagConstraints gbc_useHeaderPanel = new GridBagConstraints();
		gbc_useHeaderPanel.fill = GridBagConstraints.BOTH;
		gbc_useHeaderPanel.insets = new Insets(0, 0, 5, 0);
		gbc_useHeaderPanel.gridx = 0;
		gbc_useHeaderPanel.gridy = gridY;
		// TODO uncomment to enable useHeader
		// csvSettingsPanel.add(useHeaderPanel, gbc_useHeaderPanel);
	}

	/**
	 * <p>getCommentIndicator.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCommentIndicator() {
		return (String) commentIndicatorCombobox.getSelectedItem();
	}

	/**
	 * <p>setCommentIndicator.</p>
	 *
	 * @param commentIndicator a {@link java.lang.String} object.
	 */
	public void setCommentIndicator(final String commentIndicator) {
		commentIndicatorCombobox.setSelectedItem(commentIndicator);
	}

	/**
	 * <p>getDecimalSeparator.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDecimalSeparator() {
		return decimalSeparatorCombobox.getSelectedItem().toString();
	}

	/**
	 * <p>setDecimalSeparator.</p>
	 *
	 * @param decimalSeparator a {@link java.lang.String} object.
	 */
	public void setDecimalSeparator(final String decimalSeparator) {
		decimalSeparatorCombobox.setSelectedItem(decimalSeparator);
	}

	/**
	 * <p>getColumnSeparator.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getColumnSeparator() {
		return (String) columnSeparatorCombobox.getSelectedItem();
	}

	/**
	 * <p>setColumnSeparator.</p>
	 *
	 * @param columnSeparator a {@link java.lang.String} object.
	 */
	public void setColumnSeparator(final String columnSeparator) {
		columnSeparatorCombobox.setSelectedItem(columnSeparator);
	}

	/**
	 * <p>getCSVFileContent.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCSVFileContent() {
		// remove line numbers from each row before returning data
		final String txt = csvFileTextArea.getText();
		final StringTokenizer tok = new StringTokenizer(txt,"\n");
		final StringBuffer buf = new StringBuffer(txt.length());
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

	/**
	 * <p>setCSVFileContent.</p>
	 *
	 * @param content a {@link java.lang.String} object.
	 */
	public void setCSVFileContent(final String content) {
		// add line numbers to content
		final String[] lines = content.split("\n");
		int count = 0, levelOfCount = 1;
		final int maxLevel = Integer.toString(csvFileRowCount).length();
		// 2:= whitespace + Constants.RAW_DATA_SEPARATOR
		final int bufferSize = content.length() + (lines.length * (maxLevel + 2));
		final StringBuffer sb = new StringBuffer(bufferSize);
		for (final String line : lines) {

			for (int i = levelOfCount; i < maxLevel; i++) {
				sb.append(" ");
			}
			sb.append(count)
    			.append(Constants.RAW_DATA_SEPARATOR)
    			.append(" ")
    			.append(line)
    			.append("\n");
			//
			//	preparation for next round
			count++;
			levelOfCount = Integer.toString(count).length();
		}
		sb.trimToSize();
		csvFileTextArea.setText(sb.toString());
		csvFileTextArea.setCaretPosition(0);
	}

	/**
	 * <p>setCSVFileHighlight.</p>
	 *
	 * @param number a int.
	 */
	public void setCSVFileHighlight(final int number) {
		if (logger.isTraceEnabled()) {
			logger.trace("setCSVFileHighlight()");
		}
		try {
			csvFileTextArea.getHighlighter().removeAllHighlights();
			if (number >= 0) {
				final int lineStart = csvFileTextArea.getLineStartOffset(number);
				final int lineEnd = csvFileTextArea.getLineEndOffset(csvFileRowCount-1);
				final HighlightPainter painter = new DefaultHighlighter.
						DefaultHighlightPainter(
								Constants.DEFAULT_HIGHLIGHT_COLOR);
				csvFileTextArea.getHighlighter().addHighlight(lineStart, lineEnd, painter);
			}
		} catch (final BadLocationException e1) {
			logger.error("Exception thrown: " + e1.getMessage(), e1);
		}
	}

	/**
	 * <p>getFirstLineWithData.</p>
	 *
	 * @return user input or <code>-1</code> if invalid input is defined
	 */
	public int getFirstLineWithData() {
		return lineModel.getNumber().intValue();
	}

	/**
	 * <p>setFirstLineWithData.</p>
	 *
	 * @param firstLineWithData a int.
	 */
	public void setFirstLineWithData(final int firstLineWithData) {
		lineModel.setValue(firstLineWithData);
	}

	/**
	 * <p>getTextQualifier.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTextQualifier() {
		return (String) textQualifierCombobox.getSelectedItem();
	}

	/**
	 * <p>setTextQualifier.</p>
	 *
	 * @param textQualifier a {@link java.lang.String} object.
	 */
	public void setTextQualifier(final String textQualifier) {
		textQualifierCombobox.setSelectedItem(textQualifier);
	}

	/**
	 * <p>getUseHeader.</p>
	 *
	 * @return a boolean.
	 */
	public boolean getUseHeader() {
		return useHeaderJCB.isSelected();
	}

	/**
	 * <p>setUseHeader.</p>
	 *
	 * @param useHeader a boolean.
	 */
	public void setUseHeader(final boolean useHeader) {
		useHeaderJCB.setSelected(useHeader);
	}

	/**
	 * <p>setSampleBased.</p>
	 *
	 * @param isSampleBased a boolean.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBased(final boolean isSampleBased) {
		isSampleBasedCheckBox.setSelected(isSampleBased);
		setSampleBasedElementsEnabled(isSampleBased);
		return this;
	}

	/**
	 * <p>isSampleBased.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSampleBased() {
		return isSampleBasedCheckBox.isSelected();
	}

	/**
	 * <p>getSampleBasedStartRegEx.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSampleBasedStartRegEx() {
		return startRegExTF.getText();
	}

	/**
	 * <p>setSampleBasedStartRegEx.</p>
	 *
	 * @param sampleBasedStartRegEx a {@link java.lang.String} object.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedStartRegEx(final String sampleBasedStartRegEx) {
		startRegExTF.setText(sampleBasedStartRegEx);
		return this;
	}

	/**
	 * <p>getSampleBasedDateOffset.</p>
	 *
	 * @return a int.
	 */
	public int getSampleBasedDateOffset() {
		return dateOffsetModel.getNumber().intValue();
	}

	/**
	 * <p>setSampleBasedDateOffset.</p>
	 *
	 * @param dateOffset a int.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedDateOffset(final int dateOffset) {
		dateOffsetModel.setValue(dateOffset);
		return this;
	}

	/**
	 * <p>getSampleBasedDateExtractionRegEx.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSampleBasedDateExtractionRegEx() {
		return dateExtractionRegExTF.getText();
	}

	/**
	 * <p>setSampleBasedDateExtractionRegEx.</p>
	 *
	 * @param sampleBasedDateExtractionRegEx a {@link java.lang.String} object.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedDateExtractionRegEx(final String sampleBasedDateExtractionRegEx) {
		dateExtractionRegExTF.setText(sampleBasedDateExtractionRegEx);
		return this;
	}

	/**
	 * <p>getSampleBasedDatePattern.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSampleBasedDatePattern() {
		return datePatternTF.getText();
	}

	/**
	 * <p>setSampleBasedDatePattern.</p>
	 *
	 * @param sampleBasedDatePattern a {@link java.lang.String} object.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedDatePattern(final String sampleBasedDatePattern) {
		datePatternTF.setText(sampleBasedDatePattern);
		return this;
	}

	/**
	 * <p>getSampleBasedDataOffset.</p>
	 *
	 * @return a int.
	 */
	public int getSampleBasedDataOffset() {
		return dataOffsetModel.getNumber().intValue();
	}

	/**
	 * <p>setSampleBasedDataOffset.</p>
	 *
	 * @param dataOffset a int.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedDataOffset(final int dataOffset) {
		dataOffsetModel.setValue(dataOffset);
		return this;
	}

	/**
	 * <p>getSampleBasedSampleSizeOffset.</p>
	 *
	 * @return a int.
	 */
	public int getSampleBasedSampleSizeOffset() {
		return sampleSizeOffsetModel.getNumber().intValue();
	}

	/**
	 * <p>setSampleBasedSampleSizeOffset.</p>
	 *
	 * @param sampleSizeOffset a int.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedSampleSizeOffset(final int sampleSizeOffset) {
		sampleSizeOffsetModel.setValue(sampleSizeOffset);
		return this;
	}

	/**
	 * <p>getSampleBasedSampleSizeRegEx.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSampleBasedSampleSizeRegEx() {
		return sampleSizeRegExTF.getText();
	}

	/**
	 * <p>setSampleBasedSampleSizeRegEx.</p>
	 *
	 * @param sampleBasedSampleSizeRegEx a {@link java.lang.String} object.
	 * @return a {@link org.n52.sos.importer.view.Step2Panel} object.
	 */
	public Step2Panel setSampleBasedSampleSizeRegEx(final String sampleBasedSampleSizeRegEx) {
		sampleSizeRegExTF.setText(sampleBasedSampleSizeRegEx);
		return this;
	}


}
