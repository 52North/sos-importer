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

import javax.swing.JOptionPane;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consists of a combobox for the EPSG code and a combobox for the
 * name of the spatial reference system; both are linked with each other
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class MissingEPSGCodePanel extends MissingComponentPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MissingEPSGCodePanel.class);

    private final Position position;

    private final EditableJComboBoxPanel EPSGCodeComboBox;
    private final EditableJComboBoxPanel referenceSystemNameComboBox;

    /**
     * <p>Constructor for MissingEPSGCodePanel.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public MissingEPSGCodePanel(final Position position) {
        super();
        this.position = position;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        referenceSystemNameComboBox = new EditableJComboBoxPanel(
                EditableComboBoxItems.getInstance().getReferenceSystemNames(),
                Lang.l().referenceSystem(),
                ToolTips.get(ToolTips.REFERENCE_SYSTEMS));
        EPSGCodeComboBox = new EditableJComboBoxPanel(
                EditableComboBoxItems.getInstance().getEPSGCodes(),
                Lang.l().epsgCode(),
                ToolTips.get(ToolTips.EPSG));
        this.add(referenceSystemNameComboBox);
        this.add(EPSGCodeComboBox);
        EPSGCodeComboBox.setPartnerComboBox(referenceSystemNameComboBox);
        referenceSystemNameComboBox.setPartnerComboBox(EPSGCodeComboBox);
    }
    /** {@inheritDoc} */
    @Override
    public void assignValues() {
        final int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
        position.setEPSGCode(new EPSGCode(code));
    }

    /** {@inheritDoc} */
    @Override
    public void unassignValues() {
        position.setEPSGCode(null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkValues() {
        int code = 0;
        try {
            code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
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

    /** {@inheritDoc} */
    @Override
    public Component getMissingComponent() {
        final int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
        return new EPSGCode(code);
    }

    /** {@inheritDoc} */
    @Override
    public void setMissingComponent(final Component c) {
        EPSGCodeComboBox.setSelectedItem(String.valueOf(((EPSGCode)c).getValue()));
    }
}
