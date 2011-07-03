package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.EditableJComboBox;
import org.n52.sos.importer.config.EditableComboBoxItems;

public class Step8Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBox sosComboBox;
	
	public Step8Panel() {
		sosComboBox = new EditableJComboBox(EditableComboBoxItems.getInstance().getSensorObservationServices());
		this.add(sosComboBox);
	}
	
	public String getSOSURL() {
		return (String) sosComboBox.getSelectedItem();
	}
}


