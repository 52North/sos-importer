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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.i18n.Lang;

public class OmParameterSelectionPanel extends SelectionPanel {

    private static final long serialVersionUID = 1L;

    private JTextField parameterNameTextField;
    private OmParameter omParameter;

    public OmParameterSelectionPanel(JPanel containerPanel, OmParameter omParameter) {
        super(containerPanel);
        this.omParameter = omParameter;
        JLabel parameterNameLabel = new JLabel(Lang.l().step3OmParameterNameLabel() + ":");
        parameterNameTextField = new JTextField(20);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(parameterNameLabel);
        add(parameterNameTextField);
    }

    @Override
    protected void setSelection(String parameterName) {
        parameterNameTextField.setText(parameterName);
    }

    @Override
    protected String getSelection() {
        return parameterNameTextField.getText().trim();
    }

    @Override
    public void setDefaultSelection() {
        parameterNameTextField.setText("");
    }

    @Override
    public void assign(TableElement tableElement) {
        omParameter.setTableElement(tableElement);
        ModelStore.getInstance().add(omParameter);
    }

    @Override
    public void unAssign(TableElement tableElement) {
        OmParameter parameterToRemove = null;
        for (Resource op : omParameter.getList()) {
            if (op instanceof OmParameter && op.getTableElement().equals(tableElement)) {
                parameterToRemove = (OmParameter) op;
                break;
            }
        }
        if (parameterToRemove != null) {
            ModelStore.getInstance().remove(parameterToRemove);
        }
    }

}
