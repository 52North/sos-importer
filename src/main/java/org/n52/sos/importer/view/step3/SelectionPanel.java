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
	
	protected abstract void setSelection(String s);
	
	protected abstract String getSelection();
	
	/**
	 * called when a combobox which contains patterns 
	 * changes its selection
	 */
	protected void patternChanged() {	
	}
	
	/**
	 * called when a selection has been restored
	 */
	protected void reinit() {
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
		reinit();
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
	 * in step 3
	 * @param selections
	 */
	public void store(List<String> selections) {
		String s = getSelection();
		selections.add(s);
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
		reinit();
		
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
	public abstract void unassign(TableElement tableElement);
}
