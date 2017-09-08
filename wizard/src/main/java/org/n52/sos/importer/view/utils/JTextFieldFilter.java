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
package org.n52.sos.importer.view.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>JTextFieldFilter class.</p>
 *
 * @author http://www.java-tips.org/java-se-tips/javax.swing/apply-special-filter-to-a-jtextfield.html
 */
public class JTextFieldFilter extends PlainDocument {

    /** Constant <code>LOWERCASE="abcdefghijklmnopqrstuvwxyz"</code> */
    public static final String LOWERCASE  =
            "abcdefghijklmnopqrstuvwxyz";
    /** Constant <code>UPPERCASE="ABCDEFGHIJKLMNOPQRSTUVWXYZ"</code> */
    public static final String UPPERCASE  =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** Constant <code>ALPHA="LOWERCASE + UPPERCASE"</code> */
    public static final String ALPHA   =
            LOWERCASE + UPPERCASE;
    /** Constant <code>NUMERIC="0123456789"</code> */
    public static final String NUMERIC =
            "0123456789";
    /** Constant <code>FLOAT="NUMERIC + ."</code> */
    public static final String FLOAT =
            NUMERIC + ".";
    /** Constant <code>ALPHA_NUMERIC="ALPHA + NUMERIC"</code> */
    public static final String ALPHA_NUMERIC =
            ALPHA + NUMERIC;

    private static final long serialVersionUID = 1L;
    protected String acceptedChars;
    protected boolean negativeAccepted;

    /**
     * <p>Constructor for JTextFieldFilter.</p>
     */
    public JTextFieldFilter() {
        this(ALPHA_NUMERIC);
    }

    /**
     * <p>Constructor for JTextFieldFilter.</p>
     *
     * @param acceptedchars a {@link java.lang.String} object.
     */
    public JTextFieldFilter(String acceptedchars) {
        acceptedChars = acceptedchars;
    }

    /**
     * <p>Setter for the field <code>negativeAccepted</code>.</p>
     *
     * @param negativeaccepted a boolean.
     */
    public void setNegativeAccepted(boolean negativeaccepted) {
        if (acceptedChars.equals(NUMERIC) ||
                acceptedChars.equals(FLOAT) ||
                acceptedChars.equals(ALPHA_NUMERIC)) {
            negativeAccepted = negativeaccepted;
            acceptedChars += "-";
        }
    }

    @Override
    public void insertString(int offset, final String  string, final AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }

        String str = string;
        if (acceptedChars.equals(UPPERCASE)) {
            str = str.toUpperCase();
        } else if (acceptedChars.equals(LOWERCASE)) {
            str = str.toLowerCase();
        }

        for (int i = 0; i < str.length(); i++) {
            if (acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1) {
                return;
            }
        }

        if (acceptedChars.equals(FLOAT) ||
                (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
            if (str.indexOf(".") != -1) {
                if (getText(0, getLength()).indexOf(".") != -1) {
                    return;
                }
            }
        }

        if (negativeAccepted && str.indexOf("-") != -1) {
            if (str.indexOf("-") != 0 || offset != 0) {
                return;
            }
        }

        super.insertString(offset, str, attr);
    }
}
