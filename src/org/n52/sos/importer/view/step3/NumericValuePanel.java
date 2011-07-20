package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.measuredValue.NumericValue;

public class NumericValuePanel extends SelectionPanel {

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

	public NumericValuePanel(JPanel containerPanel) {
		super(containerPanel);
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
		if (separators[1].equals(" ")) thousandsSeparatorCombobox.setSelectedItem("Space");
		else thousandsSeparatorCombobox.setSelectedItem(separators[1]);
		selectionChanged();
	}

	@Override
	protected String getSelection() {
		String decimalSeparator = (String) decimalSeparatorCombobox.getSelectedItem();
		String thousandsSeparator = (String) thousandsSeparatorCombobox.getSelectedItem();
		if (thousandsSeparator.equals("Space")) thousandsSeparator = " ";
		return decimalSeparator+":"+thousandsSeparator;
	}

	@Override
	public void setDefaultSelection() {
		decimalSeparatorCombobox.setSelectedItem(decimalSeparators[0]);
		thousandsSeparatorCombobox.setSelectedItem(thousandsSeparators[0]);
		numericValue.setDecimalSeparator(decimalSeparators[0]);
		numericValue.setThousandsSeparator(thousandsSeparators[0]);
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
			else if (thousandsSeparator.equals(".") && decimalSeparator.equals("."))
				thousandsSeparatorCombobox.setSelectedItem(",");
			else 
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
			else if (thousandsSeparator.equals(".") && decimalSeparator.equals("."))
				decimalSeparatorCombobox.setSelectedItem(",");
			else				
				selectionChanged();
		}		
	}
	
}
