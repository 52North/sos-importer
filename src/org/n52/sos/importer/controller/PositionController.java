package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	public String getParsed(Cell measuredValuePosition) {
		String lat = position.getLatitude().getParsedValue(measuredValuePosition);
		String lon = position.getLongitude().getParsedValue(measuredValuePosition);
		String height = position.getHeight().getParsedValue(measuredValuePosition);
		int epsgCode = position.getEPSGCode().getParsedValue(measuredValuePosition);
		return "Position[lat=" + lat + ",lon=" + lon + ",height=" + height + ",epsgCode=" + epsgCode + "]";
	}
	
}
