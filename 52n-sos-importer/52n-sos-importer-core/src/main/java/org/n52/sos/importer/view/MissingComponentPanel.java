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
package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.model.Component;

/**
 * represents the view of a component (e.g. a day) which
 * is not available in the CSV file; therefore, it has to
 * be chosen manually
 * @author Raimund
 *
 */
public abstract class MissingComponentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * allocate values of the missing component
	 */
	public abstract void assignValues();
	
	/**
	 * release values of the missing component
	 */
	public abstract void unassignValues();
	
	/**
	 * checks if all values are in the defined range 
	 * of this component; returns false, if not
	 */
	public abstract boolean checkValues();
	
	/**
	 * returns the missing component
	 */
	public abstract Component getMissingComponent();
	
	/**
	 * initializes the missing component
	 */
	public abstract void setMissingComponent(Component c);
}
