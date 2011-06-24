package org.n52.sos.importer.model.resources;

import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;

public class FeatureOfInterest extends Resource {
	
	private Position position;
	
	public void assign(MeasuredValue measuredValue) {
		measuredValue.setFeatureOfInterest(this);
	}
	
	public boolean isAssigned(MeasuredValue measuredValue) {
		return measuredValue.getFeatureOfInterest() != null;
	}
	
	public String toString() {
		return "Feature Of Interest";
	}

	@Override
	public void unassignFromMeasuredValues() {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getFeatureOfInterest() == this)
				mv.setFeatureOfInterest(null);
		}		
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
	
	@Override
	public String print(Cell measuredValuePosition) {
		StringBuilder sb = new StringBuilder();
		sb.append(super.print(measuredValuePosition));
		PositionController pc = new PositionController(position);
		sb.append(pc.getParsed(new Cell(0,0)));
		//TODO
		return sb.toString();
	}
}
