package org.n52.sos.importer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class Step1 extends StepPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel csvFileLabel = new JLabel("CSV file:   ");
	private final JTextField csvFileTextField = new JTextField(25);
	private final JButton browse = new JButton("Browse");
	
	public Step1(MainFrame mainFrame) {
		super(mainFrame);

		csvFileTextField.setMinimumSize(new Dimension(100, 0));
		browse.addActionListener(new BrowseButtonClicked());
		
		this.setLayout(new FlowLayout());
		this.add(csvFileLabel);
		this.add(csvFileTextField);
		this.add(browse);
	}
	
	public void chooseCSVFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new CSVFileFilter()); 
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			csvFileTextField.setText(fc.getSelectedFile().getAbsolutePath());
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
	
	private class BrowseButtonClicked implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
		    chooseCSVFile();
		}
	}
	
	private String readFile(File f) {
		StringBuffer sb = new StringBuffer();
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
	
	@Override
	protected String getDescription() {
		return "Step 1: Choose CSV file";
	}

	@Override
	protected void back() {
	}

	@Override
	protected void next() {
		String filePath = csvFileTextField.getText();
		
		System.out.println(filePath);
		
		if (filePath.equals("")) {
			JOptionPane.showMessageDialog(getMainFrame(),
				    "Please choose a CSV file",
				    "File missing",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}	

		File f = new File(filePath);
		//TODO
		/*
		if (!f.exists() || !f.isDirectory()) {
			JOptionPane.showMessageDialog(getMainFrame(),
				    "The specified file does not exist",
				    "File not found",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}*/
		String s = readFile(f);
		getMainFrame().setStepPanel(new Step2(getMainFrame(), s));	
	}
}
