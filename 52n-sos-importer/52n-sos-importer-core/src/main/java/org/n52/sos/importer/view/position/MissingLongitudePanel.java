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
package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a text field for the longitude and a combobox for the units
 * @author Raimund
 *
 */
public class MissingLongitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MissingLongitudePanel.class);

	private final Position position;
	
	private final JLabel longitudeLabel;
	private final JTextField longitudeTextField = new JTextField(8);
	private final JLabel longitudeUnitLabel;
	private final JComboBox longitudeUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getLatLonUnits());
	
	public MissingLongitudePanel(Position position) {
		super();
		this.position = position;
		longitudeTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.longitudeLabel = new JLabel("   " + Lang.l().longitudeEasting() + ": ");
		this.add(longitudeLabel);
		this.add(longitudeTextField);
		this.longitudeUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
		this.add(longitudeUnitLabel);
		this.add(longitudeUnitComboBox);
	}
	
	public void assignValues() {
		double value = Double.parseDouble(longitudeTextField.getText());
		String unit = (String) longitudeUnitComboBox.getSelectedItem();
		Longitude l = new Longitude(value, unit);
		position.setLongitude(l);
	}
	
	public void unassignValues() {
		position.setLongitude(null);
	}

	@Override
	public boolean checkValues() {
		String longVal = null;
		try {
			longVal = longitudeTextField.getText();
			Double.parseDouble(longVal);
		} catch (NumberFormatException e) {
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
		double value = Double.parseDouble(longitudeTextField.getText());
		String unit = (String) longitudeUnitComboBox.getSelectedItem();
		return new Longitude(value, unit);
	}

	@Override
	public void setMissingComponent(Component c) {
		Longitude longitude = (Longitude)c;
		longitudeTextField.setText(longitude.getValue() + "");
		longitudeUnitComboBox.setSelectedItem(longitude.getUnit());
	}
}