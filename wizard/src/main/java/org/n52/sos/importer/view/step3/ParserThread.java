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

import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.model.Parseable;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class ParserThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ParserThread.class);
    private static final String HTML_CLOSE = "</html>";
    private static final String HTML_OPEN = "<html>";
    private final Parseable parser;

    private final ParseTestLabel parseTestLabel;

    private int firstLineWithData;
    private List<String> values;

    public ParserThread(ParseTestLabel parseTestLabel, Parseable parser, int firstLineWithData) {
        this.parseTestLabel = parseTestLabel;
        this.parser = parser;
        this.firstLineWithData = firstLineWithData;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public void run() {
        if (logger.isTraceEnabled()) {
            logger.trace("[" + hashCode() + "]." +
                    "run() <- parsing values ###########################################################");
        }
        if (values == null) {
            logger.error("Nothing to parser, hence stopped. Values: 'null'");
            return;
        }
        int notParseableValues = 0;
        int currentLine = 0;
        final StringBuilder notParseable = new StringBuilder();
        String text = "";
        final Set<String> notParseableStrings = new HashSet<String>();
        //
        notParseable.append(HTML_OPEN);
        // do the test parsing
        for (final String value: values) {
            if (currentLine >= firstLineWithData) {
                try {
                    parser.parse(value);
                } catch (final Exception e) {
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
            parseTestLabel.setForeground(Color.blue);
        } else if (notParseableValues == 1) {
            text = Lang.l().step3aParseTest1Failed();
            parseTestLabel.setForeground(Color.red);
        } else {
            text = Lang.l().step3aParseTestNFailed(notParseableValues);
            parseTestLabel.setForeground(Color.red);
        }
        parseTestLabel.setText(HTML_OPEN + text + HTML_CLOSE);
        notParseable.append(HTML_CLOSE);
        parseTestLabel.setToolTipText(notParseable.toString());
        // enabled next button after parsing
        // TODO maybe add check if no value could be parsed -> dialog and not enabling next
        BackNextController.getInstance().setNextButtonEnabled(true);
    }
}
