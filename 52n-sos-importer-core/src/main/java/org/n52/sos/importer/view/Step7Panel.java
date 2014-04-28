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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step7Controller;
import org.n52.sos.importer.view.combobox.EditableComboBoxItems;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses the URL of the Sensor Observation Service
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step7Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private EditableJComboBoxPanel sosUrlComboBox;
	private JLabel configFileJL;
	private JButton configFileDirSelectorJB;
	private JTextField configFileNameJT;
	private String configFileName = Constants.XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX;
	private String configFilePath = "";
	/*
	 * OFFERING
	 */
	private JPanel offeringPanel;
	private JCheckBox offeringGenerateCheckbox;
	private JTextField offeringInputTextField;
	private JLabel offeringInputLabel;
	private JPanel sosURLPanel;
	private JLabel configFileNameLabel;
	private JLabel configFileSelectedFolderLabel;
	private JPanel panel;
	private JLabel spacer;
	private JLabel sosURLInstructionsLabel;
	private JLabel sosVersionInstructionsLabel;
	private JPanel sosVersionPanel;
	private JTextField sosVersionTextField;
	private JLabel sosBindingLabel;
	private JTextField sosBindingTextField;
	private JPanel sosBindingPanel;
	private JPanel configFilePanel;

	public Step7Panel() {
		setBorder(new TitledBorder(null, "Sensor Observation Service", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{750, 0};
		gridBagLayout.rowHeights = new int[]{80, 0, 0, 43, 74, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		initSosUrlPanel();
		initOfferingPanel();
		initConfigFilePanel();
		initSosVersionPanel();
		initSosBindingPanel();
		
		final GridBagConstraints gbc_sosURLPanel = new GridBagConstraints();
		gbc_sosURLPanel.fill = GridBagConstraints.BOTH;
		gbc_sosURLPanel.insets = new Insets(0, 0, 5, 5);
		gbc_sosURLPanel.gridx = 0;
		gbc_sosURLPanel.gridy = 0;
		add(sosURLPanel, gbc_sosURLPanel);
		
		final GridBagConstraints gbc_configFilePanel = new GridBagConstraints();
		gbc_configFilePanel.insets = new Insets(0, 0, 5, 5);
		gbc_configFilePanel.fill = GridBagConstraints.BOTH;
		gbc_configFilePanel.gridx = 0;
		gbc_configFilePanel.gridy = 1;
		add(configFilePanel, gbc_configFilePanel);
		
		final GridBagConstraints gbc_offeringPanel = new GridBagConstraints();
		gbc_offeringPanel.fill = GridBagConstraints.BOTH;
		gbc_offeringPanel.insets = new Insets(0, 0, 5, 5);
		gbc_offeringPanel.gridx = 0;
		gbc_offeringPanel.gridy = 2;
		add(offeringPanel, gbc_offeringPanel);
		
		final GridBagConstraints gbc_versionPanel = new GridBagConstraints();
		gbc_versionPanel.fill = GridBagConstraints.BOTH;
		gbc_versionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_versionPanel.gridx = 0;
		gbc_versionPanel.gridy = 3;
		add(sosVersionPanel, gbc_versionPanel);
		
		final GridBagConstraints gbc_bindingPanel = new GridBagConstraints();
		gbc_bindingPanel.fill = GridBagConstraints.BOTH;
		gbc_bindingPanel.insets = new Insets(0, 0, 5, 5);
		gbc_bindingPanel.gridx = 0;
		gbc_bindingPanel.gridy = 4;
		add(sosBindingPanel, gbc_bindingPanel);
	}

	private void initConfigFilePanel() {
		String tmp = MainController.getInstance().getFilename();
		if (tmp == null) {
			tmp = "not-set.";
		}
		tmp = tmp + configFileName;
		configFilePanel = new JPanel();
		configFilePanel.setBorder(new TitledBorder(null, Lang.l().step7ConfigurationFile(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		final GridBagLayout gbl_configFilePanel = new GridBagLayout();
		gbl_configFilePanel.columnWidths = new int[] {50, 50, 0};
		gbl_configFilePanel.rowHeights = new int[]{23, 0, 0};
		gbl_configFilePanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_configFilePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		configFilePanel.setLayout(gbl_configFilePanel);
		configFileJL = new JLabel(Lang.l().step7ConfigFileLabel() + ":");
		final GridBagConstraints gbc_configFileJL = new GridBagConstraints();
		gbc_configFileJL.anchor = GridBagConstraints.EAST;
		gbc_configFileJL.insets = new Insets(0, 0, 5, 5);
		gbc_configFileJL.gridx = 0;
		gbc_configFileJL.gridy = 0;
		configFilePanel.add(configFileJL, gbc_configFileJL);

		panel = new JPanel();
		final GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		configFilePanel.add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		configFileDirSelectorJB = new JButton(Lang.l().step7ConfigFileButton());
		panel.add(configFileDirSelectorJB);

		spacer = new JLabel(" ");
		panel.add(spacer);

		configFileSelectedFolderLabel = new JLabel();
		panel.add(configFileSelectedFolderLabel);
		configFileDirSelectorJB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showDialog(Step7Panel.this, Lang.l().step7ConfigFileDialogTitel())
						== JFileChooser.APPROVE_OPTION){
					if (fc.getSelectedFile().isDirectory() && fc.getSelectedFile().canWrite()) {
						configFilePath  = fc.getSelectedFile().getAbsolutePath();
						configFileSelectedFolderLabel.setText(configFilePath + File.separatorChar);
						BackNextController.getInstance().setNextButtonEnabled(true);
						return;
					} else {
						JOptionPane.showMessageDialog(Step7Panel.this, 
								Lang.l().step7ConfigDirNotDirOrWriteable(
										fc.getSelectedFile().getAbsolutePath()), 
										Lang.l().errorDialogTitle(), 
										JOptionPane.ERROR_MESSAGE);
					}
				}
				BackNextController.getInstance().setNextButtonEnabled(false);
			}
		});
		
		configFileNameJT = new JTextField(50);
		configFileNameJT.setText(tmp);

		configFileNameLabel = new JLabel(new StringBuffer().append(Lang.l().name().substring(0,1).toUpperCase())
				.append(Lang.l().name().substring(1)).
				append(":").toString());
		configFileNameLabel.setLabelFor(configFileNameJT);
		final GridBagConstraints gbc_configFileNameLabel = new GridBagConstraints();
		gbc_configFileNameLabel.anchor = GridBagConstraints.EAST;
		gbc_configFileNameLabel.insets = new Insets(0, 0, 0, 5);
		gbc_configFileNameLabel.gridx = 0;
		gbc_configFileNameLabel.gridy = 1;
		configFilePanel.add(configFileNameLabel, gbc_configFileNameLabel);
		final GridBagConstraints gbc_configFileNameJT = new GridBagConstraints();
		gbc_configFileNameJT.anchor = GridBagConstraints.WEST;
		gbc_configFileNameJT.gridx = 1;
		gbc_configFileNameJT.gridy = 1;
		configFilePanel.add(configFileNameJT, gbc_configFileNameJT);
	}

	private void initOfferingPanel() {
		final JLabel offeringLabel = new JLabel(Lang.l().step7OfferingCheckBoxLabel());
		
		offeringInputLabel = new JLabel(Lang.l().step7OfferingInputTextfieldLabel());
		offeringInputLabel.setVisible(false);

		offeringGenerateCheckbox = new JCheckBox();
		offeringGenerateCheckbox.setSelected(true);
		offeringGenerateCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!offeringGenerateCheckbox.isSelected()) {
					offeringInputTextField.setVisible(true);
					offeringInputLabel.setVisible(true);
				} else {
					offeringInputTextField.setVisible(false);
					offeringInputLabel.setVisible(false);
				}
			}
		});
		
		offeringInputTextField = new JTextField();
		offeringInputTextField.setColumns(10);
		offeringInputTextField.setVisible(false);
		
		offeringPanel = new JPanel();
		offeringPanel.setBorder(new TitledBorder(null, Lang.l().offering(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		offeringPanel.setToolTipText(ToolTips.get(ToolTips.OFFERING));
		offeringPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		offeringPanel.add(offeringLabel);
		offeringPanel.add(offeringGenerateCheckbox);
		offeringPanel.add(offeringInputLabel);
		offeringPanel.add(offeringInputTextField);
	}

	private void initSosUrlPanel() {
		sosURLPanel = new JPanel();
		sosURLPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Lang.l().url(), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		final GridBagLayout gbl_sosURLPanel = new GridBagLayout();
		gbl_sosURLPanel.columnWidths = new int[]{750, 0};
		gbl_sosURLPanel.rowHeights = new int[]{30, 0, 0};
		gbl_sosURLPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_sosURLPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		sosURLPanel.setLayout(gbl_sosURLPanel);
		sosUrlComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), Lang.l().url(), ToolTips.get(ToolTips.SOS));
		sosUrlComboBox.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		final GridBagConstraints gbc_sosComboBox = new GridBagConstraints();
		gbc_sosComboBox.fill = GridBagConstraints.BOTH;
		gbc_sosComboBox.gridx = 0;
		gbc_sosComboBox.gridy = 1;
		sosURLPanel.add(sosUrlComboBox, gbc_sosComboBox);
		sosURLInstructionsLabel = new JLabel("  " + Lang.l().step7SosUrlInstructions());
		final GridBagConstraints gbc_sosURLInstructionsLabel = new GridBagConstraints();
		gbc_sosURLInstructionsLabel.gridy = 0;
		gbc_sosURLInstructionsLabel.gridx = 0;
		gbc_sosURLInstructionsLabel.fill = GridBagConstraints.BOTH;
		sosURLPanel.add(sosURLInstructionsLabel, gbc_sosURLInstructionsLabel);
	}
	
	private void initSosVersionPanel() {
		sosVersionPanel = new JPanel();
		sosVersionPanel.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"),
						Lang.l().specificationVersion(),
						TitledBorder.LEADING,
						TitledBorder.TOP,
						null, 
						new Color(0, 0, 0)));
		
		final GridBagLayout gbl_sosURLPanel = new GridBagLayout();
		gbl_sosURLPanel.columnWidths = new int[]{750, 0};
		gbl_sosURLPanel.rowHeights = new int[]{30, 0, 0};
		gbl_sosURLPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_sosURLPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		sosVersionPanel.setLayout(gbl_sosURLPanel);
		
		sosVersionInstructionsLabel = new JLabel("  " + Lang.l().step7SosVersionInstructions());
		
		final GridBagConstraints gbc_sosURLInstructionsLabel = new GridBagConstraints();
		gbc_sosURLInstructionsLabel.insets = new Insets(0, 0, 5, 0);
		gbc_sosURLInstructionsLabel.gridy = 0;
		gbc_sosURLInstructionsLabel.gridx = 0;
		gbc_sosURLInstructionsLabel.fill = GridBagConstraints.BOTH;
		sosVersionPanel.add(sosVersionInstructionsLabel, gbc_sosURLInstructionsLabel);
		
		sosVersionTextField = new JTextField();
		sosVersionTextField.setColumns(10);
		sosVersionTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e)
			{
				if (sosVersionTextField.getText().equalsIgnoreCase("2.0.0"))
				{
					sosBindingPanel.setVisible(true);
				}
				else
				{
					sosBindingPanel.setVisible(false);
					sosBindingTextField.setText("");
				}
			}
		});
		final GridBagConstraints gbc_sosVersionTextField = new GridBagConstraints();
		gbc_sosVersionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sosVersionTextField.gridx = 0;
		gbc_sosVersionTextField.gridy = 1;
		sosVersionPanel.add(sosVersionTextField, gbc_sosVersionTextField);
	}
	
	private void initSosBindingPanel() {
		sosBindingPanel = new JPanel();
		sosBindingPanel.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"),
						Lang.l().binding(), 
						TitledBorder.LEADING, 
						TitledBorder.TOP, 
						null, 
						new Color(0, 0, 0)));
		
		final GridBagLayout gbl_sosBindingPanel = new GridBagLayout();
		gbl_sosBindingPanel.columnWidths = new int[]{750, 0};
		gbl_sosBindingPanel.rowHeights = new int[]{20, 0, 0};
		gbl_sosBindingPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_sosBindingPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		sosBindingPanel.setLayout(gbl_sosBindingPanel);
		
		sosBindingLabel = new JLabel("  " + Lang.l().step7SosBindingInstructions());
		
		final GridBagConstraints gbc_sosBindingLabel = new GridBagConstraints();
		gbc_sosBindingLabel.insets = new Insets(0, 0, 5, 0);
		gbc_sosBindingLabel.gridx = 0;
		gbc_sosBindingLabel.gridy = 0;
		gbc_sosBindingLabel.fill = GridBagConstraints.BOTH;
		sosBindingPanel.add(sosBindingLabel, gbc_sosBindingLabel);
		
		sosBindingTextField = new JTextField();
		sosBindingTextField.setColumns(10);
		final GridBagConstraints gbc_sosBindingTextField = new GridBagConstraints();
		gbc_sosBindingTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sosBindingTextField.gridx = 0;
		gbc_sosBindingTextField.gridy = 1;
		sosBindingPanel.add(sosBindingTextField, gbc_sosBindingTextField);
		sosBindingPanel.setVisible(false);
	}

	public String getSOSURL() {
		return (String) sosUrlComboBox.getSelectedItem();
	}

	public void setSOSURL(final String sosURL) {
		sosUrlComboBox.setSelectedItem(sosURL);
	}

	public String getConfigFile() {
		return configFilePath + File.separatorChar + configFileNameJT.getText();
	}

	public void setConfigFile(final String configFile) {
		// split String by last index of File.separatorChar and save to fields
		configFilePath = configFile.substring(0, configFile.lastIndexOf(File.separatorChar)+1);
		configFileName = configFile.substring(configFile.lastIndexOf(File.separatorChar)+1);
	}

	public boolean isGenerateOfferingFromSensorName() {
		return offeringGenerateCheckbox.isSelected();
	}

	public String getOfferingName() {
		return offeringInputTextField.getText();
	}

	public String getSosBinding()
	{
		return sosBindingTextField.getText();
	}
	
	public void setBinding(final String binding)
	{
		sosBindingTextField.setText(binding);
	}
	
	public String getSosVersion()
	{
		return sosVersionTextField.getText();
	}
	
	public void setVersion(final String version)
	{
		sosVersionTextField.setText(version);
	}
}


