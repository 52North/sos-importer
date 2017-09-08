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
package org.n52.sos.importer.view.position;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;

/**
 * <p>MissingPositionPanel class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class MissingPositionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Step6cModel s6cM;

    // GUI stuff
    private final JPanel containerPanel;
    private final JPanel manualInputPanel;

    /**
     * <p>Constructor for MissingPositionPanel.</p>
     *
     * @param s6cM a {@link org.n52.sos.importer.model.Step6cModel} object.
     */
    public MissingPositionPanel(final Step6cModel s6cM) {
        setLayout(new BorderLayout(0, 0));

        this.s6cM = s6cM;

        manualInputPanel = initManualInputPanel();

        containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout(0, 0));
        containerPanel.add(manualInputPanel, BorderLayout.CENTER);

        add(containerPanel, BorderLayout.CENTER);

        if (Constants.isGuiDebug()) {
            manualInputPanel.setBorder(Constants.DEBUG_BORDER);
        }
        setVisible(true);
    }

    private JPanel initManualInputPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(new MissingLatitudePanel(s6cM.getPosition()));
        panel.add(new MissingLongitudePanel(s6cM.getPosition()));
        panel.add(new MissingHeightPanel(s6cM.getPosition()));
        panel.add(new MissingEPSGCodePanel(s6cM.getPosition()));
        return panel;
    }

    /**
     * <p>isFinished.</p>
     *
     * @return a boolean.
     */
    public boolean isFinished() {
        final java.awt.Component[] subPanels = manualInputPanel.getComponents();
        for (final java.awt.Component component : subPanels) {
            if (component instanceof MissingComponentPanel) {
                final MissingComponentPanel mcp = (MissingComponentPanel) component;
                if (!mcp.checkValues()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>saveSettings.</p>
     */
    public void saveSettings() {
        final java.awt.Component[] subPanels = manualInputPanel.getComponents();
        for (final java.awt.Component component : subPanels) {
            if (component instanceof MissingComponentPanel) {
                final MissingComponentPanel mcp = (MissingComponentPanel) component;
                mcp.assignValues();
            }
        }
    }

    /**
     * <p>loadSettings.</p>
     */
    public void loadSettings() {
        // load settings from model and set map and manual interface to model position
        final Position p = s6cM.getPosition();
        if (p.getEPSGCode() == null && p.getHeight() == null && p.getLatitude() == null && p.getLongitude() == null) {
            // on init -> set to default
            return;
        }
        if (p.getEPSGCode() != null && p.getEPSGCode().getValue() != Constants.DEFAULT_EPSG_CODE) {
            final java.awt.Component[] subPanels = manualInputPanel.getComponents();
            for (final java.awt.Component component : subPanels) {
                if (component instanceof MissingLatitudePanel) {
                    final MissingLatitudePanel mcp = (MissingLatitudePanel) component;
                    mcp.setMissingComponent(p.getLatitude());
                } else if (component instanceof MissingLongitudePanel) {
                    final MissingLongitudePanel mcp = (MissingLongitudePanel) component;
                    mcp.setMissingComponent(p.getLongitude());
                } else if (component instanceof MissingHeightPanel) {
                    final MissingHeightPanel mcp = (MissingHeightPanel) component;
                    mcp.setMissingComponent(p.getHeight());
                } else if (component instanceof MissingEPSGCodePanel) {
                    final MissingEPSGCodePanel mcp = (MissingEPSGCodePanel) component;
                    mcp.setMissingComponent(p.getEPSGCode());
                }
            }
        }
    }

}
