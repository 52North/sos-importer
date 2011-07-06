package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.n52.sos.importer.config.NonEditableComboBoxItems;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Position;

public class MissingHeightPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private final Position position;
	
	private final JLabel heightLabel = new JLabel("   Height: ");
	private final JTextField heightTextField = new JTextField(8);
	private final JLabel heightUnitLabel = new JLabel("   Unit: ");
	private final JComboBox heightUnitComboBox = new JComboBox(NonEditableComboBoxItems.getInstance().getHeightUnits());
	
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
}