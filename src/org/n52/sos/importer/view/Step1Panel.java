package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.controller.Step1Controller;
import org.n52.sos.importer.model.tooltips.ToolTipLabel;
import org.n52.sos.importer.model.tooltips.ToolTips;

public class Step1Panel extends JPanel {
	
	static final long serialVersionUID = 1L;
	private final Step1Controller step1Controller;
	
	private final JLabel csvFileLabel = new ToolTipLabel("CSV File: ", ToolTips.get("CSVFile"));
	private final JTextField csvFileTextField = new JTextField(25);
	private final JButton browse = new JButton("Browse");
	
	public Step1Panel(Step1Controller step1Controller) {
		super();
		this.step1Controller = step1Controller;
		this.setLayout(new FlowLayout());
		this.add(csvFileLabel);
		this.add(csvFileTextField);
		this.add(browse);
		
		//csvFileTextField.setMinimumSize(new Dimension(100, 0));
		browse.addActionListener(new BrowseButtonClicked());
	}
	
	public void setCSVFilePath(String filePath) {
		csvFileTextField.setText(filePath);
	}
	
	public String getCSVFilePath() {
		return csvFileTextField.getText();
	}
	
	private class BrowseButtonClicked implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			step1Controller.browseButtonClicked();
		}
	}
}
