/**
ï»¿Copyright (C) 2011
by 52 North Initiative for Geospatial Open Source Software GmbH

Contact: Andreas Wytzisk
52 North Initiative for Geospatial Open Source Software GmbH
Martin-Luther-King-Weg 24
48155 Muenster, Germany
info@52north.org

This program is free software; you can redistribute and/or modify it under 
the terms of the GNU General Public License version 2 as published by the 
Free Software Foundation.

This program is distributed WITHOUT ANY WARRANTY; even without the implied
WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program (see gnu-gpl v2.txt). If not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
visit the Free Software Foundation web page, http://www.fsf.org.

Author: Raimund Schnürer
Created: 2011-05-26
Modified: 2011-05-26
*/

package org.n52.sos.importer.view;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.controller.MainController;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final MainController mainController;
	
	private final JPanel stepContainerPanel = new JPanel();
	private final DescriptionPanel descriptionPanel = DescriptionPanel.getInstance();
	private final BackNextPanel backNextPanel = BackNextPanel.getInstance();
	
	public MainFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		this.setTitle("SOS Importer");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowChanged());
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(descriptionPanel);
		this.getContentPane().add(stepContainerPanel);
		this.getContentPane().add(backNextPanel);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void setStepPanel(JPanel stepPanel) {		
		stepContainerPanel.removeAll();
		stepContainerPanel.add(stepPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void showExitDialog() {
		int n = JOptionPane.showConfirmDialog(
			    this, "Do you really want to exit?\n",
			    "Exit", JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);

		if (n == JOptionPane.YES_OPTION) {
			ComboBoxItems.getInstance().save();
			System.exit(0);
		}
	}
	
	private class WindowChanged implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {		
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			mainController.exit();		
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}		
	}
}
