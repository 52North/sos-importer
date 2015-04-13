/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.view.combobox;

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

import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JCombobox with extended functionality: 
 *  - label in front of the combobox
 *  - insert a new item into the combobox after having clicked the corresponding button
 *  - delete an item from the combobox after having clicked the corresponding button
 *  - combine it with another combobox for synchronizing actions
 * @author Raimund
 *
 */
public class EditableJComboBoxPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(EditableJComboBoxPanel.class);
	
	private static final long serialVersionUID = 1L;
	
	private static final String ICON_FILE_PATH = "/org/n52/sos/importer/combobox/icons/";
	
	private static final String WHITESPACE = "                        ";
	
	private String lastSelectedItem;

	private final JLabel label;
	
	private final JComboBox comboBox;
	
	private final DefaultComboBoxModel model;
	
	private final ActionListener selectionChanged;
	
	private final JButton newItemButton;
	
	private final JButton deleteItemButton;
	
	private EditableJComboBoxPanel partnerComboBox;
	
	private boolean secondComboBox = false;
	
	private boolean enterPressed = false;
	
	/**
	 * @param model
	 * @param labelName
	 * @param toolTip
	 */
	public EditableJComboBoxPanel(final DefaultComboBoxModel model, final String labelName, final String toolTip) {
		super();
		this.model = model;
		label = new JLabel(labelName + ":   ");
		comboBox = new JComboBox(model);
		comboBox.setToolTipText(toolTip);
		
		if (model.getSize() == 0 || (model.getSize() == 1 && model.getElementAt(0).equals(""))) {
			model.addElement(WHITESPACE);
		}
		
		newItemButton = createIconButton("newItem.png", Lang.l().editableComboBoxNewItemButton());
		deleteItemButton = createIconButton("deleteItem.png", Lang.l().editableComboBoxDeleteItemButton());
		
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(label);
		this.add(comboBox);
		this.add(newItemButton);
		this.add(deleteItemButton);
		
		newItemButton.addActionListener(new NewItem());
		deleteItemButton.addActionListener(new DeleteItem());
		selectionChanged = new SelectionChanged();
		comboBox.getEditor().getEditorComponent().addKeyListener(new EnterOrESCPressed());
		comboBox.getEditor().getEditorComponent().addFocusListener(new FocusChanged());
	}	
	
	public Object getSelectedItem() {
		return model.getSelectedItem();
	}
	
	public void setSelectedIndex(int i) {
		final int max = comboBox.getModel().getSize();
		// fixing bug when having selected element nr 2 and deleting element nr 1
		if (i > max) {
			i = max;
		}
		comboBox.setSelectedIndex(i);
	}
	
	
	/**
	 * Set the value of the selected item. The selected item may be null. 
	 * @param item The combo box value or null for no selection.
	 */
	public void setSelectedItem(final Object item) {
		model.setSelectedItem(item);
	}
	
	public void addActionListener(final ActionListener al) {
		comboBox.addActionListener(al);
	}
	
	public boolean isEditable() {
		return comboBox.isEditable();
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected JButton createIconButton(final String fileName, final String toolTip) {
		final JButton iconButton = new JButton();
		final URL imgURL = getClass().getResource(ICON_FILE_PATH + fileName);
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
			removeSelectionChangeListener();
			getPartnerComboBox().disableButtons();
		}
		
		lastSelectedItem = (String) comboBox.getSelectedItem();
		comboBox.setEditable(true);
		comboBox.getEditor().getEditorComponent().requestFocus();
		comboBox.getEditor().setItem("");
		
		if (model.getElementAt(0).equals(WHITESPACE)) {
			final JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
			editor.setCaretPosition(0);
		}	
	}
	
	protected void escPressed() {
		comboBox.setEditable(false);
		comboBox.setSelectedItem(lastSelectedItem);
		enableButtons();
		if (getPartnerComboBox() != null) {
			getPartnerComboBox().enableButtons();
		}
	}

	public void saveNewItem() {
		String newItem = comboBox.getEditor().getItem().toString().trim();
		
		if (newItem.equals("")) { //whitespace entered
			if (getPartnerComboBox() == null || !isSecondComboBox()) {
				comboBox.setEditable(false);
				comboBox.setSelectedItem(lastSelectedItem);
				enableButtons();
				if (getPartnerComboBox() != null) {
					getPartnerComboBox().enableButtons();
				}
				return;
			} else { //when it is the second combobox
				newItem = getNextBiggestWhiteSpace(); //since no duplicate values are allowed
			}
		} else if (model.getSize() == 1 && model.getIndexOf(WHITESPACE) != -1) {
			model.removeElement(WHITESPACE);
		} else if (model.getIndexOf(newItem) != -1) { //element already in list
			if (getPartnerComboBox() != null) {
				comboBox.setEditable(false);
				if (isSecondComboBox()) {
					getPartnerComboBox().deleteFirstItem();
					setSecondComboBox(false);
				}
				comboBox.setSelectedItem(newItem);
				addSelectionChangeListener();
				enableButtons();
				getPartnerComboBox().enableButtons();
				return;
			}
		} else if (lastSelectedItem.trim().length() == 0) { //when last element was an empty string
			if (getPartnerComboBox() != null && !isSecondComboBox()) {
				//put element at the place of the old element
				final int index = model.getIndexOf(lastSelectedItem);
				model.removeElement(lastSelectedItem);
				
				final Object[] items = new Object[model.getSize()];
				for (int i = 0; i < model.getSize(); i++) {
					items[i] = model.getElementAt(i);
				}
				model.removeAllElements();
				
				for (int i = 0; i < index; i++) {
					model.addElement(items[i]);
				}
				model.addElement(newItem);
				for (int i = index; i < items.length; i++) {
					model.addElement(items[i]);
				}
				
				comboBox.setEditable(false);
				comboBox.setSelectedItem(newItem);
				enableButtons();
				getPartnerComboBox().enableButtons();
				return;
			}			
		}
		//put the new element at the first position of the list
		final Object[] items = new Object[model.getSize()];
		for (int i = 0; i < model.getSize(); i++) {
			items[i] = model.getElementAt(i);
		}
		
		model.removeAllElements();
		model.addElement(newItem);
		for (final Object item : items) {
			model.addElement(item);
		}
		
		comboBox.setEditable(false);
		comboBox.setSelectedItem(newItem);
		
		if (getPartnerComboBox() != null) {
			if (isSecondComboBox())  {
				addSelectionChangeListener();
				setSecondComboBox(false);
				enableButtons();
				getPartnerComboBox().enableButtons();
			} else {
				addSelectionChangeListener();
				getPartnerComboBox().setSecondComboBox(true);
				getPartnerComboBox().insertNewItem();				
			}				
		} else {
			enableButtons();
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
				final String item = (String) model.getElementAt(i);
				
				whiteSpaces = 0;
				for (final char ch: item.toCharArray()) {
					if (Character.isWhitespace(ch)) {
						whiteSpaces++;
					}
				}
				
				if (whiteSpaces > maxWhiteSpaces) {
					maxWhiteSpaces = whiteSpaces;
				}
			}
			
			String newBiggestWhiteSpace = " ";
			for (int j = 0; j < maxWhiteSpaces; j++) {
				newBiggestWhiteSpace += " ";
			}

			return newBiggestWhiteSpace;
		}
	}
	
	private void deleteFirstItem() {
		removeSelectionChangeListener();
		comboBox.removeItemAt(0);
		addSelectionChangeListener();
	}
	
	public void deleteSelectedItem() {
		if (getPartnerComboBox() != null) {
			removeSelectionChangeListener();
		}
		
		final int index = comboBox.getSelectedIndex();
		if (index > -1) {
			model.removeElementAt(index);
		}
		
		//no shrinking in case of no elements
		if (model.getSize() == 0) {
			model.addElement(WHITESPACE);
			comboBox.setSelectedItem(WHITESPACE);
		}
		
		//delete also the item from the partner ComboBox
		if (getPartnerComboBox() != null) {
			if (isSecondComboBox()) {
				addSelectionChangeListener();
				setSecondComboBox(false);
			} else {
				addSelectionChangeListener();
				getPartnerComboBox().setSecondComboBox(true);
				getPartnerComboBox().deleteSelectedItem();				
			}
		}
		// set selectedItem to null to prevent NPE if current selected 
		// itemIndex > combox.getModel().getSize()
		setSelectedItem(null);
	}
	
	public void selectionChanged() {
		if (getPartnerComboBox() != null) {
			final int i = comboBox.getSelectedIndex();
			getPartnerComboBox().removeSelectionChangeListener();
			getPartnerComboBox().setSelectedIndex(i);
			getPartnerComboBox().addSelectionChangeListener();
		}		
	}
	
	public void setPartnerComboBox(final EditableJComboBoxPanel partnerComboBox) {
		this.partnerComboBox = partnerComboBox;
		addSelectionChangeListener();
	}

	public EditableJComboBoxPanel getPartnerComboBox() {
		return partnerComboBox;
	}

	public void setSecondComboBox(final boolean flag) {
		secondComboBox = flag;
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
	
	public void addSelectionChangeListener() {
		comboBox.addActionListener(selectionChanged);
	}
	
	public void removeSelectionChangeListener() {
		comboBox.removeActionListener(selectionChanged);
	}

	public void setEnterPressed(final boolean enterPressed) {
		this.enterPressed = enterPressed;
	}

	public boolean isEnterPressed() {
		return enterPressed;
	}

	private class FocusChanged implements FocusListener {

		@Override
		public void focusGained(final FocusEvent arg0) {	
		}

		@Override
		public void focusLost(final FocusEvent arg0) {
			if (isEnterPressed()) {
				setEnterPressed(false);
			} else {
				saveNewItem();
			}
		}
	}
	
	private class EnterOrESCPressed implements KeyListener {

		@Override
		public void keyPressed(final KeyEvent arg0) {
			final int key = arg0.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
		    	setEnterPressed(true);
				saveNewItem();
		    } else if (key == KeyEvent.VK_ESCAPE) {
		    	escPressed();
		    }
		}

		@Override
		public void keyReleased(final KeyEvent arg0) {			
		}

		@Override
		public void keyTyped(final KeyEvent arg0) {
		}	
	}
	
	private class NewItem implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			if (!comboBox.isEditable()) {
				insertNewItem();
			}
		}
	}
	
	private class DeleteItem implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (!comboBox.isEditable()) {
				deleteSelectedItem();
			}		
		}
	}
	
	private class SelectionChanged implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			selectionChanged();
		}	
	}
}
