package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JOptionPane;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.EditableJComboBoxPanel;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.tooltips.ToolTips;

public class MissingEPSGCodePanel extends MissingComponentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final Position position;
	
	private final EditableJComboBoxPanel EPSGCodeComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getEPSGCodes(), "EPSG-Code", ToolTips.get("EPSG"));
	private final EditableJComboBoxPanel referenceSystemNameComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getReferenceSystemNames(), "Reference System", ToolTips.get("ReferenceSystem"));

	public MissingEPSGCodePanel(Position position) {
		super();
		this.position = position;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(referenceSystemNameComboBox);
		this.add(EPSGCodeComboBox);
		EPSGCodeComboBox.setPartnerComboBox(referenceSystemNameComboBox);
		referenceSystemNameComboBox.setPartnerComboBox(EPSGCodeComboBox);
	}
	@Override
	public void assignValues() {
		int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		position.setEPSGCode(new EPSGCode(code));
	}
	
	@Override
	public void unassignValues() {
		position.setEPSGCode(null);	
	}
	
	@Override
	public boolean checkValues() {
		int code = 0;
		try {
			code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    "The EPSG-Code has to be a natural number.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		if (code < 0 || code > 32767) {
			JOptionPane.showMessageDialog(null,
				    "The EPSG-Code has to be in the range of 0 and 32767.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		return new EPSGCode(code);
	}
	
	@Override
	public void setMissingComponent(Component c) {
		EPSGCodeComboBox.setSelectedItem(String.valueOf(((EPSGCode)c).getValue()));
	}	
}
