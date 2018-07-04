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
package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.wizard.utils.EPSGHelper;
import org.n52.sos.importer.wizard.utils.ToolTips;
import org.opengis.referencing.cs.CoordinateSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consists of a combobox for the EPSG code and a combobox for the
 * name of the spatial reference system; both are linked with each other
 *
 * @author Raimund
 */
public class MissingEPSGCodePanel extends MissingComponentPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MissingEPSGCodePanel.class);

    private final Position position;

    private final EditableJComboBoxPanel EPSGCodeComboBox;

    private List<MissingPositionComponentPanel> componentPanels;

    private MissingEPSGCodePanel this_;

    /**
     * <p>Constructor for MissingEPSGCodePanel.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public MissingEPSGCodePanel(Position position) {
        super();
        this_ = this;
        this.position = position;
        componentPanels = new LinkedList<>();

        setLayout(new FlowLayout(FlowLayout.LEFT));
        EPSGCodeComboBox = new EditableJComboBoxPanel(
                EditableComboBoxItems.getInstance().getEPSGCodes(),
                Lang.l().epsgCode(),
                ToolTips.get(ToolTips.EPSG));
        EPSGCodeComboBox.setSelectedIndex(-1);
        EPSGCodeComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = (String) EPSGCodeComboBox.getSelectedItem();
                if (selectedText == null || selectedText.isEmpty()) {
                    hidePanels();
                    return;
                }
                int selectedItem = Integer.parseInt(selectedText);
                // get CS
                if (EPSGHelper.isValidEPSGCode(selectedItem)) {
                    CoordinateSystem cs = EPSGHelper.getCoordinateSystem(selectedItem);
                    // depending on CS disable or enabled other panels
                    if (cs.getDimension() == 2) {
                        triggerCoord2Panel(false);
                    } else {
                        triggerCoord2Panel(true);
                    }
                } else {
                    triggerCoord2Panel(true);
                }

            }

            private void hidePanels() {
                for (MissingPositionComponentPanel mpcp : this_.componentPanels) {
                    mpcp.setVisible(false);
                }
            }

            private void triggerCoord2Panel(boolean value) {
                for (MissingPositionComponentPanel mpcp : this_.componentPanels) {
                    if (mpcp != null && mpcp.getId().equals(Id.COORD_2)) {
                        mpcp.setVisible(value);
                    }
                }
            }
        });
        add(EPSGCodeComboBox);
    }

    @Override
    public void assignValues() {
        final int code = Integer.parseInt((String) EPSGCodeComboBox.getSelectedItem());
        position.setEPSGCode(new EPSGCode(code));
    }

    @Override
    public void unassignValues() {
        position.setEPSGCode(null);
    }

    @Override
    public boolean checkValues() {
        int code = 0;
        try {
            code = Integer.parseInt((String) EPSGCodeComboBox.getSelectedItem());
        } catch (final NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    Lang.l().epsgCodeWarningDialogNaturalNumber(),
                    Lang.l().warningDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            logger.error("The EPSG code has be a natural number.", e);
            return false;
        }

        if (code < 0 || code > 32767) {
            JOptionPane.showMessageDialog(null,
                    Lang.l().epsgCodeWarningDialogOutOfRange(),
                    Lang.l().warningDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            logger.error("The EPSG-Code has to be in the range of 0 and 32767.");
            return false;
        }

        return true;
    }

    @Override
    public Component getMissingComponent() {
        final int code = Integer.parseInt((String) EPSGCodeComboBox.getSelectedItem());
        return new EPSGCode(code);
    }

    @Override
    public void setMissingComponent(final Component c) {
        EPSGCodeComboBox.setSelectedItem(String.valueOf(((EPSGCode) c).getValue()));
    }

    public void addCoordinatePanels(List<MissingComponentPanel> missingComponentPanels) {
        for (MissingComponentPanel mcp : missingComponentPanels) {
            if (mcp instanceof MissingPositionComponentPanel) {
                componentPanels.add((MissingPositionComponentPanel) mcp);
            }
        }
    }
}
