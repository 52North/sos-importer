package org.n52.sos.importer;

import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public abstract class SelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected HashMap<String, SelectionPanel> childSelectionPanels = new HashMap<String, SelectionPanel>();
	
	private String childSelectionPanelName;
	
	private final MainFrame mainFrame;
	
	private final JPanel containerPanel;
	
	public SelectionPanel(MainFrame mainFrame, JPanel containerPanel) {
		super();
		this.mainFrame = mainFrame;
		this.containerPanel = containerPanel;
	}
	
	protected abstract void setSelection(String s);
	
	protected abstract String getSelection();
	
	protected void selectionChanged() {	
	}
	
	public void restore(List<String> selections) {
		getContainerPanel().add(this);
		String s = selections.get(0);
		setSelection(s);
		SelectionPanel childPanel = childSelectionPanels.get(s);
		selections.remove(0);
		
		if (childPanel != null) {
			childPanel.restore(selections);
		}
	}	
	
	public void restoreDefault() {
		setDefaultSelection();
		
		for (SelectionPanel sp: childSelectionPanels.values())
			sp.setDefaultSelection();
	}
	
	public abstract void setDefaultSelection();
	
	public void store(List<String> selections) {
		String s = getSelection();
		selections.add(s);
		SelectionPanel childPanel = childSelectionPanels.get(s);
		if (childPanel != null)
			childPanel.store(selections);
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public JPanel getContainerPanel() {
		return containerPanel;
	}
	
	public SelectionPanel getChildSelectionPanel() {
		return childSelectionPanels.get(childSelectionPanelName);
	}
	
	public void setChildSelectionPanel(String childSelectionPanelName) {
		this.childSelectionPanelName = childSelectionPanelName;
	}
	
	/**
	 * removes this selection panel from its container
	 * and recursively all child selection panels
	 */
	public void removeFromContainerPanel() {
		getContainerPanel().removeAll();
		
		for (SelectionPanel sp: childSelectionPanels.values()) 
			sp.removeFromContainerPanel();
	}
}
