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
package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Day;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for a single day
 * @author Raimund
 */
public class MissingDayPanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	private JLabel dayLabel;
	private DateAndTime dateAndTime;
	
	private SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
	private JSpinner daySpinner = new JSpinner(dayModel);
	
	public MissingDayPanel(DateAndTime dateAndTime) {
		super();
		this.dateAndTime = dateAndTime;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.dayLabel = new JLabel(Lang.l().day() + ": ");
		this.add(dayLabel);
		this.add(daySpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setDay(new Day(dayModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setDay(null);		
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Day(dayModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		dayModel.setValue(((Day)c).getValue());
	}
}
