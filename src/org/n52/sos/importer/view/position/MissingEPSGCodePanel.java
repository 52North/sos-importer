package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.EditableJComboBoxPanel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;

public class MissingEPSGCodePanel extends MissingComponentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final Position position;
	
	private final EditableJComboBoxPanel EPSGCodeComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getEPSGCodes(), "EPSG-Code");;
	private final EditableJComboBoxPanel referenceSystemNameComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getReferenceSystemNames(), "Reference System");

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
	
}
