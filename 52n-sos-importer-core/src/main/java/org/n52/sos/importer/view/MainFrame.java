/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.view.combobox.ComboBoxItems;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the actual frame of the application which can swap the 
 * different step panels
 * @author Raimund
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
	
	private final MainController mainController;
	
	private final JPanel stepContainerPanel;
	private final DescriptionPanel descriptionPanel;
	private final BackNextPanel backNextPanel;
	
	// TODO read this from general configuration file
	private final String frameTitle = Lang.l().frameTitle();
	
	public MainFrame(final MainController mainController) {
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
		
		final Container cp = getContentPane();
			
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
		} catch (final ClassNotFoundException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\". Class not found.",e);
		} catch (final InstantiationException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\". Could not instantiate class.",e);
		} catch (final IllegalAccessException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\"",e);
		} catch (final UnsupportedLookAndFeelException e) {
			logger.error("System Look and Feel could not be set to \"" + 
					lookNFeelClassName + "\"",e);
		}
		//
		//
	}

	public void setStepPanel(final JPanel stepPanel) {		
		stepContainerPanel.removeAll();
		initLookAndFeel();
		stepContainerPanel.add(stepPanel);
		// prevent irrational frame resizing
		// pack();
		setBounds(this.getBounds().x, this.getBounds().y, Constants.DIALOG_WIDTH, Constants.DIALOG_HEIGHT);
		setVisible(true);
	}
	
	public void showExitDialog() {
		final int n = JOptionPane.showConfirmDialog(
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
		@Override
		public void windowClosing(final WindowEvent arg0) {
			mainController.exit();		
		}
		@Override
		public void windowDeactivated(final WindowEvent arg0) {}
		@Override
		public void windowDeiconified(final WindowEvent arg0) {}
		@Override
		public void windowIconified(final WindowEvent arg0) {}
		@Override
		public void windowOpened(final WindowEvent arg0) {}
		@Override
		public void windowActivated(final WindowEvent arg0) {}
		@Override
		public void windowClosed(final WindowEvent arg0) {}		
	}

	public void updateTitle(final String csvFilePath) {
		final int endOfPath = csvFilePath.lastIndexOf(File.separatorChar)+1;
		final String file = csvFilePath.substring(endOfPath);
		final String path = csvFilePath.substring(0,endOfPath);
		final String newTitle = frameTitle + Lang.l().frameTitleExtension(file,path);
		setTitle(newTitle);
	}
	
	@Override
	public void repaint() {
		backNextPanel.repaint();
		super.repaint();
	}
}
