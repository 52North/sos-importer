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
package org.n52.sos.importer.model.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.model.Step3Model;
import org.x52North.sensorweb.sos.importer.x04.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x04.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step3ModelHandlerTest {

    @Test
    public void shouldStoreGroupWhenNotSet() {
        List<String> selection = new ArrayList<String>();
        selection.add("Date & Time");
        selection.add("Combination");
        selection.add("M/d/yyyy h.mm aSEPnull");
        Step3Model model = new Step3Model(0, 0, false);
        model.addSelection(selection);

        SosImportConfiguration sosImportConf = SosImportConfiguration.Factory.newInstance();
        new Step3ModelHandler().handleModel(model, sosImportConf);

        final Metadata[] metadataArray = sosImportConf.getCsvMetadata().getColumnAssignments().getColumnArray(0)
                .getMetadataArray();
        final int groupIndex = getGroupMetadataElementIndex(metadataArray);
        Assert.assertThat(groupIndex, Matchers.equalTo(0));
        Assert.assertThat(metadataArray[groupIndex].getValue(),Is.is("1"));
    }

    private int getGroupMetadataElementIndex(final Metadata[] metadataArray) {
        int i = 0;
        for (Metadata metadata : metadataArray) {
            if (metadata.getKey().equals(Key.GROUP)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Test
    public void shouldStoreGroupWhenSet() {
        String testGroup = "test-group";
        List<String> selection = Arrays.asList("Date & Time", "Combination", "M/d/yyyy h.mm aSEP" + testGroup);
        Step3Model model = new Step3Model(0, 0, false);
        model.addSelection(selection);

        SosImportConfiguration sosImportConf = SosImportConfiguration.Factory.newInstance();
        new Step3ModelHandler().handleModel(model, sosImportConf);

        final Metadata[] metadataArray = sosImportConf.getCsvMetadata().getColumnAssignments().getColumnArray(0).getMetadataArray();
        final int groupIndex = getGroupMetadataElementIndex(metadataArray);
        Assert.assertThat(groupIndex, Matchers.greaterThanOrEqualTo(0));
        Assert.assertThat(metadataArray[groupIndex].getValue(), Is.is(testGroup));
    }

}
