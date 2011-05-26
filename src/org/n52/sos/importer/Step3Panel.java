package org.n52.sos.importer;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.bean.FeatureOfInterest;
import org.n52.sos.importer.bean.MeasuredValueColumn;
import org.n52.sos.importer.bean.ObservedProperty;
import org.n52.sos.importer.bean.SensorName;
import org.n52.sos.importer.bean.UnitOfMeasurement;

public class Step3Panel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel selectionModeLabel = new JLabel("Selection mode: ");
	
	private JPanel rootPanel = new JPanel();
	private JPanel additionalPanel1 = new JPanel();
	private JPanel additionalPanel2 = new JPanel();
	private TablePanel tablePanel;
	private final SelectionPanel radioButtonPanel;
	
	private final HashMap<Integer, List<String>> columnStore = new HashMap<Integer, List<String>>();
	private final HashMap<Integer, List<String>> rowStore = new HashMap<Integer, List<String>>();
	
	public Step3Panel(MainFrame mainFrame) {
		super(mainFrame);
		radioButtonPanel = new RadioButtonPanel(mainFrame);	
		radioButtonPanel.getContainerPanel().add(radioButtonPanel);
		this.tablePanel = getMainFrame().getTablePanel();
		
		changeSelectionMode(TablePanel.COLUMNS);
		tablePanel.addSelectionListener(new TableSelectionChanged());
		
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

	@Override
	protected String getDescription() {
		return "Step 3: Choose Metadata";
	}

	private void changeSelectionMode(int sm) {
		switch(sm) {
		case TablePanel.COLUMNS:
			tablePanel.setSelectionMode(TablePanel.COLUMNS);
			selectionModeLabel.setText("For Columns");
			break;
		case TablePanel.ROWS:
			tablePanel.setSelectionMode(TablePanel.ROWS);
			selectionModeLabel.setText("For Rows");
			break;
		case TablePanel.CELLS:
			tablePanel.setSelectionMode(TablePanel.CELLS);
			selectionModeLabel.setText("For Cells");
			break;
		}
	}
	
	@Override
	protected void back() {
		switch(tablePanel.getSelectionMode()) {
		case TablePanel.COLUMNS:
			getMainFrame().setStepPanel(getMainFrame().getStep2Panel());
			break;
		case TablePanel.ROWS:
			changeSelectionMode(TablePanel.COLUMNS);
			break;
		case TablePanel.CELLS:
			changeSelectionMode(TablePanel.ROWS);
			break;
		}			
	}

	@Override
	protected void next() {
		switch(tablePanel.getSelectionMode()) {
		case TablePanel.COLUMNS:
			changeSelectionMode(TablePanel.ROWS);
			break;
		case TablePanel.ROWS:
			changeSelectionMode(TablePanel.CELLS);
			break;
		case TablePanel.CELLS:
			List<MeasuredValueColumn> measuredValues = new ArrayList<MeasuredValueColumn>();
			List<FeatureOfInterest> featuresOfInterest = new ArrayList<FeatureOfInterest>();
			List<ObservedProperty> observedProperties = new ArrayList<ObservedProperty>();
			List<UnitOfMeasurement> unitOfMeasurements = new ArrayList<UnitOfMeasurement>();
			List<SensorName> sensorNames = new ArrayList<SensorName>();
			
			for (Integer i: columnStore.keySet()) {
				List<String> column = columnStore.get(i);
				if (column.get(0).equals("Measured Value")) {
					MeasuredValueColumn mvc = new MeasuredValueColumn(column.get(1));
					mvc.setColumnNumber(i);
					measuredValues.add(mvc);
				} else if (column.get(0).equals("Date & Time")) {
					if (column.get(1).equals("Combination")) {
						String pattern = column.get(1);
			        	if (pattern.indexOf("y") != -1);
			        	if (pattern.indexOf("M") != -1 || pattern.indexOf("w") != -1 || pattern.indexOf("D") != -1);
			        	if (pattern.indexOf("d") != -1);
			        	if (pattern.indexOf("H") != -1 || pattern.indexOf("k") != -1);
			        	if (pattern.indexOf("K") != -1 || pattern.indexOf("h") != -1 && pattern.indexOf("a") != -1); //am/pm times
			        	if (pattern.indexOf("m") != -1);
			        	if (pattern.indexOf("s") != -1);
			        	if (pattern.indexOf("Z") != -1 || pattern.indexOf("z") != -1);
					}
		        		
					//DateAndTimeColumn dtc = new DateAndTimeColumn(i, )
				} else if (column.get(0).equals("Feature Of Interest")) {
					FeatureOfInterest foi = new FeatureOfInterest();
					foi.setColumnNumber(i);
					featuresOfInterest.add(foi);
				} else if (column.get(0).equals("Observed Property")) {
					ObservedProperty op = new ObservedProperty();
					op.setColumnNumber(i);
					observedProperties.add(op);
				} else if (column.get(0).equals("Unit of Measurement")) {
					UnitOfMeasurement uom = new UnitOfMeasurement();
					uom.setColumnNumber(i);
					unitOfMeasurements.add(uom);
				} else if (column.get(0).equals("Sensor Name")) {
					SensorName sm = new SensorName();
					sm.setColumnNumber(i);
					sensorNames.add(sm);
				}
			}
			
			if (featuresOfInterest.isEmpty())
				//while there are measurement columns or rows without any fois do:
				getMainFrame().setStepPanel(new Step5aPanel(getMainFrame(), Step5aPanel.FEATURE_OF_INTEREST));	
			
			//else if 1:1 mapping
			
			else { //featuresOfInterest.isNotEmpty()
				//for each foi columns or row choose measurement column:
				//getMainFrame().setStepPanel(new Step4aPanel(getMainFrame(), Step4aPanel.FEATURE_OF_INTEREST));
				
				//while there are measurement columns or rows without any fois do:
				getMainFrame().setStepPanel(new Step5aPanel(getMainFrame(), Step5aPanel.FEATURE_OF_INTEREST));				
			}
			
			// TODO Auto-generated method stub
			break;
		}	
	}	
	
	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}
	
	private class RadioButtonPanel extends ButtonGroupPanel {
		
		private static final long serialVersionUID = 1L;
		
		public RadioButtonPanel(MainFrame mainFrame) {	
			super(mainFrame, rootPanel);
			addRadioButton("Undefined");
			addRadioButton("Measured Value", new MeasuredValuePanel(mainFrame, additionalPanel1));
			addRadioButton("Date & Time", new DateAndTimePanel(mainFrame, additionalPanel1));
			addRadioButton("Position", new PositionPanel(mainFrame, additionalPanel1));
			addRadioButton("Feature of Interest");
			addRadioButton("Sensor Name");
			addRadioButton("Observed Property");
			addRadioButton("Unit of Measurement");
			addRadioButton("Combination");
			addRadioButton("Do not export");				
		}

		private class MeasuredValuePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public MeasuredValuePanel(MainFrame mainFrame, JPanel containerPanel) {	
				super(mainFrame, containerPanel);		
				addRadioButton("Numeric Value" , new NumericValuePanel(mainFrame, additionalPanel2));
				addRadioButton("Count", new ParseTestPanel(mainFrame, additionalPanel2, new CountParser()));
				addRadioButton("Boolean", new ParseTestPanel(mainFrame, additionalPanel2, new BooleanParser()));
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
			
				public NumericValuePanel(MainFrame mainFrame, JPanel containerPanel) {
					super(mainFrame, containerPanel);
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
					//reformat();
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
					parseTestLabel.parseValues(tablePanel.getSelectedValues());
					//reformat();
				};
				
				@Override
				protected void reinit() {
					parseTestLabel.parseValues(tablePanel.getSelectedValues());
					//reformat();
				}
				
				private void reformat() {
					String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
					String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
					
					DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
					unusualSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
					unusualSymbols.setGroupingSeparator(thousandsSeparator.charAt(0));
					
					try {
						DecimalFormat weirdFormatter = new DecimalFormat("###,###.###", unusualSymbols);
						
						String n = weirdFormatter.format("123.456,78");
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
							
						s = s.replace(thousandsSeparator, "");
						s = s.replace(decimalSeparator, ".");
						return Double.parseDouble(s);
					}
				}			
			}	
			
			private class ParseTestPanel extends SelectionPanel {

				private static final long serialVersionUID = 1L;
				
				private final ParseTestLabel parseTestLabel;
				
				public ParseTestPanel(MainFrame mainFrame, JPanel containerPanel, Parser parser) {
					super(mainFrame, containerPanel);
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
					parseTestLabel.parseValues(tablePanel.getSelectedValues());
				}		
			}		
		}
		
		private class DateAndTimePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public DateAndTimePanel(MainFrame mainFrame, JPanel containerPanel) {
				super(mainFrame, containerPanel);
				addRadioButton("Combination", new DateAndTimeCombinationPanel(mainFrame, additionalPanel2));
				addRadioButton("Timezone", new TimeZonePanel(mainFrame, additionalPanel2));
				addRadioButton("UNIX time");
			}

			private class TimeZonePanel extends ButtonGroupPanel {

				private static final long serialVersionUID = 1L;
				
				public TimeZonePanel(MainFrame mainFrame, JPanel containerPanel) {
					super(mainFrame, containerPanel);	
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
				
				public DateAndTimeCombinationPanel(MainFrame mainFrame, JPanel containerPanel) {	
					super(mainFrame, containerPanel);	
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
			    	parseTestLabel.parseValues(tablePanel.getSelectedValues());
			    }
			    
			    @Override
			    protected void reinit() {
			    	reformat();
			    	parseTestLabel.parseValues(tablePanel.getSelectedValues());
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
			
			public PositionPanel(MainFrame mainFrame, JPanel containerPanel) {
				super(mainFrame, containerPanel);	
				addRadioButton("Longitude / X");
				addRadioButton("Latitude / Y");
				addRadioButton("Altitude / Z");
				addRadioButton("Reference System", new ReferenceSystemPanel(mainFrame, additionalPanel2));
				addRadioButton("Combination");
			}
			
			private class ReferenceSystemPanel extends ButtonGroupPanel {

				private static final long serialVersionUID = 1L;
				
				public ReferenceSystemPanel(MainFrame mainFrame, JPanel containerPanel) {
					super(mainFrame, containerPanel);				
					addRadioButton("Name");
					addRadioButton("EPSG-Code");
				}
			}	
		}	
	}	
	
	private class TableSelectionChanged implements TableSelectionListener {
		
		@Override
		public void columnSelectionChanged(int oldColumn, int newColumn) {
			List<String> selections = new ArrayList<String>();
			radioButtonPanel.store(selections);
			columnStore.put(oldColumn, selections);
			    
			selections = columnStore.get(newColumn);
			additionalPanel1.removeAll();
			additionalPanel2.removeAll();
			
			if (selections == null) radioButtonPanel.restoreDefault();
			else radioButtonPanel.restore(selections);
			
			getMainFrame().pack();
		}
		
		@Override
		public void rowSelectionChanged(int oldRow, int newRow) {
			List<String> selections = new ArrayList<String>();
			radioButtonPanel.store(selections);
			rowStore.put(oldRow, selections);
			
			selections = rowStore.get(newRow);
			additionalPanel1.removeAll();
			additionalPanel2.removeAll();
			
			if (selections == null) radioButtonPanel.restoreDefault();
			else radioButtonPanel.restore(selections);
			
			getMainFrame().pack();
		}
	}
}
