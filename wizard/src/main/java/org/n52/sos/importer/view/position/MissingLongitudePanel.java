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
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consists of a text field for the longitude and a combobox for the units
 * @author Raimund
 *
 */
public class MissingLongitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(MissingLongitudePanel.class);

	private final Position position;
	
	private final JLabel longitudeLabel;
	private final JTextField longitudeTextField = new JTextField(8);
	private final JLabel longitudeUnitLabel;
	private final JComboBox<String> longitudeUnitComboBox = new JComboBox<>(ComboBoxItems.getInstance().getLatLonUnits());
	
	public MissingLongitudePanel(final Position position) {
		super();
		this.position = position;
		longitudeTextField.setText("0");
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		longitudeLabel = new JLabel("   " + Lang.l().longitudeEasting() + ": ");
		this.add(longitudeLabel);
		this.add(longitudeTextField);
		longitudeUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
		this.add(longitudeUnitLabel);
		this.add(longitudeUnitComboBox);
	}
	
	@Override
	public void assignValues() {
		final double value = Double.parseDouble(longitudeTextField.getText());
		final String unit = (String) longitudeUnitComboBox.getSelectedItem();
		final Longitude l = new Longitude(value, unit);
		position.setLongitude(l);
	}
	
	@Override
	public void unassignValues() {
		position.setLongitude(null);
	}

	@Override
	public boolean checkValues() {
		String longVal = null;
		try {
			longVal = longitudeTextField.getText();
			Double.parseDouble(longVal);
		} catch (final NumberFormatException e) {
			logger.error("Given Longitude value could not be parsed: " + longVal, e);
			JOptionPane.showMessageDialog(null,
				    Lang.l().longitudeDialogDecimalValue(),
				    Lang.l().warningDialogTitle(),
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		final double value = Double.parseDouble(longitudeTextField.getText());
		final String unit = (String) longitudeUnitComboBox.getSelectedItem();
		return new Longitude(value, unit);
	}

	@Override
	public void setMissingComponent(final Component c) {
		final Longitude longitude = (Longitude)c;
		longitudeTextField.setText(longitude.getValue() + "");
		longitudeUnitComboBox.setSelectedItem(longitude.getUnit());
	}
}