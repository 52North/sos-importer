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

import java.awt.Color;

import javax.swing.JLabel;

import org.n52.sos.importer.model.Formatable;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * formats an exemplary String along the selected pattern
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class ExampleFormatLabel extends JLabel {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ExampleFormatLabel.class);

    private Formatable formatter;

    /**
     * <p>Constructor for ExampleFormatLabel.</p>
     *
     * @param formatter a {@link org.n52.sos.importer.model.Formatable} object.
     */
    public ExampleFormatLabel(final Formatable formatter) {
        super();
        if (logger.isTraceEnabled()) {
            logger.trace("ExampleFormatLabel(formatter: " +
                    (formatter != null
                    ? formatter.getClass().getSimpleName()
                            : "null")
                    + ")");
        }
        this.formatter = formatter;
    }

    /**
     * This method formats the given object and sets the result as text for the
     * example label.
     *
     * @param o a {@link java.lang.Object} object.
     */
    public void reformat(final Object o) {
        try {
            final String formattedValue = formatter.format(o);
            setForeground(Color.black);
            setText(formattedValue);
        } catch (final Exception e) {
            setForeground(Color.red);
            setText(Lang.l().error() + ": " + e.getLocalizedMessage());
        }
    }

    /**
     * <p>Getter for the field <code>formatter</code>.</p>
     *
     * @return the formatter
     */
    public Formatable getFormatter() {
        return formatter;
    }

    /**
     * <p>Setter for the field <code>formatter</code>.</p>
     *
     * @param formatter the formatter to set
     */
    public void setFormatter(final Formatable formatter) {
        this.formatter = formatter;
    }

}
