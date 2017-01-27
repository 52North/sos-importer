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

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.model.Parseable;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * label which shows the success of parsing a marked column
 * in the table and shows all values which could not be parsed
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class ParseTestLabel extends JLabel {

    private static final String STRING = "].";
    private static final Logger logger = LoggerFactory.getLogger(ParseTestLabel.class);
    private static final long serialVersionUID = 1L;

    private final ParserThread parserThread;

    /**
     * <p>Constructor for ParseTestLabel.</p>
     *
     * @param parser a {@link org.n52.sos.importer.model.Parseable} object.
     * @param firstLineWithData a int.
     */
    public ParseTestLabel(final Parseable parser, final int firstLineWithData) {
        super();
        if (logger.isTraceEnabled()) {
            logger.trace("ParseTestLabel()[" + hashCode() + "]");
        }
        parserThread = new ParserThread(this, parser, firstLineWithData);
    }

    /**
     * <p>parseValues.</p>
     *
     * @param valuesToParse a {@link java.util.List} object.
     */
    public void parseValues(final List<String> valuesToParse) {
        if (logger.isTraceEnabled()) {
            logger.trace("[" + hashCode() + STRING +
                    "parseValues()");
        }
        setText("<html><u>" + Lang.l().waitForParseResultsLabel() +
                "</u></html>");
        parserThread.setValues(valuesToParse);
        BackNextController.getInstance().setNextButtonEnabled(false);
        // call invokeLater()
        SwingUtilities.invokeLater(parserThread);
    }

}
