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
package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;

/**
 * aggregates year, month and day
 * @author Raimund
 */
public class Date extends Component {

	private Year year;
	
	private Month month;
	
	private Day day;

	public void setYear(Year year) {
		this.year = year;
	}

	public Year getYear() {		
		return year;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Month getMonth() {
		return month;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public Day getDay() {
		return day;
	}

	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingDatePanel((DateAndTime)c);
	}
}
