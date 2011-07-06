package org.n52.sos.importer;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;


public class EditableJComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel model;
	
	public EditableJComboBox(DefaultComboBoxModel model) {
		super();
		this.model = model;
		this.setEditable(true);
		this.setModel(model);
		this.getEditor().getEditorComponent().addFocusListener(new FocusChanged());
	}

	public void saveSelectedItem() {
		String selectedItem = this.getEditor().getItem().toString();
		if (selectedItem == null || selectedItem.equals("")) return;
		if (model.getIndexOf(selectedItem) == -1)
			model.addElement(selectedItem.trim());
	}
	
	private class FocusChanged implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {	
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			saveSelectedItem();		
		}
	}
}
