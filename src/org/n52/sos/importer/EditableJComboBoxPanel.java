package org.n52.sos.importer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class EditableJComboBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label;
	
	private JComboBox comboBox;
	
	private DefaultComboBoxModel model;
	
	private JButton newItemButton;
	
	private JButton deleteItemButton;
	
	public EditableJComboBoxPanel(DefaultComboBoxModel model, String labelName) {
		super();
		this.model = model;
		label = new JLabel(labelName + ": ");
		comboBox = new JComboBox(model);
		newItemButton = createIconButton("newItem.png", "Add a new item to the list");
		deleteItemButton = createIconButton("deleteItem.png", "Delete the selected item from the list");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(label);
		this.add(comboBox);
		this.add(newItemButton);
		this.add(deleteItemButton);
		
		newItemButton.addActionListener(new NewItem());
		deleteItemButton.addActionListener(new DeleteItem());
		comboBox.addActionListener(new EnterPressed());
		comboBox.getEditor().getEditorComponent().addFocusListener(new FocusChanged());
	}	
	
	public Object getSelectedItem() {
		return model.getSelectedItem();
	}
	
	public void setSelectedItem(Object item) {
		model.setSelectedItem(item);
	}
	
	public void addActionListener(ActionListener al) {
		comboBox.addActionListener(al);
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected JButton createIconButton(String fileName, String toolTip) {
		JButton iconButton = new JButton();
		URL imgURL = getClass().getResource("/org/n52/sos/importer/config/" + fileName);
		ImageIcon icon = null;
		if (imgURL != null) {
			icon = new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + fileName);
	        return null;
	    }
		
		iconButton.setIcon(icon);
		iconButton.setContentAreaFilled(false);
		iconButton.setBorderPainted(false);
		iconButton.setToolTipText(toolTip);
		iconButton.setPreferredSize(new Dimension(20, 20));
		
		return iconButton;
	}

	public void saveNewItem() {
		String newItem = comboBox.getEditor().getItem().toString();
		comboBox.setEditable(false);
		if (newItem == null || newItem.equals("")) 
			return;
		
		if (model.getIndexOf(newItem) == -1) {
			Object[] items = new Object[model.getSize()];
			for (int i = 0; i < model.getSize(); i++)
				items[i] = model.getElementAt(i);
			
			model.removeAllElements();
			model.addElement(newItem.trim());
			for (int i = 0; i < items.length; i++)
				model.addElement(items[i]);
			
			model.setSelectedItem(newItem);
		}
	}
	
	public void deleteSelectedItem() {
		if (model.getSize() == 0) return;
		
		Object selectedItem = comboBox.getSelectedItem();
		model.removeElement(selectedItem);
	}
	
	private class FocusChanged implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {	
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			saveNewItem();
		}
	}
	
	public class EnterPressed implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (comboBox.isEditable())
				saveNewItem();
		}	
	}
	
	private class NewItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			comboBox.setEditable(true);
			comboBox.getEditor().getEditorComponent().requestFocus();
		}
	}
	
	private class DeleteItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			deleteSelectedItem();		
		}
	}
}
