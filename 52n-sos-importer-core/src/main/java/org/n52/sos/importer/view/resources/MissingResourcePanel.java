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
package org.n52.sos.importer.view.resources;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ArrayListTransferHandler;
import org.n52.sos.importer.view.utils.Constants;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * consists of a combobox for the name and a combobox for the URI;
 * both are linked with each other
 * @author Raimund
 *
 */
public class MissingResourcePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MissingResourcePanel.class);

	private final EditableJComboBoxPanel nameComboBox; 
	private final EditableJComboBoxPanel uriComboBox; 

	private JRadioButton manualInputJRB;
	private JRadioButton automaticGenerationJRB;

	private JPanel manualPanel;
	private JPanel automaticPanel;

	private Resource resource;
	private JTextField columnConcationationString;
	private JTextField uriOrPrefixTextField;
	private JList columnList;
	private JCheckBox useNameAfterPrefixCheckBox;

	public MissingResourcePanel(Resource resource) {
		this.resource = resource;
		ButtonGroup bGroup = new ButtonGroup();
		/*
		 * 	MANUAL INPUT
		 */
		String name = Lang.l().name();
		// 
		if (resource instanceof UnitOfMeasurement) {
			name = Lang.l().code();
		}
		/*
		 * 
		 * 	AUTOMATIC GENERATION
		 * 
		 */
		TableController tc = TableController.getInstance();
		JLabel nameJL = new JLabel(Lang.l().name() + ":");
		JPanel autoNamePanel = new JPanel();
		String[] columnHeadingsWithId = tc.getUsedColumnHeadingsWithId();
		columnList = new JList(toListModel(columnHeadingsWithId));
		JScrollPane listView;
		ArrayListTransferHandler aLTH = new ArrayListTransferHandler();
		//
		columnList.setDragEnabled(true);
		columnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		columnList.setTransferHandler(aLTH);
		//
		listView = new JScrollPane(columnList);
		
		JTextArea nameInstructions = new JTextArea(Lang.l().step6bSelectColumnsLabel());
		nameInstructions.setEditable(false);
		nameInstructions.setFocusable(false);
		nameInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		nameInstructions.setFont(Constants.DEFAULT_LABEL_FONT);
		nameInstructions.setLineWrap(true);
		nameInstructions.setWrapStyleWord(true);
		
		// build components
		JLabel uriJL = new JLabel(Lang.l().uri() + ":");
		// build panel
		JPanel autoURIPanel = new JPanel();
		
		JTextArea uriInstructions = new JTextArea(Lang.l().step6bURIInstructions());
		uriInstructions.setEditable(false);
		uriInstructions.setFocusable(false);
		uriInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		uriInstructions.setFont(Constants.DEFAULT_LABEL_FONT);
		uriInstructions.setLineWrap(true);
		uriInstructions.setWrapStyleWord(true);
		//

		automaticPanel = new JPanel();
		automaticPanel.setLayout(new BoxLayout(automaticPanel, BoxLayout.LINE_AXIS));
		automaticPanel.add(autoNamePanel);
		
		JTextArea concatLabel = new JTextArea(Lang.l().step6bDefineConcatString());
		concatLabel.setEditable(false);
		concatLabel.setFocusable(false);
		concatLabel.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
		concatLabel.setFont(Constants.DEFAULT_LABEL_FONT);
		concatLabel.setLineWrap(true);
		concatLabel.setWrapStyleWord(true);
		
		columnConcationationString = new JTextField();
		columnConcationationString.setColumns(10);
		
		GroupLayout gl_autoNamePanel = new GroupLayout(autoNamePanel);
		gl_autoNamePanel.setHorizontalGroup(
			gl_autoNamePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_autoNamePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_autoNamePanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(columnConcationationString, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
						.addComponent(concatLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_autoNamePanel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(listView, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
							.addComponent(nameJL, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(nameInstructions, Alignment.LEADING)))
					.addContainerGap())
		);
		gl_autoNamePanel.setVerticalGroup(
			gl_autoNamePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_autoNamePanel.createSequentialGroup()
					.addComponent(nameJL)
					.addGap(1)
					.addComponent(nameInstructions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(listView, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(concatLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(columnConcationationString, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		autoNamePanel.setLayout(gl_autoNamePanel);
		automaticPanel.add(autoURIPanel);
		
		uriOrPrefixTextField = new JTextField();
		uriOrPrefixTextField.setColumns(10);
		
		useNameAfterPrefixCheckBox = new JCheckBox(Lang.l().step6bUseNameAfterPrefix());
		GroupLayout gl_autoURIPanel = new GroupLayout(autoURIPanel);
		gl_autoURIPanel.setHorizontalGroup(
			gl_autoURIPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(uriInstructions, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
				.addComponent(useNameAfterPrefixCheckBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
				.addComponent(uriOrPrefixTextField, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
				.addComponent(uriJL, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
		);
		gl_autoURIPanel.setVerticalGroup(
			gl_autoURIPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_autoURIPanel.createSequentialGroup()
					.addComponent(uriJL)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uriInstructions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(useNameAfterPrefixCheckBox)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uriOrPrefixTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(157, Short.MAX_VALUE))
		);
		autoURIPanel.setLayout(gl_autoURIPanel);
		automaticPanel.setVisible(false);
		/*
		 * 	FINAL GUI BUILDING
		 */
		if (logger.isDebugEnabled() && Constants.GUI_DEBUG) {
			this.setBorder(Constants.DEBUG_BORDER);
			manualPanel.setBorder(Constants.DEBUG_BORDER);
			automaticPanel.setBorder(Constants.DEBUG_BORDER);
		}

		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.PAGE_AXIS));

		manualInputJRB = new JRadioButton(Lang.l().step6ManualInput());
		radioButtonPanel.add(manualInputJRB);
		manualInputJRB.addActionListener(this.radioButtionActionListener());

		bGroup.add(manualInputJRB);

		automaticGenerationJRB = new JRadioButton(Lang.l().step6AutomaticGeneration());
		radioButtonPanel.add(automaticGenerationJRB);
		automaticGenerationJRB.addActionListener(this.radioButtionActionListener());
		bGroup.add(automaticGenerationJRB);

		JPanel containerPanel = new JPanel();
		FlowLayout conPanelLayout = (FlowLayout) containerPanel.getLayout();
		conPanelLayout.setAlignment(FlowLayout.LEADING);
		containerPanel.add(automaticPanel);
		nameComboBox = new EditableJComboBoxPanel(resource.getNames(),
				name,
				ToolTips.get(ToolTips.NAME));
		uriComboBox = new EditableJComboBoxPanel(resource.getURIs(),
				Lang.l().uri(),
				ToolTips.get(ToolTips.URI));
		uriComboBox.setPartnerComboBox(nameComboBox);
		nameComboBox.setPartnerComboBox(uriComboBox);

		manualPanel = new JPanel();
		manualPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		manualPanel.add(nameComboBox);
		manualPanel.add(uriComboBox);
		manualPanel.setVisible(false);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(4)
					.addComponent(radioButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(containerPanel, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
						.addComponent(manualPanel, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(manualPanel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(containerPanel, GroupLayout.PREFERRED_SIZE, 361, GroupLayout.PREFERRED_SIZE))
						.addComponent(radioButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(48))
		);
		setLayout(groupLayout);
	}

	private ListModel toListModel(String[] columnHeadingsWithId) {
		if (logger.isTraceEnabled()) {
			logger.trace("toListModel()");
		}
		DefaultListModel listModel = new DefaultListModel();
		for (String string : columnHeadingsWithId) {
			listModel.addElement(string);
		}
		return listModel;
	}

	private ActionListener radioButtionActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// show additional gui elements if checkbox is selected
				if (manualInputJRB.isSelected()) {
					manualPanel.setVisible(true);
					automaticPanel.setVisible(false);
				} else {
					manualPanel.setVisible(false);
					automaticPanel.setVisible(true);
				}
			}
		};
	}

	@Override
	// FIXME check new implementation
	public boolean checkValues() {
		// manual or auto generate selected
		if (automaticGenerationJRB.isSelected()) {
			/*
			 * AUTOMATIC
			 * 
			 * Check:
			 * - column selection
			 * - URI
			 */
			Object[] selectedColumns = columnList.getSelectedValues();
			String uriOrPrefix = uriOrPrefixTextField.getText();
			String concatString = columnConcationationString.getText();
			// if no user input was given
			if (selectedColumns.length < 1 && 
					(uriOrPrefix == null || uriOrPrefix.equalsIgnoreCase("")) &&
					(concatString == null || concatString.equalsIgnoreCase(""))) {
				showNoInputAtAllDialog();
				return false;
			} else {
				// check URI validity at least for the prefix
				if(!isUriValid(uriOrPrefix)) {
					return false;
				}
				return true;
			}
		} else if (manualInputJRB.isSelected()) {
			/*
			 * MANUAL
			 * 
			 * Check:
			 * - name
			 * - URI
			 */
			String name = nameComboBox.getSelectedItem().toString().trim();
			String uri = uriComboBox.getSelectedItem().toString().trim();

			if(!isUriValid(uri)) {
				return false;
			}

			if (name.equals("") && uri.equals("")) {
				showNoInputAtAllDialog();
				return false;
			}

			return true;
		} else {
			showNoInputAtAllDialog();
			return false;
		}
	}

	private boolean isUriValid(String uri) {
		if (logger.isTraceEnabled()) {
			logger.trace("isUriValid()");
		}
		//check syntax of URI
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			logger.error("URI syntax not valid: " + uri, e);
			showInvalidURISyntaxDialog(uri);
			return false;
		}
	}

	private void showNoInputAtAllDialog() {
		if (logger.isTraceEnabled()) {
			logger.trace("showNoInputAtAllDialog()");
		}
		JOptionPane.showMessageDialog(null,
				Lang.l().step6MissingUserInput(),
				Lang.l().infoDialogTitle(),
				JOptionPane.INFORMATION_MESSAGE);		
	}

	private void showInvalidURISyntaxDialog(String uri) {
		if (logger.isTraceEnabled()) {
			logger.trace("showInvalidURISyntaxDialog()");
		}
		JOptionPane.showMessageDialog(null,
				Lang.l().uriSyntaxNotValidDialogMessage(uri),
				Lang.l().warningDialogTitle(),
				JOptionPane.WARNING_MESSAGE);
	}

	@Override
	// FIXME check new possibilities
	public void assignValues() {
		URI uri = null;
		String name = null;
		/*
		 * MANUAL
		 */
		if (manualInputJRB.isSelected()) {
			name = (String) nameComboBox.getSelectedItem();
			name = name.trim();
			uri = toURI((String) uriComboBox.getSelectedItem());
			
		} else if (automaticGenerationJRB.isSelected()) {
			/*
			 * AUTOMATIC
			 */
			String uriPrefix = uriOrPrefixTextField.getText();
			uriPrefix = uriPrefix.trim();
			if (!useNameAfterPrefixCheckBox.isSelected()) {
				uri = toURI(uriPrefix);
			} 
			resource.setUriPrefix(uriPrefix);
			// get TableElements
			Column[] relatedCols = 
					getColumnsFromSelection(columnList.getSelectedValues());
			resource.setRelatedCols(relatedCols);
		}
		resource.setName(name);
		if (uri != null) {
			resource.setURI(uri);
		}
	}

	private Column[] getColumnsFromSelection(Object[] selectedValues) {
		if (logger.isTraceEnabled()) {
			logger.trace("getColumnsFromSelection()");
		}
		if (selectedValues == null | selectedValues.length < 1) {
			return null;
		}
		int fLWD = TableController.getInstance().getFirstLineWithData();
		ArrayList<Column> result = new ArrayList<Column>(selectedValues.length);
		for (Object obj : selectedValues) {
			if (obj instanceof String) {
				String s = (String) obj;
				int index = Integer.parseInt(s.substring(0,s.indexOf(":")));
				Column c = new Column(index, fLWD);
				result.add(c);
			}
		}
		result.trimToSize();
		return result.toArray(new Column[result.size()]);
	}

	private URI toURI(String uri) {
		if (logger.isTraceEnabled()) {
			logger.trace("toURI()");
		}
		uri = uri.trim();
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Given URI syntax not valid: " + uri, e);
		}	
		return null;
	}

	@Override
	// FIXME check new implementation
	public void unassignValues() {
		/*
		 * MANUAL
		 */
		resource.setName(null);
		resource.setURI(null);
		/*
		 * AUTOMATIC
		 */
		resource.setConcatString(null);
		resource.setRelatedCols(null);
		resource.setUriPrefix(null);
	}

	@Override
	public Component getMissingComponent() {
		return resource;
	}

	@Override
	public void setMissingComponent(Component c) {
		Resource r = (Resource) c;
		String name = r.getName();
		if (name != null)
			nameComboBox.setSelectedItem(name);
	}
}
