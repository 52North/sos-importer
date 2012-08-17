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

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.TableElement;

/**
 * assigns or unassigns columns to Booleans, Counts and Text
 * @author Raimund
 *
 */
public class MeasuredValueSelectionPanel extends SelectionPanel {

	private static final long serialVersionUID = 1L;
	
	private final ParseTestLabel parseTestLabel;
	
	private MeasuredValue measuredValue;
	
	public MeasuredValueSelectionPanel(JPanel containerPanel, MeasuredValue measuredValue,int firstLineWithData) {
		super(containerPanel);
		this.measuredValue = measuredValue;
		parseTestLabel = new ParseTestLabel(measuredValue,firstLineWithData);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(parseTestLabel);
	}

	@Override
	protected void setSelection(String s) {	}

	@Override
	protected String getSelection() { return "0"; }

	@Override
	public void setDefaultSelection() {	}
	
	protected void reInit() {
		//parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
	}		
	
	@Override
	public void assign(TableElement tableElement) {
		measuredValue.setTableElement(tableElement);
		ModelStore.getInstance().add(measuredValue);
	}
	
	@Override
	public void unAssign(TableElement tableElement) {
		MeasuredValue measuredValueToRemove = null;
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			if (tableElement.equals(mv.getTableElement())) {
				measuredValueToRemove = mv;
				break;
			}
				
		ModelStore.getInstance().remove(measuredValueToRemove);
	}
}
