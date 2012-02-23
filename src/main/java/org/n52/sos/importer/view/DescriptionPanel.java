package org.n52.sos.importer.view;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * description label at the top of the main frame
 * @author Raimund
 *
 */
public class DescriptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static DescriptionPanel instance = null;
	
	private final JLabel descriptionLabel = new JLabel();

	private DescriptionPanel() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(descriptionLabel);
	}

	public static DescriptionPanel getInstance() {
		if (instance == null)
			instance = new DescriptionPanel();
		return instance;
	}	
	
	public void setText(String stepDescription) {
		descriptionLabel.setText(stepDescription);
	}
}
