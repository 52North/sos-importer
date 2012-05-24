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
package org.n52.sos.importer.test;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.xmlbeans.XmlException;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.OfferingDocument.Offering;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.SosMetadataDocument.SosMetadata;

public class ConfigurationTest {

	private static final Logger logger = Logger.getLogger(ConfigurationTest.class);
	
	private static final File testConfig = new File("src/test/xml/configuration-test.xml");
	/**
	 * @param args
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) throws XmlException, IOException {
		// init looger
		Logger root = Logger.getRootLogger();
		root.setLevel(Level.DEBUG);
		root.addAppender(new ConsoleAppender(new PatternLayout("%-6r %-1p (%c{1}.java:%L) - %m %n")));
		//
		if (logger.isDebugEnabled()) {
			logger.debug("Starting configuration test.");
			logger.debug("try loading file \"" + 
					testConfig.getAbsolutePath() + "\"");
		}
		SosImportConfigurationDocument importerConfiguration = SosImportConfigurationDocument.Factory.parse(testConfig);
		SosImportConfiguration importConf = importerConfiguration.getSosImportConfiguration();
		if(importConf.getDataFile().isSetLocalFile()) {
			String path = importConf.getDataFile().getLocalFile().getPath();
			if(logger.isDebugEnabled()) {
				logger.debug("Path for datafile: " + path);
			}
		}
		CsvMetadata csvMeta = importConf.getCsvMetadata();
		if (logger.isDebugEnabled()) {
			logger.debug("CSV metadata found:");
			//
			boolean useHeader = csvMeta.getUseHeader();
			int firstLineWithData = csvMeta.getFirstLineWithData();
			String commentIndicator = csvMeta.getParameter().getCommentIndicator();
			String elemSep = csvMeta.getParameter().getColumnSeparator();
			String txtIndi = csvMeta.getParameter().getTextIndicator();
			Column[] columns = csvMeta.getColumnAssignments().getColumnArray();
			Metadata[] colMetadata;
			//
			logger.debug("useHeader: "         + useHeader);
			logger.debug("firstLineWithData: " + firstLineWithData);
			logger.debug("commentIndicator: "  + commentIndicator);
			logger.debug("elementSeparator: "  + elemSep);
			logger.debug("textIndicator: "     + txtIndi);
			logger.debug("Columns (" + columns.length + "): ");
			for (int i = 0; i < columns.length; i++) {
				Column column = columns[i];
				logger.debug("Column[" + column.getNumber() + "]: " + 
						"; type: " +
						column.getType().toString() +
						"; metadata?: " +
						column.sizeOfMetadataArray());
				if(column.sizeOfMetadataArray() > 0) {
					colMetadata = column.getMetadataArray();
					for (int j = 0; j < colMetadata.length; j++) {
						Metadata m = colMetadata[j];
						logger.debug("\tKey: " +
								m.getKey().toString() + 
								"; Value: " +
								m.getValue());
					}
				}
			}
		}
		SosMetadata sosMeta = importConf.getSosMetadata();
		if (logger.isDebugEnabled()) {
			logger.debug("SOS metadata found");
			//
			String sosUrl = sosMeta.getURL();
			boolean offeringAutogenerate = false;
			Offering sosOff = sosMeta.getOffering();
			String offeringInfo = sosOff.getStringValue();
			//
			if(sosOff.isSetGenerate()) {
				offeringAutogenerate = sosOff.getGenerate();
			}
			//
			logger.debug("sosURL: " + sosUrl);
			logger.debug("autogenerate Offering?: " + offeringAutogenerate);
			logger.debug("offeringInfo: \"" + offeringInfo + "\"");
		}
	}

}
