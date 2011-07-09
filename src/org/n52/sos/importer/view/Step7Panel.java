package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.config.EditableComboBoxItems;

public class Step7Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel sosComboBox;
	
	public Step7Panel() {
		sosComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), "URL");
		this.add(sosComboBox);
	}
	
	public String getSOSURL() {
		return (String) sosComboBox.getSelectedItem();
	}
}


