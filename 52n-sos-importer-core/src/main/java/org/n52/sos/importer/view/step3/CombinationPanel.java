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

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;

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
	
	private static final Logger logger = Logger.getLogger(CombinationPanel.class);

	private JLabel groupLabel;
	private JComboBox groupComboBox = new JComboBox(getGroupItems());
	
	private EditableJComboBoxPanel patternComboBox;
	
    private JLabel exampleLabel;
    private ExampleFormatLabel exampleFormatLabel;
	
    private ParseTestLabel parseTestLabel;

	public CombinationPanel(JPanel containerPanel,int firstLineWithData) {	
		super(containerPanel);
		
		this.parseTestLabel = new ParseTestLabel(getCombination(),firstLineWithData);
		this.patternComboBox= new EditableJComboBoxPanel(getPatterns(), Lang.l().format(), getPatternToolTip());
		this.exampleFormatLabel =  new ExampleFormatLabel(getCombination());
		patternComboBox.addActionListener(new FormatChanged());
		groupComboBox.setToolTipText(getGroupToolTip());
		//setDefaultSelection();
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		formatPanel.add(patternComboBox);
							
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.groupLabel = new JLabel(Lang.l().group() + ": ");
		groupPanel.add(groupLabel);
		groupPanel.add(groupComboBox);
		
		JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.exampleLabel = new JLabel(Lang.l().example() + ": ");
		examplePanel.add(exampleLabel);
		examplePanel.add(exampleFormatLabel);
		
		JPanel dateTimePanel = new JPanel();
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
	public void setSelection(String s) {
    	if (logger.isTraceEnabled()) {
			logger.trace("setSelection()");
		}
    	String[] part = s.split(Constants.SEPARATOR_STRING);
		patternComboBox.setSelectedItem(part[0]);
		groupComboBox.setSelectedItem(part[1]);
	}
	
	@Override
	public void setDefaultSelection() {
		if (logger.isTraceEnabled()) {
			logger.trace("setDefaultSelection()");
		}
		String pattern = (String) getPatterns().getElementAt(0);
		String group = getGroupItems()[0];
		patternComboBox.setSelectedItem(pattern);
		this.getCombination().setPattern(pattern);
		groupComboBox.setSelectedItem(group);
	}
	
	@Override
	public String getSelection() {
		String pattern = (String) patternComboBox.getSelectedItem();
		String group = (String) groupComboBox.getSelectedItem();
		return pattern + Constants.SEPARATOR_STRING + group;
	}
    
    protected void patternChanged() {
    	String pattern = (String) patternComboBox.getSelectedItem();
    	if (logger.isTraceEnabled()) {
			logger.trace("patternChanged(" + pattern + ")");
		}
    	List<String> values = TableController.getInstance().getMarkedValues();
    	Object tester = getTestValue();
    	//
    	this.getCombination().setPattern(pattern);
    	this.exampleFormatLabel.getFormatter().setPattern(pattern);
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
    	this.patternChanged();
    }
  	    
	private class FormatChanged implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if (!patternComboBox.isEditable())
				patternChanged();
	    }
	}
}
