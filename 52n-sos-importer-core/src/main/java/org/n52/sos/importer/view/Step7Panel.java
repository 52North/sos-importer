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
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.Constants;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses the URL of the Sensor Observation Service
 * @author Raimund
 * TODO ADD offering autogenerate button
 */
public class Step7Panel extends JPanel {

	private static final Logger logger = Logger.getLogger(Step7Panel.class);
	
	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel sosComboBox;
	
	private Step7Panel _this;
	
	private JLabel saveConfigJL;
	private JCheckBox saveConfigJCB;
	
	private JLabel directImportJL;
	private JCheckBox directImportJCB;
	
	private JLabel configFileJL;
	private JButton configFileDirSelectorJB;
	private JTextField configFileNameJT;
	
	private String configFileName = Constants.XML_CONFIG_DEFAULT_FILE_NAME;

	private String configFilePath = "";
	
	public Step7Panel(Step7Controller s7C) {
		this._this = this;
		Step7Model s7M = (Step7Model) s7C.getModel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		sosComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), Lang.l().sosURL(), ToolTips.get(ToolTips.SOS));
		this.add(sosComboBox);
		/*
		 * 	directImport Checkbox
		 */
		directImportJL = new JLabel(Lang.l().step7DirectImport() + "?");
		directImportJCB = new JCheckBox();
		directImportJCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * if deactivated, active saveConfig checkbox. 
				 * Than the user needs an active step to lose their work
				 * controller switch
				 */
				if (!directImportJCB.isSelected()) {
					_this.configFileDirSelectorJB.setVisible(true);
					_this.configFileNameJT.setVisible(true);
					_this.configFileJL.setVisible(true);
					_this.saveConfigJCB.setSelected(true);
					BackNextController.getInstance().setNextButtonEnabled(false);
				} else if (!_this.saveConfigJCB.isSelected()) {
					BackNextController.getInstance().setNextButtonEnabled(true);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("directImport state changed. is selected?" + 
							directImportJCB.isSelected());
				}
			}
		});
		directImportJCB.setSelected(s7M.isDirectImport());
		directImportJCB.setEnabled(true);
		JPanel directImportPanel = new JPanel();
		directImportPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		directImportPanel.add(directImportJL);
		directImportPanel.add(directImportJCB);
		this.add(directImportPanel);
		/*
		 *	saveConfig Checkbox
		 */
		saveConfigJL = new JLabel(Lang.l().step7SaveConfig() + "?");
		saveConfigJCB = new JCheckBox();
		saveConfigJCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * If selected, display configuration file selector
				 * if not, remove display and reset values
				 */
				if (_this.saveConfigJCB.isSelected()) {
					_this.configFileDirSelectorJB.setVisible(true);
					_this.configFileNameJT.setVisible(true);
					_this.configFileJL.setVisible(true);
					if (_this.configFilePath == null || 
							_this.configFileName == null ||
							_this.configFilePath.equals("") ||
							_this.configFileName.equals("") || 
							_this.configFileName.indexOf(".xml") == -1 ) {
						BackNextController.getInstance().setNextButtonEnabled(false);
					}
				} else {
					_this.configFileDirSelectorJB.setVisible(false);
					_this.configFileNameJT.setVisible(false);
					_this.configFileJL.setVisible(false);
					if (!_this.directImportJCB.isSelected()) {
						BackNextController.getInstance().setNextButtonEnabled(false);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("saveConfig state changed. is selected?" + 
							_this.saveConfigJCB.isSelected());
				}
			}
		});
		saveConfigJCB.setSelected(s7M.isSaveConfig());
		saveConfigJCB.setEnabled(true);
		JPanel saveConfigPanel = new JPanel();
		saveConfigPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		saveConfigPanel.add(saveConfigJL);
		saveConfigPanel.add(saveConfigJCB);
		this.add(saveConfigPanel);
		/*
		 * configFileChooser
		 */
		configFileDirSelectorJB = new JButton(Lang.l().step7ConfigFileButton());
		configFileDirSelectorJB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showDialog(_this, Lang.l().step7ConfigFileDialogTitel())
						== JFileChooser.APPROVE_OPTION){
					if (fc.getSelectedFile().isDirectory() && fc.getSelectedFile().canWrite()) {
						_this.configFilePath  = fc.getSelectedFile().getAbsolutePath();
						BackNextController.getInstance().setNextButtonEnabled(true);
						return;
					} else {
						JOptionPane.showMessageDialog(_this, 
								Lang.l().step7ConfigDirNotDirOrWriteable(
										fc.getSelectedFile().getAbsolutePath()), 
								Lang.l().errorDialogTitle(), 
								JOptionPane.ERROR_MESSAGE);
					}
				}
				BackNextController.getInstance().setNextButtonEnabled(false);
			}
		});
		configFileDirSelectorJB.setVisible(s7M.isSaveConfig());
		configFileJL = new JLabel(Lang.l().step7ConfigFileLabel() + ":");
		configFileNameJT = new JTextField(15);
		configFileNameJT.setText(configFileName);
		configFileNameJT.setVisible(s7M.isSaveConfig());
		configFileJL.setVisible(s7M.isSaveConfig());
		JPanel configFilePanel = new JPanel();
		configFilePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		configFilePanel.add(configFileJL);
		configFilePanel.add(configFileDirSelectorJB);
		configFilePanel.add(configFileNameJT);
		this.add(configFilePanel);
		// enable next button only if something is checked!
		BackNextController.getInstance().setNextButtonEnabled(
				saveConfigJCB.isSelected() || directImportJCB.isSelected() );
	}
	
	public String getSOSURL() {
		return (String) this.sosComboBox.getSelectedItem();
	}
	
	public void setSOSURL(String sosURL) {
		this.sosComboBox.setSelectedItem(sosURL);
	}
	
	public boolean isDirectImport() {
		return this.directImportJCB.isSelected();
	}
	
	public void setDirectImport(boolean isDirectImport) {
		this.directImportJCB.setSelected(isDirectImport);
	}
	
	public boolean isSaveConfig() {
		return this.saveConfigJCB.isSelected();
	}
	
	public void setSaveConfig(boolean isSaveConfig) {
		this.saveConfigJCB.setSelected(isSaveConfig);
	}
	
	public String getConfigFile() {
		return this.configFilePath + File.separatorChar + this.configFileName;
	}
	
	public void setConfigFile(String configFile) {
		// split String by last index of File.separatorChar and save to fields
		this.configFilePath = configFile.substring(0, configFile.lastIndexOf(File.separatorChar));
		this.configFileName = configFile.substring(configFile.lastIndexOf(File.separatorChar)+1);
	}
}


