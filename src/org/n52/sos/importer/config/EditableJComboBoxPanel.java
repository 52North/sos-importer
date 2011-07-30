package org.n52.sos.importer.config;

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
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

public class EditableJComboBoxPanel extends JPanel {

	private static final Logger logger = Logger.getLogger(EditableJComboBoxPanel.class);
	
	private static final long serialVersionUID = 1L;
	
	private static final String WHITESPACE = "                        ";
	
	private String lastSelectedItem;

	private JLabel label;
	
	private JComboBox comboBox;
	
	private DefaultComboBoxModel model;
	
	private ActionListener selectionChanged = new SelectionChanged();
	
	private JButton newItemButton;
	
	private JButton deleteItemButton;
	
	private EditableJComboBoxPanel partnerComboBox;
	
	private boolean secondComboBox = false;
	
	private boolean enterPressed = false;
	
	public EditableJComboBoxPanel(DefaultComboBoxModel model, String labelName) {
		super();
		this.model = model;
		label = new JLabel(labelName + ":   ");
		comboBox = new JComboBox(model);
		
		if (model.getSize() == 0) 
			model.addElement(WHITESPACE);
		
		newItemButton = createIconButton("newItem.png", "Add a new item to the list");
		deleteItemButton = createIconButton("deleteItem.png", "Delete the selected item from the list");
		
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
	
	public boolean isEditable() {
		return comboBox.isEditable();
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected JButton createIconButton(String fileName, String toolTip) {
		JButton iconButton = new JButton();
		URL imgURL = getClass().getResource("/org/n52/sos/importer/icons/" + fileName);
		ImageIcon icon = null;
		if (imgURL != null) {
			icon = new ImageIcon(imgURL);
	    } else {
	        logger.error("Couldn't find file " + fileName + " for icon");
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
		disableButtons();
		
		if (getPartnerComboBox() != null) {
			comboBox.removeActionListener(selectionChanged);
			getPartnerComboBox().disableButtons();
		}
		
		lastSelectedItem = (String) comboBox.getSelectedItem();
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
		
		if (newItem.equals("")) { //whitespace entered
			if (getPartnerComboBox() == null || !isSecondComboBox()) {
				comboBox.setEditable(false);
				comboBox.setSelectedItem(lastSelectedItem);
				enableButtons();
				if (getPartnerComboBox() != null)
					getPartnerComboBox().enableButtons();
				return;
			} else { //when it is the second combobox
				newItem = getNextBiggestWhiteSpace(); //since no duplicate values are allowed
			}
		} else if (model.getSize() == 1 && model.getIndexOf(WHITESPACE) != -1) {
			model.removeElement(WHITESPACE);
		} else if (model.getIndexOf(newItem) != -1) { //element already in list
			if (getPartnerComboBox() != null) {
				comboBox.addActionListener(selectionChanged);
				comboBox.setEditable(false);
				if (this.isSecondComboBox()) 
					getPartnerComboBox().deleteFirstItem();
				comboBox.setSelectedItem(newItem);
				enableButtons();
				getPartnerComboBox().enableButtons();
				return;
			}
		} else if (lastSelectedItem.trim().length() == 0) { //when last element was an empty string
			if (getPartnerComboBox() != null && !isSecondComboBox()) {
				//put element at the place of the old element
				int index = model.getIndexOf(lastSelectedItem);
				model.removeElement(lastSelectedItem);
				
				Object[] items = new Object[model.getSize()];
				for (int i = 0; i < model.getSize(); i++)
					items[i] = model.getElementAt(i);
				model.removeAllElements();
				
				for (int i = 0; i < index; i++)
					model.addElement(items[i]);
				model.addElement(newItem);
				for (int i = index; i < items.length; i++)
					model.addElement(items[i]);
				
				comboBox.setEditable(false);
				comboBox.setSelectedItem(newItem);
				enableButtons();
				getPartnerComboBox().enableButtons();
				return;
			}			
		}
		//put the new element at the first position of the list
		Object[] items = new Object[model.getSize()];
		for (int i = 0; i < model.getSize(); i++)
			items[i] = model.getElementAt(i);
		
		model.removeAllElements();
		model.addElement(newItem);
		for (int i = 0; i < items.length; i++)
			model.addElement(items[i]);
		
		comboBox.setEditable(false);
		comboBox.setSelectedItem(newItem);
		
		if (getPartnerComboBox() != null) {
			if (this.isSecondComboBox())  {
				comboBox.addActionListener(selectionChanged);
				setSecondComboBox(false);
				enableButtons();
				getPartnerComboBox().enableButtons();
			} else {
				comboBox.addActionListener(selectionChanged);
				getPartnerComboBox().setSecondComboBox(true);
				getPartnerComboBox().insertNewItem();				
			}				
		} 
	}
	
	private String getNextBiggestWhiteSpace() {
		if (model.getIndexOf(WHITESPACE) != -1) {
			model.removeElement(WHITESPACE);
			return " ";
		}
		else {
			int maxWhiteSpaces = 0;
			int whiteSpaces = 0;
			for (int i = 0; i < model.getSize(); i++) {
				String item = (String) model.getElementAt(i);
				
				whiteSpaces = 0;
				for (char ch: item.toCharArray())
					if (Character.isWhitespace(ch)) 
						whiteSpaces++;
				
				if (whiteSpaces > maxWhiteSpaces) 
					maxWhiteSpaces = whiteSpaces;
			}
			
			String newBiggestWhiteSpace = " ";
			for (int j = 0; j < maxWhiteSpaces; j++)
				newBiggestWhiteSpace += " ";

			return newBiggestWhiteSpace;
		}
	}
	
	private void deleteFirstItem() {
		comboBox.removeActionListener(selectionChanged);
		comboBox.removeItemAt(0);
		comboBox.addActionListener(selectionChanged);
	}
	
	public void deleteSelectedItem() {
		if (getPartnerComboBox() != null) 
			comboBox.removeActionListener(selectionChanged);
		
		int index = comboBox.getSelectedIndex();
		model.removeElementAt(index);
		
		//no shrinking in case of no elements
		if (model.getSize() == 0) {
			model.addElement(WHITESPACE);
			comboBox.setSelectedItem(WHITESPACE);
		}
		
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
	
	public void enableButtons() {
		newItemButton.setEnabled(true);
		deleteItemButton.setEnabled(true);
	}
	
	public void disableButtons() {
		newItemButton.setEnabled(false);
		deleteItemButton.setEnabled(false);
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
			else 
				saveNewItem();
		}
	}
	
	private class EnterPressed implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key = arg0.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
		    	setEnterPressed(true);
				saveNewItem();
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
			if (!comboBox.isEditable())
				insertNewItem();
		}
	}
	
	private class DeleteItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(!comboBox.isEditable());
			if (!comboBox.isEditable())
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
