/**
 * ﻿Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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

        private static final String HTML_CLOSE = "</html>";
        private static final String HTML_OPEN = "<html>";
        
        private final Parseable parser;

        private int firstLineWithData;

        private final ParseTestLabel parseTestLabel;

        private List<String> values;
        
        private static final Logger logger = LoggerFactory.getLogger(ParserThread.class);

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