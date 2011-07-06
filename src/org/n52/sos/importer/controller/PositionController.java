package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<MissingComponentPanel> getMissingComponentPanels() {		
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
		
		if (position.getLongitude() == null) 
			missingComponentPanels.add(new MissingLongitudePanel(position));
		
		if (position.getLatitude() == null)
			missingComponentPanels.add(new MissingLatitudePanel(position));
		
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
	
}
