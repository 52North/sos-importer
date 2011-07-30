package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;

public class Step6cModel {
	
	private final String description = "What is the position of";
	
	private FeatureOfInterest featureOfInterest;

	/** a feature of interest name in the feature of interest column/row */
	private String featureOfInterestName;
	
	private Position position;
	
	private List<Component> missingPositionComponents;
	
	public Step6cModel(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
		position = new Position();
		missingPositionComponents = new ArrayList<Component>();
	}
	
	public Step6cModel(FeatureOfInterest featureOfInterest, String featureOfInterestName) {
		this.featureOfInterest = featureOfInterest;
		this.featureOfInterestName = featureOfInterestName;
		position = new Position();
		missingPositionComponents = new ArrayList<Component>();
	}
	
	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setFeatureOfInterestName(String featureOfInterestName) {
		this.featureOfInterestName = featureOfInterestName;
	}

	public String getFeatureOfInterestName() {
		return featureOfInterestName;
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
