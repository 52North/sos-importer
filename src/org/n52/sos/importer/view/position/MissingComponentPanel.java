package org.n52.sos.importer.view.position;

import javax.swing.JPanel;

public abstract class MissingComponentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract void assignValues();
	
	public abstract void unassignValues();
	
	public boolean checkValues() {
		return true;
	}
}
