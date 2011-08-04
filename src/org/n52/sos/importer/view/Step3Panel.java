package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.n52.sos.importer.model.measuredValue.Boolean;
import org.n52.sos.importer.model.measuredValue.Count;
import org.n52.sos.importer.model.measuredValue.Text;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.tooltips.ToolTips;
import org.n52.sos.importer.view.step3.DateAndTimeCombinationPanel;
import org.n52.sos.importer.view.step3.NumericValuePanel;
import org.n52.sos.importer.view.step3.MeasuredValueSelectionPanel;
import org.n52.sos.importer.view.step3.PositionCombinationPanel;
import org.n52.sos.importer.view.step3.RadioButtonPanel;
import org.n52.sos.importer.view.step3.ResourceSelectionPanel;
import org.n52.sos.importer.view.step3.SelectionPanel;

public class Step3Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel rootPanel = new JPanel();
	private JPanel additionalPanel1 = new JPanel();
	private JPanel additionalPanel2 = new JPanel();
	private TablePanel tablePanel = TablePanel.getInstance();
	private final SelectionPanel radioButtonPanel;
	
	public Step3Panel() {
		super();
		radioButtonPanel = new RootPanel();	
		radioButtonPanel.getContainerPanel().add(radioButtonPanel);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
	    this.add(tablePanel);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(rootPanel);
		buttonPanel.add(additionalPanel1);
		buttonPanel.add(additionalPanel2);
		this.add(buttonPanel);
	}
	
	public void clearAdditionalPanels() {
		additionalPanel1.removeAll();
		additionalPanel2.removeAll();
	}
	
	public SelectionPanel getLastChildPanel() {
		SelectionPanel lastChildPanel = radioButtonPanel;
		SelectionPanel nextPanel = radioButtonPanel.getSelectedChildPanel();
		while (nextPanel != null) {
			lastChildPanel = nextPanel;
			nextPanel = nextPanel.getSelectedChildPanel();
		}
		return lastChildPanel;
	}
	
	public void store(List<String> selection) {
		radioButtonPanel.store(selection);
	}
	
	public void restore(List<String> selection) {
		radioButtonPanel.restore(selection);
	}
	
	public void restoreDefault() {
		radioButtonPanel.restoreDefault();
	}
	
	private class RootPanel extends RadioButtonPanel {
		
		private static final long serialVersionUID = 1L;
		
		public RootPanel() {	
			super(rootPanel);
			addRadioButton("Undefined");
			addRadioButton("Measured Value", ToolTips.get("MeasuredValue"), new MeasuredValuePanel());
			addRadioButton("Date & Time", ToolTips.get("DateAndTime"), new DateAndTimePanel());
			addRadioButton("Position", ToolTips.get("Position"), new PositionPanel());
			addRadioButton("Feature of Interest", ToolTips.get("FeatureOfInterest"), new ResourceSelectionPanel(additionalPanel1, new FeatureOfInterest()));
			addRadioButton("Observed Property", ToolTips.get("ObservedProperty"), new ResourceSelectionPanel(additionalPanel1, new ObservedProperty()));
			addRadioButton("Unit of Measurement", ToolTips.get("UnitOfMeasurement"), new ResourceSelectionPanel(additionalPanel1, new UnitOfMeasurement()));
			addRadioButton("Sensor", ToolTips.get("Sensor"), new ResourceSelectionPanel(additionalPanel1, new Sensor()));
			addRadioButton("Do not export");				
		}

		private class MeasuredValuePanel extends RadioButtonPanel {

			private static final long serialVersionUID = 1L;
			
			public MeasuredValuePanel() {	
				super(additionalPanel1);		
				addRadioButton("Numeric Value", ToolTips.get("NumericValue"), new NumericValuePanel(additionalPanel2));
				addRadioButton("Count", ToolTips.get("Count"), new MeasuredValueSelectionPanel(additionalPanel2, new Count()));
				addRadioButton("Boolean", ToolTips.get("Boolean"), new MeasuredValueSelectionPanel(additionalPanel2, new Boolean()));
				addRadioButton("Text", ToolTips.get("Text"), new MeasuredValueSelectionPanel(additionalPanel2, new Text()));
			}	
		}
		
		private class DateAndTimePanel extends RadioButtonPanel {

			private static final long serialVersionUID = 1L;
			
			public DateAndTimePanel() {
				super(additionalPanel1);
				addRadioButton("Combination", null, new DateAndTimeCombinationPanel(additionalPanel2));
				addRadioButton("UNIX time");
			}	
		}

		private class PositionPanel extends RadioButtonPanel {

			private static final long serialVersionUID = 1L;
			
			public PositionPanel() {
				super(additionalPanel1);	
				addRadioButton("Combination", null, new PositionCombinationPanel(additionalPanel2));
			}
		}	
	}		
}
