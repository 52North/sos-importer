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

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.view.utils.Constants;

/**
 * selection panel in step 3 for date&time and position combinations,
 * offers the functions to choose different patterns, to provide an 
 * example for the pattern, to test parsing the marked values in the 
 * table and to choose a group for the combination
 * @author Raimund
 *
 */
public abstract class CombinationPanel extends SelectionPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;

	private JLabel groupLabel = new JLabel("Group: ");
	private JComboBox groupComboBox = new JComboBox(getGroupItems());
	
	private EditableJComboBoxPanel patternComboBox = new EditableJComboBoxPanel(getPatterns(), "Format", getPatternToolTip());
	
    private JLabel exampleLabel = new JLabel("Example: ");
    private final ExampleFormatLabel exampleFormatLabel = new ExampleFormatLabel(getCombination());
	
    private final ParseTestLabel parseTestLabel;

	public CombinationPanel(JPanel containerPanel,int firstLineWithData) {	
		super(containerPanel);
		setDefaultSelection();
		
		this.parseTestLabel = new ParseTestLabel(getCombination(),firstLineWithData);
		patternComboBox.addActionListener(new FormatChanged());
		groupComboBox.setToolTipText(getGroupToolTip());
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		formatPanel.add(patternComboBox);
							
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupPanel.add(groupLabel);
		groupPanel.add(groupComboBox);
		
		JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
    	String[] part = s.split(Constants.SEPARATOR_STRING);
		patternComboBox.setSelectedItem(part[0]);
		groupComboBox.setSelectedItem(part[1]);
    	patternChanged();
	}
	
	@Override
	public void setDefaultSelection() {
		String pattern = (String) getPatterns().getElementAt(0);
		String group = (String) getGroupItems()[0];
		patternComboBox.setSelectedItem(pattern);
		getCombination().setPattern(pattern);
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
    	getCombination().setPattern(pattern);
    	parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
    	exampleFormatLabel.reformat(getTestValue());
    }
    
    @Override
    protected void reInit() {
    	parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
    	exampleFormatLabel.reformat(getTestValue());
    };
  	    
	private class FormatChanged implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if (!patternComboBox.isEditable())
				patternChanged();
	    }
	}
}
