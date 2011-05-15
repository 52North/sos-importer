package org.n52.sos.importer;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class Step3Panel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel selectionModeLabel = new JLabel("Selection mode: ");
	private final String[] selectionModeValues = { "Columns", "Rows", "Cells" };
	private final JComboBox selectionModeComboBox = new JComboBox(selectionModeValues);
	
	private final JRadioButton undefinedButton = new JRadioButton("Undefined");
	private final JRadioButton measuredValueButton = new JRadioButton("Measured Value");
	private final JRadioButton dateAndTimeButton = new JRadioButton("Date & Time");
	private final JRadioButton positionButton = new JRadioButton("Position");
	private final JRadioButton featureOfInterestButton = new JRadioButton("Feature of Interest");
	private final JRadioButton sensorNameButton = new JRadioButton("Sensor Name");
	private final JRadioButton observedPropertyButton = new JRadioButton("Observed Property");
	private final JRadioButton unitOfMeasurementButton = new JRadioButton("Unit of Measurement");
	private final JRadioButton combinationButton = new JRadioButton("Combination");
	private final JRadioButton doNotExportButton = new JRadioButton("Do not export");
	
	private final JPanel additionalPanel1 = new JPanel();
	private final JPanel additionalPanel2 = new JPanel();
	
	private final MeasuredValuePanel measuredValuePanel = new MeasuredValuePanel();
	private final DateAndTimePanel dateAndTimePanel = new DateAndTimePanel();
	private final PositionPanel positionPanel = new PositionPanel();
	
	private final JTable table;
	
	public Step3Panel(MainFrame mainFrame, Object[][] csvFileContent) {
		super(mainFrame);
		selectionModeComboBox.addActionListener(new SelectionModeChanged());
		
		JPanel selectionModelPanel = new JPanel();
		selectionModelPanel.setLayout(new FlowLayout());
		selectionModelPanel.add(selectionModeLabel);
		selectionModelPanel.add(selectionModeComboBox);
		this.add(selectionModelPanel);
		
		//initialize blank table headers
		int columns = csvFileContent[0].length;
		String[] columnHeaders = new String[columns];
		for (int i = 0; i < columns; i++) {
			columnHeaders[i] = "";
		}
		table = new JTable(csvFileContent, columnHeaders) {  
			private static final long serialVersionUID = 1L;
			//turn editing of cells off
			public boolean isCellEditable(int row, int col) {  
                return false;  
            }  
        };
        setSelectionMode("Columns");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//ListSelectionModel.MULTIPLE_INTERVAL_SELECTIONS
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		undefinedButton.setSelected(true);
		
		measuredValueButton.addActionListener(new MeasuredValueSelected());
		dateAndTimeButton.addActionListener(new DateAndTimeSelected());
		positionButton.addActionListener(new PositionSelected());
		
		RemoveAdditionalPanels removeAdditionalPanels = new RemoveAdditionalPanels();
		undefinedButton.addActionListener(removeAdditionalPanels);
		featureOfInterestButton.addActionListener(removeAdditionalPanels);
		sensorNameButton.addActionListener(removeAdditionalPanels);
		observedPropertyButton.addActionListener(removeAdditionalPanels);
		unitOfMeasurementButton.addActionListener(removeAdditionalPanels);
		combinationButton.addActionListener(removeAdditionalPanels);
		doNotExportButton.addActionListener(removeAdditionalPanels);
		
		ButtonGroup group = new ButtonGroup();
		group.add(undefinedButton);
	    group.add(measuredValueButton);
	    group.add(dateAndTimeButton);
	    group.add(positionButton);
	    group.add(featureOfInterestButton);
	    group.add(sensorNameButton);
	    group.add(observedPropertyButton);
	    group.add(unitOfMeasurementButton);
	    group.add(combinationButton);
	    group.add(doNotExportButton);
	    
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(undefinedButton);
        radioPanel.add(measuredValueButton);
        radioPanel.add(dateAndTimeButton);
        radioPanel.add(positionButton);
        radioPanel.add(featureOfInterestButton);
        radioPanel.add(sensorNameButton);
        radioPanel.add(observedPropertyButton);
        radioPanel.add(unitOfMeasurementButton);
	    radioPanel.add(combinationButton);
        radioPanel.add(doNotExportButton);
        
	    this.add(radioPanel);
	    this.add(additionalPanel1);
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
	
	public void changeAddtionalPanels(JPanel p1, JPanel p2) {
		additionalPanel1.removeAll();
		additionalPanel2.removeAll();
		if (p1 != null) additionalPanel1.add(p1);
		if (p2 != null) additionalPanel1.add(p2);
		getMainFrame().pack();
	}
	
	public void changeAdditionalPanel2(JPanel p2) {
		additionalPanel2.removeAll();
		if (p2 != null) additionalPanel1.add(p2);
		getMainFrame().pack();
	}
	
	private class RemoveAdditionalPanels implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeAddtionalPanels(null, null);
		}		
	}

	
	private class MeasuredValueSelected implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeAddtionalPanels(measuredValuePanel, null);
		}
	}
	
	private class DateAndTimeSelected implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changeAddtionalPanels(dateAndTimePanel, null);
		}
	}
	
	private class PositionSelected implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changeAddtionalPanels(positionPanel, null);
		}
	}

	private class MeasuredValuePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private final JRadioButton numericValueButton = new JRadioButton("Numeric Value");
		private final JRadioButton countButton = new JRadioButton("Count");
		private final JRadioButton textButton = new JRadioButton("Text");
		private final JRadioButton booleanButton = new JRadioButton("Boolean");	
		
		public MeasuredValuePanel() {
			ButtonGroup group = new ButtonGroup();
		    group.add(numericValueButton);
		    group.add(countButton);
		    group.add(textButton);
		    group.add(booleanButton);
			
	        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
	        radioPanel.add(numericValueButton);
	        radioPanel.add(countButton);
	        radioPanel.add(textButton);
	        radioPanel.add(booleanButton);
	        add(radioPanel);
		}
	}
	
	private class DateAndTimePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private final JRadioButton combinationButton = new JRadioButton("Combination");
		private final JRadioButton dateButton = new JRadioButton("Date");
		private final JRadioButton timeButton = new JRadioButton("Time");
		private final JRadioButton timezoneButton = new JRadioButton("Timezone");
		private final JRadioButton unixButton = new JRadioButton("UNIX time");	
		
		private final DateAndTimeCombinationPanel dateAndTimeCombinationPanel = new DateAndTimeCombinationPanel();
		
		public DateAndTimePanel() {
			super();
			ButtonGroup group = new ButtonGroup();
		    group.add(combinationButton);
		    group.add(dateButton);
		    group.add(timeButton);
		    group.add(timezoneButton);
		    group.add(unixButton);
			
	        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
	        radioPanel.add(combinationButton);
	        radioPanel.add(dateButton);
	        radioPanel.add(timeButton);
	        radioPanel.add(timezoneButton);
	        radioPanel.add(unixButton);
	        this.add(radioPanel);
	        
	        combinationButton.addActionListener(new DateAndTimeCombinationSelected());
		}
		
		private class DateAndTimeCombinationSelected implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeAdditionalPanel2(dateAndTimeCombinationPanel);
			}		
		}
		
		private class DateAndTimeCombinationPanel extends JPanel {

			private static final long serialVersionUID = 1L;
			
			private final JLabel formatLabel = new JLabel("Format: ");
	        String[] patternExamples = {
	                 "yyyy.MM.dd G 'at' hh:mm:ss z",
	                 "yyyy.MMMMM.dd GGG hh:mm aaa"
	                 };
			
			private final JComboBox dateAndTimeFormatComboBox = new JComboBox(patternExamples);
			private final JButton removeButton = new JButton("remove");
			
			public DateAndTimeCombinationPanel() {	
				super();
				dateAndTimeFormatComboBox.setEditable(true);
				
				removeButton.setBorderPainted(false);
				removeButton.setContentAreaFilled(false);
				removeButton.addActionListener(new RemoveButtonClicked());
				
				this.setLayout(new FlowLayout(FlowLayout.LEFT));
				this.add(formatLabel);
				this.add(dateAndTimeFormatComboBox);
				this.add(removeButton);
			}
			
			private class RemoveButtonClicked implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					int index = dateAndTimeFormatComboBox.getSelectedIndex();
					dateAndTimeFormatComboBox.removeItemAt(index);
				}		
			}
		}
	}

	private class PositionPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private final JRadioButton longitudeButton = new JRadioButton("Longitude / X");
		private final JRadioButton latitudeButton = new JRadioButton("Latitude / Y");
		private final JRadioButton altitudeButton = new JRadioButton("Altitude / Z");
		private final JRadioButton referenceSystemButton = new JRadioButton("Reference System");
		private final JRadioButton combinationButton = new JRadioButton("Combination");	
		
		private final ReferenceSystemPanel referenceSystemPanel = new ReferenceSystemPanel();
		
		public PositionPanel() {
			super();
			
			referenceSystemButton.addActionListener(new ReferenceSystemSelected());
			
			ButtonGroup group = new ButtonGroup();
		    group.add(longitudeButton);
		    group.add(latitudeButton);
		    group.add(altitudeButton);
		    group.add(referenceSystemButton);
		    group.add(combinationButton);
			
	        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
	        radioPanel.add(longitudeButton);
	        radioPanel.add(latitudeButton);
	        radioPanel.add(altitudeButton);
	        radioPanel.add(referenceSystemButton);
	        radioPanel.add(combinationButton);
	        this.add(radioPanel);
		}
		
		private class ReferenceSystemPanel extends JPanel {

			private static final long serialVersionUID = 1L;
			private final JRadioButton referenceSystemNameButton = new JRadioButton("Name");
			private final JRadioButton referenceSystemEPSGCodeButton = new JRadioButton("EPSG-Code");
			
			public ReferenceSystemPanel() {
				super();
				
				ButtonGroup group = new ButtonGroup();
			    group.add(referenceSystemNameButton);
			    group.add(referenceSystemEPSGCodeButton);
			    
		        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		        radioPanel.add(referenceSystemNameButton);
		        radioPanel.add(referenceSystemEPSGCodeButton);
		        this.add(radioPanel);
			}
		}
		
		private class ReferenceSystemSelected implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeAdditionalPanel2(referenceSystemPanel);			
			}		
		}		
	}
	
	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}
}
