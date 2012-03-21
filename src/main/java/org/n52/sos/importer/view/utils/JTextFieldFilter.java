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
package org.n52.sos.importer.view.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author http://www.java-tips.org/java-se-tips/javax.swing/apply-special-filter-to-a-jtextfield.html
 *
 */
public class JTextFieldFilter extends PlainDocument {

	private static final long serialVersionUID = 1L;
	public static final String LOWERCASE  =
		"abcdefghijklmnopqrstuvwxyz";
	public static final String UPPERCASE  =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA   =
		LOWERCASE + UPPERCASE;
	public static final String NUMERIC =
		"0123456789";
	public static final String FLOAT =
		NUMERIC + ".";
	public static final String ALPHA_NUMERIC =
		ALPHA + NUMERIC;

	protected String acceptedChars = null;
	protected boolean negativeAccepted = false;

	public JTextFieldFilter() {
		this(ALPHA_NUMERIC);
	}
	public JTextFieldFilter(String acceptedchars) {
		acceptedChars = acceptedchars;
	}

	public void setNegativeAccepted(boolean negativeaccepted) {
		if (acceptedChars.equals(NUMERIC) ||
				acceptedChars.equals(FLOAT) ||
				acceptedChars.equals(ALPHA_NUMERIC)){
			negativeAccepted = negativeaccepted;
			acceptedChars += "-";
		}
	}

	public void insertString
	(int offset, String  str, AttributeSet attr)
	throws BadLocationException {
		if (str == null) return;

		if (acceptedChars.equals(UPPERCASE))
			str = str.toUpperCase();
		else if (acceptedChars.equals(LOWERCASE))
			str = str.toLowerCase();

		for (int i=0; i < str.length(); i++) {
			if (acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1)
				return;
		}

		if (acceptedChars.equals(FLOAT) ||
				(acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
			if (str.indexOf(".") != -1) {
				if (getText(0, getLength()).indexOf(".") != -1) {
					return;
				}
			}
		}

		if (negativeAccepted && str.indexOf("-") != -1) {
			if (str.indexOf("-") != 0 || offset != 0 ) {
				return;
			}
		}

		super.insertString(offset, str, attr);
	}
}
