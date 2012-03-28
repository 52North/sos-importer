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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Parseable;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * label which shows the success of parsing a marked column 
 * in the table and shows all values which could not be parsed
 * @author Raimund
 *
 */
public class ParseTestLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private Parseable parser;
	
	private int firstLineWithData = 0;
	
	private static final Logger logger = Logger.getLogger(ParseTestLabel.class);
	
	public ParseTestLabel(Parseable parser, int firstLineWithData) {
		super();
		this.parser = parser;
		this.firstLineWithData = firstLineWithData;
	}
	
	public void parseValues(List<String> values) {
		int notParseableValues = 0;
		int currentLine = 0;
		StringBuilder notParseable = new StringBuilder();
		Set<String> notParseableStrings = new HashSet<String>();
		notParseable.append("<html>");

		for (String value: values) {
			if(currentLine >= firstLineWithData) {
				try {
					parser.parse(value);
				} catch (Exception e) {
					if (notParseableStrings.add(value))
						notParseable.append(value + "<br>");
					notParseableValues++;
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("skipping line to parse #" + currentLine);
				}
			}
			currentLine++;
		}
		
		String text = "";
		if (notParseableValues == 0) {
			text = Lang.l().step3aParseTestAllOk();
			this.setForeground(Color.blue);
		} else if (notParseableValues == 1) {
			text = Lang.l().step3aParseTest1Failed();
			this.setForeground(Color.red);
		} else {
			text = Lang.l().step3aParseTestNFailed(notParseableValues);
			this.setForeground(Color.red);
		}
		
		this.setText("<html><u>" + text+ "</u></html>");
		
		notParseable.append("</html>");
		this.setToolTipText(notParseable.toString());
	}			
}