/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consists of a text field for the height and a combobox for the units
 * @author Raimund
 *
 */
public class MissingHeightPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private static final Logger logger = LoggerFactory.getLogger(MissingHeightPanel.class);
	
	private final JLabel heightLabel;
	private final JTextField heightTextField = new JTextField(8);
	private final JLabel heightUnitLabel;
	private final JComboBox<String> heightUnitComboBox = new JComboBox<>(ComboBoxItems.getInstance().getHeightUnits());
	
	public MissingHeightPanel(final Position position) {
		super();
		this.position = position;	
		heightTextField.setText("0");
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		heightLabel = new JLabel("   " + Lang.l().altitude() + ": ");
		heightUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
		
		this.add(heightLabel);
		this.add(heightTextField);
		this.add(heightUnitLabel);
		this.add(heightUnitComboBox);
	}
	
	@Override
	public void assignValues() {
		final double value = Double.parseDouble(heightTextField.getText());
		final String unit = (String) heightUnitComboBox.getSelectedItem();
		final Height h = new Height(value, unit);
		position.setHeight(h);
	}
	
	@Override
	public void unassignValues() {
		position.setHeight(null);
	}

	@Override
	public boolean checkValues() {
		try {
			Double.parseDouble(heightTextField.getText());
		} catch (final NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    Lang.l().heightWarningDialogDecimalNumber(),
				    Lang.l().warningDialogTitle(),
				    JOptionPane.WARNING_MESSAGE);
			logger.error("The height has to be a decimal number.", e);
			return false;
		}
		
		return true;
	}

	@Override
	public Component getMissingComponent() {
		final double value = Double.parseDouble(heightTextField.getText());
		final String unit = (String) heightUnitComboBox.getSelectedItem();
		return new Height(value, unit);
	}

	@Override
	public void setMissingComponent(final Component c) {
		final Height height = (Height)c;
		heightTextField.setText(height.getValue() + "");
		heightUnitComboBox.setSelectedItem(height.getUnit());
	}
}