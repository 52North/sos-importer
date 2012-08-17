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
package org.n52.sos.importer.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingEPSGCodePanel;
import org.n52.sos.importer.view.position.MissingHeightPanel;
import org.n52.sos.importer.view.position.MissingLatitudePanel;
import org.n52.sos.importer.view.position.MissingLongitudePanel;

/**
 * handles operations on Position objects
 * @author Raimund
 *
 */
public class PositionController {
	
	private static final Logger logger = Logger.getLogger(PositionController.class);
	
	private Position position;
	
	private List<MissingComponentPanel> missingComponentPanels;
	
	public PositionController() {
		position = new Position();
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
	}
	
	public PositionController(Position position) {
		this.position = position;
		missingComponentPanels = new ArrayList<MissingComponentPanel>();
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public void assignPattern(String pattern, TableElement tableElement) {		
		logger.info("Assign pattern " + pattern + " to " + position + " in " + tableElement);
		
    	if (pattern.indexOf("LAT") != -1) 
    		position.setLatitude(new Latitude(tableElement, pattern));
    	if (pattern.indexOf("LON") != -1) 
    		position.setLongitude(new Longitude(tableElement, pattern));
    	if (pattern.indexOf("ALT") != -1) 
    		position.setHeight(new Height(tableElement, pattern));
    	if (pattern.indexOf("EPSG") != -1) 
    		position.setEPSGCode(new EPSGCode(tableElement, pattern));
	}
	
	public Position getNextPositionWithMissingValues() {
		List<MissingComponentPanel> missingComponentPanels;
		
		for (Position position: ModelStore.getInstance().getPositions()) {
			setPosition(position);
			missingComponentPanels = getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return position;
		}
		return null;
	}

	public List<MissingComponentPanel> getMissingComponentPanels() {		
		if (!missingComponentPanels.isEmpty()) return missingComponentPanels;
		
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
	
	public void setMissingComponents(List<Component> components) {
		for (Component c: components) {
			MissingComponentPanel mcp = c.getMissingComponentPanel(position);
			mcp.setMissingComponent(c);
			missingComponentPanels.add(mcp);
		}
	}
	
	public List<Component> getMissingComponents() {
		List<Component> components = new ArrayList<Component>();
		for (MissingComponentPanel mcp: missingComponentPanels) 
			components.add(mcp.getMissingComponent());
		return components;
	}
	
	public void assignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.assignValues();
	}
	
	public boolean checkMissingComponentValues() {
		boolean ok;
		for (MissingComponentPanel mcp: missingComponentPanels) {
			ok = mcp.checkValues();
			if (!ok) return false;
		}
		return true;
	}
	
	public void unassignMissingComponentValues() {
		for (MissingComponentPanel mcp: missingComponentPanels) 
			mcp.unassignValues();
	}
	
	public Position forThis(Cell featureOfInterestPosition) {
		Latitude latitude = position.getLatitude().forThis(featureOfInterestPosition);
		Longitude longitude = position.getLongitude().forThis(featureOfInterestPosition);
		Height height = position.getHeight().forThis(featureOfInterestPosition);
		EPSGCode epsgCode = position.getEPSGCode().forThis(featureOfInterestPosition);
		return new Position(latitude, longitude, height, epsgCode);
	}
	
	public void markComponents() {
		if (position.getLatitude() != null)
			position.getLatitude().mark();
		
		if (position.getLongitude() != null) 
			position.getLongitude().mark();
		
		if (position.getHeight() != null)
			position.getHeight().mark();
		
		if (position.getEPSGCode() != null) 
			position.getEPSGCode().mark();
	}
	
	public void mergePositions() {
		logger.info("Merge Positions");
		List<Position> positions = ModelStore.getInstance().getPositions();
		ArrayList<Position> mergedPositions = new ArrayList<Position>();
		while (!positions.isEmpty()) {
			Position p1 = positions.get(0);
			positions.remove(p1);
			// create tmp list from left over ps
			List<Position> list2 = new ArrayList<Position>(positions);
			Iterator<Position> pIter = list2.iterator();
			while (pIter.hasNext()) {
				Position p2 = pIter.next();
				if (p1.getGroup().equals(p2.getGroup())) {
					merge(p1, p2);
					positions.remove(p2);
				}
			}
			mergedPositions.add(p1);
		}
		mergedPositions.trimToSize();
		ModelStore.getInstance().setPositions(mergedPositions);
	}
	
	public void merge(Position position1, Position position2) {
		if (position1.getLatitude() == null && position2.getLatitude() != null)
			position1.setLatitude(position2.getLatitude());
		
		if (position1.getLongitude() == null && position2.getLongitude() != null) 
			position1.setLongitude(position2.getLongitude());
		
		if (position1.getHeight() == null && position2.getHeight() != null)
			position1.setHeight(position2.getHeight());
		
		if (position1.getEPSGCode() == null && position2.getEPSGCode() != null) 
			position1.setEPSGCode(position2.getEPSGCode());
	}
	
}
