package org.n52.sos.importer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ComboBoxPanel extends SelectionPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private final JLabel label = new JLabel();
	private final JComboBox comboBox = new JComboBox();
	
	public ComboBoxPanel(MainFrame mainFrame, JPanel containerPanel) {
		super(mainFrame, containerPanel);
		comboBox.setEditable(true);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(label);
		this.add(comboBox);
		comboBox.addActionListener(this);
	}
	
	public void setLabelText(String text) {
		label.setText(text);
	}
	
	public void setComboBoxObjects(Object[] objects) {
		for (Object o: objects)
			comboBox.addItem(o);
	}
	
	public void setSelection(String s) {
		comboBox.setSelectedItem(s);
	}
	
	@Override
	public void setDefaultSelection() {
		comboBox.setSelectedIndex(0);
	}
	
	
	public String getSelection() {
		return (String)comboBox.getSelectedItem();
	}
	
	public void actionPerformed(ActionEvent e) {
		//TODO remove and add selection to the list when enter pressed
        //String newSelection = getSelection();
        selectionChanged();
    }
}
