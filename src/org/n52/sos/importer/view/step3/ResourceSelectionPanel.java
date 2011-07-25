package org.n52.sos.importer.view.step3;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;

public class ResourceSelectionPanel extends SelectionPanel {

	private static final long serialVersionUID = 1L;
	
	private Resource resource;

	public ResourceSelectionPanel(JPanel containerPanel, Resource resource) {
		super(containerPanel);
		this.resource = resource;
	}

	@Override
	protected void setSelection(String s) {	
	}

	@Override
	protected String getSelection() {
		return "0";
	}

	@Override
	public void setDefaultSelection() {		
	}
	
	@Override
	public void assign(TableElement tableElement) {
		resource.setTableElement(tableElement);
		ModelStore.getInstance().add(resource);
	}

	@Override
	public void unassign(TableElement tableElement) {
		Resource resourceToRemove = null;
		for (Resource r: resource.getList())
			if (tableElement.equals(r.getTableElement())) {
				resourceToRemove = r;
				break;
			}
				
		ModelStore.getInstance().remove(resourceToRemove);
	}
}
