package org.n52.sos.importer;
import java.awt.Color;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Step3Panel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel selectionModeLabel = new JLabel("Selection mode: ");
	
	private final SelectionPanel radioButtonPanel;
	private JPanel rootPanel = new JPanel();
	private JPanel additionalPanel1 = new JPanel();
	private JPanel additionalPanel2 = new JPanel();
	private TablePanel tablePanel;
	
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
			selectionModeLabel.setText("Columns");
			break;
		case TablePanel.ROWS:
			tablePanel.setSelectionMode(TablePanel.ROWS);
			selectionModeLabel.setText("Rows");
			break;
		case TablePanel.CELLS:
			tablePanel.setSelectionMode(TablePanel.CELLS);
			selectionModeLabel.setText("Cells");
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
			
			private final ParseTestPanel parseTestLabel = new ParseTestPanel();
			
			public MeasuredValuePanel(MainFrame mainFrame, JPanel containerPanel) {	
				super(mainFrame, containerPanel);		
				addRadioButton("Numeric Value");
				addRadioButton("Count");
				addRadioButton("Text");
				addRadioButton("Boolean");
				add(parseTestLabel);
			}

			@Override
			protected void selectionChanged() {
				
				String s = getSelection();
				if (s.equals("Numeric Value")) {
					parseTestLabel.parseValues();
				}
			}
			
			private class ParseTestPanel extends JLabel {

				private static final long serialVersionUID = 1L;
				
				public ParseTestPanel() {
					super();
				}
				
				public void parseValues() {
					int notParseableValues = 0;
					StringBuilder notParseable = new StringBuilder();
					notParseable.append("<html>");
					List<String> values = tablePanel.getSelectedValues();

					for (String value: values) {
						try {
							parse(value);
						} catch (Exception e) {
							notParseable.append(value + "<br>");
							notParseableValues++;
						}
					}
					
					this.setForeground(Color.blue);
					this.setText("<html><u>" + notParseableValues + " values not parseable</u></html>");
					
					notParseable.append("</html>");
					this.setToolTipText(notParseable.toString());
				}
				
				public void parse(String value) {
					Integer.parseInt(value);
				}				
			}
			
		}
		
		private class DateAndTimePanel extends ButtonGroupPanel {

			private static final long serialVersionUID = 1L;
			
			public DateAndTimePanel(MainFrame mainFrame, JPanel containerPanel) {
				super(mainFrame, containerPanel);
				addRadioButton("Combination", new DateAndTimeCombinationPanel(mainFrame, additionalPanel2));
				//addRadioButton("Date", new DatePanel());
				//addRadioButton("Time", new TimePanel());
				addRadioButton("Timezone", new TimeZonePanel(mainFrame, additionalPanel2));
				addRadioButton("UNIX time");
			}
			
			/*
			private class DatePanel extends ComboBoxPanel {

				private static final long serialVersionUID = 1L;
			
				private String format = "Format: ";
				
		        private String[] patternExamples = {
		                "dd MMMMM yyyy",
		                "dd.MM.yy",
		                "MM/dd/yy",
		                "EEE, MMM d, ''yy",
		                 };
				
				public DatePanel() {	
					super();
					setLabelText(format);
					setComboBoxObjects(patternExamples);
				}
				
				@Override
				protected JPanel getChildContainerPanel() {
					return null;
				}
			}
			
			
			private class TimePanel extends ComboBoxPanel {

				private static final long serialVersionUID = 1L;
				
				private String format = "Format: ";
				
		        String[] patternExamples = {
		        		"h:mm a",
		                "H:mm:ss:SSS",
		                "K:mm a,z",
		                 };
				
				public TimePanel() {	
					super();
					setLabelText(format);
					setComboBoxObjects(patternExamples);
				}

				@Override
				protected JPanel getChildContainerPanel() {
					return null;
				}
			}
			*/
			private class TimeZonePanel extends ButtonGroupPanel {

				private static final long serialVersionUID = 1L;
				
				public TimeZonePanel(MainFrame mainFrame, JPanel containerPanel) {
					super(mainFrame, containerPanel);	
					super.addRadioButton("Country or City Name");
					super.addRadioButton("UTC Offset");		       
				}
			}
			
			private class DateAndTimeCombinationPanel extends ComboBoxPanel {
				//source: 	http://download.oracle.com/javase/tutorial/uiswing/
				// 			examples/components/ComboBoxDemo2Project/src/components/
				// 			ComboBoxDemo2.java
				private static final long serialVersionUID = 1L;

				private String format = "Format: ";
				
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
		        
		        private JLabel exampleDateTime = new JLabel("");
				
				public DateAndTimeCombinationPanel(MainFrame mainFrame, JPanel containerPanel) {	
					super(mainFrame, containerPanel);	
					setLabelText(format);
					setComboBoxObjects(patternExamples);
					this.add(exampleDateTime);
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
			            exampleDateTime.setText("e.g. " + dateString);
			        } catch (IllegalArgumentException iae) {
			        	exampleDateTime.setForeground(Color.red);
			        	exampleDateTime.setText("Error: " + iae.getMessage());
			        }
			    }
			    
			    @Override
			    protected void selectionChanged() {
			    	reformat();
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
