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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;


/**
 * consists of an instruction panel, the table and
 * a container panel for all missing components
 * (used for steps 5a, 5c, 6a, 6b)
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class Step5Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for Step5Panel.</p>
	 *
	 * @param description a {@link java.lang.String} object.
	 * @param missingComponentPanels a {@link java.util.List} object.
	 */
	public Step5Panel(String description, List<MissingComponentPanel> missingComponentPanels) {
		super();

		JLabel descriptionLabel = new JLabel(description);
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);

		TablePanel tablePanel = TablePanel.getInstance();

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		for (MissingComponentPanel mcp: missingComponentPanels) {
			containerPanel.add(mcp);
		}

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{795, 0};
		gridBagLayout.rowHeights = new int[]{25, 138, 1, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		GridBagConstraints gbc_descriptionPanel = new GridBagConstraints();
		gbc_descriptionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_descriptionPanel.fill = GridBagConstraints.BOTH;
		gbc_descriptionPanel.gridx = 0;
		gbc_descriptionPanel.gridy = 0;
		add(descriptionPanel, gbc_descriptionPanel);

		GridBagConstraints gbc_tablePanel = new GridBagConstraints();
		gbc_tablePanel.gridx = 0;
		gbc_tablePanel.gridy = 1;
		gbc_tablePanel.fill = GridBagConstraints.BOTH;
		add(tablePanel, gbc_tablePanel);


		GridBagConstraints gbc_containerPanel = new GridBagConstraints();
		gbc_containerPanel.gridx = 0;
		gbc_containerPanel.gridy = 2;
		gbc_containerPanel.fill = GridBagConstraints.BOTH;
		add(containerPanel, gbc_containerPanel);

		if (Constants.GUI_DEBUG) {
			descriptionPanel.setBorder(Constants.DEBUG_BORDER);
			containerPanel.setBorder(Constants.DEBUG_BORDER);
		}
	}
}
