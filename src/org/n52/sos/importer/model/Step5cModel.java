package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.position.Position;

public class Step5cModel {

	private final String description = 
		"Complete missing information for the marked position.";
	
	private Position position;
	
	private List<Component> missingPositionComponents;
	
	public Step5cModel(Position position) {
		this.setPosition(position);
		setMissingPositionComponents(new ArrayList<Component>());
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public String getDescription() {
		return description;
	}

	public void setMissingPositionComponents(List<Component> missingPositionComponents) {
		this.missingPositionComponents = missingPositionComponents;
	}

	public List<Component> getMissingPositionComponents() {
		return missingPositionComponents;
	}
}
