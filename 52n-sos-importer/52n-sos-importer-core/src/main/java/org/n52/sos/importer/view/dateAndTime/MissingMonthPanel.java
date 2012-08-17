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
import org.n52.sos.importer.model.dateAndTime.Month;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for a single month
 * @author Raimund
 */
public class MissingMonthPanel extends MissingDateAndTimePanel {

	private static final long serialVersionUID = 1L;

	private JLabel monthLabel;
	
	private SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
	private JSpinner monthSpinner = new JSpinner(monthModel);
	
	public MissingMonthPanel(DateAndTime dateAndTime) {
		super(dateAndTime);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.monthLabel = new JLabel(Lang.l().month() + ": ");
		this.add(monthLabel);
		this.add(monthSpinner);
	}

	@Override
	public void assignValues() {
		dateAndTime.setMonth(new Month(monthModel.getNumber().intValue()));	
	}

	@Override
	public void unassignValues() {
		dateAndTime.setMonth(null);	
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	@Override
	public Component getMissingComponent() {
		return new Month(monthModel.getNumber().intValue());
	}

	@Override
	public void setMissingComponent(Component c) {
		monthModel.setValue(((Month)c).getValue());
	}
}
