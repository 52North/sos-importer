package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.n52.sos.importer.config.NonEditableComboBoxItems;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Position;

public class MissingLatitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel latitudeLabel = new JLabel("   Latitude / Northing: ");
	private final JTextField latitudeTextField = new JTextField(8);
	private final JLabel latitudeUnitLabel = new JLabel("   Unit: ");
	private final JComboBox latitudeUnitComboBox = new JComboBox(NonEditableComboBoxItems.getInstance().getLatLonUnits());
	
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
}
