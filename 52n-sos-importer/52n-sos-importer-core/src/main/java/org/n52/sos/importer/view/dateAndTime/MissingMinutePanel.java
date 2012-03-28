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
import org.n52.sos.importer.model.dateAndTime.Minute;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for a single minute
 * @author Raimund
 */
public class MissingMinutePanel extends MissingDateAndTimePanel {
	
	private static final long serialVersionUID = 1L;

	private JLabel minuteLabel;
	
	private SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner minuteSpinner = new JSpinner(minuteModel);
	
	public MissingMinutePanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.minuteLabel = new JLabel(Lang.l().minutes() + ": ");
		this.add(minuteLabel);
		this.add(minuteSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setMinute(new Minute(minuteModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setMinute(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Minute(minuteModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		minuteModel.setValue(((Minute)c).getValue());
	}
}
