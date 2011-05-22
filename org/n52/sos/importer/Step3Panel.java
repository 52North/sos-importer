package org.n52.sos.importer;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Step3Panel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel selectionModeLabel = new JLabel("Selection mode: ");
	private final String[] selectionModeValues = { "Columns", "Rows", "Cells" };
	private final JComboBox selectionModeComboBox = new JComboBox(selectionModeValues);
	
	private JTable table = new JTable();
	
	private JPanel rootPanel = new JPanel();
	private final SelectionPanel radioButtonPanel;
	private JPanel additionalPanel1 = new JPanel();
	private JPanel additionalPanel2 = new JPanel();
	private JPanel tablePanel = new JPanel();
	
	private final HashMap<Integer, List<String>> columnStore = new HashMap<Integer, List<String>>();
	private final HashMap<Integer, List<String>> rowStore = new HashMap<Integer, List<String>>();
	
	public Step3Panel(MainFrame mainFrame) {
		super(mainFrame);
		radioButtonPanel = new RadioButtonPanel(mainFrame);
		selectionModeComboBox.addActionListener(new SelectionModeChanged());
		
		JPanel selectionModelPanel = new JPanel();
		selectionModelPanel.setLayout(new FlowLayout());
		selectionModelPanel.add(selectionModeLabel);
		selectionModelPanel.add(selectionModeComboBox);
		this.add(selectionModelPanel);
		
	    this.add(rootPanel);
	    this.add(additionalPanel1);
	    this.add(additionalPanel2);
	    this.add(tablePanel);
	}
	
	public void setTableContent(Object[][] content) {
		//initialize blank table headers
		int columns = content[0].length;
		String[] columnHeaders = new String[columns];
		for (int i = 0; i < columns; i++) {
			columnHeaders[i] = "";
		}
		table = new JTable(content, columnHeaders) {  
			private static final long serialVersionUID = 1L;
			//turn editing of cells off
			public boolean isCellEditable(int row, int col) {  
                return false;  
            }  
        };
        
        setSelectionMode("Columns");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//select first column
		table.setColumnSelectionInterval(0, 0);
		
		SelectionListener listener = new SelectionListener(table);
		table.getSelectionModel().addListSelectionListener(listener);
		table.getColumnModel().getSelectionModel()
		    .addListSelectionListener(listener);
		
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.removeAll();
		tablePanel.add(scrollPane);	
		getMainFrame().pack();
	}
	
	private void setSelectionMode(String sm) {
		if (sm.equals("Columns")) {
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(false);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(false);
		} else if (sm.equals("Rows")) {
			table.setColumnSelectionAllowed(false);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(true);
		} else { //Cells
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(true);
		}
	}
	
	public List<String> getSelectedValues() {
		//column selection
		ArrayList<String> values = new ArrayList<String>();
		int column = table.getSelectedColumn();
		int rows = table.getRowCount();
		for (int i = 0; i < rows; i++)
			values.add((String)table.getValueAt(i, column));
		return values;
	}

	@Override
	protected String getDescription() {
		return "Step 3: Choose Metadata";
	}

	@Override
	protected void back() {
		getMainFrame().setStepPanel(getMainFrame().getStep2Panel());
		
	}

	@Override
	protected void next() {
		// TODO Auto-generated method stub
		
	}
	
	private class SelectionModeChanged implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        String sm = (String)selectionModeComboBox.getSelectedItem();
	        setSelectionMode(sm);
	    }
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
					List<String> values = getSelectedValues();

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
	
	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	public class SelectionListener implements ListSelectionListener {
	    JTable table;
	    int columnSelection;
	    // It is necessary to keep the table since it is not possible
	    // to determine the table from the event's source
	    SelectionListener(JTable table) {
	        this.table = table;
	    }
	    public void valueChanged(ListSelectionEvent e) {
	        // If cell selection is enabled, both row and column change events are fired
            // Row selection changed
	        if (e.getSource() == table.getSelectionModel()
	              && table.getRowSelectionAllowed()) {
	            // Column selection changed
	            
	            System.out.println(table.getSelectedRow());
	            
	        } else if (e.getSource() == table.getColumnModel().getSelectionModel()
	               && table.getColumnSelectionAllowed() ){
	            List<String> selections = new ArrayList<String>();
	            radioButtonPanel.store(selections);
	            columnStore.put(columnSelection, selections);
	            
	            columnSelection = table.getSelectedColumn();
	            
	            selections = columnStore.get(columnSelection);
	            additionalPanel1.removeAll();
	            additionalPanel2.removeAll();

	            if (selections == null) {
	            	radioButtonPanel.setDefaultSelection();
	            } else radioButtonPanel.restore(selections);
	            getMainFrame().pack();
	        }

	        if (e.getValueIsAdjusting()) {
	            // The mouse button has not yet been released
	        }
	    }
	}
}
