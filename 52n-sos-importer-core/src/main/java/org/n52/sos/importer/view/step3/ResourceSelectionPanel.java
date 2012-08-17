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

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;

/**
 * assigns or unassigns columns to Feature of Interests, Observed 
 * Properties, Units of Measurement and Sensors
 * @author Raimund
 *
 */
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
	public void unAssign(TableElement tableElement) {
		Resource resourceToRemove = null;
		for (Resource r: resource.getList())
			if (tableElement.equals(r.getTableElement())) {
				resourceToRemove = r;
				break;
			}
				
		ModelStore.getInstance().remove(resourceToRemove);
	}
}
