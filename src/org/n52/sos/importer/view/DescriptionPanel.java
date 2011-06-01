package org.n52.sos.importer.view;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DescriptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel descriptionLabel = new JLabel();
	
	public DescriptionPanel() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(descriptionLabel);
	}
	
	public void setText(String stepDescription) {
		descriptionLabel.setText(stepDescription);
	}
}
