package org.n52.sos.importer;

import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public abstract class SelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final MainFrame mainFrame;
	
	private final JPanel containerPanel;
	
	private HashMap<String, SelectionPanel> childPanels = new HashMap<String, SelectionPanel>();
	
	private SelectionPanel selectedChildPanel;
	
	public SelectionPanel(MainFrame mainFrame, JPanel containerPanel) {
		super();
		this.mainFrame = mainFrame;
		this.containerPanel = containerPanel;
	}
	
	protected abstract void setSelection(String s);
	
	protected abstract String getSelection();
	
	protected void selectionChanged() {	
		//TODO selectionListener instead
	}
	
	public void restore(List<String> selections) {
		getContainerPanel().add(this);
		String s = selections.get(0);
		setSelection(s);
		SelectionPanel childPanel = childPanels.get(s);
		selections.remove(0);
		
		if (childPanel != null) {
			childPanel.restore(selections);
		}
	}	
	
	public void restoreDefault() {
		setDefaultSelection();
		
		for (SelectionPanel sp: childPanels.values())
			sp.restoreDefault();
	}
	
	public abstract void setDefaultSelection();
	
	public void store(List<String> selections) {
		String s = getSelection();
		selections.add(s);
		SelectionPanel childPanel = childPanels.get(s);
		if (childPanel != null)
			childPanel.store(selections);
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public JPanel getContainerPanel() {
		return containerPanel;
	}
	
	public SelectionPanel getSelectedChildPanel() {
		return selectedChildPanel;
	}
	
	public void setSelectedChildPanel(SelectionPanel childPanel) {
		this.selectedChildPanel = childPanel;
	}
	
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
	
	public void addToContainerPanel() {
		getContainerPanel().add(this);		
		SelectionPanel childPanel = getSelectedChildPanel();
		
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
}
