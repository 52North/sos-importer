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
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a text field for the latitude and a combobox for the units
 * @author Raimund
 *
 */
public class MissingLatitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MissingLatitudePanel.class);

	private final Position position;
	
	private JLabel latitudeLabel;
	private final JTextField latitudeTextField = new JTextField(8);
	private JLabel latitudeUnitLabel;
	private final JComboBox latitudeUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getLatLonUnits());
	
	public MissingLatitudePanel(Position position) {
		super();
		this.position = position;
		latitudeTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.latitudeLabel = new JLabel("   " + Lang.l().latitudeNorthing() + ": ");
		this.add(latitudeLabel);
		this.add(latitudeTextField);
		this.latitudeUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
		this.add(latitudeUnitLabel);
		this.add(latitudeUnitComboBox);
	}
	
	public void assignValues() {
		double value = Double.parseDouble(latitudeTextField.getText());
		String unit = (String) latitudeUnitComboBox.getSelectedItem();
		Latitude l = new Latitude(value, unit);
		position.setLatitude(l);
	}
	
	public void unassignValues() {
		position.setLatitude(null);
	}

	@Override
	public boolean checkValues() {
		String latVal = null;
		try {
			latVal = latitudeTextField.getText();
			Double.parseDouble(latVal);
		} catch (NumberFormatException e) {
			logger.error("Latitude value could not be parsed: " + latVal, e);
			JOptionPane.showMessageDialog(null,
				    Lang.l().latitudeDialogDecimalValue(),
				    Lang.l().warningDialogTitle(),
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		double value = Double.parseDouble(latitudeTextField.getText());
		String unit = (String) latitudeUnitComboBox.getSelectedItem();
		return new Latitude(value, unit);
	}

	@Override
	public void setMissingComponent(Component c) {
		Latitude latitude = (Latitude)c;
		latitudeTextField.setText(latitude.getValue() + "");
		latitudeUnitComboBox.setSelectedItem(latitude.getUnit());
	}
}
