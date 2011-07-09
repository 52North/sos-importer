package org.n52.sos.importer.view.resources;

import java.awt.FlowLayout;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.view.position.MissingComponentPanel;

public class MissingResourcePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MissingResourcePanel.class);

	private Resource resource;
	
	private final EditableJComboBoxPanel nameComboBox; 

	private final EditableJComboBoxPanel uriComboBox; 
	
	public MissingResourcePanel(Resource resource) {
		this.resource = resource;
		nameComboBox = new EditableJComboBoxPanel(resource.getNames(), "Name");
		uriComboBox = new EditableJComboBoxPanel(resource.getURIs(), "URI");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(nameComboBox);
		this.add(uriComboBox);

	}
	
	public boolean checkValues() {
		//get and check name
		String name = (String) nameComboBox.getSelectedItem();
		if (name == null) {
			logger.warn("No Name given.");
			return false;
		}
		
		//get and check URI
		String uri = (String) uriComboBox.getSelectedItem();
		URI URI = null;
		try {
			if (uri != null)
				URI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.warn("Wrong URI", e);
			return false;
		}
		return true;
	}
	
	
	@Override
	public void assignValues() {
		String name = (String) nameComboBox.getSelectedItem();
		resource.setName(name);
		String uri = (String) uriComboBox.getSelectedItem();
		try {
			if (uri != null) {
				URI URI = new URI(uri);
				resource.setURI(URI);
			}
		} catch (URISyntaxException e) {
		}	
	}

	@Override
	public void unassignValues() {
		resource.setName(null);
		resource.setURI(null);
	}

}
