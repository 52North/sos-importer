/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.view;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.DefaultEditorKit;

import org.n52.sos.importer.view.i18n.Lang;

/**
 * Provides a cut, copy, and paste {@link JPopupMenu}.
 *
 * See <a href="https://docs.oracle.com/javase/tutorial/uiswing/dnd/textpaste.html">Oracle Swing Tutorial</a>.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class CutCopyPasteContextMenu extends JPopupMenu {

    private static final long serialVersionUID = 1L;

    public CutCopyPasteContextMenu() {
        //
        // CUT
        //
        JMenuItem jMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        jMenuItem.setText(Lang.l().cut());
        add(jMenuItem);
        //
        // COPY
        //
        jMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        jMenuItem.setText(Lang.l().copy());
        add(jMenuItem);
        //
        // PASTE
        //
        jMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        jMenuItem.setText(Lang.l().paste());
        add(jMenuItem);
    }

}
