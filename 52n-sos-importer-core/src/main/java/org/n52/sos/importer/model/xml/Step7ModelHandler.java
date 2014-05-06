/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.model.xml;

import org.n52.sos.importer.Constants.ImportStrategy;
import org.n52.sos.importer.model.Step7Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.OfferingDocument.Offering;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.SosMetadataDocument.SosMetadata;

/**
 * Saves the SOS URL and the offering settings.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step7ModelHandler implements ModelHandler<Step7Model> {

	private static final Logger LOG = LoggerFactory.getLogger(Step7ModelHandler.class);

	@Override
	public void handleModel(final Step7Model stepModel,
			final SosImportConfiguration sosImportConf) {
		LOG.trace("handleModel()");
		SosMetadata sosMeta = sosImportConf.getSosMetadata();
		//
		if (sosMeta == null) {
			sosMeta = sosImportConf.addNewSosMetadata();
			LOG.debug("Added new SosMetadata element.");
		}
		sosMeta.setURL(stepModel.getSosURL());
		Offering off = sosMeta.getOffering();
		if (off == null) {
			off = sosMeta.addNewOffering();
			LOG.debug("Added new Offering element");
		}
		off.setGenerate(stepModel.isGenerateOfferingFromSensorName());
		if (!off.getGenerate()) {
			off.setStringValue(stepModel.getOffering());
		}
		if (stepModel.getBinding() != null && !stepModel.getBinding().isEmpty()) {
			sosMeta.setBinding(stepModel.getBinding());
		}
		if (stepModel.getVersion() != null && !stepModel.getVersion().isEmpty()) {
			sosMeta.setVersion(stepModel.getVersion());
		}
		addImportStrategy(stepModel.getImportStrategy(), sosImportConf);
		if (stepModel.getImportStrategy().equals(ImportStrategy.SweArrayObservationWithSplitExtension)) {
			sosMeta.setInsertSweArrayObservationTimeoutBuffer(stepModel.getSendBuffer());
			addHunkSize(stepModel.getHunkSize(), sosImportConf);
		}
	}

	private void addHunkSize(final int hunkSize,
			final SosImportConfiguration sosImportConf) {
		addAdditionalMetadata(sosImportConf, Key.HUNK_SIZE, Integer.toString(hunkSize));
	}

	private void addImportStrategy(final ImportStrategy importStrategy,
			final SosImportConfiguration sosImportConf) {
		final Enum key = Key.IMPORT_STRATEGY;
		String value = "SingleObservation";
		if (importStrategy == ImportStrategy.SweArrayObservationWithSplitExtension) {
			value = "SweArrayObservationWithSplitExtension";
		}
		addAdditionalMetadata(sosImportConf, key, value);
	}

	private void addAdditionalMetadata(final SosImportConfiguration sosImportConf,
			final Enum key,
			final String value) {
		if (!sosImportConf.isSetAdditionalMetadata()) {
			sosImportConf.addNewAdditionalMetadata();
		}
		Metadata importStrategyMetadata = null;
		if (sosImportConf.getAdditionalMetadata().getMetadataArray() == null ||
				sosImportConf.getAdditionalMetadata().getMetadataArray().length == 0) {
			importStrategyMetadata = sosImportConf.getAdditionalMetadata().addNewMetadata();
		} else {
			boolean notFound = true;
			for (final Metadata metadata : sosImportConf.getAdditionalMetadata().getMetadataArray()) {
				if (metadata.getKey().equals(key)) {
					importStrategyMetadata = metadata;
					notFound = false;
					break;
				}
			}
			if (notFound) {
				importStrategyMetadata = sosImportConf.getAdditionalMetadata().addNewMetadata();
			}
		}
		importStrategyMetadata.setKey(key);
		importStrategyMetadata.setValue(value);
	}

}
