package org.n52.sos.importer.view.step3;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Combination;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.TableElement;

public class PositionCombinationPanel extends CombinationPanel {

	private static final long serialVersionUID = 1L;
	
	private Position position;
	
	public PositionCombinationPanel(JPanel containerPanel) {
		super(containerPanel);
		
	}

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
	
	@Override
	public void assign(TableElement tableElement) {
    	String[] part = getSelection().split("SEP");
		String pattern = part[0];
		String group = part[1];
		
		PositionController pc = new PositionController();
		pc.assignPattern(pattern, tableElement);			
		Position position = pc.getPosition();
		position.setGroup(group);
		ModelStore.getInstance().add(position);
	}

	@Override
	public void unassign(TableElement tableElement) {
		Position positionToRemove = null;
		for (Position p: ModelStore.getInstance().getPositions()) {
			Latitude lat = p.getLatitude();
			Longitude lon = p.getLongitude();
			Height alt = p.getHeight();
			EPSGCode epsg = p.getEPSGCode();
			
			if ((lat != null && tableElement.equals(lat.getTableElement())) ||
				(lon != null && tableElement.equals(lon.getTableElement())) ||
				(alt != null && tableElement.equals(alt.getTableElement())) ||
				(epsg != null && tableElement.equals(epsg.getTableElement()))) {
				positionToRemove = p;
				break;
			}
		}
		
		ModelStore.getInstance().remove(positionToRemove);
	}
}
