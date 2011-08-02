package org.n52.sos.importer.model.tooltips;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ToolTipConverter {

	private static final String TEXT_FILE_PATH = "/org/n52/sos/importer/model/tooltips/ToolTips";
	
	private static final String PROPERTIES_FILE_PATH = "/org/n52/sos/importer/model/tooltips/tooltips.properties";
	
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
