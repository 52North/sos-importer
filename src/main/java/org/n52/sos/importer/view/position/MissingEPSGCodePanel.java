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
package org.n52.sos.importer.view.position;

import java.awt.FlowLayout;

import javax.swing.JOptionPane;

import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.tooltips.ToolTips;

/**
 * consists of a combobox for the EPSG code and a combobox for the 
 * name of the spatial reference system; both are linked with each other
 * @author Raimund
 *
 */
public class MissingEPSGCodePanel extends MissingComponentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final Position position;
	
	private final EditableJComboBoxPanel EPSGCodeComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getEPSGCodes(), "EPSG-Code", ToolTips.get("EPSG"));
	private final EditableJComboBoxPanel referenceSystemNameComboBox
		= new EditableJComboBoxPanel(EditableComboBoxItems.getInstance().getReferenceSystemNames(), "Reference System", ToolTips.get("ReferenceSystem"));

	public MissingEPSGCodePanel(Position position) {
		super();
		this.position = position;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(referenceSystemNameComboBox);
		this.add(EPSGCodeComboBox);
		EPSGCodeComboBox.setPartnerComboBox(referenceSystemNameComboBox);
		referenceSystemNameComboBox.setPartnerComboBox(EPSGCodeComboBox);
	}
	@Override
	public void assignValues() {
		int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		position.setEPSGCode(new EPSGCode(code));
	}
	
	@Override
	public void unassignValues() {
		position.setEPSGCode(null);	
	}
	
	@Override
	public boolean checkValues() {
		int code = 0;
		try {
			code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    "The EPSG-Code has to be a natural number.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		if (code < 0 || code > 32767) {
			JOptionPane.showMessageDialog(null,
				    "The EPSG-Code has to be in the range of 0 and 32767.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		int code = Integer.valueOf((String) EPSGCodeComboBox.getSelectedItem());
		return new EPSGCode(code);
	}
	
	@Override
	public void setMissingComponent(Component c) {
		EPSGCodeComboBox.setSelectedItem(String.valueOf(((EPSGCode)c).getValue()));
	}	
}
