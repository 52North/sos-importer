package org.n52.sos.importer.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Step6bPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel questionLabel = new JLabel();
	private final JLabel nameLabel = new JLabel("Name: ");
	private final JComboBox nameComboBox = new JComboBox();
	private final JButton removeButton = new JButton("remove");
	private final JLabel URILabel = new JLabel("URI: ");
	private final JTextField URITextField = new JTextField(30);	
	private final JLabel markingLabel = new JLabel();
	private final TablePanel tablePanel = TablePanel.getInstance();
	
	public Step6bPanel(String questionText, String markingText) {
		super();
		questionLabel.setText(questionText);
		markingLabel.setText(markingText);
		nameComboBox.setEditable(true);
		
		removeButton.setBorderPainted(false);
		removeButton.setContentAreaFilled(false);
		removeButton.addActionListener(new RemoveButtonClicked());
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		questionPanel.add(questionLabel);
		this.add(questionPanel);
		
		JPanel missingResourcePanel = new JPanel();
		missingResourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		missingResourcePanel.add(nameLabel);
		missingResourcePanel.add(nameComboBox);
		missingResourcePanel.add(removeButton);
		missingResourcePanel.add(URILabel);
		missingResourcePanel.add(URITextField);
		this.add(missingResourcePanel);
		
		JPanel markingPanel = new JPanel();
		markingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		markingPanel.add(markingLabel);
		this.add(markingPanel);
		
		this.add(tablePanel);	
	}
	
	public String getResourceName() {
		return (String) nameComboBox.getSelectedItem();
	}
	
	public String getResourceURI() {
		return URITextField.getText();
	}
	
	public void setResourceName(String name) {
		nameComboBox.setSelectedItem(name);
	}
	
	public void setResourceURI(String uri) {
		URITextField.setText(uri);
	}

	
	private class RemoveButtonClicked implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub		
		}		
	}

}
