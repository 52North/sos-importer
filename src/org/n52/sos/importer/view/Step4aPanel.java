package org.n52.sos.importer.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Step4aPanel extends JPanel {

	private static final long serialVersionUID = 1L;
		
	private final JLabel markingLabel = new JLabel();
	
	private final JPanel tablePanel = TablePanel.getInstance();
	
	public Step4aPanel(String text) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		markingLabel.setText(text);
		//markingLabel.setBackground(markingColor);
		//markingLabel.setOpaque(true);
		this.add(markingLabel);
		
		this.add(tablePanel);
	}
}
