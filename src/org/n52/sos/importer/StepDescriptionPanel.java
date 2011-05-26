package org.n52.sos.importer;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StepDescriptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel stepDescriptionLabel = new JLabel();
	
	public StepDescriptionPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(stepDescriptionLabel);
	}
	
	public void setText(String stepDescription) {
		stepDescriptionLabel.setText(stepDescription);
	}
}
