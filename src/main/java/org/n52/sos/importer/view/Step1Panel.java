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
package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.controller.Step1Controller;
import org.n52.sos.importer.tooltips.ToolTips;

/**
 * chooses a CSV file
 * @author Raimund
 *
 */
public class Step1Panel extends JPanel {
	
	static final long serialVersionUID = 1L;
	private final Step1Controller step1Controller;
	
	private final JLabel csvFileLabel = new JLabel("CSV File: ");
	private final JTextField csvFileTextField = new JTextField(25);
	private final JButton browse = new JButton("Browse");
	
	public Step1Panel(Step1Controller step1Controller) {
		super();
		this.step1Controller = step1Controller;
		this.setLayout(new FlowLayout());
		this.add(csvFileLabel);
		this.add(csvFileTextField);
		this.add(browse);
		
		csvFileTextField.setToolTipText(ToolTips.get("CSVFile"));
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
