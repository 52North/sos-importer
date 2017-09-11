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
package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.i18n.Lang;

public class FeatureOfInterestPanel extends ResourceSelectionPanel {

    private final JCheckBox hasParentFeatureCheckbox;
    private final JTextField parentFeatureIdentifierTextField;
    private final JLabel parentFeatureIdentifierLabel;

    public FeatureOfInterestPanel(JPanel containerPanel) {
        super(containerPanel, new FeatureOfInterest());

        final JLabel hasParentFeatureCheckboxLabel = new JLabel(Lang.l().step3HasParentFeatureCheckBox());
        hasParentFeatureCheckbox = new JCheckBox();
        hasParentFeatureCheckbox.setSelected(false);
        hasParentFeatureCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(hasParentFeatureCheckbox)) {
                    if (hasParentFeatureCheckbox.isSelected()) {
                        parentFeatureIdentifierLabel.setVisible(true);
                        parentFeatureIdentifierTextField.setVisible(true);
                    } else {
                        parentFeatureIdentifierLabel.setVisible(false);
                        parentFeatureIdentifierTextField.setVisible(false);
                    }
                }
            }
        });
        final JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        checkBoxPanel.add(hasParentFeatureCheckboxLabel);
        checkBoxPanel.add(hasParentFeatureCheckbox);

        parentFeatureIdentifierLabel = new JLabel(Lang.l().step3ParentFeatureIdentifierLabel() + ":");
        parentFeatureIdentifierLabel.setVisible(false);
        parentFeatureIdentifierTextField = new JTextField(20);
        parentFeatureIdentifierTextField.setVisible(false);
        final JPanel identifierPanel = new JPanel();
        identifierPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        identifierPanel.add(parentFeatureIdentifierLabel);
        identifierPanel.add(parentFeatureIdentifierTextField);

        setLayout(new GridLayout(2, 1));
        add(checkBoxPanel);
        add(identifierPanel);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void setSelection(String s) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getSelection() {
        // TODO Auto-generated method stub
        // consider state of checkbox
        return null;
    }

    @Override
    public void setDefaultSelection() {
        // TODO Auto-generated method stub

    }

    @Override
    public void assign(TableElement tableElement) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unAssign(TableElement tableElement) {
        // TODO Auto-generated method stub

    }

}