package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.EditableJComboBoxPanel;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class DateAndTimeCombinationPanel extends SelectionPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;

	private final JLabel groupLabel = new JLabel("Group: ");
	
	private final JComboBox groupComboBox = new JComboBox(Settings.getInstance().getDateAndTimeGroups());
	
	private final DateAndTime dateAndTime = new DateAndTime();
	
    private final JLabel exampleLabel = new JLabel("Example: ");

    private final DefaultComboBoxModel dateAndTimePatterns = EditableComboBoxItems.getInstance().getDateAndTimePatterns();
    private final EditableJComboBoxPanel dateAndTimeComboBox = new EditableJComboBoxPanel(dateAndTimePatterns, "Format");
    
    private final ParseTestLabel parseTestLabel = new ParseTestLabel(dateAndTime);
    private final ExampleFormatLabel exampleFormatLabel = new ExampleFormatLabel(dateAndTime);
	
	public DateAndTimeCombinationPanel(JPanel containerPanel) {	
		super(containerPanel);
		setDefaultSelection();
		dateAndTimeComboBox.addActionListener(new FormatChanged());
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		formatPanel.add(dateAndTimeComboBox);
							
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupPanel.add(groupLabel);
		groupPanel.add(groupComboBox);
		
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
		dateAndTimeComboBox.setSelectedItem((String)dateAndTimePatterns.getElementAt(0));
		dateAndTime.setPattern(getSelection());
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
