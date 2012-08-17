/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6cModel implements StepModel {
	
	private FeatureOfInterest featureOfInterest;

	/** a feature of interest name in the feature of interest column/row */
	private String featureOfInterestName;
	
	private Position position;
	
	private List<Component> missingPositionComponents;
	
	public Step6cModel(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
		position = this.featureOfInterest.getPosition()!=null?this.featureOfInterest.getPosition():new Position();
		missingPositionComponents = new ArrayList<Component>();
	}
	
	public Step6cModel(FeatureOfInterest featureOfInterest, String featureOfInterestName) {
		this(featureOfInterest);
		this.featureOfInterestName = featureOfInterestName;
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
		return Lang.l().step6cModelDescription();
	}

	/**
	 * saves the components which are missing for this step
	 * (= all position components)
	 */
	public void setMissingPositionComponents(List<Component> missingPositionComponents) {
		this.missingPositionComponents = missingPositionComponents;
	}

	/**
	 * returns the components which were missing for this step 
	 * (= all position components)
	 */
	public List<Component> getMissingPositionComponents() {
		return missingPositionComponents;
	}

}
