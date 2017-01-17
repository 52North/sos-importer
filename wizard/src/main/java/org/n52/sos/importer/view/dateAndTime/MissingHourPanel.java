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
package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.dateAndTime.Hour;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * consists of a label and a JSpinner for a single hour
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class MissingHourPanel extends MissingDateAndTimePanel {

    private static final long serialVersionUID = 1L;

    private JLabel hourLabel;

    private SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 59, 1);
    private JSpinner hourSpinner = new JSpinner(hourModel);

    /**
     * <p>Constructor for MissingHourPanel.</p>
     *
     * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
     */
    public MissingHourPanel(DateAndTime dateAndTime) {
        super(dateAndTime);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.hourLabel =  new JLabel(Lang.l().hours() + ": ");
        this.add(hourLabel);
        this.add(hourSpinner);
    }

    /** {@inheritDoc} */
    @Override
    public void assignValues() {
        dateAndTime.setHour(new Hour(hourModel.getNumber().intValue()));
    }

    /** {@inheritDoc} */
    @Override
    public void unassignValues() {
        dateAndTime.setHour(null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkValues() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Component getMissingComponent() {
        return new Hour(hourModel.getNumber().intValue());
    }

    /** {@inheritDoc} */
    @Override
    public void setMissingComponent(Component c) {
        hourModel.setValue(((Hour)c).getValue());
    }
}
