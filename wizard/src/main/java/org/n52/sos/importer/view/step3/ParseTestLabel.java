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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final long serialVersionUID = 1L;

    private final Parseable parser;

    private int firstLineWithData = 0;

    private static final Logger logger = LoggerFactory.getLogger(ParseTestLabel.class);

    private final ParseTestLabel _this;

    private List<String> values;

    private final Runnable parserThread;

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
        this.parser = parser;
        this.firstLineWithData = firstLineWithData;
        _this = this;
        parserThread = new ParserThread();
    }

    /**
     * <p>parseValues.</p>
     *
     * @param values a {@link java.util.List} object.
     */
    public void parseValues(final List<String> values) {
        if (logger.isTraceEnabled()) {
            logger.trace("[" + hashCode() + "]." +
                    "parseValues()");
        }
        setText("<html><u>" + Lang.l().waitForParseResultsLabel() +
                "</u></html>");
        this.values = values;
        BackNextController.getInstance().setNextButtonEnabled(false);
        // call invokeLater()
        SwingUtilities.invokeLater(parserThread);
    }

    private class ParserThread implements Runnable{
        @Override
        public void run() {
            if (logger.isTraceEnabled()) {
                logger.trace("[" + hashCode() + "]." +
                        "run() <- parsing values ###########################################################");
            }
            int notParseableValues = 0;
            int currentLine = 0;
            final StringBuilder notParseable = new StringBuilder();
            String text = "";
            final Set<String> notParseableStrings = new HashSet<String>();
            //
            notParseable.append("<html>");
            // do the test parsing
            for (final String value: values) {
                if(currentLine >= firstLineWithData) {
                    try {
                        parser.parse(value);
                    } catch (final Exception e) { // $codepro.audit.disable
                        if (notParseableStrings.add(value)) {
                            notParseable.append(value + "<br>");
                        }
                        notParseableValues++;
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("skipping line to parse #" + currentLine);
                    }
                }
                currentLine++;
            }
            // handle the results of the test parsing
            if (notParseableValues == 0) {
                text = Lang.l().step3aParseTestAllOk();
                _this.setForeground(Color.blue);
            } else if (notParseableValues == 1) {
                text = Lang.l().step3aParseTest1Failed();
                _this.setForeground(Color.red);
            } else {
                text = Lang.l().step3aParseTestNFailed(notParseableValues);
                _this.setForeground(Color.red);
            }
            _this.setText("<html>" + text+ "</html>");
            notParseable.append("</html>");
            _this.setToolTipText(notParseable.toString());
            // enabled next button after parsing
            // TODO maybe add check if no value could be parsed -> dialog and not enabling next
            BackNextController.getInstance().setNextButtonEnabled(true);
        }
    }

}
