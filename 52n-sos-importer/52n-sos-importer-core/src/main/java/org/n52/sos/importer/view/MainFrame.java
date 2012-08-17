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
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;

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
	
	// TODO read this from general configuration file
	private String frameTitle = Lang.l().frameTitle();
	
	public MainFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		initLookAndFeel();
		backNextPanel = BackNextPanel.getInstance();
		descriptionPanel = DescriptionPanel.getInstance();
		stepContainerPanel = new JPanel();
		stepContainerPanel.setLayout(new BorderLayout());
		setTitle(frameTitle);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(new WindowChanged());
		
		Container cp = this.getContentPane();
			
		cp.setLayout(new BorderLayout());
		cp.add(descriptionPanel, BorderLayout.NORTH);
		cp.add(stepContainerPanel, BorderLayout.CENTER);
		cp.add(backNextPanel, BorderLayout.SOUTH);

		if (Constants.GUI_DEBUG) {
			descriptionPanel.setBorder(Constants.DEBUG_BORDER);
			stepContainerPanel.setBorder(Constants.DEBUG_BORDER);
			backNextPanel.setBorder(Constants.DEBUG_BORDER);
			setResizable(true);
		}
		pack();
		// this centers the dialog on the current screen of the user
		setBounds(0, 0, Constants.DIALOG_WIDTH, Constants.DIALOG_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
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
		stepContainerPanel.removeAll();
		initLookAndFeel();
		stepContainerPanel.add(stepPanel);
		pack();
		setBounds(this.getBounds().x, this.getBounds().y, Constants.DIALOG_WIDTH, Constants.DIALOG_HEIGHT);
		setVisible(true);
	}
	
	public void showExitDialog() {
		int n = JOptionPane.showConfirmDialog(
			    this, Lang.l().exitDialogQuestion(),
			    Lang.l().exitDialogTitle(), JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);

		if (n == JOptionPane.YES_OPTION) {
			ComboBoxItems.getInstance().save();
			Constants.save();
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
		String newTitle = this.frameTitle + Lang.l().frameTitleExtension(file,path);
		this.setTitle(newTitle);
	}
	
	@Override
	public void repaint() {
		this.backNextPanel.repaint();
		super.repaint();
	}
}
