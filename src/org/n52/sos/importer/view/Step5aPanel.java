package org.n52.sos.importer.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.view.position.MissingComponentPanel;

public class Step5aPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public Step5aPanel(String description, List<MissingComponentPanel> missingComponentPanels) {
		super();
		JLabel descriptionLabel = new JLabel(description);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);
		this.add(descriptionPanel);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		
		for (MissingComponentPanel mcp: missingComponentPanels)
			containerPanel.add(mcp);
		
		this.add(containerPanel);
		
		TablePanel tablePanel = TablePanel.getInstance();
		this.add(tablePanel);
	}
}
