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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses the URL of the Sensor Observation Service
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * 
 * TODO change to use OneTimeFeeder from feeder module
 */
public class Step7Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Step7Panel _this;
	/*
	 * SOS URL
	 */
	private EditableJComboBoxPanel sosComboBox;
	
//	private JLabel directImportJL;
//	private JCheckBox directImportJCB;

	/*
	 * CONFIG
	 */
//	private JLabel saveConfigJL;
//	private JCheckBox saveConfigJCB;
	
	private JLabel configFileJL;
	private JButton configFileDirSelectorJB;
	private JTextField configFileNameJT;
	private String configFileName = Constants.XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX;
	private String configFilePath = "";
	/*
	 * OFFERING
	 */
	private JPanel offeringPanel;
	private JCheckBox offeringGenerationCheckbox;
	private JPanel offeringNamePanel;
	private JTextField offeringInputTextField;
	
	public Step7Panel(Step7Controller s7C) {
		_this = this;
		Step7Model s7M = (Step7Model) s7C.getModel();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		sosComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), Lang.l().sosURL(), ToolTips.get(ToolTips.SOS));
		sosComboBox.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		add(sosComboBox);
		/*
		 * 	directImport Checkbox
		 */
//		directImportJL = new JLabel(Lang.l().step7DirectImport() + "?");
//		directImportJCB = new JCheckBox();
//		directImportJCB.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				/*
//				 * if deactivated, active saveConfig checkbox. 
//				 * Than the user needs an active step to lose their work
//				 * controller switch
//				 */
//				if (!directImportJCB.isSelected()) {
//					configFileDirSelectorJB.setVisible(true);
//					configFileNameJT.setVisible(true);
//					configFileJL.setVisible(true);
//					saveConfigJCB.setSelected(true);
//					BackNextController.getInstance().setNextButtonEnabled(false);
//				} else if (!saveConfigJCB.isSelected()) {
//					BackNextController.getInstance().setNextButtonEnabled(true);
//				}
//				if (logger.isDebugEnabled()) {
//					logger.debug("directImport state changed. is selected?" + 
//							directImportJCB.isSelected());
//				}
//			}
//		});
//		directImportJCB.setSelected(s7M.isDirectImport());
//		directImportJCB.setEnabled(true);
//		JPanel directImportPanel = new JPanel();
//		directImportPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		directImportPanel.add(directImportJL);
//		directImportPanel.add(directImportJCB);
//		add(directImportPanel);
		/*
		 * 	Offering input and checkbox
		 */
		offeringPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) offeringPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		add(offeringPanel);
		
		JLabel offeringLabel = new JLabel(Lang.l().step7OfferingCheckBoxLabel());
		offeringPanel.add(offeringLabel);
		
		offeringGenerationCheckbox = new JCheckBox();
		offeringGenerationCheckbox.setSelected(true);
		offeringGenerationCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!offeringGenerationCheckbox.isSelected()) {
					offeringNamePanel.setVisible(true);
				} else {
					offeringNamePanel.setVisible(false);
				}
			}
		});
		offeringPanel.add(offeringGenerationCheckbox);
		offeringPanel.setToolTipText(ToolTips.get(ToolTips.OFFERING));
		
		offeringNamePanel = new JPanel();
		offeringNamePanel.setVisible(false);
		FlowLayout fl_offeringNamePanel = (FlowLayout) offeringNamePanel.getLayout();
		fl_offeringNamePanel.setAlignment(FlowLayout.LEADING);
		add(offeringNamePanel);
		
		JLabel offeringInputLabel = new JLabel(Lang.l().step7OfferingInputTextfieldLabel());
		offeringNamePanel.add(offeringInputLabel);
		
		offeringInputTextField = new JTextField();
		offeringNamePanel.add(offeringInputTextField);
		offeringInputTextField.setColumns(10);
		/*
		 *	saveConfig Checkbox
		 */
//		saveConfigJL = new JLabel(Lang.l().step7SaveConfig() + "?");
//		saveConfigJCB = new JCheckBox();
//		saveConfigJCB.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				/*
//				 * If selected, display configuration file selector
//				 * if not, remove display and reset values
//				 */
//				if (saveConfigJCB.isSelected()) {
//					configFileDirSelectorJB.setVisible(true);
//					configFileNameJT.setVisible(true);
//					configFileJL.setVisible(true);
//					if (configFilePath == null || 
//							configFileName == null ||
//							configFilePath.equals("") ||
//							configFileName.equals("") || 
//							configFileName.indexOf(".xml") == -1 ) {
//						BackNextController.getInstance().setNextButtonEnabled(false);
//					}
//				} else {
//					configFileDirSelectorJB.setVisible(false);
//					configFileNameJT.setVisible(false);
//					configFileJL.setVisible(false);
//					if (!directImportJCB.isSelected()) {
//						BackNextController.getInstance().setNextButtonEnabled(false);
//					}
//				}
//				if (logger.isDebugEnabled()) {
//					logger.debug("saveConfig state changed. is selected?" + 
//							saveConfigJCB.isSelected());
//				}
//			}
//		});
//		saveConfigJCB.setSelected(s7M.isSaveConfig());
//		saveConfigJCB.setEnabled(true);
//		JPanel saveConfigPanel = new JPanel();
//		saveConfigPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		saveConfigPanel.add(saveConfigJL);
//		saveConfigPanel.add(saveConfigJCB);
//		add(saveConfigPanel);
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
						configFilePath  = fc.getSelectedFile().getAbsolutePath();
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
		String tmp = MainController.getInstance().getFilename();
		tmp = tmp + configFileName;
		configFileNameJT.setText(tmp);
		configFileNameJT.setVisible(s7M.isSaveConfig());
		configFileJL.setVisible(s7M.isSaveConfig());
		JPanel configFilePanel = new JPanel();
		configFilePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		configFilePanel.add(configFileJL);
		configFilePanel.add(configFileDirSelectorJB);
		configFilePanel.add(configFileNameJT);
		add(configFilePanel);
		// TODO remove if not required anymore
		configFileDirSelectorJB.setVisible(true);
		configFileNameJT.setVisible(true);
		configFileJL.setVisible(true);
		configFilePanel.setVisible(true);
		configFilePanel.setEnabled(true);
		// End of remove
		// enable next button only if something is checked!
//		BackNextController.getInstance().setNextButtonEnabled(
//				saveConfigJCB.isSelected() || directImportJCB.isSelected() );
	}
	
	public String getSOSURL() {
		return (String) sosComboBox.getSelectedItem();
	}
	
	public void setSOSURL(String sosURL) {
		sosComboBox.setSelectedItem(sosURL);
	}
	
	public boolean isDirectImport() {
		// TODO update
		return false; //directImportJCB.isSelected();
	}
	
	public void setDirectImport(boolean isDirectImport) {
		// TODO update
		//directImportJCB.setSelected(isDirectImport);
	}
	
	public boolean isSaveConfig() {
		// TODO update
		return true; // this.saveConfigJCB.isSelected();
	}
	
	public void setSaveConfig(boolean isSaveConfig) {
		// TODO update
		// saveConfigJCB.setSelected(isSaveConfig);
	}
	
	public String getConfigFile() {
		return configFilePath + File.separatorChar + configFileNameJT.getText();
	}
	
	public void setConfigFile(String configFile) {
		// split String by last index of File.separatorChar and save to fields
		configFilePath = configFile.substring(0, configFile.lastIndexOf(File.separatorChar)+1);
		configFileName = configFile.substring(configFile.lastIndexOf(File.separatorChar)+1);
	}

	public boolean isGenerateOfferingFromSensorName() {
		return offeringGenerationCheckbox.isSelected();
	}

	public String getOfferingName() {
		return offeringInputTextField.getText();
	}
}


