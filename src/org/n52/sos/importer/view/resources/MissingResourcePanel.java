package org.n52.sos.importer.view.resources;

import java.awt.FlowLayout;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.n52.sos.importer.config.EditableJComboBoxPanel;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;

public class MissingResourcePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private Resource resource;
	
	private final EditableJComboBoxPanel nameComboBox; 

	private final EditableJComboBoxPanel uriComboBox; 
	
	public MissingResourcePanel(Resource resource) {
		this.resource = resource;
		String name = "Name";
		if (resource instanceof UnitOfMeasurement) name = "Code";
		nameComboBox = new EditableJComboBoxPanel(resource.getNames(), name);
		uriComboBox = new EditableJComboBoxPanel(resource.getURIs(), "URI");
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
