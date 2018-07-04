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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.view.MissingComponentPanel;

/**
 * <p>MissingPositionPanel class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class MissingPositionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Step6cModel s6cM;

    // GUI stuff
    private JPanel containerPanel;
    private JPanel manualInputPanel;

    public MissingPositionPanel(Step6cModel s6cM) {
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        MissingEPSGCodePanel comp = new MissingEPSGCodePanel(s6cM.getPosition());
        panel.add(comp);
        List<MissingComponentPanel> panels = new ArrayList<>(Id.values().length);
        for (Id id : Id.values()) {
            MissingPositionComponentPanel mpcp = new MissingPositionComponentPanel(id, s6cM.getPosition());
            mpcp.setVisible(false);
            panel.add(mpcp);
            panels.add(mpcp);
        }
        if (!panels.isEmpty()) {
            comp.addCoordinatePanels(panels);
        }
        return panel;
    }

    public boolean isFinished() {
        java.awt.Component[] subPanels = manualInputPanel.getComponents();
        for (java.awt.Component component : subPanels) {
            if (component instanceof MissingComponentPanel) {
                MissingComponentPanel mcp = (MissingComponentPanel) component;
                if (!mcp.checkValues()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void saveSettings() {
        java.awt.Component[] subPanels = manualInputPanel.getComponents();
        for (java.awt.Component component : subPanels) {
            if (component instanceof MissingComponentPanel) {
                MissingComponentPanel mcp = (MissingComponentPanel) component;
                mcp.assignValues();
            }
        }
    }

    public void loadSettings() {
        // load settings from model and set map and manual interface to model position
        Position p = s6cM.getPosition();
        if (p.getEPSGCode() == null &&
                p.getCoordinate(Id.COORD_0) == null &&
                p.getCoordinate(Id.COORD_1) == null &&
                p.getCoordinate(Id.COORD_2) == null) {
            // on init -> set to default
            return;
        }
        if (p.getEPSGCode() != null && p.getEPSGCode().getValue() != Constants.DEFAULT_EPSG_CODE) {
            java.awt.Component[] subPanels = manualInputPanel.getComponents();
            for (java.awt.Component component : subPanels) {
                if (component instanceof MissingPositionComponentPanel) {
                    MissingPositionComponentPanel mcp = (MissingPositionComponentPanel) component;
                    switch (mcp.getId()) {
                        case COORD_1:
                            mcp.setMissingComponent(p.getCoordinate(Id.COORD_1));
                            break;
                        case COORD_2:
                            mcp.setMissingComponent(p.getCoordinate(Id.COORD_2));
                            break;
                        case COORD_0:
                        default:
                            mcp.setMissingComponent(p.getCoordinate(Id.COORD_0));
                            break;
                    }
                } else if (component instanceof MissingEPSGCodePanel) {
                    MissingEPSGCodePanel mcp = (MissingEPSGCodePanel) component;
                    mcp.setMissingComponent(p.getEPSGCode());
                }
            }
        }
    }

}
