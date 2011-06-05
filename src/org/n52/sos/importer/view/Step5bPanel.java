package org.n52.sos.importer.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Step5bPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel descriptionLabel = new JLabel("Complete time information " +
														"for the marked components.");

	private final JPanel containerPanel = new JPanel();
	
	private final TablePanel tablePanel = TablePanel.getInstance();
	
	public Step5bPanel() {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);
		this.add(descriptionPanel);
		
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		this.add(containerPanel);

		this.add(tablePanel);
	}
	
	public void addMissingComponents(List<JPanel> missingComponents) {
		for (JPanel p: missingComponents) {
			containerPanel.add(p);
		}
	}
}
