package org.n52.sos.importer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


public class CSVTest {

	public static void main(String[] args) {
		File f = new File("C:\\Users\\Raimund\\Documents\\Mappe1.csv");
		try {
			CSVReader reader = new CSVReader(new FileReader(f), ';');
			
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
				for (String s: nextLine) {
					System.out.println(s);
				}
		    }

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
