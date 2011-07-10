package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.ButtonGroupPanel;
import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.ExampleFormatLabel;
import org.n52.sos.importer.ParseTestLabel;
import org.n52.sos.importer.Parseable;
import org.n52.sos.importer.SelectionPanel;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.Boolean;
import org.n52.sos.importer.model.measuredValue.Count;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.measuredValue.Text;

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
				addRadioButton("Numeric Value" , new NumericValuePanel());
				addRadioButton("Count", new ParseTestPanel(new Count()));
				addRadioButton("Boolean", new ParseTestPanel(new Boolean()));
				addRadioButton("Text", new ParseTestPanel(new Text()));
			}
			
			private class NumericValuePanel extends SelectionPanel {

				private static final long serialVersionUID = 1L;
				
				private final NumericValue numericValue = new NumericValue();
				private final double exampleValue = 1234567.89;
				
				private final JLabel decimalSeparatorLabel = new JLabel("Decimal separator: ");
				private final JLabel thousandsSeparatorLabel = new JLabel("Thousands separator: ");
				private final JLabel exampleLabel = new JLabel("Example: ");
			
				private final String[] decimalSeparators = Settings.getInstance().getDecimalSeparators();
				private final String[] thousandsSeparators = Settings.getInstance().getThousandsSeparators();
				
				private final JComboBox decimalSeparatorCombobox = new JComboBox(decimalSeparators);
				private final JComboBox thousandsSeparatorCombobox = new JComboBox(thousandsSeparators);
				
				private final ParseTestLabel parseTestLabel = new ParseTestLabel(numericValue);
				private final ExampleFormatLabel exampleNumberLabel = new ExampleFormatLabel(numericValue);
			
				public NumericValuePanel() {
					super(additionalPanel2);
					setDefaultSelection();
					decimalSeparatorCombobox.addActionListener(new DecimalSeparatorChanged());
					thousandsSeparatorCombobox.addActionListener(new ThousandsSeparatorChanged());
					
					this.setLayout(new FlowLayout(FlowLayout.LEFT));
					JPanel separatorPanel = new JPanel();
					separatorPanel.setLayout(new GridLayout(3,2));
					separatorPanel.add(decimalSeparatorLabel);
					separatorPanel.add(decimalSeparatorCombobox);
					separatorPanel.add(thousandsSeparatorLabel);
					separatorPanel.add(thousandsSeparatorCombobox);
					separatorPanel.add(exampleLabel);
					separatorPanel.add(exampleNumberLabel);
					this.add(separatorPanel);
					this.add(parseTestLabel);
				}

				@Override
				protected void setSelection(String s) {
					String[] separators = s.split(":");
					decimalSeparatorCombobox.setSelectedItem(separators[0]);
					thousandsSeparatorCombobox.setSelectedItem(separators[1]);
					selectionChanged();
				}

				@Override
				protected String getSelection() {
					String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
					String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();			
					return decimalSeparator+":"+thousandsSeparator;
				}

				@Override
				public void setDefaultSelection() {
					setSelection(decimalSeparators[0]+":"+thousandsSeparators[0]);	
				}
				
				@Override
				protected void selectionChanged() {	
					String[] separators = getSelection().split(":");
					numericValue.setDecimalSeparator(separators[0]);
					numericValue.setThousandsSeparator(separators[1]);
					List<String> values = TableController.getInstance().getMarkedValues();				
					parseTestLabel.parseValues(values);
					exampleNumberLabel.reformat(exampleValue);
				};
				
				@Override
				protected void reinit() {
					parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
					exampleNumberLabel.reformat(exampleValue);
				}
				
				private class DecimalSeparatorChanged implements ActionListener {

					@Override
					public void actionPerformed(ActionEvent e) {
						String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
						String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
						
						if (thousandsSeparator.equals(",") && decimalSeparator.equals(","))
							thousandsSeparatorCombobox.setSelectedItem(".");			
						if (thousandsSeparator.equals(".") && decimalSeparator.equals("."))
							thousandsSeparatorCombobox.setSelectedItem(",");
						
						selectionChanged();
					}		
				}
				
				private class ThousandsSeparatorChanged implements ActionListener {

					@Override
					public void actionPerformed(ActionEvent e) {
						String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
						String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
						
						if (thousandsSeparator.equals(",") && decimalSeparator.equals(","))
							decimalSeparatorCombobox.setSelectedItem(".");			
						if (thousandsSeparator.equals(".") && decimalSeparator.equals("."))
							decimalSeparatorCombobox.setSelectedItem(",");
						
						selectionChanged();
					}		
				}
						
			}	
			
			private class ParseTestPanel extends SelectionPanel {

				private static final long serialVersionUID = 1L;
				
				private final ParseTestLabel parseTestLabel;
				
				public ParseTestPanel(Parseable parser) {
					super(additionalPanel2);
					parseTestLabel = new ParseTestLabel(parser);
					this.setLayout(new FlowLayout(FlowLayout.LEFT));
					this.add(parseTestLabel);
				}

				@Override
				protected void setSelection(String s) {				
				}

				@Override
				protected String getSelection() {
					return "0";
				}

				@Override
				public void setDefaultSelection() {	
				}
				
				@Override
				protected void reinit() {
					parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
				}		
			}		
		}
		
		private class DateAndTimePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public DateAndTimePanel() {
				super(additionalPanel1);
				addRadioButton("Combination", new DateAndTimeCombinationPanel());
				addRadioButton("Timezone", new TimeZonePanel());
				addRadioButton("UNIX time");
			}

			private class TimeZonePanel extends ButtonGroupPanel {

				private static final long serialVersionUID = 1L;
				
				public TimeZonePanel() {
					super(additionalPanel2);	
					super.addRadioButton("Country or City Name");
					super.addRadioButton("UTC Offset");		       
				}
			}
			
			private class DateAndTimeCombinationPanel extends SelectionPanel {
				//source: 	http://download.oracle.com/javase/tutorial/uiswing/
				// 			examples/components/ComboBoxDemo2Project/src/components/
				// 			ComboBoxDemo2.java
				private static final long serialVersionUID = 1L;

				private final DateAndTime dateAndTime = new DateAndTime();
				
		        private final JLabel exampleLabel = new JLabel("Example: ");
			
		        private final DefaultComboBoxModel dateAndTimePatterns = EditableComboBoxItems.getInstance().getDateAndTimePatterns();
		        private final EditableJComboBoxPanel dateAndTimeComboBox = new EditableJComboBoxPanel(dateAndTimePatterns, "Format");
		        
		        private final ParseTestLabel parseTestLabel = new ParseTestLabel(dateAndTime);
		        private final ExampleFormatLabel exampleFormatLabel = new ExampleFormatLabel(dateAndTime);
				
				public DateAndTimeCombinationPanel() {	
					super(additionalPanel2);
					setDefaultSelection();
					dateAndTimeComboBox.addActionListener(new FormatChanged());
					
					this.setLayout(new FlowLayout(FlowLayout.LEFT));
					
					JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					formatPanel.add(dateAndTimeComboBox);
										
					JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					examplePanel.add(exampleLabel);
					examplePanel.add(exampleFormatLabel);
					
					JPanel dateTimePanel = new JPanel();
					dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.PAGE_AXIS));
					dateTimePanel.add(formatPanel);	
					dateTimePanel.add(examplePanel);	
					this.add(dateTimePanel);
					this.add(parseTestLabel);
				}

			    @Override
				public void setSelection(String s) {
					dateAndTimeComboBox.setSelectedItem(s);
			    	selectionChanged();
				}
				
				@Override
				public void setDefaultSelection() {
					setSelection((String)dateAndTimePatterns.getElementAt(0));
				}
				
				@Override
				public String getSelection() {
					return (String)dateAndTimeComboBox.getSelectedItem();
				}
			    
			    @Override
			    protected void selectionChanged() {
			    	dateAndTime.setPattern(getSelection());
			    	parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
			    	exampleFormatLabel.reformat(new Date());
			    }
			    
			    @Override
			    protected void reinit() {
			    	parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
			    	exampleFormatLabel.reformat(new Date());
			    };
			  	    
				private class FormatChanged implements ActionListener {
					
					public void actionPerformed(ActionEvent e) {
				        selectionChanged();
				    }
				}
			}
		}

		private class PositionPanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public PositionPanel() {
				super(additionalPanel1);	
				addRadioButton("Longitude / X");
				addRadioButton("Latitude / Y");
				addRadioButton("Altitude / Z");
				addRadioButton("Reference System", new ReferenceSystemPanel());
				addRadioButton("Combination");
			}
			
			private class ReferenceSystemPanel extends ButtonGroupPanel {

				private static final long serialVersionUID = 1L;
				
				public ReferenceSystemPanel() {
					super(additionalPanel2);				
					addRadioButton("Name");
					addRadioButton("EPSG-Code");
				}
			}	
		}	
	}	
	
	
}
