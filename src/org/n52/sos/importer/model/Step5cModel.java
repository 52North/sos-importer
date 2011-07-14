package org.n52.sos.importer.model;

import org.n52.sos.importer.model.position.Position;

public class Step5cModel {

	private final String description = 
		"Complete missing information for the marked position.";
	
	private Position position;
	
	public Step5cModel(Position position) {
		this.setPosition(position);
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
}
