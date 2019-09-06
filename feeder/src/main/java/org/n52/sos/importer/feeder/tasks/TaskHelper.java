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
package org.n52.sos.importer.feeder.tasks;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.n52.sos.importer.feeder.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains helper and shared methods for the tasks.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class TaskHelper {

    private static final Logger LOG = LoggerFactory.getLogger(TaskHelper.class);
    private Configuration configuration;

    public TaskHelper(Configuration configuration) {
        this.configuration = configuration;
    }

    protected List<File> getFiles(File directory) {
        final File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.isFile() &&
                        pathname.canRead() &&
                        (configuration.getLocaleFilePattern() != null
                        ? configuration.getLocaleFilePattern().matcher(pathname.getName()).matches()
                                : true);
            }
        });
        if (files == null || files.length == 0) {
            LOG.error("No file found in directory '{}'", directory.getAbsolutePath());
            return Collections.emptyList();
        }
        return Arrays.asList(files);
    }

}
