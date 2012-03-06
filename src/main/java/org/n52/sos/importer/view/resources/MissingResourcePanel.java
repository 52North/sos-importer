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
package org.n52.sos.importer.view.resources;

import java.awt.FlowLayout;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.tooltips.ToolTips;

/**
 * consists of a combobox for the name and a combobox for the URI;
 * both are linked with each other
 * @author Raimund
 *
 */
public class MissingResourcePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private Resource resource;
	
	private final EditableJComboBoxPanel nameComboBox; 

	private final EditableJComboBoxPanel uriComboBox; 
	
	public MissingResourcePanel(Resource resource) {
		this.resource = resource;
		String name = "Name";
		if (resource instanceof UnitOfMeasurement) name = "Code";
		nameComboBox = new EditableJComboBoxPanel(resource.getNames(), name, ToolTips.get("Name"));
		uriComboBox = new EditableJComboBoxPanel(resource.getURIs(), "URI", ToolTips.get("URI"));
		nameComboBox.setPartnerComboBox(uriComboBox);
		uriComboBox.setPartnerComboBox(nameComboBox);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(nameComboBox);
		this.add(uriComboBox);

	}
	
	@Override
	public boolean checkValues() {
		String name = nameComboBox.getSelectedItem().toString().trim();
		String uri = uriComboBox.getSelectedItem().toString().trim();
		
		//check syntax of URI
		try {
			new URI(uri);
		} catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(null,
				    "The entered URI is syntactically not correct.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		if (name.equals("") && uri.equals("")) {
			JOptionPane.showMessageDialog(null,
				    "You have to type in at least a name or a URI.",
				    "Information",
				    JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	@Override
	public void assignValues() {
		String name = (String) nameComboBox.getSelectedItem();
		name = name.trim();
		resource.setName(name);
		
		String uri = (String) uriComboBox.getSelectedItem();
		uri = uri.trim();
		try {
			URI URI = new URI(uri);
			resource.setURI(URI);		
		} catch (URISyntaxException e) {
		}	
	}

	@Override
	public void unassignValues() {
		resource.setName(null);
		resource.setURI(null);
	}

	@Override
	public Component getMissingComponent() {
		return resource;
	}

	@Override
	public void setMissingComponent(Component c) {
		Resource r = (Resource) c;
		String name = r.getName();
		if (name != null)
			nameComboBox.setSelectedItem(name);
	}
}
