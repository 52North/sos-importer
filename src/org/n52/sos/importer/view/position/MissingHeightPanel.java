package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Position;

public class MissingHeightPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel heightLabel = new JLabel("   Altitude / Height: ");
	private final JTextField heightTextField = new JTextField(8);
	private final JLabel heightUnitLabel = new JLabel("   Unit: ");
	private final JComboBox heightUnitComboBox = new JComboBox(ComboBoxItems.getInstance().getHeightUnits());
	
	public MissingHeightPanel(Position position) {
		super();
		this.position = position;	
		heightTextField.setText("0");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
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
				    "The height has to be a decimal number.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
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