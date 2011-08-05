package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;

/**
 * consists of a text field for the longitude and a combobox for the units
 * @author Raimund
 *
 */
public class MissingLongitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel longitudeLabel = new JLabel("   Longitude / Easting: ");
	private final JTextField longitudeTextField = new JTextField(8);
	private final JLabel longitudeUnitLabel = new JLabel("   Unit: ");
	private final JComboBox longitudeUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getLatLonUnits());
	
	public MissingLongitudePanel(Position position) {
		super();
		this.position = position;
		longitudeTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(longitudeLabel);
		this.add(longitudeTextField);
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
		try {
			Double.parseDouble(longitudeTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    "The longitude/easting can only be a decimal number so far.",
				    "Warning",
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