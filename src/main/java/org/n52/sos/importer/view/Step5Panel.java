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
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.interfaces.MissingComponentPanel;

/**
 * consists of an instruction panel, the table and 
 * a container panel for all missing components
 * (used for steps 5a, 5c, 6a, 6b) 
 * @author Raimund
 *
 */
public class Step5Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public Step5Panel(String description, List<MissingComponentPanel> missingComponentPanels) {
		super();
		JLabel descriptionLabel = new JLabel(description);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);
		this.add(descriptionPanel);
		
		TablePanel tablePanel = TablePanel.getInstance();
		this.add(tablePanel);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		
		for (MissingComponentPanel mcp: missingComponentPanels)
			containerPanel.add(mcp);
		
		this.add(containerPanel);
	}
}
