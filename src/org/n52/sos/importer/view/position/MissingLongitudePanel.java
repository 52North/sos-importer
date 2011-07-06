package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;

public class MissingLongitudePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel longitudeLabel = new JLabel("   Longitude / Easting: ");
	private final JTextField longitudeTextField = new JTextField(8);
	private final JLabel longitudeUnitLabel = new JLabel("   Unit: ");
	private final JComboBox longitudeUnitComboBox = new JComboBox(Settings.getInstance().getLatLonUnits());
	
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
}