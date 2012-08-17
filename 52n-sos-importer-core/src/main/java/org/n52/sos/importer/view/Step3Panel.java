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
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.measuredValue.Text;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.step3.DateAndTimeCombinationPanel;
import org.n52.sos.importer.view.step3.MeasuredValueSelectionPanel;
import org.n52.sos.importer.view.step3.PositionCombinationPanel;
import org.n52.sos.importer.view.step3.RadioButtonPanel;
import org.n52.sos.importer.view.step3.ResourceSelectionPanel;
import org.n52.sos.importer.view.step3.SelectionPanel;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * consists of the table and a radio button panel for 
 * different types of metadata
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step3Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * This panel holds the first column with radio buttons for each column
	 * type, or Do-not-export
	 */
	private JPanel columnTypeJP;
	private JPanel columnSubTypeJP;
	private JPanel columnSubTypeMetadataJP;
	private TablePanel tablePanel;
	private final SelectionPanel columnTypeRadioButtonPanel;
	
	public Step3Panel(int firstLineWithData) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		/*
		 * 	Table preview
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
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBorder(new TitledBorder(null, Lang.l().column().substring(0,1).toUpperCase() + Lang.l().column().substring(1) + " " + Lang.l().metadata(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPanel.add(columnTypeJP);
		buttonPanel.add(columnSubTypeJP);
		buttonPanel.add(columnSubTypeMetadataJP);
		
		add(tablePanel);
		add(buttonPanel);
	}
	
	public void clearAdditionalPanels() {
		columnSubTypeJP.removeAll();
		columnSubTypeMetadataJP.removeAll();
	}
	
	/**
	 * Get the last child panel of <b><code>this</code></b> {@link org.n52.sos.importer.view.step3.SelectionPanel}
	 * The last one should contain the user input.
	 * @return
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
	 * @param selection list of all selected items, e.g. column type and the corresponding meta data
	 */
	public void store(List<String> selection) {
		columnTypeRadioButtonPanel.store(selection);
	}
	
	public void restore(List<String> selection) {
		columnTypeRadioButtonPanel.restore(selection);
	}
	
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
		public ColumnTypePanel(int firstLineWithData) {	
			super(columnTypeJP);
			addRadioButton(Lang.l().step3ColTypeDoNotExport());	
			addRadioButton(Lang.l().step3ColTypeMeasuredValue(), ToolTips.get(ToolTips.MEASURED_VALUE), new MeasuredValuePanel(firstLineWithData));
			addRadioButton(Lang.l().step3ColTypeDateTime(), ToolTips.get(ToolTips.DATE_AND_TIME), new DateAndTimePanel(firstLineWithData));
			addRadioButton(Lang.l().position(), ToolTips.get(ToolTips.POSITION), new PositionPanel(firstLineWithData));
			addRadioButton(Lang.l().featureOfInterest(), ToolTips.get(ToolTips.FEATURE_OF_INTEREST), new ResourceSelectionPanel(columnSubTypeJP, new FeatureOfInterest()));
			addRadioButton(Lang.l().observedProperty(), ToolTips.get(ToolTips.OBSERVED_PROPERTY), new ResourceSelectionPanel(columnSubTypeJP, new ObservedProperty()));
			addRadioButton(Lang.l().unitOfMeasurement(), ToolTips.get(ToolTips.UNIT_OF_MEASUREMENT), new ResourceSelectionPanel(columnSubTypeJP, new UnitOfMeasurement()));
			addRadioButton(Lang.l().sensor(), ToolTips.get(ToolTips.SENSOR), new ResourceSelectionPanel(columnSubTypeJP, new Sensor()));			
		}

		private class MeasuredValuePanel extends RadioButtonPanel {

			private static final long serialVersionUID = 1L;
			
			/**
			 * JPanel for the definition of the measure value type
			 * @param firstLineWithData required for the test parsing results
			 */
			public MeasuredValuePanel(int firstLineWithData) {	
				super(columnSubTypeJP);		
				addRadioButton(Lang.l().step3MeasuredValNumericValue(), ToolTips.get(ToolTips.NUMERIC_VALUE), new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new NumericValue(), firstLineWithData));
// TODO uncomment next lines when observation types are supported by OXF
//				addRadioButton(Lang.l().step3MeasuredValCount(), ToolTips.get(ToolTips.COUNT), new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Count(),firstLineWithData));
//				addRadioButton(Lang.l().step3MeasuredValBoolean(), ToolTips.get(ToolTips.BOOLEAN), new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Boolean(),firstLineWithData));
// TODO update Lang.l().step3MeasuredValText() if TEXT observations are available				
				addRadioButton(Lang.l().step3MeasuredValText(), ToolTips.get(ToolTips.TEXT), new MeasuredValueSelectionPanel(columnSubTypeMetadataJP, new Text(),firstLineWithData));
			}	
		}
		
		private class DateAndTimePanel extends RadioButtonPanel {

			private static final long serialVersionUID = 1L;
			
			/**
			 * JPanel for the definition of the date time type
			 * @param firstLineWithData required for the test parsing results
			 */
			public DateAndTimePanel(int firstLineWithData) {
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
			public PositionPanel(int firstLineWithData) {
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
