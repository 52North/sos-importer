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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.position.PositionComponent;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consists of a text field for the coordinate value and a combobox for the unit
 *
 * @author Raimund
 */
public class MissingPositionComponentPanel extends MissingComponentPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MissingPositionComponentPanel.class);

    private final Position position;

    private final JLabel label;
    private final JTextField textField = new JTextField(8);
    private final JLabel unitLabel;
    private final JComboBox<String> unitComboBox = new JComboBox<>(ComboBoxItems.getInstance().getLatLonUnits());

    private Id id;

    /**
     * <p>Constructor for MissingPositionComponentPanel.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public MissingPositionComponentPanel(Id id, Position position) {
        super();
        this.position = position;
        this.id = id;
        textField.setText("0");

        setLayout(new FlowLayout(FlowLayout.LEFT));
        final String labelSpacer = "   ";
        final String colon = ": ";
        label = new JLabel(labelSpacer + id.name() + colon);
        this.add(label);
        this.add(textField);
        unitLabel = new JLabel(labelSpacer + Lang.l().unit() + colon);
        this.add(unitLabel);
        this.add(unitComboBox);
    }

    @Override
    public void assignValues() {
        final double value = Double.parseDouble(textField.getText());
        final String unit = (String) unitComboBox.getSelectedItem();
        final PositionComponent l = new PositionComponent(id, value, unit);
        position.addCoordinate(l);
    }

    @Override
    public void unassignValues() {
        position.removeCoordinate(id);
    }

    @Override
    public boolean checkValues() {
        String val = null;
        try {
            val = textField.getText();
            Double.parseDouble(val);
        } catch (final NumberFormatException e) {
            logger.error("Coordinate value could not be parsed: " + val, e);
            JOptionPane.showMessageDialog(null,
                    Lang.l().coordinateDialogDecimalValue(),
                    Lang.l().warningDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public Component getMissingComponent() {
        final double value = Double.parseDouble(textField.getText());
        final String unit = (String) unitComboBox.getSelectedItem();
        return new PositionComponent(id, value, unit);
    }

    @Override
    public void setMissingComponent(final Component c) {
        final PositionComponent coordinate = (PositionComponent) c;
        textField.setText(coordinate.getValue() + "");
        unitComboBox.setSelectedItem(coordinate.getUnit());
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
