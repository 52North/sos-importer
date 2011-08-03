package org.n52.sos.importer.model.tooltips;

import javax.swing.JLabel;

public class ToolTipLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public ToolTipLabel(String caption, String toolTip) {
		super();
		this.setText("<html><span style='BORDER-BOTTOM: #FF7F50 dotted'>" + 
				caption + "</span></html>");
		this.setToolTipText(toolTip);
	}
}
