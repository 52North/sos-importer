package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.tooltips.ToolTips;

public class Step7Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel sosComboBox;
	
	public Step7Panel() {
		sosComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), "SOS-URL", ToolTips.get("SOS"));
		this.add(sosComboBox);
	}
	
	public String getSOSURL() {
		return (String) sosComboBox.getSelectedItem();
	}
}


