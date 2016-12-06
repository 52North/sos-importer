/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
