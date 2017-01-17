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
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consists of a text field for the latitude and a combobox for the units
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class MissingLatitudePanel extends MissingComponentPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MissingLatitudePanel.class);

    private final Position position;

    private final JLabel latitudeLabel;
    private final JTextField latitudeTextField = new JTextField(8);
    private final JLabel latitudeUnitLabel;
    private final JComboBox<String> latitudeUnitComboBox = new JComboBox<>(ComboBoxItems.getInstance().getLatLonUnits());

    /**
     * <p>Constructor for MissingLatitudePanel.</p>
     *
     * @param position a {@link org.n52.sos.importer.model.position.Position} object.
     */
    public MissingLatitudePanel(final Position position) {
        super();
        this.position = position;
        latitudeTextField.setText("0");

        setLayout(new FlowLayout(FlowLayout.LEFT));
        latitudeLabel = new JLabel("   " + Lang.l().latitudeNorthing() + ": ");
        this.add(latitudeLabel);
        this.add(latitudeTextField);
        latitudeUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
        this.add(latitudeUnitLabel);
        this.add(latitudeUnitComboBox);
    }

    /** {@inheritDoc} */
    @Override
    public void assignValues() {
        final double value = Double.parseDouble(latitudeTextField.getText());
        final String unit = (String) latitudeUnitComboBox.getSelectedItem();
        final Latitude l = new Latitude(value, unit);
        position.setLatitude(l);
    }

    /** {@inheritDoc} */
    @Override
    public void unassignValues() {
        position.setLatitude(null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkValues() {
        String latVal = null;
        try {
            latVal = latitudeTextField.getText();
            Double.parseDouble(latVal);
        } catch (final NumberFormatException e) {
            logger.error("Latitude value could not be parsed: " + latVal, e);
            JOptionPane.showMessageDialog(null,
                    Lang.l().latitudeDialogDecimalValue(),
                    Lang.l().warningDialogTitle(),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Component getMissingComponent() {
        final double value = Double.parseDouble(latitudeTextField.getText());
        final String unit = (String) latitudeUnitComboBox.getSelectedItem();
        return new Latitude(value, unit);
    }

    /** {@inheritDoc} */
    @Override
    public void setMissingComponent(final Component c) {
        final Latitude latitude = (Latitude)c;
        latitudeTextField.setText(latitude.getValue() + "");
        latitudeUnitComboBox.setSelectedItem(latitude.getUnit());
    }
}
