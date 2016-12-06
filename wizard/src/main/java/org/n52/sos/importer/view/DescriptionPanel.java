/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
