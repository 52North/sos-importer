package org.n52.sos.importer.model.resources;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;

public class FeatureOfInterest extends Resource {
	
	private Position position;
	
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setFeatureOfInterest(this);
		ModelStore.getInstance().addFeatureOfInterest(this);
	}
	
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getFeatureOfInterest() != null;
	}
	
	public String toString() {
		return "Feature Of Interest";
	}

	@Override
	public void unassign(MeasuredValue mv) {
		mv.setFeatureOfInterest(null);		
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
	
	public FeatureOfInterest forThis(Cell measuredValuePosition) {
		FeatureOfInterest foi = new FeatureOfInterest();
		if (getTableElement() == null) {
			foi.setName(getName());
			foi.setURI(getURI());
		} else {
			String name = getTableElement().getValueFor(measuredValuePosition);
			foi.setName(name);
		}
		
		PositionController pc = new PositionController(position);
		Position p = pc.forThis(new Cell(0,0)); //TODO
		foi.setPosition(p);
		return foi;
	}

	@Override
	public DefaultComboBoxModel getNames() {
		return EditableComboBoxItems.getInstance().getFeatureOfInterestNames();
	}

	@Override
	public DefaultComboBoxModel getURIs() {
		return EditableComboBoxItems.getInstance().getFeatureOfInterestURIs();
	}
}
