package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Position;

/**
 * consists of a text field for the latitude and a combobox for the units
 * @author Raimund
 *
 */
public class MissingLatitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel latitudeLabel = new JLabel("   Latitude / Northing: ");
	private final JTextField latitudeTextField = new JTextField(8);
	private final JLabel latitudeUnitLabel = new JLabel("   Unit: ");
	private final JComboBox latitudeUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getLatLonUnits());
	
	public MissingLatitudePanel(Position position) {
		super();
		this.position = position;
		latitudeTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(latitudeLabel);
		this.add(latitudeTextField);
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
		try {
			Double.parseDouble(latitudeTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    "The latitude/northing can only be a decimal number so far.",
				    "Warning",
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
