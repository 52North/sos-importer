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
package org.n52.sos.importer.view.step3;

import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.table.TableElement;

/**
 * used for different kinds of selections in step 3
 *
 * @author Raimund
 * @version $Id: $Id
 */
public abstract class SelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JPanel containerPanel;

    private HashMap<String, SelectionPanel> childPanels = new HashMap<String, SelectionPanel>();

    private SelectionPanel selectedChildPanel;

    /**
     * <p>Constructor for SelectionPanel.</p>
     *
     * @param containerPanel a {@link javax.swing.JPanel} object.
     */
    public SelectionPanel(JPanel containerPanel) {
        super();
        this.containerPanel = containerPanel;
    }

    /**
     * This method is called when the settings for the step controller are loaded
     *
     * @param s a {@link java.lang.String} object.
     */
    protected abstract void setSelection(String s);

    /**
     * Returns the current selected options done by the user
     *
     * @return a {@linkplain java.lang.String} with the encoded user input
     */
    protected abstract String getSelection();

    /**
     * called when a combobox which contains patterns
     * changes its selection
     */
    protected void patternChanged() {
    }

    /**
     * called when a selection has been restored and the panel is added to view
     */
    protected void reInit() {
    }

    /**
     * called to restore the selection of a column
     * in step 3
     *
     * @param selections a {@link java.util.List} object.
     */
    public void restore(List<String> selections) {
        getContainerPanel().add(this);
        String s = selections.get(0);
        setSelection(s);
        SelectionPanel childPanel = childPanels.get(s);
        selections.remove(0);
        reInit();
        setSelectedChildPanel(childPanel);

        if (childPanel != null) {
            childPanel.restore(selections);
        }
    }

    /**
     * called when a column in step 3 has
     * not been visited yet
     */
    public void restoreDefault() {
        setDefaultSelection();

        for (SelectionPanel sp: childPanels.values()) {
            sp.restoreDefault();
        }
    }

    /**
     * <p>setDefaultSelection.</p>
     */
    public abstract void setDefaultSelection();

    /**
     * stores the current selection for this column
     * in step 3 in the {@linkplain org.n52.sos.importer.model.ModelStore}
     *
     * @param selections list of all selected items, e.g. column type and the corresponding meta data
     */
    public void store(List<String> selections) {
        // get our own selection and add them to the selections list
        String s = this.getSelection();
        selections.add(s);
        // ask my child panels if one is
        SelectionPanel childPanel = childPanels.get(s);
        if (childPanel != null) {
            childPanel.store(selections);
        }
    }

    /**
     * <p>Getter for the field <code>containerPanel</code>.</p>
     *
     * @return a {@link javax.swing.JPanel} object.
     */
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    /**
     * <p>Getter for the field <code>selectedChildPanel</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.view.step3.SelectionPanel} object.
     */
    public SelectionPanel getSelectedChildPanel() {
        return selectedChildPanel;
    }

    /**
     * called when the child panel's selection changes
     *
     * @param childPanel a {@link org.n52.sos.importer.view.step3.SelectionPanel} object.
     */
    public void setSelectedChildPanel(SelectionPanel childPanel) {
        this.selectedChildPanel = childPanel;
    }

    /**
     * called when the child panel's selection changes
     *
     * @param childPanelName a {@link java.lang.String} object.
     */
    public void setSelectedChildPanel(String childPanelName) {
        this.selectedChildPanel = childPanels.get(childPanelName);
    }

    /**
     * adds a child panel to the list
     *
     * @param childPanelName a {@link java.lang.String} object.
     * @param childPanel a {@link org.n52.sos.importer.view.step3.SelectionPanel} object.
     */
    public void addChildPanel(String childPanelName, SelectionPanel childPanel) {
        childPanels.put(childPanelName, childPanel);
    }

    /**
     * adds this and recursively the selected child panel
     * to one of the container panels of step 3
     */
    public void addToContainerPanel() {
        getContainerPanel().add(this);
        SelectionPanel childPanel = getSelectedChildPanel();
        reInit();

        if (childPanel != null) {
            setSelectedChildPanel(childPanel);
            childPanel.addToContainerPanel();
        }
    }

    /**
     * removes this selection panel from its container
     * and recursively all child selection panels
     */
    public void removeFromContainerPanel() {
        getContainerPanel().removeAll();

        for (SelectionPanel sp: childPanels.values()) {
            sp.removeFromContainerPanel();
        }
    }

    /**
     * assigns this selection to a column, row or a cell
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public abstract void assign(TableElement tableElement);

    /**
     * unassigns this selection from a column, row or a cell
     *
     * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
     */
    public abstract void unAssign(TableElement tableElement);
}
