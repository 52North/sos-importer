package org.n52.sos.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	}

	public void saveSelectedItem() {
		model.addElement(this.getSelectedItem());
	}
	
	public void addItems(HashSet<Object> items) {
		for (Object o: items)
			this.addItem(o);
	}
	
	public Object[] getItems() {
		List<Object> items = new ArrayList<Object>();
		for (int i = 0; i < this.getItemCount(); i++) 
			items.add(this.getItemAt(i));		
		return items.toArray();
	}
}
