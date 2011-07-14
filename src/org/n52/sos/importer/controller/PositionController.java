package org.n52.sos.importer.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.position.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;
import org.n52.sos.importer.view.position.MissingHeightPanel;
import org.n52.sos.importer.view.position.MissingLatitudePanel;
import org.n52.sos.importer.view.position.MissingLongitudePanel;

public class PositionController {
	
	private Position position;
	
	private List<MissingComponentPanel> missingComponentPanels;
	
	public PositionController() {
		position = new Position();
	}
	
	public PositionController(Position position) {
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Position getNextPositionWithMissingValues() {
		List<MissingComponentPanel> missingComponentPanels;
		
		for (Position position: ModelStore.getInstance().getPositions()) {
			missingComponentPanels = getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return position;
		}
		return null;
	}

	public List<MissingComponentPanel> getMissingComponentPanels() {		
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
		
		if (position.getLatitude() == null)
			missingComponentPanels.add(new MissingLatitudePanel(position));
		
		if (position.getLongitude() == null) 
			missingComponentPanels.add(new MissingLongitudePanel(position));
		
		if (position.getHeight() == null)
			missingComponentPanels.add(new MissingHeightPanel(position));
		
		if (position.getEPSGCode() == null) 
			missingComponentPanels.add(new MissingEPSGCodePanel(position));
		
		return missingComponentPanels;
	}	
	
	public void assignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.assignValues();
	}
	
	public Position forThis(Cell featureOfInterestPosition) {
		Position p = new Position();
		double lat = position.getLatitude().getParsedValue(featureOfInterestPosition);
		String latUnit = position.getLatitude().getParsedUnit();
		double lon = position.getLongitude().getParsedValue(featureOfInterestPosition);
		String lonUnit = position.getLongitude().getParsedUnit();
		double height = position.getHeight().getParsedValue(featureOfInterestPosition);
		String heightUnit = position.getHeight().getParsedUnit();
		int epsgCode = position.getEPSGCode().getParsedValue(featureOfInterestPosition);
		
		p.setLatitude(new Latitude(lat, latUnit));
		p.setLongitude(new Longitude(lon, lonUnit));
		p.setHeight(new Height(height, heightUnit));
		p.setEPSGCode(new EPSGCode(epsgCode));
		return p;
	}
	
	
	public void mark(Color color) {
		if (position.getLatitude() != null)
			position.getLatitude().mark(color);
		
		if (position.getLongitude() == null) 
			position.getLongitude().mark(color);
		
		if (position.getHeight() == null)
			position.getHeight().mark(color);
		
		if (position.getEPSGCode() == null) 
			position.getEPSGCode().mark(color);
	}
	
	public void mergePositions() {
		List<Position> positions = ModelStore.getInstance().getPositions();
		List<Position> mergedPositions = new ArrayList<Position>();
		for (int i = 0; i < positions.size(); i++) {
			Position p1 = positions.get(i);
			positions.remove(p1);
			for (int j = 0; j < positions.size(); j++) {
				Position p2 = positions.get(j);
				if (p1.getGroup().equals(p2.getGroup())) {
					merge(p1, p2);
					positions.remove(p2);
				}
			}
			mergedPositions.add(p1);
		}
		ModelStore.getInstance().setPositions(mergedPositions);
	}
	
	public void merge(Position position1, Position position2) {
		if (position1.getLatitude() == null)
			position1.setLatitude(position2.getLatitude());
		
		if (position1.getLongitude() == null) 
			position1.setLongitude(position2.getLongitude());
		
		if (position1.getHeight() == null)
			position1.setHeight(position2.getHeight());
		
		if (position1.getEPSGCode() == null) 
			position1.setEPSGCode(position2.getEPSGCode());
	}
	
}
