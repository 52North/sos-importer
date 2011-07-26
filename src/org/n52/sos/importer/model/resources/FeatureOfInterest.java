package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;

public class FeatureOfInterest extends Resource {
	
	/** single position or position column/row */
	private Position position;
	
	private HashMap<String,Position> positions = new HashMap<String, Position>();
	
	@Override
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setFeatureOfInterest(this);
	}
	
	@Override
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getFeatureOfInterest() != null;
	}
	
	@Override
	public boolean isAssignedTo(MeasuredValue measuredValue) {
		return measuredValue.getFeatureOfInterest() == this;
	}

	@Override
	public void unassign(MeasuredValue mv) {
		mv.setFeatureOfInterest(null);		
	}
	
	public FeatureOfInterest forThis(Cell measuredValuePosition) {
		FeatureOfInterest foi = new FeatureOfInterest();
		//case: this is not a feature of interest row or column
		if (getTableElement() == null) {
			foi.setName(getName());
			foi.setURI(getURI());		
			foi.setPosition(position);
		} else {
			String name = getTableElement().getValueFor(measuredValuePosition);
			foi.setName(name);
			
			//case: this feature of interest row or column is
			//associated with a position row or column in the table
			if (position != null) {
				PositionController pc = new PositionController(position);
				Cell c = getTableElement().getCellFor(measuredValuePosition);
				Position p = pc.forThis(c); 
				foi.setPosition(p);
			//case: this feature of interest row or column is not
			//associated with a position row or column in the table
			} else {
				Position p = positions.get(name);
				foi.setPosition(p);
			}
		}
		
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

	@Override
	public List<Resource> getList() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.addAll(ModelStore.getInstance().getFeatureOfInterests());
		return resources;
	}

	@Override
	public Resource getNextResourceType() {
		return new ObservedProperty();
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
	
	public void setPositionFor(String featureOfInterestName, Position position) {
		positions.put(featureOfInterestName, position);
	}	
	
	public Position getPositionFor(String featureOfInterestName) {
		return positions.get(featureOfInterestName);
	}
	
	@Override
	public String toString() {
		return "Feature Of Interest" + super.toString();
	}
}
