package org.n52.sos.importer.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.view.position.MissingComponentPanel;

public class Step6cPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel featureOfInterestLabel = new JLabel("Please choose the position " +
			"for the following feature of interest:");
	private final JTextField featureOfInterestTextField = new JTextField(10);
	
	private final JPanel containerPanel = new JPanel();
	
	public Step6cPanel() {
		super();
		featureOfInterestTextField.setEditable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel featureOfInterestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		featureOfInterestPanel.add(featureOfInterestLabel);
		featureOfInterestPanel.add(featureOfInterestTextField);
		this.add(featureOfInterestPanel);
		
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		this.add(containerPanel);
		
	}
	
	public void addMissingComponentPanels(List<MissingComponentPanel> missingComponentPanels) {
		for (MissingComponentPanel mcp: missingComponentPanels) {
			containerPanel.add(mcp);
		}
	}
	
	public void setFeatureOfInterestName(String name) {
		featureOfInterestTextField.setText(name);
	}
}
