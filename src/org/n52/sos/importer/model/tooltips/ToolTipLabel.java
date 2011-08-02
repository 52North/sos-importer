package org.n52.sos.importer.model.tooltips;

import javax.swing.JLabel;
import javax.swing.ToolTipManager;

public class ToolTipLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public ToolTipLabel(String caption, String toolTip) {
		super();
		this.setText("<html><u>" + caption + "</u></html>");
		this.setToolTipText(toolTip);
		ToolTipManager.sharedInstance().setInitialDelay(250);
		ToolTipManager.sharedInstance().setDismissDelay(50000);
	}
}
