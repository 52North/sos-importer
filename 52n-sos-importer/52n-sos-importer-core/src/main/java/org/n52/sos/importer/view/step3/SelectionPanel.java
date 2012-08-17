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

import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.table.TableElement;

/**
 * used for different kinds of selections in step 3
 * @author Raimund
 *
 */
public abstract class SelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JPanel containerPanel;
	
	private HashMap<String, SelectionPanel> childPanels = new HashMap<String, SelectionPanel>();
	
	private SelectionPanel selectedChildPanel;
	
	public SelectionPanel(JPanel containerPanel) {
		super();
		this.containerPanel = containerPanel;
	}
	
	/**
	 * This method is called when the settings for the step controller are loaded
	 * @param s
	 */
	protected abstract void setSelection(String s);
	
	/**
	 * Returns the current selected options done by the user
	 * @return a {@linkplain java.lang.String} with the encoded user input
	 */
	protected abstract String getSelection();
	
	/**
	 * called when a combobox which contains patterns 
	 * changes its selection
	 */
	protected void patternChanged() {	
	}
	
	/**
	 * called when a selection has been restored and the panel is added to view
	 */
	protected void reInit() {
	}
	
	/**
	 * called to restore the selection of a column
	 * in step 3
	 * @param selections
	 */
	public void restore(List<String> selections) {
		getContainerPanel().add(this);
		String s = selections.get(0);
		setSelection(s);
		SelectionPanel childPanel = childPanels.get(s);
		selections.remove(0);
		reInit();
		setSelectedChildPanel(childPanel);
		
		if (childPanel != null) {
			childPanel.restore(selections);
		}
	}	
	
	/**
	 * called when a column in step 3 has
	 * not been visited yet
	 */
	public void restoreDefault() {
		setDefaultSelection();
		
		for (SelectionPanel sp: childPanels.values())
			sp.restoreDefault();
	}
	
	public abstract void setDefaultSelection();
	
	/**
	 * stores the current selection for this column
	 * in step 3 in the {@linkplain org.n52.sos.importer.model.ModelStore}
	 * @param selections list of all selected items, e.g. column type and the corresponding meta data
	 */
	public void store(List<String> selections) {
		// get our own selection and add them to the selections list
		String s = this.getSelection();
		selections.add(s);
		// ask my child panels if one is 
		SelectionPanel childPanel = childPanels.get(s);
		if (childPanel != null)
			childPanel.store(selections);
	}
	
	public JPanel getContainerPanel() {
		return containerPanel;
	}
	
	public SelectionPanel getSelectedChildPanel() {
		return selectedChildPanel;
	}
	
	/**
	 * called when the child panel's selection changes 
	 * @param childPanel
	 */
	public void setSelectedChildPanel(SelectionPanel childPanel) {
		this.selectedChildPanel = childPanel;
	}
	
	/**
	 * called when the child panel's selection changes
	 * @param childPanel
	 */
	public void setSelectedChildPanel(String childPanelName) {
		this.selectedChildPanel = childPanels.get(childPanelName);
	}
	
	/**
	 * adds a child panel to the list
	 * @param childPanelName
	 * @param childPanel
	 */
	public void addChildPanel(String childPanelName, SelectionPanel childPanel) {
		childPanels.put(childPanelName, childPanel);
	}
	
	/**
	 * adds this and recursively the selected child panel 
	 * to one of the container panels of step 3
	 */
	public void addToContainerPanel() {
		getContainerPanel().add(this);		
		SelectionPanel childPanel = getSelectedChildPanel();
		reInit();
		
		if (childPanel != null) {
			setSelectedChildPanel(childPanel);
			childPanel.addToContainerPanel();
		}
	}
	
	/**
	 * removes this selection panel from its container
	 * and recursively all child selection panels
	 */
	public void removeFromContainerPanel() {
		getContainerPanel().removeAll();
		
		for (SelectionPanel sp: childPanels.values()) 
			sp.removeFromContainerPanel();
	}
	
	/**
	 * assigns this selection to a column, row or a cell
	 * @param tableElement
	 */
	public abstract void assign(TableElement tableElement);
	
	/**
	 * unassigns this selection from a column, row or a cell
	 * @param tableElement
	 */
	public abstract void unAssign(TableElement tableElement);
}
