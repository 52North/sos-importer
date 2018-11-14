/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.view.i18n.Lang;


/**
 * consists of a customized instruction panel and
 * a container panel for all missing components
 * (used for steps 6bspecial and 6c)
 *
 * @author Raimund
 */
public class Step6Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel descriptionLabel1 = new JLabel();
    private final JLabel descriptionLabel2 = new JLabel();
    private final JLabel questionMarkLabel = new JLabel("?");
    private final JTextField featureOfInterestTextField = new JTextField();
    private final JTextField observedPropertyTextField = new JTextField();

    private final JPanel containerPanel = new JPanel();

    /**
     * <p>Constructor for Step6Panel.</p>
     *
     * @param description a {@link java.lang.String} object.
     * @param featureOfInterestName a {@link java.lang.String} object.
     * @param observedPropertyName a {@link java.lang.String} object.
     * @param missingComponentPanels a {@link java.util.List} object.
     */
    public Step6Panel(String description, String featureOfInterestName,
            String observedPropertyName, List<MissingComponentPanel> missingComponentPanels) {
        super();
        descriptionLabel1.setText(description + " " + Lang.l().featureOfInterest() + " ");
        featureOfInterestTextField.setText(" " + featureOfInterestName + " ");
        featureOfInterestTextField.setEditable(false);

        if (observedPropertyName != null) {
            descriptionLabel2.setText(" " + Lang.l().and() + " " + Lang.l().observedProperty() + " ");
            observedPropertyTextField.setText(" " + observedPropertyName + " ");
            observedPropertyTextField.setEditable(false);
        }
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(descriptionLabel1);
        descriptionPanel.add(featureOfInterestTextField);
        if (observedPropertyName != null) {
            descriptionPanel.add(descriptionLabel2);
            descriptionPanel.add(observedPropertyTextField);
        }
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        descriptionPanel.add(questionMarkLabel);
        add(descriptionPanel);

        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
        add(containerPanel);

        for (MissingComponentPanel mcp: missingComponentPanels) {
            containerPanel.add(mcp);
        }
    }
}
