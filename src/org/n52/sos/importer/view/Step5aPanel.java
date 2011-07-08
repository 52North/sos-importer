package org.n52.sos.importer.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.view.dateAndTime.MissingDateAndTimePanel;

public class Step5aPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Color markingColor = Color.green;
	
	private final JLabel descriptionLabel = new JLabel("Complete missing information " +
														"for the marked date and time.");

	private final JPanel containerPanel = new JPanel();
	
	private final TablePanel tablePanel = TablePanel.getInstance();
	
	public Step5aPanel() {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionLabel);
		this.add(descriptionPanel);
		
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
		this.add(containerPanel);

		this.add(tablePanel);
	}
	
	public void addMissingComponentPanels(List<MissingDateAndTimePanel> missingComponentPanels) {
		for (MissingDateAndTimePanel mcp: missingComponentPanels) {
			containerPanel.add(mcp);
		}
	}

	public Color getMarkingColor() {
		return markingColor;
	}
}
