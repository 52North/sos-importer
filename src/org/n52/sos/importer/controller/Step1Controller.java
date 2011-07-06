package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.view.Step1Panel;

public class Step1Controller extends StepController {
	
	private Step1Panel step1Panel;
	private Step1Model step1Model;
	
	public Step1Controller() {
		step1Model = new Step1Model();
		step1Panel = new Step1Panel(this);
	}
	
	@Override
	public String getDescription() {
		return "Step 1: Choose CSV file";
	}
	
	@Override
	public void loadSettings() {		
		//disable "back" button
		BackNextController.getInstance().setBackButtonEnabled(false);
		
		String csvFilePath = step1Model.getCSVFilePath();
		step1Panel.setCSVFilePath(csvFilePath);
	}
	
	public void saveSettings() {
		String csvFilePath = step1Panel.getCSVFilePath();
		step1Model.setCSVFilePath(csvFilePath);
	}

	@Override
	public StepController getNextStepController() {
		String filePath = step1Panel.getCSVFilePath();
		
		if (filePath.equals("")) {
			JOptionPane.showMessageDialog(null,
				    "Please choose a CSV file",
				    "File missing",
				    JOptionPane.WARNING_MESSAGE);
			return null;
		}		
		
		File f = new File(filePath);
		if (isValid(f)) {
			String csvFileContent = readFile(f);
			//show "back" button
			BackNextController.getInstance().setBackButtonEnabled(true);
					
			Step2Model s2m = new Step2Model();
			s2m.setCSVFileContent(csvFileContent);
			return new Step2Controller(s2m);
		}
		return null;
	}
	
	private boolean isValid(File f) {		
		if (!f.exists()) {
			JOptionPane.showMessageDialog(null,
				    "The specified file does not exist.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (!f.isFile()) {
			JOptionPane.showMessageDialog(null,
				    "Please specify a file, not a directory.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
			
		if (!f.canRead()) {
			JOptionPane.showMessageDialog(null,
				    "No reading access on the specified file.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private String readFile(File f) {
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return sb.toString();
	}
	
	public void browseButtonClicked() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new CSVFileFilter()); 
		if (fc.showOpenDialog(getStepPanel()) == JFileChooser.APPROVE_OPTION) {
			step1Panel.setCSVFilePath(fc.getSelectedFile().getAbsolutePath());
		}
	}

	private class CSVFileFilter extends FileFilter {
		@Override
	    public boolean accept(File file) {
	        return file.isDirectory() || 
	        	   file.getName().toLowerCase().endsWith(".csv");
	    }	
		@Override
	    public String getDescription() {
	        return "CSV files";
	    }
	}

	@Override
	public JPanel getStepPanel() {
		return step1Panel;
	}
}
