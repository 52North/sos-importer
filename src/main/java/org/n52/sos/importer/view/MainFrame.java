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
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.controller.MainController;

/**
 * the actual frame of the application which can swap the 
 * different step panels
 * @author Raimund
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MainFrame.class);
	
	private final MainController mainController;
	
	private final JPanel stepContainerPanel;
	private final DescriptionPanel descriptionPanel;
	private final BackNextPanel backNextPanel;
	private final JPanel infoPanel;
	
	// TODO read this from general configuration file
	private String frameTitle = "SOS Importer 0.2 RC1";
	
	protected final static int DIALOG_WIDTH = 800;
	
	protected final static int DIALOG_HEIGHT = 600;
	
	public MainFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		this.initLookAndFeel();
		this.backNextPanel = BackNextPanel.getInstance();
		this.descriptionPanel = DescriptionPanel.getInstance();
		this.stepContainerPanel = new JPanel();
		this.infoPanel = new JPanel();
		this.setTitle(this.frameTitle);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowChanged());
		
		Container cp = this.getContentPane();
			
		cp.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		cp.add(descriptionPanel);
		cp.add(stepContainerPanel);
		cp.add(infoPanel);
		cp.add(backNextPanel);

		this.pack();
		// this centers the dialog on the current screen of the user
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initLookAndFeel() {
		//
		// try to set system look and feel, to nothing on error, should use
		// some default look and feel than.
		String lookNFeelClassName = "";
		try {
			lookNFeelClassName = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookNFeelClassName);
		} catch (ClassNotFoundException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\". Class not found.",e);
		} catch (InstantiationException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\". Could not instantiate class.",e);
		} catch (IllegalAccessException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\"",e);
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\"",e);
		}
		//
		//
	}

	public void setStepPanel(JPanel stepPanel) {		
		this.stepContainerPanel.removeAll();
		this.initLookAndFeel();
		this.stepContainerPanel.add(stepPanel);
		this.pack();
		this.setBounds(0, 0, MainFrame.DIALOG_WIDTH, MainFrame.DIALOG_HEIGHT);
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
		public void windowClosing(WindowEvent arg0) {
			mainController.exit();		
		}
		public void windowDeactivated(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
		public void windowOpened(WindowEvent arg0) {}
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}		
	}

	public void updateTitle(String csvFilePath) {
		int endOfPath = csvFilePath.lastIndexOf(File.separatorChar)+1;
		String file = csvFilePath.substring(endOfPath);
		String path = csvFilePath.substring(0,endOfPath);
		String newTitle = this.frameTitle + 
			" - file:\"" + file + "\" (path: \"" + path + "\")";
		this.setTitle(newTitle);
	}
}
