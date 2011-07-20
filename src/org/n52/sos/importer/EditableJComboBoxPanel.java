package org.n52.sos.importer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class EditableJComboBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String WHITESPACE = "                          ";

	private JLabel label;
	
	private JComboBox comboBox;
	
	private DefaultComboBoxModel model;
	
	private boolean enterPressed = false;
	
	private ActionListener selectionChanged = new SelectionChanged();
	
	private EditableJComboBoxPanel partnerComboBox;
	
	private boolean secondComboBox = false;
	
	public EditableJComboBoxPanel(DefaultComboBoxModel model, String labelName) {
		super();
		this.model = model;
		label = new JLabel(labelName + ":   ");
		comboBox = new JComboBox(model);
		
		if (model.getSize() == 0) 
			model.addElement(WHITESPACE);
		
		JButton newItemButton = createIconButton("newItem.png", "Add a new item to the list");
		JButton deleteItemButton = createIconButton("deleteItem.png", "Delete the selected item from the list");
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(label);
		this.add(comboBox);
		this.add(newItemButton);
		this.add(deleteItemButton);
		
		newItemButton.addActionListener(new NewItem());
		deleteItemButton.addActionListener(new DeleteItem());
		comboBox.getEditor().getEditorComponent().addKeyListener(new EnterPressed());
		comboBox.getEditor().getEditorComponent().addFocusListener(new FocusChanged());
	}	
	
	public Object getSelectedItem() {
		return model.getSelectedItem();
	}
	
	public void setSelectedIndex(int i) {
		comboBox.setSelectedIndex(i);
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
	
	public void insertNewItem() {
		if (getPartnerComboBox() != null) 
			comboBox.removeActionListener(selectionChanged);
		
		comboBox.setEditable(true);
		comboBox.getEditor().getEditorComponent().requestFocus();
		comboBox.getEditor().setItem("");
		
		if (model.getElementAt(0).equals(WHITESPACE)) {
			JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
			editor.setCaretPosition(0);
		}	
	}

	public void saveNewItem() {
		String newItem = comboBox.getEditor().getItem().toString().trim();
		
		if (model.getSize() == 1 && model.getIndexOf(WHITESPACE) != -1) {
			model.removeElement(WHITESPACE);
			model.addElement(newItem);
		} else if (model.getIndexOf(newItem) != -1) {
			if (getPartnerComboBox() != null) {
				comboBox.addActionListener(selectionChanged);
				comboBox.setEditable(false);
				if (this.isSecondComboBox()) 
					getPartnerComboBox().deleteFirstItem();
				comboBox.setSelectedItem(newItem);
				return;
			}
		} else  {
			//put the new element at the first position of the list
			Object[] items = new Object[model.getSize()];
			for (int i = 0; i < model.getSize(); i++)
				items[i] = model.getElementAt(i);
			
			model.removeAllElements();
			model.addElement(newItem);
			for (int i = 0; i < items.length; i++)
				model.addElement(items[i]);
		} 
		comboBox.setSelectedItem(newItem);
		comboBox.setEditable(false);
		
		if (getPartnerComboBox() != null) {
			if (this.isSecondComboBox())  {
				comboBox.addActionListener(selectionChanged);
				setSecondComboBox(false);
			} else {
				comboBox.addActionListener(selectionChanged);
				getPartnerComboBox().setSecondComboBox(true);
				getPartnerComboBox().insertNewItem();				
			}				
		} 
	}
	
	public void deleteSelectedItem() {
		if (getPartnerComboBox() != null) 
			comboBox.removeActionListener(selectionChanged);
		
		int index = comboBox.getSelectedIndex();
		model.removeElementAt(index);
		
		//no shrinking in case of no elements
		if (model.getSize() == 0) 
			model.addElement(WHITESPACE);
		
		//delete also the item from the partner ComboBox
		if (getPartnerComboBox() != null) {
			if (this.isSecondComboBox()) {
				comboBox.addActionListener(selectionChanged);
				setSecondComboBox(false);
			} else {
				comboBox.addActionListener(selectionChanged);
				getPartnerComboBox().setSecondComboBox(true);
				getPartnerComboBox().deleteSelectedItem();				
			}
		}
	}
	
	public void deleteFirstItem() {
		comboBox.removeActionListener(selectionChanged);
		comboBox.removeItemAt(0);
		comboBox.addActionListener(selectionChanged);
	}
	
	public void selectionChanged() {
		if (getPartnerComboBox() != null)
			if (this.isSecondComboBox())
				setSecondComboBox(false);
			else {
				getPartnerComboBox().setSecondComboBox(true);
				int i = comboBox.getSelectedIndex();
				getPartnerComboBox().setSelectedIndex(i);
			}
	}
	
	public void setPartnerComboBox(EditableJComboBoxPanel partnerComboBox) {
		this.partnerComboBox = partnerComboBox;
		comboBox.addActionListener(selectionChanged);
	}

	public EditableJComboBoxPanel getPartnerComboBox() {
		return partnerComboBox;
	}

	public void setSecondComboBox(boolean flag) {
		this.secondComboBox = flag;
	}

	public boolean isSecondComboBox() {
		return secondComboBox;
	}

	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}

	public boolean isEnterPressed() {
		return enterPressed;
	}

	private class FocusChanged implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {	
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if (isEnterPressed())
				setEnterPressed(false);
			else  {
				System.out.println("Focus lost");
				saveNewItem();
			}
		}
	}
	
	public class EnterPressed implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key = arg0.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
		    	setEnterPressed(true);
				saveNewItem();	
				System.out.println("enter pressed");
		    }
		}

		@Override
		public void keyReleased(KeyEvent arg0) {			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}	
	}
	
	private class NewItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			insertNewItem();
		}
	}
	
	private class DeleteItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			deleteSelectedItem();		
		}
	}
	
	private class SelectionChanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectionChanged();
		}	
	}
}
