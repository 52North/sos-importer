package org.n52.sos.importer.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.interfaces.MissingComponentPanel;

/**
 * consists of a customized instruction panel and 
 * a container panel for all missing components
 * (used for steps 6bspecial and 6c) 
 * @author Raimund
 *
 */
public class Step6Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel descriptionLabel1 = new JLabel();
	private final JLabel descriptionLabel2 = new JLabel();
	private final JLabel questionMarkLabel = new JLabel("?");
	private final JTextField featureOfInterestTextField = new JTextField();
	private final JTextField observedPropertyTextField = new JTextField();
	
	private final JPanel containerPanel = new JPanel();
	
	public Step6Panel(String description, String featureOfInterestName, 
			String observedPropertyName, List<MissingComponentPanel> missingComponentPanels) {
		super();
		descriptionLabel1.setText(description + " feature of interest ");
		featureOfInterestTextField.setText(" " + featureOfInterestName + " ");
		featureOfInterestTextField.setEditable(false);
		
		if (observedPropertyName != null) {
			descriptionLabel2.setText(" and observed property ");
			observedPropertyTextField.setText(" " + observedPropertyName + " ");
			observedPropertyTextField.setEditable(false);
		}
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel1);
		descriptionPanel.add(featureOfInterestTextField);
		if (observedPropertyName != null) {
			descriptionPanel.add(descriptionLabel2);
			descriptionPanel.add(observedPropertyTextField);
		}
		descriptionPanel.add(questionMarkLabel);
		this.add(descriptionPanel);
		
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		this.add(containerPanel);
		
		for (MissingComponentPanel mcp: missingComponentPanels) {
			containerPanel.add(mcp);
		}
	}
	
}
