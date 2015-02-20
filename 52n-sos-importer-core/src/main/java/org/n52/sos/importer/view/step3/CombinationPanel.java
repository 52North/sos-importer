/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * selection panel in step 3 for date&time and position combinations,
 * offers the functions to choose different patterns, to provide an 
 * example for the pattern, to test parsing the marked values in the 
 * table and to choose a group for the combination
 * @author Raimund
 * 
 * TODO document the methods
 *
 */
public abstract class CombinationPanel extends SelectionPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(CombinationPanel.class);

	private final JLabel groupLabel;
	private final JComboBox groupComboBox = new JComboBox(getGroupItems());
	
	private final EditableJComboBoxPanel patternComboBox;
	
    private final JLabel exampleLabel;
    private final ExampleFormatLabel exampleFormatLabel;
	
    private final ParseTestLabel parseTestLabel;

	public CombinationPanel(final JPanel containerPanel,final int firstLineWithData) {	
		super(containerPanel);
		
		parseTestLabel = new ParseTestLabel(getCombination(),firstLineWithData);
		patternComboBox= new EditableJComboBoxPanel(getPatterns(), Lang.l().format(), getPatternToolTip());
		exampleFormatLabel =  new ExampleFormatLabel(getCombination());
		patternComboBox.addActionListener(new FormatChanged());
		groupComboBox.setToolTipText(getGroupToolTip());
		//setDefaultSelection();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		formatPanel.add(patternComboBox);
							
		final JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupLabel = new JLabel(Lang.l().group() + ": ");
		groupPanel.add(groupLabel);
		groupPanel.add(groupComboBox);
		
		final JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		exampleLabel = new JLabel(Lang.l().example() + ": ");
		examplePanel.add(exampleLabel);
		examplePanel.add(exampleFormatLabel);
		
		final JPanel dateTimePanel = new JPanel();
		dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.PAGE_AXIS));
		dateTimePanel.add(formatPanel);	
		dateTimePanel.add(examplePanel);
		dateTimePanel.add(groupPanel);
		this.add(dateTimePanel);
		this.add(parseTestLabel);
	}
	
	public abstract Combination getCombination();
	
	public abstract String[] getGroupItems();
	
	public abstract DefaultComboBoxModel getPatterns();
	
	public abstract Object getTestValue();
	
	public abstract String getPatternToolTip();
	
	public abstract String getGroupToolTip();

    @Override
	public void setSelection(final String s) {
    	if (logger.isTraceEnabled()) {
			logger.trace("setSelection()");
		}
    	final String[] part = s.split(Constants.SEPARATOR_STRING);
		patternComboBox.setSelectedItem(part[0]);
		groupComboBox.setSelectedItem(part[1]);
	}
	
	@Override
	public void setDefaultSelection() {
		if (logger.isTraceEnabled()) {
			logger.trace("setDefaultSelection()");
		}
		final String pattern = (String) getPatterns().getElementAt(0);
		final String group = getGroupItems()[0];
		patternComboBox.setSelectedItem(pattern);
		getCombination().setPattern(pattern);
		groupComboBox.setSelectedItem(group);
	}
	
	@Override
	public String getSelection() {
		final String pattern = (String) patternComboBox.getSelectedItem();
		final String group = (String) groupComboBox.getSelectedItem();
		return pattern + Constants.SEPARATOR_STRING + group;
	}
    
    @Override
	protected void patternChanged() {
    	final String pattern = (String) patternComboBox.getSelectedItem();
    	if (logger.isTraceEnabled()) {
			logger.trace("patternChanged(" + pattern + ")");
		}
    	final List<String> values = TableController.getInstance().getMarkedValues();
    	final Object tester = getTestValue();
    	//
    	getCombination().setPattern(pattern);
    	exampleFormatLabel.getFormatter().setPattern(pattern);
    	// test if the current selected pattern is applicable 
    	parseTestLabel.parseValues(values);
    	// display example using the current defined pattern
    	exampleFormatLabel.reformat(tester);
    }
    
    @Override
    protected void reInit() {
    	if (logger.isTraceEnabled()) {
			logger.trace("reInit()");
		}
    	patternChanged();
    }
  	    
	private class FormatChanged implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			if (!patternComboBox.isEditable()) {
				patternChanged();
			}
	    }
	}
}
