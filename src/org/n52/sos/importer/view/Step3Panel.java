package org.n52.sos.importer.view;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.ButtonGroupPanel;
import org.n52.sos.importer.ParseTestLabel;
import org.n52.sos.importer.Parser;
import org.n52.sos.importer.SelectionPanel;
import org.n52.sos.importer.controller.TableController;

public class Step3Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel selectionModeLabel = new JLabel();
	
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
		
		JPanel selectionModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectionModePanel.add(selectionModeLabel);
		this.add(selectionModePanel);
		
	    this.add(tablePanel);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(rootPanel);
		buttonPanel.add(additionalPanel1);
		buttonPanel.add(additionalPanel2);
		this.add(buttonPanel);
	}

	public void setSelectionModeLabelText(String text) {
		selectionModeLabel.setText(text);
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
				addRadioButton("Count", new ParseTestPanel(new CountParser()));
				addRadioButton("Boolean", new ParseTestPanel(new BooleanParser()));
				addRadioButton("Text");
			}
			
			private class CountParser implements Parser {
				
				@Override
				public Object parse(String s) {
					s = s.trim();
					int i = Integer.parseInt(s);
					if (i < 0) throw new NumberFormatException();
					return i;
				}
			}
			
			private class BooleanParser implements Parser {
				
				@Override
				public Object parse(String s) {
					s = s.trim();
					if (s.equals("0") || s.equals("false") || s.equals("False")) return false;
					else if (s.equals("1") || s.equals("true") || s.equals("true")) return true;
					else throw new NumberFormatException();
				}
			}
			
			private class NumericValuePanel extends SelectionPanel {

				private static final long serialVersionUID = 1L;
				private final String[] decimalSeparatorValues = { ".", "," };
				private final String[] thousandsSeparatorValues = { ",", ".", "'", " " };
				
				private final JLabel decimalSeparatorLabel = new JLabel("Decimal separator: ");
				private final JLabel thousandsSeparatorLabel = new JLabel("Thousands separator: ");
				private final JLabel exampleLabel = new JLabel("Example: ");
			
				private final JComboBox decimalSeparatorCombobox = new JComboBox(decimalSeparatorValues);
				private final JComboBox thousandsSeparatorCombobox = new JComboBox(thousandsSeparatorValues);
				private final JLabel exampleNumberLabel = new JLabel();
				
				private final ParseTestLabel parseTestLabel = new ParseTestLabel(new NumericValueParser());
			
				public NumericValuePanel() {
					super(additionalPanel2);
					ActionListener sc = new SeparatorChanged();
					decimalSeparatorCombobox.addActionListener(sc);
					thousandsSeparatorCombobox.addActionListener(sc);
					
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
					reformat();
				}

				@Override
				protected void setSelection(String s) {
					String[] separators = s.split("|");
					decimalSeparatorCombobox.setSelectedItem(separators[0]);
					thousandsSeparatorCombobox.setSelectedItem(separators[1]);	
					selectionChanged();
				}

				@Override
				protected String getSelection() {
					String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
					String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();			
					return decimalSeparator+"|"+thousandsSeparator;
				}

				@Override
				public void setDefaultSelection() {
					setSelection(decimalSeparatorValues[0]+"|"+thousandsSeparatorValues[0]);			
				}
				
				@Override
				protected void selectionChanged() {
					parseTestLabel.parseValues(TableController.getInstance().getSelectedValues());
					reformat();
				};
				
				@Override
				protected void reinit() {
					parseTestLabel.parseValues(TableController.getInstance().getSelectedValues());
					//reformat();
				}
				
				private void reformat() {
					String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
					String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
					
					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setDecimalSeparator(decimalSeparator.charAt(0));
					symbols.setGroupingSeparator(thousandsSeparator.charAt(0));
					
					try {
						DecimalFormat formatter = new DecimalFormat();
						formatter.setDecimalFormatSymbols(symbols);
						String n = formatter.format(1234567.89);
						exampleNumberLabel.setText(n);
						exampleNumberLabel.setForeground(Color.black);
			        } catch (IllegalArgumentException iae) {
			        	System.out.println(iae);
			        	exampleNumberLabel.setForeground(Color.red);
			        	exampleNumberLabel.setText("Error: " + iae.getMessage());
			        }
				}
				
				private class SeparatorChanged implements ActionListener {

					@Override
					public void actionPerformed(ActionEvent e) {
						selectionChanged();
					}		
				}
				
				private class NumericValueParser implements Parser {
					
					@Override
					public Object parse(String s) {
						String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
						String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
						
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						symbols.setDecimalSeparator(decimalSeparator.charAt(0));
						symbols.setGroupingSeparator(thousandsSeparator.charAt(0));
						
						Number n;
						try {
							DecimalFormat formatter = new DecimalFormat();
							formatter.setDecimalFormatSymbols(symbols);
							n = formatter.parse(s);
				        } catch (ParseException e) {
					        throw new NumberFormatException();
						}					
						
						return n.doubleValue();
					}
				}			
			}	
			
			private class ParseTestPanel extends SelectionPanel {

				private static final long serialVersionUID = 1L;
				
				private final ParseTestLabel parseTestLabel;
				
				public ParseTestPanel(Parser parser) {
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
					parseTestLabel.parseValues(TableController.getInstance().getSelectedValues());
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

				private final JLabel formatLabel = new JLabel("Format: ");
		        private final JLabel exampleLabel = new JLabel("Example: ");
			
		        String[] patternExamples = {
		                 "dd MMMMM yyyy",
		                 "dd.MM.yy",
		                 "MM/dd/yy",
		                 "yyyy.MM.dd G 'at' hh:mm:ss z",
		                 "EEE, MMM d, ''yy",
		                 "h:mm a",
		                 "H:mm:ss:SSS",
		                 "K:mm a,z",
		                 "yyyy.MMMMM.dd GGG hh:mm aaa"
		                 };
		        
		        private final JComboBox dateAndTimeComboBox = new JComboBox(patternExamples);
		        
		        private final JLabel exampleDateTime = new JLabel("");
		        
		        private final ParseTestLabel parseTestLabel = new ParseTestLabel(new DateAndTimeParser());
				
				public DateAndTimeCombinationPanel() {	
					super(additionalPanel2);	
					dateAndTimeComboBox.setEditable(true);		
					
					this.setLayout(new FlowLayout(FlowLayout.LEFT));
					
					JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					formatPanel.add(formatLabel);
					formatPanel.add(dateAndTimeComboBox);
										
					JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					examplePanel.add(exampleLabel);
					examplePanel.add(exampleDateTime);
					
					JPanel dateTimePanel = new JPanel();
					dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.PAGE_AXIS));
					dateTimePanel.add(formatPanel);	
					dateTimePanel.add(examplePanel);	
					this.add(dateTimePanel);
					this.add(parseTestLabel);
					dateAndTimeComboBox.addActionListener(new FormatChanged());
					reformat();
				}
				
			    /** Formats and displays today's date. */
			    public void reformat() {
			    	String currentPattern = getSelection();
			        Date today = new Date();		        
			        try {
			        	SimpleDateFormat formatter =
					           new SimpleDateFormat(currentPattern);      	
			            String dateString = formatter.format(today);
			            exampleDateTime.setForeground(Color.black);
			            exampleDateTime.setText(dateString);
			        } catch (IllegalArgumentException iae) {
			        	exampleDateTime.setForeground(Color.red);
			        	exampleDateTime.setText("Error: " + iae.getMessage());
			        }
			    }

			    @Override
				public void setSelection(String s) {
					dateAndTimeComboBox.setSelectedItem(s);
				}
				
				@Override
				public void setDefaultSelection() {
					dateAndTimeComboBox.setSelectedIndex(0);
				}
				
				@Override
				public String getSelection() {
					return (String)dateAndTimeComboBox.getSelectedItem();
				}
				
				private class FormatChanged implements ActionListener {
					
					public void actionPerformed(ActionEvent e) {
						//TODO remove and add selection to the list when enter pressed
				        //String newSelection = getSelection();
				        selectionChanged();
				    }
				}
			    
			    @Override
			    protected void selectionChanged() {
			    	reformat();
			    	//parseTestLabel.parseValues(tablePanel.getSelectedValues());
			    }
			    
			    @Override
			    protected void reinit() {
			    	reformat();
			    	parseTestLabel.parseValues(TableController.getInstance().getSelectedValues());
			    };
			    
			    private class DateAndTimeParser implements Parser {

					@Override
					public Object parse(String s) {
						Date dateTime = null;
						String currentPattern = getSelection();
						SimpleDateFormat formatter =
					           new SimpleDateFormat(currentPattern);      	
			            try {
			            	dateTime = formatter.parse(s);
						} catch (ParseException e) {
							throw new NumberFormatException();
						}
						return dateTime;
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
