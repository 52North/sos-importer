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
package org.n52.sos.importer.view.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * converts human-readable tooltips in the text file 
 * into a machine-readable properties file
 * @author Raimund
 *
 */
public class ToolTipConverter {

	private static final String TEXT_FILE_PATH = "/org/n52/sos/importer/tooltips/ToolTips";
	
	private static final String PROPERTIES_FILE_PATH = "/org/n52/sos/importer/tooltips/tooltips.properties";
	
	public static void main(String[] args) {
		URL textURL = ToolTips.class.getResource(TEXT_FILE_PATH);
		URL propertiesURL = ToolTips.class.getResource(PROPERTIES_FILE_PATH);
		File textFile;
		File propertiesFile;
		
		try {
			textFile = new File(textURL.toURI());
			propertiesFile = new File(propertiesURL.toURI());
		} catch (URISyntaxException e) {
			textFile = new File(textURL.getPath());
			propertiesFile = new File(propertiesURL.getPath());
		}

		try {
			FileReader fr = new FileReader(textFile);
			FileWriter fw = new FileWriter(propertiesFile);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith("="))
					bw.write(line + "<html>\\" + "\n");
				else if (line.equals("---"))
					bw.write("</html>" + "\n");
				else 
					bw.write(line + "<br>\\" + "\n");
			}
			bw.flush();
			bw.close();
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
}
