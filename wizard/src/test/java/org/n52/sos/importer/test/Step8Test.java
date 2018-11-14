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
package org.n52.sos.importer.test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step8Controller;
import org.n52.sos.importer.model.Step7Model;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step8Test {

    //CHECKSTYLE:OFF
    public static void main(final String[] args) {
        //CHECKSTYLE:ON
        final JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            final File configFile = fc.getSelectedFile();

            final Step7Model s7M = new Step7Model("http://localhost:8080/ImportTestSOS/sos",
                    configFile, true, null, "1.0.0", null, false);
            final MainController f = MainController.getInstance();

            f.setStepController(new Step8Controller(s7M));
        }
    }

}
