package org.n52.sos.importer.view;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Step5bPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private final JLabel questionLabel = new JLabel();
	
	private final JLabel nameLabel = new JLabel();
	
	private final JLabel uriLabel = new JLabel();
	
	private final JTextField nameTextField = new JTextField(20);
	
	private final JTextField uriTextField = new JTextField(20);
	
	public Step5bPanel(List<String> names) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(questionLabel);
		
		JPanel nameURIPanel = new JPanel();
		nameURIPanel.setLayout(new GridLayout(names.size(), 4));
		for (String name: names) {
			nameURIPanel.add(nameLabel);
			nameURIPanel.add(nameTextField);
			nameTextField.setText(name);
			nameTextField.setEditable(false);
			nameURIPanel.add(uriLabel);
			nameURIPanel.add(uriTextField);
		}
	}
}
