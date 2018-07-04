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
package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.model.measuredValue.Boolean;
import org.n52.sos.importer.model.measuredValue.Count;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.measuredValue.Text;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.step3.DateAndTimeCombinationPanel;
import org.n52.sos.importer.view.step3.FeatureOfInterestPanel;
import org.n52.sos.importer.view.step3.MeasuredValueSelectionPanel;
import org.n52.sos.importer.view.step3.OmParameterSelectionPanel;
import org.n52.sos.importer.view.step3.PositionCombinationPanel;
import org.n52.sos.importer.view.step3.RadioButtonPanel;
import org.n52.sos.importer.view.step3.ResourceSelectionPanel;
import org.n52.sos.importer.view.step3.SelectionPanel;
import org.n52.sos.importer.wizard.utils.ToolTips;

/**
 * consists of the table and a radio button panel for
 * different types of metadata
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step3Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * This panel holds the first column with radio buttons for each column
     * type, or Do-not-export
     */
    private final JPanel columnTypeJP;
    private final JPanel columnSubTypeJP;
    private final JPanel columnSubTypeMetadataJP;
    private final TablePanel tablePanel;
    private final SelectionPanel columnTypeRadioButtonPanel;

    /**
     * <p>Constructor for Step3Panel.</p>
     *
     * @param firstLineWithData a int.
     */
    public Step3Panel(final int firstLineWithData) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        /*
         *  Table preview
         */
        tablePanel = TablePanel.getInstance();
        /*
         * Column Metadata panel
         */
        columnTypeJP = new JPanel();
        columnSubTypeJP = new JPanel();
        columnSubTypeMetadataJP = new JPanel();

        columnTypeRadioButtonPanel = new ColumnTypePanel(firstLineWithData);
        columnTypeRadioButtonPanel.getContainerPanel().add(columnTypeRadioButtonPanel);

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new TitledBorder(null,
                Lang.l().column().substring(0, 1).toUpperCase()
                + Lang.l().column().substring(1) + " "
                        + Lang.l().metadata(),
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        null,
                        null));
        buttonPanel.add(columnTypeJP);
        buttonPanel.add(columnSubTypeJP);
        buttonPanel.add(columnSubTypeMetadataJP);

        add(tablePanel);
        add(buttonPanel);
    }

    /**
     * <p>clearAdditionalPanels.</p>
     */
    public void clearAdditionalPanels() {
        columnSubTypeJP.removeAll();
        columnSubTypeMetadataJP.removeAll();
    }

    /**
     * Get the last child panel of <b><code>this</code></b> {@link org.n52.sos.importer.view.step3.SelectionPanel}
     * The last one should contain the user input.
     *
     * @return a {@link org.n52.sos.importer.view.step3.SelectionPanel} object.
     */
    public SelectionPanel getLastChildPanel() {
        SelectionPanel lastChildPanel = columnTypeRadioButtonPanel;
        SelectionPanel nextPanel = columnTypeRadioButtonPanel.getSelectedChildPanel();
        while (nextPanel != null) {
            lastChildPanel = nextPanel;
            nextPanel = nextPanel.getSelectedChildPanel();
        }
        return lastChildPanel;
    }

    /**
     * Stores the current selection in the {@linkplain org.n52.sos.importer.model.ModelStore}
     * instance.
     *
     * @param selection list of all selected items, e.g. column type and the corresponding meta data
     */
    public void store(final List<String> selection) {
        columnTypeRadioButtonPanel.store(selection);
    }

    /**
     * <p>restore.</p>
     *
     * @param selection a {@link java.util.List} object.
     */
    public void restore(final List<String> selection) {
        columnTypeRadioButtonPanel.restore(selection);
    }

    /**
     * <p>restoreDefault.</p>
     */
    public void restoreDefault() {
        columnTypeRadioButtonPanel.restoreDefault();
    }

    private class ColumnTypePanel extends RadioButtonPanel {

        private static final long serialVersionUID = 1L;

        /**
         * First panel presenting a column with radio buttons to define the
         * column type
         * @param firstLineWithData required for the test parsing results
         */
        ColumnTypePanel(final int firstLineWithData) {
            super(columnTypeJP);
            addRadioButton(Lang.l().step3ColTypeDoNotExport());
            addRadioButton(Lang.l().step3ColTypeMeasuredValue(),
                    ToolTips.get(ToolTips.MEASURED_VALUE),
                    new MeasuredValuePanel(firstLineWithData));
            addRadioButton(Lang.l().step3ColTypeDateTime(),
                    ToolTips.get(ToolTips.DATE_AND_TIME),
                    new DateAndTimePanel(firstLineWithData));
            addRadioButton(Lang.l().position(),
                    ToolTips.get(ToolTips.POSITION),
                    new PositionPanel(firstLineWithData));
            addRadioButton(Lang.l().featureOfInterest(),
                    ToolTips.get(ToolTips.FEATURE_OF_INTEREST),
                    new FeatureOfInterestPanel(columnSubTypeJP));
            addRadioButton(Lang.l().observedProperty(),
                    ToolTips.get(ToolTips.OBSERVED_PROPERTY),
                    new ResourceSelectionPanel(columnSubTypeJP, new ObservedProperty()));
            addRadioButton(Lang.l().step3ColTypeOmParameter(),
                    ToolTips.get(ToolTips.OM_PARAMETER),
                    new OmParameterPanel());
            addRadioButton(Lang.l().unitOfMeasurement(),
                    ToolTips.get(ToolTips.UNIT_OF_MEASUREMENT),
                    new ResourceSelectionPanel(columnSubTypeJP, new UnitOfMeasurement()));
            addRadioButton(Lang.l().sensor(),
                    ToolTips.get(ToolTips.SENSOR),
                    new ResourceSelectionPanel(columnSubTypeJP, new Sensor()));
        }

        private class OmParameterPanel extends RadioButtonPanel {

            private static final long serialVersionUID = 1L;

            /**
             * JPanel for the definition of the om:parameter type
             * TYPE->OM_PARAMETER: NUMERIC | COUNT | BOOLEAN | TEXT | CATEGORY
             */
            OmParameterPanel() {
                super(columnSubTypeJP);
                addRadioButton(Lang.l().step3MeasuredValNumericValue(),
                        ToolTips.get(ToolTips.NUMERIC_VALUE),
                        new OmParameterSelectionPanel(
                                columnSubTypeMetadataJP,
                                new OmParameter(Lang.l().step3ColTypeMeasuredValue())));
                addRadioButton(Lang.l().step3MeasuredValCount(),
                        ToolTips.get(ToolTips.COUNT),
                        new OmParameterSelectionPanel(
                                columnSubTypeMetadataJP,
                                new OmParameter(Lang.l().step3MeasuredValCount())));
                addRadioButton(Lang.l().step3MeasuredValBoolean(),
                        ToolTips.get(ToolTips.BOOLEAN),
                        new OmParameterSelectionPanel(
                                columnSubTypeMetadataJP,
                                new OmParameter(Lang.l().step3MeasuredValBoolean())));
                addRadioButton(Lang.l().step3MeasuredValText(),
                        ToolTips.get(ToolTips.TEXT),
                        new OmParameterSelectionPanel(
                                columnSubTypeMetadataJP,
                                new OmParameter(Lang.l().step3MeasuredValText())));
                addRadioButton(Lang.l().step3OmParameterCategory(),
                        ToolTips.get(ToolTips.OM_PARAMETER_CATEGORY),
                        new OmParameterSelectionPanel(
                                columnSubTypeMetadataJP,
                                new OmParameter(Lang.l().step3OmParameterCategory())));
            }
        }

        private class MeasuredValuePanel extends RadioButtonPanel {

            private static final long serialVersionUID = 1L;

            /**
             * JPanel for the definition of the measure value type
             * @param firstLineWithData required for the test parsing results
             */
            MeasuredValuePanel(final int firstLineWithData) {
                super(columnSubTypeJP);
                addRadioButton(Lang.l().step3MeasuredValNumericValue(),
                        ToolTips.get(ToolTips.NUMERIC_VALUE),
                        new MeasuredValueSelectionPanel(columnSubTypeMetadataJP,
                                new NumericValue(),
                                firstLineWithData));
                addRadioButton(Lang.l().step3MeasuredValCount(),
                        ToolTips.get(ToolTips.COUNT),
                        new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Count(), firstLineWithData));
                addRadioButton(Lang.l().step3MeasuredValBoolean(),
                        ToolTips.get(ToolTips.BOOLEAN),
                        new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Boolean(), firstLineWithData));
                addRadioButton(Lang.l().step3MeasuredValText(),
                        ToolTips.get(ToolTips.TEXT),
                        new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Text(), firstLineWithData));
            }
        }

        private class DateAndTimePanel extends RadioButtonPanel {

            private static final long serialVersionUID = 1L;

            /**
             * JPanel for the definition of the date time type
             * @param firstLineWithData required for the test parsing results
             */
            DateAndTimePanel(final int firstLineWithData) {
                super(columnSubTypeJP);
                addRadioButton(
                        Lang.l().step3DateAndTimeCombination(),
                        null,
                        new DateAndTimeCombinationPanel(
                                columnSubTypeMetadataJP,
                                firstLineWithData)
                );
                addRadioButton(Lang.l().step3DateAndTimeUnixTime());
            }
        }

        private class PositionPanel extends RadioButtonPanel {

            private static final long serialVersionUID = 1L;

            /**
             * JPanel for the definition of the position type
             * @param firstLineWithData required for the test parsing results
             */
            PositionPanel(final int firstLineWithData) {
                super(columnSubTypeJP);
                addRadioButton(
                        Lang.l().step3PositionCombination(),
                        null,
                        new PositionCombinationPanel(
                                columnSubTypeMetadataJP,
                                firstLineWithData));
            }
        }
    }

}
