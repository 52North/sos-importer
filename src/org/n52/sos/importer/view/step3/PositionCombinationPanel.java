package org.n52.sos.importer.view.step3;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Combination;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.model.position.Position;

public class PositionCombinationPanel extends CombinationPanel {

	private static final long serialVersionUID = 1L;
	
	private Position position;
	
	public PositionCombinationPanel(JPanel containerPanel) {
		super(containerPanel);
		
	}

/*
	protected void loadSelection(String s) {
		ModelStore.getInstance().remove(position);
		groupComboBox.setSelectedItem(s);	
	}


	protected String saveSelection() {
		String group = (String) groupComboBox.getSelectedItem();
		position.setGroup(group);
		TableElement selectedTableElement = TableController.getInstance().getSelectedTableElement();
		position.setTableElement(selectedTableElement);
		ModelStore.getInstance().add(position);
		return group;
	}
*/

	@Override
	public String[] getGroupItems() {
		return Settings.getInstance().getPositionGroups();
	}

	@Override
	public DefaultComboBoxModel getPatterns() {
		return EditableComboBoxItems.getInstance().getPositionPatterns();
	}

	@Override
	public Object getTestValue() {
		return "52.14° 7.52° 100m 4236";
	}

	@Override
	public Combination getCombination() {
		if (position == null) position = new Position();
		return position;
	}
}
