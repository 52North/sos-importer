package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;

public class MissingEPSGCodePanel extends MissingComponentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final Position position;
	
	private final JLabel EPSGCodeLabel = new JLabel("   EPSG-Code: ");
	private final EditableJComboBoxPanel EPSGCodeComboBox;
//	private final JLabel referenceSystemNameLabel = new JLabel("Reference system: ");	
//	private final JComboBox referenceSystemNameComboBox = new JComboBox(referenceSystemNames);

	public MissingEPSGCodePanel(Position position) {
		super();
		this.position = position;
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		EPSGCodeComboBox = new EditableJComboBoxPanel(items.getEPSGCodes());
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
//		referenceSystemPanel.add(referenceSystemNameLabel);
//		referenceSystemPanel.add(referenceSystemNameComboBox);
		this.add(EPSGCodeLabel);
		this.add(EPSGCodeComboBox);
		
	}
	@Override
	public void assignValues() {
		int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		position.setEPSGCode(new EPSGCode(code));
		EPSGCodeComboBox.saveNewItem();
	}
	@Override
	public void unassignValues() {
		position.setEPSGCode(null);	
	}

}
