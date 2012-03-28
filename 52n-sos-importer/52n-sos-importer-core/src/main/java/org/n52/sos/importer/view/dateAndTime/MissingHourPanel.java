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
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for a single hour
 * @author Raimund
 */
public class MissingHourPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private JLabel hourLabel;
	
	private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner hourSpinner = new JSpinner(hourModel);
	
	public MissingHourPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.hourLabel =  new JLabel(Lang.l().hours() + ": ");
		this.add(hourLabel);
		this.add(hourSpinner);
	}	
	
	@Override
	public void assignValues() {
		dateAndTime.setHour(new Hour(hourModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setHour(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Hour(hourModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		hourModel.setValue(((Hour)c).getValue());
	}
}
