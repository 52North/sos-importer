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
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;

/**
 * description label at the top of the main frame
 * @author Raimund
 *
 */
public class DescriptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static DescriptionPanel instance = null;
	
	private final JLabel descriptionLabel = new JLabel();

	private DescriptionPanel() {
		super();
		setLayout(new FlowLayout(FlowLayout.CENTER));
		descriptionLabel.setFont(Constants.DEFAULT_STEP_TITLE_FONT);
		add(descriptionLabel);
		if (Constants.GUI_DEBUG) {
			setBorder(Constants.DEBUG_BORDER);
		}
	}

	public static DescriptionPanel getInstance() {
		if (instance == null)
			instance = new DescriptionPanel();
		return instance;
	}	
	
	public void setText(String stepDescription) {
		descriptionLabel.setText(stepDescription);
	}
}
