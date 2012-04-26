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

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.n52.sos.importer.view.utils.Constants;

/**
 * consists of a short instruction label and the table
 * @author Raimund
 *
 */
public class Step4Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JTextArea instructionsJTA = new JTextArea();
	
	private final JPanel tablePanel = TablePanel.getInstance();
	
	public Step4Panel(String text) {
		super();
		this.setLayout(new GridLayout(2, 1));
		instructionsJTA.setText(text);
		instructionsJTA.setFont(Constants.DEFAULT_LABEL_FONT);
		instructionsJTA.setFocusable(false);
		instructionsJTA.setEditable(false);
		instructionsJTA.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		instructionsJTA.setLineWrap(true);
		this.add(instructionsJTA);
		this.add(tablePanel);
	}
}
