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
package org.n52.sos.importer.model.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

public class FeatureOfInterest extends Resource implements Comparable<FeatureOfInterest>{
	
	private static final Logger logger = Logger.getLogger(FeatureOfInterest.class);
	
	/** single position or position column/row */
	private Position position;
	
	/** corresponding positions for each feature of interest in this column/row */
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
		return this.equals(measuredValue.getFeatureOfInterest());
	}

	@Override
	public void unassign(MeasuredValue mv) {
		mv.setFeatureOfInterest(null);		
	}
	
	@Override
	public FeatureOfInterest forThis(Cell measuredValuePosition) {
		/*
		 * case A: this is not a feature of interest row or column; 
		 * 			it is a global foi or a generated one
		 */
		if (getTableElement() == null || isGenerated()) {
			return this;
		} else {
			// TODO check position handling here!
			// each row/column has its own foi, so return new instances
			FeatureOfInterest foi = new FeatureOfInterest();
			String name = getTableElement().getValueFor(measuredValuePosition);
			foi.setName(name);
			// TODO check, if the next line break any logic
			foi.setTableElement(getTableElement());
			
			/* 
			 * case B: this is a feature of interest row or column
			 * 
			 * case B1: associated with a position row or column in the table
			 */
			if (position != null) {
				PositionController pc = new PositionController(position);
				Cell c = getTableElement().getCellFor(measuredValuePosition);
				Position p = pc.forThis(c); 
				foi.setPosition(p);
			/*
			 * case B2: not associated with a position row or column in the table
			 */
			} else {
				Position p = getPositionFor(name);
				foi.setPosition(p);
			}
			return foi;
		}
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
	
	public void assignPosition(Position position) {
		logger.info("Assign " + position + " to " + this);
		this.setPosition(position);
	}

	public void unassignPosition() {
		if (position != null) {
			logger.info("Unassign " + position + " from " + this);
			this.setPosition(null);
		}
	}
	
	public void setPositionFor(String featureOfInterestName, Position position) {
		logger.info("Assign " + position + " to " + featureOfInterestName);
		positions.put(featureOfInterestName, position);
	}	
	
	public void removePositionFor(String featureOfInterestName) {
		Position p = getPositionFor(featureOfInterestName);
		if (p != null)
			logger.info("Unassign " + p + " from " + featureOfInterestName);
		positions.remove(featureOfInterestName);
	}
	
	public Position getPositionFor(String featureOfInterestName) {
		return positions.get(featureOfInterestName);
	}
	
	@Override
	public String toString() {
		return "Feature Of Interest" + super.toString();
	}

	@Override
	public String getTypeName() {
		return Lang.l().featureOfInterest();
	}

	@Override
	public String XML_PREFIX() {
		return "foi";
	}

	@Override
	public int compareTo(FeatureOfInterest o) {
		// try to compare by name
		if (getName() != null && o.getName() != null) {
			return getName().compareTo(o.getName());
		}
		// compare by xmlId
		return getXMLId().compareTo(o.getXMLId());
	}

	@Override
	public String getName() {
		if (isGenerated()) {
			return getXMLId();
		} else {
			return super.getName();
		}
	}
}
