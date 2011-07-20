package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.n52.sos.importer.model.measuredValue.Boolean;
import org.n52.sos.importer.model.measuredValue.Count;
import org.n52.sos.importer.model.measuredValue.Text;
import org.n52.sos.importer.view.step3.ButtonGroupPanel;
import org.n52.sos.importer.view.step3.DateAndTimeCombinationPanel;
import org.n52.sos.importer.view.step3.NumericValuePanel;
import org.n52.sos.importer.view.step3.ParsingTestPanel;
import org.n52.sos.importer.view.step3.PositionCombinationPanel;
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
		radioButtonPanel = new RadioButtonPanel();	
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
	
	public void store(List<String> selection) {
		radioButtonPanel.store(selection);
	}
	
	public void restore(List<String> selection) {
		radioButtonPanel.restore(selection);
	}
	
	public void restoreDefault() {
		radioButtonPanel.restoreDefault();
	}
	
	private class RadioButtonPanel extends ButtonGroupPanel {
		
		private static final long serialVersionUID = 1L;
		
		public RadioButtonPanel() {	
			super(rootPanel);
			addRadioButton("Undefined");
			addRadioButton("Measured Value", new MeasuredValuePanel());
			addRadioButton("Date & Time", new DateAndTimePanel());
			addRadioButton("Position", new PositionPanel());
			addRadioButton("Feature of Interest");
			addRadioButton("Sensor Name");
			addRadioButton("Observed Property");
			addRadioButton("Unit of Measurement");
			addRadioButton("Combination");
			addRadioButton("Do not export");				
		}

		private class MeasuredValuePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public MeasuredValuePanel() {	
				super(additionalPanel1);		
				addRadioButton("Numeric Value" , new NumericValuePanel(additionalPanel2));
				addRadioButton("Count", new ParsingTestPanel(additionalPanel2, new Count()));
				addRadioButton("Boolean", new ParsingTestPanel(additionalPanel2, new Boolean()));
				addRadioButton("Text", new ParsingTestPanel(additionalPanel2, new Text()));
			}	
		}
		
		private class DateAndTimePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public DateAndTimePanel() {
				super(additionalPanel1);
				addRadioButton("Combination", new DateAndTimeCombinationPanel(additionalPanel2));
				addRadioButton("UNIX time");
			}	
		}

		private class PositionPanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public PositionPanel() {
				super(additionalPanel1);	
				addRadioButton("Combination", new PositionCombinationPanel(additionalPanel2));
			}
		}	
	}		
}
