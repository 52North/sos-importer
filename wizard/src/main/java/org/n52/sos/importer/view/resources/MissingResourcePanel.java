/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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

/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
package org.n52.sos.importer.view.resources;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ArrayListTransferHandler;
import org.n52.sos.importer.view.utils.ToolTips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissingResourcePanel extends MissingComponentPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MissingResourcePanel.class);

    private EditableJComboBoxPanel manualResNameComboBox;
    private EditableJComboBoxPanel manualResUriComboBox;

    private JRadioButton manualResInputJRB;
    private JRadioButton generatedResJRB;

    private JPanel manualResPanel;
    private JPanel generatedResPanel;

    private final Resource resource;
    private JTextField columnConcationationString;
    private JTextField uriOrPrefixTextField;
    private JList<String> columnList;
    private JCheckBox useNameAfterPrefixCheckBox;

    /**
     * <p>Constructor for MissingResourcePanel.</p>
     *
     * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
     */
    public MissingResourcePanel(final Resource resource) {
        this.resource = resource;
        String name = Lang.l().name();
        final ModelStore ms = ModelStore.getInstance();
        boolean disableManualInput = false;
        boolean disableGeneratedInput = false;
        boolean manualDefault = false;
        final String[] columnHeadingsWithId = TableController.getInstance().getUsedColumnHeadingsWithId();
        final JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        containerPanel.add(initGeneratedResPanel(columnHeadingsWithId));
        containerPanel.add(initManualResPanel(name));

        add(initRadioButtonPanel(), BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        if (resource.getName() == null && !resource.isGenerated()) {
            if (resource instanceof FeatureOfInterest) {
                final List<Position> positions = ms.getPositions();
                if (positions != null && positions.size() > 0) {
                    disableManualInput = true;
                }
            } else if (resource instanceof ObservedProperty) {
                manualDefault = true;
            } else if (resource instanceof UnitOfMeasurement) {
                final List<ObservedProperty> ops = ms.getObservedProperties();
                if (ops != null && ops.size() == 1) {
                    /*
                     * in the case of having one observed property column
                     *  -> set generation as default (having table element of type Column set)
                     *
                     * in the case of having one generated observed property -> set generation as default
                     */
                    final ObservedProperty op = ops.get(0);
                    /*
                     * disableManualInput = true;
                     * generated is default!
                     * in the case of having one manual observed property
                     * -> disable generated (having no table element and generated set to false)
                     */
                    if (op.getTableElement() == null && !op.isGenerated()) {
                        disableGeneratedInput = true;
                    }
                    // in the case of having no observed property -> can this happen?
                }
                // TODO what happens in the case of having multiple observed properties. How to relate each other?
            } else if (resource instanceof Sensor) {
                // check for 1 foi - 1 op => 1 sensor => only manual input
                final List<FeatureOfInterest> fois = ms.getFeatureOfInterests();
                final List<ObservedProperty> ops = ms.getObservedProperties();
                if (fois != null && fois.size() == 1 && ops != null && ops.size() == 1) {
                    final FeatureOfInterest foi = fois.get(0);
                    final ObservedProperty op = ops.get(0);
                    if (!foi.isGenerated() &&
                            foi.getTableElement() == null
                            && !op.isGenerated() &&
                            op.getTableElement() == null) {
                        disableGeneratedInput = true;
                    }
                }
                // else -> generated as default and manual as optional
            }
            // Case: Back button hit -> set values from resource
        } else {
            if (resource.isGenerated()) {
                manualDefault = false;
                // URI
                useNameAfterPrefixCheckBox.setSelected(resource.isUseNameAfterPrefixAsURI());
                uriOrPrefixTextField.setText(resource.getUriPrefix());
                // Name
                columnConcationationString.setText(resource.getConcatString());
                columnList.setSelectedIndices(columnIdsToModelIndices(resource.getRelatedCols()));
            } else {
                manualDefault = true;
                // URI
                manualResUriComboBox.setSelectedItem(resource.getURI().toString());
                // Name
                manualResNameComboBox.setSelectedItem(resource.getName());
            }
        }

        // Default Settings
        if (manualDefault) {
            manualResPanel.setVisible(true);
            manualResInputJRB.setSelected(true);
            generatedResPanel.setVisible(false);
        } else {
            manualResPanel.setVisible(false);
            generatedResPanel.setVisible(true);
            generatedResJRB.setSelected(true);
        }
        // specific settings
        if (disableManualInput) {
            generatedResPanel.setVisible(true);
            generatedResJRB.setSelected(true);
            manualResPanel.setVisible(false);
            manualResInputJRB.setSelected(false);
            manualResInputJRB.setVisible(false);
        }
        if (disableGeneratedInput) {
            manualResPanel.setVisible(true);
            manualResInputJRB.setSelected(true);
            generatedResPanel.setVisible(false);
            generatedResJRB.setSelected(false);
            generatedResJRB.setVisible(false);
        }
        if (Constants.isGuiDebug()) {
            setBorder(Constants.DEBUG_BORDER);
            manualResPanel.setBorder(Constants.DEBUG_BORDER);
            generatedResPanel.setBorder(Constants.DEBUG_BORDER);
        }
        revalidate();
    }

    private JPanel initGeneratedResPanel(final String[] columnHeadingsWithId) {
        generatedResPanel = new JPanel();
        final GridBagLayout gbl_generatedResPanel = new GridBagLayout();
        gbl_generatedResPanel.columnWidths = new int[]{293, 242, 0};
        gbl_generatedResPanel.rowHeights = new int[]{275, 0};
        gbl_generatedResPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_generatedResPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        generatedResPanel.setLayout(gbl_generatedResPanel);

        final GridBagConstraints gbc_generatedNamePanel = new GridBagConstraints();
        gbc_generatedNamePanel.fill = GridBagConstraints.BOTH;
        gbc_generatedNamePanel.insets = new Insets(0, 0, 0, 5);
        gbc_generatedNamePanel.gridx = 0;
        gbc_generatedNamePanel.gridy = 0;
        generatedResPanel.add(initGeneratedNamePanel(columnHeadingsWithId), gbc_generatedNamePanel);

        // build panel
        final GridBagConstraints gbc_generatedResURIPanel = new GridBagConstraints();
        gbc_generatedResURIPanel.fill = GridBagConstraints.BOTH;
        gbc_generatedResURIPanel.gridx = 1;
        gbc_generatedResURIPanel.gridy = 0;
        generatedResPanel.add(initGeneratedResURIPanel(), gbc_generatedResURIPanel);
        return generatedResPanel;
    }

    private JPanel initGeneratedResURIPanel() {
        final GridBagLayout gbl_generatedResURIPanel = new GridBagLayout();
        gbl_generatedResURIPanel.columnWidths = new int[]{242, 0};
        gbl_generatedResURIPanel.rowHeights = new int[]{29, 20, 23, 20, 0};
        gbl_generatedResURIPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_generatedResURIPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        final JPanel generatedResURIPanel = new JPanel();
        generatedResURIPanel.setBorder(new TitledBorder(null,
                Lang.l().uri(),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                null));
        generatedResURIPanel.setToolTipText(ToolTips.get(ToolTips.URI));
        generatedResURIPanel.setLayout(gbl_generatedResURIPanel);

        final JTextArea uriInstructions = new JTextArea(Lang.l().step6bURIInstructions());
        uriInstructions.setEditable(false);
        uriInstructions.setFocusable(false);
        uriInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        uriInstructions.setFont(Constants.DEFAULT_LABEL_FONT);
        uriInstructions.setLineWrap(true);
        uriInstructions.setWrapStyleWord(true);
        final GridBagConstraints gbc_uriInstructions = new GridBagConstraints();
        gbc_uriInstructions.fill = GridBagConstraints.BOTH;
        gbc_uriInstructions.insets = new Insets(0, 0, 5, 0);
        gbc_uriInstructions.gridx = 0;
        gbc_uriInstructions.gridy = 0;
        generatedResURIPanel.add(uriInstructions, gbc_uriInstructions);

        useNameAfterPrefixCheckBox = new JCheckBox(Lang.l().step6bUseNameAfterPrefix());
        final GridBagConstraints gbc_useNameAfterPrefixCheckBox = new GridBagConstraints();
        gbc_useNameAfterPrefixCheckBox.anchor = GridBagConstraints.NORTHWEST;
        gbc_useNameAfterPrefixCheckBox.insets = new Insets(0, 0, 5, 0);
        gbc_useNameAfterPrefixCheckBox.gridx = 0;
        gbc_useNameAfterPrefixCheckBox.gridy = 1;
        generatedResURIPanel.add(useNameAfterPrefixCheckBox, gbc_useNameAfterPrefixCheckBox);

        uriOrPrefixTextField = new JTextField();
        uriOrPrefixTextField.setColumns(10);
        final GridBagConstraints gbc_uriOrPrefixTextField = new GridBagConstraints();
        gbc_uriOrPrefixTextField.insets = new Insets(0, 0, 5, 0);
        gbc_uriOrPrefixTextField.anchor = GridBagConstraints.NORTH;
        gbc_uriOrPrefixTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_uriOrPrefixTextField.gridx = 0;
        gbc_uriOrPrefixTextField.gridy = 2;
        generatedResURIPanel.add(uriOrPrefixTextField, gbc_uriOrPrefixTextField);
        return generatedResURIPanel;
    }

    private JPanel initGeneratedNamePanel(final String[] columnHeadingsWithId) {
        final JPanel generatedNamePanel = new JPanel();
        generatedNamePanel.setToolTipText(ToolTips.get(ToolTips.NAME));
        generatedNamePanel.setBorder(new TitledBorder(null,
                Lang.l().name(),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                null));
        generatedNamePanel.setFont(Constants.DEFAULT_LABEL_FONT);

        final GridBagLayout gbl_generatedNamePanel = new GridBagLayout();
        gbl_generatedNamePanel.columnWidths = new int[]{300, 0};
        gbl_generatedNamePanel.rowHeights = new int[]{30, 50, 35, 20, 0};
        gbl_generatedNamePanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_generatedNamePanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};

        generatedNamePanel.setLayout(gbl_generatedNamePanel);

        final JTextArea nameInstructions = new JTextArea(Lang.l().step6bSelectColumnsLabel());
        nameInstructions.setEditable(false);
        nameInstructions.setFocusable(false);
        nameInstructions.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        nameInstructions.setFont(Constants.DEFAULT_LABEL_FONT);
        nameInstructions.setLineWrap(true);
        nameInstructions.setWrapStyleWord(true);
        final GridBagConstraints gbc_nameInstructions = new GridBagConstraints();
        gbc_nameInstructions.fill = GridBagConstraints.BOTH;
        gbc_nameInstructions.insets = new Insets(0, 0, 5, 0);
        gbc_nameInstructions.gridx = 0;
        gbc_nameInstructions.gridy = 0;
        generatedNamePanel.add(nameInstructions, gbc_nameInstructions);

        columnList = new JList<>(toListModel(columnHeadingsWithId));
        //
        columnList.setDragEnabled(true);
        columnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        columnList.setTransferHandler(new ArrayListTransferHandler());
        //
        final JScrollPane listView = new JScrollPane(columnList);
        final GridBagConstraints gbc_listView = new GridBagConstraints();
        gbc_listView.anchor = GridBagConstraints.NORTH;
        gbc_listView.fill = GridBagConstraints.HORIZONTAL;
        gbc_listView.insets = new Insets(0, 0, 5, 0);
        gbc_listView.gridx = 0;
        gbc_listView.gridy = 1;
        generatedNamePanel.add(listView, gbc_listView);

        final JTextArea concatLabel = new JTextArea(Lang.l().step6bDefineConcatString());
        concatLabel.setEditable(false);
        concatLabel.setFocusable(false);
        concatLabel.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        concatLabel.setFont(Constants.DEFAULT_LABEL_FONT);
        concatLabel.setLineWrap(true);
        concatLabel.setWrapStyleWord(true);
        final GridBagConstraints gbc_concatLabel = new GridBagConstraints();
        gbc_concatLabel.anchor = GridBagConstraints.NORTH;
        gbc_concatLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_concatLabel.insets = new Insets(0, 0, 5, 0);
        gbc_concatLabel.gridx = 0;
        gbc_concatLabel.gridy = 2;
        generatedNamePanel.add(concatLabel, gbc_concatLabel);

        columnConcationationString = new JTextField();
        columnConcationationString.setColumns(10);
        final GridBagConstraints gbc_columnConcationationString = new GridBagConstraints();
        gbc_columnConcationationString.anchor = GridBagConstraints.NORTH;
        gbc_columnConcationationString.fill = GridBagConstraints.HORIZONTAL;
        gbc_columnConcationationString.gridx = 0;
        gbc_columnConcationationString.gridy = 3;
        generatedNamePanel.add(columnConcationationString, gbc_columnConcationationString);
        return generatedNamePanel;
    }

    private JPanel initManualResPanel(final String name) {
        manualResNameComboBox = new EditableJComboBoxPanel(resource.getNames(),
                name,
                ToolTips.get(ToolTips.NAME));
        manualResUriComboBox = new EditableJComboBoxPanel(resource.getURIs(),
                Lang.l().uri(),
                ToolTips.get(ToolTips.URI));
        manualResUriComboBox.setPartnerComboBox(manualResNameComboBox);
        manualResNameComboBox.setPartnerComboBox(manualResUriComboBox);
        manualResPanel = new JPanel();
        manualResPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        manualResPanel.add(manualResNameComboBox);
        manualResPanel.add(manualResUriComboBox);

        return manualResPanel;
    }

    private JPanel initRadioButtonPanel() {
        final ButtonGroup bGroup = new ButtonGroup();

        final JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        generatedResJRB = new JRadioButton(Lang.l().step6Generation());
        generatedResJRB.setSelected(true);
        radioButtonPanel.add(generatedResJRB);
        generatedResJRB.addActionListener(radioButtionActionListener());
        bGroup.add(generatedResJRB);

        manualResInputJRB = new JRadioButton(Lang.l().step6ManualInput());
        radioButtonPanel.add(manualResInputJRB);
        manualResInputJRB.addActionListener(radioButtionActionListener());
        setLayout(new BorderLayout(0, 0));

        bGroup.add(manualResInputJRB);
        return radioButtonPanel;
    }

    private int[] columnIdsToModelIndices(final TableElement[] relatedCols) {
        final int[] indices = new int[relatedCols.length];
        for (int i = 0; i < relatedCols.length; i++) {
            indices[i] = ((Column) relatedCols[i]).getNumber();
        }
        return indices;
    }

    private ListModel<String> toListModel(final String[] columnHeadingsWithId) {
        if (logger.isTraceEnabled()) {
            logger.trace("toListModel()");
        }
        final DefaultListModel<String> listModel = new DefaultListModel<>();
        for (final String columnHeading : columnHeadingsWithId) {
            listModel.addElement(columnHeading);
        }
        return listModel;
    }

    private ActionListener radioButtionActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // show additional gui elements if checkbox is selected
                if (manualResInputJRB.isSelected()) {
                    manualResPanel.setVisible(true);
                    generatedResPanel.setVisible(false);
                } else {
                    manualResPanel.setVisible(false);
                    generatedResPanel.setVisible(true);
                }
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public boolean checkValues() {
        // manual or generate selected
        if (generatedResJRB.isSelected()) {
            /*
             * GENERATED
             *
             * Check:
             * - Name -> column selection
             * - URI -> uriOrPrefix
             */
            final List<String> selectedColumns = columnList.getSelectedValuesList();
            final String uriOrPrefix = uriOrPrefixTextField.getText();
            final String concatString = columnConcationationString.getText();

            if (noUserInputAtAll(selectedColumns, uriOrPrefix, concatString)) {
                showNoInputAtAllDialog();
                return false;
            } else if (validUserInput(selectedColumns, uriOrPrefix)) {
                return true;
            } else {
                showMissingInputDialog();
                return false;
            }
        } else if (manualResInputJRB.isSelected()) {
            /*
             * MANUAL
             *
             * Check:
             * - name
             * - URI
             */
            final String name = manualResNameComboBox.getSelectedItem().toString().trim();
            final String uri = manualResUriComboBox.getSelectedItem().toString().trim();

            if (!isUriValid(uri)) {
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

    private boolean validUserInput(final List<String> selectedColumns,
            final String uriOrPrefix) {
        return uriOrPrefix != null &&
                !uriOrPrefix.isEmpty() &&
                isUriValid(uriOrPrefix) &&
                isAtLeastOneColumnSelected(selectedColumns);
    }

    private boolean noUserInputAtAll(final List<String> selectedColumns,
            final String uriOrPrefix,
            final String concatString) {
        return selectedColumns.size() < 1 &&
                (uriOrPrefix == null || uriOrPrefix.equalsIgnoreCase("")) &&
                (concatString == null || concatString.equalsIgnoreCase(""));
    }

    private boolean isAtLeastOneColumnSelected(final List<String> selectedColumns) {
        if (selectedColumns == null || selectedColumns.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isUriValid(final String uri) {
        if (logger.isTraceEnabled()) {
            logger.trace("isUriValid()");
        }
        //check syntax of URI
        try {
            new URI(uri);
            return true;
        } catch (final URISyntaxException e) {
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
                Lang.l().step6NoUserInput(),
                Lang.l().infoDialogTitle(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMissingInputDialog() {
        if (logger.isTraceEnabled()) {
            logger.trace("showMissingInputDialog()");
        }
        JOptionPane.showMessageDialog(null,
                Lang.l().step6MissingUserInput(),
                Lang.l().infoDialogTitle(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInvalidURISyntaxDialog(final String uri) {
        if (logger.isTraceEnabled()) {
            logger.trace("showInvalidURISyntaxDialog()");
        }
        JOptionPane.showMessageDialog(null,
                Lang.l().uriSyntaxNotValidDialogMessage(uri),
                Lang.l().warningDialogTitle(),
                JOptionPane.WARNING_MESSAGE);
    }

    /** {@inheritDoc} */
    @Override
    public void assignValues() {
        URI uri = null;
        String name = null;
        /*
         * MANUAL
         */
        if (manualResInputJRB.isSelected()) {
            name = (String) manualResNameComboBox.getSelectedItem();
            name = name.trim();
            uri = toURI((String) manualResUriComboBox.getSelectedItem());

        } else if (generatedResJRB.isSelected()) {
            /*
             * GENERATED
             */
            String uriPrefix = uriOrPrefixTextField.getText();
            uriPrefix = uriPrefix.trim();
            if (!useNameAfterPrefixCheckBox.isSelected()) {
                uri = toURI(uriPrefix);
            }
            resource.setUseNameAfterPrefixAsURI(useNameAfterPrefixCheckBox.isSelected());
            resource.setUriPrefix(uriPrefix);
            resource.setConcatString(columnConcationationString.getText());
            // get TableElements
            final Column[] relatedCols =
                    getColumnsFromSelection(columnList.getSelectedValuesList());
            resource.setRelatedCols(relatedCols);
        }
        resource.setGenerated(!manualResInputJRB.isSelected() && generatedResJRB.isSelected());
        resource.setName(name);
        if (uri != null) {
            resource.setURI(uri);
        }
    }

    private Column[] getColumnsFromSelection(final List<String> list) {
        if (logger.isTraceEnabled()) {
            logger.trace("getColumnsFromSelection()");
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        final int fLWD = TableController.getInstance().getFirstLineWithData();
        final ArrayList<Column> result = new ArrayList<Column>(list.size());
        for (final Object obj : list) {
            if (obj instanceof String) {
                final String s = (String) obj;
                final int index = Integer.parseInt(s.substring(0, s.indexOf(":")));
                final Column c = new Column(index, fLWD);
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
        try {
            return new URI(uri.trim());
        } catch (final URISyntaxException e) {
            logger.error("Given URI syntax not valid: '" + uri + "'", e);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void unassignValues() {
        /*
         * MANUAL
         */
        resource.setName(null);
        resource.setURI(null);
        /*
         * GENERATED
         */
        resource.setConcatString(null);
        resource.setRelatedCols(null);
        resource.setUriPrefix(null);
        resource.setUseNameAfterPrefixAsURI(false);
    }

    /** {@inheritDoc} */
    @Override
    public Component getMissingComponent() {
        return resource;
    }

    /** {@inheritDoc} */
    @Override
    public void setMissingComponent(final Component c) {
        final Resource r = (Resource) c;
        final String name = r.getName();
        if (name != null) {
            manualResNameComboBox.setSelectedItem(name);
        }
    }
}
