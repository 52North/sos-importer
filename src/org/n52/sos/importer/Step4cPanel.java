package org.n52.sos.importer;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


public class Step4cPanel extends StepPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int FEATURE_OF_INTEREST = 0;
	private static final int OBSERVED_PROPERTY = 1;
	private static final int UNIT_OF_MEASUREMENT = 2;
	private static final int SENSOR_NAME = 3;
	
	private final String[] questions = {
			"Which feature of interest has been observed?"};
	
	private final String[] markings = {
			"Mark all measurement columns where this feature corresponds to:"};
	
	private final JLabel questionLabel;
	private final JLabel nameLabel = new JLabel("Name: ");
	private final JComboBox nameComboBox = new JComboBox();
	private final JButton removeButton = new JButton("remove");
	private final JLabel URILabel = new JLabel("URI: ");
	private final JTextField URITextField = new JTextField(30);	
	private final JLabel markingLabel;
	
	private final JTable table;
	
	public Step4cPanel(MainFrame mainFrame, int type, Object[][] csvFileContent) {
		super(mainFrame);
		questionLabel = new JLabel(questions[type]);
		
		nameComboBox.setEditable(true);
		
		removeButton.setBorderPainted(false);
		removeButton.setContentAreaFilled(false);
		removeButton.addActionListener(new RemoveButtonClicked());
		
		markingLabel = new JLabel(markings[type]);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		questionPanel.add(questionLabel);
		this.add(questionPanel);
		
		JPanel missingElementsPanel = new JPanel();
		missingElementsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		missingElementsPanel.add(nameLabel);
		missingElementsPanel.add(nameComboBox);
		missingElementsPanel.add(removeButton);
		missingElementsPanel.add(URILabel);
		missingElementsPanel.add(URITextField);
		this.add(missingElementsPanel);
		
		JPanel markingPanel = new JPanel();
		markingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		markingPanel.add(markingLabel);
		this.add(markingPanel);
		
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
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);

		
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
		return "Step 5: Add missing Metadata";
	}

	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void next() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}
	
	private class RemoveButtonClicked implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub		
		}		
	}
}
