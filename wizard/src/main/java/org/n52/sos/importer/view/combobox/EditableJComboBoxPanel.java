/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.view.combobox;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
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
 *
 * @author Raimund
 */
public final class EditableJComboBoxPanel extends JPanel {

    private static final Logger LOG = LoggerFactory.getLogger(EditableJComboBoxPanel.class);

    private static final long serialVersionUID = 1L;

    private static final String ICON_FILE_PATH = "/org/n52/sos/importer/combobox/icons/";

    private static final String WHITESPACE = "                        ";

    private String lastSelectedItem;

    private final JLabel label;

    private final JComboBox<String> comboBox;

    private final DefaultComboBoxModel<String> model;

    private final ActionListener selectionChanged;

    private final JButton newItemButton;

    private final JButton deleteItemButton;

    private EditableJComboBoxPanel partnerComboBox;

    private boolean secondComboBox;

    private boolean enterPressed;

    /**
     * <p>Constructor for EditableJComboBoxPanel.</p>
     *
     * @param model a {@link javax.swing.DefaultComboBoxModel} object.
     * @param labelName a {@link java.lang.String} object.
     * @param toolTip a {@link java.lang.String} object.
     */
    public EditableJComboBoxPanel(final DefaultComboBoxModel<String> model,
            final String labelName,
            final String toolTip) {
        super();
        this.model = model;
        label = new JLabel(labelName + ":   ");
        comboBox = new JComboBox<>(model);
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

    /**
     * <p>getSelectedItem.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getSelectedItem() {
        return model.getSelectedItem();
    }

    /**
     * <p>setSelectedIndex.</p>
     *
     * @param i a int.
     */
    public void setSelectedIndex(final int i) {
        final int max = comboBox.getModel().getSize();
        // fixing bug when having selected element nr 2 and deleting element nr 1
        int index = i;
        if (index > max) {
            index = max;
        }
        comboBox.setSelectedIndex(index);
    }


    /**
     * Set the value of the selected item. The selected item may be null.
     *
     * @param item The combo box value or null for no selection.
     */
    public void setSelectedItem(final Object item) {
        model.setSelectedItem(item);
    }

    /**
     * <p>addActionListener.</p>
     *
     * @param al a {@link java.awt.event.ActionListener} object.
     */
    public void addActionListener(final ActionListener al) {
        comboBox.addActionListener(al);
    }

    /**
     * <p>isEditable.</p>
     *
     * @return a boolean.
     */
    public boolean isEditable() {
        return comboBox.isEditable();
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param fileName a {@link java.lang.String} object.
     * @param toolTip a {@link java.lang.String} object.
     * @return a {@link javax.swing.JButton} object.
     */
    protected JButton createIconButton(final String fileName, final String toolTip) {
        final JButton iconButton = new JButton();
        final URL imgURL = getClass().getResource(ICON_FILE_PATH + fileName);
        ImageIcon icon;
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        } else {
            LOG.error("Couldn't find file " + fileName + " for icon");
            return null;
        }

        iconButton.setIcon(icon);
        iconButton.setContentAreaFilled(false);
        iconButton.setBorderPainted(false);
        iconButton.setToolTipText(toolTip);
        iconButton.setPreferredSize(new Dimension(20, 20));

        return iconButton;
    }

    /**
     * <p>insertNewItem.</p>
     */
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

    /**
     * <p>escPressed.</p>
     */
    protected void escPressed() {
        comboBox.setEditable(false);
        comboBox.setSelectedItem(lastSelectedItem);
        enableButtons();
        if (getPartnerComboBox() != null) {
            getPartnerComboBox().enableButtons();
        }
    }

    /**
     * <p>saveNewItem.</p>
     */
    public void saveNewItem() {
        String newItem = comboBox.getEditor().getItem().toString().trim();

        //whitespace entered
        if (newItem.equals("")) {
            if (getPartnerComboBox() == null || !isSecondComboBox()) {
                comboBox.setEditable(false);
                comboBox.setSelectedItem(lastSelectedItem);
                enableButtons();
                if (getPartnerComboBox() != null) {
                    getPartnerComboBox().enableButtons();
                }
                return;
                // when it is the second combobox
            } else {
                // since no duplicate values are allowed
                newItem = getNextBiggestWhiteSpace();
            }
        } else if (model.getSize() == 1 && model.getIndexOf(WHITESPACE) != -1) {
            model.removeElement(WHITESPACE);
            // element already in list
        } else if (model.getIndexOf(newItem) != -1) {
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
            // when last element was an empty string
        } else if (lastSelectedItem.trim().length() == 0) {
            if (getPartnerComboBox() != null && !isSecondComboBox()) {
                //put element at the place of the old element
                final int index = model.getIndexOf(lastSelectedItem);
                model.removeElement(lastSelectedItem);

                final String[] items = new String[model.getSize()];
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
        final String[] items = new String[model.getSize()];
        for (int i = 0; i < model.getSize(); i++) {
            items[i] = model.getElementAt(i);
        }

        model.removeAllElements();
        model.addElement(newItem);
        for (final String item : items) {
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
        } else {
            int maxWhiteSpaces = 0;
            for (int i = 0; i < model.getSize(); i++) {
                final String item = model.getElementAt(i);

                int whiteSpaces = 0;
                for (final char ch: item.toCharArray()) {
                    if (Character.isWhitespace(ch)) {
                        whiteSpaces++;
                    }
                }

                if (whiteSpaces > maxWhiteSpaces) {
                    maxWhiteSpaces = whiteSpaces;
                }
            }

            StringBuilder newBiggestWhiteSpace = new StringBuilder(" ");
            for (int j = 0; j < maxWhiteSpaces; j++) {
                newBiggestWhiteSpace.append(" ");
            }
            newBiggestWhiteSpace.trimToSize();
            return newBiggestWhiteSpace.toString();
        }
    }

    private void deleteFirstItem() {
        removeSelectionChangeListener();
        comboBox.removeItemAt(0);
        addSelectionChangeListener();
    }

    /**
     * <p>deleteSelectedItem.</p>
     */
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

    /**
     * <p>selectionChanged.</p>
     */
    public void selectionChanged() {
        if (getPartnerComboBox() != null) {
            final int i = comboBox.getSelectedIndex();
            getPartnerComboBox().removeSelectionChangeListener();
            getPartnerComboBox().setSelectedIndex(i);
            getPartnerComboBox().addSelectionChangeListener();
        }
    }

    /**
     * <p>Setter for the field <code>partnerComboBox</code>.</p>
     *
     * @param partnerComboBox a {@link org.n52.sos.importer.view.combobox.EditableJComboBoxPanel} object.
     */
    public void setPartnerComboBox(final EditableJComboBoxPanel partnerComboBox) {
        this.partnerComboBox = partnerComboBox;
        addSelectionChangeListener();
    }

    /**
     * <p>Getter for the field <code>partnerComboBox</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.view.combobox.EditableJComboBoxPanel} object.
     */
    public EditableJComboBoxPanel getPartnerComboBox() {
        return partnerComboBox;
    }

    /**
     * <p>Setter for the field <code>secondComboBox</code>.</p>
     *
     * @param flag a boolean.
     */
    public void setSecondComboBox(final boolean flag) {
        secondComboBox = flag;
    }

    /**
     * <p>isSecondComboBox.</p>
     *
     * @return a boolean.
     */
    public boolean isSecondComboBox() {
        return secondComboBox;
    }

    /**
     * <p>enableButtons.</p>
     */
    public void enableButtons() {
        newItemButton.setEnabled(true);
        deleteItemButton.setEnabled(true);
    }

    /**
     * <p>disableButtons.</p>
     */
    public void disableButtons() {
        newItemButton.setEnabled(false);
        deleteItemButton.setEnabled(false);
    }

    /**
     * <p>addSelectionChangeListener.</p>
     */
    public void addSelectionChangeListener() {
        comboBox.addActionListener(selectionChanged);
    }

    /**
     * <p>removeSelectionChangeListener.</p>
     */
    public void removeSelectionChangeListener() {
        comboBox.removeActionListener(selectionChanged);
    }

    /**
     * <p>Setter for the field <code>enterPressed</code>.</p>
     *
     * @param enterPressed a boolean.
     */
    public void setEnterPressed(final boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    /**
     * <p>isEnterPressed.</p>
     *
     * @return a boolean.
     */
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

    private class SelectionChanged implements ActionListener, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            selectionChanged();
        }
    }
}
