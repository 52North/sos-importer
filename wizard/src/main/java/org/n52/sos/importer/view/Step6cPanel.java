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

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.position.MissingPositionPanel;

/**
 * <p>Step6cPanel class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step6cPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private MissingPositionPanel mpp;

    /**
     * <p>Constructor for Step6cPanel.</p>
     *
     * @param description a {@link java.lang.String} object.
     * @param featureOfInterestName a {@link java.lang.String} object.
     * @param s6cM a {@link org.n52.sos.importer.model.Step6cModel} object.
     */
    public Step6cPanel(String description,
            String featureOfInterestName,
            Step6cModel s6cM) {
        String questionStatement = String.format("%s %s ", description, Lang.l().featureOfInterest());
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel questionStartLabel = new JLabel(questionStatement);
        JLabel foiName = new JLabel(featureOfInterestName);
        foiName.setFont(Constants.DEFAULT_LABEL_FONT_BOLD);

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descriptionPanel.add(questionStartLabel);
        descriptionPanel.add(foiName);
        descriptionPanel.add(new JLabel("?"));
        add(descriptionPanel, BorderLayout.NORTH);

        mpp = new MissingPositionPanel(s6cM);
        add(mpp);
        if (Constants.isGuiDebug()) {
            descriptionPanel.setBorder(Constants.DEBUG_BORDER);
            mpp.setBorder(Constants.DEBUG_BORDER);
        }
    }

    /**
     * <p>isFinished.</p>
     *
     * @return a boolean.
     */
    public boolean isFinished() {
        return mpp.isFinished();
    }

    /**
     * <p>saveSettings.</p>
     */
    public void saveSettings() {
        mpp.saveSettings();
    }

    /**
     * <p>loadSettings.</p>
     */
    public void loadSettings() {
        mpp.loadSettings();
    }

}
