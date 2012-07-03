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

import java.awt.Color;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Formatable;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * formats an exemplary String along the selected pattern
 * @author Raimund
 *
 */
public class ExampleFormatLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ExampleFormatLabel.class);
	
	private Formatable formatter;
	
	public ExampleFormatLabel(Formatable formatter) {
		super();
		if (logger.isTraceEnabled()) {
			logger.trace("ExampleFormatLabel(formatter: " + 
					(formatter!=null?
							formatter.getClass().getSimpleName():
								formatter)
					+ ")");
		}
		this.formatter = formatter;
	}
	
	/**
	 * This method formats the given object and sets the result as text for the
	 * example label.
	 * @param o
	 */
	public void reformat(Object o) {
		try {
			String formattedValue = formatter.format(o);
	        this.setForeground(Color.black);
	        this.setText(formattedValue);
		} catch (Exception e) {
	    	this.setForeground(Color.red);
	    	this.setText(Lang.l().error() + ": " + e.getLocalizedMessage());
		}
	}

	/**
	 * @return the formatter
	 */
	public Formatable getFormatter() {
		return formatter;
	}

	/**
	 * @param formatter the formatter to set
	 */
	public void setFormatter(Formatable formatter) {
		this.formatter = formatter;
	}
		
}