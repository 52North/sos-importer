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
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a text field for the height and a combobox for the units
 * @author Raimund
 *
 */
public class MissingHeightPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private static final Logger logger = Logger.getLogger(MissingHeightPanel.class);
	
	private JLabel heightLabel;
	private final JTextField heightTextField = new JTextField(8);
	private final JLabel heightUnitLabel;
	private final JComboBox heightUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getHeightUnits());
	
	public MissingHeightPanel(Position position) {
		super();
		this.position = position;	
		heightTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.heightLabel = new JLabel("   " + Lang.l().altitude() + ": ");
		this.heightUnitLabel = new JLabel("   " + Lang.l().unit() + ": ");
		
		this.add(heightLabel);
		this.add(heightTextField);
		this.add(heightUnitLabel);
		this.add(heightUnitComboBox);
	}
	
	public void assignValues() {
		double value = Double.parseDouble(heightTextField.getText());
		String unit = (String) heightUnitComboBox.getSelectedItem();
		Height h = new Height(value, unit);
		position.setHeight(h);
	}
	
	public void unassignValues() {
		position.setHeight(null);
	}

	@Override
	public boolean checkValues() {
		try {
			Double.parseDouble(heightTextField.getText());
		} catch (NumberFormatException e) {
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
		double value = Double.parseDouble(heightTextField.getText());
		String unit = (String) heightUnitComboBox.getSelectedItem();
		return new Height(value, unit);
	}

	@Override
	public void setMissingComponent(Component c) {
		Height height = (Height)c;
		heightTextField.setText(height.getValue() + "");
		heightUnitComboBox.setSelectedItem(height.getUnit());
	}
}