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
package org.n52.sos.importer.view.step3;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.controller.PositionController;
import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.tooltips.ToolTips;

/**
 * selection panel in step 3 for position combinations
 * @author Raimund
 *
 */
public class PositionCombinationPanel extends CombinationPanel {

	private static final long serialVersionUID = 1L;
	
	private Position position;
	
	public PositionCombinationPanel(JPanel containerPanel) {
		super(containerPanel);
	}

	@Override
	public String[] getGroupItems() {
		return ComboBoxItems.getInstance().getPositionGroups();
	}

	@Override
	public DefaultComboBoxModel getPatterns() {
		return EditableComboBoxItems.getInstance().getPositionPatterns();
	}

	@Override
	public Object getTestValue() {
		Latitude latitude = new Latitude(52.4, "°");
		Longitude longitude = new Longitude(7.52, "°");
		Height height = new Height(126.2, "m");
		EPSGCode epsgCode = new EPSGCode(4236);
		return new Position(latitude, longitude, height, epsgCode);
	}

	@Override
	public Combination getCombination() {
		if (position == null) position = new Position();
		return position;
	}
	
	@Override
	public String getPatternToolTip() {
		return ToolTips.get("PositionPatterns");
	}
	
	@Override
	public String getGroupToolTip() {
		return ToolTips.get("PositionGroups");
	}
	
	@Override
	public void assign(TableElement tableElement) {
    	String[] part = getSelection().split("SEP");
		String pattern = part[0];
		String group = part[1];
	
		Position position = new Position();
		position.setGroup(group);
		PositionController pc = new PositionController(position);
		pc.assignPattern(pattern, tableElement);			
		ModelStore.getInstance().add(position);
	}

	@Override
	public void unassign(TableElement tableElement) {
		Position positionToRemove = null;
		for (Position p: ModelStore.getInstance().getPositions()) {
			Latitude lat = p.getLatitude();
			Longitude lon = p.getLongitude();
			Height alt = p.getHeight();
			EPSGCode epsg = p.getEPSGCode();
			
			if ((lat != null && tableElement.equals(lat.getTableElement())) ||
				(lon != null && tableElement.equals(lon.getTableElement())) ||
				(alt != null && tableElement.equals(alt.getTableElement())) ||
				(epsg != null && tableElement.equals(epsg.getTableElement()))) {
				positionToRemove = p;
				break;
			}
		}
		
		ModelStore.getInstance().remove(positionToRemove);
	}
}
